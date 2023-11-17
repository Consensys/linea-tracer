package net.consensys.linea.zktracer.module.add;

import lombok.Data;
import net.consensys.linea.zktracer.module.FW;
import net.consensys.linea.zktracer.types.UnsignedByte;

import java.io.IOException;
import java.math.BigInteger;


@Data
public class CompressedFileWriter<T> {
    T previousValue = null;
    public int lastIndex = 0;
    int seenSoFar = 0;

    private final FW fileWriter;
    public void increment() {
        seenSoFar+=1;
    }

    public void close() throws IOException {
        byte[] bytes2 = getByte(previousValue);
        fileWriter.writeShort((short)bytes2.length);
        fileWriter.write(bytes2);
    }

    public byte[] getByte(T previousValue) {
        if(previousValue instanceof BigInteger){
            return ((BigInteger)previousValue).toByteArray();
        }
        else if(previousValue instanceof Boolean){
            return new byte[]{((Boolean) previousValue) ? (byte) 1 : (byte) 0};
        }
        else return new byte[]{((UnsignedByte) previousValue).toByte()};
    }

    public void initialize(T b) {
        previousValue = b;
        seenSoFar = 1;
    }

    public void writeInt(int seenSoFar) throws IOException {
        fileWriter.writeInt(seenSoFar);
    }

    public void writeByte(byte aByte) throws IOException {
        fileWriter.writeByte(aByte);
    }

    public void writeShort(short length) throws IOException {
        fileWriter.writeShort(length);
    }

    public void write(byte[] bytes2) throws IOException {
        fileWriter.write(bytes2);
    }
}
