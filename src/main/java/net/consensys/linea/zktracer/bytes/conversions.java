package net.consensys.linea.zktracer.bytes;

import java.math.BigInteger;

import org.apache.tuweni.bytes.Bytes;

public class conversions {
  public static Bytes bigIntegerToBytes(BigInteger big) {
    Bytes bytes;
    if (big.equals(BigInteger.ZERO)) {
      bytes = Bytes.of(0x00);
    } else {
      byte[] byteArray;
      byteArray = big.toByteArray();
      if (byteArray[0] == 0) {
        Bytes tmp = Bytes.wrap(byteArray);
        bytes = Bytes.wrap(tmp.slice(1, tmp.size() - 1));
      } else {
        bytes = Bytes.wrap(byteArray);
      }
    }
    return bytes;
  }
}
