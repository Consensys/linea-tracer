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

import net.consensys.linea.zktracer.module.FW;
import net.consensys.linea.zktracer.module.add.Delta;
import net.consensys.linea.zktracer.types.UnsignedByte;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.stream.IntStream;


/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
@SuppressWarnings({"unchecked"})
public class TraceBuilder {
    private final Delta<?>[] batch;
    private final FW[] writer;

    public TraceBuilder(List<FW> writer) {
        this.writer = writer.toArray(new FW[0]);
        this.batch =new Delta<?>[writer.size()];
        IntStream.range(0, writer.size()).forEach(i -> this.batch[i]=new Delta<BigInteger>());
    }

    private final BitSet filled = new BitSet();
    public TraceBuilder acc1(BigInteger b) {

        if (filled.get(0)) {
            throw new IllegalStateException("ACC_1 already set");
        } else {
            filled.set(0);
        }
        processBigInteger(b, writer[0], batch[0] );
        return this;
    }
    public TraceBuilder acc2(BigInteger b) {

        if (filled.get(1)) {
            throw new IllegalStateException("ACC_2 already set");
        } else {
            filled.set(1);
        }
        processBigInteger(b, writer[1], batch[1] );
        return this;
    }
    public TraceBuilder arg1Hi(BigInteger b) {

        if (filled.get(2)) {
            throw new IllegalStateException("ARG_1_HI already set");
        } else {
            filled.set(2);
        }
        processBigInteger(b, writer[2], batch[2] );
        return this;
    }
    public TraceBuilder arg1Lo(BigInteger b) {

        if (filled.get(3)) {
            throw new IllegalStateException("ARG_1_LO already set");
        } else {
            filled.set(3);
        }
        processBigInteger(b, writer[3], batch[3] );
        return this;
    }
    public TraceBuilder arg2Hi(BigInteger b) {

        if (filled.get(4)) {
            throw new IllegalStateException("ARG_2_HI already set");
        } else {
            filled.set(4);
        }
        processBigInteger(b, writer[4], batch[4] );
        return this;
    }
    public TraceBuilder arg2Lo(BigInteger b) {

        if (filled.get(5)) {
            throw new IllegalStateException("ARG_2_LO already set");
        } else {
            filled.set(5);
        }
        processBigInteger(b, writer[5], batch[5] );
        return this;
    }
    public TraceBuilder byte1(UnsignedByte b) {

        if (filled.get(6)) {
            throw new IllegalStateException("BYTE_1 already set");
        } else {
            filled.set(6);
        }
        processUnsignedByte(b, writer[6], batch[6] );
        return this;
    }
    public TraceBuilder byte2(UnsignedByte b) {

        if (filled.get(7)) {
            throw new IllegalStateException("BYTE_2 already set");
        } else {
            filled.set(7);
        }
        processUnsignedByte(b, writer[7], batch[7] );
        return this;
    }
    public TraceBuilder ct(BigInteger b) {

        if (filled.get(8)) {
            throw new IllegalStateException("CT already set");
        } else {
            filled.set(8);
        }
        processBigInteger(b, writer[8], batch[8] );
        return this;
    }
    public TraceBuilder inst(BigInteger b) {

        if (filled.get(9)) {
            throw new IllegalStateException("INST already set");
        } else {
            filled.set(9);
        }
        processBigInteger(b, writer[9], batch[9] );
        return this;
    }
    public TraceBuilder overflow(Boolean b) {

        if (filled.get(10)) {
            throw new IllegalStateException("OVERFLOW already set");
        } else {
            filled.set(10);
        }
        processBoolean(b, writer[10], batch[10] );
        return this;
    }
    public TraceBuilder resHi(BigInteger b) {

        if (filled.get(11)) {
            throw new IllegalStateException("RES_HI already set");
        } else {
            filled.set(11);
        }
        processBigInteger(b, writer[11], batch[11] );
        return this;
    }
    public TraceBuilder resLo(BigInteger b) {

        if (filled.get(12)) {
            throw new IllegalStateException("RES_LO already set");
        } else {
            filled.set(12);
        }
        processBigInteger(b, writer[12], batch[12] );
        return this;
    }
    public TraceBuilder stamp(BigInteger b) {

        if (filled.get(13)) {
            throw new IllegalStateException("STAMP already set");
        } else {
            filled.set(13);
        }
        processBigInteger(b, writer[13], batch[13] );
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


    private void processUnsignedByte(UnsignedByte b,
                                     FW writer, Delta<?>d) {
        Delta<UnsignedByte> delta = (Delta<UnsignedByte>) d;
        if(delta.getPreviousValue() == null){
            delta.initialize(b);
        }
        else if (delta.getPreviousValue().equals(b)) {
            delta.increment();
        } else {
            try {
                writer.writeInt(delta.getSeenSoFar());
                writer.writeByte(b.toByte());
                delta.setPreviousValue(b);
                delta.lastIndex += delta.getSeenSoFar();
                delta.setSeenSoFar(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void processBoolean(Boolean b,  FW writer,Delta<?>d) {
        Delta<Boolean> delta = (Delta<Boolean>) d;
        if(delta.getPreviousValue() == null){
            delta.initialize(b);
        }
        else if (delta.getPreviousValue().equals(b)) {
            delta.increment();
        } else {
            try {
                writer.writeInt(delta.getSeenSoFar());
                writer.writeByte(b?(byte)1:(byte)0);
                delta.setPreviousValue(b);
                delta.lastIndex += delta.getSeenSoFar();
                delta.setSeenSoFar(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void processBigInteger(BigInteger b, FW writer, Delta<?> d) {
        Delta<BigInteger> delta = (Delta<BigInteger>) d;
        if(delta.getPreviousValue() == null){
            delta.initialize(b);
        }
        else if (delta.getPreviousValue().equals(b)) {
            delta.increment();
        } else {
            try {

                byte[] bytes2 = delta.getPreviousValue().toByteArray();
                writer.writeShort((short)bytes2.length);
                writer.write(bytes2);
                delta.setPreviousValue(b);
                delta.lastIndex += delta.getSeenSoFar();
                delta.setSeenSoFar(1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
