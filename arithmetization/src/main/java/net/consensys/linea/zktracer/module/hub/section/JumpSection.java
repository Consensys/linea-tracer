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

import static net.consensys.linea.zktracer.module.hub.AccountSnapshot.fromAddress;

import com.google.common.base.Preconditions;
import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.OobCall;
import net.consensys.linea.zktracer.module.hub.transients.DeploymentInfo;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class JumpSection extends TraceSection {

  public static void appendToTrace(Hub hub, WorldView worldView) {
    final JumpSection currentSection = new JumpSection();
    currentSection.addFragmentsAndStack(hub);

    if (hub.pch().exceptions().outOfGasException()) {
      return;
    }

    hub.addTraceSection(currentSection);

    // CONTEXT fragment
    ///////////////////
    ContextFragment contextRowCurrentContext = ContextFragment.readCurrentContextData(hub);

    // ACCOUNT fragment
    ///////////////////
    Address codeAddress = hub.messageFrame().getContractAddress();
    AccountFragment.AccountFragmentFactory accountFragmentFactory =
        new AccountFragment.AccountFragmentFactory(hub.defers());

    DeploymentInfo deploymentInfo = hub.transients().conflation().deploymentInfo();
    final int deploymentNumber = deploymentInfo.number(codeAddress);
    final boolean deploymentStatus = deploymentInfo.isDeploying(codeAddress);

    final boolean warmth = hub.messageFrame().isAddressWarm(codeAddress);
    Preconditions.checkArgument(warmth);

    AccountSnapshot codeAccount =
        fromAddress(codeAddress, warmth, deploymentNumber, deploymentStatus);

    AccountFragment accountRowCodeAccount =
        accountFragmentFactory.make(
            codeAccount, codeAccount, DomSubStampsSubFragment.standardDomSubStamps(hub, 0));

    // MISCELLANEOUS fragment
    /////////////////////////
    ImcFragment miscRow = ImcFragment.empty(hub);
    OobCall oobCall;
    boolean mustAttemptJump = false;
    switch (hub.opCode()) {
      case OpCode.JUMP -> {
        JumpOobCall jumpOobCall = new JumpOobCall(hub);
        miscRow.callOob(jumpOobCall);
        mustAttemptJump = jumpOobCall.getJumpMustBeAttempted();
        oobCall = jumpOobCall;
      }
      case OpCode.JUMPI -> {
        JumpiOobCall jumpiOobCall = new JumpiOobCall(hub);
        miscRow.callOob(jumpiOobCall);
        mustAttemptJump = jumpiOobCall.getJumpMustBeAttempted();
        oobCall = jumpiOobCall;
      }
      default -> throw new RuntimeException(
          hub.opCode().name() + " not part of the JUMP instruction family");
    }

    miscRow.callOob(oobCall);

    // CONTEXT, ACCOUNT, MISCELLANEOUS
    //////////////////////////////////
    currentSection.addFragmentsWithoutStack(
        hub, contextRowCurrentContext, accountRowCodeAccount, miscRow);

    // jump destination vetting
    ///////////////////////////
    if (mustAttemptJump) {
      currentSection.triggerJumpDestinationVetting(hub);
    }
  }

  private JumpSection() {}

  public JumpSection(Hub hub, TraceFragment... chunks) {
    this.addFragmentsAndStack(hub, chunks);
  }
}
