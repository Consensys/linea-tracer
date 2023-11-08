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

package net.consensys.linea.zktracer.module.txn_data;

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
  public TraceBuilder absTxNum(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(0)) {
      throw new IllegalStateException("ABS_TX_NUM already set");
    } else {
      filled.set(0);
    }
        processBigInteger(batch, b, 0, rowId);
    return this;
  }
  public TraceBuilder absTxNumMax(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(1)) {
      throw new IllegalStateException("ABS_TX_NUM_MAX already set");
    } else {
      filled.set(1);
    }
        processBigInteger(batch, b, 1, rowId);
    return this;
  }
  public TraceBuilder basefee(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(2)) {
      throw new IllegalStateException("BASEFEE already set");
    } else {
      filled.set(2);
    }
        processBigInteger(batch, b, 2, rowId);
    return this;
  }
  public TraceBuilder btcNum(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(3)) {
      throw new IllegalStateException("BTC_NUM already set");
    } else {
      filled.set(3);
    }
        processBigInteger(batch, b, 3, rowId);
    return this;
  }
  public TraceBuilder btcNumMax(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(4)) {
      throw new IllegalStateException("BTC_NUM_MAX already set");
    } else {
      filled.set(4);
    }
        processBigInteger(batch, b, 4, rowId);
    return this;
  }
  public TraceBuilder callDataSize(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(5)) {
      throw new IllegalStateException("CALL_DATA_SIZE already set");
    } else {
      filled.set(5);
    }
        processBigInteger(batch, b, 5, rowId);
    return this;
  }
  public TraceBuilder codeFragmentIndex(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(6)) {
      throw new IllegalStateException("CODE_FRAGMENT_INDEX already set");
    } else {
      filled.set(6);
    }
        processBigInteger(batch, b, 6, rowId);
    return this;
  }
  public TraceBuilder coinbaseHi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(7)) {
      throw new IllegalStateException("COINBASE_HI already set");
    } else {
      filled.set(7);
    }
        processBigInteger(batch, b, 7, rowId);
    return this;
  }
  public TraceBuilder coinbaseLo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(8)) {
      throw new IllegalStateException("COINBASE_LO already set");
    } else {
      filled.set(8);
    }
        processBigInteger(batch, b, 8, rowId);
    return this;
  }
  public TraceBuilder ct(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(9)) {
      throw new IllegalStateException("CT already set");
    } else {
      filled.set(9);
    }
        processBigInteger(batch, b, 9, rowId);
    return this;
  }
  public TraceBuilder cumulativeConsumedGas(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(10)) {
      throw new IllegalStateException("CUMULATIVE_CONSUMED_GAS already set");
    } else {
      filled.set(10);
    }
        processBigInteger(batch, b, 10, rowId);
    return this;
  }
  public TraceBuilder fromHi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(11)) {
      throw new IllegalStateException("FROM_HI already set");
    } else {
      filled.set(11);
    }
        processBigInteger(batch, b, 11, rowId);
    return this;
  }
  public TraceBuilder fromLo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(12)) {
      throw new IllegalStateException("FROM_LO already set");
    } else {
      filled.set(12);
    }
        processBigInteger(batch, b, 12, rowId);
    return this;
  }
  public TraceBuilder gasLimit(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(13)) {
      throw new IllegalStateException("GAS_LIMIT already set");
    } else {
      filled.set(13);
    }
        processBigInteger(batch, b, 13, rowId);
    return this;
  }
  public TraceBuilder gasPrice(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(14)) {
      throw new IllegalStateException("GAS_PRICE already set");
    } else {
      filled.set(14);
    }
        processBigInteger(batch, b, 14, rowId);
    return this;
  }
  public TraceBuilder initCodeSize(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(17)) {
      throw new IllegalStateException("INIT_CODE_SIZE already set");
    } else {
      filled.set(17);
    }
        processBigInteger(batch, b, 17, rowId);
    return this;
  }
  public TraceBuilder initialBalance(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(15)) {
      throw new IllegalStateException("INITIAL_BALANCE already set");
    } else {
      filled.set(15);
    }
        processBigInteger(batch, b, 15, rowId);
    return this;
  }
  public TraceBuilder initialGas(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(16)) {
      throw new IllegalStateException("INITIAL_GAS already set");
    } else {
      filled.set(16);
    }
        processBigInteger(batch, b, 16, rowId);
    return this;
  }
  public TraceBuilder isDep(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(18)) {
      throw new IllegalStateException("IS_DEP already set");
    } else {
      filled.set(18);
    }
        processBoolean(batch, b, 18, rowId);
    return this;
  }
  public TraceBuilder leftoverGas(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(19)) {
      throw new IllegalStateException("LEFTOVER_GAS already set");
    } else {
      filled.set(19);
    }
        processBigInteger(batch, b, 19, rowId);
    return this;
  }
  public TraceBuilder nonce(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(20)) {
      throw new IllegalStateException("NONCE already set");
    } else {
      filled.set(20);
    }
        processBigInteger(batch, b, 20, rowId);
    return this;
  }
  public TraceBuilder outgoingHi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(21)) {
      throw new IllegalStateException("OUTGOING_HI already set");
    } else {
      filled.set(21);
    }
        processBigInteger(batch, b, 21, rowId);
    return this;
  }
  public TraceBuilder outgoingLo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(22)) {
      throw new IllegalStateException("OUTGOING_LO already set");
    } else {
      filled.set(22);
    }
        processBigInteger(batch, b, 22, rowId);
    return this;
  }
  public TraceBuilder outgoingRlpTxnrcpt(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(23)) {
      throw new IllegalStateException("OUTGOING_RLP_TXNRCPT already set");
    } else {
      filled.set(23);
    }
        processBigInteger(batch, b, 23, rowId);
    return this;
  }
  public TraceBuilder phaseRlpTxn(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(24)) {
      throw new IllegalStateException("PHASE_RLP_TXN already set");
    } else {
      filled.set(24);
    }
        processBigInteger(batch, b, 24, rowId);
    return this;
  }
  public TraceBuilder phaseRlpTxnrcpt(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(25)) {
      throw new IllegalStateException("PHASE_RLP_TXNRCPT already set");
    } else {
      filled.set(25);
    }
        processBigInteger(batch, b, 25, rowId);
    return this;
  }
  public TraceBuilder refundAmount(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(26)) {
      throw new IllegalStateException("REFUND_AMOUNT already set");
    } else {
      filled.set(26);
    }
        processBigInteger(batch, b, 26, rowId);
    return this;
  }
  public TraceBuilder refundCounter(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(27)) {
      throw new IllegalStateException("REFUND_COUNTER already set");
    } else {
      filled.set(27);
    }
        processBigInteger(batch, b, 27, rowId);
    return this;
  }
  public TraceBuilder relTxNum(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(28)) {
      throw new IllegalStateException("REL_TX_NUM already set");
    } else {
      filled.set(28);
    }
        processBigInteger(batch, b, 28, rowId);
    return this;
  }
  public TraceBuilder relTxNumMax(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(29)) {
      throw new IllegalStateException("REL_TX_NUM_MAX already set");
    } else {
      filled.set(29);
    }
        processBigInteger(batch, b, 29, rowId);
    return this;
  }
  public TraceBuilder requiresEvmExecution(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(30)) {
      throw new IllegalStateException("REQUIRES_EVM_EXECUTION already set");
    } else {
      filled.set(30);
    }
        processBoolean(batch, b, 30, rowId);
    return this;
  }
  public TraceBuilder statusCode(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(31)) {
      throw new IllegalStateException("STATUS_CODE already set");
    } else {
      filled.set(31);
    }
        processBoolean(batch, b, 31, rowId);
    return this;
  }
  public TraceBuilder toHi(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(32)) {
      throw new IllegalStateException("TO_HI already set");
    } else {
      filled.set(32);
    }
        processBigInteger(batch, b, 32, rowId);
    return this;
  }
  public TraceBuilder toLo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(33)) {
      throw new IllegalStateException("TO_LO already set");
    } else {
      filled.set(33);
    }
        processBigInteger(batch, b, 33, rowId);
    return this;
  }
  public TraceBuilder type0(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(34)) {
      throw new IllegalStateException("TYPE0 already set");
    } else {
      filled.set(34);
    }
        processBoolean(batch, b, 34, rowId);
    return this;
  }
  public TraceBuilder type1(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(35)) {
      throw new IllegalStateException("TYPE1 already set");
    } else {
      filled.set(35);
    }
        processBoolean(batch, b, 35, rowId);
    return this;
  }
  public TraceBuilder type2(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(36)) {
      throw new IllegalStateException("TYPE2 already set");
    } else {
      filled.set(36);
    }
        processBoolean(batch, b, 36, rowId);
    return this;
  }
  public TraceBuilder value(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(37)) {
      throw new IllegalStateException("VALUE already set");
    } else {
      filled.set(37);
    }
        processBigInteger(batch, b, 37, rowId);
    return this;
  }
  public TraceBuilder wcpArgOneLo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(38)) {
      throw new IllegalStateException("WCP_ARG_ONE_LO already set");
    } else {
      filled.set(38);
    }
        processBigInteger(batch, b, 38, rowId);
    return this;
  }
  public TraceBuilder wcpArgTwoLo(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(39)) {
      throw new IllegalStateException("WCP_ARG_TWO_LO already set");
    } else {
      filled.set(39);
    }
        processBigInteger(batch, b, 39, rowId);
    return this;
  }
  public TraceBuilder wcpInst(VectorizedRowBatch batch, int rowId, BigInteger b) {

    if (filled.get(40)) {
      throw new IllegalStateException("WCP_INST already set");
    } else {
      filled.set(40);
    }
        processBigInteger(batch, b, 40, rowId);
    return this;
  }
  public TraceBuilder wcpResLo(VectorizedRowBatch batch, int rowId, Boolean b) {

    if (filled.get(41)) {
      throw new IllegalStateException("WCP_RES_LO already set");
    } else {
      filled.set(41);
    }
        processBoolean(batch, b, 41, rowId);
    return this;
  }

        public void validateRowAndFlush(VectorizedRowBatch batch, Writer writer)  throws IOException {
        if (!filled.get(0)) {
        throw new IllegalStateException("ABS_TX_NUM has not been filled");
        }

        if (!filled.get(1)) {
        throw new IllegalStateException("ABS_TX_NUM_MAX has not been filled");
        }

        if (!filled.get(2)) {
        throw new IllegalStateException("BASEFEE has not been filled");
        }

        if (!filled.get(3)) {
        throw new IllegalStateException("BTC_NUM has not been filled");
        }

        if (!filled.get(4)) {
        throw new IllegalStateException("BTC_NUM_MAX has not been filled");
        }

        if (!filled.get(5)) {
        throw new IllegalStateException("CALL_DATA_SIZE has not been filled");
        }

        if (!filled.get(6)) {
        throw new IllegalStateException("CODE_FRAGMENT_INDEX has not been filled");
        }

        if (!filled.get(7)) {
        throw new IllegalStateException("COINBASE_HI has not been filled");
        }

        if (!filled.get(8)) {
        throw new IllegalStateException("COINBASE_LO has not been filled");
        }

        if (!filled.get(9)) {
        throw new IllegalStateException("CT has not been filled");
        }

        if (!filled.get(10)) {
        throw new IllegalStateException("CUMULATIVE_CONSUMED_GAS has not been filled");
        }

        if (!filled.get(11)) {
        throw new IllegalStateException("FROM_HI has not been filled");
        }

        if (!filled.get(12)) {
        throw new IllegalStateException("FROM_LO has not been filled");
        }

        if (!filled.get(13)) {
        throw new IllegalStateException("GAS_LIMIT has not been filled");
        }

        if (!filled.get(14)) {
        throw new IllegalStateException("GAS_PRICE has not been filled");
        }

        if (!filled.get(15)) {
        throw new IllegalStateException("INITIAL_BALANCE has not been filled");
        }

        if (!filled.get(16)) {
        throw new IllegalStateException("INITIAL_GAS has not been filled");
        }

        if (!filled.get(17)) {
        throw new IllegalStateException("INIT_CODE_SIZE has not been filled");
        }

        if (!filled.get(18)) {
        throw new IllegalStateException("IS_DEP has not been filled");
        }

        if (!filled.get(19)) {
        throw new IllegalStateException("LEFTOVER_GAS has not been filled");
        }

        if (!filled.get(20)) {
        throw new IllegalStateException("NONCE has not been filled");
        }

        if (!filled.get(21)) {
        throw new IllegalStateException("OUTGOING_HI has not been filled");
        }

        if (!filled.get(22)) {
        throw new IllegalStateException("OUTGOING_LO has not been filled");
        }

        if (!filled.get(23)) {
        throw new IllegalStateException("OUTGOING_RLP_TXNRCPT has not been filled");
        }

        if (!filled.get(24)) {
        throw new IllegalStateException("PHASE_RLP_TXN has not been filled");
        }

        if (!filled.get(25)) {
        throw new IllegalStateException("PHASE_RLP_TXNRCPT has not been filled");
        }

        if (!filled.get(26)) {
        throw new IllegalStateException("REFUND_AMOUNT has not been filled");
        }

        if (!filled.get(27)) {
        throw new IllegalStateException("REFUND_COUNTER has not been filled");
        }

        if (!filled.get(28)) {
        throw new IllegalStateException("REL_TX_NUM has not been filled");
        }

        if (!filled.get(29)) {
        throw new IllegalStateException("REL_TX_NUM_MAX has not been filled");
        }

        if (!filled.get(30)) {
        throw new IllegalStateException("REQUIRES_EVM_EXECUTION has not been filled");
        }

        if (!filled.get(31)) {
        throw new IllegalStateException("STATUS_CODE has not been filled");
        }

        if (!filled.get(32)) {
        throw new IllegalStateException("TO_HI has not been filled");
        }

        if (!filled.get(33)) {
        throw new IllegalStateException("TO_LO has not been filled");
        }

        if (!filled.get(34)) {
        throw new IllegalStateException("TYPE0 has not been filled");
        }

        if (!filled.get(35)) {
        throw new IllegalStateException("TYPE1 has not been filled");
        }

        if (!filled.get(36)) {
        throw new IllegalStateException("TYPE2 has not been filled");
        }

        if (!filled.get(37)) {
        throw new IllegalStateException("VALUE has not been filled");
        }

        if (!filled.get(38)) {
        throw new IllegalStateException("WCP_ARG_ONE_LO has not been filled");
        }

        if (!filled.get(39)) {
        throw new IllegalStateException("WCP_ARG_TWO_LO has not been filled");
        }

        if (!filled.get(40)) {
        throw new IllegalStateException("WCP_INST has not been filled");
        }

        if (!filled.get(41)) {
        throw new IllegalStateException("WCP_RES_LO has not been filled");
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
