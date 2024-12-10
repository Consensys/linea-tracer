/*
 * Copyright ConsenSys Inc.
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

package net.consensys.linea.zktracer.module.blockdata;

import static com.google.common.base.Preconditions.checkArgument;
import static net.consensys.linea.zktracer.module.blockdata.Trace.CT_MAX_BF;
import static net.consensys.linea.zktracer.module.blockdata.Trace.CT_MAX_CB;
import static net.consensys.linea.zktracer.module.blockdata.Trace.CT_MAX_DEPTH;
import static net.consensys.linea.zktracer.module.blockdata.Trace.CT_MAX_DF;
import static net.consensys.linea.zktracer.module.blockdata.Trace.CT_MAX_GL;
import static net.consensys.linea.zktracer.module.blockdata.Trace.CT_MAX_ID;
import static net.consensys.linea.zktracer.module.blockdata.Trace.CT_MAX_NB;
import static net.consensys.linea.zktracer.module.blockdata.Trace.CT_MAX_TS;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_BASEFEE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_CHAINID;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_COINBASE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_DIFFICULTY;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_DIV;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_GASLIMIT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_GT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_ISZERO;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_LT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_NUMBER;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_TIMESTAMP;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.LINEA_BASE_FEE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.LINEA_BLOCK_GAS_LIMIT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.LINEA_DIFFICULTY;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.LLARGE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WCP_INST_GEQ;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WCP_INST_LEQ;
import static net.consensys.linea.zktracer.module.constants.Trace.GAS_LIMIT_ADJUSTMENT_FACTOR;
import static net.consensys.linea.zktracer.module.constants.Trace.LINEA_GAS_LIMIT_MAXIMUM;
import static net.consensys.linea.zktracer.module.constants.Trace.LINEA_GAS_LIMIT_MINIMUM;
import static net.consensys.linea.zktracer.types.Conversions.booleanToBytes;

import java.math.BigInteger;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.txndata.TxnData;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;

@Accessors(fluent = true)
@Getter
public class BlockdataOperation extends ModuleOperation {
  private final long timestamp;
  private final long absoluteBlockNumber;
  private final BigInteger difficulty;
  private final Wcp wcp;
  private final Euc euc;
  private final TxnData txnData;
  private final BigInteger chainId;
  private final BlockdataOperation prevOperation;
  private final Bytes ZERO = Bytes.ofUnsignedLong(0);

  // TODO: miss IOMF, CT, BLOCK_GAS_LIMIT, BASE_FEE, REL_BLOCK
  private boolean previousConflation; // TODO: how to set this variables
  private boolean currentConflation; // TODO: how to set this variables
  private final int ctMax;
  private boolean isCoinbase;
  private boolean isTimestamp;
  private boolean isNumber;
  private boolean isDifficulty;
  private boolean isGasLimit;
  private boolean isChainId;
  private boolean isBaseFee;
  private final int inst;
  private final Address coinbase;
  private final long blockGasLimit = 0; // TODO: how to set this variable
  private final long baseFee = 0; // TODO: how to set this variable
  private final long firstBlockNumber = 0; // TODO: how to set this variable
  private final int relTxMax;

  private Bytes dataHi;
  private Bytes dataLo;
  private Bytes[] arg1Hi;
  private Bytes[] arg1Lo;
  private Bytes[] arg2Hi;
  private Bytes[] arg2Lo;
  private Bytes[] res;
  private final UnsignedByte[] exoInst;
  private final boolean[] wcpFlag;
  private final boolean[] eucFlag;

  public BlockdataOperation(
      Address coinbase,
      long timestamp,
      long absoluteBlockNumber,
      BigInteger difficulty,
      int relTxMax,
      Wcp wcp,
      Euc euc,
      TxnData txnData,
      BigInteger chainId,
      int inst,
      BlockdataOperation prevOperation) {
    this.coinbase = coinbase;
    this.timestamp = timestamp;
    this.absoluteBlockNumber = absoluteBlockNumber;
    this.difficulty = difficulty;
    this.relTxMax = relTxMax;
    this.wcp = wcp;
    this.euc = euc;
    this.txnData = txnData;
    this.chainId = chainId;
    this.prevOperation = prevOperation;

    this.inst = inst;
    this.ctMax = ctMax(inst);

    // Init non-counter constant columns arrays of size ctMax
    this.wcpFlag = new boolean[ctMax];
    this.eucFlag = new boolean[ctMax];
    this.exoInst = new UnsignedByte[ctMax];
    this.arg1Hi = new Bytes[ctMax];
    this.arg1Lo = new Bytes[ctMax];
    this.arg2Hi = new Bytes[ctMax];
    this.arg2Lo = new Bytes[ctMax];
    this.res = new Bytes[ctMax];

    // Handle opcodes
    switch (inst) {
      case EVM_INST_COINBASE -> {
        isCoinbase = true;
        handleCoinbase();
      }
      case EVM_INST_TIMESTAMP -> {
        isTimestamp = true;
        handleTimestamp();
      }
      case EVM_INST_NUMBER -> {
        isNumber = true;
        handleNumber();
      }
      case EVM_INST_DIFFICULTY -> {
        isDifficulty = true;
        handleDifficulty();
      }
      case EVM_INST_GASLIMIT -> {
        isGasLimit = true;
        handleGasLimit();
      }
      case EVM_INST_CHAINID -> {
        isChainId = true;
        handleChainId();
      }
      case EVM_INST_BASEFEE -> {
        isBaseFee = true;
        handleBaseFee();
      }
      default -> {}
    }

    /*
    Sketch of what we need to do:
    - Understand which opcode we are working with;
    - Determine the corresponding CT_MAX_XXX;
    - Initialize arrays for each column that is not counter constant of size CT_MAX_XXX, so as we can later fill them;
    - Columns that are not counter constant do not need an array, but a single value is enough;
    - One tricky aspect of this module is that the current operation needs a reference to the previous operation, so we need to
      figure out how to handle this;
    - For each opcode, we need to create a method to execute the corresponding computation. For example, executing the lookups and
      filling the arrays and values to trace them later;
     */
  }

  private void handleCoinbase() {
    dataHi = EWord.ofHexString(coinbase().toHexString()).hi();
    dataLo = EWord.ofHexString(coinbase().toHexString()).lo();

    // row i
    wcpCallToLT(0, dataHi, dataLo, Bytes.ofUnsignedLong((long) Math.pow(256, 4)), ZERO);
  }

  private void handleTimestamp() {
    // row i
    wcpCallToLT(0, dataHi, dataLo, ZERO, Bytes.ofUnsignedLong((long) Math.pow(256, 6)));

    // row i + 1
    wcpCallToGT(
        1,
        dataHi,
        dataLo,
        prevOperation == null ? ZERO : prevOperation.dataHi,
        prevOperation == null ? ZERO : prevOperation.dataLo);
  }

  private boolean isPrev(BlockdataOperation operation) {
    return operation.previousConflation;
  }

  private boolean isCurr(BlockdataOperation operation) {
    return operation.currentConflation;
  }

  private void handleNumber() {
    // row i
    final boolean firstBlockIsGenesisBlock =
        wcpCallToISZERO(0, ZERO, Bytes.ofUnsignedLong(firstBlockNumber));

    // Set dataHi and dataLo
    if (isPrev(this)) {
      if (firstBlockIsGenesisBlock) {
        dataHi = ZERO;
        dataLo = ZERO;
      }
      if (!firstBlockIsGenesisBlock) {
        dataHi = ZERO;
        dataLo = Bytes.ofUnsignedLong(firstBlockNumber - 1);
      }
    }
    if (isCurr(this)) {
      if (firstBlockIsGenesisBlock) {
        if (isPrev(prevOperation)) {
          dataHi = ZERO;
          dataLo = ZERO;
        }
        if (isCurr(prevOperation)) {
          dataHi = prevOperation.dataHi;
          dataLo = Bytes.ofUnsignedLong(prevOperation.dataLo.toLong() + 1);
        }
      }
      if (!firstBlockIsGenesisBlock) {
        dataHi = prevOperation.dataHi;
        dataLo = Bytes.ofUnsignedLong(prevOperation.dataLo.toLong() + 1);
      }
    }

    // row i + 1
    if (isPrev(this)) {
      wcpCallToLT(1, dataHi, dataLo, ZERO, Bytes.ofUnsignedLong((long) Math.pow(256, 6)));
    }
  }

  private void handleDifficulty() {
    dataHi = EWord.of(LINEA_DIFFICULTY).hi();
    dataLo = EWord.of(LINEA_DIFFICULTY).lo();

    // row i
    wcpCallToGEQ(0, dataHi, dataLo, ZERO, ZERO);
  }

  private void handleGasLimit() {
    dataHi = ZERO;
    dataLo = EWord.of(blockGasLimit).lo();

    // row i
    wcpCallToGEQ(0, dataHi, dataLo, ZERO, Bytes.ofUnsignedLong(LINEA_GAS_LIMIT_MINIMUM));

    // row i + 1
    wcpCallToLEQ(1, dataHi, dataLo, ZERO, Bytes.ofUnsignedLong(LINEA_GAS_LIMIT_MAXIMUM));

    if (isCurr(this)) {
      // row i + 2
      Bytes prevGasLimit = Bytes.ofUnsignedLong(prevOperation.blockGasLimit);
      Bytes maxDeviation =
          eucCall(2, prevGasLimit, Bytes.ofUnsignedLong(GAS_LIMIT_ADJUSTMENT_FACTOR));

      // row i + 3
      wcpCallToLT(
          3,
          dataHi,
          dataLo,
          ZERO,
          Bytes.ofUnsignedLong(prevGasLimit.toLong() + maxDeviation.toLong()));

      // row i + 4
      wcpCallToLT(
          4,
          dataHi,
          dataLo,
          ZERO,
          Bytes.ofUnsignedLong(
              prevGasLimit.toLong() - maxDeviation.toLong())); // TODO: double check this
    }
  }

  private void handleChainId() {
    dataHi = prevOperation.dataHi;
    dataLo = prevOperation.dataLo;

    // row i
    wcpCallToGEQ(0, dataHi, dataLo, ZERO, ZERO);
  }

  private void handleBaseFee() {
    dataHi = ZERO;
    dataLo = EWord.of(baseFee).lo();

    // row i
    wcpCallToGEQ(0, dataHi, dataLo, ZERO, ZERO);
  }

  @Override
  protected int computeLineCount() {
    return ctMax() + 1;
  }

  private int ctMax(int inst) {
    switch (inst) {
      case EVM_INST_COINBASE -> {
        return CT_MAX_CB;
      }
      case EVM_INST_TIMESTAMP -> {
        return CT_MAX_TS;
      }
      case EVM_INST_NUMBER -> {
        return CT_MAX_NB;
      }
      case EVM_INST_DIFFICULTY -> {
        return CT_MAX_DF;
      }
      case EVM_INST_GASLIMIT -> {
        return CT_MAX_GL;
      }
      case EVM_INST_CHAINID -> {
        return CT_MAX_ID;
      }
      case EVM_INST_BASEFEE -> {
        return CT_MAX_BF;
      }
      default -> {
        return CT_MAX_DEPTH;
      }
    }
  }

  public void trace(
      Trace trace, final int relBlock, final long firstBlockNumber, final BigInteger chainId) {
    for (short ct = 0; ct <= ctMax(); ct++) {
      trace
          .firstBlockNumber(firstBlockNumber)
          .relBlock((short) relBlock)
          .relTxNumMax((short) this.relTxMax)
          .coinbaseHi(this.coinbase.slice(0, 4).toLong())
          .coinbaseLo(this.coinbase.slice(4, LLARGE))
          .blockGasLimit(LINEA_BLOCK_GAS_LIMIT)
          .basefee(LINEA_BASE_FEE);

      // TODO: add missing columns
      trace
          .isCoinbase(isCoinbase)
          .isTimestamp(isTimestamp)
          .isNumber(isNumber)
          .isDifficulty(isDifficulty)
          .isGaslimit(isGasLimit)
          .isChainid(isChainId)
          .isBasefee(isBaseFee)
          .arg1Hi(arg1Hi[ct])
          .arg1Lo(arg1Lo[ct])
          .arg2Hi(arg2Hi[ct])
          .arg2Lo(arg2Lo[ct])
          .res(res[ct])
          .exoInst(exoInst[ct])
          .wcpFlag(wcpFlag[ct])
          .eucFlag(eucFlag[ct]);

      trace.validateRow();
    }
  }

  // Module call macros
  private boolean wcpCallTo(
      int w, Bytes arg1Hi, Bytes arg1Lo, Bytes arg2Hi, Bytes arg2Lo, int inst) {
    checkArgument(arg1Hi.bitLength() / 8 <= 16);
    checkArgument(arg1Lo.bitLength() / 8 <= 16);
    checkArgument(arg2Hi.bitLength() / 8 <= 16);
    checkArgument(arg2Lo.bitLength() / 8 <= 16);
    final EWord arg1 = EWord.of(Bytes.concatenate(arg1Hi, arg1Lo));
    final EWord arg2 = EWord.of(Bytes.concatenate(arg2Hi, arg2Lo));

    this.arg1Hi[w] = arg1Hi;
    this.arg1Lo[w] = arg1Lo;
    this.arg2Hi[w] = arg2Hi;
    this.arg2Lo[w] = arg2Lo;

    final boolean r;
    r =
        switch (inst) {
          case EVM_INST_LT -> wcp.callLT(arg1, arg2);
          case EVM_INST_GT -> wcp.callGT(arg1, arg2);
          case WCP_INST_LEQ -> wcp.callLEQ(arg1, arg2);
          case WCP_INST_GEQ -> wcp.callGEQ(arg1, arg2);
          case EVM_INST_ISZERO -> wcp.callISZERO(arg1);
          default -> throw new IllegalStateException("Unexpected value: " + inst);
        };
    res[w] = booleanToBytes(r);

    exoInst[w] = UnsignedByte.of(inst);

    wcpFlag[w] = true;
    eucFlag[w] = false;

    return r;
  }

  private boolean wcpCallToLT(int w, Bytes arg1Hi, Bytes arg1Lo, Bytes arg2Hi, Bytes arg2Lo) {
    return wcpCallTo(w, arg1Hi, arg1Lo, arg2Hi, arg2Lo, EVM_INST_LT);
  }

  private boolean wcpCallToGT(int w, Bytes arg1Hi, Bytes arg1Lo, Bytes arg2Hi, Bytes arg2Lo) {
    return wcpCallTo(w, arg1Hi, arg1Lo, arg2Hi, arg2Lo, EVM_INST_GT);
  }

  private boolean wcpCallToLEQ(int w, Bytes arg1Hi, Bytes arg1Lo, Bytes arg2Hi, Bytes arg2Lo) {
    return wcpCallTo(w, arg1Hi, arg1Lo, arg2Hi, arg2Lo, WCP_INST_LEQ);
  }

  private boolean wcpCallToGEQ(int w, Bytes arg1Hi, Bytes arg1Lo, Bytes arg2Hi, Bytes arg2Lo) {
    return wcpCallTo(w, arg1Hi, arg1Lo, arg2Hi, arg2Lo, WCP_INST_GEQ);
  }

  private boolean wcpCallToISZERO(int w, Bytes arg1Hi, Bytes arg1Lo) {
    return wcpCallTo(w, arg1Hi, arg1Lo, ZERO, ZERO, EVM_INST_ISZERO);
  }

  private Bytes eucCall(int w, Bytes arg1Lo, Bytes arg2Lo) {
    checkArgument(arg1Lo.bitLength() / 8 <= 16);
    checkArgument(arg2Lo.bitLength() / 8 <= 16);

    this.arg1Hi[w] = ZERO;
    this.arg1Lo[w] = arg1Lo;
    this.arg2Hi[w] = ZERO;
    this.arg2Lo[w] = arg2Lo;

    res[w] = euc.callEUC(arg1Lo, arg2Lo).quotient();

    exoInst[w] = UnsignedByte.of(EVM_INST_DIV); // TODO: is this correct?

    wcpFlag[w] = false;
    eucFlag[w] = true;

    return res[w];
  }
}
