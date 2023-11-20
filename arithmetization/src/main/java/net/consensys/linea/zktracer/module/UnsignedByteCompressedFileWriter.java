package net.consensys.linea.zktracer.module;

import net.consensys.linea.zktracer.types.UnsignedByte;

import java.io.IOException;


public class UnsignedByteCompressedFileWriter extends AbstractCompressedFileWriter<UnsignedByte> {
  private UnsignedByte previousValue = null;

  public UnsignedByteCompressedFileWriter(FW fileWriter) {
    super(fileWriter);
  }

  public void addUnsignedByte(UnsignedByte b) {
    flush();
    initialize(b);
  }

  public void flush() {
    if(previousValue!=null) {
      try {
        fileWriter.writeInt(seenSoFar);
        fileWriter.writeByte(previousValue.toByte());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void initialize(UnsignedByte b) {
    previousValue = b;
    seenSoFar = 1;
  }

  public UnsignedByte getPreviousValue() {
    return previousValue;
  }

  public void processUnsignedByte(UnsignedByte b) {
    if (getPreviousValue() == null) {
      initialize(b);
    } else if (getPreviousValue().equals(b)) {
      increment();
    } else {
      addUnsignedByte(b);
    }
  }
}
