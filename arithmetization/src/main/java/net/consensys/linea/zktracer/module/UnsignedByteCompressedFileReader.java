package net.consensys.linea.zktracer.module;

import net.consensys.linea.zktracer.types.UnsignedByte;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;


public class UnsignedByteCompressedFileReader extends CompressedFileReader<UnsignedByte> {

  private UnsignedByte currentValue;

  public UnsignedByteCompressedFileReader(RandomAccessFile underlyingFile) throws IOException {
    super(underlyingFile, 5);
  }

  @Override
  protected void read() throws IOException {
    remainingCurrent = underlyingFile.readInt();
    currentValue = UnsignedByte.of(underlyingFile.readByte());
  }

  @Override
  protected UnsignedByte getCurrentValue() {
    return currentValue;
  }

}
