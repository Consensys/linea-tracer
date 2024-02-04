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

package net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.mmu;

import static net.consensys.linea.zktracer.module.hub.Trace.PHASE_ECRECOVER_DATA;
import static net.consensys.linea.zktracer.module.hub.Trace.PHASE_ECRECOVER_RESULT;
import static net.consensys.linea.zktracer.module.hub.Trace.PHASE_SHA2_256_DATA;
import static net.consensys.linea.zktracer.module.hub.Trace.PHASE_SHA2_256_RESULT;
import static net.consensys.linea.zktracer.module.hub.Trace.PHASE_TRANSACTION_CALL_DATA;
import static net.consensys.linea.zktracer.module.mmu.Trace.MMU_INST_ANY_TO_RAM_WITH_PADDING;
import static net.consensys.linea.zktracer.module.mmu.Trace.MMU_INST_EXO_TO_RAM_TRANSPLANTS;
import static net.consensys.linea.zktracer.module.mmu.Trace.MMU_INST_MLOAD;
import static net.consensys.linea.zktracer.module.mmu.Trace.MMU_INST_MSTORE;
import static net.consensys.linea.zktracer.module.mmu.Trace.MMU_INST_MSTORE8;
import static net.consensys.linea.zktracer.module.mmu.Trace.MMU_INST_RAM_TO_EXO_WITH_PADDING;
import static net.consensys.linea.zktracer.module.mmu.Trace.MMU_INST_RAM_TO_RAM_SANS_PADDING;
import static net.consensys.linea.zktracer.module.mmu.Trace.MMU_INST_RIGHT_PADDED_WORD_EXTRACTION;

import java.util.Arrays;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.TraceSubFragment;
import net.consensys.linea.zktracer.module.hub.precompiles.PrecompileInvocation;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.MemorySpan;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.internal.Words;

@RequiredArgsConstructor
@Setter
@Accessors(fluent = true)
public class GenericMmuSubFragment implements TraceSubFragment {
  protected boolean enabled = true;
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

  protected boolean enabled() {
    return this.enabled;
  }

  protected int instruction() {
    return this.instruction;
  }

  protected int sourceId() {
    return this.sourceId;
  }

  protected int targetId() {
    return this.targetId;
  }

  protected int auxId() {
    return this.auxId;
  }

  protected EWord sourceOffset() {
    return this.sourceOffset;
  }

  protected EWord targetOffset() {
    return this.targetOffset;
  }

  protected long size() {
    return this.size;
  }

  protected long referenceOffset() {
    return this.referenceOffset;
  }

  protected long referenceSize() {
    return this.referenceSize;
  }

  protected boolean successBit() {
    return this.successBit;
  }

  protected Bytes limb1() {
    return this.limb1;
  }

  protected Bytes limb2() {
    return this.limb2;
  }

  protected long phase() {
    return this.phase;
  }

  private int exoSum = 0;

  private GenericMmuSubFragment setFlag(int pos) {
    this.exoSum |= 1 >> pos;
    return this;
  }

  final GenericMmuSubFragment setRlpTxn() {
    return this.setFlag(0);
  }

  final GenericMmuSubFragment setLog() {
    return this.setFlag(1);
  }

  final GenericMmuSubFragment setRom() {
    return this.setFlag(2);
  }

  final GenericMmuSubFragment setHash() {
    return this.setFlag(3);
  }

  final GenericMmuSubFragment setRipSha() {
    return this.setFlag(4);
  }

  final GenericMmuSubFragment setBlakeModexp() {
    return this.setFlag(5);
  }

  final GenericMmuSubFragment setEcData() {
    return this.setFlag(6);
  }

  public GenericMmuSubFragment(int instruction) {
    this.instruction = instruction;
  }

  public static GenericMmuSubFragment nop() {
    return new GenericMmuSubFragment();
  }

  public static GenericMmuSubFragment sha3(final Hub hub) {
    return new GenericMmuSubFragment(MMU_INST_RAM_TO_EXO_WITH_PADDING)
        .sourceId(hub.currentFrame().contextNumber())
        .auxId(hub.state().stamps().hashInfo())
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .size(Words.clampedToLong(hub.messageFrame().getStackItem(1)))
        .referenceSize(Words.clampedToLong(hub.messageFrame().getStackItem(1)))
        .setHash();
  }

  public static GenericMmuSubFragment callDataLoad(final Hub hub) {
    final long offset = hub.currentFrame().callDataSource().offset();
    final long size = hub.currentFrame().callDataSource().length();

    final long sourceOffset = Words.clampedToLong(hub.messageFrame().getStackItem(0));

    if (sourceOffset >= size) {
      return nop();
    }

    final EWord read =
        EWord.of(
            Bytes.wrap(
                Arrays.copyOfRange(
                    hub.currentFrame().callData().toArray(), (int) offset, (int) (offset + 32))));

    return new GenericMmuSubFragment(MMU_INST_RIGHT_PADDED_WORD_EXTRACTION)
        .sourceId(hub.callStack().get(hub.currentFrame().parentFrame()).contextNumber())
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .referenceOffset(offset)
        .referenceSize(size)
        .limb1(read.hi())
        .limb2(read.lo());
  }

  public static GenericMmuSubFragment callDataCopy(final Hub hub) {
    final MemorySpan callDataSegment = hub.currentFrame().callDataSource();
    return new GenericMmuSubFragment(MMU_INST_ANY_TO_RAM_WITH_PADDING)
        .sourceId(hub.tx().number())
        .targetId(hub.currentFrame().contextNumber())
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(1)))
        .targetOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .size(Words.clampedToLong(hub.messageFrame().getStackItem(2)))
        .referenceOffset(callDataSegment.offset())
        .referenceSize(callDataSegment.length());
  }

  public static GenericMmuSubFragment codeCopy(final Hub hub) {
    return new CodeCopyMmuSubFragment(hub);
  }

  public static GenericMmuSubFragment extCodeCopy(final Hub hub) {
    return new ExtCodeCopyMmuSubFragment(hub);
  }

  public static GenericMmuSubFragment returnDataCopy(final Hub hub) {
    final MemorySpan returnDataSegment = hub.currentFrame().currentReturnDataSource();
    return new GenericMmuSubFragment(MMU_INST_ANY_TO_RAM_WITH_PADDING)
        .sourceId(hub.callStack().get(hub.currentFrame().currentReturner()).contextNumber())
        .targetId(hub.currentFrame().contextNumber())
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(1)))
        .targetOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .size(Words.clampedToLong(hub.messageFrame().getStackItem(2)))
        .referenceOffset(returnDataSegment.offset())
        .referenceSize(returnDataSegment.length());
  }

  public static GenericMmuSubFragment mload(final Hub hub) {
    final long offset = Words.clampedToLong(hub.messageFrame().getStackItem(0));
    final EWord loadedValue = EWord.of(hub.messageFrame().shadowReadMemory(offset, 32));
    return new GenericMmuSubFragment(MMU_INST_MLOAD)
        .sourceId(hub.currentFrame().contextNumber())
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .limb1(loadedValue.hi())
        .limb2(loadedValue.lo());
  }

  public static GenericMmuSubFragment mstore(final Hub hub) {
    final EWord storedValue = EWord.of(hub.messageFrame().getStackItem(1));
    return new GenericMmuSubFragment(MMU_INST_MSTORE)
        .sourceId(hub.currentFrame().contextNumber())
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .limb1(storedValue.hi())
        .limb2(storedValue.lo());
  }

  public static GenericMmuSubFragment mstore8(final Hub hub) {
    final EWord storedValue = EWord.of(hub.messageFrame().getStackItem(1));
    return new GenericMmuSubFragment(MMU_INST_MSTORE8)
        .sourceId(hub.currentFrame().contextNumber())
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .limb1(storedValue.hi())
        .limb2(storedValue.lo());
  }

  public static GenericMmuSubFragment log(final Hub hub) {
    return new LogMmuSubFragment(hub);
  }

  public static GenericMmuSubFragment create(final Hub hub) {
    return new CreateMmuSubFragment(hub);
  }

  public static GenericMmuSubFragment returnFromDeployment(final Hub hub) {
    return new ReturnFromDeploymnetMmuSubFragment(hub);
  }

  public static GenericMmuSubFragment returnFromCall(final Hub hub) {
    return GenericMmuSubFragment.revert(hub);
  }

  public static GenericMmuSubFragment create2(final Hub hub) {
    return new Create2MmuSubFragment(hub);
  }

  public static GenericMmuSubFragment revert(final Hub hub) {
    return new GenericMmuSubFragment(MMU_INST_RAM_TO_EXO_WITH_PADDING)
        .sourceId(hub.currentFrame().contextNumber())
        .targetId(hub.callStack().get(hub.currentFrame().parentFrame()).contextNumber())
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .size(Words.clampedToLong(hub.messageFrame().getStackItem(1)))
        .referenceOffset(hub.currentFrame().returnDataTarget().offset())
        .referenceSize(hub.currentFrame().returnDataTarget().length());
  }

  public static GenericMmuSubFragment txInit(final Hub hub) {
    return new GenericMmuSubFragment(MMU_INST_EXO_TO_RAM_TRANSPLANTS)
        .sourceId(hub.tx().number())
        .targetId(hub.stamp())
        .size(hub.tx().transaction().getData().map(Bytes::size).orElse(0))
        .phase(PHASE_TRANSACTION_CALL_DATA)
        .setRlpTxn();
  }

  public static GenericMmuSubFragment forEcRecover(
      final Hub hub, PrecompileInvocation p, boolean recoverySuccessful, int i) {
    Preconditions.checkArgument(i >= 0 && i < 3);

    if (i == 0) {
      return new GenericMmuSubFragment(MMU_INST_RAM_TO_EXO_WITH_PADDING)
          .sourceId(hub.currentFrame().contextNumber())
          .targetId(hub.stamp() + 1)
          .sourceOffset(EWord.of(p.callDataSource().offset()))
          .size(p.callDataSource().length())
          .referenceSize(128)
          .successBit(recoverySuccessful)
          .phase(PHASE_ECRECOVER_DATA)
          .setEcData();
    } else if (i == 1) {
      if (recoverySuccessful) {
        return new GenericMmuSubFragment(MMU_INST_EXO_TO_RAM_TRANSPLANTS)
            .sourceId(hub.stamp() + 1)
            .targetId(hub.stamp() + 1)
            .size(32)
            .phase(PHASE_ECRECOVER_RESULT)
            .setEcData();
      } else {
        return nop();
      }
    } else {
      if (recoverySuccessful && !p.requestedReturnDataTarget().isEmpty()) {
        return new GenericMmuSubFragment(MMU_INST_RAM_TO_RAM_SANS_PADDING)
            .sourceId(hub.stamp() + 1)
            .targetId(hub.currentFrame().contextNumber())
            .sourceOffset(EWord.ZERO)
            .size(32)
            .referenceOffset(p.requestedReturnDataTarget().offset())
            .referenceSize(p.requestedReturnDataTarget().length());

      } else {
        return nop();
      }
    }
  }

  private static GenericMmuSubFragment forRipeMd160Sha(
      final Hub hub, PrecompileInvocation p, int i, Bytes emptyHi, Bytes emptyLo) {
    Preconditions.checkArgument(i >= 0 && i < 3);

    if (i == 0) {
      if (p.callDataSource().isEmpty()) {
        return nop();
      } else {
        return new GenericMmuSubFragment(MMU_INST_RAM_TO_EXO_WITH_PADDING)
            .sourceId(hub.currentFrame().contextNumber())
            .targetId(hub.stamp() + 1)
            .sourceOffset(EWord.of(p.callDataSource().offset()))
            .size(p.callDataSource().length())
            .referenceSize(p.callDataSource().length())
            .phase(PHASE_SHA2_256_DATA)
            .setRipSha();
      }
    } else if (i == 1) {
      if (p.callDataSource().isEmpty()) {
        return new GenericMmuSubFragment(MMU_INST_MSTORE)
            .targetId(hub.stamp() + 1)
            .targetOffset(EWord.ZERO)
            .limb1(emptyHi)
            .limb2(emptyLo);
      } else {
        return new GenericMmuSubFragment(MMU_INST_EXO_TO_RAM_TRANSPLANTS)
            .sourceId(hub.stamp() + 1)
            .targetId(hub.stamp() + 1)
            .size(32)
            .phase(PHASE_SHA2_256_RESULT)
            .setRipSha();
      }
    } else {
      if (p.requestedReturnDataTarget().isEmpty()) {
        return nop();
      } else {
        return new GenericMmuSubFragment(MMU_INST_RAM_TO_RAM_SANS_PADDING)
            .sourceId(hub.stamp() + 1)
            .targetId(hub.currentFrame().contextNumber())
            .sourceOffset(EWord.ZERO)
            .size(32)
            .referenceOffset(p.requestedReturnDataTarget().offset())
            .referenceSize(p.requestedReturnDataTarget().length());
      }
    }
  }

  public static GenericMmuSubFragment forSha2(final Hub hub, PrecompileInvocation p, int i) {
    return forRipeMd160Sha(
        hub,
        p,
        i,
        Bytes.fromHexString("e3b0c44298fc1c149afbf4c8996fb924"),
        Bytes.fromHexString("27ae41e4649b934ca495991b7852b855")); // SHA2-256({}) hi/lo
  }

  public static GenericMmuSubFragment forRipeMd160(final Hub hub, PrecompileInvocation p, int i) {
    return forRipeMd160Sha(
        hub,
        p,
        i,
        Bytes.fromHexString("9c1185a5"),
        Bytes.fromHexString("c5e9fc54612808977ee8f548b2258d31")); // RIPEMD160({}) hi/lo
  }

  public static GenericMmuSubFragment forIdentity(final Hub hub, PrecompileInvocation p, int i) {
    Preconditions.checkArgument(i >= 0 && i < 2);

    if (p.callDataSource().isEmpty()) {
      return nop();
    }

    if (i == 0) {
      return new GenericMmuSubFragment(MMU_INST_RAM_TO_RAM_SANS_PADDING)
          .sourceId(hub.currentFrame().contextNumber())
          .targetId(hub.stamp() + 1)
          .sourceOffset(EWord.of(p.callDataSource().offset()))
          .size(p.callDataSource().length())
          .referenceOffset(0)
          .referenceSize(p.callDataSource().length());
    } else {
      if (p.requestedReturnDataTarget().isEmpty()) {
        return nop();
      } else {
        return new GenericMmuSubFragment(MMU_INST_RAM_TO_RAM_SANS_PADDING)
            .sourceId(hub.stamp() + 1)
            .targetId(hub.currentFrame().contextNumber())
            .sourceOffset(EWord.ZERO)
            .size(p.callDataSource().length())
            .referenceOffset(p.requestedReturnDataTarget().offset())
            .referenceSize(p.requestedReturnDataTarget().length());
      }
    }
  }

  @Override
  public Trace trace(Trace trace) {
    return trace
        .pMiscellaneousMmuFlag(this.enabled())
        .pMiscellaneousMmuInst(Bytes.ofUnsignedInt(this.instruction()))
        .pMiscellaneousMmuTgtId(Bytes.ofUnsignedInt(this.sourceId()))
        .pMiscellaneousMmuSrcId(Bytes.ofUnsignedInt(this.targetId()))
        .pMiscellaneousMmuAuxId(Bytes.ofUnsignedInt(this.auxId()))
        .pMiscellaneousMmuSrcOffsetHi(this.sourceOffset().hi())
        .pMiscellaneousMmuSrcOffsetLo(this.sourceOffset().lo())
        .pMiscellaneousMmuTgtOffsetLo(this.targetOffset().lo())
        .pMiscellaneousMmuSize(Bytes.ofUnsignedLong(this.size()))
        .pMiscellaneousMmuRefOffset(Bytes.ofUnsignedLong(this.referenceOffset()))
        .pMiscellaneousMmuRefSize(Bytes.ofUnsignedLong(this.referenceSize()))
        .pMiscellaneousMmuSuccessBit(this.successBit())
        .pMiscellaneousMmuLimb1(this.limb1())
        .pMiscellaneousMmuLimb2(this.limb2())
        .pMiscellaneousMmuExoSum(Bytes.ofUnsignedLong(this.exoSum))
        .pMiscellaneousMmuPhase(Bytes.ofUnsignedLong(this.phase()));
  }
}
