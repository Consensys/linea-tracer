package net.consensys.linea.zktracer.opcode;

public record RamSettings(boolean enabled, DataLocation source, DataLocation target) {
  public RamSettings() {
    this(false, DataLocation.None, DataLocation.None);
  }
}
