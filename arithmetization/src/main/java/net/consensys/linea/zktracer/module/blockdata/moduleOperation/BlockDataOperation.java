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

package net.consensys.linea.zktracer.module.blockdata.moduleOperation;

import static net.consensys.linea.zktracer.Trace.*;
import static net.consensys.linea.zktracer.Trace.Blockdata.nROWS_BF;
import static net.consensys.linea.zktracer.Trace.Blockdata.nROWS_CB;
import static net.consensys.linea.zktracer.Trace.Blockdata.nROWS_GL;
import static net.consensys.linea.zktracer.Trace.Blockdata.nROWS_ID;
import static net.consensys.linea.zktracer.Trace.Blockdata.nROWS_NB;
import static net.consensys.linea.zktracer.Trace.Blockdata.nROWS_TS;
import static net.consensys.linea.zktracer.TraceCancun.Blockdata.nROWS_BL;
import static net.consensys.linea.zktracer.TraceLondon.Blockdata.nROWS_DF;
import static net.consensys.linea.zktracer.TraceShanghai.Blockdata.nROWS_PV;
import static net.consensys.linea.zktracer.opcode.OpCode.*;
import static net.consensys.linea.zktracer.types.Conversions.*;
import static net.consensys.linea.zktracer.types.PublicInputs.LINEA_BLOB_BASE_FEE_BYTES;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.ChainConfig;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.plugin.data.BlockHeader;

@Accessors(fluent = true)
@Getter
public abstract class BlockDataOperation extends ModuleOperation {
  private final Hub hub;
  private final Wcp wcp;
  private final Euc euc;
  private final EWord gasLimitMinimum;
  private final EWord gasLimitMaximum;
  private final Bytes chainId;
  private final BlockHeader blockHeader;
  private final BlockHeader prevBlockHeader;
  private static final EWord POWER_256_20 = EWord.of(TWOFIFTYSIX_TO_THE_TWENTY);
  private static final EWord POWER_256_8 = EWord.of(BigInteger.ONE.shiftLeft(8 * 8));

  private final boolean firstBlockInConflation;
  private final int nbRows;
  private final OpCode opCode;
  private final long firstBlockNumber;
  private final int relTxMax;
  @Getter private final int relBlock;
  private final EWord blobBaseFee;

  protected EWord data;
  private EWord[] arg1;
  private EWord[] arg2;
  private Bytes[] res;
  private final UnsignedByte[] exoInst;
  private final boolean[] wcpFlag;
  private final boolean[] eucFlag;

  public BlockDataOperation(
      Hub hub,
      BlockHeader blockHeader,
      BlockHeader prevBlockHeader,
      int relTxMax,
      Wcp wcp,
      Euc euc,
      ChainConfig chain,
      OpCode opCode,
      long firstBlockNumber,
      Map<Long, Bytes> blobBaseFees) {
    // Data from blockHeader
    this.hub = hub;
    this.blockHeader = blockHeader;
    this.prevBlockHeader = prevBlockHeader;
    this.gasLimitMinimum = EWord.of(chain.gasLimitMinimum);
    this.gasLimitMaximum = EWord.of(chain.gasLimitMaximum);
    this.chainId = EWord.of(chain.id);
    this.nbRows = nbRows(opCode);
    blobBaseFee =
        EWord.of(blobBaseFees.getOrDefault(blockHeader.getNumber(), LINEA_BLOB_BASE_FEE_BYTES));
    this.firstBlockNumber = firstBlockNumber;
    this.relTxMax = relTxMax;
    this.relBlock = (int) (blockHeader.getNumber() - firstBlockNumber + 1);
    this.firstBlockInConflation = (blockHeader.getNumber() == firstBlockNumber);
    this.wcp = wcp;
    this.euc = euc;
    this.opCode = opCode;

    // Init non-counter constant columns arrays of size ctMax
    this.wcpFlag = new boolean[nbRows];
    this.eucFlag = new boolean[nbRows];
    this.exoInst = new UnsignedByte[nbRows];
    this.arg1 = new EWord[nbRows];
    this.arg2 = new EWord[nbRows];
    this.res = new Bytes[nbRows];
    Arrays.fill(exoInst, UnsignedByte.ZERO);
    Arrays.fill(arg1, EWord.ZERO);
    Arrays.fill(arg2, EWord.ZERO);
    Arrays.fill(res, EWord.ZERO);
  }

  @Override
  protected int computeLineCount() {
    return nbRows;
  }

  public void trace(Trace.Blockdata trace) {
    for (short ct = 0; ct < nbRows; ct++) {
      trace
          .iomf(true)
          .ctMax(nbRows - 1)
          .ct(ct)
          .isCoinbase(opCode == COINBASE)
          .isTimestamp(opCode == TIMESTAMP)
          .isNumber(opCode == NUMBER);
      traceIsDifficulty(trace, opCode);
      traceIsPrevRandao(trace, opCode);
      trace
          .isGaslimit(opCode == GASLIMIT)
          .isChainid(opCode == CHAINID)
          .isBasefee(opCode == BASEFEE);
      traceIsBlobBaseFee(trace, opCode);
      trace
          .inst(opCode.unsignedByteValue()) // not fork dependant
          .coinbaseHi(hub.coinbaseAddressOfRelativeBlock(relBlock).slice(0, 4).toLong())
          .coinbaseLo(hub.coinbaseAddressOfRelativeBlock(relBlock).slice(4, LLARGE))
          .blockGasLimit(Bytes.ofUnsignedLong(blockHeader.getGasLimit()))
          .basefee(bigIntegerToBytes(blockHeader.getBaseFee().get().getAsBigInteger()))
          .firstBlockNumber(firstBlockNumber)
          .relBlock((short) relBlock);
      traceRelTxNumMax(trace, (short) relTxMax);
      trace
          .dataHi(data.hi())
          .dataLo(data.lo())
          .arg1Hi(arg1[ct].hi())
          .arg1Lo(arg1[ct].lo())
          .arg2Hi(arg2[ct].hi())
          .arg2Lo(arg2[ct].lo())
          .res(res[ct])
          .exoInst(exoInst[ct])
          .wcpFlag(wcpFlag[ct])
          .eucFlag(eucFlag[ct]);
      traceTimestampAndNumber(trace);
      trace.fillAndValidateRow();
    }
  }

  protected abstract void traceTimestampAndNumber(Trace.Blockdata trace);

  protected abstract void traceIsDifficulty(Trace.Blockdata trace, OpCode opCode);

  protected abstract void traceIsPrevRandao(Trace.Blockdata trace, OpCode opCode);

  protected abstract void traceIsBlobBaseFee(Trace.Blockdata trace, OpCode opCode);

  protected abstract void traceRelTxNumMax(Trace.Blockdata trace, short relTxMax);

  private int nbRows(OpCode opCode) {
    return switch (opCode) {
      case COINBASE -> nROWS_CB;
      case TIMESTAMP -> nROWS_TS;
      case NUMBER -> nROWS_NB;
      case DIFFICULTY -> nROWS_DF; // London only
      case PREVRANDAO -> nROWS_PV; // Paris and after
      case GASLIMIT -> nROWS_GL;
      case CHAINID -> nROWS_ID;
      case BASEFEE -> nROWS_BF;
      case BLOBBASEFEE -> nROWS_BL; // Cancun and after
      default -> throw new IllegalArgumentException("Not a valid opcode for BlockData: " + opCode);
    };
  }
}
