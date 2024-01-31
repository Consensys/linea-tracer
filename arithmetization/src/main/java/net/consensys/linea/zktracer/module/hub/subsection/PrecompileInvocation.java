/*
 * Copyright ConsenSys AG.
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

package net.consensys.linea.zktracer.module.hub.subsection;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.Precompile;
import net.consensys.linea.zktracer.module.hub.memory.MemorySpan;
import net.consensys.linea.zktracer.module.limits.precompiles.Blake2fRounds;
import net.consensys.linea.zktracer.module.limits.precompiles.EcAddEffectiveCall;
import net.consensys.linea.zktracer.module.limits.precompiles.EcMulEffectiveCall;
import net.consensys.linea.zktracer.module.limits.precompiles.EcPairingCallEffectiveCall;
import net.consensys.linea.zktracer.module.limits.precompiles.EcRecoverEffectiveCall;
import net.consensys.linea.zktracer.module.limits.precompiles.ModexpEffectiveCall;
import net.consensys.linea.zktracer.module.limits.precompiles.Rip160Blocks;
import net.consensys.linea.zktracer.module.limits.precompiles.Sha256Blocks;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.internal.Words;

public record PrecompileInvocation(
    Precompile precompile,
    MemorySpan callData,
    MemorySpan requestedReturnData,
    boolean failureKnownToHub,
    boolean failureKnownToRam,
    /* The price of the *CALL itself */
    long opCodeGas,
    long precompilePrice,
    /* The available gas just before the *CALL opcode execution */
    long gasAtCall,
    /* If applicable, the gas given to a precompile */
    long gasAllowance,
    long returnGas) {
  public boolean success() {
    return !this.failureKnownToHub && !this.failureKnownToRam;
  }

  public boolean hubSuccess() {
    return !this.failureKnownToHub;
  }

  public boolean ramSuccess() {
    return !this.failureKnownToRam;
  }

  public static PrecompileInvocation of(final Hub hub, Precompile p) {
    final boolean failureKnownToHub =
        switch (p) {
          case EC_RECOVER -> !EcRecoverEffectiveCall.hasEnoughGas(hub);
          case SHA2_256 -> !Sha256Blocks.hasEnoughGas(hub);
          case RIPEMD_160 -> !Rip160Blocks.hasEnoughGas(hub);
          case IDENTITY -> switch (hub.opCode()) {
            case CALL, STATICCALL, DELEGATECALL, CALLCODE -> {
              final Address target = Words.toAddress(hub.messageFrame().getStackItem(1));
              if (target.equals(Address.ID)) {
                long dataByteLength = 0;
                switch (hub.opCode()) {
                  case CALL, CALLCODE -> dataByteLength =
                      Words.clampedToLong(hub.messageFrame().getStackItem(4));
                  case DELEGATECALL, STATICCALL -> dataByteLength =
                      Words.clampedToLong(hub.messageFrame().getStackItem(3));
                }
                final long wordCount = (dataByteLength + 31) / 32;
                final long gasNeeded = 15 + 3 * wordCount;

                yield hub.gasAllowanceForCall() < gasNeeded;
              } else {
                throw new IllegalStateException();
              }
            }
            default -> throw new IllegalStateException();
          };
          case MODEXP -> false;
          case EC_ADD -> hub.gasAllowanceForCall() < 150;
          case EC_MUL -> hub.gasAllowanceForCall() < 6000;
          case EC_PAIRING -> EcPairingCallEffectiveCall.isHubFailure(hub);
          case BLAKE2F -> Blake2fRounds.isHubFailure(hub);
        };

    final boolean failureKnownToRam =
        !failureKnownToHub
            && switch (p) {
              case EC_RECOVER, IDENTITY, RIPEMD_160, SHA2_256 -> false;
              case MODEXP -> false; // TODO: update when MODEXP is merged
              case EC_ADD -> EcAddEffectiveCall.isRamFailure(hub);
              case EC_MUL -> EcMulEffectiveCall.isRamFailure(hub);
              case EC_PAIRING -> EcPairingCallEffectiveCall.isRamFailure(hub);
              case BLAKE2F -> Blake2fRounds.isRamFailure(hub);
            };

    final long opCodeGas = Hub.gp.of(hub.messageFrame(), hub.opCode()).total();

    final long precompilePrice =
        failureKnownToHub || failureKnownToRam
            ? hub.gasAllowanceForCall()
            : switch (p) {
              case EC_RECOVER -> EcRecoverEffectiveCall.gasCost();
              case SHA2_256 -> Sha256Blocks.gasCost(hub);
              case RIPEMD_160 -> Rip160Blocks.gasCost(hub);
              case IDENTITY -> switch (hub.opCode()) {
                case CALL, STATICCALL, DELEGATECALL, CALLCODE -> {
                  final Address target = Words.toAddress(hub.messageFrame().getStackItem(1));
                  if (target.equals(Address.ID)) {
                    long dataByteLength = 0;
                    switch (hub.opCode()) {
                      case CALL, CALLCODE -> dataByteLength =
                          Words.clampedToLong(hub.messageFrame().getStackItem(4));
                      case DELEGATECALL, STATICCALL -> dataByteLength =
                          Words.clampedToLong(hub.messageFrame().getStackItem(3));
                    }
                    final long wordCount = (dataByteLength + 31) / 32;
                    yield 15 + 3 * wordCount;
                  } else {
                    throw new IllegalStateException();
                  }
                }
                default -> throw new IllegalStateException();
              };
              case MODEXP -> ModexpEffectiveCall.gasCost(hub);
              case EC_ADD -> EcAddEffectiveCall.gasCost();
              case EC_MUL -> EcMulEffectiveCall.gasCost();
              case EC_PAIRING -> EcPairingCallEffectiveCall.gasCost(hub);
              case BLAKE2F -> Blake2fRounds.gasCost(hub);
            };

    final long returnGas =
        failureKnownToHub || failureKnownToRam ? 0 : hub.gasAllowanceForCall() - precompilePrice;

    return new PrecompileInvocation(
        p,
        hub.callStack().futureId(),
        failureKnownToHub,
        failureKnownToRam,
        opCodeGas,
        precompilePrice,
        hub.messageFrame().getRemainingGas(),
        hub.gasAllowanceForCall(),
        returnGas);
  }
}
