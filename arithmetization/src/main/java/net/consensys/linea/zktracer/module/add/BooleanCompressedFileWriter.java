package net.consensys.linea.zktracer.module.add;

import net.consensys.linea.zktracer.module.FW;

import java.io.IOException;
import java.math.BigInteger;


public class BooleanCompressedFileWriter extends CompressedFileWriter {
  private Boolean previousValue = null;

  public void addBoolean(Boolean b) {
    flush();
    initialize(b);
  }

  @Override
  protected void flush() {
    try {
      fileWriter.writeInt(seenSoFar);
      fileWriter.writeByte(b ? (byte) 1 : (byte) 0);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void initialize(Boolean b) {
    previousValue = b;
    seenSoFar = 1;
  }
}
