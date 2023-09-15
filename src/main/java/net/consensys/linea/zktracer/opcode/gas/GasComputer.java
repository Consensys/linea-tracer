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

package net.consensys.linea.zktracer.opcode.gas;

import static org.hyperledger.besu.evm.internal.Words.clampedToLong;

import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.units.bigints.UInt256;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.gascalculator.GasCalculator;
import org.hyperledger.besu.evm.internal.Words;

public final class GasComputer {
  private final GasCalculator gc;

  private static long linearCost(long a, long x, long unit) {
    assert (unit == 1) || (unit == 32);
    return Words.clampedMultiply(a, (Words.clampedAdd(x, unit) - 1) / unit);
  }

  private GasCost logCost(MessageFrame frame, long offset, long size, int numTopics) {
    return new GasCost() {
      @Override
      public long staticGas() {
        switch (numTopics) {
          case 0 -> {
            return GasConstants.G_LOG_0.cost();
          }
          case 1 -> {
            return GasConstants.G_LOG_1.cost();
          }
          case 2 -> {
            return GasConstants.G_LOG_2.cost();
          }
          case 3 -> {
            return GasConstants.G_LOG_3.cost();
          }
          case 4 -> {
            return GasConstants.G_LOG_4.cost();
          }
          default -> throw new IllegalStateException("Unexpected value: " + numTopics);
        }
      }

      @Override
      public long memoryExpansion() {
        return gc.memoryExpansionGasCost(frame, offset, size);
      }

      @Override
      public long linearPerByte() {
        return linearCost(GasConstants.G_LOG_DATA.cost(), size, 1);
      }
    };
  }

  private GasCost callCost(
      MessageFrame frame,
      long stipend,
      long inputDataOffset,
      long inputDataLength,
      long returnDataOffset,
      long returnDataLength,
      Wei value,
      Account recipient,
      Address to) {
    return new GasCost() {
      @Override
      public long memoryExpansion() {
        return Math.max(
            gc.memoryExpansionGasCost(frame, inputDataOffset, inputDataLength),
            gc.memoryExpansionGasCost(frame, returnDataOffset, returnDataLength));
      }

      @Override
      public long accountAccess() {
        if (frame.isAddressWarm(to)) {
          return GasConstants.G_WARM_ACCESS.cost();
        } else {
          return GasConstants.G_COLD_ACCOUNT_ACCESS.cost();
        }
      }

      @Override
      public long accountCreation() {
        if ((recipient == null || recipient.isEmpty()) && !value.isZero()) {
          return GasConstants.G_NEW_ACCOUNT.cost();
        } else {
          return 0L;
        }
      }

      @Override
      public long transferValue() {
        if (value.isZero()) {
          return 0L;
        } else {
          return GasConstants.G_CALL_VALUE.cost();
        }
      }

      @Override
      public long rawStipend() {
        final long cost = memoryExpansion() + accountAccess() + accountCreation() + transferValue();
        if (cost > frame.getRemainingGas()) {
          return 0L;
        } else {
          final long remaining = frame.getRemainingGas() - cost;
          final long weird = remaining - remaining / 64;

          return Math.min(weird, stipend);
        }
      }

      @Override
      public long extraStipend() {
        if (value.isZero()) {
          return 0L;
        } else {
          return GasConstants.G_CALL_STIPEND.cost();
        }
      }
    };
  }

  public GasComputer(GasCalculator gc) {
    this.gc = gc;
  }

  public GasCost of(MessageFrame frame, OpCode opCode) {
    switch (opCode) {
      case STOP -> new GasCost() {
        @Override
        public long staticGas() {
          return gc.getZeroTierGasCost();
        }
      };
      case ADD,
          SUB,
          NOT,
          LT,
          GT,
          SLT,
          SGT,
          EQ,
          ISZERO,
          AND,
          OR,
          XOR,
          BYTE,
          SHL,
          SHR,
          SAR,
          CALLDATALOAD,
          PUSH1,
          PUSH2,
          PUSH3,
          PUSH4,
          PUSH5,
          PUSH6,
          PUSH7,
          PUSH8,
          PUSH9,
          PUSH10,
          PUSH11,
          PUSH12,
          PUSH13,
          PUSH14,
          PUSH15,
          PUSH16,
          PUSH17,
          PUSH18,
          PUSH19,
          PUSH20,
          PUSH21,
          PUSH22,
          PUSH23,
          PUSH24,
          PUSH25,
          PUSH26,
          PUSH27,
          PUSH28,
          PUSH29,
          PUSH30,
          PUSH31,
          PUSH32,
          DUP1,
          DUP2,
          DUP3,
          DUP4,
          DUP5,
          DUP6,
          DUP7,
          DUP8,
          DUP9,
          DUP10,
          DUP11,
          DUP12,
          DUP13,
          DUP14,
          DUP15,
          DUP16,
          SWAP1,
          SWAP2,
          SWAP3,
          SWAP4,
          SWAP5,
          SWAP6,
          SWAP7,
          SWAP8,
          SWAP9,
          SWAP10,
          SWAP11,
          SWAP12,
          SWAP13,
          SWAP14,
          SWAP15,
          SWAP16 -> new GasCost() {
        @Override
        public long staticGas() {
          return gc.getVeryLowTierGasCost();
        }
      };
      case MUL, DIV, SDIV, MOD, SMOD, SIGNEXTEND, SELFBALANCE -> new GasCost() {
        @Override
        public long staticGas() {
          return gc.getLowTierGasCost();
        }
      };
      case ADDMOD, MULMOD, JUMP -> new GasCost() {
        @Override
        public long staticGas() {
          return gc.getMidTierGasCost();
        }
      };
      case EXP -> new GasCost() {
        @Override
        public long staticGas() {
          return GasConstants.G_EXP.cost();
        }

        @Override
        public long expGas() {
          return linearCost(GasConstants.G_EXP_BYTE.cost(), frame.getStackItem(1).bitLength(), 1);
        }
      };
      case SHA3 -> new GasCost() {
        @Override
        public long staticGas() {
          return GasConstants.G_KECCAK_256.cost();
        }

        @Override
        public long memoryExpansion() {
          long offset = clampedToLong(frame.getStackItem(0));
          long length = clampedToLong(frame.getStackItem(1));
          return gc.memoryExpansionGasCost(frame, offset, length);
        }

        @Override
        public long linearPerWord() {
          return linearCost(
              GasConstants.G_KECCAK_256_WORD.cost(), frame.getStackItem(1).bitLength(), 32);
        }
      };
      case ADDRESS,
          ORIGIN,
          CALLER,
          CALLVALUE,
          CALLDATASIZE,
          CODESIZE,
          GASPRICE,
          COINBASE,
          TIMESTAMP,
          NUMBER,
          DIFFICULTY,
          GASLIMIT,
          CHAINID,
          RETURNDATASIZE,
          POP,
          PC,
          MSIZE,
          GAS,
          BASEFEE -> new GasCost() {
        @Override
        public long staticGas() {
          return gc.getBaseTierGasCost();
        }
      };
      case BALANCE, EXTCODESIZE, EXTCODEHASH -> new GasCost() {
        @Override
        public long accountAccess() {
          Address target = Address.wrap(frame.getStackItem(0));

          if (frame.isAddressWarm(target)) {
            return gc.getWarmStorageReadCost();
          } else {
            return gc.getColdAccountAccessCost();
          }
        }
      };
      case CALLDATACOPY, CODECOPY, RETURNDATACOPY -> new GasCost() {
        @Override
        public long staticGas() {
          return gc.getVeryLowTierGasCost();
        }

        @Override
        public long memoryExpansion() {
          long offset = clampedToLong(frame.getStackItem(0));
          long length = clampedToLong(frame.getStackItem(2));
          return gc.memoryExpansionGasCost(frame, offset, length);
        }

        @Override
        public long linearPerWord() {
          return linearCost(GasConstants.G_COPY.cost(), frame.getStackItem(1).bitLength(), 32);
        }
      };
      case EXTCODECOPY -> new GasCost() {
        @Override
        public long memoryExpansion() {
          long offset = clampedToLong(frame.getStackItem(1));
          long length = clampedToLong(frame.getStackItem(3));
          return gc.memoryExpansionGasCost(frame, offset, length);
        }

        @Override
        public long accountAccess() {
          Address target = Address.wrap(frame.getStackItem(0));

          if (frame.isAddressWarm(target)) {
            return gc.getWarmStorageReadCost();
          } else {
            return gc.getColdAccountAccessCost();
          }
        }

        @Override
        public long linearPerWord() {
          return linearCost(GasConstants.G_COPY.cost(), frame.getStackItem(3).bitLength(), 32);
        }
      };
      case BLOCKHASH -> new GasCost() {
        @Override
        public long staticGas() {
          return gc.getBlockHashOperationGasCost();
        }
      };
      case MLOAD, MSTORE -> new GasCost() {
        @Override
        public long staticGas() {
          return gc.getVeryLowTierGasCost();
        }

        @Override
        public long memoryExpansion() {
          long offset = clampedToLong(frame.getStackItem(0));
          return gc.memoryExpansionGasCost(frame, offset, 32);
        }
      };
      case MSTORE8 -> new GasCost() {
        @Override
        public long staticGas() {
          return gc.getVeryLowTierGasCost();
        }

        @Override
        public long memoryExpansion() {
          long offset = clampedToLong(frame.getStackItem(0));
          return gc.memoryExpansionGasCost(frame, offset, 1);
        }
      };
      case SLOAD -> {
        return new GasCost() {
          @Override
          public long storageWarmth() {
            final UInt256 key = UInt256.fromBytes(frame.getStackItem(0));
            if (frame.isStorageWarm(frame.getRecipientAddress(), key)) {
              return GasConstants.G_WARM_ACCESS.cost();
            } else {
              return GasConstants.G_COLD_S_LOAD.cost();
            }
          }
        };
      }
      case SSTORE -> {
        final UInt256 key = UInt256.fromBytes(frame.getStackItem(0));
        final Account account = frame.getWorldUpdater().getAccount(frame.getRecipientAddress());
        final UInt256 currentValue = account.getStorageValue(key);
        final UInt256 originalValue = account.getOriginalStorageValue(key);
        final UInt256 newValue = UInt256.fromBytes(frame.getStackItem(1));

        return new GasCost() {
          @Override
          public long storageWarmth() {
            if (frame.isStorageWarm(frame.getRecipientAddress(), key)) {
              return 0L;
            } else {
              return GasConstants.G_COLD_S_LOAD.cost();
            }
          }

          @Override
          public long sStoreValue() {
            if (newValue.equals(currentValue) || !originalValue.equals(currentValue)) {
              return GasConstants.G_WARM_ACCESS.cost();
            } else {
              return originalValue.isZero()
                  ? GasConstants.G_S_SET.cost()
                  : GasConstants.G_S_RESET.cost();
            }
          }

          @Override
          public long refund() {
            long rDirtyClear = 0;
            if (!originalValue.isZero() && currentValue.isZero()) {
              rDirtyClear = -GasConstants.R_S_CLEAR.cost();
            }
            if (!originalValue.isZero() && newValue.isZero()) {
              rDirtyClear = GasConstants.R_S_CLEAR.cost();
            }

            long rDirtyReset = 0;
            if (originalValue.equals(newValue) && originalValue.isZero()) {
              rDirtyReset = GasConstants.G_S_SET.cost() - GasConstants.G_WARM_ACCESS.cost();
            }
            if (originalValue.equals(newValue) && !originalValue.isZero()) {
              rDirtyReset = GasConstants.G_S_RESET.cost() - GasConstants.G_WARM_ACCESS.cost();
            }

            long r = 0;
            if (!currentValue.equals(newValue)
                && currentValue.equals(originalValue)
                && newValue.isZero()) {
              r = GasConstants.R_S_CLEAR.cost();
            }
            if (!currentValue.equals(newValue) && !currentValue.equals(originalValue)) {
              r = rDirtyClear + rDirtyReset;
            }

            return r;
          }
        };
      }
      case JUMPI -> new GasCost() {
        @Override
        public long staticGas() {
          return gc.getHighTierGasCost();
        }
      };
      case JUMPDEST -> new GasCost() {
        @Override
        public long staticGas() {
          return gc.getJumpDestOperationGasCost();
        }
      };
      case LOG0 -> {
        long offset = clampedToLong(frame.getStackItem(0));
        long size = clampedToLong(frame.getStackItem(1));

        return logCost(frame, offset, size, 0);
      }
      case LOG1 -> {
        long offset = clampedToLong(frame.getStackItem(0));
        long size = clampedToLong(frame.getStackItem(1));

        return logCost(frame, offset, size, 1);
      }
      case LOG2 -> {
        long offset = clampedToLong(frame.getStackItem(0));
        long size = clampedToLong(frame.getStackItem(1));

        return logCost(frame, offset, size, 2);
      }
      case LOG3 -> {
        long offset = clampedToLong(frame.getStackItem(0));
        long size = clampedToLong(frame.getStackItem(1));

        return logCost(frame, offset, size, 3);
      }
      case LOG4 -> {
        long offset = clampedToLong(frame.getStackItem(0));
        long size = clampedToLong(frame.getStackItem(1));

        return logCost(frame, offset, size, 4);
      }
      case CREATE -> {
        final long initCodeOffset = clampedToLong(frame.getStackItem(1));
        final long initCodeLength = clampedToLong(frame.getStackItem(2));

        return new GasCost() {
          @Override
          public long staticGas() {
            return GasConstants.G_CREATE.cost();
          }

          @Override
          public long memoryExpansion() {
            return gc.memoryExpansionGasCost(frame, initCodeOffset, initCodeLength);
          }

          @Override
          public long rawStipend() {
            long currentGas = frame.getRemainingGas();
            long gasCost = this.staticGas() + this.memoryExpansion();

            if (gasCost > currentGas) {
              return 0;
            } else {
              return currentGas - currentGas / 64;
            }
          }
        };
      }
      case CREATE2 -> {
        final long initCodeOffset = clampedToLong(frame.getStackItem(1));
        final long initCodeLength = clampedToLong(frame.getStackItem(2));

        return new GasCost() {
          @Override
          public long staticGas() {
            return GasConstants.G_CREATE.cost();
          }

          @Override
          public long memoryExpansion() {
            return gc.memoryExpansionGasCost(frame, initCodeOffset, initCodeLength);
          }

          @Override
          public long linearPerWord() {
            return linearCost(GasConstants.G_KECCAK_256_WORD.cost(), initCodeLength, 32);
          }

          @Override
          public long rawStipend() {
            long currentGas = frame.getRemainingGas();
            long gasCost = this.staticGas() + this.memoryExpansion() + this.linearPerWord();

            if (gasCost > currentGas) {
              return 0;
            } else {
              return currentGas - currentGas / 64;
            }
          }
        };
      }
      case CALL -> {
        final long stipend = clampedToLong(frame.getStackItem(0));
        final Account recipient =
            frame.getWorldUpdater().get(Words.toAddress(frame.getStackItem(1)));
        final Address to = recipient.getAddress();
        final Wei value = Wei.wrap(frame.getStackItem(2));
        final long inputDataOffset = clampedToLong(frame.getStackItem(3));
        final long inputDataLength = clampedToLong(frame.getStackItem(4));
        final long returnDataOffset = clampedToLong(frame.getStackItem(5));
        final long returnDataLength = clampedToLong(frame.getStackItem(6));
        return callCost(
            frame,
            stipend,
            inputDataOffset,
            inputDataLength,
            returnDataOffset,
            returnDataLength,
            value,
            recipient,
            to);
      }
      case CALLCODE -> {
        final long stipend = clampedToLong(frame.getStackItem(0));
        final Account recipient = frame.getWorldUpdater().get(frame.getRecipientAddress());
        final Address to = Words.toAddress(frame.getStackItem(1));
        final Wei value = Wei.wrap(frame.getStackItem(2));
        final long inputDataOffset = clampedToLong(frame.getStackItem(3));
        final long inputDataLength = clampedToLong(frame.getStackItem(4));
        final long returnDataOffset = clampedToLong(frame.getStackItem(5));
        final long returnDataLength = clampedToLong(frame.getStackItem(6));
        return callCost(
            frame,
            stipend,
            inputDataOffset,
            inputDataLength,
            returnDataOffset,
            returnDataLength,
            value,
            recipient,
            to);
      }
      case DELEGATECALL -> {
        final long stipend = clampedToLong(frame.getStackItem(0));
        final Account recipient = frame.getWorldUpdater().get(frame.getRecipientAddress());
        final Address to = Words.toAddress(frame.getStackItem(1));
        final long inputDataOffset = clampedToLong(frame.getStackItem(2));
        final long inputDataLength = clampedToLong(frame.getStackItem(3));
        final long returnDataOffset = clampedToLong(frame.getStackItem(4));
        final long returnDataLength = clampedToLong(frame.getStackItem(5));
        return callCost(
            frame,
            stipend,
            inputDataOffset,
            inputDataLength,
            returnDataOffset,
            returnDataLength,
            Wei.ZERO,
            recipient,
            to);
      }
      case STATICCALL -> {
        final long stipend = clampedToLong(frame.getStackItem(0));
        final Account recipient =
            frame.getWorldUpdater().get(Words.toAddress(frame.getStackItem(1)));
        final Address to = recipient.getAddress();
        final long inputDataOffset = clampedToLong(frame.getStackItem(2));
        final long inputDataLength = clampedToLong(frame.getStackItem(3));
        final long returnDataOffset = clampedToLong(frame.getStackItem(4));
        final long returnDataLength = clampedToLong(frame.getStackItem(5));
        return callCost(
            frame,
            stipend,
            inputDataOffset,
            inputDataLength,
            returnDataOffset,
            returnDataLength,
            Wei.ZERO,
            recipient,
            to);
      }
      case RETURN -> {
        return new GasCost() {
          @Override
          public long memoryExpansion() {
            final long offset = clampedToLong(frame.getStackItem(0));
            final long length = clampedToLong(frame.getStackItem(1));

            return gc.memoryExpansionGasCost(frame, offset, length);
          }

          @Override
          public long codeReturn() {
            final long length = clampedToLong(frame.getStackItem(1));

            if (length > 24_576) {
              return 0L;
            } else {
              return GasConstants.G_CODE_DEPOSIT.cost() * length;
            }
          }
        };
      }
      case REVERT -> {
        final long from = clampedToLong(frame.popStackItem());
        final long length = clampedToLong(frame.popStackItem());

        return new GasCost() {
          @Override
          public long memoryExpansion() {
            return gc.memoryExpansionGasCost(frame, from, length);
          }
        };
      }
      case INVALID -> new GasCost() {};
      case SELFDESTRUCT -> new GasCost() {
        @Override
        public long staticGas() {
          return GasConstants.G_SELF_DESTRUCT.cost();
        }

        @Override
        public long accountAccess() {
          final Address beneficiaryAddress = Words.toAddress(frame.getStackItem(0));
          if (frame.isAddressWarm(beneficiaryAddress)) {
            return 0L;
          } else {
            return GasConstants.G_COLD_ACCOUNT_ACCESS.cost();
          }
        }

        @Override
        public long accountCreation() {
          final Address beneficiaryAddress = Words.toAddress(frame.getStackItem(0));
          final Account beneficiaryAccount = frame.getWorldUpdater().get(beneficiaryAddress);
          final Address me = frame.getRecipientAddress();
          final Wei balance = frame.getWorldUpdater().get(me).getBalance();

          if ((beneficiaryAccount == null || beneficiaryAccount.isEmpty()) && !balance.isZero()) {
            return GasConstants.G_NEW_ACCOUNT.cost();
          } else {
            return 0L;
          }
        }

        @Override
        public long refund() {
          return GasConstants.R_SELF_DESTRUCT.cost();
        }
      };
      default -> throw new IllegalStateException("Unexpected value: " + opCode);
    }
    throw new IllegalStateException();
  }
}
