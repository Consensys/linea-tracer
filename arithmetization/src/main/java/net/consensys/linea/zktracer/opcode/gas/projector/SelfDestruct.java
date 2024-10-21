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

package net.consensys.linea.zktracer.opcode.gas.projector;

import static net.consensys.linea.zktracer.types.AddressUtils.isAddressWarm;

import net.consensys.linea.zktracer.module.constants.GlobalConstants;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.Words;

public final class SelfDestruct extends GasProjection {
  private final MessageFrame frame;
  private Address beneficiaryAddress = null;

  public SelfDestruct(MessageFrame frame) {
    this.frame = frame;
    if (frame.stackSize() > 0) {
      this.beneficiaryAddress = Words.toAddress(frame.getStackItem(0));
    }
  }

  boolean isInvalid() {
    return this.beneficiaryAddress == null;
  }

  @Override
  public long staticGas() {
    return GlobalConstants.GAS_CONST_G_SELFDESTRUCT;
  }

  @Override
  public long accountAccess() {
    if (this.isInvalid()) {
      return 0;
    }

    if (isAddressWarm(frame, beneficiaryAddress)) {
      return 0L;
    } else {
      return GlobalConstants.GAS_CONST_G_COLD_ACCOUNT_ACCESS;
    }
  }

  @Override
  public long accountCreation() {
    if (this.isInvalid()) {
      return 0;
    }

    final Account beneficiaryAccount = frame.getWorldUpdater().get(this.beneficiaryAddress);
    final Address me = frame.getRecipientAddress();
    final Wei balance = frame.getWorldUpdater().get(me).getBalance();

    if ((beneficiaryAccount == null || beneficiaryAccount.isEmpty()) && !balance.isZero()) {
      return GlobalConstants.GAS_CONST_G_NEW_ACCOUNT;
    } else {
      return 0L;
    }
  }

  @Override
  public long refund() {
    // return GlobalConstants.REFUND_CONST_R_SELFDESTRUCT;
    return 0;
  }
}
