package net.consensys.linea.zktracer.module;

import java.io.IOException;
import java.io.RandomAccessFile;


public class BooleanCompressedFileReader extends CompressedFileReader<Boolean> {

  private boolean currentValue;

  public BooleanCompressedFileReader(RandomAccessFile underlyingFile) throws IOException {
    super(underlyingFile, 5);
  }

  @Override
  protected void read() throws IOException {
    remainingCurrent = underlyingFile.readInt();
    currentValue = underlyingFile.readBoolean();
  }

  @Override
  protected Boolean getCurrentValue() {
    return currentValue;
  }


  public boolean nextBoolean() throws IOException {
    if (remainingCurrent == 0) {
      read();
    }
    remainingCurrent -= 1;
    return currentValue;
  }
}
