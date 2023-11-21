package net.consensys.linea.zktracer.module;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class FW {

  private final int chunkSize;
  protected MappedByteBuffer channel;
  private final RandomAccessFile value;
  int currentSize;
  int pos = 0;

  public FW(RandomAccessFile value) {
    this(value, 100_000_000);
  }

  public FW(RandomAccessFile value, int chunkSize) {
    this.value = value;
    this.chunkSize = chunkSize;

    try {
      this.channel = value.getChannel().map(FileChannel.MapMode.READ_WRITE, pos, chunkSize);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void writeShort(short length) throws IOException {
//    int prevpos = pos;
//    pos += 2;
//    if (pos > currentSize) {
//      log.info("[TRACING] Expanding file");
//      currentSize += chunkSize;
//      this.channel = value.getChannel().map(FileChannel.MapMode.READ_WRITE, prevpos, currentSize);
//    }
    channel.putShort(length);
  }

  public void write(byte[] bytes2) throws IOException {
//    int prevpos = pos;
//    pos += bytes2.length;
//    if (pos > currentSize) {
//      currentSize += chunkSize;
//      this.channel = value.getChannel().map(FileChannel.MapMode.READ_WRITE, prevpos, currentSize);
//    }
    channel.put(bytes2);
  }

  public void checkSize(int pos) throws IOException {
//    int prevpos = pos;
    this.pos += pos;
//    if (pos > currentSize) {
//      currentSize += chunkSize;
//      this.channel = value.getChannel().map(FileChannel.MapMode.READ_WRITE, prevpos, currentSize);
//    }
  }
  public void writeInt(int seenSoFar) throws IOException {
//    int prevpos = pos;
//    pos += 4;
//    if (pos > currentSize) {
//      currentSize += chunkSize;
//      this.channel = value.getChannel().map(FileChannel.MapMode.READ_WRITE, prevpos, currentSize);
//    }
    channel.putInt(seenSoFar);
  }

  public void writeByte(byte aByte) throws IOException {
//    int prevpos = pos;
//    pos += 1;
//    if (pos > currentSize) {
//      currentSize += chunkSize;
//      this.channel = value.getChannel().map(FileChannel.MapMode.READ_WRITE, prevpos, currentSize);
//    }
    channel.put(aByte);
  }

  public RandomAccessFile getFile() {
    return value;
  }

  public void close() throws IOException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    channel.force();
    Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
    Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
    unsafeField.setAccessible(true);
    Object unsafe = unsafeField.get(null);
    Method invokeCleaner = unsafeClass.getMethod("invokeCleaner", ByteBuffer.class);
    invokeCleaner.invoke(unsafe, channel);
    value.setLength(pos);
  }

  public void write(ByteBuffer bf, int l) throws IOException {
//    int prevpos = pos;
//    pos += l;
//    if (pos > currentSize) {
//      currentSize += chunkSize;
//      this.channel = value.getChannel().map(FileChannel.MapMode.READ_WRITE, prevpos, currentSize);
//    }
    this.pos+=l;
    channel.put(bf);
  }
}
