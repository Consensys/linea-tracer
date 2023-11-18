package net.consensys.linea.zktracer.module.add;

import net.consensys.linea.zktracer.module.FW;
import org.junit.jupiter.api.Assertions;

import java.io.RandomAccessFile;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class BinaryFormatWriterTest {

  public void writeRead1Boolean() {

    var writer = new BooleanCompressedFileWriter(new FW(new RandomAccessFile("RW_BOOLEAN_TEST", "rw")));
    writer.initialize(true);
    writer.close();

    var reader = new BooleanCompressedFileReader(new RandomAccessFile("RW_BOOLEAN_TEST", "r"));

    Assertions.assertTrue(reader.hasNext());
    Assertions.assertEquals(reader.next());
    Assertions.assertFalse(reader.hasNext());
  }

  public void writeRead3Boolean() {
    var writer = new BooleanCompressedFileWriter(new FW(new RandomAccessFile("RW_BOOLEAN_TEST1", "rw")));
    writer.initialize(true);
    writer.addBoolean(true);
    writer.addBoolean(true);
    writer.close();

    var readFile = new RandomAccessFile("RW_BOOLEAN_TEST1", "r");
    Assertions.assertEquals(99, readFile.length());
    var reader = new BooleanCompressedFileReader(readFile);

    Assertions.assertTrue(reader.hasNext());
    Assertions.assertTrue(reader.next());
    Assertions.assertTrue(reader.next());
    Assertions.assertTrue(reader.next());
    Assertions.assertFalse(reader.hasNext());
  }

  public void writeReadBooleans() {

    var writer = new BooleanCompressedFileWriter(new FW(new RandomAccessFile("RW_BOOLEAN_TEST2", "rw")));
    writer.initialize(true);
    writer.addBoolean(false);
    writer.addBoolean(false);
    writer.addBoolean(true);
    writer.addBoolean(true);
    writer.close();

    var readFile = new RandomAccessFile("RW_BOOLEAN_TEST2", "r");
    Assertions.assertEquals(33 * 5, readFile.length());
    var reader = new BooleanCompressedFileReader(readFile);

    Assertions.assertTrue(reader.hasNext());
    Assertions.assertTrue(reader.next());
    Assertions.assertFalse(reader.next());
    Assertions.assertFalse(reader.next());
    Assertions.assertTrue(reader.next());
    Assertions.assertTrue(reader.next());
    Assertions.assertFalse(reader.hasNext());
  }


  public void writeReadByte() {
    var writer = new UnsignedByteCompressedFileWriter(new FW(new RandomAccessFile("RW_BYTE_TEST1", "rw")));
    writer.close();

    var readFile = new RandomAccessFile("RW_BYTE_TEST1", "r");
    Assertions.assertEquals(0, readFile.length());
    readFile.close();

    var writer = new UnsignedByteCompressedFileWriter(new FW(new RandomAccessFile("RW_BYTE_TEST1", "rw")));
    writer.addUnsignedByte(new UnsignedByte((short) 1));
    writer.addUnsignedByte(new UnsignedByte((short) 1));
    writer.addUnsignedByte(new UnsignedByte((short) 1));
    writer.close();
    var readFile = new RandomAccessFile("RW_BYTE_TEST1", "r");
    Assertions.assertEquals(33, readFile.length());
    var reader = new UnsignedByteCompressedFileReader(readFile);

    Assertions.assertTrue(reader.hasNext());
    Assertions.assertEquals(1, reader.next().toInteger())
    Assertions.assertEquals(1, reader.next().toInteger())
    Assertions.assertEquals(1, reader.next().toInteger())
    Assertions.assertFalse(reader.next());
  }

  public void writeRead4Byte() {
    var writer = new UnsignedByteCompressedFileWriter(new FW(new RandomAccessFile("RW_BYTE_TEST", "rw")));
    writer.addUnsignedByte(new UnsignedByte((short) 1));
    writer.addUnsignedByte(new UnsignedByte((short) 0));
    writer.addUnsignedByte(new UnsignedByte((short) 0));
    writer.addUnsignedByte(new UnsignedByte((short) 1));
    writer.close();
    var readFile = new RandomAccessFile("RW_BYTE_TEST", "r");
    Assertions.assertEquals(33 * 3, readFile.length());
    var reader = new UnsignedByteCompressedFileReader(readFile);

    Assertions.assertTrue(reader.hasNext());
    Assertions.assertEquals(1, reader.next().toInteger())
    Assertions.assertEquals(0, reader.next().toInteger())
    Assertions.assertEquals(0, reader.next().toInteger())
    Assertions.assertEquals(1, reader.next().toInteger())
    Assertions.assertFalse(reader.next());
  }


  public void writeReadBigInteger() {
    var writer = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile("RW_BIG_INTEGER_TEST1", "rw")));
    writer.close();

    var readFile = new RandomAccessFile("RW_BIG_INTEGER1", "r");
    Assertions.assertEquals(0, readFile.length());
    readFile.close();

    var writer = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile("RW_BIG_INTEGER_TEST1", "rw")));
    writer.addBigInteger(BigInteger.ONE);
    writer.addBigInteger(BigInteger.valueOf(111_111_222));
    writer.addBigInteger(BigInteger.ONE);
    writer.close();
    var readFile = new RandomAccessFile("RW_BIG_INTEGER_TEST1", "r");
    Assertions.assertEquals(22*8, readFile.length());
    var reader = new BigIntegerCompressedFileReader(readFile);

    Assertions.assertTrue(reader.hasNext());
    Assertions.assertEquals(BigInteger.ONE, reader.next())
    Assertions.assertEquals(BigInteger.valueOf(111_111_222), reader.next())
    Assertions.assertEquals(BigInteger.ONE, reader.next())
    Assertions.assertFalse(reader.next());
  }

  public void writeRead4BigInteger() {
    var writer = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile("RW_BIG_INTEGER_TEST", "rw")));
    writer.addBigInteger(BigInteger.111);
    writer.addBigInteger(BigInteger.111_111);
    writer.addBigInteger(BigInteger.111_111_222_333);
    writer.addBigInteger(BigInteger.111_111_222_333_444);
    writer.close();
    var readFile = new RandomAccessFile("RW_BIG_INTEGER_TEST", "r");
    var reader = new BigIntegerCompressedFileReader(readFile);

    Assertions.assertTrue(reader.hasNext());
    Assertions.assertEquals(BigInteger.111, reader.next())
    Assertions.assertEquals(BigInteger.111_111, reader.next())
    Assertions.assertEquals(BigInteger.111_111_222_333, reader.next())
    Assertions.assertEquals(BigInteger.111_111_222_333_444, reader.next())
    Assertions.assertFalse(reader.next());
  }

  public void writeReadLogOfBigInteger() {
    var writer = new BigIntegerCompressedFileWriter(
            new FW(new RandomAccessFile("RW_LO_BIG_INTEGER_TEST", "rw")), 100);
    for(int i = 0; i< 100; i++){
      writer.addBigInteger(BigInteger.valueOf(i *10));
    }
    writer.close();
    var readFile = new RandomAccessFile("RW__LO_BIG_INTEGER_TEST", "r");
    var reader = new BigIntegerCompressedFileReader(readFile);

    Assertions.assertTrue(reader.hasNext());
    for(int i = 0; i< 100; i++){
      Assertions.assertEquals(BigInteger.valueOf(i *10), reader.next());
    }
    Assertions.assertFalse(reader.next());
  }



}