package net.consensys.linea.zktracer.module.oob;

enum ModexpXbsCase {
  NONE,
  OOB_INST_MODEXP_BBS,
  OOB_INST_MODEXP_EBS,
  OOB_INST_MODEXP_MBS;

  public ModexpXbsCase next() {
    // Values() returns an array containing all the values of the enum in order
    // this.ordinal() returns the ordinal of this enum constant (its position in its enum
    // declaration)
    return values()[(this.ordinal() + 1) % values().length];
  }
}
