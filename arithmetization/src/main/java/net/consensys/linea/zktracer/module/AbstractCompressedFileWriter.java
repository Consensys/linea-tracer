package net.consensys.linea.zktracer.module;

public abstract class AbstractCompressedFileWriter<T> {
  protected int seenSoFar = 0;
  protected final FW fileWriter;

  public AbstractCompressedFileWriter(FW fileWriter) {
    this.fileWriter = fileWriter;
  }

  public void increment() {
    seenSoFar += 1;
  }

  public void close() {
    flush();
    try {
      fileWriter.close();
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  protected abstract void flush();
}
