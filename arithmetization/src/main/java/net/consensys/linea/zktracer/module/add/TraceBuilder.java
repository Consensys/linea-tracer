/*
 * Copyright ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package net.consensys.linea.zktracer.module.add;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.consensys.linea.zktracer.bytestheta.BaseTheta;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.FileWriter;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Base64;
import java.util.BitSet;
import java.util.Map;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
@SuppressWarnings({"unchecked"})
public class TraceBuilder {

    private static final LoadingCache<BigInteger, byte[]> biCache = CacheBuilder.<BigInteger, byte[]>newBuilder().
            build(new CacheLoader<>() {
                @Override
                public byte[] load(BigInteger key) throws Exception {
                    return key.toByteArray();
                }
            });
    private final Map<String, Delta<?>> batch;
    private final Map<String, FileChannel> writer;

    public TraceBuilder(Map<String, FileChannel> writer, Map<String, Delta<?>> batch) {
        this.writer = writer;
        this.batch = batch;
    }

    private final BitSet filled = new BitSet();
    public TraceBuilder acc1(BigInteger b) {

        if (filled.get(0)) {
            throw new IllegalStateException("ACC_1 already set");
        } else {
            filled.set(0);
        }
        processBigInteger(b, writer.get("ACC_1"), batch.computeIfAbsent("ACC_1",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder acc2(BigInteger b) {

        if (filled.get(1)) {
            throw new IllegalStateException("ACC_2 already set");
        } else {
            filled.set(1);
        }
        processBigInteger(b, writer.get("ACC_2"), batch.computeIfAbsent("ACC_2",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder arg1Hi(BigInteger b) {

        if (filled.get(2)) {
            throw new IllegalStateException("ARG_1_HI already set");
        } else {
            filled.set(2);
        }
        processBigInteger(b, writer.get("ARG_1_HI"), batch.computeIfAbsent("ARG_1_HI",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder arg1Lo(BigInteger b) {

        if (filled.get(3)) {
            throw new IllegalStateException("ARG_1_LO already set");
        } else {
            filled.set(3);
        }
        processBigInteger(b, writer.get("ARG_1_LO"), batch.computeIfAbsent("ARG_1_LO",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder arg2Hi(BigInteger b) {

        if (filled.get(4)) {
            throw new IllegalStateException("ARG_2_HI already set");
        } else {
            filled.set(4);
        }
        processBigInteger(b, writer.get("ARG_2_HI"), batch.computeIfAbsent("ARG_2_HI",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder arg2Lo(BigInteger b) {

        if (filled.get(5)) {
            throw new IllegalStateException("ARG_2_LO already set");
        } else {
            filled.set(5);
        }
        processBigInteger(b, writer.get("ARG_2_LO"), batch.computeIfAbsent("ARG_2_LO",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder byte1(UnsignedByte b) {

        if (filled.get(6)) {
            throw new IllegalStateException("BYTE_1 already set");
        } else {
            filled.set(6);
        }
        processUnsignedByte(b, writer.get("BYTE_1"), batch.computeIfAbsent("BYTE_1",
                t -> new Delta<UnsignedByte>()
        ) );
        return this;
    }
    public TraceBuilder byte2(UnsignedByte b) {

        if (filled.get(7)) {
            throw new IllegalStateException("BYTE_2 already set");
        } else {
            filled.set(7);
        }
        processUnsignedByte(b, writer.get("BYTE_2"), batch.computeIfAbsent("BYTE_2",
                t -> new Delta<UnsignedByte>()
        ) );
        return this;
    }
    public TraceBuilder ct(BigInteger b) {

        if (filled.get(8)) {
            throw new IllegalStateException("CT already set");
        } else {
            filled.set(8);
        }
        processBigInteger(b, writer.get("CT"), batch.computeIfAbsent("CT",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder inst(BigInteger b) {

        if (filled.get(9)) {
            throw new IllegalStateException("INST already set");
        } else {
            filled.set(9);
        }
        processBigInteger(b, writer.get("INST"), batch.computeIfAbsent("INST",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder overflow(Boolean b) {

        if (filled.get(10)) {
            throw new IllegalStateException("OVERFLOW already set");
        } else {
            filled.set(10);
        }
        processBoolean(b, writer.get("OVERFLOW"), batch.computeIfAbsent("OVERFLOW",
                t -> new Delta<Boolean>()
        ) );
        return this;
    }
    public TraceBuilder resHi(BigInteger b) {

        if (filled.get(11)) {
            throw new IllegalStateException("RES_HI already set");
        } else {
            filled.set(11);
        }
        processBigInteger(b, writer.get("RES_HI"), batch.computeIfAbsent("RES_HI",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder resLo(BigInteger b) {

        if (filled.get(12)) {
            throw new IllegalStateException("RES_LO already set");
        } else {
            filled.set(12);
        }
        processBigInteger(b, writer.get("RES_LO"), batch.computeIfAbsent("RES_LO",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder stamp(BigInteger b) {

        if (filled.get(13)) {
            throw new IllegalStateException("STAMP already set");
        } else {
            filled.set(13);
        }
        processBigInteger(b, writer.get("STAMP"), batch.computeIfAbsent("STAMP",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }

    public void validateRowAndFlush()  throws IOException {
        if (!filled.get(0)) {
            throw new IllegalStateException("ACC_1 has not been filled");
        }

        if (!filled.get(1)) {
            throw new IllegalStateException("ACC_2 has not been filled");
        }

        if (!filled.get(2)) {
            throw new IllegalStateException("ARG_1_HI has not been filled");
        }

        if (!filled.get(3)) {
            throw new IllegalStateException("ARG_1_LO has not been filled");
        }

        if (!filled.get(4)) {
            throw new IllegalStateException("ARG_2_HI has not been filled");
        }

        if (!filled.get(5)) {
            throw new IllegalStateException("ARG_2_LO has not been filled");
        }

        if (!filled.get(6)) {
            throw new IllegalStateException("BYTE_1 has not been filled");
        }

        if (!filled.get(7)) {
            throw new IllegalStateException("BYTE_2 has not been filled");
        }

        if (!filled.get(8)) {
            throw new IllegalStateException("CT has not been filled");
        }

        if (!filled.get(9)) {
            throw new IllegalStateException("INST has not been filled");
        }

        if (!filled.get(10)) {
            throw new IllegalStateException("OVERFLOW has not been filled");
        }

        if (!filled.get(11)) {
            throw new IllegalStateException("RES_HI has not been filled");
        }

        if (!filled.get(12)) {
            throw new IllegalStateException("RES_LO has not been filled");
        }

        if (!filled.get(13)) {
            throw new IllegalStateException("STAMP has not been filled");
        }


        filled.clear();

    }



    @NotNull
    public static byte[] getByteArray(Delta<BigInteger> delta) {
        try {
            return biCache.get(delta.previousValue);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void processUnsignedByte(UnsignedByte b,
                                     FileChannel writer, Delta<?>d) {
        Delta<UnsignedByte> delta = (Delta<UnsignedByte>) d;
        if(delta.getPreviousValue() == null){
            delta.initialize(b);
        }
        else if (delta.getPreviousValue().equals(b)) {
            delta.increment();
        } else {
            try {
                ByteBuffer bf = ((ByteBuffer.allocate(5).putInt(delta.getSeenSoFar())));
                bf.put(b.toByte());
                writer.write(bf);
                delta.setPreviousValue(b);
                delta.lastIndex += delta.getSeenSoFar();
                delta.setSeenSoFar(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void processBoolean(Boolean b,  FileChannel writer,Delta<?>d) {
        Delta<Boolean> delta = (Delta<Boolean>) d;
        if(delta.getPreviousValue() == null){
            delta.initialize(b);
        }
        else if (delta.getPreviousValue().equals(b)) {
            delta.increment();
        } else {
            try {
                ByteBuffer bf = ((ByteBuffer.allocate(5).putInt(delta.getSeenSoFar())));
                bf.put(b?(byte)1:(byte)0);
                writer.write(bf);
                delta.setPreviousValue(b);
                delta.lastIndex += delta.getSeenSoFar();
                delta.setSeenSoFar(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void processBigInteger(BigInteger b, FileChannel writer, Delta<?> d) {
        Delta<BigInteger> delta = (Delta<BigInteger>) d;
        if(delta.getPreviousValue() == null){
            delta.initialize(b);
        }
        else if (delta.getPreviousValue().equals(b)) {
            delta.increment();
        } else {
            try {
                // Convert the BigIntegers to bytes

                byte[] bytes2 = getByteArray(delta);
                ByteBuffer bf = ((ByteBuffer.allocate(6 + bytes2.length).putInt(delta.getSeenSoFar())));
                bf.putShort((short)bytes2.length);
                bf.put(bytes2);

                writer.write(bf);

                // Encode the bytes to a Base64 string

//
//                writer.append(String.valueOf(delta.seenSoFar));
//                writer.append(" ");
//                writer.append(delta.previousValue.toString());
//                writer.append("\n");
                delta.setPreviousValue(b);
                delta.lastIndex += delta.getSeenSoFar();
                delta.setSeenSoFar(1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
