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

package net.consensys.linea.zktracer.module.bls;

import static com.google.common.base.Preconditions.checkArgument;
import static net.consensys.linea.zktracer.Trace.Bls.BLS_PRIME_3;
import static net.consensys.linea.zktracer.Trace.Bls.CT_MAX_LARGE_POINT;
import static net.consensys.linea.zktracer.Trace.Bls.CT_MAX_MAP_FP2_TO_G2;
import static net.consensys.linea.zktracer.Trace.Bls.CT_MAX_MAP_FP_TO_G1;
import static net.consensys.linea.zktracer.Trace.Bls.CT_MAX_POINT_EVALUATION;
import static net.consensys.linea.zktracer.Trace.Bls.CT_MAX_SCALAR;
import static net.consensys.linea.zktracer.Trace.Bls.CT_MAX_SMALL_POINT;
import static net.consensys.linea.zktracer.Trace.Bls.POINT_EVALUATION_PRIME_HI;
import static net.consensys.linea.zktracer.Trace.Bls.POINT_EVALUATION_PRIME_LO;
import static net.consensys.linea.zktracer.Trace.LLARGE;
import static net.consensys.linea.zktracer.Trace.WORD_SIZE;
import static net.consensys.linea.zktracer.TraceCancun.Bls.BLS_PRIME_0;
import static net.consensys.linea.zktracer.TraceCancun.Bls.BLS_PRIME_1;
import static net.consensys.linea.zktracer.TraceCancun.Bls.BLS_PRIME_2;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_DATA_G1_ADD;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_DATA_G2_ADD;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_DATA_MAP_FP2_TO_G2;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_DATA_MAP_FP_TO_G1;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_DATA_POINT_EVALUATION;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_RSLT_G1_ADD;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_RSLT_G1_MSM;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_RSLT_G2_ADD;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_RSLT_G2_MSM;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_RSLT_MAP_FP2_TO_G2;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_RSLT_MAP_FP_TO_G1;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_RSLT_PAIRING_CHECK;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_RSLT_POINT_EVALUATION;
import static net.consensys.linea.zktracer.TraceCancun.PHASE_DATA_G1_ADD;
import static net.consensys.linea.zktracer.TraceCancun.PHASE_DATA_G1_MSM;
import static net.consensys.linea.zktracer.TraceCancun.PHASE_DATA_G2_ADD;
import static net.consensys.linea.zktracer.TraceCancun.PHASE_DATA_G2_MSM;
import static net.consensys.linea.zktracer.TraceCancun.PHASE_DATA_MAP_FP2_TO_G2;
import static net.consensys.linea.zktracer.TraceCancun.PHASE_DATA_MAP_FP_TO_G1;
import static net.consensys.linea.zktracer.TraceCancun.PHASE_DATA_PAIRING_CHECK;
import static net.consensys.linea.zktracer.TraceCancun.PHASE_DATA_POINT_EVALUATION;
import static net.consensys.linea.zktracer.TraceCancun.PHASE_RSLT_G1_ADD;
import static net.consensys.linea.zktracer.TraceCancun.PHASE_RSLT_G1_MSM;
import static net.consensys.linea.zktracer.TraceCancun.PHASE_RSLT_G2_ADD;
import static net.consensys.linea.zktracer.TraceCancun.PHASE_RSLT_G2_MSM;
import static net.consensys.linea.zktracer.TraceCancun.PHASE_RSLT_MAP_FP2_TO_G2;
import static net.consensys.linea.zktracer.TraceCancun.PHASE_RSLT_MAP_FP_TO_G1;
import static net.consensys.linea.zktracer.TraceCancun.PHASE_RSLT_PAIRING_CHECK;
import static net.consensys.linea.zktracer.TraceCancun.PHASE_RSLT_POINT_EVALUATION;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileFlag.PRC_BLS_G1_ADD;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileFlag.PRC_BLS_G1_MSM;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileFlag.PRC_BLS_G2_ADD;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileFlag.PRC_BLS_G2_MSM;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileFlag.PRC_BLS_MAP_FP2_TO_G2;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileFlag.PRC_BLS_MAP_FP_TO_G1;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileFlag.PRC_BLS_PAIRING_CHECK;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileFlag.PRC_POINT_EVALUATION;
import static net.consensys.linea.zktracer.types.Containers.repeat;
import static net.consensys.linea.zktracer.types.Conversions.ZERO;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;
import static net.consensys.linea.zktracer.types.Utils.leftPadTo;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

@Accessors(fluent = true)
public class BlsOperation extends ModuleOperation {
  final EWord BLS_PRIME_HI = EWord.of(BigInteger.valueOf(BLS_PRIME_3), BLS_PRIME_2);
  final EWord BLS_PRIME_LO = EWord.of(BLS_PRIME_1, BLS_PRIME_0);
  final EWord POINT_EVALUATION_PRIME =
      EWord.of(POINT_EVALUATION_PRIME_HI, POINT_EVALUATION_PRIME_LO);
  public static final int nBYTES_OF_DELTA_BYTES = 4;
  private final int SIZE_SMALL_POINT = LLARGE * (CT_MAX_SMALL_POINT + 1);
  private final int SIZE_LARGE_POINT = LLARGE * (CT_MAX_LARGE_POINT + 1);
  private final int SIZE_SCALAR = LLARGE * (CT_MAX_SCALAR + 1);

  private final Wcp wcp;

  private final Bytes callData;
  private final Bytes returnData;

  @Getter private final PrecompileScenarioFragment.PrecompileFlag precompileFlag;
  private final int nRows;
  private final int nRowsData;
  private final int nRowsResult;

  @Getter private final long id;
  private final int totalSizeData;
  private final int totalSizeResult;
  @Getter private final List<Bytes> limb;
  private boolean successBit;

  private final List<Boolean> mintBit;
  private final List<Boolean> isInfinity;

  // WCP interaction
  private final List<Boolean> wcpFlag;
  private final List<Bytes> wcpArg1Hi;
  private final List<Bytes> wcpArg1Lo;
  private final List<Bytes> wcpArg2Hi;
  private final List<Bytes> wcpArg2Lo;
  private final List<Boolean> wcpRes;
  private final List<OpCode> wcpInst;

  private BlsOperation(
      Wcp wcp,
      int id,
      final PrecompileScenarioFragment.PrecompileFlag precompileFlag,
      Bytes callData,
      Bytes returnData) {
    checkArgument(precompileFlag.isBlsPrecompile(), "invalid BLS type");

    this.precompileFlag = precompileFlag;
    this.callData = callData;
    totalSizeData = callData.size(); // TODO: do we need some padding?
    totalSizeResult = returnData.size();

    nRowsData = getIndexMax(precompileFlag, true) + 1;
    nRowsResult = getIndexMax(precompileFlag, false) + 1;
    nRows = nRowsData + nRowsResult;
    this.id = id;

    limb = repeat(Bytes.EMPTY, nRows);
    mintBit = repeat(false, nRows);
    isInfinity = repeat(false, nRows);

    wcpFlag = repeat(false, nRows);
    wcpArg1Hi = repeat(Bytes.EMPTY, nRows);
    wcpArg1Lo = repeat(Bytes.EMPTY, nRows);
    wcpArg2Hi = repeat(Bytes.EMPTY, nRows);
    wcpArg2Lo = repeat(Bytes.EMPTY, nRows);
    wcpRes = repeat(false, nRows);
    wcpInst = repeat(OpCode.INVALID, nRows);

    this.wcp = wcp;

    // Set returnData
    this.returnData = returnData;
  }

  public static BlsOperation of(
      Wcp wcp,
      int id,
      final PrecompileScenarioFragment.PrecompileFlag precompileFlag,
      Bytes callData,
      Bytes returnData) {
    BlsOperation blsOperation = new BlsOperation(wcp, id, precompileFlag, callData, returnData);
    switch (precompileFlag) {
      case PRC_POINT_EVALUATION -> blsOperation.handlePointEvaluation();
      case PRC_BLS_G1_ADD -> blsOperation.handleBlsG1Add();
      case PRC_BLS_G1_MSM -> blsOperation.handleBlsG1Msm();
      case PRC_BLS_G2_ADD -> blsOperation.handleBlsG2Add();
      case PRC_BLS_G2_MSM -> blsOperation.handleBlsG2Msm();
      case PRC_BLS_PAIRING_CHECK -> blsOperation.handleBlsPairingCheck();
      case PRC_BLS_MAP_FP_TO_G1 -> blsOperation.handleBlsMapFpToG1();
      case PRC_BLS_MAP_FP2_TO_G2 -> blsOperation.handleBlsMapFp2ToG2();
    }
    return blsOperation;
  }

  private void handlePointEvaluation() {
    // Extract inputs
    final EWord verHash = EWord.of(callData.slice(0, WORD_SIZE));
    final EWord z = EWord.of(callData.slice(WORD_SIZE, WORD_SIZE));
    final EWord y = EWord.of(callData.slice(2 * WORD_SIZE, WORD_SIZE));
    final Bytes com = callData.slice(3 * WORD_SIZE, 3 * LLARGE);
    final Bytes proof = callData.slice(3 * WORD_SIZE + 3 * LLARGE, 3 * LLARGE);

    // Set input limb
    limb.set(0, verHash.hi());
    limb.set(1, verHash.lo());
    limb.set(2, z.hi());
    limb.set(3, z.lo());
    limb.set(4, y.hi());
    limb.set(5, y.lo());
    limb.set(6, com.slice(0, LLARGE));
    limb.set(7, com.slice(LLARGE, LLARGE));
    limb.set(8, com.slice(2 * LLARGE, LLARGE));
    limb.set(9, proof.slice(0, LLARGE));
    limb.set(10, proof.slice(LLARGE, LLARGE));
    limb.set(11, proof.slice(2 * LLARGE, LLARGE));

    final boolean zIsInRange = wcpCallToLT(0, z, POINT_EVALUATION_PRIME);

    final boolean yIsInRange = wcpCallToLT(1, y, POINT_EVALUATION_PRIME);

    final boolean internalChecksPassed = zIsInRange && yIsInRange;

    for (int j = 0; j <= CT_MAX_POINT_EVALUATION; j++) {
      this.mintBit.set(j, !internalChecksPassed);
    }

    EWord fieldsElPerBlob = EWord.ZERO;
    EWord blsMod = EWord.ZERO;

    if (returnData.toArray().length != 0) {
      checkArgument(returnData.toArray().length == 64);
      fieldsElPerBlob = EWord.of(returnData.slice(0, 32));
      blsMod = EWord.of(returnData.slice(32, 32));
    }

    // Set result limb
    limb.set(12, fieldsElPerBlob.hi());
    limb.set(13, fieldsElPerBlob.lo());
    limb.set(14, blsMod.hi());
    limb.set(15, blsMod.lo());

    // TODO: set successBit and mextBit
  }

  private void handleBlsG1Add() {
    for (int k = 0; k < 2; k++) {
      final int sizeOffset = k * SIZE_SMALL_POINT;
      final int indexOffset = k * (CT_MAX_SMALL_POINT + 1);

      // Extract inputs
      final Bytes aX3 = callData.slice(sizeOffset, LLARGE);
      final Bytes aX2 = callData.slice(LLARGE + sizeOffset, LLARGE);
      final Bytes aX1 = callData.slice(2 * LLARGE + sizeOffset, LLARGE);
      final Bytes aX0 = callData.slice(3 * LLARGE + sizeOffset, LLARGE);
      final Bytes aY3 = callData.slice(4 * LLARGE + sizeOffset, LLARGE);
      final Bytes aY2 = callData.slice(5 * LLARGE + sizeOffset, LLARGE);
      final Bytes aY1 = callData.slice(6 * LLARGE + sizeOffset, LLARGE);
      final Bytes aY0 = callData.slice(7 * LLARGE + sizeOffset, LLARGE);

      // Set input limb
      limb.set(indexOffset, aX3);
      limb.set(1 + indexOffset, aX2);
      limb.set(2 + indexOffset, aX1);
      limb.set(3 + indexOffset, aX0);
      limb.set(4 + indexOffset, aY3);
      limb.set(5 + indexOffset, aY2);
      limb.set(6 + indexOffset, aY1);
      limb.set(7 + indexOffset, aY0);

      wellFormedFpCoordinateAndInfinityCheck(indexOffset, aX3, aX2, aX1, aX0, aY3, aY2, aY1, aY0);
    }

    Bytes cX3 = ZERO;
    Bytes cX2 = ZERO;
    Bytes cX1 = ZERO;
    Bytes cX0 = ZERO;
    Bytes cY3 = ZERO;
    Bytes cY2 = ZERO;
    Bytes cY1 = ZERO;
    Bytes cY0 = ZERO;

    if (returnData.toArray().length != 0) {
      checkArgument(returnData.toArray().length == SIZE_SMALL_POINT);
      cX3 = returnData.slice(0, LLARGE);
      cX2 = returnData.slice(LLARGE, LLARGE);
      cX1 = returnData.slice(2 * LLARGE, LLARGE);
      cX0 = returnData.slice(3 * LLARGE, LLARGE);
      cY3 = returnData.slice(4 * LLARGE, LLARGE);
      cY2 = returnData.slice(5 * LLARGE, LLARGE);
      cY1 = returnData.slice(6 * LLARGE, LLARGE);
      cY0 = returnData.slice(7 * LLARGE, LLARGE);
    }

    // Set result limb
    limb.set(16, cX3);
    limb.set(17, cX2);
    limb.set(18, cX1);
    limb.set(19, cX0);
    limb.set(20, cY3);
    limb.set(21, cY2);
    limb.set(22, cY1);
    limb.set(23, cY0);

    // TODO: set successBit and mextBit
  }

  private void handleBlsG1Msm() {
    final int numberOfInputs = callData.size() / (SIZE_SMALL_POINT + SIZE_SCALAR);
    for (int k = 0; k < numberOfInputs; k++) {
      final int sizeOffset = k * (SIZE_SMALL_POINT + SIZE_SCALAR);
      final int indexOffset = k * (CT_MAX_SMALL_POINT + 1 + CT_MAX_SCALAR + 1);

      // Extract inputs
      final Bytes aX3 = callData.slice(sizeOffset, LLARGE);
      final Bytes aX2 = callData.slice(LLARGE + sizeOffset, LLARGE);
      final Bytes aX1 = callData.slice(2 * LLARGE + sizeOffset, LLARGE);
      final Bytes aX0 = callData.slice(3 * LLARGE + sizeOffset, LLARGE);
      final Bytes aY3 = callData.slice(4 * LLARGE + sizeOffset, LLARGE);
      final Bytes aY2 = callData.slice(5 * LLARGE + sizeOffset, LLARGE);
      final Bytes aY1 = callData.slice(6 * LLARGE + sizeOffset, LLARGE);
      final Bytes aY0 = callData.slice(7 * LLARGE + sizeOffset, LLARGE);
      final EWord n = EWord.of(callData.slice(8 * LLARGE + sizeOffset, WORD_SIZE));

      // Set input limb
      limb.set(indexOffset, aX3);
      limb.set(1 + indexOffset, aX2);
      limb.set(2 + indexOffset, aX1);
      limb.set(3 + indexOffset, aX0);
      limb.set(4 + indexOffset, aY3);
      limb.set(5 + indexOffset, aY2);
      limb.set(6 + indexOffset, aY1);
      limb.set(7 + indexOffset, aY0);
      limb.set(8 + indexOffset, n.hi());
      limb.set(9 + indexOffset, n.lo());

      wellFormedFpCoordinateAndInfinityCheck(indexOffset, aX3, aX2, aX1, aX0, aY3, aY2, aY1, aY0);
    }

    Bytes cX3 = ZERO;
    Bytes cX2 = ZERO;
    Bytes cX1 = ZERO;
    Bytes cX0 = ZERO;
    Bytes cY3 = ZERO;
    Bytes cY2 = ZERO;
    Bytes cY1 = ZERO;
    Bytes cY0 = ZERO;

    if (returnData.toArray().length != 0) {
      checkArgument(returnData.toArray().length == SIZE_SMALL_POINT);
      cX3 = returnData.slice(0, LLARGE);
      cX2 = returnData.slice(LLARGE, LLARGE);
      cX1 = returnData.slice(2 * LLARGE, LLARGE);
      cX0 = returnData.slice(3 * LLARGE, LLARGE);
      cY3 = returnData.slice(4 * LLARGE, LLARGE);
      cY2 = returnData.slice(5 * LLARGE, LLARGE);
      cY1 = returnData.slice(6 * LLARGE, LLARGE);
      cY0 = returnData.slice(7 * LLARGE, LLARGE);
    }

    final int indexOffsetResultMax =
        (numberOfInputs - 1) * (CT_MAX_SMALL_POINT + 1 + CT_MAX_SCALAR + 1);

    // Set result limb
    limb.set(10 + indexOffsetResultMax, cX3);
    limb.set(11 + indexOffsetResultMax, cX2);
    limb.set(12 + indexOffsetResultMax, cX1);
    limb.set(13 + indexOffsetResultMax, cX0);
    limb.set(14 + indexOffsetResultMax, cY3);
    limb.set(15 + indexOffsetResultMax, cY2);
    limb.set(16 + indexOffsetResultMax, cY1);
    limb.set(17 + indexOffsetResultMax, cY0);

    // TODO: set successBit and mextBit
  }

  private void handleBlsG2Add() {
    for (int k = 0; k < 2; k++) {
      final int sizeOffset = k * SIZE_LARGE_POINT;
      final int indexOffset = k * (CT_MAX_LARGE_POINT + 1);

      // Extract inputs
      final Bytes aXIm3 = callData.slice(sizeOffset, LLARGE);
      final Bytes aXIm2 = callData.slice(LLARGE + sizeOffset, LLARGE);
      final Bytes aXIm1 = callData.slice(2 * LLARGE + sizeOffset, LLARGE);
      final Bytes aXIm0 = callData.slice(3 * LLARGE + sizeOffset, LLARGE);
      final Bytes aXRe3 = callData.slice(4 * LLARGE + sizeOffset, LLARGE);
      final Bytes aXRe2 = callData.slice(5 * LLARGE + sizeOffset, LLARGE);
      final Bytes aXRe1 = callData.slice(6 * LLARGE + sizeOffset, LLARGE);
      final Bytes aXRe0 = callData.slice(7 * LLARGE + sizeOffset, LLARGE);
      final Bytes aYIm3 = callData.slice(8 * LLARGE + sizeOffset, LLARGE);
      final Bytes aYIm2 = callData.slice(9 * LLARGE + sizeOffset, LLARGE);
      final Bytes aYIm1 = callData.slice(10 * LLARGE + sizeOffset, LLARGE);
      final Bytes aYIm0 = callData.slice(11 * LLARGE + sizeOffset, LLARGE);
      final Bytes aYRe3 = callData.slice(12 * LLARGE + sizeOffset, LLARGE);
      final Bytes aYRe2 = callData.slice(13 * LLARGE + sizeOffset, LLARGE);
      final Bytes aYRe1 = callData.slice(14 * LLARGE + sizeOffset, LLARGE);
      final Bytes aYRe0 = callData.slice(15 * LLARGE + sizeOffset, LLARGE);

      // Set input limb
      limb.set(indexOffset, aXIm3);
      limb.set(1 + indexOffset, aXIm2);
      limb.set(2 + indexOffset, aXIm1);
      limb.set(3 + indexOffset, aXIm0);
      limb.set(4 + indexOffset, aXRe3);
      limb.set(5 + indexOffset, aXRe2);
      limb.set(6 + indexOffset, aXRe1);
      limb.set(7 + indexOffset, aXRe0);
      limb.set(8 + indexOffset, aYIm3);
      limb.set(9 + indexOffset, aYIm2);
      limb.set(10 + indexOffset, aYIm1);
      limb.set(11 + indexOffset, aYIm0);
      limb.set(12 + indexOffset, aYRe3);
      limb.set(13 + indexOffset, aYRe2);
      limb.set(14 + indexOffset, aYRe1);
      limb.set(15 + indexOffset, aYRe0);

      wellFormedFp2CoordinateAndInfinityCheck(
          indexOffset,
          aXIm3,
          aXIm2,
          aXIm1,
          aXIm0,
          aXRe3,
          aXRe2,
          aXRe1,
          aXRe0,
          aYIm3,
          aYIm2,
          aYIm1,
          aYIm0,
          aYRe3,
          aYRe2,
          aYRe1,
          aYRe0);
    }

    Bytes cXIm3 = ZERO;
    Bytes cXIm2 = ZERO;
    Bytes cXIm1 = ZERO;
    Bytes cXIm0 = ZERO;
    Bytes cXRe3 = ZERO;
    Bytes cXRe2 = ZERO;
    Bytes cXRe1 = ZERO;
    Bytes cXRe0 = ZERO;
    Bytes cYIm3 = ZERO;
    Bytes cYIm2 = ZERO;
    Bytes cYIm1 = ZERO;
    Bytes cYIm0 = ZERO;
    Bytes cYRe3 = ZERO;
    Bytes cYRe2 = ZERO;
    Bytes cYRe1 = ZERO;
    Bytes cYRe0 = ZERO;

    if (returnData.toArray().length != 0) {
      checkArgument(returnData.toArray().length == SIZE_LARGE_POINT);
      cXIm3 = returnData.slice(0, LLARGE);
      cXIm2 = returnData.slice(LLARGE, LLARGE);
      cXIm1 = returnData.slice(2 * LLARGE, LLARGE);
      cXIm0 = returnData.slice(3 * LLARGE, LLARGE);
      cXRe3 = returnData.slice(4 * LLARGE, LLARGE);
      cXRe2 = returnData.slice(5 * LLARGE, LLARGE);
      cXRe1 = returnData.slice(6 * LLARGE, LLARGE);
      cXRe0 = returnData.slice(7 * LLARGE, LLARGE);
      cYIm3 = returnData.slice(8 * LLARGE, LLARGE);
      cYIm2 = returnData.slice(9 * LLARGE, LLARGE);
      cYIm1 = returnData.slice(10 * LLARGE, LLARGE);
      cYIm0 = returnData.slice(11 * LLARGE, LLARGE);
      cYRe3 = returnData.slice(12 * LLARGE, LLARGE);
      cYRe2 = returnData.slice(13 * LLARGE, LLARGE);
      cYRe1 = returnData.slice(14 * LLARGE, LLARGE);
      cYRe0 = returnData.slice(15 * LLARGE, LLARGE);
    }

    // Set result limb
    limb.set(32, cXIm3);
    limb.set(33, cXIm2);
    limb.set(34, cXIm1);
    limb.set(35, cXIm0);
    limb.set(36, cXRe3);
    limb.set(37, cXRe2);
    limb.set(38, cXRe1);
    limb.set(39, cXRe0);
    limb.set(40, cYIm3);
    limb.set(41, cYIm2);
    limb.set(42, cYIm1);
    limb.set(43, cYIm0);
    limb.set(44, cYRe3);
    limb.set(45, cYRe2);
    limb.set(46, cYRe1);
    limb.set(47, cYRe0);

    // TODO: set successBit and mextBit
  }

  private void handleBlsG2Msm() {
    final int numberOfInputs = callData.size() / (SIZE_LARGE_POINT + SIZE_SCALAR);
    for (int k = 0; k < numberOfInputs; k++) {
      final int sizeOffset = k * (SIZE_LARGE_POINT + SIZE_SCALAR);
      final int indexOffset = k * (CT_MAX_LARGE_POINT + 1 + CT_MAX_SCALAR + 1);

      // Extract inputs
      final Bytes aXIm3 = callData.slice(sizeOffset, LLARGE);
      final Bytes aXIm2 = callData.slice(LLARGE + sizeOffset, LLARGE);
      final Bytes aXIm1 = callData.slice(2 * LLARGE + sizeOffset, LLARGE);
      final Bytes aXIm0 = callData.slice(3 * LLARGE + sizeOffset, LLARGE);
      final Bytes aXRe3 = callData.slice(4 * LLARGE + sizeOffset, LLARGE);
      final Bytes aXRe2 = callData.slice(5 * LLARGE + sizeOffset, LLARGE);
      final Bytes aXRe1 = callData.slice(6 * LLARGE + sizeOffset, LLARGE);
      final Bytes aXRe0 = callData.slice(7 * LLARGE + sizeOffset, LLARGE);
      final Bytes aYIm3 = callData.slice(8 * LLARGE + sizeOffset, LLARGE);
      final Bytes aYIm2 = callData.slice(9 * LLARGE + sizeOffset, LLARGE);
      final Bytes aYIm1 = callData.slice(10 * LLARGE + sizeOffset, LLARGE);
      final Bytes aYIm0 = callData.slice(11 * LLARGE + sizeOffset, LLARGE);
      final Bytes aYRe3 = callData.slice(12 * LLARGE + sizeOffset, LLARGE);
      final Bytes aYRe2 = callData.slice(13 * LLARGE + sizeOffset, LLARGE);
      final Bytes aYRe1 = callData.slice(14 * LLARGE + sizeOffset, LLARGE);
      final Bytes aYRe0 = callData.slice(15 * LLARGE + sizeOffset, LLARGE);
      final EWord n = EWord.of(callData.slice(16 * LLARGE + sizeOffset, WORD_SIZE));

      // Set input limb
      limb.set(indexOffset, aXIm3);
      limb.set(1 + indexOffset, aXIm2);
      limb.set(2 + indexOffset, aXIm1);
      limb.set(3 + indexOffset, aXIm0);
      limb.set(4 + indexOffset, aXRe3);
      limb.set(5 + indexOffset, aXRe2);
      limb.set(6 + indexOffset, aXRe1);
      limb.set(7 + indexOffset, aXRe0);
      limb.set(8 + indexOffset, aYIm3);
      limb.set(9 + indexOffset, aYIm2);
      limb.set(10 + indexOffset, aYIm1);
      limb.set(11 + indexOffset, aYIm0);
      limb.set(12 + indexOffset, aYRe3);
      limb.set(13 + indexOffset, aYRe2);
      limb.set(14 + indexOffset, aYRe1);
      limb.set(15 + indexOffset, aYRe0);
      limb.set(16 + indexOffset, n.hi());
      limb.set(17 + indexOffset, n.lo());

      wellFormedFp2CoordinateAndInfinityCheck(
          indexOffset,
          aXIm3,
          aXIm2,
          aXIm1,
          aXIm0,
          aXRe3,
          aXRe2,
          aXRe1,
          aXRe0,
          aYIm3,
          aYIm2,
          aYIm1,
          aYIm0,
          aYRe3,
          aYRe2,
          aYRe1,
          aYRe0);
    }

    Bytes cXIm3 = ZERO;
    Bytes cXIm2 = ZERO;
    Bytes cXIm1 = ZERO;
    Bytes cXIm0 = ZERO;
    Bytes cXRe3 = ZERO;
    Bytes cXRe2 = ZERO;
    Bytes cXRe1 = ZERO;
    Bytes cXRe0 = ZERO;
    Bytes cYIm3 = ZERO;
    Bytes cYIm2 = ZERO;
    Bytes cYIm1 = ZERO;
    Bytes cYIm0 = ZERO;
    Bytes cYRe3 = ZERO;
    Bytes cYRe2 = ZERO;
    Bytes cYRe1 = ZERO;
    Bytes cYRe0 = ZERO;

    if (returnData.toArray().length != 0) {
      checkArgument(returnData.toArray().length == SIZE_LARGE_POINT);
      cXIm3 = returnData.slice(0, LLARGE);
      cXIm2 = returnData.slice(LLARGE, LLARGE);
      cXIm1 = returnData.slice(2 * LLARGE, LLARGE);
      cXIm0 = returnData.slice(3 * LLARGE, LLARGE);
      cXRe3 = returnData.slice(4 * LLARGE, LLARGE);
      cXRe2 = returnData.slice(5 * LLARGE, LLARGE);
      cXRe1 = returnData.slice(6 * LLARGE, LLARGE);
      cXRe0 = returnData.slice(7 * LLARGE, LLARGE);
      cYIm3 = returnData.slice(8 * LLARGE, LLARGE);
      cYIm2 = returnData.slice(9 * LLARGE, LLARGE);
      cYIm1 = returnData.slice(10 * LLARGE, LLARGE);
      cYIm0 = returnData.slice(11 * LLARGE, LLARGE);
      cYRe3 = returnData.slice(12 * LLARGE, LLARGE);
      cYRe2 = returnData.slice(13 * LLARGE, LLARGE);
      cYRe1 = returnData.slice(14 * LLARGE, LLARGE);
      cYRe0 = returnData.slice(15 * LLARGE, LLARGE);
    }

    final int indexOffsetResultMax =
        (numberOfInputs - 1) * (CT_MAX_LARGE_POINT + 1 + CT_MAX_SCALAR + 1);

    // Set result limb
    limb.set(18 + indexOffsetResultMax, cXIm3);
    limb.set(19 + indexOffsetResultMax, cXIm2);
    limb.set(20 + indexOffsetResultMax, cXIm1);
    limb.set(21 + indexOffsetResultMax, cXIm0);
    limb.set(22 + indexOffsetResultMax, cXRe3);
    limb.set(23 + indexOffsetResultMax, cXRe2);
    limb.set(24 + indexOffsetResultMax, cXRe1);
    limb.set(25 + indexOffsetResultMax, cXRe0);
    limb.set(26 + indexOffsetResultMax, cYIm3);
    limb.set(27 + indexOffsetResultMax, cYIm2);
    limb.set(28 + indexOffsetResultMax, cYIm1);
    limb.set(29 + indexOffsetResultMax, cYIm0);
    limb.set(30 + indexOffsetResultMax, cYRe3);
    limb.set(31 + indexOffsetResultMax, cYRe2);
    limb.set(32 + indexOffsetResultMax, cYRe1);
    limb.set(33 + indexOffsetResultMax, cYRe0);

    // TODO: set successBit and mextBit
  }

  private void handleBlsPairingCheck() {
    final int numberOfInputs = callData.size() / (SIZE_SMALL_POINT + SIZE_LARGE_POINT);
    for (int k = 0; k < numberOfInputs; k++) {
      final int sizeOffset = k * (SIZE_SMALL_POINT + SIZE_LARGE_POINT);
      final int indexOffset = k * (CT_MAX_SMALL_POINT + 1 + CT_MAX_LARGE_POINT + 1);

      // Extract inputs
      // Small point
      final Bytes aX3 = callData.slice(sizeOffset, LLARGE);
      final Bytes aX2 = callData.slice(LLARGE + sizeOffset, LLARGE);
      final Bytes aX1 = callData.slice(2 * LLARGE + sizeOffset, LLARGE);
      final Bytes aX0 = callData.slice(3 * LLARGE + sizeOffset, LLARGE);
      final Bytes aY3 = callData.slice(4 * LLARGE + sizeOffset, LLARGE);
      final Bytes aY2 = callData.slice(5 * LLARGE + sizeOffset, LLARGE);
      final Bytes aY1 = callData.slice(6 * LLARGE + sizeOffset, LLARGE);
      final Bytes aY0 = callData.slice(7 * LLARGE + sizeOffset, LLARGE);
      // Large point
      final Bytes bXIm3 = callData.slice(8 * LLARGE + sizeOffset, LLARGE);
      final Bytes bXIm2 = callData.slice(9 * LLARGE + sizeOffset, LLARGE);
      final Bytes bXIm1 = callData.slice(10 * LLARGE + sizeOffset, LLARGE);
      final Bytes bXIm0 = callData.slice(11 * LLARGE + sizeOffset, LLARGE);
      final Bytes bXRe3 = callData.slice(12 * LLARGE + sizeOffset, LLARGE);
      final Bytes bXRe2 = callData.slice(13 * LLARGE + sizeOffset, LLARGE);
      final Bytes bXRe1 = callData.slice(14 * LLARGE + sizeOffset, LLARGE);
      final Bytes bXRe0 = callData.slice(15 * LLARGE + sizeOffset, LLARGE);
      final Bytes bYIm3 = callData.slice(16 * LLARGE + sizeOffset, LLARGE);
      final Bytes bYIm2 = callData.slice(17 * LLARGE + sizeOffset, LLARGE);
      final Bytes bYIm1 = callData.slice(18 * LLARGE + sizeOffset, LLARGE);
      final Bytes bYIm0 = callData.slice(19 * LLARGE + sizeOffset, LLARGE);
      final Bytes bYRe3 = callData.slice(20 * LLARGE + sizeOffset, LLARGE);
      final Bytes bYRe2 = callData.slice(21 * LLARGE + sizeOffset, LLARGE);
      final Bytes bYRe1 = callData.slice(22 * LLARGE + sizeOffset, LLARGE);
      final Bytes bYRe0 = callData.slice(23 * LLARGE + sizeOffset, LLARGE);

      // Set input limb
      limb.set(indexOffset, aX3);
      limb.set(1 + indexOffset, aX2);
      limb.set(2 + indexOffset, aX1);
      limb.set(3 + indexOffset, aX0);
      limb.set(4 + indexOffset, aY3);
      limb.set(5 + indexOffset, aY2);
      limb.set(6 + indexOffset, aY1);
      limb.set(7 + indexOffset, aY0);
      limb.set(8 + indexOffset, bXIm3);
      limb.set(9 + indexOffset, bXIm2);
      limb.set(10 + indexOffset, bXIm1);
      limb.set(11 + indexOffset, bXIm0);
      limb.set(12 + indexOffset, bXRe3);
      limb.set(13 + indexOffset, bXRe2);
      limb.set(14 + indexOffset, bXRe1);
      limb.set(15 + indexOffset, bXRe0);
      limb.set(16 + indexOffset, bYIm3);
      limb.set(17 + indexOffset, bYIm2);
      limb.set(18 + indexOffset, bYIm1);
      limb.set(19 + indexOffset, bYIm0);
      limb.set(20 + indexOffset, bYRe3);
      limb.set(21 + indexOffset, bYRe2);
      limb.set(22 + indexOffset, bYRe1);
      limb.set(23 + indexOffset, bYRe0);

      wellFormedFpCoordinateAndInfinityCheck(indexOffset, aX3, aX2, aX1, aX0, aY3, aY2, aY1, aY0);

      wellFormedFp2CoordinateAndInfinityCheck(
          indexOffset,
          bXIm3,
          bXIm2,
          bXIm1,
          bXIm0,
          bXRe3,
          bXRe2,
          bXRe1,
          bXRe0,
          bYIm3,
          bYIm2,
          bYIm1,
          bYIm0,
          bYRe3,
          bYRe2,
          bYRe1,
          bYRe0);
    }

    EWord pairingResult = EWord.ZERO;

    if (returnData.toArray().length != 0) {
      checkArgument(returnData.toArray().length == WORD_SIZE);
      pairingResult = EWord.of(returnData.slice(0, WORD_SIZE));
    }

    final int indexOffsetResultMax =
        (numberOfInputs - 1) * (CT_MAX_SMALL_POINT + 1 + CT_MAX_LARGE_POINT + 1);

    // Set result limb
    limb.set(24 + indexOffsetResultMax, pairingResult.hi());
    limb.set(25 + indexOffsetResultMax, pairingResult.lo());

    // TODO: set successBit and mextBit
  }

  private void handleBlsMapFpToG1() {
    // Extract inputs
    final Bytes e3 = callData.slice(0, LLARGE);
    final Bytes e2 = callData.slice(LLARGE, LLARGE);
    final Bytes e1 = callData.slice(2 * LLARGE, LLARGE);
    final Bytes e0 = callData.slice(3 * LLARGE, LLARGE);

    // Set input limb
    limb.set(0, e3);
    limb.set(1, e2);
    limb.set(2, e1);
    limb.set(3, e0);

    final boolean eIsInRange = callToLTBlsPrime(0, e3, e2, e1, e0);

    final boolean internalChecksPassed = eIsInRange;

    for (int j = 0; j <= CT_MAX_MAP_FP2_TO_G2; j++) {
      this.mintBit.set(j, !internalChecksPassed);
    }

    Bytes cX3 = ZERO;
    Bytes cX2 = ZERO;
    Bytes cX1 = ZERO;
    Bytes cX0 = ZERO;
    Bytes cY3 = ZERO;
    Bytes cY2 = ZERO;
    Bytes cY1 = ZERO;
    Bytes cY0 = ZERO;

    if (returnData.toArray().length != 0) {
      checkArgument(returnData.toArray().length == SIZE_SMALL_POINT);
      cX3 = returnData.slice(0, LLARGE);
      cX2 = returnData.slice(LLARGE, LLARGE);
      cX1 = returnData.slice(2 * LLARGE, LLARGE);
      cX0 = returnData.slice(3 * LLARGE, LLARGE);
      cY3 = returnData.slice(4 * LLARGE, LLARGE);
      cY2 = returnData.slice(5 * LLARGE, LLARGE);
      cY1 = returnData.slice(6 * LLARGE, LLARGE);
      cY0 = returnData.slice(7 * LLARGE, LLARGE);
    }

    // Set result limb
    limb.set(4, cX3);
    limb.set(5, cX2);
    limb.set(6, cX1);
    limb.set(7, cX0);
    limb.set(8, cY3);
    limb.set(9, cY2);
    limb.set(10, cY1);
    limb.set(11, cY0);

    // TODO: set successBit and mextBit
  }

  private void handleBlsMapFp2ToG2() {
    // Extract inputs
    final Bytes eIm3 = callData.slice(0, LLARGE);
    final Bytes eIm2 = callData.slice(LLARGE, LLARGE);
    final Bytes eIm1 = callData.slice(2 * LLARGE, LLARGE);
    final Bytes eIm0 = callData.slice(3 * LLARGE, LLARGE);
    final Bytes eRe3 = callData.slice(4 * LLARGE, LLARGE);
    final Bytes eRe2 = callData.slice(5 * LLARGE, LLARGE);
    final Bytes eRe1 = callData.slice(6 * LLARGE, LLARGE);
    final Bytes eRe0 = callData.slice(7 * LLARGE, LLARGE);

    // Set input limb
    limb.set(0, eIm3);
    limb.set(1, eIm2);
    limb.set(2, eIm1);
    limb.set(3, eIm0);
    limb.set(4, eRe3);
    limb.set(5, eRe2);
    limb.set(6, eRe1);
    limb.set(7, eRe0);

    final boolean eImIsInRange = callToLTBlsPrime(0, eIm3, eIm2, eIm1, eIm0);

    final boolean eReIsInRange = callToLTBlsPrime(4, eRe3, eRe2, eRe1, eRe0);

    final boolean internalChecksPassed = eImIsInRange && eReIsInRange;

    for (int j = 0; j <= CT_MAX_MAP_FP2_TO_G2; j++) {
      this.mintBit.set(j, !internalChecksPassed);
    }

    Bytes cXIm3 = ZERO;
    Bytes cXIm2 = ZERO;
    Bytes cXIm1 = ZERO;
    Bytes cXIm0 = ZERO;
    Bytes cXRe3 = ZERO;
    Bytes cXRe2 = ZERO;
    Bytes cXRe1 = ZERO;
    Bytes cXRe0 = ZERO;
    Bytes cYIm3 = ZERO;
    Bytes cYIm2 = ZERO;
    Bytes cYIm1 = ZERO;
    Bytes cYIm0 = ZERO;
    Bytes cYRe3 = ZERO;
    Bytes cYRe2 = ZERO;
    Bytes cYRe1 = ZERO;
    Bytes cYRe0 = ZERO;

    if (returnData.toArray().length != 0) {
      checkArgument(returnData.toArray().length == SIZE_LARGE_POINT);
      cXIm3 = returnData.slice(0, LLARGE);
      cXIm2 = returnData.slice(LLARGE, LLARGE);
      cXIm1 = returnData.slice(2 * LLARGE, LLARGE);
      cXIm0 = returnData.slice(3 * LLARGE, LLARGE);
      cXRe3 = returnData.slice(4 * LLARGE, LLARGE);
      cXRe2 = returnData.slice(5 * LLARGE, LLARGE);
      cXRe1 = returnData.slice(6 * LLARGE, LLARGE);
      cXRe0 = returnData.slice(7 * LLARGE, LLARGE);
      cYIm3 = returnData.slice(8 * LLARGE, LLARGE);
      cYIm2 = returnData.slice(9 * LLARGE, LLARGE);
      cYIm1 = returnData.slice(10 * LLARGE, LLARGE);
      cYIm0 = returnData.slice(11 * LLARGE, LLARGE);
      cYRe3 = returnData.slice(12 * LLARGE, LLARGE);
      cYRe2 = returnData.slice(13 * LLARGE, LLARGE);
      cYRe1 = returnData.slice(14 * LLARGE, LLARGE);
      cYRe0 = returnData.slice(15 * LLARGE, LLARGE);
    }

    // Set result limb
    limb.set(8, cXIm3);
    limb.set(9, cXIm2);
    limb.set(10, cXIm1);
    limb.set(11, cXIm0);
    limb.set(12, cXRe3);
    limb.set(13, cXRe2);
    limb.set(14, cXRe1);
    limb.set(15, cXRe0);
    limb.set(16, cYIm3);
    limb.set(17, cYIm2);
    limb.set(18, cYIm1);
    limb.set(19, cYIm0);
    limb.set(20, cYRe3);
    limb.set(21, cYRe2);
    limb.set(22, cYRe1);
    limb.set(23, cYRe0);
  }

  private static short getPhase(
      PrecompileScenarioFragment.PrecompileFlag precompileFlag, boolean isData) {
    if (isData) {
      return switch (precompileFlag) {
        case PRC_POINT_EVALUATION -> PHASE_DATA_POINT_EVALUATION;
        case PRC_BLS_G1_ADD -> PHASE_DATA_G1_ADD;
        case PRC_BLS_G1_MSM -> PHASE_DATA_G1_MSM;
        case PRC_BLS_G2_ADD -> PHASE_DATA_G2_ADD;
        case PRC_BLS_G2_MSM -> PHASE_DATA_G2_MSM;
        case PRC_BLS_PAIRING_CHECK -> PHASE_DATA_PAIRING_CHECK;
        case PRC_BLS_MAP_FP_TO_G1 -> PHASE_DATA_MAP_FP_TO_G1;
        case PRC_BLS_MAP_FP2_TO_G2 -> PHASE_DATA_MAP_FP2_TO_G2;
        default -> throw new IllegalStateException("invalid BLS type");
      };
    } else {
      return switch (precompileFlag) {
        case PRC_POINT_EVALUATION -> PHASE_RSLT_POINT_EVALUATION;
        case PRC_BLS_G1_ADD -> PHASE_RSLT_G1_ADD;
        case PRC_BLS_G1_MSM -> PHASE_RSLT_G1_MSM;
        case PRC_BLS_G2_ADD -> PHASE_RSLT_G2_ADD;
        case PRC_BLS_G2_MSM -> PHASE_RSLT_G2_MSM;
        case PRC_BLS_PAIRING_CHECK -> PHASE_RSLT_PAIRING_CHECK;
        case PRC_BLS_MAP_FP_TO_G1 -> PHASE_RSLT_MAP_FP_TO_G1;
        case PRC_BLS_MAP_FP2_TO_G2 -> PHASE_RSLT_MAP_FP2_TO_G2;
        default -> throw new IllegalStateException("invalid BLS type");
      };
    }
  }

  private int getIndexMax(
      PrecompileScenarioFragment.PrecompileFlag precompileFlag, boolean isData) {
    if (isData) {
      return switch (precompileFlag) {
        case PRC_POINT_EVALUATION -> INDEX_MAX_DATA_POINT_EVALUATION;
        case PRC_BLS_G1_ADD -> INDEX_MAX_DATA_G1_ADD;
        case PRC_BLS_G2_ADD -> INDEX_MAX_DATA_G2_ADD;
        case PRC_BLS_MAP_FP_TO_G1 -> INDEX_MAX_DATA_MAP_FP_TO_G1;
        case PRC_BLS_MAP_FP2_TO_G2 -> INDEX_MAX_DATA_MAP_FP2_TO_G2;
        case PRC_BLS_G1_MSM, PRC_BLS_G2_MSM, PRC_BLS_PAIRING_CHECK -> totalSizeData / 16 - 1;
        default -> throw new IllegalStateException("invalid BLS type");
      };
    } else {
      return switch (precompileFlag) {
        case PRC_POINT_EVALUATION -> INDEX_MAX_RSLT_POINT_EVALUATION;
        case PRC_BLS_G1_ADD -> INDEX_MAX_RSLT_G1_ADD;
        case PRC_BLS_G1_MSM -> INDEX_MAX_RSLT_G1_MSM;
        case PRC_BLS_G2_ADD -> INDEX_MAX_RSLT_G2_ADD;
        case PRC_BLS_G2_MSM -> INDEX_MAX_RSLT_G2_MSM;
        case PRC_BLS_PAIRING_CHECK -> INDEX_MAX_RSLT_PAIRING_CHECK;
        case PRC_BLS_MAP_FP_TO_G1 -> INDEX_MAX_RSLT_MAP_FP_TO_G1;
        case PRC_BLS_MAP_FP2_TO_G2 -> INDEX_MAX_RSLT_MAP_FP2_TO_G2;
        default -> throw new IllegalStateException("invalid BLS type");
      };
    }
  }

  // Utilities
  private boolean wcpCallTo(int i, OpCode wcpInst, EWord arg1, EWord arg2) {
    final boolean wcpRes =
        switch (wcpInst) {
          case LT -> wcp.callLT(arg1, arg2);
          case EQ -> wcp.callEQ(arg1, arg2);
          default -> throw new IllegalStateException("Unexpected value: " + wcpInst);
        };

    wcpFlag.set(i, true);
    wcpArg1Hi.set(i, arg1.hi());
    wcpArg1Lo.set(i, arg1.lo());
    wcpArg2Hi.set(i, arg2.hi());
    wcpArg2Lo.set(i, arg2.lo());
    this.wcpRes.set(i, wcpRes);
    this.wcpInst.set(i, wcpInst);
    return wcpRes;
  }

  private boolean wcpCallToLT(int i, EWord arg1, EWord arg2) {
    return wcpCallTo(i, OpCode.LT, arg1, arg2);
  }

  private boolean wcpCallToEQ(int i, EWord arg1, EWord arg2) {
    return wcpCallTo(i, OpCode.EQ, arg1, arg2);
  }

  private boolean wcpGeneralizedCallToLT(
      int i, Bytes a, Bytes b, Bytes c, Bytes d, Bytes e, Bytes f, Bytes g, Bytes h) {
    // First argument: a, b, c, d
    // Second argument: e, f, g, h
    wcpCallToLT(i + 1, EWord.of(a, b), EWord.of(e, f));
    wcpCallToEQ(i + 2, EWord.of(a, b), EWord.of(e, f));
    wcpCallToLT(i + 3, EWord.of(c, d), EWord.of(g, h));

    final boolean wcpRes =
        this.wcpRes.get(i + 1) || (this.wcpRes.get(i + 2) && this.wcpRes.get(i + 3));
    this.wcpRes.set(i, wcpRes); // TODO: do we want to set other WCP columns here?

    return wcpRes;
  }

  // This is defined here for convenience, but not appearing in the specs
  private boolean callToLTBlsPrime(int i, Bytes p3, Bytes p2, Bytes p1, Bytes p0) {
    return wcpGeneralizedCallToLT(
        i,
        p3,
        p2,
        p1,
        p0,
        Bytes.ofUnsignedShort(BLS_PRIME_3),
        bigIntegerToBytes(BLS_PRIME_2),
        bigIntegerToBytes(BLS_PRIME_1),
        bigIntegerToBytes(BLS_PRIME_0));
  }

  private void wellFormedFpCoordinateAndInfinityCheck(
      int i,
      Bytes pX3,
      Bytes pX2,
      Bytes pX1,
      Bytes pX0,
      Bytes pY3,
      Bytes pY2,
      Bytes pY1,
      Bytes pY0) {
    final boolean pXIsInRange = callToLTBlsPrime(i, pX3, pX2, pX1, pX0);

    final boolean pYIsInRange = callToLTBlsPrime(i + 4, pY3, pY2, pY1, pY0);

    final boolean wellFormedCoordinate = pXIsInRange && pYIsInRange;

    for (int j = 0; j <= CT_MAX_SMALL_POINT; j++) {
      this.mintBit.set(i + j, !wellFormedCoordinate);
    }

    isInfinity(i, pX3, pX2, pX1, pX0, pY3, pY2, pY1, pY0);
  }

  private void wellFormedFp2CoordinateAndInfinityCheck(
      int i,
      Bytes pXIm3,
      Bytes pXIm2,
      Bytes pXIm1,
      Bytes pXIm0,
      Bytes pXRe3,
      Bytes pXRe2,
      Bytes pXRe1,
      Bytes pXRe0,
      Bytes pYIm3,
      Bytes pYIm2,
      Bytes pYIm1,
      Bytes pYIm0,
      Bytes pYRe3,
      Bytes pYRe2,
      Bytes pYRe1,
      Bytes pYRe0) {

    final boolean pXImIsInRange = callToLTBlsPrime(i, pXIm3, pXIm2, pXIm1, pXIm0);

    final boolean pXReIsInRange = callToLTBlsPrime(i + 4, pXRe3, pXRe2, pXRe1, pXRe0);

    final boolean pYImIsInRange = callToLTBlsPrime(i + 8, pYIm3, pYIm2, pYIm1, pYIm0);

    final boolean pYReIsInRange = callToLTBlsPrime(i + 12, pYRe3, pYRe2, pYRe1, pYRe0);

    final boolean wellFormedCoordinate =
        pXImIsInRange && pXReIsInRange && pYImIsInRange && pYReIsInRange;

    for (int j = 0; j <= CT_MAX_LARGE_POINT; j++) {
      this.mintBit.set(i + j, !wellFormedCoordinate);
    }

    isInfinity(
        i, pXIm3, pXIm2, pXIm1, pXIm0, pXRe3, pXRe2, pXRe1, pXRe0, pYIm3, pYIm2, pYIm1, pYIm0,
        pYRe3, pYRe2, pYRe1, pYRe0);
  }

  // Note: in the specs isInfinity receives directly the sum of the coordinate
  private void isInfinity(int i, Bytes... coordinate) {
    BigInteger coordinateSum =
        Arrays.stream(coordinate).map(Bytes::toBigInteger).reduce(BigInteger.ZERO, BigInteger::add);

    // Check if the sum of coordinates is zero, i.e., the point is at infinity
    final boolean isInfinity = coordinateSum.signum() == 0;

    // Set the isInfinity flag for all coordinates
    for (int j = 0; j < coordinate.length; j++) {
      this.isInfinity.set(i, isInfinity);
    }
  }

  private int getCtMax(
      PrecompileScenarioFragment.PrecompileFlag precompileFlag,
      boolean isData,
      boolean isFirstInput) {
    if (isData) {
      return switch (precompileFlag) {
        case PRC_POINT_EVALUATION -> isFirstInput ? CT_MAX_POINT_EVALUATION : 0;
        case PRC_BLS_G1_ADD -> CT_MAX_SMALL_POINT;
        case PRC_BLS_G1_MSM -> isFirstInput ? CT_MAX_SMALL_POINT : CT_MAX_SCALAR;
        case PRC_BLS_G2_ADD -> CT_MAX_LARGE_POINT;
        case PRC_BLS_G2_MSM -> isFirstInput ? CT_MAX_LARGE_POINT : CT_MAX_SCALAR;
        case PRC_BLS_PAIRING_CHECK -> isFirstInput ? CT_MAX_SMALL_POINT : CT_MAX_LARGE_POINT;
        case PRC_BLS_MAP_FP_TO_G1 -> isFirstInput ? CT_MAX_MAP_FP_TO_G1 : 0;
        case PRC_BLS_MAP_FP2_TO_G2 -> isFirstInput ? CT_MAX_MAP_FP2_TO_G2 : 0;
        default -> throw new IllegalStateException("invalid BLS type");
      };
    } else {
      return getIndexMax(precompileFlag, false);
    }
  }

  void trace(Trace.Bls trace, final int stamp, final long previousId) {
    final Bytes deltaByte =
        leftPadTo(Bytes.minimalBytes(id - previousId - 1), nBYTES_OF_DELTA_BYTES);

    int ct = 0;
    boolean isFirstInput = true;

    /*
    Examples:

    nRowsData = 6
    ct = 0, ctMaxFirstInput = 3, ctMaxSecondInput = 1
          | ct | ctMax | isFirstInput |
          -----------------------------
    i = 0 | 0  | 3     | true         |
    i = 1 | 1  | 3     | true         |
    i = 2 | 2  | 3     | true         |
    i = 3 | 3  | 3     | true         |
    i = 4 | 0  | 1     | false        |
    i = 5 | 1  | 1     | false        |

    nRowsData = 4
    ct = 0, ctMaxFirstInput = 3, ctMaxSecondInput = 0
          | ct | ctMax | isFirstInput |
          -----------------------------
    i = 0 | 0  | 3     | true         |
    i = 1 | 1  | 3     | true         |
    i = 2 | 2  | 3     | true         |
    i = 3 | 3  | 3     | true         |
     */

    for (int i = 0; i < nRows; i++) {
      boolean isData = i < nRowsData;
      // TODO: fill missing fields
      final int ctMax = getCtMax(precompileFlag, isData, isFirstInput);
      if (ctMax != 0) {
        // Transition from first input to second input or vice versa only if there are multiple
        // inputs
        isFirstInput = !isFirstInput;
      }

      trace
          .stamp(stamp)
          .id(id)
          .totalSize(isData ? totalSizeData : totalSizeResult)
          .index(isData ? i : i - nRowsData)
          .indexMax(getIndexMax(precompileFlag, isData))
          .phase(getPhase(precompileFlag, isData))
          .limb(limb.get(i))
          .successBit(successBit)
          .ct(ct)
          .ctMax(ctMax)
          .dataPointEvaluationFlag(precompileFlag == PRC_POINT_EVALUATION && isData)
          .dataBlsG1AddFlag(precompileFlag == PRC_BLS_G1_ADD && isData)
          .dataBlsG1MsmFlag(precompileFlag == PRC_BLS_G1_MSM && isData)
          .dataBlsG2AddFlag(precompileFlag == PRC_BLS_G2_ADD && isData)
          .dataBlsG2MsmFlag(precompileFlag == PRC_BLS_G2_MSM && isData)
          .dataBlsPairingCheckFlag(precompileFlag == PRC_BLS_PAIRING_CHECK && isData)
          .dataBlsMapFpToG1Flag(precompileFlag == PRC_BLS_MAP_FP_TO_G1 && isData)
          .dataBlsMapFp2ToG2Flag(precompileFlag == PRC_BLS_MAP_FP2_TO_G2 && isData)
          .rsltPointEvaluationFlag(precompileFlag == PRC_POINT_EVALUATION && !isData)
          .rsltBlsG1AddFlag(precompileFlag == PRC_BLS_G1_ADD && !isData)
          .rsltBlsG1MsmFlag(precompileFlag == PRC_BLS_G1_MSM && !isData)
          .rsltBlsG2AddFlag(precompileFlag == PRC_BLS_G2_ADD && !isData)
          .rsltBlsG2MsmFlag(precompileFlag == PRC_BLS_G2_MSM && !isData)
          .rsltBlsPairingCheckFlag(precompileFlag == PRC_BLS_PAIRING_CHECK && !isData)
          .rsltBlsMapFpToG1Flag(precompileFlag == PRC_BLS_MAP_FP_TO_G1 && !isData)
          .rsltBlsMapFp2ToG2Flag(precompileFlag == PRC_BLS_MAP_FP2_TO_G2 && !isData)
          .accInputs(0)
          .byteDelta(
              i < nBYTES_OF_DELTA_BYTES ? UnsignedByte.of(deltaByte.get(i)) : UnsignedByte.of(0))
          .malformedDataInternalBit(false)
          .malformedDataInternalAcc(false)
          .malformedDataInternalAccTot(false)
          .malformedDataExternalBit(false)
          .malformedDataExternalAcc(false)
          .malformedDataExternalAccTot(false)
          .wellformedDataTrivial(false)
          .wellformedDataNontrivial(false)
          .isFirstInput(isFirstInput && isData)
          .isSecondInput(!isFirstInput && isData)
          .isInfinity(false)
          .nontrivialPairOfPointsBit(false)
          .nontrivialPairOfPointsAcc(false)
          .circuitSelectorPointEvaluation(false)
          .circuitSelectorPointEvaluationFailure(false)
          .circuitSelectorC1Membership(false)
          .circuitSelectorG1Membership(false)
          .circuitSelectorC2Membership(false)
          .circuitSelectorG2Membership(false)
          .circuitSelectorBlsPairingCheck(false)
          .circuitSelectorBlsG1Add(false)
          .circuitSelectorBlsG2Add(false)
          .circuitSelectorBlsG1Msm(false)
          .circuitSelectorBlsG2Msm(false)
          .circuitSelectorBlsMapFpToG1(false)
          .circuitSelectorBlsMapFp2ToG2(false)
          .wcpFlag(wcpFlag.get(i))
          .wcpArg1Hi(wcpArg1Hi.get(i))
          .wcpArg1Lo(wcpArg1Lo.get(i))
          .wcpArg2Hi(wcpArg2Hi.get(i))
          .wcpArg2Lo(wcpArg2Lo.get(i))
          .wcpRes(wcpRes.get(i))
          .wcpInst(wcpInst.get(i).unsignedByteValue())
          .validateRow();

      // Increment ct up to ctMax, then reset to 0
      if (ct < ctMax) {
        ct++;
      } else {
        ct = 0;
      }
    }
  }

  @Override
  protected int computeLineCount() {
    return nRowsData + nRowsResult;
  }
}
