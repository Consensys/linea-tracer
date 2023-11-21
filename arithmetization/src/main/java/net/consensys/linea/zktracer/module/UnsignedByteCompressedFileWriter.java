package net.consensys.linea.zktracer.module;

import net.consensys.linea.zktracer.types.UnsignedByte;

import java.io.IOException;
import java.nio.ByteBuffer;


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
//    if(previousValue!=null) {
      try {
//        fileWriter.checkSize(5);
        var bf = ByteBuffer.allocate(5);
        bf.put((byte) (seenSoFar >> 24));
        bf.put((byte) (seenSoFar >> 16));
        bf.put((byte) (seenSoFar >> 8));
        bf.put((byte) seenSoFar);
        bf.put(previousValue.toByte());
        fileWriter.write(bf, 5);
//        fileWriter.writeInt(seenSoFar);
//        fileWriter.writeByte(previousValue.toByte());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
//    }
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
