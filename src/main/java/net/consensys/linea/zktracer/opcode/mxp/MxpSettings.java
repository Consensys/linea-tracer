package net.consensys.linea.zktracer.opcode.mxp;

public record MxpSettings(MxpType type, int gWord, int gByte, int inst) {
  public MxpSettings() {
    this(MxpType.None, 0, 0, 0);
  }
}
