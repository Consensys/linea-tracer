package net.consensys.linea.zktracer.module;

import java.io.IOException;
import java.math.BigInteger;


public class BigIntegerCompressedFileWriter extends AbstractCompressedFileWriter<BigInteger> {
  private BigInteger previousValue = null;

  public BigIntegerCompressedFileWriter(FW fileWriter) {
    super(fileWriter);
  }

  public void addBigInteger(BigInteger b) {
    flush();
    initialize(b);
  }

  @Override
  public void flush() {
    if(previousValue!=null) {
      try {
        fileWriter.writeInt(seenSoFar);
        byte[] bytes2 = previousValue.toByteArray();
        fileWriter.writeShort((short) bytes2.length);
        fileWriter.write(bytes2);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void initialize(BigInteger b) {
    previousValue = b;
    seenSoFar = 1;
  }

  public BigInteger getPreviousValue() {
    return previousValue;
  }

  public void processBigInteger(BigInteger b) {
    if (getPreviousValue() == null) {
      initialize(b);
    } else if (getPreviousValue().equals(b)) {
      increment();
    } else {
      addBigInteger(b);
    }
  }
}
