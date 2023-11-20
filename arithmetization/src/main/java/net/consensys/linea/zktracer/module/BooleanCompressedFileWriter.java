package net.consensys.linea.zktracer.module;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class BooleanCompressedFileWriter extends AbstractCompressedFileWriter<Boolean> {
  private Boolean previousValue = null;

  public BooleanCompressedFileWriter(FW fileWriter) {
    super(fileWriter);
  }

  public void addBoolean(Boolean b) {
    flush();
    initialize(b);
  }

  @Override
  protected void flush() {
    if(previousValue!=null) {
      try {
        var bf = ByteBuffer.allocate(5);
        bf.put((byte) (seenSoFar >> 24));
        bf.put((byte) (seenSoFar >> 16));
        bf.put((byte) (seenSoFar >> 8));
        bf.put((byte) seenSoFar);
        bf.put(previousValue ? (byte) 1 : (byte) 0);
        fileWriter.write(bf, 5);
//        fileWriter.checkSize(5);
//        fileWriter.channel.putInt(seenSoFar);
//        fileWriter.channel.put(previousValue ? (byte) 1 : (byte) 0);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void initialize(Boolean b) {
    previousValue = b;
    seenSoFar = 1;
  }

  public Boolean getPreviousValue() {
    return previousValue;
  }

  public void processBoolean(Boolean b) {
    if (getPreviousValue() == null) {
      initialize(b);
    } else if (getPreviousValue().equals(b)) {
      increment();
    } else {
      addBoolean(b);
    }
  }
}
