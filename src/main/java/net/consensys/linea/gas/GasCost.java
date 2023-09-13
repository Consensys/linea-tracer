package net.consensys.linea.gas;

import net.consensys.linea.zktracer.opcode.OpCode;

public interface GasCost {
  default long staticGas() {return 0;}
  default long memoryExpansion() {return 0;}
  default long accountWarmth() {return 0;}
  default long accountCreation() {return 0;}
  default long transferValue() {return 0;}
  default long linearPerWord() {return 0;}
  default long linearPerByte() {return 0;}
  default long sStoreWarmth() {return 0;}
  default long sStoreValue() {return 0;}
}
