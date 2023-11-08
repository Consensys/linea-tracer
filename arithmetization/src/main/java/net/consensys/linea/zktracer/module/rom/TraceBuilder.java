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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import net.consensys.linea.zktracer.types.UnsignedByte;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.consensys.linea.zktracer.module.ParquetTrace;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.function.Consumer;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.hadoop.hive.ql.exec.vector.BytesColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.LongColumnVector;
import org.apache.orc.Writer;
import java.io.IOException;

/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
public class TraceBuilder {

    private final int rowId;
    private final VectorizedRowBatch batch;
    private final Writer writer;

    public TraceBuilder(int row, VectorizedRowBatch batch, Writer writer) {
        this.writer = writer;
        this.batch = batch;
        this.rowId = row;
    }

    private final BitSet filled = new BitSet();
    public TraceBuilder acc(BigInteger b) {

        if (filled.get(0)) {
            throw new IllegalStateException("ACC already set");
        } else {
            filled.set(0);
        }
        processBigInteger(b, 0);
        return this;
    }
    public TraceBuilder codeFragmentIndex(BigInteger b) {

        if (filled.get(2)) {
            throw new IllegalStateException("CODE_FRAGMENT_INDEX already set");
        } else {
            filled.set(2);
        }
        processBigInteger(b, 2);
        return this;
    }
    public TraceBuilder codeFragmentIndexInfty(BigInteger b) {

        if (filled.get(3)) {
            throw new IllegalStateException("CODE_FRAGMENT_INDEX_INFTY already set");
        } else {
            filled.set(3);
        }
        processBigInteger(b, 3);
        return this;
    }
    public TraceBuilder codeSize(BigInteger b) {

        if (filled.get(4)) {
            throw new IllegalStateException("CODE_SIZE already set");
        } else {
            filled.set(4);
        }
        processBigInteger(b, 4);
        return this;
    }
    public TraceBuilder codesizeReached(Boolean b) {

        if (filled.get(1)) {
            throw new IllegalStateException("CODESIZE_REACHED already set");
        } else {
            filled.set(1);
        }
        processBoolean(b, 1);
        return this;
    }
    public TraceBuilder counter(BigInteger b) {

        if (filled.get(5)) {
            throw new IllegalStateException("COUNTER already set");
        } else {
            filled.set(5);
        }
        processBigInteger(b, 5);
        return this;
    }
    public TraceBuilder counterMax(BigInteger b) {

        if (filled.get(6)) {
            throw new IllegalStateException("COUNTER_MAX already set");
        } else {
            filled.set(6);
        }
        processBigInteger(b, 6);
        return this;
    }
    public TraceBuilder counterPush(BigInteger b) {

        if (filled.get(7)) {
            throw new IllegalStateException("COUNTER_PUSH already set");
        } else {
            filled.set(7);
        }
        processBigInteger(b, 7);
        return this;
    }
    public TraceBuilder index(BigInteger b) {

        if (filled.get(8)) {
            throw new IllegalStateException("INDEX already set");
        } else {
            filled.set(8);
        }
        processBigInteger(b, 8);
        return this;
    }
    public TraceBuilder isPush(Boolean b) {

        if (filled.get(9)) {
            throw new IllegalStateException("IS_PUSH already set");
        } else {
            filled.set(9);
        }
        processBoolean(b, 9);
        return this;
    }
    public TraceBuilder isPushData(Boolean b) {

        if (filled.get(10)) {
            throw new IllegalStateException("IS_PUSH_DATA already set");
        } else {
            filled.set(10);
        }
        processBoolean(b, 10);
        return this;
    }
    public TraceBuilder limb(BigInteger b) {

        if (filled.get(11)) {
            throw new IllegalStateException("LIMB already set");
        } else {
            filled.set(11);
        }
        processBigInteger(b, 11);
        return this;
    }
    public TraceBuilder nBytes(BigInteger b) {

        if (filled.get(21)) {
            throw new IllegalStateException("nBYTES already set");
        } else {
            filled.set(21);
        }
        processBigInteger(b, 21);
        return this;
    }
    public TraceBuilder nBytesAcc(BigInteger b) {

        if (filled.get(22)) {
            throw new IllegalStateException("nBYTES_ACC already set");
        } else {
            filled.set(22);
        }
        processBigInteger(b, 22);
        return this;
    }
    public TraceBuilder opcode(UnsignedByte b) {

        if (filled.get(12)) {
            throw new IllegalStateException("OPCODE already set");
        } else {
            filled.set(12);
        }
        processUnsignedByte(b, 12);
        return this;
    }
    public TraceBuilder paddedBytecodeByte(UnsignedByte b) {

        if (filled.get(13)) {
            throw new IllegalStateException("PADDED_BYTECODE_BYTE already set");
        } else {
            filled.set(13);
        }
        processUnsignedByte(b, 13);
        return this;
    }
    public TraceBuilder programmeCounter(BigInteger b) {

        if (filled.get(14)) {
            throw new IllegalStateException("PROGRAMME_COUNTER already set");
        } else {
            filled.set(14);
        }
        processBigInteger(b, 14);
        return this;
    }
    public TraceBuilder pushFunnelBit(Boolean b) {

        if (filled.get(15)) {
            throw new IllegalStateException("PUSH_FUNNEL_BIT already set");
        } else {
            filled.set(15);
        }
        processBoolean(b, 15);
        return this;
    }
    public TraceBuilder pushParameter(BigInteger b) {

        if (filled.get(16)) {
            throw new IllegalStateException("PUSH_PARAMETER already set");
        } else {
            filled.set(16);
        }
        processBigInteger(b, 16);
        return this;
    }
    public TraceBuilder pushValueAcc(BigInteger b) {

        if (filled.get(17)) {
            throw new IllegalStateException("PUSH_VALUE_ACC already set");
        } else {
            filled.set(17);
        }
        processBigInteger(b, 17);
        return this;
    }
    public TraceBuilder pushValueHigh(BigInteger b) {

        if (filled.get(18)) {
            throw new IllegalStateException("PUSH_VALUE_HIGH already set");
        } else {
            filled.set(18);
        }
        processBigInteger(b, 18);
        return this;
    }
    public TraceBuilder pushValueLow(BigInteger b) {

        if (filled.get(19)) {
            throw new IllegalStateException("PUSH_VALUE_LOW already set");
        } else {
            filled.set(19);
        }
        processBigInteger(b, 19);
        return this;
    }
    public TraceBuilder validJumpDestination(Boolean b) {

        if (filled.get(20)) {
            throw new IllegalStateException("VALID_JUMP_DESTINATION already set");
        } else {
            filled.set(20);
        }
        processBoolean(b, 20);
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

        if (batch.size == batch.getMaxSize()) {
            writer.addRowBatch(batch);
            batch.reset();
        }
    }

    private void processBigInteger(BigInteger b, int columnId) {
        BytesColumnVector columnVector = (BytesColumnVector) batch.cols[columnId];
        columnVector.setVal(rowId, b.toByteArray());
    }

    private void processUnsignedByte(UnsignedByte b, int columnId) {
        BytesColumnVector columnVector = (BytesColumnVector) batch.cols[columnId];
        columnVector.setVal(rowId, new byte[]{b.toByte()});
    }

    private void processBoolean(Boolean b, int columnId) {
        LongColumnVector columnVector = (LongColumnVector) batch.cols[columnId];
        columnVector.vector[rowId] = b ? 1L : 0L;
    }
}
