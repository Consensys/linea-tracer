package net.consensys.linea.zktracer.module;

import com.google.common.primitives.Ints;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;


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
//    if (previousValue != null) {
//      log.info("Flushing Big Integer nbSeen:{}, initialValue::{}, delta:{}", seenSoFar, initialValue, delta);
      try {
        byte[] bytes2 = delta.toByteArray();
        var l = 36;
         var bf = ByteBuffer.allocate(l);
//        fileWriter.writeInt(seenSoFar);
//        fileWriter.writeShort((short) bytes2.length);
//        fileWriter.write(bytes2);
        bf.put((byte) (seenSoFar >> 24));
        bf.put((byte) (seenSoFar >> 16));
        bf.put((byte) (seenSoFar >> 8));
        bf.put((byte) seenSoFar);
//        bf.putShort((short) bytes2.length);
        byte[] bytes = new byte[32];
        System.arraycopy(bytes2, 0, bytes, 32-bytes2.length, bytes2.length);
        bf.put(bytes);
        fileWriter.write(bf, l);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
//    }
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
      if (previousValue.equals(b)) {
        increment();
      } else {
        addBigInteger(b, b);
      }
    }
  }
//
//  public void processBigInteger(BigInteger b) {
//    if (initialValue == null) {
//      initialize(b, b);
//    } else {
//      var newDelta = b.add(previousValue.negate());
//      if (delta.equals(newDelta)) {
//        increment();
//        previousValue = b;
//      } else {
//        addBigInteger(b, newDelta);
//      }
//    }
//  }
}
