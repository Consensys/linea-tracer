package net.consensys.linea.zktracer.parquet;

import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.io.api.Binary;
import org.apache.parquet.io.api.RecordConsumer;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.hadoop.api.WriteSupport;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.HashMap;

public abstract class LocalWriteSupport<T> extends WriteSupport<T> {
    protected RecordConsumer recordConsumer;
    private MessageType schema;

    public LocalWriteSupport(MessageType schema) {
        this.schema = schema;
    }

    @Override
    public WriteContext init(Configuration configuration) {
        return new WriteContext(schema, new HashMap<>());
    }

    @Override
    public void prepareForWrite(RecordConsumer recordConsumer) {
        this.recordConsumer = recordConsumer;
    }

    private void writeLongField(String name, int index, long value) {
        recordConsumer.startField(name, index);
        recordConsumer.addLong(value);
        recordConsumer.endField(name, index);
    }

    protected void writeBooleanField(String name, int index, boolean value) {
        recordConsumer.startField(name, index);
        recordConsumer.addBoolean(value);
        recordConsumer.endField(name, index);
    }

    protected void writeUnsignedByteField(String name, int index, UnsignedByte value) {
        recordConsumer.startField(name, index);
        recordConsumer.addBinary(Binary.fromReusedByteBuffer(value.getByteBuffer()));
        recordConsumer.endField(name, index);
    }
    protected void writeBynaryField(String name, int index, ByteBuffer value) {
        recordConsumer.startField(name, index);
        recordConsumer.addBinary(Binary.fromReusedByteBuffer(value));
        recordConsumer.endField(name, index);
    }

    protected void writeBigIntegerField(String name, int index, BigInteger value) {
        recordConsumer.startField(name, index);
        recordConsumer.addBinary(Binary.fromReusedByteArray(value.toByteArray()));
        recordConsumer.endField(name, index);
    }

}