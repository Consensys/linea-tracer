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

import static net.consensys.linea.zktracer.module.blake2fmodexpdata.Trace.INDEX_MAX_BLAKE_DATA;
import static net.consensys.linea.zktracer.module.blake2fmodexpdata.Trace.INDEX_MAX_BLAKE_PARAMS;
import static net.consensys.linea.zktracer.module.blake2fmodexpdata.Trace.INDEX_MAX_BLAKE_RESULT;
import static net.consensys.linea.zktracer.module.blake2fmodexpdata.Trace.INDEX_MAX_MODEXP;
import static net.consensys.linea.zktracer.module.blake2fmodexpdata.Trace.INDEX_MAX_MODEXP_BASE;
import static net.consensys.linea.zktracer.module.blake2fmodexpdata.Trace.INDEX_MAX_MODEXP_EXPONENT;
import static net.consensys.linea.zktracer.module.blake2fmodexpdata.Trace.INDEX_MAX_MODEXP_MODULUS;
import static net.consensys.linea.zktracer.module.blake2fmodexpdata.Trace.INDEX_MAX_MODEXP_RESULT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.LLARGE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_BLAKE_DATA;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_BLAKE_PARAMS;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_BLAKE_RESULT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_MODEXP_BASE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_MODEXP_EXPONENT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_MODEXP_MODULUS;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_MODEXP_RESULT;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;
import static net.consensys.linea.zktracer.types.Utils.leftPadTo;

import java.math.BigInteger;
import java.util.Optional;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.Hash;

@Accessors(fluent = true)
public class BlakeModexpDataOperation extends ModuleOperation {
  private static final int MODEXP_COMPONENT_BYTE_SIZE = LLARGE * (INDEX_MAX_MODEXP + 1);
  private static final int MODEXP_COMPONENTS_LINE_COUNT =
      (INDEX_MAX_MODEXP_BASE + 1)
          + (INDEX_MAX_MODEXP_EXPONENT + 1)
          + (INDEX_MAX_MODEXP_MODULUS + 1)
          + (INDEX_MAX_MODEXP_RESULT + 1);
  private static final int BLAKE2f_COMPONENTS_LINE_COUNT =
      (INDEX_MAX_BLAKE_DATA + 1) + (INDEX_MAX_BLAKE_PARAMS + 1) + (INDEX_MAX_BLAKE_RESULT + 1);

  @Getter public final long id;

  public final Optional<ModexpComponents> modexpComponents;
  public final Optional<BlakeComponents> blake2fComponents;

  public BlakeModexpDataOperation(
      long hubStamp, ModexpComponents modexpComponents, BlakeComponents blakeComponents) {
    this.id = hubStamp + 1;
    this.modexpComponents = Optional.ofNullable(modexpComponents);
    this.blake2fComponents = Optional.ofNullable(blakeComponents);
  }

  @Override
  protected int computeLineCount() {
    return modexpComponents.isPresent()
        ? MODEXP_COMPONENTS_LINE_COUNT
        : BLAKE2f_COMPONENTS_LINE_COUNT;
  }

  void trace(Trace trace, final int stamp) {
    final UnsignedByte stampByte = UnsignedByte.of(stamp);

    if (modexpComponents.isPresent()) {
      traceBase(trace, stampByte);
      traceExponent(trace, stampByte);
      traceModulus(trace, stampByte);
      traceModexpResult(trace, stampByte);
      return;
    }

    if (blake2fComponents.isPresent()) {
      traceData(trace, stampByte);
      traceParameter(trace, stampByte);
      traceBlakeResult(trace, stampByte);
    }
  }

  private void traceData(Trace trace, UnsignedByte stamp) {
    final Bytes input = blake2fComponents.get().data();
    for (int index = 0; index <= INDEX_MAX_BLAKE_DATA; index++) {
      commonTrace(trace, stamp, index, input, INDEX_MAX_BLAKE_DATA);
      trace.phase(UnsignedByte.of(PHASE_BLAKE_DATA)).isBlakeData(true).fillAndValidateRow();
    }
  }

  private void traceParameter(Trace trace, UnsignedByte stamp) {
    // r
    commonTrace(
        trace, stamp, 0, leftPadTo(blake2fComponents.get().r(), LLARGE), INDEX_MAX_BLAKE_PARAMS);
    trace.phase(UnsignedByte.of(PHASE_BLAKE_PARAMS)).isBlakeParams(true).fillAndValidateRow();

    // f
    commonTrace(
        trace,
        stamp,
        1,
        leftPadTo(blake2fComponents.get().f(), 2 * LLARGE),
        INDEX_MAX_BLAKE_PARAMS);
    trace.phase(UnsignedByte.of(PHASE_BLAKE_PARAMS)).isBlakeParams(true).fillAndValidateRow();
  }

  private void traceBlakeResult(Trace trace, UnsignedByte stamp) {
    final Bytes input = computeBlake2fResult();
    for (int index = 0; index <= INDEX_MAX_BLAKE_RESULT; index++) {
      commonTrace(trace, stamp, index, input, INDEX_MAX_BLAKE_RESULT);
      trace.phase(UnsignedByte.of(PHASE_BLAKE_RESULT)).isBlakeResult(true).fillAndValidateRow();
    }
  }

  private void traceBase(Trace trace, final UnsignedByte stamp) {
    final Bytes input = leftPadTo(modexpComponents.get().base(), MODEXP_COMPONENT_BYTE_SIZE);
    for (int index = 0; index <= INDEX_MAX_MODEXP_BASE; index++) {
      commonTrace(trace, stamp, index, input, INDEX_MAX_MODEXP_BASE);
      trace.phase(UnsignedByte.of(PHASE_MODEXP_BASE)).isModexpBase(true).fillAndValidateRow();
    }
  }

  private void traceExponent(Trace trace, final UnsignedByte stamp) {
    final Bytes input = leftPadTo(modexpComponents.get().exp(), MODEXP_COMPONENT_BYTE_SIZE);
    for (int index = 0; index <= INDEX_MAX_MODEXP_EXPONENT; index++) {
      commonTrace(trace, stamp, index, input, INDEX_MAX_MODEXP_EXPONENT);
      trace
          .phase(UnsignedByte.of(PHASE_MODEXP_EXPONENT))
          .isModexpExponent(true)
          .fillAndValidateRow();
    }
  }

  private void traceModulus(Trace trace, final UnsignedByte stamp) {
    final Bytes input = leftPadTo(modexpComponents.get().mod(), MODEXP_COMPONENT_BYTE_SIZE);
    for (int index = 0; index <= INDEX_MAX_MODEXP_MODULUS; index++) {
      commonTrace(trace, stamp, index, input, INDEX_MAX_MODEXP_MODULUS);
      trace.phase(UnsignedByte.of(PHASE_MODEXP_MODULUS)).isModexpModulus(true).fillAndValidateRow();
    }
  }

  private void traceModexpResult(Trace trace, final UnsignedByte stamp) {
    final Bytes input = leftPadTo(computeModexpResult(), MODEXP_COMPONENT_BYTE_SIZE);
    for (int index = 0; index <= INDEX_MAX_MODEXP_RESULT; index++) {
      commonTrace(trace, stamp, index, input, INDEX_MAX_MODEXP_RESULT);
      trace.phase(UnsignedByte.of(PHASE_MODEXP_RESULT)).isModexpResult(true).fillAndValidateRow();
    }
  }

  private void commonTrace(Trace trace, UnsignedByte stamp, int index, Bytes input, int indexMax) {
    trace
        .stamp(stamp)
        .id(id)
        .index(UnsignedByte.of(index))
        .indexMax(UnsignedByte.of(indexMax))
        .limb(input.slice(index * LLARGE, LLARGE));
  }

  private Bytes computeModexpResult() {
    final BigInteger baseBigInt = modexpComponents.get().base().toUnsignedBigInteger();
    final BigInteger expBigInt = modexpComponents.get().exp().toUnsignedBigInteger();
    final BigInteger modBigInt = modexpComponents.get().mod().toUnsignedBigInteger();

    return modBigInt.equals(BigInteger.ZERO)
        ? Bytes.of(0)
        : bigIntegerToBytes(baseBigInt.modPow(expBigInt, modBigInt));
  }

  private Bytes computeBlake2fResult() {
    return Hash.blake2bf(blake2fComponents.get().data());
  }
}
