package net.consensys.linea.zktracer.opcode.mxp;

public record MxpSettings(MxpType type, Billing billing) {
  public MxpSettings() {
    this(MxpType.None, null);
  }
}
