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

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Accessors(fluent = true)
public class Blake2fModexpDataOperation extends ModuleOperation {
  private static final int BEMR_LINE_COUNT = 32 * 4;
  private static final int COMPONENT_SIZE = 512;

  private final int hubStamp;
  @Getter private int prevHubStamp;
  @EqualsAndHashCode.Include private final Bytes base;
  @EqualsAndHashCode.Include private final Bytes exp;
  @EqualsAndHashCode.Include private final Bytes mod;
  private Bytes result;

  public Blake2fModexpDataOperation(
      int hubStamp, int prevHubStamp, Bytes base, Bytes exp, Bytes mod) {
    this.hubStamp = hubStamp;
    this.prevHubStamp = prevHubStamp;
    this.base = base;
    this.exp = exp;
    this.mod = mod;
  }

  @Override
  protected int computeLineCount() {
    return BEMR_LINE_COUNT;
  }

  public void computeResult() {
    final BigInteger baseBigInt = base.toUnsignedBigInteger();
    final BigInteger expBigInt = exp.toUnsignedBigInteger();
    final BigInteger modBigInt = mod.toUnsignedBigInteger();

    result = bigIntegerToBytes(baseBigInt.modPow(expBigInt, modBigInt));
  }

  void trace(Trace trace, int stamp) {
    computeResult();

    final UnsignedByte stampBytes = UnsignedByte.of(stamp);
    final Bytes currentHubStamp = Bytes.ofUnsignedInt(this.hubStamp + 1);

    final Bytes basePadded = leftPadTo(base, COMPONENT_SIZE);
    final Bytes expPadded = leftPadTo(exp, COMPONENT_SIZE);
    final Bytes modPadded = leftPadTo(mod, COMPONENT_SIZE);
    final Bytes resultPadded = leftPadTo(result, COMPONENT_SIZE);
    final Bytes bemrLimb = Bytes.concatenate(basePadded, expPadded, modPadded, resultPadded);

    BigInteger prevHubStampBigInt = BigInteger.valueOf(prevHubStamp);
    BigInteger hubStampBigInt = currentHubStamp.toUnsignedBigInteger();
    BigInteger hubStampDiff = hubStampBigInt.subtract(prevHubStampBigInt).subtract(BigInteger.ONE);

    Preconditions.checkArgument(
        hubStampDiff.compareTo(BigInteger.valueOf(256 ^ 6)) < 0,
        "Hub stamp difference should never exceed 256 ^ 6");

    UnsignedByte[] hubStampDiffBytes =
        bytesToUnsignedBytes(
            rightPadTo(leftPadTo(bigIntegerToBytes(hubStampDiff), 6), 128).toArray());

    boolean[] phaseFlags = new boolean[7];

    for (int phaseIndex = 1; phaseIndex <= 7; phaseIndex++) {
      phaseFlags[phaseIndex - 1] = true;

      PhaseInfo phaseInfo =
          switch (phaseIndex) {
            case 1 -> new PhaseInfo(Trace.PHASE_MODEXP_BASE, 31);
            case 2 -> new PhaseInfo(Trace.PHASE_MODEXP_EXPONENT, 31);
            case 3 -> new PhaseInfo(Trace.PHASE_MODEXP_MODULUS, 31);
            case 4 -> new PhaseInfo(Trace.PHASE_MODEXP_RESULT, 31);
            case 5 -> new PhaseInfo(Trace.PHASE_BLAKE_DATA, 12);
            case 6 -> new PhaseInfo(Trace.PHASE_BLAKE_PARAMS, 1);
            case 7 -> new PhaseInfo(Trace.PHASE_BLAKE_RESULT, 3);
            default -> throw new IllegalStateException("Unexpected phase index: " + phaseIndex);
          };

      for (int index = 0; index < 32; index++) {
        int counter = 32 * (phaseIndex - 1) + index;

        trace
            //            .ct(UnsignedByte.of(counter))
            //            .bemr(UnsignedByte.of(phaseIndex))
            .phase(UnsignedByte.of(phaseInfo.id()))
            .deltaByte(hubStampDiffBytes[counter])
            .limb(bemrLimb.slice(16 * counter, 16))
            .index(UnsignedByte.of(index))
            .indexMax(UnsignedByte.of(phaseInfo.indexMax()))
            .id(currentHubStamp)
            .stamp(stampBytes)
            .isModexpBase(phaseFlags[0])
            .isModexpExponent(phaseFlags[1])
            .isModexpModulus(phaseFlags[2])
            .isModexpResult(phaseFlags[3])
            .isBlakeData(phaseFlags[4])
            .isBlakeParams(phaseFlags[5])
            .isBlakeResult(phaseFlags[6])
            .validateRow();
      }
    }

    prevHubStamp = currentHubStamp.toInt();
  }
}
