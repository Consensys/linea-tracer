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

package net.consensys.linea.zktracer.module.rom;


import net.consensys.linea.zktracer.module.FW;
import net.consensys.linea.zktracer.module.add.Delta;
import net.consensys.linea.zktracer.types.UnsignedByte;

import java.io.DataOutputStream;
import java.io.FileWriter;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.BitSet;
import java.util.Map;
import java.io.IOException;

import static net.consensys.linea.zktracer.module.add.TraceBuilder.getByteArray;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;

/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
@SuppressWarnings({"unchecked"})
public class TraceBuilder {

    private final Map<String, Delta<?>> batch;
    private final Map<String, FW> writer;

    public TraceBuilder(Map<String, FW> writer, Map<String, Delta<?>> batch) {
        this.writer = writer;
        this.batch = batch;
    }

    private final BitSet filled = new BitSet();
    public TraceBuilder acc(BigInteger b) {

        if (filled.get(0)) {
            throw new IllegalStateException("ACC already set");
        } else {
            filled.set(0);
        }
        processBigInteger(b, writer.get("ACC"), batch.computeIfAbsent("ACC",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder codeFragmentIndex(BigInteger b) {

        if (filled.get(2)) {
            throw new IllegalStateException("CODE_FRAGMENT_INDEX already set");
        } else {
            filled.set(2);
        }
        processBigInteger(b, writer.get("CODE_FRAGMENT_INDEX"), batch.computeIfAbsent("CODE_FRAGMENT_INDEX",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder codeFragmentIndexInfty(BigInteger b) {

        if (filled.get(3)) {
            throw new IllegalStateException("CODE_FRAGMENT_INDEX_INFTY already set");
        } else {
            filled.set(3);
        }
        processBigInteger(b, writer.get("CODE_FRAGMENT_INDEX_INFTY"), batch.computeIfAbsent("CODE_FRAGMENT_INDEX_INFTY",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder codeSize(BigInteger b) {

        if (filled.get(4)) {
            throw new IllegalStateException("CODE_SIZE already set");
        } else {
            filled.set(4);
        }
        processBigInteger(b, writer.get("CODE_SIZE"), batch.computeIfAbsent("CODE_SIZE",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder codesizeReached(Boolean b) {

        if (filled.get(1)) {
            throw new IllegalStateException("CODESIZE_REACHED already set");
        } else {
            filled.set(1);
        }
        processBoolean(b, writer.get("CODESIZE_REACHED"), batch.computeIfAbsent("CODESIZE_REACHED",
                t -> new Delta<Boolean>()
        ) );
        return this;
    }
    public TraceBuilder counter(BigInteger b) {

        if (filled.get(5)) {
            throw new IllegalStateException("COUNTER already set");
        } else {
            filled.set(5);
        }
        processBigInteger(b, writer.get("COUNTER"), batch.computeIfAbsent("COUNTER",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder counterMax(BigInteger b) {

        if (filled.get(6)) {
            throw new IllegalStateException("COUNTER_MAX already set");
        } else {
            filled.set(6);
        }
        processBigInteger(b, writer.get("COUNTER_MAX"), batch.computeIfAbsent("COUNTER_MAX",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder counterPush(BigInteger b) {

        if (filled.get(7)) {
            throw new IllegalStateException("COUNTER_PUSH already set");
        } else {
            filled.set(7);
        }
        processBigInteger(b, writer.get("COUNTER_PUSH"), batch.computeIfAbsent("COUNTER_PUSH",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder index(BigInteger b) {

        if (filled.get(8)) {
            throw new IllegalStateException("INDEX already set");
        } else {
            filled.set(8);
        }
        processBigInteger(b, writer.get("INDEX"), batch.computeIfAbsent("INDEX",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder isPush(Boolean b) {

        if (filled.get(9)) {
            throw new IllegalStateException("IS_PUSH already set");
        } else {
            filled.set(9);
        }
        processBoolean(b, writer.get("IS_PUSH"), batch.computeIfAbsent("IS_PUSH",
                t -> new Delta<Boolean>()
        ) );
        return this;
    }
    public TraceBuilder isPushData(Boolean b) {

        if (filled.get(10)) {
            throw new IllegalStateException("IS_PUSH_DATA already set");
        } else {
            filled.set(10);
        }
        processBoolean(b, writer.get("IS_PUSH_DATA"), batch.computeIfAbsent("IS_PUSH_DATA",
                t -> new Delta<Boolean>()
        ) );
        return this;
    }
    public TraceBuilder limb(BigInteger b) {

        if (filled.get(11)) {
            throw new IllegalStateException("LIMB already set");
        } else {
            filled.set(11);
        }
        processBigInteger(b, writer.get("LIMB"), batch.computeIfAbsent("LIMB",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder nBytes(BigInteger b) {

        if (filled.get(21)) {
            throw new IllegalStateException("nBYTES already set");
        } else {
            filled.set(21);
        }
        processBigInteger(b, writer.get("nBYTES"), batch.computeIfAbsent("nBYTES",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder nBytesAcc(BigInteger b) {

        if (filled.get(22)) {
            throw new IllegalStateException("nBYTES_ACC already set");
        } else {
            filled.set(22);
        }
        processBigInteger(b, writer.get("nBYTES_ACC"), batch.computeIfAbsent("nBYTES_ACC",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder opcode(UnsignedByte b) {

        if (filled.get(12)) {
            throw new IllegalStateException("OPCODE already set");
        } else {
            filled.set(12);
        }
        processUnsignedByte(b, writer.get("OPCODE"), batch.computeIfAbsent("OPCODE",
                t -> new Delta<UnsignedByte>()
        ) );
        return this;
    }
    public TraceBuilder paddedBytecodeByte(UnsignedByte b) {

        if (filled.get(13)) {
            throw new IllegalStateException("PADDED_BYTECODE_BYTE already set");
        } else {
            filled.set(13);
        }
        processUnsignedByte(b, writer.get("PADDED_BYTECODE_BYTE"), batch.computeIfAbsent("PADDED_BYTECODE_BYTE",
                t -> new Delta<UnsignedByte>()
        ) );
        return this;
    }
    public TraceBuilder programmeCounter(BigInteger b) {

        if (filled.get(14)) {
            throw new IllegalStateException("PROGRAMME_COUNTER already set");
        } else {
            filled.set(14);
        }
        processBigInteger(b, writer.get("PROGRAMME_COUNTER"), batch.computeIfAbsent("PROGRAMME_COUNTER",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder pushFunnelBit(Boolean b) {

        if (filled.get(15)) {
            throw new IllegalStateException("PUSH_FUNNEL_BIT already set");
        } else {
            filled.set(15);
        }
        processBoolean(b, writer.get("PUSH_FUNNEL_BIT"), batch.computeIfAbsent("PUSH_FUNNEL_BIT",
                t -> new Delta<Boolean>()
        ) );
        return this;
    }
    public TraceBuilder pushParameter(BigInteger b) {

        if (filled.get(16)) {
            throw new IllegalStateException("PUSH_PARAMETER already set");
        } else {
            filled.set(16);
        }
        processBigInteger(b, writer.get("PUSH_PARAMETER"), batch.computeIfAbsent("PUSH_PARAMETER",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder pushValueAcc(BigInteger b) {

        if (filled.get(17)) {
            throw new IllegalStateException("PUSH_VALUE_ACC already set");
        } else {
            filled.set(17);
        }
        processBigInteger(b, writer.get("PUSH_VALUE_ACC"), batch.computeIfAbsent("PUSH_VALUE_ACC",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder pushValueHigh(BigInteger b) {

        if (filled.get(18)) {
            throw new IllegalStateException("PUSH_VALUE_HIGH already set");
        } else {
            filled.set(18);
        }
        processBigInteger(b, writer.get("PUSH_VALUE_HIGH"), batch.computeIfAbsent("PUSH_VALUE_HIGH",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder pushValueLow(BigInteger b) {

        if (filled.get(19)) {
            throw new IllegalStateException("PUSH_VALUE_LOW already set");
        } else {
            filled.set(19);
        }
        processBigInteger(b, writer.get("PUSH_VALUE_LOW"), batch.computeIfAbsent("PUSH_VALUE_LOW",
                t -> new Delta<BigInteger>()
        ) );
        return this;
    }
    public TraceBuilder validJumpDestination(Boolean b) {

        if (filled.get(20)) {
            throw new IllegalStateException("VALID_JUMP_DESTINATION already set");
        } else {
            filled.set(20);
        }
        processBoolean(b, writer.get("VALID_JUMP_DESTINATION"), batch.computeIfAbsent("VALID_JUMP_DESTINATION",
                t -> new Delta<Boolean>()
        ) );
        return this;
    }

    public void validateRowAndFlush()  throws IOException {
        if (!filled.get(0)) {
            throw new IllegalStateException("ACC has not been filled");
        }

        if (!filled.get(1)) {
            throw new IllegalStateException("CODESIZE_REACHED has not been filled");
        }

        if (!filled.get(2)) {
            throw new IllegalStateException("CODE_FRAGMENT_INDEX has not been filled");
        }

        if (!filled.get(3)) {
            throw new IllegalStateException("CODE_FRAGMENT_INDEX_INFTY has not been filled");
        }

        if (!filled.get(4)) {
            throw new IllegalStateException("CODE_SIZE has not been filled");
        }

        if (!filled.get(5)) {
            throw new IllegalStateException("COUNTER has not been filled");
        }

        if (!filled.get(6)) {
            throw new IllegalStateException("COUNTER_MAX has not been filled");
        }

        if (!filled.get(7)) {
            throw new IllegalStateException("COUNTER_PUSH has not been filled");
        }

        if (!filled.get(8)) {
            throw new IllegalStateException("INDEX has not been filled");
        }

        if (!filled.get(9)) {
            throw new IllegalStateException("IS_PUSH has not been filled");
        }

        if (!filled.get(10)) {
            throw new IllegalStateException("IS_PUSH_DATA has not been filled");
        }

        if (!filled.get(11)) {
            throw new IllegalStateException("LIMB has not been filled");
        }

        if (!filled.get(12)) {
            throw new IllegalStateException("OPCODE has not been filled");
        }

        if (!filled.get(13)) {
            throw new IllegalStateException("PADDED_BYTECODE_BYTE has not been filled");
        }

        if (!filled.get(14)) {
            throw new IllegalStateException("PROGRAMME_COUNTER has not been filled");
        }

        if (!filled.get(15)) {
            throw new IllegalStateException("PUSH_FUNNEL_BIT has not been filled");
        }

        if (!filled.get(16)) {
            throw new IllegalStateException("PUSH_PARAMETER has not been filled");
        }

        if (!filled.get(17)) {
            throw new IllegalStateException("PUSH_VALUE_ACC has not been filled");
        }

        if (!filled.get(18)) {
            throw new IllegalStateException("PUSH_VALUE_HIGH has not been filled");
        }

        if (!filled.get(19)) {
            throw new IllegalStateException("PUSH_VALUE_LOW has not been filled");
        }

        if (!filled.get(20)) {
            throw new IllegalStateException("VALID_JUMP_DESTINATION has not been filled");
        }

        if (!filled.get(21)) {
            throw new IllegalStateException("nBYTES has not been filled");
        }

        if (!filled.get(22)) {
            throw new IllegalStateException("nBYTES_ACC has not been filled");
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
