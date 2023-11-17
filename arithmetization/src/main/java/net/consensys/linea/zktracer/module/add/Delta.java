package net.consensys.linea.zktracer.module.add;

import lombok.Data;
import net.consensys.linea.zktracer.types.UnsignedByte;

import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static net.consensys.linea.zktracer.module.add.TraceBuilder.getByteArray;

@Data
public class Delta<T> {
    T previousValue = null;
    public int lastIndex = 0;

    int seenSoFar = 0;

    public void increment() {
        seenSoFar+=1;
    }

    public void close(FileChannel writer) throws IOException {
        byte[] bytes2 = getByte(previousValue);
        writer.(seenSoFar);
        writer.writeShort((short) bytes2.length);
        writer.write(bytes2);
    }

    private byte[] getByte(T previousValue) {
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
}
