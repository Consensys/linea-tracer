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

package net.consensys.linea.zktracer.module.rlpAddr;

import com.fasterxml.jackson.annotation.JsonProperty;

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
  public TraceBuilder acc(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(0)) {
      throw new IllegalStateException("ACC already set");
    } else {
      filled.set(0);
    }
        processBigInteger(batch, b, 0, rowId);
    return this;
  }
  public TraceBuilder accBytesize(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(1)) {
      throw new IllegalStateException("ACC_BYTESIZE already set");
    } else {
      filled.set(1);
    }
        processBigInteger(batch, b, 1, rowId);
    return this;
  }
  public TraceBuilder addrHi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(2)) {
      throw new IllegalStateException("ADDR_HI already set");
    } else {
      filled.set(2);
    }
        processBigInteger(batch, b, 2, rowId);
    return this;
  }
  public TraceBuilder addrLo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(3)) {
      throw new IllegalStateException("ADDR_LO already set");
    } else {
      filled.set(3);
    }
        processBigInteger(batch, b, 3, rowId);
    return this;
  }
  public TraceBuilder bit1(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(4)) {
      throw new IllegalStateException("BIT1 already set");
    } else {
      filled.set(4);
    }
        processBoolean(batch, b, 4, rowId);
    return this;
  }
  public TraceBuilder bitAcc(VectorizedRowBatch batch, int rowId, UnsignedByte b) {

    if (filled.get(5)) {
      throw new IllegalStateException("BIT_ACC already set");
    } else {
      filled.set(5);
    }
        processUnsignedByte(batch, b, 5, rowId);
    return this;
  }
  public TraceBuilder byte1(VectorizedRowBatch batch, int rowId, UnsignedByte b) {

    if (filled.get(6)) {
      throw new IllegalStateException("BYTE1 already set");
    } else {
      filled.set(6);
    }
        processUnsignedByte(batch, b, 6, rowId);
    return this;
  }
  public TraceBuilder counter(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(7)) {
      throw new IllegalStateException("COUNTER already set");
    } else {
      filled.set(7);
    }
        processBigInteger(batch, b, 7, rowId);
    return this;
  }
  public TraceBuilder depAddrHi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(8)) {
      throw new IllegalStateException("DEP_ADDR_HI already set");
    } else {
      filled.set(8);
    }
        processBigInteger(batch, b, 8, rowId);
    return this;
  }
  public TraceBuilder depAddrLo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(9)) {
      throw new IllegalStateException("DEP_ADDR_LO already set");
    } else {
      filled.set(9);
    }
        processBigInteger(batch, b, 9, rowId);
    return this;
  }
  public TraceBuilder index(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(10)) {
      throw new IllegalStateException("INDEX already set");
    } else {
      filled.set(10);
    }
        processBigInteger(batch, b, 10, rowId);
    return this;
  }
  public TraceBuilder kecHi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(11)) {
      throw new IllegalStateException("KEC_HI already set");
    } else {
      filled.set(11);
    }
        processBigInteger(batch, b, 11, rowId);
    return this;
  }
  public TraceBuilder kecLo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(12)) {
      throw new IllegalStateException("KEC_LO already set");
    } else {
      filled.set(12);
    }
        processBigInteger(batch, b, 12, rowId);
    return this;
  }
  public TraceBuilder lc(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(13)) {
      throw new IllegalStateException("LC already set");
    } else {
      filled.set(13);
    }
        processBoolean(batch, b, 13, rowId);
    return this;
  }
  public TraceBuilder limb(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(14)) {
      throw new IllegalStateException("LIMB already set");
    } else {
      filled.set(14);
    }
        processBigInteger(batch, b, 14, rowId);
    return this;
  }
  public TraceBuilder nBytes(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(24)) {
      throw new IllegalStateException("nBYTES already set");
    } else {
      filled.set(24);
    }
        processBigInteger(batch, b, 24, rowId);
    return this;
  }
  public TraceBuilder nonce(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(15)) {
      throw new IllegalStateException("NONCE already set");
    } else {
      filled.set(15);
    }
        processBigInteger(batch, b, 15, rowId);
    return this;
  }
  public TraceBuilder power(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(16)) {
      throw new IllegalStateException("POWER already set");
    } else {
      filled.set(16);
    }
        processBigInteger(batch, b, 16, rowId);
    return this;
  }
  public TraceBuilder recipe(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(17)) {
      throw new IllegalStateException("RECIPE already set");
    } else {
      filled.set(17);
    }
        processBigInteger(batch, b, 17, rowId);
    return this;
  }
  public TraceBuilder recipe1(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(18)) {
      throw new IllegalStateException("RECIPE_1 already set");
    } else {
      filled.set(18);
    }
        processBoolean(batch, b, 18, rowId);
    return this;
  }
  public TraceBuilder recipe2(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(19)) {
      throw new IllegalStateException("RECIPE_2 already set");
    } else {
      filled.set(19);
    }
        processBoolean(batch, b, 19, rowId);
    return this;
  }
  public TraceBuilder saltHi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(20)) {
      throw new IllegalStateException("SALT_HI already set");
    } else {
      filled.set(20);
    }
        processBigInteger(batch, b, 20, rowId);
    return this;
  }
  public TraceBuilder saltLo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(21)) {
      throw new IllegalStateException("SALT_LO already set");
    } else {
      filled.set(21);
    }
        processBigInteger(batch, b, 21, rowId);
    return this;
  }
  public TraceBuilder stamp(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(22)) {
      throw new IllegalStateException("STAMP already set");
    } else {
      filled.set(22);
    }
        processBigInteger(batch, b, 22, rowId);
    return this;
  }
  public TraceBuilder tinyNonZeroNonce(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(23)) {
      throw new IllegalStateException("TINY_NON_ZERO_NONCE already set");
    } else {
      filled.set(23);
    }
        processBoolean(batch, b, 23, rowId);
    return this;
  }

        public void validateRowAndFlush(VectorizedRowBatch batch, Writer writer)  throws IOException {
        if (!filled.get(0)) {
        throw new IllegalStateException("ACC has not been filled");
        }

        if (!filled.get(1)) {
        throw new IllegalStateException("ACC_BYTESIZE has not been filled");
        }

        if (!filled.get(2)) {
        throw new IllegalStateException("ADDR_HI has not been filled");
        }

        if (!filled.get(3)) {
        throw new IllegalStateException("ADDR_LO has not been filled");
        }

        if (!filled.get(4)) {
        throw new IllegalStateException("BIT1 has not been filled");
        }

        if (!filled.get(5)) {
        throw new IllegalStateException("BIT_ACC has not been filled");
        }

        if (!filled.get(6)) {
        throw new IllegalStateException("BYTE1 has not been filled");
        }

        if (!filled.get(7)) {
        throw new IllegalStateException("COUNTER has not been filled");
        }

        if (!filled.get(8)) {
        throw new IllegalStateException("DEP_ADDR_HI has not been filled");
        }

        if (!filled.get(9)) {
        throw new IllegalStateException("DEP_ADDR_LO has not been filled");
        }

        if (!filled.get(10)) {
        throw new IllegalStateException("INDEX has not been filled");
        }

        if (!filled.get(11)) {
        throw new IllegalStateException("KEC_HI has not been filled");
        }

        if (!filled.get(12)) {
        throw new IllegalStateException("KEC_LO has not been filled");
        }

        if (!filled.get(13)) {
        throw new IllegalStateException("LC has not been filled");
        }

        if (!filled.get(14)) {
        throw new IllegalStateException("LIMB has not been filled");
        }

        if (!filled.get(15)) {
        throw new IllegalStateException("NONCE has not been filled");
        }

        if (!filled.get(16)) {
        throw new IllegalStateException("POWER has not been filled");
        }

        if (!filled.get(17)) {
        throw new IllegalStateException("RECIPE has not been filled");
        }

        if (!filled.get(18)) {
        throw new IllegalStateException("RECIPE_1 has not been filled");
        }

        if (!filled.get(19)) {
        throw new IllegalStateException("RECIPE_2 has not been filled");
        }

        if (!filled.get(20)) {
        throw new IllegalStateException("SALT_HI has not been filled");
        }

        if (!filled.get(21)) {
        throw new IllegalStateException("SALT_LO has not been filled");
        }

        if (!filled.get(22)) {
        throw new IllegalStateException("STAMP has not been filled");
        }

        if (!filled.get(23)) {
        throw new IllegalStateException("TINY_NON_ZERO_NONCE has not been filled");
        }

        if (!filled.get(24)) {
        throw new IllegalStateException("nBYTES has not been filled");
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
