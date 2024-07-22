/*
 * Copyright Consensys Software Inc.
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

package net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu;

import static net.consensys.linea.zktracer.module.Util.slice;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EMPTY_RIPEMD_HI;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EMPTY_RIPEMD_LO;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EMPTY_SHA2_HI;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EMPTY_SHA2_LO;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EXO_SUM_WEIGHT_BLAKEMODEXP;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EXO_SUM_WEIGHT_ECDATA;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EXO_SUM_WEIGHT_KEC;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EXO_SUM_WEIGHT_LOG;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EXO_SUM_WEIGHT_RIPSHA;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EXO_SUM_WEIGHT_ROM;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EXO_SUM_WEIGHT_TXCD;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_ANY_TO_RAM_WITH_PADDING;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_BLAKE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_EXO_TO_RAM_TRANSPLANTS;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_INVALID_CODE_PREFIX;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_MLOAD;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_MODEXP_DATA;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_MODEXP_ZERO;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_MSTORE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_RAM_TO_EXO_WITH_PADDING;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_RAM_TO_RAM_SANS_PADDING;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_RIGHT_PADDED_WORD_EXTRACTION;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_BLAKE_DATA;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_BLAKE_PARAMS;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_BLAKE_RESULT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_ECADD_DATA;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_ECADD_RESULT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_ECMUL_DATA;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_ECMUL_RESULT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_ECPAIRING_DATA;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_ECPAIRING_RESULT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_ECRECOVER_DATA;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_ECRECOVER_RESULT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_MODEXP_BASE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_MODEXP_EXPONENT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_MODEXP_MODULUS;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_MODEXP_RESULT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_RIPEMD_DATA;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_RIPEMD_RESULT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_SHA2_DATA;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_SHA2_RESULT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.RLP_TXN_PHASE_DATA;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;

import java.util.Optional;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.State;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.TraceSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu.opcode.CodeCopy;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu.opcode.Create;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu.opcode.Create2;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu.opcode.ExtCodeCopy;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu.opcode.ReturnFromDeployment;
import net.consensys.linea.zktracer.module.hub.precompiles.Blake2fMetadata;
import net.consensys.linea.zktracer.module.hub.precompiles.ModExpMetadata;
import net.consensys.linea.zktracer.module.hub.precompiles.PrecompileInvocation;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.runtime.LogInvocation;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.MemorySpan;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.internal.Words;

/**
 * This class represents a call to the MMU. However, some MMU calls may have their actual content
 * only definitely defined at tracing time, post conflation. In these cases, subclasses of this
 * class implement this defer mechanism.
 */
@RequiredArgsConstructor
@Setter
@Getter
@Accessors(fluent = true)
public class MmuCall implements TraceSubFragment {
  protected int instruction = 0;
  protected int sourceId = 0;
  protected int targetId = 0;
  protected int auxId = 0;
  protected EWord sourceOffset = EWord.ZERO;
  protected EWord targetOffset = EWord.ZERO;
  protected long size = 0;
  protected long referenceOffset = 0;
  protected long referenceSize = 0;
  protected boolean successBit = false;
  protected Bytes limb1 = Bytes.EMPTY;
  protected Bytes limb2 = Bytes.EMPTY;
  protected long phase = 0;

  private Optional<Bytes> sourceRamBytes = Optional.empty();
  private Optional<Bytes> targetRamBytes = Optional.empty();
  private Optional<Bytes> exoBytes = Optional.empty();

  protected boolean exoIsRlpTxn = false;
  protected boolean exoIsLog = false;
  protected boolean exoIsRom = false;
  protected boolean exoIsKec = false;
  protected boolean exoIsRipSha = false;
  protected boolean exoIsBlakeModexp = false;
  protected boolean exoIsEcData = false;
  private int exoSum = 0;

  private MmuCall updateExoSum(final int exoValue) {
    this.exoSum += exoValue;
    return this;
  }

  final MmuCall setRlpTxn() {
    return this.exoIsRlpTxn(true).updateExoSum(EXO_SUM_WEIGHT_TXCD);
  }

  public final MmuCall setLog() {
    return this.exoIsLog(true).updateExoSum(EXO_SUM_WEIGHT_LOG);
  }

  public final void setRom() {
    this.exoIsRom(true).updateExoSum(EXO_SUM_WEIGHT_ROM);
  }

  public final MmuCall setKec() {
    return this.exoIsKec(true).updateExoSum(EXO_SUM_WEIGHT_KEC);
  }

  final MmuCall setRipSha() {
    return this.exoIsRipSha(true).updateExoSum(EXO_SUM_WEIGHT_RIPSHA);
  }

  final MmuCall setBlakeModexp() {
    return this.exoIsBlakeModexp(true).updateExoSum(EXO_SUM_WEIGHT_BLAKEMODEXP);
  }

  final MmuCall setEcData() {
    return this.exoIsEcData(true).updateExoSum(EXO_SUM_WEIGHT_ECDATA);
  }

  // TODO: make the instruction an enum
  public MmuCall(final int instruction) {
    this.instruction = instruction;
  }

  public static MmuCall nop() {
    return new MmuCall().instruction(-1);
  }

  public static MmuCall sha3(final Hub hub) {
    return new MmuCall(MMU_INST_RAM_TO_EXO_WITH_PADDING)
        .sourceId(hub.currentFrame().contextNumber())
        .sourceRamBytes(
            Optional.of(
                hub.currentFrame()
                    .frame()
                    .shadowReadMemory(0, hub.currentFrame().frame().memoryByteSize())))
        .auxId(hub.state().stamps().hub())
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .size(Words.clampedToLong(hub.messageFrame().getStackItem(1)))
        .referenceSize(Words.clampedToLong(hub.messageFrame().getStackItem(1)))
        .setKec();
  }

  public static MmuCall callDataCopy(final Hub hub) {
    final MemorySpan callDataSegment = hub.currentFrame().callDataInfo().memorySpan();

    final int callDataContextNumber = callDataContextNumber(hub);
    final CallFrame callFrame = hub.callStack().getByContextNumber(callDataContextNumber);

    return new MmuCall(MMU_INST_ANY_TO_RAM_WITH_PADDING)
        .sourceId(callDataContextNumber)
        .sourceRamBytes(Optional.of(callFrame.callDataInfo().data()))
        .targetId(hub.currentFrame().contextNumber())
        .targetRamBytes(
            Optional.of(
                hub.currentFrame()
                    .frame()
                    .shadowReadMemory(0, hub.currentFrame().frame().memoryByteSize())))
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(1)))
        .targetOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .size(Words.clampedToLong(hub.messageFrame().getStackItem(2)))
        .referenceOffset(callDataSegment.offset())
        .referenceSize(callDataSegment.length());
  }

  public static int callDataContextNumber(final Hub hub) {
    final CallFrame currentFrame = hub.callStack().current();

    return currentFrame.isRoot()
        ? currentFrame.contextNumber() - 1
        : hub.callStack().parent().contextNumber();
  }

  public static MmuCall LogX(final Hub hub, final LogInvocation logInvocation) {
    return new MmuCall(MMU_INST_RAM_TO_EXO_WITH_PADDING)
        .sourceId(logInvocation.callFrame.contextNumber())
        .targetId(hub.state.stamps().log())
        .sourceOffset(logInvocation.offset)
        .size(logInvocation.size)
        .referenceSize(logInvocation.size)
        .sourceRamBytes(Optional.of(logInvocation.ramSourceBytes))
        .exoBytes(
            Optional.of(
                slice(
                    logInvocation.ramSourceBytes,
                    (int) Words.clampedToLong(logInvocation.offset),
                    (int) logInvocation.size)))
        .setLog();
  }

  public static MmuCall codeCopy(final Hub hub) {
    return new CodeCopy(hub);
  }

  public static MmuCall extCodeCopy(final Hub hub) {
    return new ExtCodeCopy(hub);
  }

  public static MmuCall returnDataCopy(final Hub hub) {
    final MemorySpan returnDataSegment = hub.currentFrame().latestReturnDataSource();
    final CallFrame returnerFrame =
        hub.callStack().getById(hub.currentFrame().returnDataContextNumber());
    return new MmuCall(MMU_INST_ANY_TO_RAM_WITH_PADDING)
        .sourceId(returnerFrame.contextNumber())
        .sourceRamBytes(
            Optional.of(
                returnerFrame.frame().shadowReadMemory(0, returnerFrame.frame().memoryByteSize())))
        .targetId(hub.currentFrame().contextNumber())
        .targetRamBytes(
            Optional.of(
                hub.currentFrame()
                    .frame()
                    .shadowReadMemory(0, hub.currentFrame().frame().memoryByteSize())))
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(1)))
        .targetOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .size(Words.clampedToLong(hub.messageFrame().getStackItem(2)))
        .referenceOffset(returnDataSegment.offset())
        .referenceSize(returnDataSegment.length());
  }

  public static MmuCall create(final Hub hub) {
    return new Create(hub);
  }

  public static MmuCall returnFromDeployment(final Hub hub) {
    return new ReturnFromDeployment(hub);
  }

  public static MmuCall returnFromCall(final Hub hub) {
    return MmuCall.revert(hub);
  }

  public static MmuCall create2(final Hub hub) {
    return new Create2(hub);
  }

  public static MmuCall invalidCodePrefix(final Hub hub) {
    return new MmuCall(MMU_INST_INVALID_CODE_PREFIX)
        .sourceId(hub.currentFrame().contextNumber())
        .sourceRamBytes(
            Optional.of(
                hub.currentFrame()
                    .frame()
                    .shadowReadMemory(0, hub.currentFrame().frame().memoryByteSize())))
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .successBit(Exceptions.any(hub.pch().exceptions()));
  }

  public static MmuCall revert(final Hub hub) {
    return new MmuCall(MMU_INST_RAM_TO_RAM_SANS_PADDING)
        .sourceId(hub.currentFrame().contextNumber())
        .targetId(hub.callStack().getById(hub.currentFrame().parentFrame()).contextNumber())
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .size(Words.clampedToLong(hub.messageFrame().getStackItem(1)))
        .referenceOffset(hub.currentFrame().requestedReturnDataTarget().offset())
        .referenceSize(hub.currentFrame().requestedReturnDataTarget().length());
  }

  public static MmuCall txInit(final Hub hub) {
    return new MmuCall(MMU_INST_EXO_TO_RAM_TRANSPLANTS)
        .sourceId(hub.txStack().current().getAbsoluteTransactionNumber())
        .targetId(hub.stamp())
        .size(hub.txStack().current().getBesuTransaction().getData().map(Bytes::size).orElse(0))
        .phase(RLP_TXN_PHASE_DATA)
        .setRlpTxn();
  }

  public static MmuCall forEcRecover(
      final Hub hub, PrecompileInvocation p, boolean recoverySuccessful, int i) {
    Preconditions.checkArgument(i >= 0 && i < 3);

    final int precompileContextNumber = p.hubStamp() + 1;

    if (i == 0) {
      final long inputSize = p.callDataSource().length();
      return inputSize == 0
          ? nop()
          : new MmuCall(MMU_INST_RAM_TO_EXO_WITH_PADDING)
              .sourceId(hub.currentFrame().contextNumber())
              .targetId(precompileContextNumber)
              .sourceOffset(EWord.of(p.callDataSource().offset()))
              .size(inputSize)
              .referenceSize(128)
              .successBit(recoverySuccessful)
              .phase(PHASE_ECRECOVER_DATA)
              .setEcData();
    } else if (i == 1) {
      if (recoverySuccessful) {
        return new MmuCall(MMU_INST_EXO_TO_RAM_TRANSPLANTS)
            .sourceId(precompileContextNumber)
            .targetId(precompileContextNumber)
            .size(WORD_SIZE)
            .phase(PHASE_ECRECOVER_RESULT)
            .setEcData();
      } else {
        return nop();
      }
    } else {
      if (recoverySuccessful && !p.requestedReturnDataTarget().isEmpty()) {

        return new MmuCall(MMU_INST_RAM_TO_RAM_SANS_PADDING)
            .sourceId(precompileContextNumber)
            .targetId(hub.currentFrame().contextNumber())
            .sourceOffset(EWord.ZERO)
            .size(WORD_SIZE)
            .referenceOffset(p.requestedReturnDataTarget().offset())
            .referenceSize(p.requestedReturnDataTarget().length());

      } else {
        return nop();
      }
    }
  }

  private static MmuCall forRipeMd160Sha(
      final Hub hub, PrecompileInvocation p, int i, final boolean isSha) {
    Preconditions.checkArgument(i >= 0 && i < 3);

    final int precompileContextNumber = p.hubStamp() + 1;

    if (i == 0) {
      if (p.callDataSource().isEmpty()) {
        return nop();
      } else {
        return new MmuCall(MMU_INST_RAM_TO_EXO_WITH_PADDING)
            .sourceId(hub.currentFrame().contextNumber())
            .targetId(precompileContextNumber)
            .sourceOffset(EWord.of(p.callDataSource().offset()))
            .size(p.callDataSource().length())
            .referenceSize(p.callDataSource().length())
            .phase(isSha ? PHASE_SHA2_DATA : PHASE_RIPEMD_DATA)
            .setRipSha();
      }
    } else if (i == 1) {
      if (p.callDataSource().isEmpty()) {
        return new MmuCall(MMU_INST_MSTORE)
            .targetId(precompileContextNumber)
            .targetOffset(EWord.ZERO)
            .limb1(isSha ? bigIntegerToBytes(EMPTY_SHA2_HI) : Bytes.ofUnsignedLong(EMPTY_RIPEMD_HI))
            .limb2(isSha ? bigIntegerToBytes(EMPTY_SHA2_LO) : bigIntegerToBytes(EMPTY_RIPEMD_LO));
      } else {
        return new MmuCall(MMU_INST_EXO_TO_RAM_TRANSPLANTS)
            .sourceId(precompileContextNumber)
            .targetId(precompileContextNumber)
            .size(WORD_SIZE)
            .phase(isSha ? PHASE_SHA2_RESULT : PHASE_RIPEMD_RESULT)
            .setRipSha();
      }
    } else {
      if (p.requestedReturnDataTarget().isEmpty()) {
        return nop();
      } else {
        return new MmuCall(MMU_INST_RAM_TO_RAM_SANS_PADDING)
            .sourceId(precompileContextNumber)
            .targetId(hub.currentFrame().contextNumber())
            .sourceOffset(EWord.ZERO)
            .size(WORD_SIZE)
            .referenceOffset(p.requestedReturnDataTarget().offset())
            .referenceSize(p.requestedReturnDataTarget().length());
      }
    }
  }

  public static MmuCall forSha2(final Hub hub, PrecompileInvocation p, int i) {
    return forRipeMd160Sha(hub, p, i, true);
  }

  public static MmuCall forRipeMd160(final Hub hub, PrecompileInvocation p, int i) {
    return forRipeMd160Sha(hub, p, i, false);
  }

  public static MmuCall forIdentity(final Hub hub, final PrecompileInvocation p, int i) {
    Preconditions.checkArgument(i >= 0 && i < 2);

    if (p.callDataSource().isEmpty()) {
      return nop();
    }

    final int precompileContextNumber = p.hubStamp() + 1;

    if (i == 0) {
      return new MmuCall(MMU_INST_RAM_TO_RAM_SANS_PADDING)
          .sourceId(hub.currentFrame().contextNumber())
          .targetId(precompileContextNumber)
          .sourceOffset(EWord.of(p.callDataSource().offset()))
          .size(p.callDataSource().length())
          .referenceOffset(0)
          .referenceSize(p.callDataSource().length());
    } else {
      if (p.requestedReturnDataTarget().isEmpty()) {
        return nop();
      } else {
        return new MmuCall(MMU_INST_RAM_TO_RAM_SANS_PADDING)
            .sourceId(precompileContextNumber)
            .targetId(hub.currentFrame().contextNumber())
            .sourceOffset(EWord.ZERO)
            .size(p.callDataSource().length())
            .referenceOffset(p.requestedReturnDataTarget().offset())
            .referenceSize(p.requestedReturnDataTarget().length());
      }
    }
  }

  public static MmuCall forEcAdd(final Hub hub, final PrecompileInvocation p, int i) {
    Preconditions.checkArgument(i >= 0 && i < 3);
    final int precompileContextNumber = p.hubStamp() + 1;
    if (i == 0) {
      final long inputSize = p.callDataSource().length();
      return inputSize == 0
          ? nop()
          : new MmuCall(MMU_INST_RAM_TO_EXO_WITH_PADDING)
              .sourceId(hub.currentFrame().contextNumber())
              .targetId(precompileContextNumber)
              .sourceOffset(EWord.of(p.callDataSource().offset()))
              .size(inputSize)
              .referenceSize(128)
              .successBit(!p.ramFailure())
              .setEcData()
              .phase(PHASE_ECADD_DATA);
    } else if (i == 1) {
      return new MmuCall(MMU_INST_EXO_TO_RAM_TRANSPLANTS)
          .sourceId(precompileContextNumber)
          .targetId(precompileContextNumber)
          .size(64)
          .setEcData()
          .phase(PHASE_ECADD_RESULT);
    } else {
      return new MmuCall(MMU_INST_RAM_TO_RAM_SANS_PADDING)
          .sourceId(precompileContextNumber)
          .targetId(hub.currentFrame().contextNumber())
          .targetOffset(EWord.of(p.requestedReturnDataTarget().offset()))
          .size(p.requestedReturnDataTarget().length())
          .referenceSize(64);
    }
  }

  public static MmuCall forEcMul(final Hub hub, final PrecompileInvocation p, int i) {
    Preconditions.checkArgument(i >= 0 && i < 3);
    final int precompileContextNumber = p.hubStamp() + 1;
    if (i == 0) {
      final long inputSize = p.callDataSource().length();
      return inputSize == 0
          ? nop()
          : new MmuCall(MMU_INST_RAM_TO_EXO_WITH_PADDING)
              .sourceId(hub.currentFrame().contextNumber())
              .targetId(precompileContextNumber)
              .sourceOffset(EWord.of(p.callDataSource().offset()))
              .size(inputSize)
              .referenceSize(96)
              .successBit(!p.ramFailure())
              .setEcData()
              .phase(PHASE_ECMUL_DATA);
    } else if (i == 1) {
      return new MmuCall(MMU_INST_EXO_TO_RAM_TRANSPLANTS)
          .sourceId(precompileContextNumber)
          .targetId(precompileContextNumber)
          .size(64)
          .setEcData()
          .phase(PHASE_ECMUL_RESULT);
    } else {
      return new MmuCall(MMU_INST_RAM_TO_RAM_SANS_PADDING)
          .sourceId(precompileContextNumber)
          .targetId(hub.currentFrame().contextNumber())
          .targetOffset(EWord.of(p.requestedReturnDataTarget().offset()))
          .size(p.requestedReturnDataTarget().length())
          .referenceSize(64);
    }
  }

  public static MmuCall forEcPairing(final Hub hub, final PrecompileInvocation p, int i) {
    Preconditions.checkArgument(i >= 0 && i < 3);
    final int precompileContextNumber = p.hubStamp() + 1;
    if (i == 0) {
      final long inputSize = p.callDataSource().length();
      return inputSize == 0
          ? nop()
          : new MmuCall(MMU_INST_RAM_TO_EXO_WITH_PADDING)
              .sourceId(hub.currentFrame().contextNumber())
              .targetId(precompileContextNumber)
              .sourceOffset(EWord.of(p.callDataSource().offset()))
              .size(inputSize)
              .referenceSize(p.callDataSource().length())
              .successBit(!p.ramFailure())
              .setEcData()
              .phase(PHASE_ECPAIRING_DATA);
    } else if (i == 1) {
      if (p.callDataSource().isEmpty()) {
        return new MmuCall(MMU_INST_MSTORE).targetId(precompileContextNumber).limb2(Bytes.of(1));
      } else {
        return new MmuCall(MMU_INST_EXO_TO_RAM_TRANSPLANTS)
            .sourceId(precompileContextNumber)
            .targetId(precompileContextNumber)
            .size(WORD_SIZE)
            .setEcData()
            .phase(PHASE_ECPAIRING_RESULT);
      }
    } else {
      return new MmuCall(MMU_INST_RAM_TO_RAM_SANS_PADDING)
          .sourceId(precompileContextNumber)
          .targetId(hub.currentFrame().contextNumber())
          .targetOffset(EWord.of(p.requestedReturnDataTarget().offset()))
          .size(p.requestedReturnDataTarget().length())
          .referenceSize(32);
    }
  }

  public static MmuCall forBlake2f(final Hub hub, final PrecompileInvocation p, int i) {
    Preconditions.checkArgument(i >= 0 && i < 4);
    final int precompileContextNumber = p.hubStamp() + 1;
    if (i == 0) {
      return new MmuCall(MMU_INST_BLAKE)
          .sourceId(hub.currentFrame().contextNumber())
          .targetId(precompileContextNumber)
          .sourceOffset(EWord.of(p.callDataSource().offset()))
          .successBit(!p.ramFailure())
          .limb1(EWord.of(((Blake2fMetadata) p.metadata()).r()))
          .limb2(EWord.of(((Blake2fMetadata) p.metadata()).f()))
          .setBlakeModexp()
          .phase(PHASE_BLAKE_PARAMS);
    } else if (i == 1) {
      return new MmuCall(MMU_INST_RAM_TO_EXO_WITH_PADDING)
          .sourceId(hub.currentFrame().contextNumber())
          .targetId(precompileContextNumber)
          .sourceOffset(EWord.of(p.callDataSource().offset() + 4))
          .size(208)
          .referenceSize(208)
          .setBlakeModexp()
          .phase(PHASE_BLAKE_DATA);
    } else if (i == 2) {
      return new MmuCall(MMU_INST_EXO_TO_RAM_TRANSPLANTS)
          .sourceId(precompileContextNumber)
          .targetId(precompileContextNumber)
          .size(64)
          .setBlakeModexp()
          .phase(PHASE_BLAKE_RESULT);
    } else {
      if (p.requestedReturnDataTarget().isEmpty()) {
        return MmuCall.nop();
      } else {
        return new MmuCall(MMU_INST_RAM_TO_RAM_SANS_PADDING)
            .sourceId(precompileContextNumber)
            .targetId(hub.currentFrame().contextNumber())
            .size(64)
            .referenceOffset(p.requestedReturnDataTarget().offset())
            .referenceSize(p.requestedReturnDataTarget().length());
      }
    }
  }

  public static MmuCall forModExp(final Hub hub, final PrecompileInvocation p, int i) {
    Preconditions.checkArgument(i >= 2 && i < 12);
    final ModExpMetadata m = (ModExpMetadata) p.metadata();
    final int precompileContextNumber = p.hubStamp() + 1;

    if (i == 2) {
      return new MmuCall(MMU_INST_RIGHT_PADDED_WORD_EXTRACTION)
          .sourceId(hub.currentFrame().contextNumber())
          .referenceOffset(p.callDataSource().offset())
          .referenceSize(p.callDataSource().length())
          .limb1(m.bbs().hi())
          .limb2(m.bbs().lo());
    } else if (i == 3) {
      return new MmuCall(MMU_INST_RIGHT_PADDED_WORD_EXTRACTION)
          .sourceId(hub.currentFrame().contextNumber())
          .sourceOffset(EWord.of(32))
          .referenceOffset(p.callDataSource().offset())
          .referenceSize(p.callDataSource().length())
          .limb1(m.ebs().hi())
          .limb2(m.ebs().lo());
    } else if (i == 4) {
      return new MmuCall(MMU_INST_RIGHT_PADDED_WORD_EXTRACTION)
          .sourceId(hub.currentFrame().contextNumber())
          .sourceOffset(EWord.of(64))
          .referenceOffset(p.callDataSource().offset())
          .referenceSize(p.callDataSource().length())
          .limb1(m.mbs().hi())
          .limb2(m.mbs().lo());
    } else if (i == 5) {
      return new MmuCall(MMU_INST_MLOAD)
          .sourceId(hub.currentFrame().contextNumber())
          .sourceOffset(EWord.of(p.callDataSource().offset() + 96 + m.bbs().toInt()))
          .limb1(m.rawLeadingWord().hi())
          .limb2(m.rawLeadingWord().lo());
    } else if (i == 7) {
      if (m.extractBase()) {
        return new MmuCall(MMU_INST_MODEXP_DATA)
            .sourceId(hub.currentFrame().contextNumber())
            .targetId(precompileContextNumber)
            .sourceOffset(EWord.of(96))
            .size(m.bbs().toInt())
            .referenceOffset(p.callDataSource().offset())
            .referenceSize(p.callDataSource().length())
            .phase(PHASE_MODEXP_BASE)
            .setBlakeModexp();
      } else {
        return new MmuCall(MMU_INST_MODEXP_ZERO)
            .targetId(precompileContextNumber)
            .phase(PHASE_MODEXP_BASE)
            .setBlakeModexp();
      }
    } else if (i == 8) {
      if (m.extractExponent()) {
        return new MmuCall(MMU_INST_MODEXP_DATA)
            .sourceId(hub.currentFrame().contextNumber())
            .targetId(precompileContextNumber)
            .sourceOffset(EWord.of(96 + m.bbs().toInt()))
            .size(m.ebs().toInt())
            .referenceOffset(p.callDataSource().offset())
            .referenceSize(p.callDataSource().length())
            .phase(PHASE_MODEXP_EXPONENT)
            .setBlakeModexp();
      } else {
        return new MmuCall(MMU_INST_MODEXP_ZERO)
            .targetId(precompileContextNumber)
            .phase(PHASE_MODEXP_EXPONENT)
            .setBlakeModexp();
      }
    } else if (i == 9) {
      return new MmuCall(MMU_INST_MODEXP_DATA)
          .sourceId(hub.currentFrame().contextNumber())
          .targetId(precompileContextNumber)
          .sourceOffset(EWord.of(96 + m.bbs().toInt() + m.ebs().toInt()))
          .size(m.mbs().toInt())
          .referenceOffset(p.callDataSource().offset())
          .referenceSize(p.callDataSource().length())
          .phase(PHASE_MODEXP_MODULUS)
          .setBlakeModexp();
    } else if (i == 10) {
      return new MmuCall(MMU_INST_EXO_TO_RAM_TRANSPLANTS)
          .sourceId(precompileContextNumber)
          .targetId(precompileContextNumber)
          .size(512)
          .phase(PHASE_MODEXP_RESULT)
          .setBlakeModexp();
    } else if (i == 11) {
      return new MmuCall(MMU_INST_RAM_TO_RAM_SANS_PADDING)
          .sourceId(precompileContextNumber)
          .targetId(hub.currentFrame().contextNumber())
          .sourceOffset(EWord.of(512 - m.mbs().toInt()))
          .size(m.mbs().toInt())
          .referenceOffset(p.requestedReturnDataTarget().offset())
          .referenceSize(p.requestedReturnDataTarget().length());
    } else {
      throw new IllegalArgumentException("need a boolean");
    }
  }

  @Override
  public Trace trace(Trace trace, State.TxState.Stamps stamps) {
    stamps.incrementMmuStamp();
    return trace
        .pMiscMmuFlag(true)
        .pMiscMmuInst(
            this.instruction() == -1
                ? 0
                : this.instruction()) // TODO: WTF I wanted to put -1? Only for debug?
        .pMiscMmuTgtId(this.targetId())
        .pMiscMmuSrcId(this.sourceId())
        .pMiscMmuAuxId(this.auxId())
        .pMiscMmuSrcOffsetHi(this.sourceOffset().hi())
        .pMiscMmuSrcOffsetLo(this.sourceOffset().lo())
        .pMiscMmuTgtOffsetLo(this.targetOffset().lo())
        .pMiscMmuSize(this.size())
        .pMiscMmuRefOffset(this.referenceOffset())
        .pMiscMmuRefSize(this.referenceSize())
        .pMiscMmuSuccessBit(this.successBit())
        .pMiscMmuLimb1(this.limb1())
        .pMiscMmuLimb2(this.limb2())
        .pMiscMmuExoSum(this.exoSum)
        .pMiscMmuPhase(this.phase());
  }
}
