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

package net.consensys.linea.zktracer.module.ecdata;

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_ECADD_DATA;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_ECADD_RESULT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_ECMUL_DATA;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_ECMUL_RESULT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_ECPAIRING_DATA;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_ECPAIRING_RESULT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_ECRECOVER_DATA;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_ECRECOVER_RESULT;
import static net.consensys.linea.zktracer.module.ecdata.Trace.CT_MAX_LARGE_POINT;
import static net.consensys.linea.zktracer.module.ecdata.Trace.CT_MAX_SMALL_POINT;
import static net.consensys.linea.zktracer.module.ecdata.Trace.ECADD;
import static net.consensys.linea.zktracer.module.ecdata.Trace.ECMUL;
import static net.consensys.linea.zktracer.module.ecdata.Trace.ECPAIRING;
import static net.consensys.linea.zktracer.module.ecdata.Trace.ECRECOVER;
import static net.consensys.linea.zktracer.module.ecdata.Trace.INDEX_MAX_ECADD_DATA;
import static net.consensys.linea.zktracer.module.ecdata.Trace.INDEX_MAX_ECADD_RESULT;
import static net.consensys.linea.zktracer.module.ecdata.Trace.INDEX_MAX_ECMUL_DATA;
import static net.consensys.linea.zktracer.module.ecdata.Trace.INDEX_MAX_ECMUL_RESULT;
import static net.consensys.linea.zktracer.module.ecdata.Trace.INDEX_MAX_ECPAIRING_DATA_MIN;
import static net.consensys.linea.zktracer.module.ecdata.Trace.INDEX_MAX_ECPAIRING_RESULT;
import static net.consensys.linea.zktracer.module.ecdata.Trace.INDEX_MAX_ECRECOVER_DATA;
import static net.consensys.linea.zktracer.module.ecdata.Trace.INDEX_MAX_ECRECOVER_RESULT;
import static net.consensys.linea.zktracer.module.ecdata.Trace.P_BN_HI;
import static net.consensys.linea.zktracer.module.ecdata.Trace.P_BN_LO;
import static net.consensys.linea.zktracer.module.ecdata.Trace.SECP256K1N_HI;
import static net.consensys.linea.zktracer.module.ecdata.Trace.SECP256K1N_LO;
import static net.consensys.linea.zktracer.module.ecdata.Trace.TOTAL_SIZE_ECADD_DATA;
import static net.consensys.linea.zktracer.module.ecdata.Trace.TOTAL_SIZE_ECADD_RESULT;
import static net.consensys.linea.zktracer.module.ecdata.Trace.TOTAL_SIZE_ECMUL_DATA;
import static net.consensys.linea.zktracer.module.ecdata.Trace.TOTAL_SIZE_ECMUL_RESULT;
import static net.consensys.linea.zktracer.module.ecdata.Trace.TOTAL_SIZE_ECPAIRING_DATA_MIN;
import static net.consensys.linea.zktracer.module.ecdata.Trace.TOTAL_SIZE_ECPAIRING_RESULT;
import static net.consensys.linea.zktracer.module.ecdata.Trace.TOTAL_SIZE_ECRECOVER_DATA;
import static net.consensys.linea.zktracer.module.ecdata.Trace.TOTAL_SIZE_ECRECOVER_RESULT;
import static net.consensys.linea.zktracer.types.Containers.repeat;
import static net.consensys.linea.zktracer.types.Utils.leftPadTo;

import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.ext.Ext;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.altbn128.AltBn128Fq2Point;
import org.hyperledger.besu.crypto.altbn128.Fq2;

@Accessors(fluent = true)
public class EcDataOperation extends ModuleOperation {
  private static final Set<Integer> EC_TYPES = Set.of(ECRECOVER, ECADD, ECMUL, ECPAIRING);
  private static final EWord P_BN = EWord.of(P_BN_HI, P_BN_LO);
  public static final EWord SECP256K1N = EWord.of(SECP256K1N_HI, SECP256K1N_LO);
  public static final int nBYTES_OF_DELTA_BYTES = 4;

  private final Wcp wcp;
  private final Ext ext;

  @Getter private final long id;
  private final Bytes data;

  private final int ecType;
  private final int nRows;
  private final int nRowsData;
  private final int nRowsResult;

  @Getter private final List<Bytes> limb;
  private final List<Boolean> hurdle;
  @Getter private boolean internalChecksPassed;

  // WCP interaction
  private final List<Boolean> wcpFlag;
  private final List<Bytes> wcpArg1Hi;
  private final List<Bytes> wcpArg1Lo;
  private final List<Bytes> wcpArg2Hi;
  private final List<Bytes> wcpArg2Lo;
  private final List<Boolean> wcpRes;
  private final List<OpCode> wcpInst;

  // EXT interaction
  private final List<Boolean> extFlag;
  private final List<Bytes> extArg1Hi;
  private final List<Bytes> extArg1Lo;
  private final List<Bytes> extArg2Hi;
  private final List<Bytes> extArg2Lo;
  private final List<Bytes> extArg3Hi;
  private final List<Bytes> extArg3Lo;
  private final List<Bytes> extResHi;
  private final List<Bytes> extResLo;
  private final List<OpCode> extInst;

  @Getter private boolean successBit;
  private boolean circuitSelectorEcrecover;
  private boolean circuitSelectorEcadd;
  private boolean circuitSelectorEcmul;

  // pairing-specific
  private final int totalPairings;

  private List<Boolean> notOnG2; // counter-constant
  private List<Boolean> notOnG2Acc; // counter-constant
  private boolean notOnG2AccMax; // index-constant
  private List<Boolean> isInfinity; // counter-constant
  private List<Boolean> overallTrivialPairing; // counter-constant

  // For debugging
  private boolean returnDataSet = false;

  private EcDataOperation(Wcp wcp, Ext ext, int id, int ecType, Bytes data) {
    Preconditions.checkArgument(EC_TYPES.contains(ecType), "invalid EC type");

    final int minInputLength = ecType == ECMUL ? 96 : 128;
    if (data.size() < minInputLength) {
      this.data = leftPadTo(data, minInputLength);
    } else {
      this.data = data;
    }
    this.ecType = ecType;

    if (ecType == ECPAIRING) {
      totalPairings = data.size() / 192;
    } else {
      totalPairings = 0;
    }

    nRowsData = getIndexMax(ecType, true) + 1;
    nRowsResult = getIndexMax(ecType, false) + 1;
    nRows = nRowsData + nRowsResult;
    this.id = id;

    limb = repeat(Bytes.EMPTY, nRows);
    hurdle = repeat(false, nRows);

    wcpFlag = repeat(false, nRows);
    wcpArg1Hi = repeat(Bytes.EMPTY, nRows);
    wcpArg1Lo = repeat(Bytes.EMPTY, nRows);
    wcpArg2Hi = repeat(Bytes.EMPTY, nRows);
    wcpArg2Lo = repeat(Bytes.EMPTY, nRows);
    wcpRes = repeat(false, nRows);
    wcpInst = repeat(OpCode.INVALID, nRows);

    extFlag = repeat(false, nRows);
    extArg1Hi = repeat(Bytes.EMPTY, nRows);
    extArg1Lo = repeat(Bytes.EMPTY, nRows);
    extArg2Hi = repeat(Bytes.EMPTY, nRows);
    extArg2Lo = repeat(Bytes.EMPTY, nRows);
    extArg3Hi = repeat(Bytes.EMPTY, nRows);
    extArg3Lo = repeat(Bytes.EMPTY, nRows);
    extResHi = repeat(Bytes.EMPTY, nRows);
    extResLo = repeat(Bytes.EMPTY, nRows);
    extInst = repeat(OpCode.INVALID, nRows);

    this.wcp = wcp;
    this.ext = ext;

    isInfinity = repeat(false, nRows);
    overallTrivialPairing = repeat(true, nRows);
    notOnG2 = repeat(false, nRows);
    notOnG2Acc = repeat(false, nRows);
  }

  public static EcDataOperation of(Wcp wcp, Ext ext, int id, final int ecType, Bytes data) {
    EcDataOperation ecDataRes = new EcDataOperation(wcp, ext, id, ecType, data);
    switch (ecType) {
      case ECRECOVER -> ecDataRes.handleRecover();
      case ECADD -> ecDataRes.handleAdd();
      case ECMUL -> ecDataRes.handleMul();
      case ECPAIRING -> ecDataRes.handlePairing();
    }
    return ecDataRes;
  }

  public void setReturnData(Bytes returnData) {
    switch (ecType) {
      case ECRECOVER -> {
        EWord recoveredAddress = EWord.ZERO;

        // Extract output
        if (internalChecksPassed) {
          System.out.println(returnData.toArray().length);
          recoveredAddress = EWord.of(returnData);
        }

        // Set success bit and output limb
        successBit = !recoveredAddress.isZero();
        limb.set(8, recoveredAddress.hi());
        limb.set(9, recoveredAddress.lo());
      }
      case ECADD -> {
        EWord resX = EWord.ZERO;
        EWord resY = EWord.ZERO;

        // Extract output
        if (internalChecksPassed && returnData.toArray().length != 0) {
          Preconditions.checkArgument(returnData.toArray().length == 64);
          System.out.println(returnData.toArray().length);
          resX = EWord.of(returnData.slice(0, 32));
          resY = EWord.of(returnData.slice(32, 32));
        }

        // Set success bit and output limb
        successBit = internalChecksPassed;
        limb.set(8, resX.hi());
        limb.set(9, resX.lo());
        limb.set(10, resY.hi());
        limb.set(11, resY.lo());
      }
      case ECMUL -> {
        EWord resX = EWord.ZERO;
        EWord resY = EWord.ZERO;

        // Extract output
        if (internalChecksPassed && returnData.toArray().length != 0) {
          Preconditions.checkArgument(returnData.toArray().length == 64);
          System.out.println(returnData.toArray().length);
          resX = EWord.of(returnData.slice(0, 32));
          resY = EWord.of(returnData.slice(32, 32));
        }

        // Set success bit and output limb
        successBit = internalChecksPassed;
        limb.set(6, resX.hi());
        limb.set(7, resX.lo());
        limb.set(8, resY.hi());
        limb.set(9, resY.lo());
      }
      case ECPAIRING -> {
        EWord pairingResult = EWord.ZERO;

        // Extract output
        if (internalChecksPassed) {
          System.out.println(returnData.toArray().length);
          pairingResult = EWord.of(returnData);
        }

        // Set output limb
        limb.set(limb.size() - 2, pairingResult.hi());
        limb.set(limb.size() - 1, pairingResult.lo());

        // Set successBit
        if (!internalChecksPassed || notOnG2AccMax) {
          successBit = false;
        } else {
          successBit = true;
        }
      }
    }
    returnDataSet = true;
  }

  private int getTotalSize(int ecType, boolean isData) {
    Preconditions.checkArgument(returnDataSet, "returnData must be set before");

    if (isData) {
      return switch (ecType) {
        case ECRECOVER -> TOTAL_SIZE_ECRECOVER_DATA;
        case ECADD -> TOTAL_SIZE_ECADD_DATA;
        case ECMUL -> TOTAL_SIZE_ECMUL_DATA;
        case ECPAIRING -> TOTAL_SIZE_ECPAIRING_DATA_MIN * totalPairings;
        default -> throw new IllegalArgumentException("invalid EC type");
      };
    } else {
      return switch (ecType) {
        case ECRECOVER -> successBit ? TOTAL_SIZE_ECRECOVER_RESULT : 0;
        case ECADD -> successBit ? TOTAL_SIZE_ECADD_RESULT : 0;
        case ECMUL -> successBit ? TOTAL_SIZE_ECMUL_RESULT : 0;
        case ECPAIRING -> successBit ? TOTAL_SIZE_ECPAIRING_RESULT : 0;
        default -> throw new IllegalArgumentException("invalid EC type");
      };
    }
  }

  private static short getPhase(int ecType, boolean isData) {
    if (isData) {
      return switch (ecType) {
        case ECRECOVER -> PHASE_ECRECOVER_DATA;
        case ECADD -> PHASE_ECADD_DATA;
        case ECMUL -> PHASE_ECMUL_DATA;
        case ECPAIRING -> PHASE_ECPAIRING_DATA;
        default -> throw new IllegalArgumentException("invalid EC type");
      };
    } else {
      return switch (ecType) {
        case ECRECOVER -> PHASE_ECRECOVER_RESULT;
        case ECADD -> PHASE_ECADD_RESULT;
        case ECMUL -> PHASE_ECMUL_RESULT;
        case ECPAIRING -> PHASE_ECPAIRING_RESULT;
        default -> throw new IllegalArgumentException("invalid EC type");
      };
    }
  }

  private int getIndexMax(int ecType, boolean isData) {
    if (isData) {
      return switch (ecType) {
        case ECRECOVER -> INDEX_MAX_ECRECOVER_DATA;
        case ECADD -> INDEX_MAX_ECADD_DATA;
        case ECMUL -> INDEX_MAX_ECMUL_DATA;
        case ECPAIRING -> (INDEX_MAX_ECPAIRING_DATA_MIN + 1) * totalPairings - 1;
        default -> throw new IllegalArgumentException("invalid EC type");
      };
    } else {
      return switch (ecType) {
        case ECRECOVER -> INDEX_MAX_ECRECOVER_RESULT;
        case ECADD -> INDEX_MAX_ECADD_RESULT;
        case ECMUL -> INDEX_MAX_ECMUL_RESULT;
        case ECPAIRING -> INDEX_MAX_ECPAIRING_RESULT;
        default -> throw new IllegalArgumentException("invalid EC type");
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

  private EWord callExt(int i, OpCode extInst, EWord arg1, EWord arg2, EWord arg3) {
    final EWord extRes = EWord.of(ext.call(extInst, arg1, arg2, arg3));

    extFlag.set(i, true);
    extArg1Hi.set(i, arg1.hi());
    extArg1Lo.set(i, arg1.lo());
    extArg2Hi.set(i, arg2.hi());
    extArg2Lo.set(i, arg2.lo());
    extArg3Hi.set(i, arg3.hi());
    extArg3Lo.set(i, arg3.lo());
    extResHi.set(i, extRes.hi());
    extResLo.set(i, extRes.lo());
    this.extInst.set(i, extInst);
    return extRes;
  }

  void handleRecover() {
    // Extract inputs
    final EWord h = EWord.of(data.slice(0, 32));
    final EWord v = EWord.of(data.slice(32, 32));
    final EWord r = EWord.of(data.slice(64, 32));
    final EWord s = EWord.of(data.slice(96, 32));

    // Set input limb
    limb.set(0, h.hi());
    limb.set(1, h.lo());
    limb.set(2, v.hi());
    limb.set(3, v.lo());
    limb.set(4, r.hi());
    limb.set(5, r.lo());
    limb.set(6, s.hi());
    limb.set(7, s.lo());

    // Compute internal checks
    // row i
    boolean rIsInRange = callWcp(0, OpCode.LT, r, SECP256K1N); // r < secp256k1N

    // row i + 1
    boolean rIsPositive = callWcp(1, OpCode.LT, EWord.ZERO, r); // 0 < r

    // row i + 2
    boolean sIsInRange = callWcp(2, OpCode.LT, s, SECP256K1N); // s < secp256k1N

    // row i + 3
    boolean sIsPositive = callWcp(3, OpCode.LT, EWord.ZERO, s); // 0 < s

    // row i+ 4
    boolean vIs27 = callWcp(4, OpCode.EQ, v, EWord.of(27)); // v == 27

    // row i + 5
    boolean vIs28 = callWcp(5, OpCode.EQ, v, EWord.of(28)); // v == 28

    // Set hurdle
    hurdle.set(0, rIsInRange && rIsPositive);
    hurdle.set(1, sIsInRange && sIsPositive);
    hurdle.set(2, hurdle.get(0) && hurdle.get(1));
    hurdle.set(INDEX_MAX_ECRECOVER_DATA, hurdle.get(2) && (vIs27 || vIs28));

    // Set internal checks passed
    internalChecksPassed = hurdle.get(INDEX_MAX_ECRECOVER_DATA);

    // Success bit is set in setReturnData

    // Set circuitSelectorEcrecover
    if (internalChecksPassed) {
      circuitSelectorEcrecover = true;
    }

    // Very unlikely edge case: if the ext module is never used elsewhere, we need to insert a
    // useless row, in order to trigger the construction of the first empty row, useful for the ext
    // lookup.
    // Because of the hashmap in the ext module, this useless row will only be inserted one time.
    // Tested by TestEcRecoverWithEmptyExt
    ext.callADDMOD(Bytes.EMPTY, Bytes.EMPTY, Bytes.EMPTY);
  }

  void handleAdd() {
    // Extract inputs
    final EWord pX = EWord.of(data.slice(0, 32));
    final EWord pY = EWord.of(data.slice(32, 32));
    final EWord qX = EWord.of(data.slice(64, 32));
    final EWord qY = EWord.of(data.slice(96, 32));

    // Set limb
    limb.set(0, pX.hi());
    limb.set(1, pX.lo());
    limb.set(2, pY.hi());
    limb.set(3, pY.lo());
    limb.set(4, qX.hi());
    limb.set(5, qX.lo());
    limb.set(6, qY.hi());
    limb.set(7, qY.lo());

    // Compute internal checks
    // row i
    boolean c1MembershipFirstPoint = callToC1Membership(0, pX, pY).getLeft();

    // row i + 4
    boolean c1MembershipSecondPoint = callToC1Membership(4, qX, qY).getLeft();

    // Complete set hurdle
    hurdle.set(INDEX_MAX_ECADD_DATA, c1MembershipFirstPoint && c1MembershipSecondPoint);

    // Set intenral checks passed
    internalChecksPassed = hurdle.get(INDEX_MAX_ECADD_DATA);

    // Success bit is set in setReturnData

    // set circuitSelectorEcadd
    circuitSelectorEcadd = internalChecksPassed;
  }

  void handleMul() {
    // Extract inputs
    final EWord pX = EWord.of(data.slice(0, 32));
    final EWord pY = EWord.of(data.slice(32, 32));
    final EWord n = EWord.of(data.slice(64, 32));

    // Set limb
    limb.set(0, pX.hi());
    limb.set(1, pX.lo());
    limb.set(2, pY.hi());
    limb.set(3, pY.lo());
    limb.set(4, n.hi());
    limb.set(5, n.lo());

    // Compute internal checks
    // row i
    boolean c1Membership = callToC1Membership(0, pX, pY).getLeft();

    // Complete set hurdle
    hurdle.set(INDEX_MAX_ECMUL_DATA, c1Membership);

    // Set intenral checks passed
    internalChecksPassed = hurdle.get(INDEX_MAX_ECMUL_DATA);

    // Success bit is set in setReturnData

    // Set circuitSelectorEcmul
    circuitSelectorEcmul = internalChecksPassed;
  }

  void handlePairing() {
    boolean atLeastOneLargePointIsNotInfinity = false;
    boolean firstLargePointNotInfinity = false;
    boolean atLeastOneLargePointIsNotOnG2 = false;
    boolean firstLargePointNotOnG2 = false;

    for (int accPairings = 1; accPairings <= totalPairings; accPairings++) {
      // Extract inputs
      final int bytesOffset = (accPairings - 1) * 192;
      final EWord aX = EWord.of(data.slice(bytesOffset, 32));
      final EWord aY = EWord.of(data.slice(32 + bytesOffset, 32));
      final EWord bXIm = EWord.of(data.slice(64 + bytesOffset, 32));
      final EWord bXRe = EWord.of(data.slice(96 + bytesOffset, 32));
      final EWord bYIm = EWord.of(data.slice(128 + bytesOffset, 32));
      final EWord bYRe = EWord.of(data.slice(160 + bytesOffset, 32));

      // Set limb
      final int rowsOffset = (accPairings - 1) * (INDEX_MAX_ECPAIRING_DATA_MIN + 1); // 12
      limb.set(rowsOffset, aX.hi());
      limb.set(1 + rowsOffset, aX.lo());
      limb.set(2 + rowsOffset, aY.hi());
      limb.set(3 + rowsOffset, aY.lo());
      limb.set(4 + rowsOffset, bXIm.hi());
      limb.set(5 + rowsOffset, bXIm.lo());
      limb.set(6 + rowsOffset, bXRe.hi());
      limb.set(7 + rowsOffset, bXRe.lo());
      limb.set(8 + rowsOffset, bYIm.hi());
      limb.set(9 + rowsOffset, bYIm.lo());
      limb.set(10 + rowsOffset, bYRe.hi());
      limb.set(11 + rowsOffset, bYRe.lo());

      // Compute internal checks
      // row i
      Pair<Boolean, Boolean> callToC1MembershipReturnedValues =
          callToC1Membership(rowsOffset, aX, aY);
      boolean c1Membership = callToC1MembershipReturnedValues.getLeft();
      boolean smallPointIsAtInfinity = callToC1MembershipReturnedValues.getRight();

      // row i + 4
      Pair<Boolean, Boolean> callToWellFormedCoordinatesReturnedValues =
          callToWellFormedCoordinates(4 + rowsOffset, bXIm, bXRe, bYIm, bYRe);
      boolean wellFormedCoordinates = callToWellFormedCoordinatesReturnedValues.getLeft();
      boolean largePointIsAtInfinity = callToWellFormedCoordinatesReturnedValues.getRight();

      // Check if the large point is on G2
      final Fq2 bX = Fq2.create(bXRe.toUnsignedBigInteger(), bXIm.toUnsignedBigInteger());
      final Fq2 bY = Fq2.create(bYRe.toUnsignedBigInteger(), bYIm.toUnsignedBigInteger());
      final AltBn128Fq2Point b = new AltBn128Fq2Point(bX, bY);
      if (!atLeastOneLargePointIsNotOnG2 && (!b.isOnCurve() || !b.isInGroup())) {
        atLeastOneLargePointIsNotOnG2 = true;
        firstLargePointNotOnG2 = true;
        notOnG2AccMax = true;
      }

      // Set isInfinity, overallTrivialPairing, notOnG2, notOnG2Acc
      for (int i = 0; i <= INDEX_MAX_ECPAIRING_DATA_MIN; i++) {
        if (!largePointIsAtInfinity && !atLeastOneLargePointIsNotInfinity) {
          atLeastOneLargePointIsNotInfinity = true;
          firstLargePointNotInfinity = true;
        }

        if (firstLargePointNotInfinity) {
          if (i > CT_MAX_SMALL_POINT) {
            // Transition should happen at the beginning of large point
            overallTrivialPairing.set(i + rowsOffset, false);
          }
        } else {
          overallTrivialPairing.set(i + rowsOffset, !atLeastOneLargePointIsNotInfinity);
        }

        if (firstLargePointNotOnG2) {
          if (i > CT_MAX_SMALL_POINT) {
            // Transition should happen at the beginning of large point
            notOnG2.set(i + rowsOffset, true);
            notOnG2Acc.set(i + rowsOffset, true);
          }
        } else {
          notOnG2Acc.set(i + rowsOffset, atLeastOneLargePointIsNotOnG2);
        }
      }

      // Set firstLargePointNotInfinity back to false
      firstLargePointNotInfinity = false;

      // Set firstLargePointNotOnG2 back to false
      firstLargePointNotOnG2 = false;

      // Set hurdle and internal checks passed
      if (accPairings == 1) {
        internalChecksPassed = c1Membership && wellFormedCoordinates;

        hurdle.set(INDEX_MAX_ECPAIRING_DATA_MIN, internalChecksPassed);
      } else {
        boolean prevInternalChecksPassed = internalChecksPassed;
        internalChecksPassed = c1Membership && wellFormedCoordinates && prevInternalChecksPassed;

        hurdle.set(
            INDEX_MAX_ECPAIRING_DATA_MIN - 1 + rowsOffset, c1Membership && wellFormedCoordinates);
        hurdle.set(INDEX_MAX_ECPAIRING_DATA_MIN + rowsOffset, internalChecksPassed);
      }
    }

    // This is after all pairings have been processed

    // Success bit is set in setReturnData

    // acceptablePairOfPointForPairingCircuit, g2MembershipTestRequired, circuitSelectorEcpairing,
    // circuitSelectorG2Membership are set in the trace method
  }

  void trace(Trace trace, final int stamp, final long previousId) {
    Preconditions.checkArgument(returnDataSet, "returnData must be set before");

    final Bytes deltaByte =
        leftPadTo(Bytes.minimalBytes(id - previousId - 1), nBYTES_OF_DELTA_BYTES);

    // Part of the columns are computed here
    int ct = 0;
    boolean isSmallPoint = false;
    boolean isLargePoint = false;
    boolean smallPointIsAtInfinity = false;
    boolean largePointIsAtInfinity = false;

    for (int i = 0; i < nRows; i++) {
      boolean isData = i < nRowsData;
      // Turn isSmallPoint on if we are in the first row of a new pairing
      if (ecType == ECPAIRING && isData && ct == 0 && !isSmallPoint && !isLargePoint) {
        isSmallPoint = true;
        smallPointIsAtInfinity = isInfinity.get(i);
        largePointIsAtInfinity = isInfinity.get(i + CT_MAX_SMALL_POINT + 1);
      }

      boolean notOnG2AccMax =
          ecType == ECPAIRING
              && isData
              && this
                  .notOnG2AccMax; // && conditions is necessary since we want IS_ECPAIRING_DATA = 1
      boolean g2MembershipTestRequired =
          notOnG2AccMax
              ? isLargePoint && !largePointIsAtInfinity && notOnG2.get(i)
              : isLargePoint && !largePointIsAtInfinity && smallPointIsAtInfinity;
      boolean acceptablePairOfPointForPairingCircuit =
          !notOnG2AccMax && !largePointIsAtInfinity && !smallPointIsAtInfinity;

      if (ecType != ECPAIRING || !isData) {
        Preconditions.checkArgument(ct == 0);
      }
      Preconditions.checkArgument(!(isSmallPoint && isLargePoint));

      trace
          .stamp(stamp)
          .id(id)
          .index(isData ? UnsignedByte.of(i) : UnsignedByte.of(i - nRowsData))
          .limb(limb.get(i))
          .totalSize(Bytes.ofUnsignedLong(getTotalSize(ecType, isData)))
          .phase(getPhase(ecType, isData))
          .indexMax(Bytes.ofUnsignedLong(getIndexMax(ecType, isData)))
          .successBit(successBit)
          .isEcrecoverData(ecType == ECRECOVER && isData)
          .isEcrecoverResult(ecType == ECRECOVER && !isData)
          .isEcaddData(ecType == ECADD && isData)
          .isEcaddResult(ecType == ECADD && !isData)
          .isEcmulData(ecType == ECMUL && isData)
          .isEcmulResult(ecType == ECMUL && !isData)
          .isEcpairingData(ecType == ECPAIRING && isData)
          .isEcpairingResult(ecType == ECPAIRING && !isData)
          .totalPairings(Bytes.ofUnsignedLong(totalPairings))
          .accPairings(
              ecType == ECPAIRING && isData
                  ? Bytes.ofUnsignedLong(1 + i / (INDEX_MAX_ECPAIRING_DATA_MIN + 1))
                  : Bytes.of(0))
          .internalChecksPassed(internalChecksPassed)
          .hurdle(hurdle.get(i))
          .byteDelta(
              i < nBYTES_OF_DELTA_BYTES ? UnsignedByte.of(deltaByte.get(i)) : UnsignedByte.of(0))
          .ct((short) ct)
          .ctMax(
              (short) (isSmallPoint ? CT_MAX_SMALL_POINT : (isLargePoint ? CT_MAX_LARGE_POINT : 0)))
          .isSmallPoint(isSmallPoint)
          .isLargePoint(isLargePoint)
          .notOnG2(notOnG2.get(i))
          .notOnG2Acc(notOnG2Acc.get(i))
          .notOnG2AccMax(notOnG2AccMax)
          .isInfinity(isInfinity.get(i))
          .overallTrivialPairing(
              ecType == ECPAIRING
                  && isData
                  && overallTrivialPairing.get(
                      i)) // && conditions necessary because default value is true
          .g2MembershipTestRequired(g2MembershipTestRequired)
          .acceptablePairOfPointForPairingCircuit(acceptablePairOfPointForPairingCircuit)
          .circuitSelectorEcrecover(circuitSelectorEcrecover)
          .circuitSelectorEcadd(circuitSelectorEcadd)
          .circuitSelectorEcmul(circuitSelectorEcmul)
          .circuitSelectorEcpairing(acceptablePairOfPointForPairingCircuit)
          .circuitSelectorG2Membership(g2MembershipTestRequired)
          .wcpFlag(wcpFlag.get(i))
          .wcpArg1Hi(wcpArg1Hi.get(i))
          .wcpArg1Lo(wcpArg1Lo.get(i))
          .wcpArg2Hi(wcpArg2Hi.get(i))
          .wcpArg2Lo(wcpArg2Lo.get(i))
          .wcpRes(wcpRes.get(i))
          .wcpInst(wcpInst.get(i).unsignedByteValue())
          .extFlag(extFlag.get(i))
          .extArg1Hi(extArg1Hi.get(i))
          .extArg1Lo(extArg1Lo.get(i))
          .extArg2Hi(extArg2Hi.get(i))
          .extArg2Lo(extArg2Lo.get(i))
          .extArg3Hi(extArg3Hi.get(i))
          .extArg3Lo(extArg3Lo.get(i))
          .extResHi(extResHi.get(i))
          .extResLo(extResLo.get(i))
          .extInst(extInst.get(i).unsignedByteValue())
          .validateRow();

      // Update ct, isSmallPoint, isLargePoint
      if (ecType == ECPAIRING && isData) {
        ct++;
        if (isSmallPoint && ct == CT_MAX_SMALL_POINT + 1) {
          isSmallPoint = false;
          isLargePoint = true;
          ct = 0;
        } else if (isLargePoint && ct == CT_MAX_LARGE_POINT + 1) {
          isLargePoint = false;
          ct = 0;
          smallPointIsAtInfinity = false;
          largePointIsAtInfinity = false;
        }
      }
    }
  }

  @Override
  protected int computeLineCount() {
    return nRowsData + nRowsResult;
  }

  private Pair<Boolean, Boolean> callToC1Membership(int k, EWord pX, EWord pY) {
    // EXT
    EWord pYSquare = callExt(k, OpCode.MULMOD, pY, pY, P_BN);
    EWord pXSquare = callExt(k + 1, OpCode.MULMOD, pX, pX, P_BN);
    EWord pXCube = callExt(k + 2, OpCode.MULMOD, pXSquare, pX, P_BN);
    EWord pXCubePlus3 = callExt(k + 3, OpCode.ADDMOD, pXCube, EWord.of(3), P_BN);

    // WCP
    boolean pXIsInRange = callWcp(k, OpCode.LT, pX, P_BN);
    boolean pYIsInRange = callWcp(k + 1, OpCode.LT, pY, P_BN);
    boolean pSatisfiesCubic = callWcp(k + 2, OpCode.EQ, pYSquare, pXCubePlus3);

    // Set hurdle
    boolean pIsRange = pXIsInRange && pYIsInRange;
    boolean pIsPointAtInfinity = pIsRange && pX.isZero() && pY.isZero();
    boolean c1Membership = pIsRange && (pIsPointAtInfinity || pSatisfiesCubic);
    hurdle.set(k + 1, pIsRange);
    hurdle.set(k, c1Membership);

    // Set isInfinity
    for (int i = 0; i <= CT_MAX_SMALL_POINT; i++) {
      isInfinity.set(i + k, pIsPointAtInfinity);
    }

    return Pair.of(c1Membership, pIsPointAtInfinity);
  }

  private Pair<Boolean, Boolean> callToWellFormedCoordinates(
      int k, EWord bXIm, EWord bXRe, EWord bYIm, EWord bYRe) {
    // WCP
    boolean bXImIsInRange = callWcp(k, OpCode.LT, bXIm, P_BN);
    boolean bXReIsInRange = callWcp(k + 1, OpCode.LT, bXRe, P_BN);
    boolean bYImIsInRange = callWcp(k + 2, OpCode.LT, bYIm, P_BN);
    boolean bYReIsInRange = callWcp(k + 3, OpCode.LT, bYRe, P_BN);

    // Set hurdle
    boolean bXIsRange = bXImIsInRange && bXReIsInRange;
    boolean bYIsRange = bYImIsInRange && bYReIsInRange;
    boolean wellFormedCoordinates = bXIsRange && bYIsRange;
    boolean bIsPointAtInfinity =
        wellFormedCoordinates && bXIm.isZero() && bXRe.isZero() && bYIm.isZero() && bYRe.isZero();
    hurdle.set(k + 2, bXIsRange);
    hurdle.set(k + 1, bYIsRange);
    hurdle.set(k, wellFormedCoordinates);

    // Set isInfinity
    for (int i = 0; i <= CT_MAX_LARGE_POINT; i++) {
      isInfinity.set(i + k, bIsPointAtInfinity);
    }

    return Pair.of(wellFormedCoordinates, bIsPointAtInfinity);
  }
}
