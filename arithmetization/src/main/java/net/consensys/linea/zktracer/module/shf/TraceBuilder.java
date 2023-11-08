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

package net.consensys.linea.zktracer.module.shf;

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

private final BitSet filled = new BitSet();
  public TraceBuilder acc1(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(0)) {
      throw new IllegalStateException("ACC_1 already set");
    } else {
      filled.set(0);
    }
        processBigInteger(batch, b, 0, rowId);
    return this;
  }
  public TraceBuilder acc2(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(1)) {
      throw new IllegalStateException("ACC_2 already set");
    } else {
      filled.set(1);
    }
        processBigInteger(batch, b, 1, rowId);
    return this;
  }
  public TraceBuilder acc3(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(2)) {
      throw new IllegalStateException("ACC_3 already set");
    } else {
      filled.set(2);
    }
        processBigInteger(batch, b, 2, rowId);
    return this;
  }
  public TraceBuilder acc4(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(3)) {
      throw new IllegalStateException("ACC_4 already set");
    } else {
      filled.set(3);
    }
        processBigInteger(batch, b, 3, rowId);
    return this;
  }
  public TraceBuilder acc5(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(4)) {
      throw new IllegalStateException("ACC_5 already set");
    } else {
      filled.set(4);
    }
        processBigInteger(batch, b, 4, rowId);
    return this;
  }
  public TraceBuilder arg1Hi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(5)) {
      throw new IllegalStateException("ARG_1_HI already set");
    } else {
      filled.set(5);
    }
        processBigInteger(batch, b, 5, rowId);
    return this;
  }
  public TraceBuilder arg1Lo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(6)) {
      throw new IllegalStateException("ARG_1_LO already set");
    } else {
      filled.set(6);
    }
        processBigInteger(batch, b, 6, rowId);
    return this;
  }
  public TraceBuilder arg2Hi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(7)) {
      throw new IllegalStateException("ARG_2_HI already set");
    } else {
      filled.set(7);
    }
        processBigInteger(batch, b, 7, rowId);
    return this;
  }
  public TraceBuilder arg2Lo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(8)) {
      throw new IllegalStateException("ARG_2_LO already set");
    } else {
      filled.set(8);
    }
        processBigInteger(batch, b, 8, rowId);
    return this;
  }
  public TraceBuilder bit1(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(10)) {
      throw new IllegalStateException("BIT_1 already set");
    } else {
      filled.set(10);
    }
        processBoolean(batch, b, 10, rowId);
    return this;
  }
  public TraceBuilder bit2(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(11)) {
      throw new IllegalStateException("BIT_2 already set");
    } else {
      filled.set(11);
    }
        processBoolean(batch, b, 11, rowId);
    return this;
  }
  public TraceBuilder bit3(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(12)) {
      throw new IllegalStateException("BIT_3 already set");
    } else {
      filled.set(12);
    }
        processBoolean(batch, b, 12, rowId);
    return this;
  }
  public TraceBuilder bit4(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(13)) {
      throw new IllegalStateException("BIT_4 already set");
    } else {
      filled.set(13);
    }
        processBoolean(batch, b, 13, rowId);
    return this;
  }
  public TraceBuilder bitB3(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(14)) {
      throw new IllegalStateException("BIT_B_3 already set");
    } else {
      filled.set(14);
    }
        processBoolean(batch, b, 14, rowId);
    return this;
  }
  public TraceBuilder bitB4(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(15)) {
      throw new IllegalStateException("BIT_B_4 already set");
    } else {
      filled.set(15);
    }
        processBoolean(batch, b, 15, rowId);
    return this;
  }
  public TraceBuilder bitB5(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(16)) {
      throw new IllegalStateException("BIT_B_5 already set");
    } else {
      filled.set(16);
    }
        processBoolean(batch, b, 16, rowId);
    return this;
  }
  public TraceBuilder bitB6(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(17)) {
      throw new IllegalStateException("BIT_B_6 already set");
    } else {
      filled.set(17);
    }
        processBoolean(batch, b, 17, rowId);
    return this;
  }
  public TraceBuilder bitB7(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(18)) {
      throw new IllegalStateException("BIT_B_7 already set");
    } else {
      filled.set(18);
    }
        processBoolean(batch, b, 18, rowId);
    return this;
  }
  public TraceBuilder bits(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(9)) {
      throw new IllegalStateException("BITS already set");
    } else {
      filled.set(9);
    }
        processBoolean(batch, b, 9, rowId);
    return this;
  }
  public TraceBuilder byte1(VectorizedRowBatch batch, int rowId, UnsignedByte b) {

    if (filled.get(19)) {
      throw new IllegalStateException("BYTE_1 already set");
    } else {
      filled.set(19);
    }
        processUnsignedByte(batch, b, 19, rowId);
    return this;
  }
  public TraceBuilder byte2(VectorizedRowBatch batch, int rowId, UnsignedByte b) {

    if (filled.get(20)) {
      throw new IllegalStateException("BYTE_2 already set");
    } else {
      filled.set(20);
    }
        processUnsignedByte(batch, b, 20, rowId);
    return this;
  }
  public TraceBuilder byte3(VectorizedRowBatch batch, int rowId, UnsignedByte b) {

    if (filled.get(21)) {
      throw new IllegalStateException("BYTE_3 already set");
    } else {
      filled.set(21);
    }
        processUnsignedByte(batch, b, 21, rowId);
    return this;
  }
  public TraceBuilder byte4(VectorizedRowBatch batch, int rowId, UnsignedByte b) {

    if (filled.get(22)) {
      throw new IllegalStateException("BYTE_4 already set");
    } else {
      filled.set(22);
    }
        processUnsignedByte(batch, b, 22, rowId);
    return this;
  }
  public TraceBuilder byte5(VectorizedRowBatch batch, int rowId, UnsignedByte b) {

    if (filled.get(23)) {
      throw new IllegalStateException("BYTE_5 already set");
    } else {
      filled.set(23);
    }
        processUnsignedByte(batch, b, 23, rowId);
    return this;
  }
  public TraceBuilder counter(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(24)) {
      throw new IllegalStateException("COUNTER already set");
    } else {
      filled.set(24);
    }
        processBigInteger(batch, b, 24, rowId);
    return this;
  }
  public TraceBuilder inst(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(25)) {
      throw new IllegalStateException("INST already set");
    } else {
      filled.set(25);
    }
        processBigInteger(batch, b, 25, rowId);
    return this;
  }
  public TraceBuilder isData(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(26)) {
      throw new IllegalStateException("IS_DATA already set");
    } else {
      filled.set(26);
    }
        processBoolean(batch, b, 26, rowId);
    return this;
  }
  public TraceBuilder known(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(27)) {
      throw new IllegalStateException("KNOWN already set");
    } else {
      filled.set(27);
    }
        processBoolean(batch, b, 27, rowId);
    return this;
  }
  public TraceBuilder leftAlignedSuffixHigh(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(28)) {
      throw new IllegalStateException("LEFT_ALIGNED_SUFFIX_HIGH already set");
    } else {
      filled.set(28);
    }
        processBigInteger(batch, b, 28, rowId);
    return this;
  }
  public TraceBuilder leftAlignedSuffixLow(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(29)) {
      throw new IllegalStateException("LEFT_ALIGNED_SUFFIX_LOW already set");
    } else {
      filled.set(29);
    }
        processBigInteger(batch, b, 29, rowId);
    return this;
  }
  public TraceBuilder low3(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(30)) {
      throw new IllegalStateException("LOW_3 already set");
    } else {
      filled.set(30);
    }
        processBigInteger(batch, b, 30, rowId);
    return this;
  }
  public TraceBuilder microShiftParameter(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(31)) {
      throw new IllegalStateException("MICRO_SHIFT_PARAMETER already set");
    } else {
      filled.set(31);
    }
        processBigInteger(batch, b, 31, rowId);
    return this;
  }
  public TraceBuilder neg(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(32)) {
      throw new IllegalStateException("NEG already set");
    } else {
      filled.set(32);
    }
        processBoolean(batch, b, 32, rowId);
    return this;
  }
  public TraceBuilder oneLineInstruction(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(34)) {
      throw new IllegalStateException("ONE_LINE_INSTRUCTION already set");
    } else {
      filled.set(34);
    }
        processBoolean(batch, b, 34, rowId);
    return this;
  }
  public TraceBuilder ones(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(33)) {
      throw new IllegalStateException("ONES already set");
    } else {
      filled.set(33);
    }
        processBigInteger(batch, b, 33, rowId);
    return this;
  }
  public TraceBuilder resHi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(35)) {
      throw new IllegalStateException("RES_HI already set");
    } else {
      filled.set(35);
    }
        processBigInteger(batch, b, 35, rowId);
    return this;
  }
  public TraceBuilder resLo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(36)) {
      throw new IllegalStateException("RES_LO already set");
    } else {
      filled.set(36);
    }
        processBigInteger(batch, b, 36, rowId);
    return this;
  }
  public TraceBuilder rightAlignedPrefixHigh(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(37)) {
      throw new IllegalStateException("RIGHT_ALIGNED_PREFIX_HIGH already set");
    } else {
      filled.set(37);
    }
        processBigInteger(batch, b, 37, rowId);
    return this;
  }
  public TraceBuilder rightAlignedPrefixLow(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(38)) {
      throw new IllegalStateException("RIGHT_ALIGNED_PREFIX_LOW already set");
    } else {
      filled.set(38);
    }
        processBigInteger(batch, b, 38, rowId);
    return this;
  }
  public TraceBuilder shb3Hi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(39)) {
      throw new IllegalStateException("SHB_3_HI already set");
    } else {
      filled.set(39);
    }
        processBigInteger(batch, b, 39, rowId);
    return this;
  }
  public TraceBuilder shb3Lo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(40)) {
      throw new IllegalStateException("SHB_3_LO already set");
    } else {
      filled.set(40);
    }
        processBigInteger(batch, b, 40, rowId);
    return this;
  }
  public TraceBuilder shb4Hi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(41)) {
      throw new IllegalStateException("SHB_4_HI already set");
    } else {
      filled.set(41);
    }
        processBigInteger(batch, b, 41, rowId);
    return this;
  }
  public TraceBuilder shb4Lo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(42)) {
      throw new IllegalStateException("SHB_4_LO already set");
    } else {
      filled.set(42);
    }
        processBigInteger(batch, b, 42, rowId);
    return this;
  }
  public TraceBuilder shb5Hi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(43)) {
      throw new IllegalStateException("SHB_5_HI already set");
    } else {
      filled.set(43);
    }
        processBigInteger(batch, b, 43, rowId);
    return this;
  }
  public TraceBuilder shb5Lo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(44)) {
      throw new IllegalStateException("SHB_5_LO already set");
    } else {
      filled.set(44);
    }
        processBigInteger(batch, b, 44, rowId);
    return this;
  }
  public TraceBuilder shb6Hi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(45)) {
      throw new IllegalStateException("SHB_6_HI already set");
    } else {
      filled.set(45);
    }
        processBigInteger(batch, b, 45, rowId);
    return this;
  }
  public TraceBuilder shb6Lo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(46)) {
      throw new IllegalStateException("SHB_6_LO already set");
    } else {
      filled.set(46);
    }
        processBigInteger(batch, b, 46, rowId);
    return this;
  }
  public TraceBuilder shb7Hi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(47)) {
      throw new IllegalStateException("SHB_7_HI already set");
    } else {
      filled.set(47);
    }
        processBigInteger(batch, b, 47, rowId);
    return this;
  }
  public TraceBuilder shb7Lo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(48)) {
      throw new IllegalStateException("SHB_7_LO already set");
    } else {
      filled.set(48);
    }
        processBigInteger(batch, b, 48, rowId);
    return this;
  }
  public TraceBuilder shiftDirection(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(49)) {
      throw new IllegalStateException("SHIFT_DIRECTION already set");
    } else {
      filled.set(49);
    }
        processBoolean(batch, b, 49, rowId);
    return this;
  }
  public TraceBuilder shiftStamp(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(50)) {
      throw new IllegalStateException("SHIFT_STAMP already set");
    } else {
      filled.set(50);
    }
        processBigInteger(batch, b, 50, rowId);
    return this;
  }

        public void validateRowAndFlush(VectorizedRowBatch batch, Writer writer)  throws IOException {
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
        throw new IllegalStateException("ACC_5 has not been filled");
        }

        if (!filled.get(5)) {
        throw new IllegalStateException("ARG_1_HI has not been filled");
        }

        if (!filled.get(6)) {
        throw new IllegalStateException("ARG_1_LO has not been filled");
        }

        if (!filled.get(7)) {
        throw new IllegalStateException("ARG_2_HI has not been filled");
        }

        if (!filled.get(8)) {
        throw new IllegalStateException("ARG_2_LO has not been filled");
        }

        if (!filled.get(9)) {
        throw new IllegalStateException("BITS has not been filled");
        }

        if (!filled.get(10)) {
        throw new IllegalStateException("BIT_1 has not been filled");
        }

        if (!filled.get(11)) {
        throw new IllegalStateException("BIT_2 has not been filled");
        }

        if (!filled.get(12)) {
        throw new IllegalStateException("BIT_3 has not been filled");
        }

        if (!filled.get(13)) {
        throw new IllegalStateException("BIT_4 has not been filled");
        }

        if (!filled.get(14)) {
        throw new IllegalStateException("BIT_B_3 has not been filled");
        }

        if (!filled.get(15)) {
        throw new IllegalStateException("BIT_B_4 has not been filled");
        }

        if (!filled.get(16)) {
        throw new IllegalStateException("BIT_B_5 has not been filled");
        }

        if (!filled.get(17)) {
        throw new IllegalStateException("BIT_B_6 has not been filled");
        }

        if (!filled.get(18)) {
        throw new IllegalStateException("BIT_B_7 has not been filled");
        }

        if (!filled.get(19)) {
        throw new IllegalStateException("BYTE_1 has not been filled");
        }

        if (!filled.get(20)) {
        throw new IllegalStateException("BYTE_2 has not been filled");
        }

        if (!filled.get(21)) {
        throw new IllegalStateException("BYTE_3 has not been filled");
        }

        if (!filled.get(22)) {
        throw new IllegalStateException("BYTE_4 has not been filled");
        }

        if (!filled.get(23)) {
        throw new IllegalStateException("BYTE_5 has not been filled");
        }

        if (!filled.get(24)) {
        throw new IllegalStateException("COUNTER has not been filled");
        }

        if (!filled.get(25)) {
        throw new IllegalStateException("INST has not been filled");
        }

        if (!filled.get(26)) {
        throw new IllegalStateException("IS_DATA has not been filled");
        }

        if (!filled.get(27)) {
        throw new IllegalStateException("KNOWN has not been filled");
        }

        if (!filled.get(28)) {
        throw new IllegalStateException("LEFT_ALIGNED_SUFFIX_HIGH has not been filled");
        }

        if (!filled.get(29)) {
        throw new IllegalStateException("LEFT_ALIGNED_SUFFIX_LOW has not been filled");
        }

        if (!filled.get(30)) {
        throw new IllegalStateException("LOW_3 has not been filled");
        }

        if (!filled.get(31)) {
        throw new IllegalStateException("MICRO_SHIFT_PARAMETER has not been filled");
        }

        if (!filled.get(32)) {
        throw new IllegalStateException("NEG has not been filled");
        }

        if (!filled.get(33)) {
        throw new IllegalStateException("ONES has not been filled");
        }

        if (!filled.get(34)) {
        throw new IllegalStateException("ONE_LINE_INSTRUCTION has not been filled");
        }

        if (!filled.get(35)) {
        throw new IllegalStateException("RES_HI has not been filled");
        }

        if (!filled.get(36)) {
        throw new IllegalStateException("RES_LO has not been filled");
        }

        if (!filled.get(37)) {
        throw new IllegalStateException("RIGHT_ALIGNED_PREFIX_HIGH has not been filled");
        }

        if (!filled.get(38)) {
        throw new IllegalStateException("RIGHT_ALIGNED_PREFIX_LOW has not been filled");
        }

        if (!filled.get(39)) {
        throw new IllegalStateException("SHB_3_HI has not been filled");
        }

        if (!filled.get(40)) {
        throw new IllegalStateException("SHB_3_LO has not been filled");
        }

        if (!filled.get(41)) {
        throw new IllegalStateException("SHB_4_HI has not been filled");
        }

        if (!filled.get(42)) {
        throw new IllegalStateException("SHB_4_LO has not been filled");
        }

        if (!filled.get(43)) {
        throw new IllegalStateException("SHB_5_HI has not been filled");
        }

        if (!filled.get(44)) {
        throw new IllegalStateException("SHB_5_LO has not been filled");
        }

        if (!filled.get(45)) {
        throw new IllegalStateException("SHB_6_HI has not been filled");
        }

        if (!filled.get(46)) {
        throw new IllegalStateException("SHB_6_LO has not been filled");
        }

        if (!filled.get(47)) {
        throw new IllegalStateException("SHB_7_HI has not been filled");
        }

        if (!filled.get(48)) {
        throw new IllegalStateException("SHB_7_LO has not been filled");
        }

        if (!filled.get(49)) {
        throw new IllegalStateException("SHIFT_DIRECTION has not been filled");
        }

        if (!filled.get(50)) {
        throw new IllegalStateException("SHIFT_STAMP has not been filled");
        }


        filled.clear();

        if (batch.size == batch.getMaxSize()) {
        writer.addRowBatch(batch);
        batch.reset();
        }
        }

        private void processBigInteger(VectorizedRowBatch batch, BigInteger b, int columnId, int rowId) {
        BytesColumnVector columnVector = (BytesColumnVector) batch.cols[columnId];
        columnVector.setVal(rowId, b.toByteArray());
        }

        private void processUnsignedByte(VectorizedRowBatch batch, UnsignedByte b, int columnId, int rowId) {
        BytesColumnVector columnVector = (BytesColumnVector) batch.cols[columnId];
        columnVector.setVal(rowId, new byte[]{b.toByte()});
        }

        private void processBoolean(VectorizedRowBatch batch, Boolean b, int columnId, int rowId) {
        LongColumnVector columnVector = (LongColumnVector) batch.cols[columnId];
        columnVector.vector[rowId] = b ? 1L : 0L;
        }
}
