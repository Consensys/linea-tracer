package net.consensys.linea.zktracer.module;

import java.io.IOException;


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
        fileWriter.writeInt(seenSoFar);
        fileWriter.writeByte(previousValue ? (byte) 1 : (byte) 0);
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
