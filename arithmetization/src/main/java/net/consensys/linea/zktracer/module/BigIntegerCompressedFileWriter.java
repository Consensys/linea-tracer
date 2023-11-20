package net.consensys.linea.zktracer.module;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigInteger;


@Slf4j
public class BigIntegerCompressedFileWriter extends AbstractCompressedFileWriter<BigInteger> {
  private BigInteger previousValue = null;
  private BigInteger initialValue = null;
  private BigInteger delta = null;

  public BigIntegerCompressedFileWriter(FW fileWriter) {
    super(fileWriter);
  }

  public void addBigInteger(BigInteger b, BigInteger newDelta) {
    flush();
    initialize(b, newDelta);
  }


  @Override
  public void flush() {
    if (previousValue != null) {
//      log.info("Flushing Big Integer nbSeen:{}, initialValue::{}, delta:{}", seenSoFar, initialValue, delta);
      try {
        fileWriter.writeInt(seenSoFar);
        byte[] bytes2 = delta.toByteArray();
        fileWriter.writeShort((short) bytes2.length);
        fileWriter.write(bytes2);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private void initialize(BigInteger b, BigInteger newDelta) {
    initialValue = b;
    previousValue = b;
    delta = newDelta;
    seenSoFar = 1;
  }

  public void processBigInteger(BigInteger b) {
    if (initialValue == null) {
      initialize(b, b);
    } else {
      var newDelta = b.add(previousValue.negate());
      if (delta.equals(newDelta)) {
        increment();
        previousValue = b;
      } else {
        addBigInteger(b, newDelta);
      }
    }
  }
}
