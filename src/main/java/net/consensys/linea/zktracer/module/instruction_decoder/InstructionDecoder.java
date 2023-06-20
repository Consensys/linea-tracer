package net.consensys.linea.zktracer.module.instruction_decoder;

import java.util.HashMap;
import java.util.Map;

import net.consensys.linea.zktracer.OpCode;

public class InstructionDecoder {
  static Map<OpCode, Map<String, Integer>> instructionDecoder = new HashMap<>();

  static {
    //        instructionDecoder.put(OpCode.MSIZE, new HashMap<>());
    //        instructionDecoder.get(OpCode.MSIZE).put(Columns.MXP_TYPE_1,1);
    instructionDecoder.put(OpCode.MLOAD, new HashMap<>());
    instructionDecoder.get(OpCode.MLOAD).put(Columns.MXP_TYPE_2, 1);
    instructionDecoder.put(OpCode.MSTORE, new HashMap<>());
    instructionDecoder.get(OpCode.MSTORE).put(Columns.MXP_TYPE_2, 1);
    instructionDecoder.put(OpCode.MSTORE8, new HashMap<>());
    instructionDecoder.get(OpCode.MSTORE8).put(Columns.MXP_TYPE_3, 1);
  }

  public static Integer get(final OpCode opCode, final String instructionName) {
    if (instructionDecoder.containsKey(opCode)) {
      return instructionDecoder.get(opCode).getOrDefault(instructionName, 0);
    } else {
      return 0;
    }
  }
}
