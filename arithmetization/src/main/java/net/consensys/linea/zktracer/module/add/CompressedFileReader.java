package net.consensys.linea.zktracer.module.add;

import java.io.IOException;
import java.io.RandomAccessFile;


public class CompressedFileReader<T> implements Iterator<T> {

  protected int remainingCurrent;
  protected final RandomAccessFile underlyingFile;
  protected final int length;

  public CompressedFileReader(RandomAccessFile underlyingFile, int chunkSize) {
    this(validateLength(underlyingFile));
  }

  private static RandomAccessFile validateLength(RandomAccessFile underlyingFile, int chunkSize) {
    if (length % chunkSize != 0) {
      throw new RuntimeException("Invalid file length for " + underlyingFile + " not a multiple of "
              + chunkSize + " while it's an UnsignedByte. Length is " + length);
    }
    return underlyingFile;
  }

  public CompressedFileReader(RandomAccessFile underlyingFile) {
    length = underlyingFile.length();

    this.underlyingFile = underlyingFile;

    if (underlyingFile.length() == 0) {
      remainingCurrent = 0;
    } else {
      read(underlyingFile);
    }
  }

  protected abstract void read() throws IOException;

  @Override
  public boolean hasNext() {
    return remainingCurrent == 0 & underlyingFile.getFilePointer() >= length;
  }

  @Override
  public T next() {
    if(remainingCurrent == 0){
      read();
    }
    remainingCurrent -= 1;
    return getCurrentValue();
  }

  abstract protected T getCurrentValue();
}
