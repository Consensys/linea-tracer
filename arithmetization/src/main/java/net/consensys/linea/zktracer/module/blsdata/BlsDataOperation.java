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

package net.consensys.linea.zktracer.module.blsdata;

import static com.google.common.base.Preconditions.checkArgument;
import static net.consensys.linea.zktracer.Trace.LLARGE;
import static net.consensys.linea.zktracer.Trace.WORD_SIZE;
import static net.consensys.linea.zktracer.TraceCancun.Bls.BLS_PRIME_0;
import static net.consensys.linea.zktracer.TraceCancun.Bls.BLS_PRIME_1;
import static net.consensys.linea.zktracer.TraceCancun.Bls.BLS_PRIME_2;
import static net.consensys.linea.zktracer.TraceCancun.Bls.BLS_PRIME_3;
import static net.consensys.linea.zktracer.TraceCancun.Bls.CT_MAX_LARGE_POINT;
import static net.consensys.linea.zktracer.TraceCancun.Bls.CT_MAX_MAP_FP2_TO_G2;
import static net.consensys.linea.zktracer.TraceCancun.Bls.CT_MAX_MAP_FP_TO_G1;
import static net.consensys.linea.zktracer.TraceCancun.Bls.CT_MAX_POINT_EVALUATION;
import static net.consensys.linea.zktracer.TraceCancun.Bls.CT_MAX_SCALAR;
import static net.consensys.linea.zktracer.TraceCancun.Bls.CT_MAX_SMALL_POINT;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_DATA_G1_ADD;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_DATA_G1_MSM_MIN;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_DATA_G2_ADD;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_DATA_G2_MSM_MIN;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_DATA_MAP_FP2_TO_G2;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_DATA_MAP_FP_TO_G1;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_DATA_PAIRING_CHECK_MIN;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_DATA_POINT_EVALUATION;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_RSLT_G1_ADD;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_RSLT_G1_MSM;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_RSLT_G2_ADD;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_RSLT_G2_MSM;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_RSLT_MAP_FP2_TO_G2;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_RSLT_MAP_FP_TO_G1;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_RSLT_PAIRING_CHECK;
import static net.consensys.linea.zktracer.TraceCancun.Bls.INDEX_MAX_RSLT_POINT_EVALUATION;
import static net.consensys.linea.zktracer.TraceCancun.Bls.POINT_EVALUATION_PRIME_HI;
import static net.consensys.linea.zktracer.TraceCancun.Bls.POINT_EVALUATION_PRIME_LO;
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

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.Bytes16;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.nativelib.gnark.LibGnarkEIP2537;

@Accessors(fluent = true)
public class BlsDataOperation extends ModuleOperation {
  public static final BigInteger BLS_PRIME =
      Bytes.concatenate(
              Bytes.ofUnsignedShort(BLS_PRIME_3),
              bigIntegerToBytes(BLS_PRIME_2),
              bigIntegerToBytes(BLS_PRIME_1),
              bigIntegerToBytes(BLS_PRIME_0))
          .toUnsignedBigInteger();

  static final EWord POINT_EVALUATION_PRIME =
      EWord.of(POINT_EVALUATION_PRIME_HI, POINT_EVALUATION_PRIME_LO);

  public static final int nBYTES_OF_DELTA_BYTES = 4;
  private static final int SIZE_SMALL_POINT = LLARGE * (CT_MAX_SMALL_POINT + 1);
  private static final int SIZE_LARGE_POINT = LLARGE * (CT_MAX_LARGE_POINT + 1);
  private static final int SIZE_SCALAR = LLARGE * (CT_MAX_SCALAR + 1);

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
  private final boolean successBit;

  private final List<Boolean> mintBit;
  private final List<Boolean> mextBit;
  private final List<Boolean> isInfinity;
  private final List<Boolean> nontrivialPairOfPointsBit;

  @Getter private boolean mint;
  @Getter private boolean mext;
  @Getter private boolean wtrv;
  @Getter private boolean wnon;
  @Getter private boolean firstPointNotInSubgroupIsSmall;
  @Getter private int nontrivialPopCounter;
  @Getter private int trivialPopDueToG2PointCounter; // Counting trivial pairs of the form (P,inf)
  @Getter private int trivialPopDueToG1PointCounter; // Counting trivial pairs of the form (inf,Q)

  // WCP interaction
  private final List<Boolean> wcpFlag;
  private final List<Bytes> wcpArg1Hi;
  private final List<Bytes> wcpArg1Lo;
  private final List<Bytes> wcpArg2Hi;
  private final List<Bytes> wcpArg2Lo;
  private final List<Boolean> wcpRes;
  private final List<OpCode> wcpInst;

  private BlsDataOperation(
      Wcp wcp,
      int id,
      final PrecompileScenarioFragment.PrecompileFlag precompileFlag,
      Bytes callData,
      Bytes returnData,
      boolean successBit) {
    checkArgument(precompileFlag.isBlsPrecompile(), "invalid BLS type");

    this.precompileFlag = precompileFlag;
    this.callData = callData;
    totalSizeData = callData.size();
    totalSizeResult = returnData.size();

    nRowsData = getIndexMax(precompileFlag, true) + 1;
    nRowsResult = getIndexMax(precompileFlag, false) + 1;
    nRows = nRowsData + nRowsResult;
    this.id = id;

    mintBit = repeat(false, nRows);
    mextBit = repeat(false, nRows);
    isInfinity = repeat(false, nRows);
    nontrivialPairOfPointsBit = repeat(false, nRows);

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

    // Set successBit
    this.successBit = successBit;
    final int returnDataSize = returnData.toArray().length;
    Preconditions.checkArgument(
        returnDataSize == (successBit ? expectedReturnDataSize(precompileFlag) : 0));
  }

  public static int expectedReturnDataSize(
      final PrecompileScenarioFragment.PrecompileFlag precompileFlag) {
    return switch (precompileFlag) {
          case PRC_POINT_EVALUATION -> INDEX_MAX_RSLT_POINT_EVALUATION + 1;
          case PRC_BLS_G1_ADD -> INDEX_MAX_RSLT_G1_ADD + 1;
          case PRC_BLS_G1_MSM -> INDEX_MAX_RSLT_G1_MSM + 1;
          case PRC_BLS_G2_ADD -> INDEX_MAX_RSLT_G2_ADD + 1;
          case PRC_BLS_G2_MSM -> INDEX_MAX_RSLT_G2_MSM + 1;
          case PRC_BLS_PAIRING_CHECK -> INDEX_MAX_RSLT_PAIRING_CHECK + 1;
          case PRC_BLS_MAP_FP_TO_G1 -> INDEX_MAX_RSLT_MAP_FP_TO_G1 + 1;
          case PRC_BLS_MAP_FP2_TO_G2 -> INDEX_MAX_RSLT_MAP_FP2_TO_G2 + 1;
          default -> throw new IllegalStateException("Unexpected value: " + precompileFlag);
        }
        * 16;
  }

  public static BlsDataOperation of(
      Wcp wcp,
      int id,
      final PrecompileScenarioFragment.PrecompileFlag precompileFlag,
      Bytes callData,
      Bytes returnData,
      boolean successBit) {
    BlsDataOperation blsDataOperation =
        new BlsDataOperation(wcp, id, precompileFlag, callData, returnData, successBit);
    switch (precompileFlag) {
      case PRC_POINT_EVALUATION -> blsDataOperation.handlePointEvaluation();
      case PRC_BLS_G1_ADD -> blsDataOperation.handleBlsG1Add();
      case PRC_BLS_G1_MSM -> blsDataOperation.handleBlsG1Msm();
      case PRC_BLS_G2_ADD -> blsDataOperation.handleBlsG2Add();
      case PRC_BLS_G2_MSM -> blsDataOperation.handleBlsG2Msm();
      case PRC_BLS_PAIRING_CHECK -> blsDataOperation.handleBlsPairingCheck();
      case PRC_BLS_MAP_FP_TO_G1 -> blsDataOperation.handleBlsMapFpToG1();
      case PRC_BLS_MAP_FP2_TO_G2 -> blsDataOperation.handleBlsMapFp2ToG2();
      default -> throw new IllegalArgumentException(
          "BlsOperation expects to be called on a bls precompile, not on " + precompileFlag.name());
    }
    blsDataOperation.handleGlobalColumns();
    return blsDataOperation;
  }

  private void handleGlobalColumns() {
    mint = mintBit.stream().reduce(false, Boolean::logicalOr);
    mext = mextBit.stream().reduce(false, Boolean::logicalOr);
    final boolean nonTrivialPairOfPointsTot =
        nontrivialPairOfPointsBit.stream().reduce(false, Boolean::logicalOr);
    wtrv = !mint && !mext && precompileFlag == PRC_BLS_PAIRING_CHECK && !nonTrivialPairOfPointsTot;
    wnon = !mint && !mext && (precompileFlag != PRC_BLS_PAIRING_CHECK || nonTrivialPairOfPointsTot);
  }

  private void handlePointEvaluation() {
    // Extract inputs
    final EWord z = EWord.of(callData.slice(WORD_SIZE, WORD_SIZE));
    final EWord y = EWord.of(callData.slice(2 * WORD_SIZE, WORD_SIZE));

    final boolean zIsInRange = wcpCallToLT(0, z, POINT_EVALUATION_PRIME);

    final boolean yIsInRange = wcpCallToLT(1, y, POINT_EVALUATION_PRIME);

    final boolean internalChecksPassed = zIsInRange && yIsInRange;

    final boolean mextBit = internalChecksPassed && !successBit;

    for (int j = 0; j <= CT_MAX_POINT_EVALUATION; j++) {
      this.mintBit.set(j, !internalChecksPassed);
      this.mextBit.set(j, mextBit);
    }
  }

  private void handleBlsG1Add() {
    boolean mextBitIsSet = false;

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

      final boolean wellFormedCoordinate =
          wellFormedFpCoordinate(indexOffset, aX3, aX2, aX1, aX0, aY3, aY2, aY1, aY0);
      final boolean isSmallPointOnCurve =
          isSmallPointOnCurve(indexOffset, aX3, aX2, aX1, aX0, aY3, aY2, aY1, aY0);
      final boolean mextBit = wellFormedCoordinate && !isSmallPointOnCurve;
      Preconditions.checkArgument(mextBit == (wellFormedCoordinate && !successBit));

      if (mextBit && !mextBitIsSet) {
        for (int j = 0; j <= CT_MAX_SMALL_POINT; j++) {
          this.mextBit.set(indexOffset + j, true);
        }
        mextBitIsSet = true;
      }
    }
  }

  private void handleBlsG1Msm() {
    boolean mextBitIsSet = false;

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

      final boolean wellFormedCoordinate =
          wellFormedFpCoordinate(indexOffset, aX3, aX2, aX1, aX0, aY3, aY2, aY1, aY0);
      final boolean isSmallPointInSubgroup =
          isSmallPointInSubGroup(indexOffset, aX3, aX2, aX1, aX0, aY3, aY2, aY1, aY0);
      final boolean mextBit = wellFormedCoordinate && !isSmallPointInSubgroup;
      Preconditions.checkArgument(mextBit == (wellFormedCoordinate && !successBit));

      if (mextBit && !mextBitIsSet) {
        for (int j = 0; j <= CT_MAX_SMALL_POINT; j++) {
          this.mextBit.set(indexOffset + j, true);
        }
        mextBitIsSet = true;
      }
    }
  }

  private void handleBlsG2Add() {
    boolean mextBitIsSet = false;

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

      final boolean wellFormedCoordinate =
          wellFormedFp2Coordinate(
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
      final boolean isLargePointOnCurve =
          isLargePointOnCurve(
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
      final boolean mextBit = wellFormedCoordinate && !isLargePointOnCurve;
      Preconditions.checkArgument(mextBit == (wellFormedCoordinate && !successBit));

      if (mextBit && !mextBitIsSet) {
        for (int j = 0; j <= CT_MAX_LARGE_POINT; j++) {
          this.mextBit.set(indexOffset + j, true);
        }
        mextBitIsSet = true;
      }
    }
  }

  private void handleBlsG2Msm() {
    boolean mextBitIsSet = false;

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

      final boolean wellFormedCoordinate =
          wellFormedFp2Coordinate(
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
      final boolean isLargePointInSubgroup =
          isLargePointInSubGroup(
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
      final boolean mextBit = wellFormedCoordinate && !isLargePointInSubgroup;
      Preconditions.checkArgument(mextBit == (wellFormedCoordinate && !successBit));

      if (mextBit && !mextBitIsSet) {
        for (int j = 0; j <= CT_MAX_LARGE_POINT; j++) {
          this.mextBit.set(indexOffset + j, true);
        }
        mextBitIsSet = true;
      }
    }
  }

  private void handleBlsPairingCheck() {
    boolean mextBitIsSet = false;

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

      final boolean wellFormedFpCoordinate =
          wellFormedFpCoordinate(indexOffset, aX3, aX2, aX1, aX0, aY3, aY2, aY1, aY0);
      final boolean isSmallPointInSubgroup =
          isSmallPointInSubGroup(indexOffset, aX3, aX2, aX1, aX0, aY3, aY2, aY1, aY0);
      final boolean mextBitSmall = wellFormedFpCoordinate && !isSmallPointInSubgroup;
      Preconditions.checkArgument(mextBitSmall == (wellFormedFpCoordinate && !successBit));

      if (mextBitSmall && !mextBitIsSet) {
        for (int j = 0; j <= CT_MAX_SMALL_POINT; j++) {
          this.mextBit.set(indexOffset + j, true);
        }
        mextBitIsSet = true;
        firstPointNotInSubgroupIsSmall = true;
      }

      final boolean wellFormedFp2Coordinate =
          wellFormedFp2Coordinate(
              8 + indexOffset,
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
      final boolean isLargePointInSubgroup =
          isLargePointInSubGroup(
              8 + indexOffset,
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
      final boolean mextBitLarge = wellFormedFp2Coordinate && !isLargePointInSubgroup;
      Preconditions.checkArgument(mextBitLarge == (wellFormedFp2Coordinate && !successBit));

      if (mextBitLarge && !mextBitIsSet) {
        for (int j = 0; j <= CT_MAX_LARGE_POINT; j++) {
          this.mextBit.set(8 + indexOffset + j, true);
        }
        mextBitIsSet = true;
      }

      final boolean smallPointIsAtInfinity = isInfinity.get(indexOffset);
      final boolean largePointIsAtInfinity = isInfinity.get(8 + indexOffset);
      final boolean pairOfPointsNonTrivialBit = !smallPointIsAtInfinity && !largePointIsAtInfinity;
      if (pairOfPointsNonTrivialBit) {
        nontrivialPopCounter++;
      }
      if (!smallPointIsAtInfinity && largePointIsAtInfinity) {
        trivialPopDueToG2PointCounter++;
      }
      if (smallPointIsAtInfinity && !largePointIsAtInfinity) {
        trivialPopDueToG1PointCounter++;
      }
      for (int j = indexOffset; j < 24 + indexOffset; j++) {
        this.nontrivialPairOfPointsBit.set(j, pairOfPointsNonTrivialBit);
      }
    }
  }

  private void handleBlsMapFpToG1() {
    boolean mextBitIsSet = false;

    // Extract inputs
    final Bytes e3 = callData.slice(0, LLARGE);
    final Bytes e2 = callData.slice(LLARGE, LLARGE);
    final Bytes e1 = callData.slice(2 * LLARGE, LLARGE);
    final Bytes e0 = callData.slice(3 * LLARGE, LLARGE);

    final boolean eIsInRange = callToLTBlsPrime(0, e3, e2, e1, e0);

    final boolean internalChecksPassed = eIsInRange;

    for (int j = 0; j <= CT_MAX_MAP_FP_TO_G1; j++) {
      this.mintBit.set(j, !internalChecksPassed);
    }
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

    final boolean eImIsInRange = callToLTBlsPrime(0, eIm3, eIm2, eIm1, eIm0);

    final boolean eReIsInRange = callToLTBlsPrime(4, eRe3, eRe2, eRe1, eRe0);

    final boolean internalChecksPassed = eImIsInRange && eReIsInRange;

    for (int j = 0; j <= CT_MAX_MAP_FP2_TO_G2; j++) {
      this.mintBit.set(j, !internalChecksPassed);
    }
  }

  private boolean isSmallPointOnCurve(
      int i,
      Bytes pX3,
      Bytes pX2,
      Bytes pX1,
      Bytes pX0,
      Bytes pY3,
      Bytes pY2,
      Bytes pY1,
      Bytes pY0) {
    final boolean isInfinity = isInfinity(i, pX3, pX2, pX1, pX0, pY3, pY2, pY1, pY0);
    if (isInfinity) {
      return true;
    }

    byte[] input =
        Bytes.concatenate(
                Bytes16.leftPad(pX3),
                Bytes16.leftPad(pX2),
                Bytes16.leftPad(pX1),
                Bytes16.leftPad(pX0),
                Bytes16.leftPad(pY3),
                Bytes16.leftPad(pY2),
                Bytes16.leftPad(pY1),
                Bytes16.leftPad(pY0))
            .toArray();
    byte[] error = new byte[256];
    return LibGnarkEIP2537.eip2537G1IsOnCurve(input, error, input.length, error.length);
  }

  // Note: this checks also if the point is on curve
  private boolean isSmallPointInSubGroup(
      int i,
      Bytes pX3,
      Bytes pX2,
      Bytes pX1,
      Bytes pX0,
      Bytes pY3,
      Bytes pY2,
      Bytes pY1,
      Bytes pY0) {
    final boolean isOnCurve = isSmallPointOnCurve(i, pX3, pX2, pX1, pX0, pY3, pY2, pY1, pY0);
    if (!isOnCurve) {
      return false;
    }

    byte[] input =
        Bytes.concatenate(
                Bytes16.leftPad(pX3),
                Bytes16.leftPad(pX2),
                Bytes16.leftPad(pX1),
                Bytes16.leftPad(pX0),
                Bytes16.leftPad(pY3),
                Bytes16.leftPad(pY2),
                Bytes16.leftPad(pY1),
                Bytes16.leftPad(pY0))
            .toArray();
    byte[] error = new byte[256];
    return LibGnarkEIP2537.eip2537G1IsInSubGroup(input, error, input.length, error.length);
  }

  private boolean isLargePointOnCurve(
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
    final boolean isInfinity =
        isInfinity(
            i, pXIm3, pXIm2, pXIm1, pXIm0, pXRe3, pXRe2, pXRe1, pXRe0, pYIm3, pYIm2, pYIm1, pYIm0,
            pYRe3, pYRe2, pYRe1, pYRe0);
    if (isInfinity) {
      return true;
    }

    byte[] input =
        Bytes.concatenate(
                Bytes16.leftPad(pXIm3),
                Bytes16.leftPad(pXIm2),
                Bytes16.leftPad(pXIm1),
                Bytes16.leftPad(pXIm0),
                Bytes16.leftPad(pXRe3),
                Bytes16.leftPad(pXRe2),
                Bytes16.leftPad(pXRe1),
                Bytes16.leftPad(pXRe0),
                Bytes16.leftPad(pYIm3),
                Bytes16.leftPad(pYIm2),
                Bytes16.leftPad(pYIm1),
                Bytes16.leftPad(pYIm0),
                Bytes16.leftPad(pYRe3),
                Bytes16.leftPad(pYRe2),
                Bytes16.leftPad(pYRe1),
                Bytes16.leftPad(pYRe0))
            .toArray();
    byte[] error = new byte[256];
    return LibGnarkEIP2537.eip2537G2IsOnCurve(input, error, input.length, error.length);
  }

  // Note: this checks also if the point is on curve
  private boolean isLargePointInSubGroup(
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
    final boolean isOnCurve =
        isLargePointOnCurve(
            i, pXIm3, pXIm2, pXIm1, pXIm0, pXRe3, pXRe2, pXRe1, pXRe0, pYIm3, pYIm2, pYIm1, pYIm0,
            pYRe3, pYRe2, pYRe1, pYRe0);
    if (!isOnCurve) {
      return false;
    }

    byte[] input =
        Bytes.concatenate(
                Bytes16.leftPad(pXIm3),
                Bytes16.leftPad(pXIm2),
                Bytes16.leftPad(pXIm1),
                Bytes16.leftPad(pXIm0),
                Bytes16.leftPad(pXRe3),
                Bytes16.leftPad(pXRe2),
                Bytes16.leftPad(pXRe1),
                Bytes16.leftPad(pXRe0),
                Bytes16.leftPad(pYIm3),
                Bytes16.leftPad(pYIm2),
                Bytes16.leftPad(pYIm1),
                Bytes16.leftPad(pYIm0),
                Bytes16.leftPad(pYRe3),
                Bytes16.leftPad(pYRe2),
                Bytes16.leftPad(pYRe1),
                Bytes16.leftPad(pYRe0))
            .toArray();
    byte[] error = new byte[256];
    return LibGnarkEIP2537.eip2537G2IsInSubGroup(input, error, input.length, error.length);
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

  private boolean wellFormedFpCoordinate(
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

    return wellFormedCoordinate;
  }

  private boolean wellFormedFp2Coordinate(
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

    return wellFormedCoordinate;
  }

  // Note: in the specs isInfinity receives directly the sum of the coordinate
  private boolean isInfinity(int i, Bytes... coordinate) {
    BigInteger coordinateSum =
        Arrays.stream(coordinate).map(Bytes::toBigInteger).reduce(BigInteger.ZERO, BigInteger::add);

    // Check if the sum of coordinates is zero, i.e., the point is at infinity
    final boolean isInfinity = coordinateSum.signum() == 0;

    // Set the isInfinity flag for all coordinates
    for (int j = 0; j < coordinate.length; j++) {
      this.isInfinity.set(i + j, isInfinity);
    }

    return isInfinity;
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
    final Bytes limb = Bytes.concatenate(callData, returnData);
    final boolean returnDataIsNonEmpty = returnData.toArray().length > 0;

    final Bytes deltaByte =
        leftPadTo(Bytes.minimalBytes(id - previousId - 1), nBYTES_OF_DELTA_BYTES);

    int ct = 0;
    boolean isFirstInput = true;
    int accInputs = 0;
    boolean mintBitAcc = false;
    boolean mextBitAcc = false;
    boolean nontrivialPairOfPointsAcc = false;

    for (int i = 0; i < nRows; i++) {
      boolean isData = i < nRowsData;
      isFirstInput =
          switch (precompileFlag) {
            case PRC_POINT_EVALUATION -> i <= CT_MAX_POINT_EVALUATION;
            case PRC_BLS_G1_ADD -> i <= CT_MAX_SMALL_POINT;
            case PRC_BLS_G1_MSM -> (i % (CT_MAX_SMALL_POINT + CT_MAX_SCALAR + 2))
                <= CT_MAX_SMALL_POINT;
            case PRC_BLS_G2_ADD -> i <= CT_MAX_LARGE_POINT;
            case PRC_BLS_G2_MSM -> (i % (CT_MAX_LARGE_POINT + CT_MAX_SCALAR + 2))
                <= CT_MAX_LARGE_POINT;
            case PRC_BLS_PAIRING_CHECK -> (i % (CT_MAX_SMALL_POINT + CT_MAX_LARGE_POINT + 2))
                <= CT_MAX_SMALL_POINT;
            case PRC_BLS_MAP_FP_TO_G1 -> i <= CT_MAX_MAP_FP_TO_G1;
            case PRC_BLS_MAP_FP2_TO_G2 -> i <= CT_MAX_MAP_FP2_TO_G2;
            default -> throw new IllegalStateException("invalid BLS type");
          };
      final int ctMax = getCtMax(precompileFlag, isData, isFirstInput);
      final int indexMax = getIndexMax(precompileFlag, isData);

      if (isData) {
        switch (precompileFlag) {
          case PRC_BLS_G1_MSM -> accInputs = i / (INDEX_MAX_DATA_G1_MSM_MIN + 1) + 1;
          case PRC_BLS_G2_MSM -> accInputs = i / (INDEX_MAX_DATA_G2_MSM_MIN + 1) + 1;
          case PRC_BLS_PAIRING_CHECK -> accInputs = i / (INDEX_MAX_DATA_PAIRING_CHECK_MIN + 1) + 1;
          default -> accInputs = 0;
        }
      }

      mintBitAcc = mintBitAcc || mintBit.get(i);
      mextBitAcc = mextBitAcc || mextBit.get(i);
      nontrivialPairOfPointsAcc = nontrivialPairOfPointsAcc || nontrivialPairOfPointsBit.get(i);

      trace
          .stamp(stamp)
          .id(id)
          .totalSize(isData ? totalSizeData : totalSizeResult)
          .index(isData ? i : i - nRowsData)
          .indexMax(indexMax)
          .phase(isData ? precompileFlag.dataPhase() : precompileFlag.resultPhase())
          .limb(isData || returnDataIsNonEmpty ? limb.slice(i * LLARGE, LLARGE) : ZERO)
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
          .accInputs(accInputs)
          .byteDelta(
              i < nBYTES_OF_DELTA_BYTES ? UnsignedByte.of(deltaByte.get(i)) : UnsignedByte.of(0))
          .malformedDataInternalBit(mintBit.get(i))
          .malformedDataInternalAcc(mintBitAcc)
          .malformedDataInternalAccTot(mint)
          .malformedDataExternalBit(mextBit.get(i) && isData)
          .malformedDataExternalAcc(mextBitAcc && isData)
          .malformedDataExternalAccTot(mext)
          .wellformedDataTrivial(wtrv)
          .wellformedDataNontrivial(wnon)
          .isFirstInput(isFirstInput && isData)
          .isSecondInput(!isFirstInput && isData)
          .isInfinity(isInfinity.get(i))
          .nontrivialPairOfPointsBit(nontrivialPairOfPointsBit.get(i))
          .nontrivialPairOfPointsAcc(nontrivialPairOfPointsAcc)
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
