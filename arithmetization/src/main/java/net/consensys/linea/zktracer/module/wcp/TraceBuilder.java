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

package net.consensys.linea.zktracer.module.wcp;

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
  public TraceBuilder acc6(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(5)) {
      throw new IllegalStateException("ACC_6 already set");
    } else {
      filled.set(5);
    }
        processBigInteger(batch, b, 5, rowId);
    return this;
  }
  public TraceBuilder argument1Hi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(6)) {
      throw new IllegalStateException("ARGUMENT_1_HI already set");
    } else {
      filled.set(6);
    }
        processBigInteger(batch, b, 6, rowId);
    return this;
  }
  public TraceBuilder argument1Lo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(7)) {
      throw new IllegalStateException("ARGUMENT_1_LO already set");
    } else {
      filled.set(7);
    }
        processBigInteger(batch, b, 7, rowId);
    return this;
  }
  public TraceBuilder argument2Hi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(8)) {
      throw new IllegalStateException("ARGUMENT_2_HI already set");
    } else {
      filled.set(8);
    }
        processBigInteger(batch, b, 8, rowId);
    return this;
  }
  public TraceBuilder argument2Lo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(9)) {
      throw new IllegalStateException("ARGUMENT_2_LO already set");
    } else {
      filled.set(9);
    }
        processBigInteger(batch, b, 9, rowId);
    return this;
  }
  public TraceBuilder bit1(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(11)) {
      throw new IllegalStateException("BIT_1 already set");
    } else {
      filled.set(11);
    }
        processBoolean(batch, b, 11, rowId);
    return this;
  }
  public TraceBuilder bit2(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(12)) {
      throw new IllegalStateException("BIT_2 already set");
    } else {
      filled.set(12);
    }
        processBoolean(batch, b, 12, rowId);
    return this;
  }
  public TraceBuilder bit3(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(13)) {
      throw new IllegalStateException("BIT_3 already set");
    } else {
      filled.set(13);
    }
        processBoolean(batch, b, 13, rowId);
    return this;
  }
  public TraceBuilder bit4(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(14)) {
      throw new IllegalStateException("BIT_4 already set");
    } else {
      filled.set(14);
    }
        processBoolean(batch, b, 14, rowId);
    return this;
  }
  public TraceBuilder bits(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(10)) {
      throw new IllegalStateException("BITS already set");
    } else {
      filled.set(10);
    }
        processBoolean(batch, b, 10, rowId);
    return this;
  }
  public TraceBuilder byte1(VectorizedRowBatch batch, int rowId, UnsignedByte b) {

    if (filled.get(15)) {
      throw new IllegalStateException("BYTE_1 already set");
    } else {
      filled.set(15);
    }
        processUnsignedByte(batch, b, 15, rowId);
    return this;
  }
  public TraceBuilder byte2(VectorizedRowBatch batch, int rowId, UnsignedByte b) {

    if (filled.get(16)) {
      throw new IllegalStateException("BYTE_2 already set");
    } else {
      filled.set(16);
    }
        processUnsignedByte(batch, b, 16, rowId);
    return this;
  }
  public TraceBuilder byte3(VectorizedRowBatch batch, int rowId, UnsignedByte b) {

    if (filled.get(17)) {
      throw new IllegalStateException("BYTE_3 already set");
    } else {
      filled.set(17);
    }
        processUnsignedByte(batch, b, 17, rowId);
    return this;
  }
  public TraceBuilder byte4(VectorizedRowBatch batch, int rowId, UnsignedByte b) {

    if (filled.get(18)) {
      throw new IllegalStateException("BYTE_4 already set");
    } else {
      filled.set(18);
    }
        processUnsignedByte(batch, b, 18, rowId);
    return this;
  }
  public TraceBuilder byte5(VectorizedRowBatch batch, int rowId, UnsignedByte b) {

    if (filled.get(19)) {
      throw new IllegalStateException("BYTE_5 already set");
    } else {
      filled.set(19);
    }
        processUnsignedByte(batch, b, 19, rowId);
    return this;
  }
  public TraceBuilder byte6(VectorizedRowBatch batch, int rowId, UnsignedByte b) {

    if (filled.get(20)) {
      throw new IllegalStateException("BYTE_6 already set");
    } else {
      filled.set(20);
    }
        processUnsignedByte(batch, b, 20, rowId);
    return this;
  }
  public TraceBuilder counter(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(21)) {
      throw new IllegalStateException("COUNTER already set");
    } else {
      filled.set(21);
    }
        processBigInteger(batch, b, 21, rowId);
    return this;
  }
  public TraceBuilder inst(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(22)) {
      throw new IllegalStateException("INST already set");
    } else {
      filled.set(22);
    }
        processBigInteger(batch, b, 22, rowId);
    return this;
  }
  public TraceBuilder neg1(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(23)) {
      throw new IllegalStateException("NEG_1 already set");
    } else {
      filled.set(23);
    }
        processBoolean(batch, b, 23, rowId);
    return this;
  }
  public TraceBuilder neg2(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(24)) {
      throw new IllegalStateException("NEG_2 already set");
    } else {
      filled.set(24);
    }
        processBoolean(batch, b, 24, rowId);
    return this;
  }
  public TraceBuilder oneLineInstruction(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(25)) {
      throw new IllegalStateException("ONE_LINE_INSTRUCTION already set");
    } else {
      filled.set(25);
    }
        processBoolean(batch, b, 25, rowId);
    return this;
  }
  public TraceBuilder resultHi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(26)) {
      throw new IllegalStateException("RESULT_HI already set");
    } else {
      filled.set(26);
    }
        processBigInteger(batch, b, 26, rowId);
    return this;
  }
  public TraceBuilder resultLo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(27)) {
      throw new IllegalStateException("RESULT_LO already set");
    } else {
      filled.set(27);
    }
        processBigInteger(batch, b, 27, rowId);
    return this;
  }
  public TraceBuilder wordComparisonStamp(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(28)) {
      throw new IllegalStateException("WORD_COMPARISON_STAMP already set");
    } else {
      filled.set(28);
    }
        processBigInteger(batch, b, 28, rowId);
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
        throw new IllegalStateException("ACC_6 has not been filled");
        }

        if (!filled.get(6)) {
        throw new IllegalStateException("ARGUMENT_1_HI has not been filled");
        }

        if (!filled.get(7)) {
        throw new IllegalStateException("ARGUMENT_1_LO has not been filled");
        }

        if (!filled.get(8)) {
        throw new IllegalStateException("ARGUMENT_2_HI has not been filled");
        }

        if (!filled.get(9)) {
        throw new IllegalStateException("ARGUMENT_2_LO has not been filled");
        }

        if (!filled.get(10)) {
        throw new IllegalStateException("BITS has not been filled");
        }

        if (!filled.get(11)) {
        throw new IllegalStateException("BIT_1 has not been filled");
        }

        if (!filled.get(12)) {
        throw new IllegalStateException("BIT_2 has not been filled");
        }

        if (!filled.get(13)) {
        throw new IllegalStateException("BIT_3 has not been filled");
        }

        if (!filled.get(14)) {
        throw new IllegalStateException("BIT_4 has not been filled");
        }

        if (!filled.get(15)) {
        throw new IllegalStateException("BYTE_1 has not been filled");
        }

        if (!filled.get(16)) {
        throw new IllegalStateException("BYTE_2 has not been filled");
        }

        if (!filled.get(17)) {
        throw new IllegalStateException("BYTE_3 has not been filled");
        }

        if (!filled.get(18)) {
        throw new IllegalStateException("BYTE_4 has not been filled");
        }

        if (!filled.get(19)) {
        throw new IllegalStateException("BYTE_5 has not been filled");
        }

        if (!filled.get(20)) {
        throw new IllegalStateException("BYTE_6 has not been filled");
        }

        if (!filled.get(21)) {
        throw new IllegalStateException("COUNTER has not been filled");
        }

        if (!filled.get(22)) {
        throw new IllegalStateException("INST has not been filled");
        }

        if (!filled.get(23)) {
        throw new IllegalStateException("NEG_1 has not been filled");
        }

        if (!filled.get(24)) {
        throw new IllegalStateException("NEG_2 has not been filled");
        }

        if (!filled.get(25)) {
        throw new IllegalStateException("ONE_LINE_INSTRUCTION has not been filled");
        }

        if (!filled.get(26)) {
        throw new IllegalStateException("RESULT_HI has not been filled");
        }

        if (!filled.get(27)) {
        throw new IllegalStateException("RESULT_LO has not been filled");
        }

        if (!filled.get(28)) {
        throw new IllegalStateException("WORD_COMPARISON_STAMP has not been filled");
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
