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
import static net.consensys.linea.zktracer.Trace.Bls.POINT_EVALUATION_PRIME_HI;
import static net.consensys.linea.zktracer.Trace.Bls.POINT_EVALUATION_PRIME_LO;
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
import static net.consensys.linea.zktracer.types.Containers.repeat;

import java.math.BigInteger;
import java.util.List;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;

@Accessors(fluent = true)
public class BlsOperation extends ModuleOperation {
  final EWord BLS_PRIME_HI = EWord.of(BigInteger.valueOf(BLS_PRIME_3), BLS_PRIME_2);
  final EWord BLS_PRIME_LO = EWord.of(BLS_PRIME_1, BLS_PRIME_0);
  final EWord POINT_EVALUATION_PRIME =
      EWord.of(POINT_EVALUATION_PRIME_HI, POINT_EVALUATION_PRIME_LO);

  private final Bytes returnData;

  private final Wcp wcp;

  @Getter private final long id;
  private final Bytes callData;

  @Getter private final PrecompileScenarioFragment.PrecompileFlag precompileFlag;
  private final int nRows;
  private final int nRowsData;
  private final int nRowsResult;

  @Getter private final List<Bytes> limb;
  private final int totalSizeData;
  private final int totalSizeResult;
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
    final EWord verHash = EWord.of(callData.slice(0, 32));
    final EWord z = EWord.of(callData.slice(32, 32));
    final EWord y = EWord.of(callData.slice(64, 32));
    final Bytes com = callData.slice(96, 48);
    final Bytes proof = callData.slice(144, 48);

    // Set input limb
    limb.set(0, verHash.hi());
    limb.set(1, verHash.lo());
    limb.set(2, z.hi());
    limb.set(3, z.lo());
    limb.set(4, y.hi());
    limb.set(5, y.lo());
    limb.set(6, com.slice(0, 16));
    limb.set(7, com.slice(16, 16));
    limb.set(8, com.slice(32, 16));
    limb.set(9, proof.slice(0, 16));
    limb.set(10, proof.slice(16, 16));
    limb.set(11, proof.slice(32, 16));

    final boolean zIsInRange = wcpCallToLT(0, z, POINT_EVALUATION_PRIME);

    final boolean yIsInRange = wcpCallToLT(1, y, POINT_EVALUATION_PRIME);

    final boolean internalChecksPassed = zIsInRange && yIsInRange;

    // TODO: propagate condition
    this.mintBit.set(0, !internalChecksPassed);
  }

  private void handleBlsG1Add() {}

  private void handleBlsG1Msm() {}

  private void handleBlsG2Add() {}

  private void handleBlsG2Msm() {}

  private void handleBlsPairingCheck() {}

  private void handleBlsMapFpToG1() {}

  private void handleBlsMapFp2ToG2() {}

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
      int i, EWord arg1Hi, EWord arg1Lo, EWord arg2Hi, EWord arg2Lo) {
    wcpCallToLT(i + 1, arg1Hi, arg2Hi);
    wcpCallToEQ(i + 2, arg1Hi, arg2Hi);
    wcpCallToLT(i + 3, arg1Lo, arg2Lo);

    final boolean wcpRes =
        this.wcpRes.get(i + 1) || (this.wcpRes.get(i + 2) && this.wcpRes.get(i + 3));
    this.wcpRes.set(i, wcpRes); // TODO: do we want to set other WCP columns here?

    return wcpRes;
  }

  private void wellFormedFpCoordinateAndInfinityCheck(
      int i, EWord pXHi, EWord pXLo, EWord pYHi, EWord pYLo) {
    final boolean pXIsInRange = wcpGeneralizedCallToLT(i, pXHi, pXLo, BLS_PRIME_HI, BLS_PRIME_LO);

    final boolean pYIsInRange =
        wcpGeneralizedCallToLT(i + 4, pYHi, pYLo, BLS_PRIME_HI, BLS_PRIME_LO);

    final boolean wellFormedCoordinate = pXIsInRange && pYIsInRange;

    // TODO: propagate condition
    this.mintBit.set(i, !wellFormedCoordinate);

    isInfinity(
        i,
        pXHi.toBigInteger()
            .add(pXLo.toBigInteger())
            .add(pYHi.toBigInteger())
            .add(pYLo.toBigInteger()));
  }

  private void wellFormedFp2CoordinateAndInfinityCheck(
      int i,
      EWord pXImHi,
      EWord pXImLo,
      EWord pXReHi,
      EWord pXReLo,
      EWord pYImHi,
      EWord pYImLo,
      EWord pYReHi,
      EWord pYReLo) {
    final boolean pXImIsInRange =
        wcpGeneralizedCallToLT(i, pXImHi, pXImLo, BLS_PRIME_HI, BLS_PRIME_LO);
    final boolean pXReIsInRange =
        wcpGeneralizedCallToLT(i + 4, pXReHi, pXReLo, BLS_PRIME_HI, BLS_PRIME_LO);
    final boolean pYImIsInRange =
        wcpGeneralizedCallToLT(i + 8, pYImHi, pYImLo, BLS_PRIME_HI, BLS_PRIME_LO);
    final boolean pYReIsInRange =
        wcpGeneralizedCallToLT(i + 12, pYReHi, pYReLo, BLS_PRIME_HI, BLS_PRIME_LO);

    final boolean wellFormedCoordinate =
        pXImIsInRange && pXReIsInRange && pYImIsInRange && pYReIsInRange;

    // TODO: propagate condition
    this.mintBit.set(i, !wellFormedCoordinate);

    isInfinity(
        i,
        pXImHi
            .toBigInteger()
            .add(pXImLo.toBigInteger())
            .add(pXReHi.toBigInteger())
            .add(pXReLo.toBigInteger())
            .add(pYImHi.toBigInteger())
            .add(pYImLo.toBigInteger())
            .add(pYReHi.toBigInteger())
            .add(pYReLo.toBigInteger()));
  }

  private void isInfinity(int i, BigInteger coordinateSum) {
    // TODO: propagate condition
    this.isInfinity.set(i, coordinateSum.signum() == 0);
  }

  void trace(Trace.Bls trace, final int stamp, final long previousId) {
    trace.fillAndValidateRow();
  }

  @Override
  protected int computeLineCount() {
    return nRowsData + nRowsResult;
  }
}
