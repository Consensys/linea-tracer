package net.consensys.linea.zktracer.module.add;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;


public class BooleanCompressedFileReader implements CompressedFileReader<Boolean> {

  private boolean currentValue;

  public BooleanCompressedFileReader(RandomAccessFile underlyingFile) {
    super(underlyingFile, 33);
  }

  private void read() throws IOException {
    remainingCurrent = underlyingFile.readInt();
    currentValue = underlyingFile.readBoolean();
  }


  public boolean nextBoolean() {
    if (remainingCurrent == 0) {
      read();
    }
    remainingCurrent -= 1;
    return currentValue;
  }
}
