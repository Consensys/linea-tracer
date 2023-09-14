package net.consensys.linea.zktracer.opcode.gas;

public interface GasCost {
  default long staticGas() {
    return 0;
  }

  default long expGas() {
    return 0;
  }

  default long memoryExpansion() {
    return 0;
  }

  default long accountAccess() {
    return 0;
  }

  default long accountCreation() {
    return 0;
  }

  default long transferValue() {
    return 0;
  }

  default long linearPerWord() {
    return 0;
  }

  default long linearPerByte() {
    return 0;
  }

  default long storageWarmth() {
    return 0;
  }

  default long sStoreValue() {
    return 0;
  }

  default long rawStipend() {
    return 0;
  }

  default long extraStipend() {
    return 0;
  }

  default long codeReturn() {
    return 0;
  }

  default long refund() {
    return 0;
  }
}
