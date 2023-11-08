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

package net.consensys.linea.zktracer.module.romLex;

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
  public TraceBuilder addrHi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(0)) {
      throw new IllegalStateException("ADDR_HI already set");
    } else {
      filled.set(0);
    }
        processBigInteger(batch, b, 0, rowId);
    return this;
  }
  public TraceBuilder addrLo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(1)) {
      throw new IllegalStateException("ADDR_LO already set");
    } else {
      filled.set(1);
    }
        processBigInteger(batch, b, 1, rowId);
    return this;
  }
  public TraceBuilder codeFragmentIndex(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(2)) {
      throw new IllegalStateException("CODE_FRAGMENT_INDEX already set");
    } else {
      filled.set(2);
    }
        processBigInteger(batch, b, 2, rowId);
    return this;
  }
  public TraceBuilder codeFragmentIndexInfty(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(3)) {
      throw new IllegalStateException("CODE_FRAGMENT_INDEX_INFTY already set");
    } else {
      filled.set(3);
    }
        processBigInteger(batch, b, 3, rowId);
    return this;
  }
  public TraceBuilder codeSize(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(4)) {
      throw new IllegalStateException("CODE_SIZE already set");
    } else {
      filled.set(4);
    }
        processBigInteger(batch, b, 4, rowId);
    return this;
  }
  public TraceBuilder commitToState(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(5)) {
      throw new IllegalStateException("COMMIT_TO_STATE already set");
    } else {
      filled.set(5);
    }
        processBoolean(batch, b, 5, rowId);
    return this;
  }
  public TraceBuilder depNumber(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(6)) {
      throw new IllegalStateException("DEP_NUMBER already set");
    } else {
      filled.set(6);
    }
        processBigInteger(batch, b, 6, rowId);
    return this;
  }
  public TraceBuilder depStatus(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(7)) {
      throw new IllegalStateException("DEP_STATUS already set");
    } else {
      filled.set(7);
    }
        processBoolean(batch, b, 7, rowId);
    return this;
  }
  public TraceBuilder readFromState(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(8)) {
      throw new IllegalStateException("READ_FROM_STATE already set");
    } else {
      filled.set(8);
    }
        processBoolean(batch, b, 8, rowId);
    return this;
  }

        public void validateRowAndFlush(VectorizedRowBatch batch, Writer writer)  throws IOException {
        if (!filled.get(0)) {
        throw new IllegalStateException("ADDR_HI has not been filled");
        }

        if (!filled.get(1)) {
        throw new IllegalStateException("ADDR_LO has not been filled");
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
        throw new IllegalStateException("COMMIT_TO_STATE has not been filled");
        }

        if (!filled.get(6)) {
        throw new IllegalStateException("DEP_NUMBER has not been filled");
        }

        if (!filled.get(7)) {
        throw new IllegalStateException("DEP_STATUS has not been filled");
        }

        if (!filled.get(8)) {
        throw new IllegalStateException("READ_FROM_STATE has not been filled");
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
