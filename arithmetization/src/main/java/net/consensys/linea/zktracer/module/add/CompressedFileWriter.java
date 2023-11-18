package net.consensys.linea.zktracer.module.add;

import lombok.Data;
import net.consensys.linea.zktracer.module.FW;
import net.consensys.linea.zktracer.types.UnsignedByte;

import java.io.IOException;
import java.math.BigInteger;

public class CompressedFileWriter<T> {
  protected seenSoFar = 0;
  protected final FW fileWriter;

  public void increment() {
    seenSoFar += 1;
  }

  public void close() {
    flush();
    fileWriter.close();
  }

  protected abstract void flush();
}
