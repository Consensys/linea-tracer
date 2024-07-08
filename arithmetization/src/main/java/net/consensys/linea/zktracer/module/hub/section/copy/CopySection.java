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

package net.consensys.linea.zktracer.module.hub.section.copy;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.opcode.OpCode;

public class CopySection extends TraceSection {

  // TODO: this constructor is not really necessary
  public CopySection(Hub hub) {
    super(hub);
  }

  public void populateSection(Hub hub) {

    /*
    CopySection.addTraceSection(this);
    TraceSection copySection = new CopySection(this);
    if (!this.opCode().equals(OpCode.RETURNDATACOPY) && !this.opCode().equals(OpCode.CALLDATACOPY)) {
      final Bytes rawTargetAddress =
          switch (this.currentFrame().opCode()) {
            case CODECOPY -> this.currentFrame().byteCodeAddress();
            case EXTCODECOPY -> frame.getStackItem(0);
            default -> throw new IllegalStateException(
                String.format("unexpected opcode %s", this.opCode()));
          };
      final Address targetAddress = Words.toAddress(rawTargetAddress);
      final Account targetAccount = frame.getWorldUpdater().get(targetAddress);

      AccountSnapshot accountBefore =
          AccountSnapshot.fromAccount(
              targetAccount,
              frame.isAddressWarm(targetAddress),
              this.transients.conflation().deploymentInfo().number(targetAddress),
              this.transients.conflation().deploymentInfo().isDeploying(targetAddress));

      AccountSnapshot accountAfter =
          AccountSnapshot.fromAccount(
              targetAccount,
              true,
              this.transients.conflation().deploymentInfo().number(targetAddress),
              this.transients.conflation().deploymentInfo().isDeploying(targetAddress));

      // TODO: this is deprecated, update it. Factories creates an account row (accountFragment)
      // ImcFragment is for miscellaneaous rows
      // ContextFragment
      DomSubStampsSubFragment doingDomSubStamps =
          DomSubStampsSubFragment.standardDomSubStamps(this, 0);
      copySection.addFragment(
          this.currentFrame().opCode() == OpCode.EXTCODECOPY
              ? this.factories
                  .accountFragment()
                  .makeWithTrm(
                      accountBefore, accountBefore, rawTargetAddress, doingDomSubStamps)
              : this.factories
                  .accountFragment()
                  .make(accountBefore, accountAfter, doingDomSubStamps));

      if (this.callStack.current().willRevert()) {
        DomSubStampsSubFragment undoingDomSubStamps =
            DomSubStampsSubFragment.revertWithCurrentDomSubStamps(this, 0);
        copySection.addFragment(
            this.factories
                .accountFragment()
                .make(accountAfter, accountBefore, undoingDomSubStamps));
      }
    } else {
      copySection.addFragment(ContextFragment.readCurrentContextData(this));
    }
    this.addTraceSection(copySection);
    */

    switch (hub.opCode()) {
      case OpCode.CALLDATACOPY -> new CallDataCopySection(hub);
      case OpCode.RETURNDATACOPY -> {
        ReturnDataCopySection returnDataCopySection = new ReturnDataCopySection(hub);
        hub.addTraceSection(returnDataCopySection);
        returnDataCopySection.populateSection(hub);
      }
      case OpCode.CODECOPY -> new CodeCopySection(hub);
      case OpCode.EXTCODECOPY -> {
        ExtCodeCopySection extCodeCopySection = new ExtCodeCopySection(hub);
        hub.addTraceSection(extCodeCopySection);
        extCodeCopySection.populate(hub);
      }
    }
  }
}
