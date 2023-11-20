package net.consensys.linea.zktracer.module;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;


public class BigIntegerCompressedFileReader extends CompressedFileReader<BigInteger> {

  private BigInteger currentValue;

  public BigIntegerCompressedFileReader(RandomAccessFile underlyingFile) throws IOException {
    super(underlyingFile);
  }

  @Override
  protected void read() throws IOException {
    remainingCurrent = underlyingFile.readInt();
    short nbBit = underlyingFile.readShort();
    byte[] bigIntBytes = new byte[nbBit];
    int res = underlyingFile.read(bigIntBytes);
    if (res == -1) {
      throw new RuntimeException("Something went wrong while reading big ints...");
    }
    currentValue = new BigInteger(bigIntBytes);
  }

  @Override
  protected BigInteger getCurrentValue() {
    return currentValue;
  }


}
