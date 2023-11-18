package net.consensys.linea.zktracer.module.add;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.Iterator;


public class BigIntegerCompressedFileReader implements CompressedFileReader<BigInteger> {

  private BigInteger currentValue;

  public BigIntegerCompressedFileReader(RandomAccessFile underlyingFile) {
    super(underlyingFile, 33);
  }

  private void read() throws IOException {
    remainingCurrent = underlyingFile.readInt();
    short nbBit = underlyingFile.readShort();
    byte[] bigIntBytes = new byte[nbBit] ();
    int res = underlyingFile.read(bigIntBytes);
    if(res == -1){
      throw new RuntimeException("Something went wrong while reading big ints...");
    }
    currentValue = new BigInteger(bigIntBytes);
  }

  public boolean nextBoolean() {
    if (remainingCurrent == 0) {
      read();
    }
    remainingCurrent -= 1;
    return currentValue;
  }
}
