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

package net.consensys.linea.zktracer.module.blake2fmodexpdata;

import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;
import static net.consensys.linea.zktracer.types.Conversions.bytesToUnsignedBytes;
import static net.consensys.linea.zktracer.types.Utils.leftPadTo;
import static net.consensys.linea.zktracer.types.Utils.rightPadTo;

import java.math.BigInteger;
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
public class Blake2fModexpDataOperation extends ModuleOperation {
  private static final int MODEXP_COMPONENT_INT_BYTE_SIZE = 32;
  private static final int MODEXP_COMPONENT_SIZE = 512;
  private static final int MODEXP_LIMB_INT_BYTE_SIZE = 16;
  private static final int BLAKE2f_DATA_SIZE = Trace.INDEX_MAX_BLAKE_DATA + 1;
  private static final int BLAKE2f_RESULT_SIZE = Trace.INDEX_MAX_BLAKE_RESULT + 1;
  private static final int BLAKE2f_LIMB_INT_BYTE_SIZE = 16;
  private static final int MODEXP_COMPONENTS_LINE_COUNT = 32 * 4;
  private static final int BLAKE2f_COMPONENTS_LINE_COUNT =
      BLAKE2f_DATA_SIZE + BLAKE2f_RESULT_SIZE + 2;

  private final int hubStamp;
  @Getter private int prevHubStamp;

  @EqualsAndHashCode.Include private final Optional<ModexpComponents> modexpComponents;
  @EqualsAndHashCode.Include private final Optional<Blake2fComponents> blake2fComponents;

  public Blake2fModexpDataOperation(
      int hubStamp,
      int prevHubStamp,
      ModexpComponents modexpComponents,
      Blake2fComponents blake2fComponents) {
    this.hubStamp = hubStamp;
    this.prevHubStamp = prevHubStamp;
    this.modexpComponents = Optional.ofNullable(modexpComponents);
    this.blake2fComponents = Optional.ofNullable(blake2fComponents);
  }

  @Override
  protected int computeLineCount() {
    return MODEXP_COMPONENTS_LINE_COUNT + BLAKE2f_COMPONENTS_LINE_COUNT;
  }

  void trace(Trace trace, int stamp) {
    final UnsignedByte stampBytes = UnsignedByte.of(stamp);
    final Bytes currentHubStamp = Bytes.ofUnsignedInt(this.hubStamp + 1);

    final UnsignedByte[] hubStampDiffBytes = getHubStampDiffBytes(currentHubStamp);

    final Bytes modexpComponentsLimb =
        modexpComponents.map(this::buildModexpComponentsLimb).orElse(Bytes.EMPTY);

    final Bytes blake2fResult =
        blake2fComponents.map(c -> computeBlake2fResult(c.rawInput())).orElse(Bytes.EMPTY);

    boolean[] phaseFlags = new boolean[7];

    for (int phaseIndex = 1; phaseIndex <= 7; phaseIndex++) {
      phaseFlags[phaseIndex - 1] = true;

      PhaseInfo phaseInfo = getPhaseInfo(phaseIndex);

      for (int index = 0; index < 32; index++) {
        int counter = 32 * (phaseIndex - 1) + index;

        trace
            .phase(UnsignedByte.of(phaseInfo.id()))
            .deltaByte(hubStampDiffBytes[counter])
            .id(currentHubStamp)
            .index(UnsignedByte.of(index))
            .indexMax(UnsignedByte.of(phaseInfo.indexMax()))
            .stamp(stampBytes);

        if (!modexpComponentsLimb.isEmpty()) {
          trace.limb(
              modexpComponentsLimb.slice(
                  MODEXP_LIMB_INT_BYTE_SIZE * counter, MODEXP_LIMB_INT_BYTE_SIZE));
        }

        boolean isBlakeData = phaseFlags[4];
        boolean isBlakeParams = phaseFlags[5];
        boolean isBlakeResult = phaseFlags[6];

        if (blake2fComponents.isPresent()) {
          Blake2fComponents components = blake2fComponents.get();
          if (isBlakeData) {
            for (int i = 0; i < BLAKE2f_DATA_SIZE; i++) {
              trace.limb(
                  components
                      .data()
                      .slice(BLAKE2f_LIMB_INT_BYTE_SIZE * i, BLAKE2f_LIMB_INT_BYTE_SIZE));
            }
          } else if (isBlakeParams) {
            trace.limb(components.r());
            trace.limb(components.f());
          } else if (isBlakeResult && !blake2fResult.isEmpty()) {
            for (int i = 0; i < BLAKE2f_RESULT_SIZE; i++) {
              trace.limb(
                  blake2fResult.slice(BLAKE2f_LIMB_INT_BYTE_SIZE * i, BLAKE2f_LIMB_INT_BYTE_SIZE));
            }
          }
        }

        trace
            .isModexpBase(phaseFlags[0])
            .isModexpExponent(phaseFlags[1])
            .isModexpModulus(phaseFlags[2])
            .isModexpResult(phaseFlags[3])
            .isBlakeData(isBlakeData)
            .isBlakeParams(isBlakeParams)
            .isBlakeResult(isBlakeResult)
            .validateRow();
      }
    }

    prevHubStamp = currentHubStamp.toInt();
  }

  private Bytes buildModexpComponentsLimb(ModexpComponents components) {
    final Bytes result = computeModexpResult(components);
    final Bytes basePadded = leftPadTo(components.base(), MODEXP_COMPONENT_SIZE);
    final Bytes expPadded = leftPadTo(components.exp(), MODEXP_COMPONENT_SIZE);
    final Bytes modPadded = leftPadTo(components.mod(), MODEXP_COMPONENT_SIZE);
    final Bytes resultPadded = leftPadTo(result, MODEXP_COMPONENT_SIZE);

    return Bytes.concatenate(basePadded, expPadded, modPadded, resultPadded);
  }

  private PhaseInfo getPhaseInfo(int phaseIndex) {
    return switch (phaseIndex) {
      case 1 -> new PhaseInfo(Trace.PHASE_MODEXP_BASE, Trace.INDEX_MAX_MODEXP_BASE);
      case 2 -> new PhaseInfo(Trace.PHASE_MODEXP_EXPONENT, Trace.INDEX_MAX_MODEXP_EXPONENT);
      case 3 -> new PhaseInfo(Trace.PHASE_MODEXP_MODULUS, Trace.INDEX_MAX_MODEXP_MODULUS);
      case 4 -> new PhaseInfo(Trace.PHASE_MODEXP_RESULT, Trace.INDEX_MAX_MODEXP_RESULT);
      case 5 -> new PhaseInfo(Trace.PHASE_BLAKE_DATA, Trace.INDEX_MAX_BLAKE_DATA);
      case 6 -> new PhaseInfo(Trace.PHASE_BLAKE_PARAMS, Trace.INDEX_MAX_BLAKE_PARAMS);
      case 7 -> new PhaseInfo(Trace.PHASE_BLAKE_RESULT, Trace.INDEX_MAX_BLAKE_RESULT);
      default -> throw new IllegalStateException("Unexpected phase index: " + phaseIndex);
    };
  }

  private Bytes computeModexpResult(ModexpComponents modexpComponents) {
    final BigInteger baseBigInt = modexpComponents.base().toUnsignedBigInteger();
    final BigInteger expBigInt = modexpComponents.exp().toUnsignedBigInteger();
    final BigInteger modBigInt = modexpComponents.mod().toUnsignedBigInteger();

    return bigIntegerToBytes(baseBigInt.modPow(expBigInt, modBigInt));
  }

  private Bytes computeBlake2fResult(Bytes input) {
    return Hash.blake2bf(input);
  }

  private UnsignedByte[] getHubStampDiffBytes(Bytes currentHubStamp) {
    BigInteger prevHubStampBigInt = BigInteger.valueOf(prevHubStamp);
    BigInteger hubStampBigInt = currentHubStamp.toUnsignedBigInteger();
    BigInteger hubStampDiff = hubStampBigInt.subtract(prevHubStampBigInt).subtract(BigInteger.ONE);

    Preconditions.checkArgument(
        hubStampDiff.compareTo(BigInteger.valueOf(256 ^ 6)) < 0,
        "Hub stamp difference should never exceed 256 ^ 6");

    return bytesToUnsignedBytes(
        rightPadTo(leftPadTo(bigIntegerToBytes(hubStampDiff), 6), 128).toArray());
  }
}
