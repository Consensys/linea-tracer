package net.consensys.linea.zktracer.module;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FW {

    private static final int CHUNK = 100_000;
    private MappedByteBuffer channel;
    private final RandomAccessFile value;
    int currentSize;
    int pos = 0;
    public FW(RandomAccessFile value) {
        this.value=value;
        currentSize = CHUNK;

        try {
           this.channel = value.getChannel().map(FileChannel.MapMode.READ_WRITE,pos, currentSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeShort(short length) throws IOException {
        int prevpos = pos;
        pos+=2;
        if(pos>currentSize){
            currentSize+=CHUNK;
            this.channel = value.getChannel().map(FileChannel.MapMode.READ_WRITE,prevpos, currentSize);
        }
        channel.putShort(length);
    }

    public void write(byte[] bytes2) throws IOException {
        int prevpos = pos;
        pos+=bytes2.length;
        if(pos>currentSize){
            currentSize+=CHUNK;
            this.channel = value.getChannel().map(FileChannel.MapMode.READ_WRITE,prevpos, currentSize);
        }
        channel.put(bytes2);
    }

    public void writeInt(int seenSoFar) throws IOException {
        int prevpos = pos;
        pos+=4;
        if(pos>currentSize){
            currentSize+=CHUNK;
            this.channel = value.getChannel().map(FileChannel.MapMode.READ_WRITE,prevpos, currentSize);
        }
        channel.putInt(seenSoFar);
    }

    public void writeByte(byte aByte) throws IOException {
        int prevpos = pos;
        pos+=1;
        if(pos>currentSize){
            currentSize+=CHUNK;
            this.channel = value.getChannel().map(FileChannel.MapMode.READ_WRITE,prevpos, currentSize);
        }
        channel.put(aByte);
    }

    public RandomAccessFile getFile() {
        return value;
    }
}
