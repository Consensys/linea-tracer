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

package net.consensys.linea.zktracer.module.shakiradata;

import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;
import static net.consensys.linea.zktracer.types.Conversions.bytesToUnsignedBytes;
import static net.consensys.linea.zktracer.types.Utils.leftPadTo;
import static net.consensys.linea.zktracer.types.Utils.rightPadTo;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;
import java.util.function.BiConsumer;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.Hash;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Accessors(fluent = true)
public class ShakiraDataOperation extends ModuleOperation {
  private static final int PHASE_EXTRA = 0;
  private static final int INDEX_MAX_EXTRA = 2;
  private static final int TOTAL_SIZE_RESULT_PHASES = 32;
  private static final int TOTAL_SIZE_EXTRA_PHASES = 0;
  public static final int RESULT_ROWS_COUNT = 2;
  public static final int EXTRA_ROWS_COUNT = 3;

  @Getter @EqualsAndHashCode.Include private final int hubStamp;
  @Getter @Setter private int prevId;
  @Getter @Setter private int currentId;

  @EqualsAndHashCode.Include private final ShakiraPrecompileType precompileType;
  @EqualsAndHashCode.Include private final Bytes hashInput;

  public ShakiraDataOperation(int hubStamp, ShakiraPrecompileType precompileType, Bytes hashInput) {
    this.hubStamp = hubStamp;
    this.precompileType = precompileType;
    this.hashInput = hashInput;
  }

  @Override
  protected int computeLineCount() {
    return totalNumberOfRows();
  }

  void trace(Trace trace, int stamp) {
    var tracerBuilder = ShakiraTraceHelper.builder().trace(trace).currentId(currentId).stamp(stamp);

    switch (precompileType) {
      case SHA256 -> buildSha2Trace(trace, tracerBuilder);
      case KECCAK -> buildKeccakTrace(trace, tracerBuilder);
      case RIPEMD -> buildRipeMdTrace(trace, tracerBuilder);
    }

    prevId = currentId;
  }

  private void buildRipeMdTrace(
      Trace trace, ShakiraTraceHelper.ShakiraTraceHelperBuilder tracerBuilder) {
    final Map<Integer, PhaseInfo> phaseInfoMap = getPhaseInfoMap();
    final Bytes result = computeRipeMdResult(hashInput);

    final ShakiraTraceHelper ripeMdTraceHelper =
        tracerBuilder
            .phaseInfoMap(phaseInfoMap)
            .startPhaseIndex(Trace.PHASE_RIPEMD_DATA)
            .endPhaseIndex(Trace.PHASE_RIPEMD_RESULT)
            .traceLimbConsumer(
                traceLimbConsumer(
                    trace, Trace.PHASE_RIPEMD_DATA, Trace.PHASE_RIPEMD_RESULT, result))
            .build();

    final ShakiraTraceHelper extraTraceHelper =
        tracerBuilder.startPhaseIndex(PHASE_EXTRA).endPhaseIndex(PHASE_EXTRA).build();

    ripeMdTraceHelper.trace();
    extraTraceHelper.trace();
  }

  private void buildKeccakTrace(
      Trace trace, ShakiraTraceHelper.ShakiraTraceHelperBuilder tracerBuilder) {
    final Map<Integer, PhaseInfo> phaseInfoMap = getPhaseInfoMap();
    final Bytes result = computeKeccakResult(hashInput);

    final ShakiraTraceHelper keccakTraceHelper =
        tracerBuilder
            .phaseInfoMap(phaseInfoMap)
            .startPhaseIndex(Trace.PHASE_KECCAK_DATA)
            .endPhaseIndex(Trace.PHASE_KECCAK_RESULT)
            .traceLimbConsumer(
                traceLimbConsumer(
                    trace, Trace.PHASE_KECCAK_DATA, Trace.PHASE_KECCAK_RESULT, result))
            .build();

    final ShakiraTraceHelper extraTraceHelper =
        tracerBuilder.startPhaseIndex(PHASE_EXTRA).endPhaseIndex(PHASE_EXTRA).build();

    keccakTraceHelper.trace();
    extraTraceHelper.trace();
  }

  private void buildSha2Trace(
      Trace trace, ShakiraTraceHelper.ShakiraTraceHelperBuilder tracerBuilder) {
    final Map<Integer, PhaseInfo> phaseInfoMap = getPhaseInfoMap();
    final Bytes result = computeSha256Result(hashInput);

    final ShakiraTraceHelper sha2TraceHelper =
        tracerBuilder
            .phaseInfoMap(phaseInfoMap)
            .startPhaseIndex(Trace.PHASE_SHA2_DATA)
            .endPhaseIndex(Trace.PHASE_SHA2_RESULT)
            .traceLimbConsumer(
                traceLimbConsumer(trace, Trace.PHASE_SHA2_DATA, Trace.PHASE_SHA2_RESULT, result))
            .build();

    final ShakiraTraceHelper extraTraceHelper =
        tracerBuilder.startPhaseIndex(PHASE_EXTRA).endPhaseIndex(PHASE_EXTRA).build();

    sha2TraceHelper.trace();
    extraTraceHelper.trace();
  }

  private BiConsumer<Integer, Integer> traceLimbConsumer(
      Trace trace, int dataPhase, int resultPhase, Bytes computedResult) {
    return (rowIndex, phaseIndex) -> {
      if (phaseIndex == dataPhase) {
        final Bytes limbRowData = extractLimbRowData(rowIndex);
        trace.limb(limbRowData);

        final int nBytesAcc = Math.min((rowIndex + 1) * Trace.LLARGE, hashInput.size());
        final int nBytes = nBytesAcc - (rowIndex * Trace.LLARGE);
        trace.nBytes(UnsignedByte.of(nBytes)).nBytesAcc(nBytesAcc);

      } else if (phaseIndex == resultPhase) {
        trace.limb(computedResult.slice(Trace.LLARGE * rowIndex, Trace.LLARGE));
        trace.nBytes(UnsignedByte.ZERO).nBytesAcc(0L);
      }
    };
  }

  private static Bytes computeSha256Result(Bytes input) {
    return Hash.sha256(input);
  }

  private static Bytes computeKeccakResult(Bytes input) {
    return Hash.keccak256(input);
  }

  private static Bytes computeRipeMdResult(Bytes input) {
    return leftPadTo(Hash.ripemd160(input), 32);
  }

  private int totalNumberOfDataRows() {
    return (hashInput.size() + Trace.LLARGEMO) / Trace.LLARGE;
  }

  private int totalNumberOfRows() {
    return totalNumberOfDataRows() + RESULT_ROWS_COUNT + EXTRA_ROWS_COUNT;
  }

  private Map<Integer, PhaseInfo> getPhaseInfoMap() {
    final int indexMaxDataPhases = totalNumberOfDataRows() - 1;
    final int totalSizeDataPhases = hashInput.size();
    final UnsignedByte[] deltaBytesDataPhases = getDataPhaseDeltaBytes();
    final UnsignedByte[] deltaBytesResultPhases = getResultPhaseDeltaBytes();
    final UnsignedByte[] deltaBytesExtraPhases = getExtraPhaseDeltaBytes();

    return Map.of(
        Trace.PHASE_SHA2_DATA,
        new PhaseInfo(
            Trace.PHASE_SHA2_DATA, indexMaxDataPhases, totalSizeDataPhases, deltaBytesDataPhases),
        Trace.PHASE_SHA2_RESULT,
        new PhaseInfo(
            Trace.PHASE_SHA2_RESULT,
            Trace.INDEX_MAX_SHA2_RESULT,
            TOTAL_SIZE_RESULT_PHASES,
            deltaBytesResultPhases),
        Trace.PHASE_RIPEMD_DATA,
        new PhaseInfo(
            Trace.PHASE_RIPEMD_DATA, indexMaxDataPhases, totalSizeDataPhases, deltaBytesDataPhases),
        Trace.PHASE_RIPEMD_RESULT,
        new PhaseInfo(
            Trace.PHASE_RIPEMD_RESULT,
            Trace.INDEX_MAX_RIPEMD_RESULT,
            TOTAL_SIZE_RESULT_PHASES,
            deltaBytesResultPhases),
        Trace.PHASE_KECCAK_DATA,
        new PhaseInfo(
            Trace.PHASE_KECCAK_DATA, indexMaxDataPhases, totalSizeDataPhases, deltaBytesDataPhases),
        Trace.PHASE_KECCAK_RESULT,
        new PhaseInfo(
            Trace.PHASE_KECCAK_RESULT,
            Trace.INDEX_MAX_KECCAK_RESULT,
            TOTAL_SIZE_RESULT_PHASES,
            deltaBytesResultPhases),
        PHASE_EXTRA,
        new PhaseInfo(
            PHASE_EXTRA, INDEX_MAX_EXTRA, TOTAL_SIZE_EXTRA_PHASES, deltaBytesExtraPhases));
  }

  private Bytes extractLimbRowData(Integer rowIndex) {
    final int inputDataSize = hashInput.size();
    final int startLimbOffsetIndex = Trace.LLARGE * rowIndex;

    final boolean limbSizeIsLessThan16 = inputDataSize - startLimbOffsetIndex < Trace.LLARGE;

    return !limbSizeIsLessThan16
        ? hashInput.slice(startLimbOffsetIndex, Trace.LLARGE)
        : rightPadTo(
            hashInput.slice(startLimbOffsetIndex, inputDataSize - startLimbOffsetIndex),
            Trace.LLARGE);
  }

  private UnsignedByte[] getHubStampDiffBytes(int currentHubStamp) {
    final BigInteger prevHubStampBigInt = BigInteger.valueOf(prevId);
    final BigInteger hubStampBigInt = BigInteger.valueOf(currentHubStamp);
    final BigInteger hubStampDiff =
        hubStampBigInt.subtract(prevHubStampBigInt).subtract(BigInteger.ONE);

    Preconditions.checkArgument(
        hubStampDiff.compareTo(BigInteger.valueOf((long) Math.pow(256, 4))) < 0,
        "Hub stamp difference should never exceed 256 ^ 4");

    return bytesToUnsignedBytes(leftPadTo(bigIntegerToBytes(hubStampDiff), 4).toArray());
  }

  private UnsignedByte[] getDataPhaseDeltaBytes() {
    final int from = 0;
    final int to = totalNumberOfDataRows();

    return Arrays.copyOfRange(getDeltaBytes(), from, to);
  }

  private UnsignedByte[] getResultPhaseDeltaBytes() {
    final int from = totalNumberOfDataRows();
    final int to = from + RESULT_ROWS_COUNT;

    return Arrays.copyOfRange(getDeltaBytes(), from, to);
  }

  private UnsignedByte[] getExtraPhaseDeltaBytes() {
    final int from = totalNumberOfDataRows() + RESULT_ROWS_COUNT;
    final int to = from + EXTRA_ROWS_COUNT;

    return Arrays.copyOfRange(getDeltaBytes(), from, to);
  }

  private UnsignedByte[] getDeltaBytes() {
    final UnsignedByte[] deltaBytes = new UnsignedByte[totalNumberOfRows()];
    Arrays.fill(deltaBytes, UnsignedByte.ZERO);

    final UnsignedByte[] hubStampDiffBytes = getHubStampDiffBytes(currentId);

    System.arraycopy(hubStampDiffBytes, 0, deltaBytes, 0, hubStampDiffBytes.length);

    final int remainder = hashInput.size() % Trace.LLARGE;
    final int lastNBytes = remainder == 0 ? Trace.LLARGE : remainder;

    deltaBytes[deltaBytes.length - 2] = UnsignedByte.of(lastNBytes - 1);
    deltaBytes[deltaBytes.length - 1] = UnsignedByte.of(256 - Trace.LLARGE + lastNBytes - 1);

    return deltaBytes;
  }
}
