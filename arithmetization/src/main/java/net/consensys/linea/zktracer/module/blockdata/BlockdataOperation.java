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
import static net.consensys.linea.zktracer.module.blockdata.Trace.*;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_GT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_ISZERO;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_LT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.LLARGE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WCP_INST_GEQ;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WCP_INST_LEQ;
import static net.consensys.linea.zktracer.module.constants.Trace.GAS_LIMIT_ADJUSTMENT_FACTOR;
import static net.consensys.linea.zktracer.module.constants.Trace.LINEA_GAS_LIMIT_MAXIMUM;
import static net.consensys.linea.zktracer.module.constants.Trace.LINEA_GAS_LIMIT_MINIMUM;
import static net.consensys.linea.zktracer.types.Conversions.booleanToBytes;

import java.util.Arrays;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.plugin.data.BlockHeader;

@Accessors(fluent = true)
@Getter
public class BlockdataOperation extends ModuleOperation {
  private final Wcp wcp;
  private final Euc euc;
  private final Bytes chainId;
  private final BlockHeader blockHeader;
  private final BlockHeader prevBlockHeader;
  private final Bytes ZERO = Bytes.fromHexString("0x00000000000000000000000000000000");
  private final Bytes POWER_256_4 = Bytes.fromHexString("0x00000000000000000000000100000000");
  private final Bytes POWER_256_6 = Bytes.fromHexString("0x00000000000000000001000000000000");

  private final boolean firstBlockInConflation;
  private final int ctMax;
  @EqualsAndHashCode.Include @Getter private final OpCode opCode;
  private final long firstBlockNumber;
  private final int relTxMax;
  private final long relBlock;

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
      BlockHeader blockHeader,
      BlockHeader prevBlockHeader,
      int relTxMax,
      Wcp wcp,
      Euc euc,
      Bytes chainId,
      OpCode opCode,
      long firstBlockNumber) {
    // Data from blockHeader
    this.blockHeader = blockHeader;
    this.prevBlockHeader = prevBlockHeader;

    this.chainId = chainId;
    this.ctMax = ctMax(opCode);
    this.firstBlockNumber = firstBlockNumber;
    this.relTxMax = relTxMax;
    // TODO: add method
    this.relBlock = blockHeader.getNumber() - firstBlockNumber + 1;
    this.firstBlockInConflation = (blockHeader.getNumber() == firstBlockNumber);
    this.wcp = wcp;
    this.euc = euc;
    this.opCode = opCode;

    // Init non-counter constant columns arrays of size ctMax
    this.wcpFlag = new boolean[ctMax];
    this.eucFlag = new boolean[ctMax];
    this.exoInst = new UnsignedByte[ctMax];
    this.arg1Hi = new Bytes[ctMax];
    this.arg1Lo = new Bytes[ctMax];
    this.arg2Hi = new Bytes[ctMax];
    this.arg2Lo = new Bytes[ctMax];
    this.res = new Bytes[ctMax];
    Arrays.fill(exoInst, UnsignedByte.ZERO);
    Arrays.fill(arg1Hi, ZERO);
    Arrays.fill(arg1Lo, ZERO);
    Arrays.fill(arg2Hi, ZERO);
    Arrays.fill(arg2Lo, ZERO);
    Arrays.fill(res, ZERO);

    // Handle opcodes
    switch (opCode) {
      case OpCode.COINBASE -> {
        handleCoinbase();
      }
      case OpCode.TIMESTAMP -> {
        handleTimestamp();
      }
      case OpCode.NUMBER -> {
        handleNumber();
      }
      case OpCode.DIFFICULTY -> {
        handleDifficulty();
      }
      case OpCode.GASLIMIT -> {
        handleGasLimit();
      }
      case OpCode.CHAINID -> {
        handleChainId();
      }
      case OpCode.BASEFEE -> {
        handleBaseFee();
      }
    }
  }

  private void handleCoinbase() {
    dataHi = EWord.ofHexString(blockHeader.getCoinbase().toHexString()).hi();
    dataLo = EWord.ofHexString(blockHeader.getCoinbase().toHexString()).lo();

    // row i
    wcpCallToLT(0, dataHi, dataLo, POWER_256_4, ZERO);
  }

  private void handleTimestamp() {
    dataHi = ZERO;
    dataLo = EWord.of(blockHeader.getTimestamp()).lo();
    Bytes prevDataLo =
        prevBlockHeader == null ? ZERO : EWord.of(prevBlockHeader.getTimestamp()).lo();

    // row i
    wcpCallToLT(0, dataHi, dataLo, ZERO, POWER_256_6);

    // row i + 1
    wcpCallToGT(1, dataHi, dataLo, ZERO, prevDataLo);
  }

  private void handleNumber() {
    dataHi = ZERO;
    dataLo = Bytes.ofUnsignedLong(blockHeader.getNumber());

    wcpCallToISZERO(0, dataHi, Bytes.ofUnsignedLong(firstBlockNumber));

    // row i
    if (firstBlockInConflation) {
      wcpCallToLT(1, dataHi, dataLo, ZERO, POWER_256_6);
    }
  }

  private void handleDifficulty() {
    dataHi = EWord.of(blockHeader.getDifficulty().getAsBigInteger()).hi(); // ?
    dataLo = EWord.of(blockHeader.getDifficulty().getAsBigInteger()).lo();

    // row i
    wcpCallToGEQ(0, dataHi, dataLo, ZERO, ZERO);
  }

  private void handleGasLimit() {
    dataHi = ZERO;
    dataLo = EWord.of(blockHeader.getGasLimit()).lo();

    // row i
    wcpCallToGEQ(0, dataHi, dataLo, ZERO, Bytes.ofUnsignedLong(LINEA_GAS_LIMIT_MINIMUM));

    // row i + 1
    wcpCallToLEQ(1, dataHi, dataLo, ZERO, Bytes.ofUnsignedLong(LINEA_GAS_LIMIT_MAXIMUM));

    if (!firstBlockInConflation) {
      Bytes prevGasLimit = Bytes.ofUnsignedLong(prevBlockHeader.getGasLimit());
      // row i + 2
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
      wcpCallToGT(
          4,
          dataHi,
          dataLo,
          ZERO,
          Bytes.ofUnsignedLong(
              prevGasLimit.toLong() - maxDeviation.toLong())); // TODO: double check this
    }
  }

  private void handleChainId() {
    dataHi = ZERO;
    dataLo = EWord.of(chainId).lo();

    // row i
    wcpCallToGEQ(0, dataHi, dataLo, ZERO, ZERO);
  }

  private void handleBaseFee() {
    dataHi = ZERO;
    dataLo =
        EWord.of(blockHeader.getBaseFee().get().getAsBigInteger()).lo(); // TODO: change to baseFee

    // row i
    wcpCallToGEQ(0, dataHi, dataLo, ZERO, ZERO);
  }

  @Override
  protected int computeLineCount() {
    return ctMax;
  }

  public void trace(Trace trace) {
    for (short ct = 0; ct < ctMax; ct++) {
      trace
          .iomf(true)
          .ctMax(ctMax - 1)
          .ct(ct)
          .isCoinbase(opCode == OpCode.COINBASE)
          .isTimestamp(opCode == OpCode.TIMESTAMP)
          .isNumber(opCode == OpCode.NUMBER)
          .isDifficulty(opCode == OpCode.DIFFICULTY)
          .isGaslimit(opCode == OpCode.GASLIMIT)
          .isChainid(opCode == OpCode.CHAINID)
          .isBasefee(opCode == OpCode.BASEFEE)
          .inst(UnsignedByte.of(opCode.byteValue()))
          .coinbaseHi(blockHeader.getCoinbase().slice(0, 4).toLong())
          .coinbaseLo(blockHeader.getCoinbase().slice(4, LLARGE))
          .blockGasLimit(blockHeader.getGasLimit())
          .basefee(
              blockHeader.getBaseFee().get().getAsBigInteger().longValue()) // TODO: add baseFee
          .firstBlockNumber(firstBlockNumber)
          .relBlock((short) relBlock)
          .relTxNumMax((short) relTxMax)
          .dataHi(dataHi)
          .dataLo(dataLo)
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

    wcpFlag[w] = false;
    eucFlag[w] = true;

    return res[w];
  }

  private int ctMax(OpCode opCode) {
    switch (opCode) {
      case OpCode.COINBASE -> {
        return nROWS_CB;
      }
      case OpCode.TIMESTAMP -> {
        return nROWS_TS;
      }
      case OpCode.NUMBER -> {
        return nROWS_NB;
      }
      case OpCode.DIFFICULTY -> {
        return nROWS_DF;
      }
      case OpCode.GASLIMIT -> {
        return nROWS_GL;
      }
      case OpCode.CHAINID -> {
        return nROWS_ID;
      }
      case OpCode.BASEFEE -> {
        return nROWS_BF;
      }
      default -> {
        return nROWS_DEPTH;
      }
    }
  }
}
