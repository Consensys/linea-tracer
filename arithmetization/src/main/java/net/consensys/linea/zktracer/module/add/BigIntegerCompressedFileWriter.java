package net.consensys.linea.zktracer.module.add;

import lombok.Data;
import net.consensys.linea.zktracer.module.FW;
import net.consensys.linea.zktracer.types.UnsignedByte;

import java.io.IOException;
import java.math.BigInteger;


public class BigIntegerCompressedFileWriter extends CompressedFileWriter {
  private BigInteger previousValue = null;

  public void addBigInteger(BigInteger b) {
    flush();
    initialize(b);
  }

  @Override
  public void flush() {
    try {
      byte[] bytes2 = getPreviousValue().toByteArray();
      fileWriter.writeShort((short) bytes2.length);
      fileWriter.write(bytes2);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void initialize(BigInteger b) {
    previousValue = b;
    seenSoFar = 1;
  }

}
