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
import static net.consensys.linea.zktracer.Trace.Ecdata.P_BN_HI;
import static net.consensys.linea.zktracer.Trace.Ecdata.P_BN_LO;
import static net.consensys.linea.zktracer.Trace.Ecdata.SECP256K1N_HI;
import static net.consensys.linea.zktracer.Trace.Ecdata.SECP256K1N_LO;
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
  private static final EWord P_BN = EWord.of(P_BN_HI, P_BN_LO);
  public static final EWord SECP256K1N = EWord.of(SECP256K1N_HI, SECP256K1N_LO);
  public static final int nBYTES_OF_DELTA_BYTES = 4;

  private final Bytes returnData;

  private final Wcp wcp;

  @Getter private final long id;
  // private final Bytes rightPaddedCallData;

  @Getter private final PrecompileScenarioFragment.PrecompileFlag precompileFlag;
  private final int nRows;
  private final int nRowsData;
  private final int nRowsResult;

  @Getter private final List<Bytes> limb;
  private final int totalSize;

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
    totalSize = callData.size();
    // TODO: do we need some padding? Is this just the size of the input? What's
    //  the meaning of total size?

    nRowsData = getIndexMax(precompileFlag, true) + 1;
    nRowsResult = getIndexMax(precompileFlag, false) + 1;
    nRows = nRowsData + nRowsResult;
    this.id = id;

    limb = repeat(Bytes.EMPTY, nRows);

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

  private void handlePointEvaluation() {}

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
        case PRC_BLS_G1_MSM -> totalSize / 16 - 1; // TODO: here we need the size of the input!
        case PRC_BLS_G2_ADD -> INDEX_MAX_DATA_G2_ADD;
        case PRC_BLS_G2_MSM -> totalSize / 16 - 1;
        case PRC_BLS_PAIRING_CHECK -> totalSize / 16 - 1;
        case PRC_BLS_MAP_FP_TO_G1 -> INDEX_MAX_DATA_MAP_FP_TO_G1;
        case PRC_BLS_MAP_FP2_TO_G2 -> INDEX_MAX_DATA_MAP_FP2_TO_G2;
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

  private boolean callWcp(int i, OpCode wcpInst, EWord arg1, EWord arg2) {
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

  void trace(Trace.Bls trace, final int stamp, final long previousId) {
    // TODO: look at ECDATA as a reference
    trace.fillAndValidateRow();
  }

  @Override
  protected int computeLineCount() {
    return nRowsData + nRowsResult;
  }
}
