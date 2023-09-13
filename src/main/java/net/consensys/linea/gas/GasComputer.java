package net.consensys.linea.gas;

import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.gascalculator.GasCalculator;

public record StopGas(GasCalculator gc) implements GasCost {
  @Override
  public long staticGas() {
    return gc.getZeroTierGasCost();
  }
}

public final class GasComputer {
  private final GasCalculator gc;
  public GasComputer(GasCalculator gc) {this.gc = gc;}

  public GasCost of(MessageFrame frame, OpCode opCode) {
    switch (opCode) {
      case STOP -> {
        return new StopGas(gc);
      }
      case ADD -> {
      }
      case MUL -> {
      }
      case SUB -> {
      }
      case DIV -> {
      }
      case SDIV -> {
      }
      case MOD -> {
      }
      case SMOD -> {
      }
      case ADDMOD -> {
      }
      case MULMOD -> {
      }
      case EXP -> {
      }
      case SIGNEXTEND -> {
      }
      case LT -> {
      }
      case GT -> {
      }
      case SLT -> {
      }
      case SGT -> {
      }
      case EQ -> {
      }
      case ISZERO -> {
      }
      case AND -> {
      }
      case OR -> {
      }
      case XOR -> {
      }
      case NOT -> {
      }
      case BYTE -> {
      }
      case SHL -> {
      }
      case SHR -> {
      }
      case SAR -> {
      }
      case SHA3 -> {
      }
      case ADDRESS -> {
      }
      case BALANCE -> {
      }
      case ORIGIN -> {
      }
      case CALLER -> {
      }
      case CALLVALUE -> {
      }
      case CALLDATALOAD -> {
      }
      case CALLDATASIZE -> {
      }
      case CALLDATACOPY -> {
      }
      case CODESIZE -> {
      }
      case CODECOPY -> {
      }
      case GASPRICE -> {
      }
      case EXTCODESIZE -> {
      }
      case EXTCODECOPY -> {
      }
      case RETURNDATASIZE -> {
      }
      case RETURNDATACOPY -> {
      }
      case EXTCODEHASH -> {
      }
      case BLOCKHASH -> {
      }
      case COINBASE -> {
      }
      case TIMESTAMP -> {
      }
      case NUMBER -> {
      }
      case DIFFICULTY -> {
      }
      case GASLIMIT -> {
      }
      case CHAINID -> {
      }
      case SELFBALANCE -> {
      }
      case BASEFEE -> {
      }
      case POP -> {
      }
      case MLOAD -> {
      }
      case MSTORE -> {
      }
      case MSTORE8 -> {
      }
      case SLOAD -> {
      }
      case SSTORE -> {
      }
      case JUMP -> {
      }
      case JUMPI -> {
      }
      case PC -> {
      }
      case MSIZE -> {
      }
      case GAS -> {
      }
      case JUMPDEST -> {
      }
      case PUSH1 -> {
      }
      case PUSH2 -> {
      }
      case PUSH3 -> {
      }
      case PUSH4 -> {
      }
      case PUSH5 -> {
      }
      case PUSH6 -> {
      }
      case PUSH7 -> {
      }
      case PUSH8 -> {
      }
      case PUSH9 -> {
      }
      case PUSH10 -> {
      }
      case PUSH11 -> {
      }
      case PUSH12 -> {
      }
      case PUSH13 -> {
      }
      case PUSH14 -> {
      }
      case PUSH15 -> {
      }
      case PUSH16 -> {
      }
      case PUSH17 -> {
      }
      case PUSH18 -> {
      }
      case PUSH19 -> {
      }
      case PUSH20 -> {
      }
      case PUSH21 -> {
      }
      case PUSH22 -> {
      }
      case PUSH23 -> {
      }
      case PUSH24 -> {
      }
      case PUSH25 -> {
      }
      case PUSH26 -> {
      }
      case PUSH27 -> {
      }
      case PUSH28 -> {
      }
      case PUSH29 -> {
      }
      case PUSH30 -> {
      }
      case PUSH31 -> {
      }
      case PUSH32 -> {
      }
      case DUP1 -> {
      }
      case DUP2 -> {
      }
      case DUP3 -> {
      }
      case DUP4 -> {
      }
      case DUP5 -> {
      }
      case DUP6 -> {
      }
      case DUP7 -> {
      }
      case DUP8 -> {
      }
      case DUP9 -> {
      }
      case DUP10 -> {
      }
      case DUP11 -> {
      }
      case DUP12 -> {
      }
      case DUP13 -> {
      }
      case DUP14 -> {
      }
      case DUP15 -> {
      }
      case DUP16 -> {
      }
      case SWAP1 -> {
      }
      case SWAP2 -> {
      }
      case SWAP3 -> {
      }
      case SWAP4 -> {
      }
      case SWAP5 -> {
      }
      case SWAP6 -> {
      }
      case SWAP7 -> {
      }
      case SWAP8 -> {
      }
      case SWAP9 -> {
      }
      case SWAP10 -> {
      }
      case SWAP11 -> {
      }
      case SWAP12 -> {
      }
      case SWAP13 -> {
      }
      case SWAP14 -> {
      }
      case SWAP15 -> {
      }
      case SWAP16 -> {
      }
      case LOG0 -> {
      }
      case LOG1 -> {
      }
      case LOG2 -> {
      }
      case LOG3 -> {
      }
      case LOG4 -> {
      }
      case CREATE -> {
      }
      case CALL -> {
      }
      case CALLCODE -> {
      }
      case RETURN -> {
      }
      case DELEGATECALL -> {
      }
      case CREATE2 -> {
      }
      case STATICCALL -> {
      }
      case REVERT -> {
      }
      case INVALID -> {
      }
      case SELFDESTRUCT -> {
      }
    }
    throw new IllegalStateException();
  }
}
