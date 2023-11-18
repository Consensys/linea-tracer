package net.consensys.linea.zktracer.module.add;

import net.consensys.linea.zktracer.types.UnsignedByte;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;


public class UnsignedByteCompressedFileReader extends CompressedFileReader<UnsignedByte> {

  private UnsignedByte currentValue;

  public UnsignedByteCompressedFileReader(RandomAccessFile underlyingFile) {
    super(underlyingFile, 33);
  }

  private void read() throws IOException {
    remainingCurrent = underlyingFile.readInt();
    currentValue = new UnsignedByte(underlyingFile.readByte());
  }

}
