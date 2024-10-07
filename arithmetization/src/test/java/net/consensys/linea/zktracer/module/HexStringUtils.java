package net.consensys.linea.zktracer.module;

import java.math.BigInteger;

public class HexStringUtils {

  /**
   * Shifts the given hexadecimal value to the right by the specified number of bits.
   *
   * @param value the hexadecimal value to be shifted
   * @param n the number of bits to shift to the right
   * @return the resulting hexadecimal value after the shift
   */
  public static String rightShift(String value, int n) {
    return new BigInteger(value, 16).shiftRight(n).toString(16);
  }

  /**
   * Shifts the given hexadecimal value to the left by the specified number of bits.
   *
   * @param value the hexadecimal value to be shifted
   * @param n the number of bits to shift to the left
   * @return the resulting hexadecimal value after the shift
   */
  public static String leftShift(String value, int n) {
    return new BigInteger(value, 16).shiftLeft(n).toString(16);
  }

  /**
   * Performs a bitwise AND operation on the given hexadecimal value and mask.
   *
   * @param value the hexadecimal value
   * @param mask the hexadecimal mask
   * @return the resulting hexadecimal value after the AND operation
   */
  public static String and(String value, String mask) {
    return new BigInteger(value, 16).and(new BigInteger(mask, 16)).toString(16);
  }

  /**
   * Performs a bitwise XOR operation on the given hexadecimal value and mask.
   *
   * @param value the hexadecimal value
   * @param mask the hexadecimal mask
   * @return the resulting hexadecimal value after the XOR operation
   */
  public static String xor(String value, String mask) {
    return new BigInteger(value, 16).xor(new BigInteger(mask, 16)).toString(16);
  }

  /**
   * Generates a hexadecimal even value with the specified number of trailing zeros (other digits
   * are ones).
   *
   * @param nTrailingZeros the number of trailing zeros
   * @return the resulting hexadecimal value
   */
  public static String even(int nTrailingZeros) {
    return new BigInteger("1".repeat(256 - nTrailingZeros) + "0".repeat(nTrailingZeros), 2)
        .toString(16);
  }

  /**
   * Generates a hexadecimal odd value with the specified number of trailing ones (other digits are
   * zeros).
   *
   * @param nTrailingOnes the number of trailing ones
   * @return the resulting hexadecimal value
   */
  public static String odd(int nTrailingOnes) {
    return new BigInteger("0".repeat(256 - nTrailingOnes) + "1".repeat(nTrailingOnes), 2)
        .toString(16);
  }
}
