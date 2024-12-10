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
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_GASLIMIT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_LT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_NUMBER;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_TIMESTAMP;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.LINEA_BASE_FEE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.LINEA_BLOCK_GAS_LIMIT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.LLARGE;
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
  private final Address coinbase;
  private final long timestamp;
  private final long absoluteBlockNumber;
  private final BigInteger difficulty;
  private final int relTxMax;
  private final Wcp wcp;
  private final Euc euc;
  private final TxnData txnData;
  private final BigInteger chainId;

  // TODO: follow the order of the specs
  int ctMax;
  int inst;
  boolean isCoinbase;
  boolean isTimestamp;
  boolean isNumber;
  boolean isDifficulty;
  boolean isGasLimit;
  boolean isChainId;
  boolean isBaseFee;
  Bytes dataHi;
  Bytes dataLo;
  boolean[] wcpFlag;
  boolean[] eucFlag;
  UnsignedByte[] exoInst;
  Bytes[] arg1Hi;
  Bytes[] arg1Lo;
  Bytes[] arg2Hi;
  Bytes[] arg2Lo;
  Bytes[] res;

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
      int inst) {
    this.coinbase = coinbase;
    this.timestamp = timestamp;
    this.absoluteBlockNumber = absoluteBlockNumber;
    this.difficulty = difficulty;
    this.relTxMax = relTxMax;
    this.wcp = wcp;
    this.euc = euc;
    this.txnData = txnData;
    this.chainId = chainId;

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
        EWord coinbaseAsEWord = EWord.ofHexString(this.coinbase.toHexString());
        handleCoinbase(coinbaseAsEWord);
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

  private void handleCoinbase(EWord coinbaseAsEWord) {
    this.dataHi = coinbaseAsEWord.hi();
    this.dataLo = coinbaseAsEWord.lo();

    // Row i
    wcpCallToLT(
        0, dataHi, dataLo, Bytes.ofUnsignedLong((long) Math.pow(256, 4)), Bytes.ofUnsignedLong(0));
  }

  // TODO: implement the ones below

  private void handleTimestamp() {}

  private void handleNumber() {}

  private void handleDifficulty() {}

  private void handleGasLimit() {}

  private void handleChainId() {}

  private void handleBaseFee() {}

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
  private boolean wcpCallToLT(int w, Bytes arg1Hi, Bytes arg1Lo, Bytes arg2Hi, Bytes arg2Lo) {
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

    final boolean r = wcp.callLT(arg1, arg2);
    res[w] = booleanToBytes(r);

    exoInst[w] = UnsignedByte.of(EVM_INST_LT);

    wcpFlag[w] = true;
    eucFlag[w] = false;

    return r;
  }

  // TODO: add other call macros
}
