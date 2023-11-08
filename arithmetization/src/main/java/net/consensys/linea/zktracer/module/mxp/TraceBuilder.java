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

package net.consensys.linea.zktracer.module.mxp;

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
    public TraceBuilder acc1(BigInteger b) {

        if (filled.get(0)) {
            throw new IllegalStateException("ACC_1 already set");
        } else {
            filled.set(0);
        }
        processBigInteger(b, 0);
        return this;
    }
    public TraceBuilder acc2(BigInteger b) {

        if (filled.get(1)) {
            throw new IllegalStateException("ACC_2 already set");
        } else {
            filled.set(1);
        }
        processBigInteger(b, 1);
        return this;
    }
    public TraceBuilder acc3(BigInteger b) {

        if (filled.get(2)) {
            throw new IllegalStateException("ACC_3 already set");
        } else {
            filled.set(2);
        }
        processBigInteger(b, 2);
        return this;
    }
    public TraceBuilder acc4(BigInteger b) {

        if (filled.get(3)) {
            throw new IllegalStateException("ACC_4 already set");
        } else {
            filled.set(3);
        }
        processBigInteger(b, 3);
        return this;
    }
    public TraceBuilder accA(BigInteger b) {

        if (filled.get(4)) {
            throw new IllegalStateException("ACC_A already set");
        } else {
            filled.set(4);
        }
        processBigInteger(b, 4);
        return this;
    }
    public TraceBuilder accQ(BigInteger b) {

        if (filled.get(5)) {
            throw new IllegalStateException("ACC_Q already set");
        } else {
            filled.set(5);
        }
        processBigInteger(b, 5);
        return this;
    }
    public TraceBuilder accW(BigInteger b) {

        if (filled.get(6)) {
            throw new IllegalStateException("ACC_W already set");
        } else {
            filled.set(6);
        }
        processBigInteger(b, 6);
        return this;
    }
    public TraceBuilder byte1(UnsignedByte b) {

        if (filled.get(7)) {
            throw new IllegalStateException("BYTE_1 already set");
        } else {
            filled.set(7);
        }
        processUnsignedByte(b, 7);
        return this;
    }
    public TraceBuilder byte2(UnsignedByte b) {

        if (filled.get(8)) {
            throw new IllegalStateException("BYTE_2 already set");
        } else {
            filled.set(8);
        }
        processUnsignedByte(b, 8);
        return this;
    }
    public TraceBuilder byte3(UnsignedByte b) {

        if (filled.get(9)) {
            throw new IllegalStateException("BYTE_3 already set");
        } else {
            filled.set(9);
        }
        processUnsignedByte(b, 9);
        return this;
    }
    public TraceBuilder byte4(UnsignedByte b) {

        if (filled.get(10)) {
            throw new IllegalStateException("BYTE_4 already set");
        } else {
            filled.set(10);
        }
        processUnsignedByte(b, 10);
        return this;
    }
    public TraceBuilder byteA(UnsignedByte b) {

        if (filled.get(11)) {
            throw new IllegalStateException("BYTE_A already set");
        } else {
            filled.set(11);
        }
        processUnsignedByte(b, 11);
        return this;
    }
    public TraceBuilder byteQ(UnsignedByte b) {

        if (filled.get(12)) {
            throw new IllegalStateException("BYTE_Q already set");
        } else {
            filled.set(12);
        }
        processUnsignedByte(b, 12);
        return this;
    }
    public TraceBuilder byteQq(BigInteger b) {

        if (filled.get(13)) {
            throw new IllegalStateException("BYTE_QQ already set");
        } else {
            filled.set(13);
        }
        processBigInteger(b, 13);
        return this;
    }
    public TraceBuilder byteR(BigInteger b) {

        if (filled.get(14)) {
            throw new IllegalStateException("BYTE_R already set");
        } else {
            filled.set(14);
        }
        processBigInteger(b, 14);
        return this;
    }
    public TraceBuilder byteW(UnsignedByte b) {

        if (filled.get(15)) {
            throw new IllegalStateException("BYTE_W already set");
        } else {
            filled.set(15);
        }
        processUnsignedByte(b, 15);
        return this;
    }
    public TraceBuilder cMem(BigInteger b) {

        if (filled.get(19)) {
            throw new IllegalStateException("C_MEM already set");
        } else {
            filled.set(19);
        }
        processBigInteger(b, 19);
        return this;
    }
    public TraceBuilder cMemNew(BigInteger b) {

        if (filled.get(20)) {
            throw new IllegalStateException("C_MEM_NEW already set");
        } else {
            filled.set(20);
        }
        processBigInteger(b, 20);
        return this;
    }
    public TraceBuilder cn(BigInteger b) {

        if (filled.get(16)) {
            throw new IllegalStateException("CN already set");
        } else {
            filled.set(16);
        }
        processBigInteger(b, 16);
        return this;
    }
    public TraceBuilder comp(Boolean b) {

        if (filled.get(17)) {
            throw new IllegalStateException("COMP already set");
        } else {
            filled.set(17);
        }
        processBoolean(b, 17);
        return this;
    }
    public TraceBuilder ct(BigInteger b) {

        if (filled.get(18)) {
            throw new IllegalStateException("CT already set");
        } else {
            filled.set(18);
        }
        processBigInteger(b, 18);
        return this;
    }
    public TraceBuilder deploys(Boolean b) {

        if (filled.get(21)) {
            throw new IllegalStateException("DEPLOYS already set");
        } else {
            filled.set(21);
        }
        processBoolean(b, 21);
        return this;
    }
    public TraceBuilder expands(Boolean b) {

        if (filled.get(22)) {
            throw new IllegalStateException("EXPANDS already set");
        } else {
            filled.set(22);
        }
        processBoolean(b, 22);
        return this;
    }
    public TraceBuilder gasMxp(BigInteger b) {

        if (filled.get(23)) {
            throw new IllegalStateException("GAS_MXP already set");
        } else {
            filled.set(23);
        }
        processBigInteger(b, 23);
        return this;
    }
    public TraceBuilder gbyte(BigInteger b) {

        if (filled.get(24)) {
            throw new IllegalStateException("GBYTE already set");
        } else {
            filled.set(24);
        }
        processBigInteger(b, 24);
        return this;
    }
    public TraceBuilder gword(BigInteger b) {

        if (filled.get(25)) {
            throw new IllegalStateException("GWORD already set");
        } else {
            filled.set(25);
        }
        processBigInteger(b, 25);
        return this;
    }
    public TraceBuilder inst(BigInteger b) {

        if (filled.get(26)) {
            throw new IllegalStateException("INST already set");
        } else {
            filled.set(26);
        }
        processBigInteger(b, 26);
        return this;
    }
    public TraceBuilder linCost(BigInteger b) {

        if (filled.get(27)) {
            throw new IllegalStateException("LIN_COST already set");
        } else {
            filled.set(27);
        }
        processBigInteger(b, 27);
        return this;
    }
    public TraceBuilder maxOffset(BigInteger b) {

        if (filled.get(28)) {
            throw new IllegalStateException("MAX_OFFSET already set");
        } else {
            filled.set(28);
        }
        processBigInteger(b, 28);
        return this;
    }
    public TraceBuilder maxOffset1(BigInteger b) {

        if (filled.get(29)) {
            throw new IllegalStateException("MAX_OFFSET_1 already set");
        } else {
            filled.set(29);
        }
        processBigInteger(b, 29);
        return this;
    }
    public TraceBuilder maxOffset2(BigInteger b) {

        if (filled.get(30)) {
            throw new IllegalStateException("MAX_OFFSET_2 already set");
        } else {
            filled.set(30);
        }
        processBigInteger(b, 30);
        return this;
    }
    public TraceBuilder mxpType1(Boolean b) {

        if (filled.get(32)) {
            throw new IllegalStateException("MXP_TYPE_1 already set");
        } else {
            filled.set(32);
        }
        processBoolean(b, 32);
        return this;
    }
    public TraceBuilder mxpType2(Boolean b) {

        if (filled.get(33)) {
            throw new IllegalStateException("MXP_TYPE_2 already set");
        } else {
            filled.set(33);
        }
        processBoolean(b, 33);
        return this;
    }
    public TraceBuilder mxpType3(Boolean b) {

        if (filled.get(34)) {
            throw new IllegalStateException("MXP_TYPE_3 already set");
        } else {
            filled.set(34);
        }
        processBoolean(b, 34);
        return this;
    }
    public TraceBuilder mxpType4(Boolean b) {

        if (filled.get(35)) {
            throw new IllegalStateException("MXP_TYPE_4 already set");
        } else {
            filled.set(35);
        }
        processBoolean(b, 35);
        return this;
    }
    public TraceBuilder mxpType5(Boolean b) {

        if (filled.get(36)) {
            throw new IllegalStateException("MXP_TYPE_5 already set");
        } else {
            filled.set(36);
        }
        processBoolean(b, 36);
        return this;
    }
    public TraceBuilder mxpx(Boolean b) {

        if (filled.get(31)) {
            throw new IllegalStateException("MXPX already set");
        } else {
            filled.set(31);
        }
        processBoolean(b, 31);
        return this;
    }
    public TraceBuilder noop(Boolean b) {

        if (filled.get(37)) {
            throw new IllegalStateException("NOOP already set");
        } else {
            filled.set(37);
        }
        processBoolean(b, 37);
        return this;
    }
    public TraceBuilder offset1Hi(BigInteger b) {

        if (filled.get(38)) {
            throw new IllegalStateException("OFFSET_1_HI already set");
        } else {
            filled.set(38);
        }
        processBigInteger(b, 38);
        return this;
    }
    public TraceBuilder offset1Lo(BigInteger b) {

        if (filled.get(39)) {
            throw new IllegalStateException("OFFSET_1_LO already set");
        } else {
            filled.set(39);
        }
        processBigInteger(b, 39);
        return this;
    }
    public TraceBuilder offset2Hi(BigInteger b) {

        if (filled.get(40)) {
            throw new IllegalStateException("OFFSET_2_HI already set");
        } else {
            filled.set(40);
        }
        processBigInteger(b, 40);
        return this;
    }
    public TraceBuilder offset2Lo(BigInteger b) {

        if (filled.get(41)) {
            throw new IllegalStateException("OFFSET_2_LO already set");
        } else {
            filled.set(41);
        }
        processBigInteger(b, 41);
        return this;
    }
    public TraceBuilder quadCost(BigInteger b) {

        if (filled.get(42)) {
            throw new IllegalStateException("QUAD_COST already set");
        } else {
            filled.set(42);
        }
        processBigInteger(b, 42);
        return this;
    }
    public TraceBuilder roob(Boolean b) {

        if (filled.get(43)) {
            throw new IllegalStateException("ROOB already set");
        } else {
            filled.set(43);
        }
        processBoolean(b, 43);
        return this;
    }
    public TraceBuilder size1Hi(BigInteger b) {

        if (filled.get(44)) {
            throw new IllegalStateException("SIZE_1_HI already set");
        } else {
            filled.set(44);
        }
        processBigInteger(b, 44);
        return this;
    }
    public TraceBuilder size1Lo(BigInteger b) {

        if (filled.get(45)) {
            throw new IllegalStateException("SIZE_1_LO already set");
        } else {
            filled.set(45);
        }
        processBigInteger(b, 45);
        return this;
    }
    public TraceBuilder size2Hi(BigInteger b) {

        if (filled.get(46)) {
            throw new IllegalStateException("SIZE_2_HI already set");
        } else {
            filled.set(46);
        }
        processBigInteger(b, 46);
        return this;
    }
    public TraceBuilder size2Lo(BigInteger b) {

        if (filled.get(47)) {
            throw new IllegalStateException("SIZE_2_LO already set");
        } else {
            filled.set(47);
        }
        processBigInteger(b, 47);
        return this;
    }
    public TraceBuilder stamp(BigInteger b) {

        if (filled.get(48)) {
            throw new IllegalStateException("STAMP already set");
        } else {
            filled.set(48);
        }
        processBigInteger(b, 48);
        return this;
    }
    public TraceBuilder words(BigInteger b) {

        if (filled.get(49)) {
            throw new IllegalStateException("WORDS already set");
        } else {
            filled.set(49);
        }
        processBigInteger(b, 49);
        return this;
    }
    public TraceBuilder wordsNew(BigInteger b) {

        if (filled.get(50)) {
            throw new IllegalStateException("WORDS_NEW already set");
        } else {
            filled.set(50);
        }
        processBigInteger(b, 50);
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
            throw new IllegalStateException("ACC_3 has not been filled");
        }

        if (!filled.get(3)) {
            throw new IllegalStateException("ACC_4 has not been filled");
        }

        if (!filled.get(4)) {
            throw new IllegalStateException("ACC_A has not been filled");
        }

        if (!filled.get(5)) {
            throw new IllegalStateException("ACC_Q has not been filled");
        }

        if (!filled.get(6)) {
            throw new IllegalStateException("ACC_W has not been filled");
        }

        if (!filled.get(7)) {
            throw new IllegalStateException("BYTE_1 has not been filled");
        }

        if (!filled.get(8)) {
            throw new IllegalStateException("BYTE_2 has not been filled");
        }

        if (!filled.get(9)) {
            throw new IllegalStateException("BYTE_3 has not been filled");
        }

        if (!filled.get(10)) {
            throw new IllegalStateException("BYTE_4 has not been filled");
        }

        if (!filled.get(11)) {
            throw new IllegalStateException("BYTE_A has not been filled");
        }

        if (!filled.get(12)) {
            throw new IllegalStateException("BYTE_Q has not been filled");
        }

        if (!filled.get(13)) {
            throw new IllegalStateException("BYTE_QQ has not been filled");
        }

        if (!filled.get(14)) {
            throw new IllegalStateException("BYTE_R has not been filled");
        }

        if (!filled.get(15)) {
            throw new IllegalStateException("BYTE_W has not been filled");
        }

        if (!filled.get(16)) {
            throw new IllegalStateException("CN has not been filled");
        }

        if (!filled.get(17)) {
            throw new IllegalStateException("COMP has not been filled");
        }

        if (!filled.get(18)) {
            throw new IllegalStateException("CT has not been filled");
        }

        if (!filled.get(19)) {
            throw new IllegalStateException("C_MEM has not been filled");
        }

        if (!filled.get(20)) {
            throw new IllegalStateException("C_MEM_NEW has not been filled");
        }

        if (!filled.get(21)) {
            throw new IllegalStateException("DEPLOYS has not been filled");
        }

        if (!filled.get(22)) {
            throw new IllegalStateException("EXPANDS has not been filled");
        }

        if (!filled.get(23)) {
            throw new IllegalStateException("GAS_MXP has not been filled");
        }

        if (!filled.get(24)) {
            throw new IllegalStateException("GBYTE has not been filled");
        }

        if (!filled.get(25)) {
            throw new IllegalStateException("GWORD has not been filled");
        }

        if (!filled.get(26)) {
            throw new IllegalStateException("INST has not been filled");
        }

        if (!filled.get(27)) {
            throw new IllegalStateException("LIN_COST has not been filled");
        }

        if (!filled.get(28)) {
            throw new IllegalStateException("MAX_OFFSET has not been filled");
        }

        if (!filled.get(29)) {
            throw new IllegalStateException("MAX_OFFSET_1 has not been filled");
        }

        if (!filled.get(30)) {
            throw new IllegalStateException("MAX_OFFSET_2 has not been filled");
        }

        if (!filled.get(31)) {
            throw new IllegalStateException("MXPX has not been filled");
        }

        if (!filled.get(32)) {
            throw new IllegalStateException("MXP_TYPE_1 has not been filled");
        }

        if (!filled.get(33)) {
            throw new IllegalStateException("MXP_TYPE_2 has not been filled");
        }

        if (!filled.get(34)) {
            throw new IllegalStateException("MXP_TYPE_3 has not been filled");
        }

        if (!filled.get(35)) {
            throw new IllegalStateException("MXP_TYPE_4 has not been filled");
        }

        if (!filled.get(36)) {
            throw new IllegalStateException("MXP_TYPE_5 has not been filled");
        }

        if (!filled.get(37)) {
            throw new IllegalStateException("NOOP has not been filled");
        }

        if (!filled.get(38)) {
            throw new IllegalStateException("OFFSET_1_HI has not been filled");
        }

        if (!filled.get(39)) {
            throw new IllegalStateException("OFFSET_1_LO has not been filled");
        }

        if (!filled.get(40)) {
            throw new IllegalStateException("OFFSET_2_HI has not been filled");
        }

        if (!filled.get(41)) {
            throw new IllegalStateException("OFFSET_2_LO has not been filled");
        }

        if (!filled.get(42)) {
            throw new IllegalStateException("QUAD_COST has not been filled");
        }

        if (!filled.get(43)) {
            throw new IllegalStateException("ROOB has not been filled");
        }

        if (!filled.get(44)) {
            throw new IllegalStateException("SIZE_1_HI has not been filled");
        }

        if (!filled.get(45)) {
            throw new IllegalStateException("SIZE_1_LO has not been filled");
        }

        if (!filled.get(46)) {
            throw new IllegalStateException("SIZE_2_HI has not been filled");
        }

        if (!filled.get(47)) {
            throw new IllegalStateException("SIZE_2_LO has not been filled");
        }

        if (!filled.get(48)) {
            throw new IllegalStateException("STAMP has not been filled");
        }

        if (!filled.get(49)) {
            throw new IllegalStateException("WORDS has not been filled");
        }

        if (!filled.get(50)) {
            throw new IllegalStateException("WORDS_NEW has not been filled");
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
