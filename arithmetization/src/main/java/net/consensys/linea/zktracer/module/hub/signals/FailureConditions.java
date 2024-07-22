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

package net.consensys.linea.zktracer.module.hub.signals;

import static net.consensys.linea.zktracer.types.AddressUtils.getDeploymentAddress;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.account.AccountState;
import org.hyperledger.besu.evm.frame.MessageFrame;

@RequiredArgsConstructor
public final class FailureConditions {
  private boolean deploymentAddressHasNonZeroNonce;
  private boolean deploymentAddressHasNonEmptyCode;

  public void reset() {
    this.deploymentAddressHasNonEmptyCode = false;
    this.deploymentAddressHasNonZeroNonce = false;
  }

  public void prepare(MessageFrame frame) {
    final OpCode instruction = OpCode.of(frame.getCurrentOperation().getOpcode());

    if (!instruction.isCreate()) {
      return;
    }
    final Address deploymentAddress = getDeploymentAddress(frame);

    final Optional<Account> deploymentAccount =
        Optional.ofNullable(frame.getWorldUpdater().get(deploymentAddress));
    deploymentAddressHasNonZeroNonce = deploymentAccount.map(a -> a.getNonce() != 0).orElse(false);
    deploymentAddressHasNonEmptyCode = deploymentAccount.map(AccountState::hasCode).orElse(false);
  }

  public boolean any() {
    return deploymentAddressHasNonEmptyCode || deploymentAddressHasNonZeroNonce;
  }
}
