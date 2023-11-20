package net.consensys.linea.zktracer.module;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;

public abstract class CompressedFileReader<T> implements Iterator<T> {

  protected int remainingCurrent;
  protected final RandomAccessFile underlyingFile;
  protected final long length;

  public CompressedFileReader(RandomAccessFile underlyingFile, int chunkSize) throws IOException {
    this(validateLength(underlyingFile, chunkSize));
  }

  private static RandomAccessFile validateLength(RandomAccessFile underlyingFile, int chunkSize) throws IOException {
    var length = underlyingFile.length();
    if (length % chunkSize != 0) {
      throw new RuntimeException("Invalid file length for " + underlyingFile + " not a multiple of "
              + chunkSize + " while it's an UnsignedByte. Length is " + length);
    }
    return underlyingFile;
  }

  public CompressedFileReader(RandomAccessFile underlyingFile) throws IOException {
    length = underlyingFile.length();

    this.underlyingFile = underlyingFile;

    if (underlyingFile.length() == 0) {
      remainingCurrent = 0;
    } else {
      read();
    }
  }

  protected abstract void read() throws IOException;

  @Override
  public boolean hasNext() {
    try {
      return remainingCurrent > 0 || underlyingFile.getFilePointer() < length;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public T next() {
    if(remainingCurrent == 0){
      try {
        read();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    remainingCurrent -= 1;
    return getCurrentValue();
  }

  abstract protected T getCurrentValue();
}
