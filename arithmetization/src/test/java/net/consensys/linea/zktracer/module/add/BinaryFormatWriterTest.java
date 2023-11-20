package net.consensys.linea.zktracer.module.add;

import net.consensys.linea.zktracer.module.*;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;

public class BinaryFormatWriterTest {

  @Test
  public void writeRead1Boolean() throws IOException {

    var writer = new BooleanCompressedFileWriter(new FW(new RandomAccessFile("RW_BOOLEAN_TEST", "rw")));
    writer.initialize(true);
    writer.close();

    var reader = new BooleanCompressedFileReader(new RandomAccessFile("RW_BOOLEAN_TEST", "r"));

    Assertions.assertTrue(reader.hasNext());
    Assertions.assertEquals(true, reader.next());
    Assertions.assertFalse(reader.hasNext());
  }

  @Test
  public void writeRead3Boolean() throws IOException {
    var writer = new BooleanCompressedFileWriter(new FW(new RandomAccessFile("RW_BOOLEAN_TEST1", "rw")));
    writer.processBoolean(true);
    writer.processBoolean(true);
    writer.processBoolean(true);
    writer.close();

    var readFile = new RandomAccessFile("RW_BOOLEAN_TEST1", "r");
    Assertions.assertEquals(5, readFile.length());
    var reader = new BooleanCompressedFileReader(readFile);

    Assertions.assertTrue(reader.hasNext());
    Assertions.assertTrue(reader.next());
    Assertions.assertTrue(reader.next());
    Assertions.assertTrue(reader.next());
    Assertions.assertFalse(reader.hasNext());
  }
  @Test
  public void writeReadBooleans() throws IOException {

    var writer = new BooleanCompressedFileWriter(new FW(new RandomAccessFile("RW_BOOLEAN_TEST2", "rw")));
    writer.processBoolean(true);
    writer.processBoolean(false);
    writer.processBoolean(false);
    writer.processBoolean(true);
    writer.processBoolean(true);
    writer.close();

    var readFile = new RandomAccessFile("RW_BOOLEAN_TEST2", "r");
    Assertions.assertEquals(15, readFile.length());
    var reader = new BooleanCompressedFileReader(readFile);

    Assertions.assertTrue(reader.hasNext());
    Assertions.assertTrue(reader.next());
    Assertions.assertFalse(reader.next());
    Assertions.assertFalse(reader.next());
    Assertions.assertTrue(reader.next());
    Assertions.assertTrue(reader.next());
    Assertions.assertFalse(reader.hasNext());
  }

  @Test
  public void writeReadByte() throws IOException {
    var writer = new UnsignedByteCompressedFileWriter(new FW(new RandomAccessFile("RW_BYTE_TEST1", "rw")));
    writer.close();

    var readFile = new RandomAccessFile("RW_BYTE_TEST1", "r");
    Assertions.assertEquals(0, readFile.length());
    readFile.close();

    writer = new UnsignedByteCompressedFileWriter(new FW(new RandomAccessFile("RW_BYTE_TEST1", "rw")));
    writer.processUnsignedByte(UnsignedByte.of((short) 1));
    writer.processUnsignedByte(UnsignedByte.of((short) 1));
    writer.processUnsignedByte(UnsignedByte.of((short) 1));
    writer.close();
    readFile = new RandomAccessFile("RW_BYTE_TEST1", "r");
    Assertions.assertEquals(5, readFile.length());
    var reader = new UnsignedByteCompressedFileReader(readFile);

    Assertions.assertTrue(reader.hasNext());
    Assertions.assertEquals(1, reader.next().toInteger());
    Assertions.assertEquals(1, reader.next().toInteger());
    Assertions.assertEquals(1, reader.next().toInteger());
    Assertions.assertFalse(reader.hasNext());
  }

  @Test
  public void writeRead4Byte() throws IOException {
    var writer = new UnsignedByteCompressedFileWriter(new FW(new RandomAccessFile("RW_BYTE_TEST", "rw")));
    writer.processUnsignedByte(UnsignedByte.of((short) 1));
    writer.processUnsignedByte(UnsignedByte.of((short) 0));
    writer.processUnsignedByte(UnsignedByte.of((short) 0));
    writer.processUnsignedByte(UnsignedByte.of((short) 1));
    writer.close();
    var readFile = new RandomAccessFile("RW_BYTE_TEST", "r");
    Assertions.assertEquals(5 * 3, readFile.length());
    var reader = new UnsignedByteCompressedFileReader(readFile);

    Assertions.assertTrue(reader.hasNext());
    Assertions.assertEquals(1, reader.next().toInteger());
    Assertions.assertEquals(0, reader.next().toInteger());
    Assertions.assertEquals(0, reader.next().toInteger());
    Assertions.assertEquals(1, reader.next().toInteger());
    Assertions.assertFalse(reader.hasNext());
  }

  @Test
  public void writeReadBigInteger() throws IOException {
    var writer = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile("RW_BIG_INTEGER_TEST1", "rw")));
    writer.close();

    var readFile = new RandomAccessFile("RW_BIG_INTEGER_TEST1", "r");
    Assertions.assertEquals(0, readFile.length());
    readFile.close();

    writer = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile("RW_BIG_INTEGER_TEST1", "rw")));
    writer.processBigInteger(BigInteger.ONE);
    writer.processBigInteger(BigInteger.valueOf(111_111_222));
    writer.processBigInteger(BigInteger.ONE);
    writer.close();
    readFile = new RandomAccessFile("RW_BIG_INTEGER_TEST1", "r");
    var reader = new BigIntegerCompressedFileReader(readFile);

    Assertions.assertTrue(reader.hasNext());
    Assertions.assertEquals(BigInteger.ONE, reader.next());
    Assertions.assertEquals(BigInteger.valueOf(111_111_222), reader.next());
    Assertions.assertEquals(BigInteger.ONE, reader.next());
    Assertions.assertFalse(reader.hasNext());
  }

  @Test
  public void writeRead4BigInteger() throws IOException {
    var writer = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile("RW_BIG_INTEGER_TEST", "rw")));
    writer.processBigInteger(BigInteger.valueOf(111));
    writer.processBigInteger(BigInteger.valueOf(111_111));
    writer.processBigInteger(BigInteger.valueOf(111_111_222_333L));
    writer.processBigInteger(BigInteger.valueOf(111_111_222_333_444L));
    writer.close();
    var readFile = new RandomAccessFile("RW_BIG_INTEGER_TEST", "r");
    var reader = new BigIntegerCompressedFileReader(readFile);

    Assertions.assertTrue(reader.hasNext());
    Assertions.assertEquals(BigInteger.valueOf(111), reader.next());
    Assertions.assertEquals(BigInteger.valueOf(111_111), reader.next());
    Assertions.assertEquals(BigInteger.valueOf(111_111_222_333L), reader.next());
    Assertions.assertEquals(BigInteger.valueOf(111_111_222_333_444L), reader.next());
    Assertions.assertFalse(reader.hasNext());
  }

  @Test
  public void writeReadLogOfBigInteger() throws IOException {
    var writer = new BigIntegerCompressedFileWriter(
            new FW(new RandomAccessFile("RW_LO_BIG_INTEGER_TEST", "rw"), 100));
    for(int i = 0; i< 100; i++){
      writer.processBigInteger(BigInteger.valueOf(i *10));
    }
    writer.close();
    var readFile = new RandomAccessFile("RW__LO_BIG_INTEGER_TEST", "r");
    var reader = new BigIntegerCompressedFileReader(readFile);

    Assertions.assertTrue(reader.hasNext());
    for(int i = 0; i< 100; i++){
      Assertions.assertEquals(BigInteger.valueOf(i *10), reader.next());
    }
    Assertions.assertFalse(reader.hasNext());
  }



}