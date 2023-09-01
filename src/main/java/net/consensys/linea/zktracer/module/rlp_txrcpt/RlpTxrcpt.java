package net.consensys.linea.zktracer.module.rlp_txrcpt;

import java.math.BigInteger;
import java.util.List;
import java.util.function.Function;

import net.consensys.linea.zktracer.bytes.UnsignedByte;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.rlp_patterns.RlpBitDecOutput;
import net.consensys.linea.zktracer.module.rlp_patterns.RlpByteCountAndPowerOutput;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.plugin.data.BlockBody;
import org.hyperledger.besu.plugin.data.BlockHeader;
import org.hyperledger.besu.plugin.data.Log;
import org.hyperledger.besu.plugin.data.TransactionReceipt;

public class RlpTxrcpt implements Module {

  int LLARGE = RlpTxrcptTrace.LLARGE.intValue();
  int int_short = RlpTxrcptTrace.int_short.intValue();
  int int_long = RlpTxrcptTrace.int_long.intValue();
  int list_short = RlpTxrcptTrace.list_short.intValue();
  int list_long = RlpTxrcptTrace.list_long.intValue();

  int abs_tx_num = 0;
  int abs_log_num = 0;

  Trace.TraceBuilder trace = new Trace.TraceBuilder();

  @Override
  public String jsonKey() {
    return "rlpTxRcpt";
  }

  @Override
  public List<OpCode> supportedOpCodes() {
    return List.of();
  }

  @Override
  public void traceEndBlock(final BlockHeader blockHeader, final BlockBody blockBody) {
    for (Transaction tx : blockBody.getTransactions()) {
      // this.traceTransaction(tx);
    }
  }

  public void traceTransaction(TransactionReceipt txrcpt, TransactionType txType) {

    this.abs_tx_num += 1;
    RlpTxrcptColumnsValue traceValue = new RlpTxrcptColumnsValue();
    traceValue.TXRCPT_SIZE = TxRcptSize(txrcpt);

    /** PHASE 0: RLP Prefix */
    Phase0(traceValue, txType);

    /** PHASE 1: Status code Rz */
    Phase1(traceValue, txrcpt);

    /** PHASE 2: Cumulative gas Ru */
    Phase2(traceValue, txrcpt);

    /** PHASE 3: Bloom Filter Rb */
    Phase3(traceValue, txrcpt);

    /** Phase 4: log series Rl */
    Phase4(traceValue, txrcpt);
  }

  @Override
  public void traceEndConflation() {
    /** Rewrite the ABS_TX_NUM_MAX and ABS_LOG_NUM_MAX columns */
    for (int i = 0; i < this.trace.size(); i++) {
      this.trace.setAbsTxNumMaxAt(BigInteger.valueOf(this.abs_tx_num), i);
      this.trace.setAbsLogNumMaxAt(BigInteger.valueOf(this.abs_log_num), i);
    }
  }

  private void Phase0(RlpTxrcptColumnsValue traceValue, TransactionType txType) {

    /** byte TYPE concatenation */
    traceValue.PartialReset(0, 1);
    traceValue.IS_PREFIX = true;
    if (txType == TransactionType.FRONTIER) {
      traceValue.LC_CORRECTION = true;
    } else {
      traceValue.LIMB_CONSTRUCTED = true;
      traceValue.INPUT_1 = Bytes.of(txType.getSerializedType());
      traceValue.LIMB = BigInteger.valueOf(txType.getSerializedType());
      traceValue.nBYTES = 1;
    }
    TraceRow(traceValue);
    /** RLP prefix of the txRcpt list */
    RlpByteString(0, traceValue.TXRCPT_SIZE, true, false, false, false, false, true, traceValue);
  }

  private void Phase1(RlpTxrcptColumnsValue traceValue, TransactionReceipt txrcpt) {
    traceValue.PartialReset(1, 1);
    traceValue.LIMB_CONSTRUCTED = true;
    if (txrcpt.getStatus() == 0) {
      traceValue.INPUT_1 = Bytes.ofUnsignedShort(0);
      traceValue.LIMB = BigInteger.valueOf(int_short);
    } else {
      traceValue.INPUT_1 = Bytes.ofUnsignedShort(1);
      traceValue.LIMB = BigInteger.ONE;
    }
    traceValue.nBYTES = 1;
    traceValue.PHASE_END = true;
    TraceRow(traceValue);
  }

  private void Phase2(RlpTxrcptColumnsValue traceValue, TransactionReceipt txrcpt) {
    RlpInt(2, txrcpt.getCumulativeGasUsed(), false, false, false, false, true, false, traceValue);
  }

  private void Phase3(RlpTxrcptColumnsValue traceValue, TransactionReceipt txrcpt) {
    /** RLP prefix */
    traceValue.PartialReset(3, 1);
    traceValue.IS_PREFIX = true;
    traceValue.PHASE_SIZE = 256;
    traceValue.LIMB_CONSTRUCTED = true;
    traceValue.LIMB =
        BigInteger.valueOf(int_long + 2)
            .multiply(BigInteger.valueOf(256))
            .multiply(BigInteger.valueOf(256))
            .add(BigInteger.valueOf(256));
    traceValue.nBYTES = 3;
    TraceRow(traceValue);

    /** Concatenation of Byte slice of the bloom Filter */
    for (int i = 0; i < 4; i++) {
      traceValue.PartialReset(3, LLARGE);
      traceValue.INPUT_1 = txrcpt.getBloomFilter().slice(64 * i, LLARGE);
      traceValue.INPUT_2 = txrcpt.getBloomFilter().slice(64 * i + LLARGE, LLARGE);
      traceValue.INPUT_3 = txrcpt.getBloomFilter().slice(64 * i + 2 * LLARGE, LLARGE);
      traceValue.INPUT_4 = txrcpt.getBloomFilter().slice(64 * i + 3 * LLARGE, LLARGE);
      for (int ct = 0; ct < LLARGE; ct++) {
        traceValue.COUNTER = ct;
        traceValue.BYTE_1 = traceValue.INPUT_1.get(ct);
        traceValue.ACC_1 = traceValue.INPUT_1.slice(0, ct + 1);
        traceValue.BYTE_2 = traceValue.INPUT_2.get(ct);
        traceValue.ACC_2 = traceValue.INPUT_2.slice(0, ct + 1);
        traceValue.BYTE_3 = traceValue.INPUT_3.get(ct);
        traceValue.ACC_3 = traceValue.INPUT_3.slice(0, ct + 1);
        traceValue.BYTE_4 = traceValue.INPUT_4.get(ct);
        traceValue.ACC_4 = traceValue.INPUT_4.slice(0, ct + 1);
        switch (ct) {
          case 12:
            traceValue.LIMB_CONSTRUCTED = true;
            traceValue.LIMB = traceValue.INPUT_1.toUnsignedBigInteger();
            traceValue.nBYTES = LLARGE;
            break;

          case 13:
            traceValue.LIMB_CONSTRUCTED = true;
            traceValue.LIMB = traceValue.INPUT_2.toUnsignedBigInteger();
            traceValue.nBYTES = LLARGE;
            break;

          case 14:
            traceValue.LIMB_CONSTRUCTED = true;
            traceValue.LIMB = traceValue.INPUT_3.toUnsignedBigInteger();
            traceValue.nBYTES = LLARGE;
            break;

          case 15:
            traceValue.LIMB_CONSTRUCTED = true;
            traceValue.LIMB = traceValue.INPUT_4.toUnsignedBigInteger();
            traceValue.nBYTES = LLARGE;
            traceValue.PHASE_END = (i == 3);
            break;

          default:
            traceValue.LIMB_CONSTRUCTED = false;
            traceValue.LIMB = BigInteger.ZERO;
            traceValue.nBYTES = 0;
        }
        TraceRow(traceValue);
        /** Update INDEX_LOCAL after the row when LIMB is constructed */
        if (traceValue.LIMB_CONSTRUCTED) {
          traceValue.INDEX_LOCAL += 1;
        }
      }
    }
    // Put to 0 INDEX_LOCAL at the end of the phase
    traceValue.INDEX_LOCAL = 0;
  }

  private void Phase4(RlpTxrcptColumnsValue traceValue, TransactionReceipt txrcpt) {
    /** Trivial case, there are no log entries */
    if (txrcpt.getLogs().isEmpty()) {
      traceValue.PartialReset(4, 1);
      traceValue.IS_PREFIX = true;
      traceValue.LIMB_CONSTRUCTED = true;
      traceValue.LIMB = BigInteger.valueOf(list_short);
      traceValue.nBYTES = 1;
      traceValue.PHASE_END = true;
      TraceRow(traceValue);
    } /*** General Case */ else {
      /** RLP prefix of the list of log entries */
      int nbLog = txrcpt.getLogs().size();
      for (int i = 0; i < nbLog; i++) {
        traceValue.PHASE_SIZE += OuterRlpSize(LogSize(txrcpt.getLogs().get(i)));
      }
      traceValue.PartialReset(4, 8);
      RlpByteString(4, traceValue.PHASE_SIZE, true, true, false, false, false, false, traceValue);

      /** Trace each Log Entry */
      for (int i = 0; i < nbLog; i++) {
        /** Update ABS_LOG_NUM */
        this.abs_log_num += 1;

        /** Log Entry RLP Prefix */
        traceValue.PartialReset(4, 8);
        traceValue.LOG_ENTRY_SIZE = LogSize(txrcpt.getLogs().get(i));
        RlpByteString(
            4, traceValue.LOG_ENTRY_SIZE, true, true, true, false, false, false, traceValue);

        /** Logger's Address */
        traceValue.PartialReset(4, 3);
        traceValue.DEPTH_1 = true;
        traceValue.INPUT_1 = txrcpt.getLogs().get(i).getLogger().slice(0, 4);
        traceValue.INPUT_2 = txrcpt.getLogs().get(i).getLogger().slice(4, LLARGE);
        traceValue.LIMB_CONSTRUCTED = true;

        traceValue.COUNTER = 0;
        traceValue.LIMB = BigInteger.valueOf(int_short + 20);
        traceValue.nBYTES = 1;
        TraceRow(traceValue);

        traceValue.COUNTER = 1;
        traceValue.LIMB = txrcpt.getLogs().get(i).getLogger().slice(0, 4).toUnsignedBigInteger();
        traceValue.nBYTES = 4;
        TraceRow(traceValue);

        traceValue.COUNTER = 2;
        traceValue.LIMB =
            txrcpt.getLogs().get(i).getLogger().slice(4, LLARGE).toUnsignedBigInteger();
        traceValue.nBYTES = LLARGE;
        TraceRow(traceValue);

        /** Log Topic's RLP prefix */
        traceValue.PartialReset(4, 1);
        traceValue.DEPTH_1 = true;
        traceValue.IS_PREFIX = true;
        traceValue.IS_TOPIC = true;
        traceValue.LOCAL_SIZE = 33 * txrcpt.getLogs().get(i).getTopics().size();
        traceValue.LIMB_CONSTRUCTED = true;

        if (txrcpt.getLogs().get(i).getTopics().isEmpty()
            || txrcpt.getLogs().get(i).getTopics().size() == 1) {
          traceValue.LIMB = BigInteger.valueOf(list_short + traceValue.LOCAL_SIZE);
          traceValue.nBYTES = 1;
        } else {
          traceValue.LIMB = BigInteger.valueOf(256L * (list_long + 1) + traceValue.LOCAL_SIZE);
          traceValue.nBYTES = 2;
        }
        TraceRow(traceValue);

        /** RLP Log Topic (if exist) */
        if (!txrcpt.getLogs().get(i).getTopics().isEmpty()) {
          for (int j = 0; j < txrcpt.getLogs().get(i).getTopics().size(); j++) {
            traceValue.PartialReset(4, 3);
            traceValue.DEPTH_1 = true;
            traceValue.IS_TOPIC = true;
            traceValue.INDEX_LOCAL += 1;
            traceValue.INPUT_1 = txrcpt.getLogs().get(i).getTopics().get(j).slice(0, LLARGE);
            traceValue.INPUT_2 = txrcpt.getLogs().get(i).getTopics().get(j).slice(LLARGE, LLARGE);
            traceValue.LIMB_CONSTRUCTED = true;

            traceValue.COUNTER = 0;
            traceValue.LIMB = BigInteger.valueOf(int_short + 32);
            traceValue.nBYTES = 1;
            traceValue.LOCAL_SIZE -= traceValue.nBYTES;
            TraceRow(traceValue);

            traceValue.COUNTER = 1;
            traceValue.LIMB = traceValue.INPUT_1.toUnsignedBigInteger();
            traceValue.nBYTES = LLARGE;
            traceValue.LOCAL_SIZE -= traceValue.nBYTES;
            TraceRow(traceValue);

            traceValue.COUNTER = 2;
            traceValue.LIMB = traceValue.INPUT_2.toUnsignedBigInteger();
            traceValue.nBYTES = LLARGE;
            traceValue.LOCAL_SIZE -= traceValue.nBYTES;
            TraceRow(traceValue);
          }
        }

        /** RLP Prefix of the Data */
        /**
         * In INPUT_2 is stored the number of topics, stored in INDEX_LOCAL at the previous row
         * (needed for LookUp)
         */
        traceValue.INPUT_2 = Bytes.ofUnsignedShort(traceValue.INDEX_LOCAL);
        traceValue.INDEX_LOCAL = 0;

        switch (txrcpt.getLogs().get(i).getData().size()) {
          case 0:
            traceValue.PartialReset(4, 1);
            traceValue.DEPTH_1 = true;
            traceValue.IS_PREFIX = true;
            traceValue.IS_DATA = true;
            traceValue.INPUT_1 = Bytes.ofUnsignedInt(txrcpt.getLogs().get(i).getData().size());
            traceValue.LOCAL_SIZE = txrcpt.getLogs().get(i).getData().size();
            traceValue.LIMB_CONSTRUCTED = true;
            traceValue.LIMB = BigInteger.valueOf(int_short);
            traceValue.nBYTES = 1;
            traceValue.PHASE_END = (i == nbLog - 1);
            TraceRow(traceValue);

          case 1:
            traceValue.PartialReset(4, 8);
            RlpInt(
                4,
                txrcpt.getLogs().get(i).getData().get(0),
                true,
                true,
                false,
                true,
                false,
                true,
                traceValue);
            for (int k = 0; k < 8; k++) {
              this.trace.setInput1Relative(BigInteger.ONE, k);
              this.trace.setInput2Relative(
                  BigInteger.valueOf(txrcpt.getLogs().get(i).getTopics().size()), k);
              this.trace.setInput3Relative(
                  BigInteger.valueOf(txrcpt.getLogs().get(i).getData().get(0)), k);
            }

          default:
            traceValue.PartialReset(4, 8);
            traceValue.LOCAL_SIZE = txrcpt.getLogs().get(i).getData().size();
            RlpByteString(
                4,
                txrcpt.getLogs().get(i).getData().size(),
                false,
                true,
                true,
                false,
                true,
                false,
                traceValue);
            for (int k = 0; k < 8; k++) {
              this.trace.setInput2Relative(
                  BigInteger.valueOf(txrcpt.getLogs().get(i).getTopics().size()), k);
            }
        }

        /** Tracing the Data */
        if (!txrcpt.getLogs().get(i).getData().isEmpty()) {
          int nbDataSlice = 1 + (txrcpt.getLogs().get(i).getData().size() - 1) / 16;
          int sizeDataLastSlice =
              txrcpt.getLogs().get(i).getData().size() - LLARGE * (nbDataSlice - 1);
          if (sizeDataLastSlice == 0) {
            sizeDataLastSlice = LLARGE;
          }
          traceValue.PartialReset(4, nbDataSlice);
          traceValue.DEPTH_1 = true;
          traceValue.IS_DATA = true;
          traceValue.LIMB_CONSTRUCTED = true;

          for (int ct = 0; ct < nbDataSlice; ct++) {
            traceValue.COUNTER = ct;
            traceValue.INDEX_LOCAL = ct;
            if (!(ct == nbDataSlice - 1)) {
              traceValue.INPUT_1 = txrcpt.getLogs().get(i).getData().slice(LLARGE * ct, LLARGE);
              traceValue.LIMB = traceValue.INPUT_1.toUnsignedBigInteger();
              traceValue.nBYTES = LLARGE;
              traceValue.LOCAL_SIZE -= LLARGE;
              TraceRow(traceValue);
            } else {
              traceValue.INPUT_1 =
                  txrcpt.getLogs().get(i).getData().slice(LLARGE * ct, sizeDataLastSlice);
              traceValue.LIMB = traceValue.INPUT_1.toUnsignedBigInteger();
              traceValue.nBYTES = sizeDataLastSlice;
              traceValue.LOCAL_SIZE -= sizeDataLastSlice;
              traceValue.PHASE_END = (i == nbLog - 1);
              TraceRow(traceValue);
              this.trace.setInput1Relative(
                  traceValue
                      .INPUT_1
                      .toUnsignedBigInteger()
                      .shiftLeft(8 * (LLARGE - traceValue.nBYTES)),
                  0);
            }
          }
        }
      }
    }
  }

  private void RlpByteString(
      int phase,
      long length,
      boolean isList,
      boolean isPrefix,
      boolean depth1,
      boolean isTopic,
      boolean isData,
      boolean endPhase,
      RlpTxrcptColumnsValue traceValue) {
    int lengthSize =
        Bytes.ofUnsignedLong(length).size()
            - Bytes.ofUnsignedLong(length).numberOfLeadingZeroBytes();
    RlpByteCountAndPowerOutput byteCountingOutput = ByteCounting(lengthSize, 8);
    traceValue.PartialReset(phase, 8);
    traceValue.INPUT_1 = Bytes.ofUnsignedInt(length);
    traceValue.IS_PREFIX = isPrefix;
    traceValue.DEPTH_1 = depth1;
    traceValue.IS_TOPIC = isTopic;
    traceValue.IS_DATA = isData;
    Bytes INPUT1_rightshift = addLeftZeroToGivenSize(traceValue.INPUT_1, 8);
    long ACC2LastRow = 0;
    if (length >= 56) {
      ACC2LastRow = length - 56;
    } else {
      ACC2LastRow = 55 - length;
    }
    Bytes Acc2LastRowShift = addLeftZeroToGivenSize(Bytes.ofUnsignedInt(ACC2LastRow), 8);
    for (int ct = 0; ct < 8; ct++) {
      traceValue.COUNTER = ct;
      traceValue.ACC_SIZE = byteCountingOutput.AccByteSizeList.get(ct);
      traceValue.POWER = byteCountingOutput.PowerList.get(ct);
      traceValue.BYTE_1 = INPUT1_rightshift.get(ct);
      traceValue.ACC_1 = INPUT1_rightshift.slice(0, ct + 1);
      traceValue.BYTE_2 = Acc2LastRowShift.get(ct);
      traceValue.ACC_2 = Acc2LastRowShift.slice(0, ct + 1);

      if (length >= 56) {
        if (ct == 6) {
          traceValue.LIMB_CONSTRUCTED = true;
          traceValue.nBYTES = 1;
          if (isList) {
            traceValue.LIMB = BigInteger.valueOf(list_long + lengthSize);
          } else {
            traceValue.LIMB = BigInteger.valueOf(int_long + lengthSize);
          }
        }
        if (ct == 7) {
          traceValue.LIMB_CONSTRUCTED = true;
          traceValue.LIMB = BigInteger.valueOf(length);
          traceValue.nBYTES = lengthSize;
          traceValue.BIT = true;
          traceValue.BIT_ACC = 1;
          traceValue.PHASE_END = endPhase;
        }
      } else {
        if (ct == 7) {
          traceValue.LIMB_CONSTRUCTED = true;
          if (isList) {
            traceValue.LIMB = BigInteger.valueOf(list_short + length);
          } else {
            traceValue.LIMB = BigInteger.valueOf(int_short + length);
          }
          traceValue.nBYTES = 1;
          traceValue.PHASE_END = endPhase;
        }
      }
      TraceRow(traceValue);
    }
  }

  private void RlpInt(
      int phase,
      long input,
      boolean isPrefix,
      boolean depth1,
      boolean isTopic,
      boolean isData,
      boolean endPhase,
      boolean onlyPrefix,
      RlpTxrcptColumnsValue traceValue) {
    int inputSize =
        Bytes.ofUnsignedInt(input).size() - Bytes.ofUnsignedInt(input).numberOfLeadingZeroBytes();
    traceValue.PartialReset(phase, 8);
    Bytes inputBytes = addLeftZeroToGivenSize(Bytes.ofUnsignedInt(input), 8);
    traceValue.IS_PREFIX = isPrefix;
    traceValue.DEPTH_1 = depth1;
    traceValue.IS_TOPIC = isTopic;
    traceValue.IS_DATA = isData;
    RlpByteCountAndPowerOutput byteCountingOutput = ByteCounting(inputSize, 8);
    RlpBitDecOutput bitDecOutput =
        BitDecomposition(0xff & inputBytes.get(inputBytes.size() - 1), 8);
    traceValue.INPUT_1 = Bytes.ofUnsignedInt(input);

    for (int ct = 0; ct < 8; ct++) {
      traceValue.COUNTER = ct;
      traceValue.BYTE_1 = inputBytes.get(ct);
      traceValue.ACC_1 = inputBytes.slice(0, ct + 1);
      traceValue.POWER = byteCountingOutput.PowerList.get(ct);
      traceValue.ACC_SIZE = byteCountingOutput.AccByteSizeList.get(ct);
      traceValue.BIT = bitDecOutput.BitDecList.get(ct);
      traceValue.BIT_ACC = bitDecOutput.BitAccList.get(ct);

      if (input >= 128 && ct == 6) {
        traceValue.LIMB_CONSTRUCTED = true;
        traceValue.LIMB = BigInteger.valueOf(int_short + inputSize);
        traceValue.nBYTES = 1;
      }

      if (ct == 7) {
        if (onlyPrefix) {
          traceValue.LC_CORRECTION = true;
          traceValue.LIMB_CONSTRUCTED = false;
          traceValue.LIMB = BigInteger.ZERO;
          traceValue.nBYTES = 0;
        } else {
          traceValue.LIMB_CONSTRUCTED = true;
          traceValue.LIMB = BigInteger.valueOf(input);
          traceValue.nBYTES = inputSize;
          traceValue.PHASE_END = endPhase;
        }
      }
      TraceRow(traceValue);
    }
  }

  private void TraceRow(RlpTxrcptColumnsValue traceValue) {

    /** Decrements sizes */
    if (traceValue.LIMB_CONSTRUCTED) {
      if (traceValue.phase != 0) {
        traceValue.TXRCPT_SIZE -= traceValue.nBYTES;
      }
      if ((traceValue.phase == 3 && !traceValue.IS_PREFIX)
          || (traceValue.phase == 4 && traceValue.DEPTH_1)) {
        traceValue.PHASE_SIZE -= traceValue.nBYTES;
      }
      if ((traceValue.phase == 4 && traceValue.DEPTH_1 && (!traceValue.IS_PREFIX))
          || traceValue.IS_TOPIC
          || traceValue.IS_DATA) {
        traceValue.LOG_ENTRY_SIZE -= traceValue.nBYTES;
      }
    }
    this.trace
        .absLogNum(BigInteger.valueOf(this.abs_log_num))
        .absLogNumMax(BigInteger.ONE)
        .absTxNum(BigInteger.valueOf(this.abs_tx_num))
        .absTxNumMax(BigInteger.ONE)
        .acc1(traceValue.ACC_1.toUnsignedBigInteger())
        .acc2(traceValue.ACC_2.toUnsignedBigInteger())
        .acc3(traceValue.ACC_3.toUnsignedBigInteger())
        .acc4(traceValue.ACC_4.toUnsignedBigInteger())
        .accSize(BigInteger.valueOf(traceValue.ACC_SIZE))
        .bit(traceValue.BIT)
        .bitAcc(UnsignedByte.of(traceValue.BIT_ACC))
        .byte1(UnsignedByte.of(traceValue.BYTE_1))
        .byte2(UnsignedByte.of(traceValue.BYTE_2))
        .byte3(UnsignedByte.of(traceValue.BYTE_3))
        .byte4(UnsignedByte.of(traceValue.BYTE_4))
        .counter(UnsignedByte.of(traceValue.COUNTER))
        .depth1(traceValue.DEPTH_1)
        .done(traceValue.COUNTER == traceValue.nSTEP - 1)
        .index(BigInteger.valueOf(traceValue.INDEX))
        .indexLocal(BigInteger.valueOf(traceValue.INDEX_LOCAL))
        .input1(traceValue.INPUT_1.toUnsignedBigInteger())
        .input2(traceValue.INPUT_2.toUnsignedBigInteger())
        .input3(traceValue.INPUT_3.toUnsignedBigInteger())
        .input4(traceValue.INPUT_4.toUnsignedBigInteger())
        .isData(traceValue.IS_DATA)
        .isPrefix(traceValue.IS_PREFIX)
        .isTopic(traceValue.IS_TOPIC)
        .lcCorrection(traceValue.LC_CORRECTION)
        .limb(traceValue.LIMB.shiftLeft(8 * (LLARGE - traceValue.nBYTES)))
        .limbConstructed(traceValue.LIMB_CONSTRUCTED)
        .localSize(BigInteger.valueOf(traceValue.LOCAL_SIZE))
        .logEntrySize(BigInteger.valueOf(traceValue.LOG_ENTRY_SIZE))
        .nBytes(UnsignedByte.of(traceValue.nBYTES))
        .nStep(UnsignedByte.of(traceValue.nSTEP));
    List<Function<Boolean, Trace.TraceBuilder>> phaseColumns =
        List.of(
            this.trace::phase0,
            this.trace::phase1,
            this.trace::phase2,
            this.trace::phase3,
            this.trace::phase4);
    for (int i = 0; i < phaseColumns.size(); i++) {
      phaseColumns.get(i).apply(i == traceValue.phase);
    }
    this.trace
        .phaseEnd(traceValue.PHASE_END)
        .phaseSize(BigInteger.valueOf(traceValue.PHASE_SIZE))
        .power(traceValue.POWER)
        .txrcptSize(BigInteger.valueOf(traceValue.TXRCPT_SIZE));
    this.trace.validateRow();

    /** Increments Index */
    if (traceValue.LIMB_CONSTRUCTED) {
      traceValue.INDEX += 1;
    }
  }

  /**
   * Returns the size of RLP(something) where something is of size inputSize (!=1) (it can be ZERO
   * though).
   */
  public int OuterRlpSize(int inputSize) {
    assert (inputSize != 1);
    int rlpSize = inputSize + 1;

    if (inputSize >= 56) {
      rlpSize +=
          Bytes.ofUnsignedShort(inputSize).size()
              - Bytes.ofUnsignedShort(inputSize).numberOfLeadingZeroBytes();
    }

    return rlpSize;
  }

  /** This function returns the size of the RLP of a transaction receipt WITHOUT its RLP prefix */
  private int TxRcptSize(TransactionReceipt txrcpt) {

    /* The encoded status code is always of size 1 */
    int size = 1;

    /** As the cumulative gas is Gtransaction=21000, its size is >1 */
    size +=
        OuterRlpSize(
            Bytes.ofUnsignedInt(txrcpt.getCumulativeGasUsed()).size()
                - Bytes.ofUnsignedInt(txrcpt.getCumulativeGasUsed()).numberOfLeadingZeroBytes());

    /** RLP(Rb) is always 259 (256+3) long */
    size += 259;

    /** add the size of the RLP(Log) */
    int nbLog = txrcpt.getLogs().size();
    if (nbLog == 0) {
      size += 1;
    } else {
      int tmp = 0;
      for (int i = 0; i < nbLog; i++) {
        tmp += OuterRlpSize(LogSize(txrcpt.getLogs().get(i)));
      }
      size += OuterRlpSize(tmp);
    }

    return size;
  }

  /** Gives the byte size of the RLPisation of a log entry WITHOUT its RLP prefix */
  private int LogSize(Log log) {
    /** The size of RLP(Oa) is always 21. */
    int logSize = 21;

    /** RLP(Topic) is of size 1 for 0 topic, 33+1 for 1 topic, 2 + 33*nTOPIC for 2 <= nTOPIC <=4. */
    logSize += OuterRlpSize(33 * log.getTopics().size());

    /** RLP(Od) is of size OuterRlpSize(datasize) except if the Data is made of one byte */
    if (log.getData().size() == 1) {
      /** If the byte is of value >= 128, its RLP is 2 byte, else 1 byte (no RLP prefix) */
      if (log.getData().bitLength() == 8) {
        logSize += 2;
      } else {
        logSize += 1;
      }
    } else {
      logSize += OuterRlpSize(log.getData().size());
    }
    return logSize;
  }

  /**
   * Add 0's to the left of the Bytes to create a Bytes of the given size. The wantedSize must be at
   * least the size of the Bytes
   */
  public Bytes addLeftZeroToGivenSize(Bytes input, int wantedSize) {
    assert wantedSize >= input.size() : " wantedSize can't be shorter than the input size";
    byte nullbyte = 0;
    Bytes output = Bytes.concatenate(Bytes.repeat(nullbyte, wantedSize - input.size()), input);
    return output;
  }

  /** Create the Power and AccSize list of the ByteCountAndPower RLP pattern */
  public RlpByteCountAndPowerOutput ByteCounting(int inputByteLen, int nb_step) {
    /**
     * inputByteLen represents the number of meaningfull byte of inputByte, ie without the zero left
     * padding
     */
    RlpByteCountAndPowerOutput output = new RlpByteCountAndPowerOutput();

    BigInteger power;
    int accByteSize = 0;
    int offset = 16 - nb_step;

    if (inputByteLen == nb_step) {
      power = BigInteger.valueOf(256).pow(offset);
      accByteSize = 1;
    } else {
      offset += 1;
      power = BigInteger.valueOf(256).pow(offset);
    }
    output.PowerList.add(0, power);
    output.AccByteSizeList.add(0, accByteSize);

    for (int i = 1; i < nb_step; i++) {
      if (inputByteLen + i < nb_step) {
        power = power.multiply(BigInteger.valueOf(256));
      } else {
        accByteSize += 1;
      }
      output.PowerList.add(i, power);
      output.AccByteSizeList.add(i, accByteSize);
    }
    return output;
  }

  /** Create the Bit and BitDec list of the RLP pattern of an int */
  public RlpBitDecOutput BitDecomposition(int input, int nb_step) {
    assert (nb_step >= 8);

    RlpBitDecOutput output = new RlpBitDecOutput();

    int bitAcc = 0;
    boolean bitDec = false;
    double div = 0;

    for (int i = 7; i >= 0; i--) {
      div = Math.pow(2, i);
      bitAcc *= 2;

      if (input >= div) {
        bitDec = true;
        bitAcc += 1;
        input -= div;
      } else {
        bitDec = false;
      }

      output.BitDecList.add(nb_step - i - 1, bitDec);
      output.BitAccList.add(nb_step - i - 1, bitAcc);
    }
    return output;
  }

  @Override
  public Object commit() {
    return new RlpTxrcptTrace(trace.build());
  }
}
