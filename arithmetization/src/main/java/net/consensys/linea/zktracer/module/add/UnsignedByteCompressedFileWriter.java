package net.consensys.linea.zktracer.module.add;

import net.consensys.linea.zktracer.module.FW;
import net.consensys.linea.zktracer.types.UnsignedByte;

import java.io.IOException;
import java.math.BigInteger;


public class UnsignedByteCompressedFileWriter extends CompressedFileWriter {
  private UnsignedByte previousValue = null;

  public void addUnsignedByte(UnsignedByte b) {
    flush();
    initialize(b);
  }

  public void flush() {
    try {
      fileWriter.writeInt(compressedFileWriter.getSeenSoFar());
      fileWriter.writeByte(b.toByte());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void initialize(UnsignedByte b) {
    previousValue = b;
    seenSoFar = 1;
  }

}
