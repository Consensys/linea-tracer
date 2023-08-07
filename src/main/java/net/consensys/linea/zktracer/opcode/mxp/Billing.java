package net.consensys.linea.zktracer.opcode.mxp;

import net.consensys.linea.zktracer.opcode.Gas;

/** Represents the unit used to bill the gas usage of an instruction */
enum BillingRate {
  ByWord,
  ByByte,
}

/**
 * An ancillary class to compute gas billing of some instructions
 *
 * @param perUnit gas cost of a unit
 * @param billingRate the unit used to bill gas
 */
public record Billing(Gas perUnit, BillingRate billingRate, MxpType type) {
  public Billing() {
    this(null, null, MxpType.None);
  }

  public static Billing byMxp(MxpType type) {
    return new Billing(null, null, type);
  }

  public static Billing byWord(MxpType type, Gas perUnit) {
    return new Billing(perUnit, BillingRate.ByWord, type);
  }

  public static Billing byByte(MxpType type, Gas perUnit) {
    return new Billing(perUnit, BillingRate.ByByte, type);
  }
}
