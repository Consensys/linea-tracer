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
import java.util.Map;
import java.util.Optional;

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
  private static final int DEFAULT_EXTRA_LIMB_SIZE = -1;

  private static final int SHA2_DATA_SIZE = Trace.INDEX_MAX_SHA2_DATA + 1;
  private static final int SHA2_RESULT_SIZE = Trace.INDEX_MAX_SHA2_RESULT + 1;
  private static final int SHA2_COMPONENTS_LINE_COUNT = SHA2_DATA_SIZE + SHA2_RESULT_SIZE;
  private static final int SHA2_LIMB_SIZE = 179;
  private static final int SHA2_LIMB_INT_BYTE_SIZE = 16;

  private static final int RIPEMD_DATA_SIZE = Trace.INDEX_MAX_RIPEMD_DATA + 1;
  private static final int RIPEMD_RESULT_SIZE = Trace.INDEX_MAX_RIPEMD_RESULT + 1;
  private static final int RIPEMD_COMPONENTS_LINE_COUNT = RIPEMD_DATA_SIZE + RIPEMD_RESULT_SIZE;
  private static final int RIPEMD_LIMB_SIZE = 64;
  private static final int RIPEMD_LIMB_INT_BYTE_SIZE = 16;

  private static final int KECCAK_DATA_SIZE = Trace.INDEX_MAX_KECCAK_DATA + 1;
  private static final Bytes KECCAK_DATA_LIMB = Bytes.fromHexString("0xDEADBEEF");
  private static final int KECCAK_RESULT_SIZE = Trace.INDEX_MAX_KECCAK_RESULT + 1;
  private static final int KECCAK_COMPONENTS_LINE_COUNT = KECCAK_DATA_SIZE + KECCAK_RESULT_SIZE;
  private static final int KECCAK_LIMB_SIZE = 7;

  private static final Map<Integer, PhaseInfo> PHASE_INFO_MAP =
      Map.of(
          Trace.PHASE_SHA2_DATA,
          new PhaseInfo(Trace.PHASE_SHA2_DATA, Trace.INDEX_MAX_SHA2_DATA, SHA2_LIMB_SIZE),
          Trace.PHASE_SHA2_RESULT,
          new PhaseInfo(Trace.PHASE_SHA2_RESULT, Trace.INDEX_MAX_SHA2_RESULT, SHA2_LIMB_SIZE),
          Trace.PHASE_RIPEMD_DATA,
          new PhaseInfo(Trace.PHASE_RIPEMD_DATA, Trace.INDEX_MAX_RIPEMD_DATA, RIPEMD_LIMB_SIZE),
          Trace.PHASE_RIPEMD_RESULT,
          new PhaseInfo(Trace.PHASE_RIPEMD_RESULT, Trace.INDEX_MAX_RIPEMD_RESULT, RIPEMD_LIMB_SIZE),
          Trace.PHASE_KECCAK_DATA,
          new PhaseInfo(Trace.PHASE_KECCAK_DATA, Trace.INDEX_MAX_KECCAK_DATA, KECCAK_LIMB_SIZE),
          Trace.PHASE_KECCAK_RESULT,
          new PhaseInfo(Trace.PHASE_KECCAK_RESULT, Trace.INDEX_MAX_KECCAK_RESULT, KECCAK_LIMB_SIZE),
          PHASE_EXTRA,
          new PhaseInfo(PHASE_EXTRA, INDEX_MAX_EXTRA, DEFAULT_EXTRA_LIMB_SIZE));

  @EqualsAndHashCode.Include private final int hubStamp;
  @Getter private int prevHubStamp;

  @EqualsAndHashCode.Include private final Optional<Sha2Components> sha2Components;
  @EqualsAndHashCode.Include private final Optional<RipeMdComponents> ripeMdComponents;
  @EqualsAndHashCode.Include private final Optional<KeccakComponents> keccakComponents;

  public ShakiraDataOperation(
      int hubStamp,
      int prevHubStamp,
      Sha2Components modexpComponents,
      RipeMdComponents blake2fComponents,
      KeccakComponents keccakComponents) {
    this.hubStamp = hubStamp;
    this.prevHubStamp = prevHubStamp;
    this.sha2Components = Optional.ofNullable(modexpComponents);
    this.ripeMdComponents = Optional.ofNullable(blake2fComponents);
    this.keccakComponents = Optional.ofNullable(keccakComponents);
  }

  @Override
  protected int computeLineCount() {
    if (sha2Components.isPresent()) {
      return SHA2_COMPONENTS_LINE_COUNT;
    }

    if (ripeMdComponents.isPresent()) {
      return RIPEMD_COMPONENTS_LINE_COUNT;
    }

    return KECCAK_COMPONENTS_LINE_COUNT;
  }

  void trace(Trace trace, int stamp) {
    final int currentHubStamp = this.hubStamp + 1;

    final UnsignedByte[] hubStampDiffBytes = getHubStampDiffBytes(currentHubStamp);

    var tracerBuilder =
        ShakiraTraceHelper.builder()
            .trace(trace)
            .currentHubStamp(currentHubStamp)
            .prevHubStamp(prevHubStamp)
            .phaseInfoMap(PHASE_INFO_MAP)
            .hubStampDiffBytes(hubStampDiffBytes)
            .stamp(stamp);

    sha2Components.ifPresent(
        components -> {
          buildSha2Trace(trace, tracerBuilder, components);
          // TODO: add extra data trace.
        });

    ripeMdComponents.ifPresent(
        components -> {
          buildRipeMdTrace(trace, tracerBuilder, components);
          // TODO: add extra data trace.
        });

    keccakComponents.ifPresent(
        components -> {
          buildKeccakTrace(trace, tracerBuilder, components);
          // TODO: add extra data trace.
        });
  }

  private void buildRipeMdTrace(
      Trace trace,
      ShakiraTraceHelper.ShakiraTraceHelperBuilder tracerBuilder,
      RipeMdComponents components) {
    final Bytes ripeMdResult = computeRipeMdResult(components.data());

    ShakiraTraceHelper ripeMdTraceHelper =
        tracerBuilder
            .startPhaseIndex(Trace.PHASE_RIPEMD_DATA)
            .endPhaseIndex(Trace.PHASE_RIPEMD_RESULT)
            .currentRowIndexFunction(((phaseInfo, phaseIndex, index) -> index))
            .traceLimbConsumer(
                (rowIndex, phaseIndex) -> {
                  if (phaseIndex == Trace.PHASE_RIPEMD_DATA) {
                    trace.limb(
                        components
                            .data()
                            .slice(
                                RIPEMD_LIMB_INT_BYTE_SIZE * rowIndex, RIPEMD_LIMB_INT_BYTE_SIZE));

                    final UnsignedByte nBytes = UnsignedByte.of(RIPEMD_LIMB_INT_BYTE_SIZE);

                    final int nBytesAcc =
                        rowIndex == 0
                            ? RIPEMD_LIMB_INT_BYTE_SIZE
                            : (rowIndex + 1) * RIPEMD_LIMB_INT_BYTE_SIZE;

                    trace.nBytes(nBytes).nBytesAcc(nBytesAcc);
                  } else if (phaseIndex == Trace.PHASE_RIPEMD_RESULT) {
                    trace.limb(
                        ripeMdResult.slice(
                            RIPEMD_LIMB_INT_BYTE_SIZE * rowIndex, RIPEMD_LIMB_INT_BYTE_SIZE));
                  }
                })
            .build();

    ripeMdTraceHelper.trace();

    prevHubStamp = ripeMdTraceHelper.prevHubStamp();
  }

  private void buildKeccakTrace(
      Trace trace,
      ShakiraTraceHelper.ShakiraTraceHelperBuilder tracerBuilder,
      KeccakComponents components) {
    final Bytes keccakResult = computeKeccakResult(components.data());

    ShakiraTraceHelper keccakTraceHelper =
        tracerBuilder
            .startPhaseIndex(Trace.PHASE_KECCAK_DATA)
            .endPhaseIndex(Trace.PHASE_KECCAK_RESULT)
            .currentRowIndexFunction(((phaseInfo, phaseIndex, index) -> index))
            .traceLimbConsumer(
                (rowIndex, phaseIndex) -> {
                  if (phaseIndex == Trace.PHASE_KECCAK_DATA) {
                    trace
                        .limb(KECCAK_DATA_LIMB)
                        .nBytes(UnsignedByte.of(KECCAK_DATA_SIZE))
                        .nBytesAcc(KECCAK_DATA_SIZE);
                  } else if (phaseIndex == Trace.PHASE_KECCAK_RESULT) {
                    trace.limb(keccakResult);
                  }
                })
            .build();

    keccakTraceHelper.trace();

    prevHubStamp = keccakTraceHelper.prevHubStamp();
  }

  private void buildSha2Trace(
      Trace trace,
      ShakiraTraceHelper.ShakiraTraceHelperBuilder tracerBuilder,
      Sha2Components components) {
    final Bytes sha256Result = computeSha256Result(components.data());

    ShakiraTraceHelper sha2TraceHelper =
        tracerBuilder
            .startPhaseIndex(Trace.PHASE_SHA2_DATA)
            .endPhaseIndex(Trace.PHASE_SHA2_RESULT)
            .currentRowIndexFunction(((phaseInfo, phaseIndex, index) -> index))
            .traceLimbConsumer(
                (rowIndex, phaseIndex) -> {
                  if (phaseIndex == Trace.PHASE_SHA2_DATA) {
                    trace.limb(
                        components
                            .data()
                            .slice(SHA2_LIMB_INT_BYTE_SIZE * rowIndex, SHA2_LIMB_INT_BYTE_SIZE));

                    int nBytes = SHA2_LIMB_INT_BYTE_SIZE;

                    int nBytesAcc =
                        rowIndex == 0
                            ? SHA2_LIMB_INT_BYTE_SIZE
                            : (rowIndex + 1) * SHA2_LIMB_INT_BYTE_SIZE;

                    if (nBytesAcc > SHA2_LIMB_SIZE) {
                      nBytes = nBytesAcc - SHA2_LIMB_SIZE;
                      nBytesAcc = SHA2_LIMB_SIZE;
                    }

                    trace.nBytes(UnsignedByte.of(nBytes)).nBytesAcc(nBytesAcc);
                  } else if (phaseIndex == Trace.PHASE_SHA2_RESULT) {
                    trace.limb(
                        sha256Result.slice(
                            SHA2_LIMB_INT_BYTE_SIZE * rowIndex, SHA2_LIMB_INT_BYTE_SIZE));
                  }
                })
            .build();

    sha2TraceHelper.trace();

    prevHubStamp = sha2TraceHelper.prevHubStamp();
  }

  private Bytes computeSha256Result(Bytes input) {
    return Hash.sha256(input);
  }

  private Bytes computeKeccakResult(Bytes input) {
    return Hash.keccak256(input);
  }

  private Bytes computeRipeMdResult(Bytes input) {
    return Hash.ripemd160(input);
  }

  private UnsignedByte[] getHubStampDiffBytes(int currentHubStamp) {
    BigInteger prevHubStampBigInt = BigInteger.valueOf(prevHubStamp);
    BigInteger hubStampBigInt = BigInteger.valueOf(currentHubStamp);
    BigInteger hubStampDiff = hubStampBigInt.subtract(prevHubStampBigInt).subtract(BigInteger.ONE);

    Preconditions.checkArgument(
        hubStampDiff.compareTo(BigInteger.valueOf(256 ^ 6)) < 0,
        "Hub stamp difference should never exceed 256 ^ 6");

    return bytesToUnsignedBytes(
        rightPadTo(leftPadTo(bigIntegerToBytes(hubStampDiff), 6), 128).toArray());
  }
}
