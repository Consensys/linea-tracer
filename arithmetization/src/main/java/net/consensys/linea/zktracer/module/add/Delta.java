package net.consensys.linea.zktracer.module.add;

import lombok.Data;

import java.io.FileWriter;
import java.io.IOException;

@Data
public class Delta<T> {
    T previousValue = null;
    public int lastIndex = 0;

    int seenSoFar = 0;

    public void increment() {
        seenSoFar+=1;
    }

    public void close(FileWriter writer) throws IOException {
        writer.append(Integer.toString(seenSoFar));
        writer.append(" ");
        writer.append(previousValue.toString());
        writer.append("\n");
    }

    public void initialize(T b) {
        previousValue = b;
        seenSoFar = 1;
    }
}
