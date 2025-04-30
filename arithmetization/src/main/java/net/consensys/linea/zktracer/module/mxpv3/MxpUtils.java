package net.consensys.linea.zktracer.module.mxpv3;

import static net.consensys.linea.zktracer.Trace.GAS_CONST_G_MEMORY;
import static org.hyperledger.besu.evm.internal.Words.clampedAdd;
import static org.hyperledger.besu.evm.internal.Words.clampedMultiply;

import net.consensys.linea.zktracer.opcode.OpCode;

public class MxpUtils {
  public static boolean isSingleOffsetOpcode(OpCode opCode) {
    return opCode == OpCode.MLOAD
        || opCode == OpCode.MSTORE
        || opCode == OpCode.MSTORE8
        || opCode == OpCode.REVERT
        || opCode == OpCode.RETURN
        || opCode.isLog()
        || opCode == OpCode.SHA3
        || opCode.isCopy()
        || opCode.isCreate();
  }

  public static boolean isDoubleOffsetOpcode(OpCode opCode) {
    return opCode == OpCode.MCOPY || opCode.isCall();
  }

  public static boolean isWordPricingOpcode(OpCode opCode) {
    return opCode.isLog()
        || opCode == OpCode.SHA3
        || opCode.isCopy()
        || opCode.isCreate()
        || opCode == OpCode.MCOPY;
  }

  public static boolean isBytePricingOpcode(OpCode opCode) {
    return opCode == OpCode.MLOAD
        || opCode == OpCode.MSTORE
        || opCode == OpCode.MSTORE8
        || opCode == OpCode.REVERT
        || opCode == OpCode.RETURN
        || opCode.isCall();
  }

  // This is a copy and past from FrontierGasCalculator.java
  public static long memoryCost(final long length) {
    final long lengthSquare = clampedMultiply(length, length);
    final long base =
        (lengthSquare == Long.MAX_VALUE)
            ? clampedMultiply(length / 512, length)
            : lengthSquare / 512;
    return clampedAdd(clampedMultiply(GAS_CONST_G_MEMORY, length), base);
  }
}
