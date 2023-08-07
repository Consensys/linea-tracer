package net.consensys.linea.zktracer.opcode.mxp;

import net.consensys.linea.zktracer.opcode.Gas;

/** The unit used to bill the gas usage of an instruction */
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

  /**
   * Create a billing scheme only dependent on the {@link Mxp}
   *
   * @param type the MXP type
   * @return the billing scheme
   */
  public static Billing byMxp(MxpType type) {
    return new Billing(null, null, type);
  }

  /**
   * Create a per-word billing scheme
   *
   * @param type the MXP type
   * @param wordPrice gas cost of a word
   * @return the billing scheme
   */
  public static Billing byWord(MxpType type, Gas wordPrice) {
    return new Billing(wordPrice, BillingRate.ByWord, type);
  }

  /**
   * Create a per-byte billing scheme
   *
   * @param type the MXP type
   * @param bytePrice gas cost of a byte
   * @return the billing scheme
   */
  public static Billing byByte(MxpType type, Gas bytePrice) {
    return new Billing(bytePrice, BillingRate.ByByte, type);
  }
}
