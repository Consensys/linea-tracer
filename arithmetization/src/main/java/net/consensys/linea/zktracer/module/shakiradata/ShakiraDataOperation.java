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
  private static final int LIMB_INT_BYTE_SIZE = 16;
  private static final int TOTAL_SIZE_RESULT_PHASES = 32;
  private static final int TOTAL_SIZE_EXTRA_PHASES = 0;
  public static final int RESULT_ROWS_COUNT = 2;
  public static final int EXTRA_ROWS_COUNT = 3;

  @EqualsAndHashCode.Include private final int hubStamp;
  @Getter private int prevHubStamp;

  @EqualsAndHashCode.Include private final ShakiraPrecompileType precompileType;
  @EqualsAndHashCode.Include private final Bytes precompileInput;

  public ShakiraDataOperation(
      int hubStamp, int prevHubStamp, ShakiraPrecompileType precompileType, Bytes precompileInput) {
    this.hubStamp = hubStamp;
    this.prevHubStamp = prevHubStamp;
    this.precompileType = precompileType;
    this.precompileInput = precompileInput;
  }

  @Override
  protected int computeLineCount() {
    //    if (sha2Components.isPresent()) {
    //      return SHA2_COMPONENTS_LINE_COUNT;
    //    }
    //
    //    if (ripeMdComponents.isPresent()) {
    //      return RIPEMD_COMPONENTS_LINE_COUNT;
    //    }
    //
    //    return KECCAK_COMPONENTS_LINE_COUNT;
    return 0;
  }

  void trace(Trace trace, int stamp) {
    final int currentHubStamp = this.hubStamp + 1;

    var tracerBuilder =
        ShakiraTraceHelper.builder()
            .trace(trace)
            .currentHubStamp(currentHubStamp)
            .prevHubStamp(prevHubStamp)
            .stamp(stamp);

    switch (precompileType) {
      case SHA256 -> buildSha2Trace(trace, tracerBuilder);
      case KECCAK -> buildKeccakTrace(trace, tracerBuilder);
      case RIPEMD -> buildRipeMdTrace(trace, tracerBuilder);
    }
  }

  private void buildRipeMdTrace(
      Trace trace, ShakiraTraceHelper.ShakiraTraceHelperBuilder tracerBuilder) {
    final Map<Integer, PhaseInfo> phaseInfoMap = getPhaseInfoMap(hubStamp, precompileInput);
    final Bytes result = computeRipeMdResult(precompileInput);

    final ShakiraTraceHelper ripeMdTraceHelper =
        tracerBuilder
            .phaseInfoMap(phaseInfoMap)
            .startPhaseIndex(Trace.PHASE_RIPEMD_DATA)
            .endPhaseIndex(Trace.PHASE_RIPEMD_RESULT)
            .traceLimbConsumer(
                traceLimbConsumer(
                    trace,
                    Trace.PHASE_RIPEMD_DATA,
                    Trace.PHASE_RIPEMD_RESULT,
                    precompileInput,
                    result))
            .build();

    final ShakiraTraceHelper extraTraceHelper =
        tracerBuilder.startPhaseIndex(PHASE_EXTRA).endPhaseIndex(PHASE_EXTRA).build();

    ripeMdTraceHelper.trace();
    extraTraceHelper.trace();

    prevHubStamp = ripeMdTraceHelper.prevHubStamp();
  }

  private void buildKeccakTrace(
      Trace trace, ShakiraTraceHelper.ShakiraTraceHelperBuilder tracerBuilder) {
    final Map<Integer, PhaseInfo> phaseInfoMap = getPhaseInfoMap(hubStamp, precompileInput);
    final Bytes result = computeKeccakResult(precompileInput);

    final ShakiraTraceHelper keccakTraceHelper =
        tracerBuilder
            .phaseInfoMap(phaseInfoMap)
            .startPhaseIndex(Trace.PHASE_KECCAK_DATA)
            .endPhaseIndex(Trace.PHASE_KECCAK_RESULT)
            .traceLimbConsumer(
                traceLimbConsumer(
                    trace,
                    Trace.PHASE_KECCAK_DATA,
                    Trace.PHASE_KECCAK_RESULT,
                    precompileInput,
                    result))
            .build();

    final ShakiraTraceHelper extraTraceHelper =
        tracerBuilder.startPhaseIndex(PHASE_EXTRA).endPhaseIndex(PHASE_EXTRA).build();

    keccakTraceHelper.trace();
    extraTraceHelper.trace();

    prevHubStamp = keccakTraceHelper.prevHubStamp();
  }

  private void buildSha2Trace(
      Trace trace, ShakiraTraceHelper.ShakiraTraceHelperBuilder tracerBuilder) {
    final Map<Integer, PhaseInfo> phaseInfoMap = getPhaseInfoMap(hubStamp, precompileInput);
    final Bytes result = computeSha256Result(precompileInput);

    final ShakiraTraceHelper sha2TraceHelper =
        tracerBuilder
            .phaseInfoMap(phaseInfoMap)
            .startPhaseIndex(Trace.PHASE_SHA2_DATA)
            .endPhaseIndex(Trace.PHASE_SHA2_RESULT)
            .traceLimbConsumer(
                traceLimbConsumer(
                    trace, Trace.PHASE_SHA2_DATA, Trace.PHASE_SHA2_RESULT, precompileInput, result))
            .build();

    final ShakiraTraceHelper extraTraceHelper =
        tracerBuilder.startPhaseIndex(PHASE_EXTRA).endPhaseIndex(PHASE_EXTRA).build();

    sha2TraceHelper.trace();
    extraTraceHelper.trace();

    prevHubStamp = sha2TraceHelper.prevHubStamp();
  }

  private static BiConsumer<Integer, Integer> traceLimbConsumer(
      Trace trace, int dataPhase, int resultPhase, Bytes inputData, Bytes computedResult) {
    return (rowIndex, phaseIndex) -> {
      if (phaseIndex == dataPhase) {
        final Bytes limbRowData = extractLimbRowData(inputData, rowIndex);
        trace.limb(limbRowData);

        final int nBytesAcc = Math.min((rowIndex + 1) * LIMB_INT_BYTE_SIZE, inputData.size());
        final int nBytes = nBytesAcc - (rowIndex * LIMB_INT_BYTE_SIZE);
        trace.nBytes(UnsignedByte.of(nBytes)).nBytesAcc(nBytesAcc);

      } else if (phaseIndex == resultPhase) {
        trace.limb(computedResult.slice(LIMB_INT_BYTE_SIZE * rowIndex, LIMB_INT_BYTE_SIZE));
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

  private static int totalNumberOfDataRows(Bytes inputData) {
    return (inputData.size() + LIMB_INT_BYTE_SIZE - 1) / LIMB_INT_BYTE_SIZE;
  }

  private static int totalNumberOfRows(Bytes inputData) {
    return totalNumberOfDataRows(inputData) + RESULT_ROWS_COUNT + EXTRA_ROWS_COUNT;
  }

  private Map<Integer, PhaseInfo> getPhaseInfoMap(int currentHubStamp, Bytes inputData) {
    final int indexMaxDataPhases = totalNumberOfDataRows(inputData) - 1;
    final int totalSizeDataPhases = inputData.size();
    final UnsignedByte[] deltaBytesDataPhases = getDataPhaseDeltaBytes(currentHubStamp, inputData);
    final UnsignedByte[] deltaBytesResultPhases =
        getResultPhaseDeltaBytes(currentHubStamp, inputData);
    final UnsignedByte[] deltaBytesExtraPhases =
        getExtraPhaseDeltaBytes(currentHubStamp, inputData);

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

  private static Bytes extractLimbRowData(Bytes inputData, Integer rowIndex) {
    final int inputDataSize = inputData.size();
    final int startLimbOffsetIndex = LIMB_INT_BYTE_SIZE * rowIndex;

    final boolean limbSizeIsLessThan16 = inputDataSize - startLimbOffsetIndex < LIMB_INT_BYTE_SIZE;

    return !limbSizeIsLessThan16
        ? inputData.slice(startLimbOffsetIndex, LIMB_INT_BYTE_SIZE)
        : rightPadTo(
            inputData.slice(startLimbOffsetIndex, inputDataSize - startLimbOffsetIndex),
            LIMB_INT_BYTE_SIZE);
  }

  private UnsignedByte[] getHubStampDiffBytes(int currentHubStamp) {
    final BigInteger prevHubStampBigInt = BigInteger.valueOf(prevHubStamp);
    final BigInteger hubStampBigInt = BigInteger.valueOf(currentHubStamp);
    final BigInteger hubStampDiff =
        hubStampBigInt.subtract(prevHubStampBigInt).subtract(BigInteger.ONE);

    Preconditions.checkArgument(
        hubStampDiff.compareTo(BigInteger.valueOf(256 ^ 4)) < 0,
        "Hub stamp difference should never exceed 256 ^ 4");

    return bytesToUnsignedBytes(leftPadTo(bigIntegerToBytes(hubStampDiff), 4).toArray());
  }

  private UnsignedByte[] getDataPhaseDeltaBytes(int currentHubStamp, Bytes inputData) {
    final int from = 0;
    final int to = totalNumberOfDataRows(inputData);

    return Arrays.copyOfRange(getDeltaBytes(currentHubStamp, inputData), from, to);
  }

  private UnsignedByte[] getResultPhaseDeltaBytes(int currentHubStamp, Bytes inputData) {
    final int from = totalNumberOfDataRows(inputData);
    final int to = from + RESULT_ROWS_COUNT;

    return Arrays.copyOfRange(getDeltaBytes(currentHubStamp, inputData), from, to);
  }

  private UnsignedByte[] getExtraPhaseDeltaBytes(int currentHubStamp, Bytes inputData) {
    final int from = totalNumberOfDataRows(inputData) + RESULT_ROWS_COUNT;
    final int to = from + EXTRA_ROWS_COUNT;

    return Arrays.copyOfRange(getDeltaBytes(currentHubStamp, inputData), from, to);
  }

  private UnsignedByte[] getDeltaBytes(int currentHubStamp, Bytes inputData) {
    final UnsignedByte[] deltaBytes = new UnsignedByte[totalNumberOfRows(inputData)];
    Arrays.fill(deltaBytes, UnsignedByte.ZERO);

    final UnsignedByte[] hubStampDiffBytes = getHubStampDiffBytes(currentHubStamp);

    System.arraycopy(hubStampDiffBytes, 0, deltaBytes, 0, hubStampDiffBytes.length);

    final int remainder = inputData.size() % LIMB_INT_BYTE_SIZE;
    final int lastNBytes = remainder == 0 ? LIMB_INT_BYTE_SIZE : remainder;

    deltaBytes[deltaBytes.length - 2] = UnsignedByte.of(lastNBytes - 1);
    deltaBytes[deltaBytes.length - 1] = UnsignedByte.of(256 - LIMB_INT_BYTE_SIZE + lastNBytes - 1);

    return deltaBytes;
  }
}
