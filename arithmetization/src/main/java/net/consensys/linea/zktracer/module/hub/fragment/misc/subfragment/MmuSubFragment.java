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

package net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment;

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
import java.util.function.IntSupplier;

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
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.internal.Words;

@RequiredArgsConstructor
@Setter
@Accessors(fluent = true)
public class MmuSubFragment implements TraceSubFragment {
  /**
   * Captures an integer and implements {@link IntSupplier} always returning the captured value.
   *
   * @param x the constant to capture
   */
  private record ConstantIntSupplier(int x) implements IntSupplier {
    static ConstantIntSupplier of(int x) {
      return new ConstantIntSupplier(x);
    }

    @Override
    public int getAsInt() {
      return x;
    }
  }

  /**
   * Captures a code fragment index and implements {@link IntSupplier} returning its sorted alter
   * ego.
   *
   * @param hub the execution environment
   * @param cfi the CFI to find the sorted alter ego
   */
  private record SortedCfiProvider(Hub hub, int cfi) implements IntSupplier {
    static SortedCfiProvider of(final Hub hub, int cfi) {
      return new SortedCfiProvider(hub, cfi);
    }

    static SortedCfiProvider ofLookup(final Hub hub, final Address targetAddress) {
      int targetCfi = 0;
      try {
        targetCfi =
            hub.romLex()
                .getCfiByMetadata(
                    targetAddress,
                    hub.conflation().deploymentInfo().number(targetAddress),
                    hub.conflation().deploymentInfo().isDeploying(targetAddress));
      } catch (Exception ignored) {
        // In this case, targetCfi is already equal to 0
      }

      return new SortedCfiProvider(hub, targetCfi);
    }

    @Override
    public int getAsInt() {
      return hub.romLex().getCfiById(this.cfi);
    }
  }

  private final int instruction;
  /*
   * The next two are provider instead of simple integers to allow fetching sorted code fragment indices,
   * which only exist after the conflation execution. However, this will probably lead to performances
   * issues at some point, to which the long-term solution will be to convert the MmuSubFragment into an
   * interface and subclass it for all different invocation methods.
   */
  private IntSupplier sourceId;
  private IntSupplier targetId;
  private int auxId = 0;
  private EWord sourceOffset = EWord.ZERO;
  private EWord targetOffset = EWord.ZERO;
  private long size = 0;
  private long referenceOffset = 0;
  private long referenceSize = 0;
  private boolean successBit = false;
  private Bytes limb1 = Bytes.EMPTY;
  private Bytes limb2 = Bytes.EMPTY;
  private int exoSum = 0;
  private long phase = 0;

  private MmuSubFragment setFlag(int pos) {
    this.exoSum |= 1 >> pos;
    return this;
  }

  MmuSubFragment setRlpTxn() {
    return this.setFlag(0);
  }

  MmuSubFragment setLog() {
    return this.setFlag(1);
  }

  MmuSubFragment setRom() {
    return this.setFlag(2);
  }

  MmuSubFragment setHash() {
    return this.setFlag(3);
  }

  MmuSubFragment setRipSha() {
    return this.setFlag(4);
  }

  MmuSubFragment setBlakeModexp() {
    return this.setFlag(5);
  }

  MmuSubFragment setEcData() {
    return this.setFlag(6);
  }

  public static MmuSubFragment nop() {
    return new MmuSubFragment(0);
  }

  public static MmuSubFragment sha3(final Hub hub) {
    return new MmuSubFragment(MMU_INST_RAM_TO_EXO_WITH_PADDING)
        .sourceId(ConstantIntSupplier.of(hub.currentFrame().contextNumber()))
        .auxId(hub.state().stamps().hashInfo())
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .size(Words.clampedToLong(hub.messageFrame().getStackItem(1)))
        .referenceSize(Words.clampedToLong(hub.messageFrame().getStackItem(1)))
        .setHash();
  }

  public static MmuSubFragment callDataLoad(final Hub hub) {
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

    return new MmuSubFragment(MMU_INST_RIGHT_PADDED_WORD_EXTRACTION)
        .sourceId(
            ConstantIntSupplier.of(
                hub.callStack().get(hub.currentFrame().parentFrame()).contextNumber()))
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .referenceOffset(offset)
        .referenceSize(size)
        .limb1(read.hi())
        .limb2(read.lo());
  }

  public static MmuSubFragment callDataCopy(final Hub hub) {
    final MemorySpan callDataSegment = hub.currentFrame().callDataSource();
    return new MmuSubFragment(MMU_INST_ANY_TO_RAM_WITH_PADDING)
        .sourceId(ConstantIntSupplier.of(hub.tx().number()))
        .targetId(ConstantIntSupplier.of(hub.currentFrame().contextNumber()))
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(1)))
        .targetOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .size(Words.clampedToLong(hub.messageFrame().getStackItem(2)))
        .referenceOffset(callDataSegment.offset())
        .referenceSize(callDataSegment.length());
  }

  public static MmuSubFragment codeCopy(final Hub hub) {
    return new MmuSubFragment(MMU_INST_ANY_TO_RAM_WITH_PADDING)
        .sourceId(SortedCfiProvider.of(hub, hub.currentFrame().codeFragmentIndex()))
        .targetId(ConstantIntSupplier.of(hub.currentFrame().contextNumber()))
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(1)))
        .targetOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .size(Words.clampedToLong(hub.messageFrame().getStackItem(2)))
        .referenceSize(hub.currentFrame().code().getSize())
        .setRom();
  }

  public static MmuSubFragment extCodeCopy(final Hub hub, final Address sourceAddress) {
    return new MmuSubFragment(MMU_INST_ANY_TO_RAM_WITH_PADDING)
        .sourceId(SortedCfiProvider.ofLookup(hub, sourceAddress))
        .targetId(ConstantIntSupplier.of(hub.currentFrame().contextNumber()))
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(2)))
        .targetOffset(EWord.of(hub.messageFrame().getStackItem(1)))
        .size(Words.clampedToLong(hub.messageFrame().getStackItem(3)))
        .referenceSize(
            hub.romLex()
                .getChunkByMetadata(
                    sourceAddress,
                    hub.conflation().deploymentInfo().number(sourceAddress),
                    hub.conflation().deploymentInfo().isDeploying(sourceAddress))
                .map(c -> c.byteCode().size())
                .orElse(0))
        .setRom();
  }

  public static MmuSubFragment returnDataCopy(final Hub hub) {
    final MemorySpan returnDataSegment = hub.currentFrame().currentReturnDataSource();
    return new MmuSubFragment(MMU_INST_ANY_TO_RAM_WITH_PADDING)
        .sourceId(
            ConstantIntSupplier.of(
                hub.callStack().get(hub.currentFrame().currentReturner()).contextNumber()))
        .targetId(ConstantIntSupplier.of(hub.currentFrame().contextNumber()))
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(1)))
        .targetOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .size(Words.clampedToLong(hub.messageFrame().getStackItem(2)))
        .referenceOffset(returnDataSegment.offset())
        .referenceSize(returnDataSegment.length());
  }

  public static MmuSubFragment mload(final Hub hub) {
    final long offset = Words.clampedToLong(hub.messageFrame().getStackItem(0));
    final EWord loadedValue = EWord.of(hub.messageFrame().shadowReadMemory(offset, 32));
    return new MmuSubFragment(MMU_INST_MLOAD)
        .sourceId(ConstantIntSupplier.of(hub.currentFrame().contextNumber()))
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .limb1(loadedValue.hi())
        .limb2(loadedValue.lo());
  }

  public static MmuSubFragment mstore(final Hub hub) {
    final EWord storedValue = EWord.of(hub.messageFrame().getStackItem(1));
    return new MmuSubFragment(MMU_INST_MSTORE)
        .sourceId(ConstantIntSupplier.of(hub.currentFrame().contextNumber()))
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .limb1(storedValue.hi())
        .limb2(storedValue.lo());
  }

  public static MmuSubFragment mstore8(final Hub hub) {
    final EWord storedValue = EWord.of(hub.messageFrame().getStackItem(1));
    return new MmuSubFragment(MMU_INST_MSTORE8)
        .sourceId(ConstantIntSupplier.of(hub.currentFrame().contextNumber()))
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .limb1(storedValue.hi())
        .limb2(storedValue.lo());
  }

  public static MmuSubFragment log(final Hub hub) {
    return new MmuSubFragment(MMU_INST_RAM_TO_EXO_WITH_PADDING)
        .sourceId(ConstantIntSupplier.of(hub.currentFrame().contextNumber()))
        .targetId(ConstantIntSupplier.of(0)) // TODO: absolute log number
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .size(Words.clampedToLong(hub.messageFrame().getStackItem(1)))
        .referenceSize(Words.clampedToLong(hub.messageFrame().getStackItem(1)))
        .setLog();
  }

  public static MmuSubFragment create(final Hub hub) {
    return new MmuSubFragment(MMU_INST_RAM_TO_EXO_WITH_PADDING)
        .sourceId(ConstantIntSupplier.of(hub.currentFrame().contextNumber()))
        .targetId(SortedCfiProvider.of(hub, hub.romLex().nextCfiBeforeReordering()))
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(1)))
        .size(Words.clampedToLong(hub.messageFrame().getStackItem(2)))
        .referenceSize(Words.clampedToLong(hub.messageFrame().getStackItem(2)))
        .setRom();
  }

  public static MmuSubFragment returnFromDeployment(final Hub hub) {
    return new MmuSubFragment(MMU_INST_RAM_TO_EXO_WITH_PADDING)
        .sourceId(ConstantIntSupplier.of(hub.currentFrame().contextNumber()))
        .targetId(SortedCfiProvider.of(hub, hub.romLex().nextCfiBeforeReordering()))
        .auxId(hub.state().stamps().hashInfo())
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .size(Words.clampedToLong(hub.messageFrame().getStackItem(1)))
        .referenceSize(Words.clampedToLong(hub.messageFrame().getStackItem(1)))
        .setHash()
        .setRom();
  }

  public static MmuSubFragment returnFromCall(final Hub hub) {
    return MmuSubFragment.revert(hub);
  }

  public static MmuSubFragment create2(final Hub hub) {
    return new MmuSubFragment(MMU_INST_RAM_TO_EXO_WITH_PADDING)
        .sourceId(ConstantIntSupplier.of(hub.currentFrame().contextNumber()))
        .targetId(SortedCfiProvider.of(hub, hub.romLex().nextCfiBeforeReordering()))
        .auxId(hub.state().stamps().hashInfo())
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(1)))
        .size(Words.clampedToLong(hub.messageFrame().getStackItem(2)))
        .referenceSize(Words.clampedToLong(hub.messageFrame().getStackItem(2)))
        .setHash()
        .setRom();
  }

  public static MmuSubFragment revert(final Hub hub) {
    return new MmuSubFragment(MMU_INST_RAM_TO_EXO_WITH_PADDING)
        .sourceId(ConstantIntSupplier.of(hub.currentFrame().contextNumber()))
        .targetId(
            ConstantIntSupplier.of(
                hub.callStack().get(hub.currentFrame().parentFrame()).contextNumber()))
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(0)))
        .size(Words.clampedToLong(hub.messageFrame().getStackItem(1)))
        .referenceOffset(hub.currentFrame().returnDataTarget().offset())
        .referenceSize(hub.currentFrame().returnDataTarget().length());
  }

  public static MmuSubFragment txInit(final Hub hub) {
    return new MmuSubFragment(MMU_INST_EXO_TO_RAM_TRANSPLANTS)
        .sourceId(ConstantIntSupplier.of(hub.tx().number()))
        .targetId(ConstantIntSupplier.of(hub.stamp()))
        .size(hub.tx().transaction().getData().map(Bytes::size).orElse(0))
        .phase(PHASE_TRANSACTION_CALL_DATA)
        .setRlpTxn();
  }

  public static MmuSubFragment forEcRecover(
      final Hub hub, PrecompileInvocation p, boolean recoverySuccessful, int i) {
    Preconditions.checkArgument(i >= 0 && i < 3);

    if (i == 0) {
      return new MmuSubFragment(MMU_INST_RAM_TO_EXO_WITH_PADDING)
          .sourceId(ConstantIntSupplier.of(hub.currentFrame().contextNumber()))
          .targetId(ConstantIntSupplier.of(hub.stamp() + 1))
          .sourceOffset(EWord.of(p.callDataSource().offset()))
          .size(p.callDataSource().length())
          .referenceSize(128)
          .successBit(recoverySuccessful)
          .phase(PHASE_ECRECOVER_DATA)
          .setEcData();
    } else if (i == 1) {
      if (recoverySuccessful) {
        return new MmuSubFragment(MMU_INST_EXO_TO_RAM_TRANSPLANTS)
            .sourceId(ConstantIntSupplier.of(hub.stamp() + 1))
            .targetId(ConstantIntSupplier.of(hub.stamp() + 1))
            .size(32)
            .phase(PHASE_ECRECOVER_RESULT)
            .setEcData();
      } else {
        return nop();
      }
    } else {
      if (recoverySuccessful && !p.requestedReturnDataTarget().isEmpty()) {
        return new MmuSubFragment(MMU_INST_RAM_TO_RAM_SANS_PADDING)
            .sourceId(ConstantIntSupplier.of(hub.stamp() + 1))
            .targetId(ConstantIntSupplier.of(hub.currentFrame().contextNumber()))
            .sourceOffset(EWord.ZERO)
            .size(32)
            .referenceOffset(p.requestedReturnDataTarget().offset())
            .referenceSize(p.requestedReturnDataTarget().length());

      } else {
        return nop();
      }
    }
  }

  private static MmuSubFragment forRipeMd160Sha(
      final Hub hub, PrecompileInvocation p, int i, Bytes emptyHi, Bytes emptyLo) {
    Preconditions.checkArgument(i >= 0 && i < 3);

    if (i == 0) {
      if (p.callDataSource().isEmpty()) {
        return nop();
      } else {
        return new MmuSubFragment(MMU_INST_RAM_TO_EXO_WITH_PADDING)
            .sourceId(ConstantIntSupplier.of(hub.currentFrame().contextNumber()))
            .targetId(ConstantIntSupplier.of(hub.stamp() + 1))
            .sourceOffset(EWord.of(p.callDataSource().offset()))
            .size(p.callDataSource().length())
            .referenceSize(p.callDataSource().length())
            .phase(PHASE_SHA2_256_DATA)
            .setRipSha();
      }
    } else if (i == 1) {
      if (p.callDataSource().isEmpty()) {
        return new MmuSubFragment(MMU_INST_MSTORE)
            .targetId(ConstantIntSupplier.of(hub.stamp() + 1))
            .targetOffset(EWord.ZERO)
            .limb1(emptyHi)
            .limb2(emptyLo);
      } else {
        return new MmuSubFragment(MMU_INST_EXO_TO_RAM_TRANSPLANTS)
            .sourceId(ConstantIntSupplier.of(hub.stamp() + 1))
            .targetId(ConstantIntSupplier.of(hub.stamp() + 1))
            .size(32)
            .phase(PHASE_SHA2_256_RESULT)
            .setRipSha();
      }
    } else {
      if (p.requestedReturnDataTarget().isEmpty()) {
        return nop();
      } else {
        return new MmuSubFragment(MMU_INST_RAM_TO_RAM_SANS_PADDING)
            .sourceId(ConstantIntSupplier.of(hub.stamp() + 1))
            .targetId(ConstantIntSupplier.of(hub.currentFrame().contextNumber()))
            .sourceOffset(EWord.ZERO)
            .size(32)
            .referenceOffset(p.requestedReturnDataTarget().offset())
            .referenceSize(p.requestedReturnDataTarget().length());
      }
    }
  }

  public static MmuSubFragment forSha2(final Hub hub, PrecompileInvocation p, int i) {
    return forRipeMd160Sha(
        hub,
        p,
        i,
        Bytes.fromHexString("e3b0c44298fc1c149afbf4c8996fb924"),
        Bytes.fromHexString("27ae41e4649b934ca495991b7852b855")); // SHA2-256({}) hi/lo
  }

  public static MmuSubFragment forRipeMd160(final Hub hub, PrecompileInvocation p, int i) {
    return forRipeMd160Sha(
        hub,
        p,
        i,
        Bytes.fromHexString("9c1185a5"),
        Bytes.fromHexString("c5e9fc54612808977ee8f548b2258d31")); // RIPEMD160({}) hi/lo
  }

  public static MmuSubFragment forIdentity(final Hub hub, PrecompileInvocation p, int i) {
    Preconditions.checkArgument(i >= 0 && i < 2);

    if (p.callDataSource().isEmpty()) {
      return nop();
    }

    if (i == 0) {
      return new MmuSubFragment(MMU_INST_RAM_TO_RAM_SANS_PADDING)
          .sourceId(ConstantIntSupplier.of(hub.currentFrame().contextNumber()))
          .targetId(ConstantIntSupplier.of(hub.stamp() + 1))
          .sourceOffset(EWord.of(p.callDataSource().offset()))
          .size(p.callDataSource().length())
          .referenceOffset(0)
          .referenceSize(p.callDataSource().length());
    } else {
      if (p.requestedReturnDataTarget().isEmpty()) {
        return nop();
      } else {
        return new MmuSubFragment(MMU_INST_RAM_TO_RAM_SANS_PADDING)
            .sourceId(ConstantIntSupplier.of(hub.stamp() + 1))
            .targetId(ConstantIntSupplier.of(hub.currentFrame().contextNumber()))
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
        .pMiscellaneousMmuFlag(true)
        .pMiscellaneousMmuInst(Bytes.ofUnsignedInt(this.instruction))
        .pMiscellaneousMmuTgtId(Bytes.ofUnsignedInt(this.sourceId.getAsInt()))
        .pMiscellaneousMmuSrcId(Bytes.ofUnsignedInt(this.targetId.getAsInt()))
        .pMiscellaneousMmuAuxId(Bytes.ofUnsignedInt(this.auxId))
        .pMiscellaneousMmuSrcOffsetHi(this.sourceOffset.hi())
        .pMiscellaneousMmuSrcOffsetLo(this.sourceOffset.lo())
        .pMiscellaneousMmuTgtOffsetLo(this.targetOffset.lo())
        .pMiscellaneousMmuSize(Bytes.ofUnsignedLong(this.size))
        .pMiscellaneousMmuRefOffset(Bytes.ofUnsignedLong(this.referenceOffset))
        .pMiscellaneousMmuRefSize(Bytes.ofUnsignedLong(this.referenceSize))
        .pMiscellaneousMmuSuccessBit(this.successBit)
        .pMiscellaneousMmuLimb1(this.limb1)
        .pMiscellaneousMmuLimb2(this.limb2)
        .pMiscellaneousMmuExoSum(Bytes.ofUnsignedLong(this.exoSum))
        .pMiscellaneousMmuPhase(Bytes.ofUnsignedLong(this.phase));
  }
}
