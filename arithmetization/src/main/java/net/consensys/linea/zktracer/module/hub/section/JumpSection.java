/*
 * Copyright Consensys Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package net.consensys.linea.zktracer.module.hub.section;

import static net.consensys.linea.zktracer.module.hub.AccountSnapshot.*;

import com.google.common.base.Preconditions;
import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.opcodes.JumpOobCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.opcodes.JumpiOobCall;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.module.hub.transients.DeploymentInfo;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.datatypes.Address;

public class JumpSection extends TraceSection {

  public JumpSection(Hub hub) {
    // 5 = 1 + 4
    // There are up to 4 NSR's
    super(hub, (short) 5);
    this.populateSection(hub);
    hub.addTraceSection(this);
  }

  public void populateSection(Hub hub) {

    this.addFragmentsAndStack(hub);

    if (Exceptions.outOfGasException(hub.pch().exceptions())) {
      return;
    }

    // CONTEXT fragment
    ///////////////////
    ContextFragment contextRowCurrentContext = ContextFragment.readCurrentContextData(hub);

    // ACCOUNT fragment
    ///////////////////
    Address codeAddress = hub.messageFrame().getContractAddress();

    DeploymentInfo deploymentInfo = hub.transients().conflation().deploymentInfo();
    final int deploymentNumber = deploymentInfo.number(codeAddress);
    final boolean deploymentStatus = deploymentInfo.isDeploying(codeAddress);

    final boolean warmth = hub.messageFrame().isAddressWarm(codeAddress);
    Preconditions.checkArgument(warmth);

    AccountSnapshot codeAccount = canonical(hub, codeAddress);

    AccountFragment accountRowCodeAccount =
        hub.factories()
            .accountFragment()
            .make(
                codeAccount,
                codeAccount,
                DomSubStampsSubFragment.standardDomSubStamps(this.hubStamp(), 0));

    // MISCELLANEOUS fragment
    /////////////////////////
    ImcFragment miscellaneousRow = ImcFragment.empty(hub);
    boolean mustAttemptJump = false;
    switch (hub.opCode()) {
      case OpCode.JUMP -> {
        JumpOobCall jumpOobCall = new JumpOobCall();
        miscellaneousRow.callOob(jumpOobCall);
        mustAttemptJump = jumpOobCall.isJumpMustBeAttempted();
      }
      case OpCode.JUMPI -> {
        JumpiOobCall jumpiOobCall = new JumpiOobCall();
        miscellaneousRow.callOob(jumpiOobCall);
        mustAttemptJump = jumpiOobCall.isJumpMustBeAttempted();
      }
      default -> throw new RuntimeException(
          hub.opCode().name() + " not part of the JUMP instruction family");
    }

    // CONTEXT, ACCOUNT, MISCELLANEOUS
    //////////////////////////////////
    this.addFragmentsWithoutStack(
        contextRowCurrentContext, accountRowCodeAccount, miscellaneousRow);

    // jump destination vetting
    ///////////////////////////
    if (mustAttemptJump) {
      this.triggerJumpDestinationVetting(hub);
    }
  }
}
