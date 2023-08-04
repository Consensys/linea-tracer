/*
 * Copyright contributors to Hyperledger Besu
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

package net.consensys.linea.zktracer.module.stp;

import java.math.BigInteger;
import java.util.List;

import net.consensys.linea.zktracer.OpCode;
import net.consensys.linea.zktracer.bytes.Bytes16;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.operation.*;

public class Stp implements Module {
  @Override
  public String jsonKey() {
    return "stp";
  }

  private final Trace.TraceBuilder trace = Trace.builder();

  private int stamp = 0;
  private final Wcp wcpModule;

  public Stp(Wcp wcpModule) {
    this.wcpModule = wcpModule;
  }

  public List<OpCode> supportedOpCodes() {
    return List.of(
        OpCode.CALL,
        OpCode.CALLCODE,
        OpCode.DELEGATECALL,
        OpCode.STATICCALL,
        OpCode.CREATE,
        OpCode.CREATE2);
  }

  private long fetchCost(MessageFrame frame, Operation op) {
    long cost;
    if (op instanceof CreateOperation createOperation) {
      cost = createOperation.cost(frame);
    } else if (op instanceof Create2Operation create2Operation) {
      cost = create2Operation.cost(frame);
    } else if (op instanceof CallOperation callOperation) {
      cost = callOperation.cost(frame);
    } else if (op instanceof CallCodeOperation callCodeOperation) {
      cost = callCodeOperation.cost(frame);
    } else if (op instanceof DelegateCallOperation delegateCallOperation) {
      cost = delegateCallOperation.cost(frame);
    } else if (op instanceof StaticCallOperation staticCallOperation) {
      cost = staticCallOperation.cost(frame);
    } else {
      throw new RuntimeException("Neither call nor create in cost calculation");
    }

    return cost;
  }

  private long fetchStpd(MessageFrame frame, Operation op) {
    long stpd;
    if (op instanceof CreateOperation createOperation) {
      stpd = createOperation.cost(frame);
    } else if (op instanceof Create2Operation create2Operation) {
      stpd = create2Operation.cost(frame);
    } else if (op instanceof CallOperation callOperation) {
      stpd = callOperation.gasAvailableForChildCall(frame);
    } else if (op instanceof CallCodeOperation callCodeOperation) {
      stpd = callCodeOperation.gasAvailableForChildCall(frame);
    } else if (op instanceof DelegateCallOperation delegateCallOperation) {
      stpd = delegateCallOperation.gasAvailableForChildCall(frame);
    } else if (op instanceof StaticCallOperation staticCallOperation) {
      stpd = staticCallOperation.gasAvailableForChildCall(frame);
    } else {
      throw new RuntimeException("Neither call nor create in stipend calculation");
    }

    return stpd;
  }

  private BigInteger one64thFloorSub(BigInteger remainingGas, long gasCost) {
    BigInteger subtraction = remainingGas.subtract(BigInteger.valueOf(gasCost));
    return subtraction.divide(BigInteger.valueOf(64));
  }

  @Override
  public void trace(MessageFrame frame) {
    final Operation op = frame.getCurrentOperation();
    final OpCode opCode = OpCode.of(op.getOpcode());

    final Bytes32 gas = Bytes32.wrap(frame.getStackItem(0));
    final Bytes16 gasHi = Bytes16.wrap(gas.slice(0, 16));
    final Bytes16 gasLo = Bytes16.wrap(gas.slice(16));

    final long remainingGas1 = frame.getRemainingGas();
    final BigInteger remainingGas = BigInteger.valueOf(frame.getRemainingGas());
    final long gasCost = fetchCost(frame, op);
    final boolean cctv = isCCTV(opCode);
    final Bytes32 value =
        cctv ? Bytes32.wrap(frame.getStackItem(2)) : Bytes32.wrap(Bytes.ofUnsignedShort(0));
    final Bytes16 valueHi = Bytes16.wrap(value.slice(0, 16));
    final Bytes16 valueLo = Bytes16.wrap(value.slice(16));
    final boolean call = isCall(opCode);
    final BigInteger arg1Lo_iP1 = remainingGas.subtract(BigInteger.valueOf(gasCost));

    this.stamp++;

    for (int counter = 0; counter < 4; counter++) {
      boolean modFlag = counter == 1;
      boolean wcpFlag = !modFlag;
      OpCode exoInst;
      exoInst =
          switch (counter) {
            case 0, 2 -> OpCode.LT;
            case 1 -> OpCode.DIV;
            case 3 -> OpCode.ISZERO;
            default -> throw new RuntimeException("Stipend module counter misbehaving");
          };
      // todo double-check these calculations
      BigInteger passedArg2Lo =
          switch (counter) {
            case 0 -> BigInteger.valueOf(gasCost);
            case 1 -> BigInteger.valueOf(64);
            case 2 -> arg1Lo_iP1.subtract(arg1Lo_iP1.divide(BigInteger.valueOf(64)));
            case 3 -> BigInteger.valueOf(0);
            default -> throw new RuntimeException("Stipend module counter misbehaving");
          };
      BigInteger passedArg1Hi =
          switch (counter) {
            case 0, 1 -> BigInteger.valueOf(0);
            case 2 -> gasHi.toUnsignedBigInteger();
            case 3 -> valueHi.toUnsignedBigInteger();
            default -> throw new RuntimeException("Stipend module counter misbehaving");
          };
      BigInteger passedArg1Lo =
          switch (counter) {
            case 0 -> remainingGas;
            case 1 -> arg1Lo_iP1;
            case 2 -> one64thFloorSub(remainingGas, gasCost);
            case 3 -> valueLo.toUnsignedBigInteger();
            default -> throw new RuntimeException("Stipend module counter misbehaving");
          };
      if (wcpFlag) {
        switch (exoInst) {
          case LT -> {
            if (counter == 0) {
              wcpModule.callLt(
                  Bytes32.wrap(Bytes.ofUnsignedInt(remainingGas1)),
                  Bytes32.wrap(Bytes.ofUnsignedInt(gasCost)));
            } else {
              // (remainingGas - gasCost) - (remainingGas - GasCost)/64
              Bytes32 interimArg =
                  Bytes32.wrap(
                      Bytes.ofUnsignedInt(
                          remainingGas1 - gasCost - (remainingGas1 - gasCost) / 64));
              wcpModule.callLt(gas, interimArg);
            }
          }
          case ISZERO -> wcpModule.callIsZero(value);
          default -> throw new RuntimeException("Stipend module mismatch wcp exoinst");
        }
      }
      if (modFlag) {
        throw new RuntimeException("DIV call not implemented"); // todo implement div function call
      }
      trace
          .stamp(BigInteger.valueOf(stamp))
          .ct(BigInteger.valueOf(counter))
          .inst(BigInteger.valueOf(opCode.value))
          .extType(!call)
          .cctv(cctv)
          .gasActl(remainingGas)
          .gasCost(BigInteger.valueOf(gasCost))
          .gasStpd(BigInteger.valueOf(fetchStpd(frame, op)))
          .gasHi(gasHi.toUnsignedBigInteger())
          .gasLo(gasLo.toUnsignedBigInteger())
          .valueHi(valueHi.toUnsignedBigInteger())
          .valueLo(valueLo.toUnsignedBigInteger())
          .wcpFlag(wcpFlag)
          .modFlag(modFlag)
          .exoInst(BigInteger.valueOf(exoInst.value))
          .arg1Hi(passedArg1Hi)
          .arg1Lo(passedArg1Lo)
          .arg2Lo(passedArg2Lo)
          .zeroConst(BigInteger.valueOf(0));
    }
  }

  private boolean isCCTV(OpCode opCode) {
    return switch (opCode) {
      case CALL, CALLCODE -> true;
      case DELEGATECALL, STATICCALL, CREATE, CREATE2 -> false;
      default -> throw new RuntimeException("Stipend module was given the wrong opcode");
    };
  }

  /**
   * Returns true if opCode is a CALL, false if opCode is CREATE. Throws an exception otherwise
   *
   * @param opCode the opCode
   */
  private boolean isCall(OpCode opCode) {
    return switch (opCode) {
      case CREATE, CREATE2 -> false;
      case CALL, CALLCODE, DELEGATECALL, STATICCALL -> true;
      default -> throw new RuntimeException("Stipend module was given the wrong opcode");
    };
  }

  @Override
  public Object commit() {
    return new StpTrace(this.trace.build());
  }
}
