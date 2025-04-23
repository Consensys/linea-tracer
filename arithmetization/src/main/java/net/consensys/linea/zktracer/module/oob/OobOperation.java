/*
 * Copyright ConsenSys AG.
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

package net.consensys.linea.zktracer.module.oob;

import static com.google.common.math.BigIntegerMath.log2;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static net.consensys.linea.zktracer.Trace.*;
import static net.consensys.linea.zktracer.Trace.Oob.G_QUADDIVISOR;
import static net.consensys.linea.zktracer.module.hub.precompiles.ModexpMetadata.BASE_MIN_OFFSET;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBoolean;
import static net.consensys.linea.zktracer.types.Utils.rightPadTo;

import java.math.BigInteger;
import java.math.RoundingMode;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.add.Add;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.imc.oob.OobCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.ModexpCallDataSizeOobCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.ModexpExtractOobCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.ModexpLeadOobCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.ModexpPricingOobCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.ModexpXbsOobCall;
import net.consensys.linea.zktracer.module.mod.Mod;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.frame.MessageFrame;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class OobOperation extends ModuleOperation {
  @EqualsAndHashCode.Include @Setter public OobCall oobCall;

  private BigInteger precompileCost;
  boolean insufficientGasForPrecompile;

  public int ctMax() {
    return oobCall.ctMax();
  }

  public int nRows() {
    return ctMax() + 1;
  }

  public OobOperation(
      OobCall oobCall,
      final Hub hub,
      final MessageFrame frame,
      final Add add,
      final Mod mod,
      final Wcp wcp) {
    this.oobCall = oobCall;

    oobCall.setInputData(frame, hub);
    oobCall.callExoModules(add, mod, wcp);
  }

  public void populateColumnsForPrecompile(MessageFrame frame) {
    // final OpCode opCode = getOpCode(frame);
    // final long argsOffset =
    //     clampedToLong(
    //         opCode.callHasValueArgument()
    //             ? hub.messageFrame().getStackItem(3)
    //             : hub.messageFrame().getStackItem(2));
    // final int cdsIndex = opCode.callHasValueArgument() ? 4 : 3;
    // final int returnAtCapacityIndex = opCode.callHasValueArgument() ? 6 : 5;

    if (oobCall instanceof ModexpPricingOobCall) {
      calleeGas = ((ModexpPricingOobCall) oobCall).getCallGas();
    }

    // final BigInteger cds = EWord.of(frame.getStackItem(cdsIndex)).toUnsignedBigInteger();
    //
    // final BigInteger returnAtCapacity =
    //     EWord.of(frame.getStackItem(returnAtCapacityIndex)).toUnsignedBigInteger();

    if (isModexpPrecompile()) {
      final Bytes unpaddedCallData = frame.shadowReadMemory(argsOffset, cds.longValue());
      // pad unpaddedCallData to 96
      final Bytes paddedCallData =
          cds.intValue() < 96 ? rightPadTo(unpaddedCallData, 96) : unpaddedCallData;

      final BigInteger bbs = paddedCallData.slice(0, 32).toUnsignedBigInteger();
      final BigInteger ebs = paddedCallData.slice(32, 32).toUnsignedBigInteger();
      final BigInteger mbs = paddedCallData.slice(64, 32).toUnsignedBigInteger();

      // Check if bbs, ebs and mbs are <= 512
      if (bbs.compareTo(BigInteger.valueOf(512)) > 0
          || ebs.compareTo(BigInteger.valueOf(512)) > 0
          || mbs.compareTo(BigInteger.valueOf(512)) > 0) {
        throw new IllegalArgumentException("byte sizes are too big");
      }
      int exponentLog =
          computeExponentLog(paddedCallData, cds.intValue(), bbs.intValue(), ebs.intValue());
      switch (oobCall.oobInstruction) {
        case OOB_INST_MODEXP_CDS -> {
          final ModexpCallDataSizeOobCall prcModexpCdsCall = (ModexpCallDataSizeOobCall) oobCall;
          prcModexpCdsCall.setCds(cds);
          setModexpCds(prcModexpCdsCall);
        }
        case OOB_INST_MODEXP_XBS -> {
          final ModexpXbsOobCall prcModexpXbsOobCall;
          switch (((ModexpXbsOobCall) oobCall).getModexpXbsCase()) {
            case OOB_INST_MODEXP_BBS -> {
              prcModexpXbsOobCall = (ModexpXbsOobCall) oobCall;
              prcModexpXbsOobCall.setXbsHi(EWord.of(bbs).hiBigInt());
              prcModexpXbsOobCall.setXbsLo(EWord.of(bbs).loBigInt());
              prcModexpXbsOobCall.setYbsLo(BigInteger.ZERO);
              prcModexpXbsOobCall.setComputeMax(false);
            }
            case OOB_INST_MODEXP_EBS -> {
              prcModexpXbsOobCall = (ModexpXbsOobCall) oobCall;
              prcModexpXbsOobCall.setXbsHi(EWord.of(ebs).hiBigInt());
              prcModexpXbsOobCall.setXbsLo(EWord.of(ebs).loBigInt());
              prcModexpXbsOobCall.setYbsLo(BigInteger.ZERO);
              prcModexpXbsOobCall.setComputeMax(false);
            }
            case OOB_INST_MODEXP_MBS -> {
              prcModexpXbsOobCall = (ModexpXbsOobCall) oobCall;
              prcModexpXbsOobCall.setXbsHi(EWord.of(mbs).hiBigInt());
              prcModexpXbsOobCall.setXbsLo(EWord.of(mbs).loBigInt());
              prcModexpXbsOobCall.setYbsLo(EWord.of(bbs).loBigInt());
              prcModexpXbsOobCall.setComputeMax(true);
            }
            default -> throw new RuntimeException("modexpXbsCase is not set to a valid value");
          }
          setModexpXbs(prcModexpXbsOobCall);
        }
        case OOB_INST_MODEXP_LEAD -> {
          final ModexpLeadOobCall prcModexpLeadOobCall = (ModexpLeadOobCall) oobCall;
          prcModexpLeadOobCall.setBbs(bbs);
          prcModexpLeadOobCall.setCds(cds);
          prcModexpLeadOobCall.setEbs(ebs);
          setModexpLead(prcModexpLeadOobCall);
        }
        case OOB_INST_MODEXP_PRICING -> {
          int maxMbsBbs = max(mbs.intValue(), bbs.intValue());
          final ModexpPricingOobCall prcModexpPricingOobCall = (ModexpPricingOobCall) oobCall;
          // prcModexpPricingOobCall.setCallGas(calleeGas);
          prcModexpPricingOobCall.setReturnAtCapacity(returnAtCapacity);
          prcModexpPricingOobCall.setExponentLog(BigInteger.valueOf(exponentLog));
          prcModexpPricingOobCall.setMaxMbsBbs(maxMbsBbs);
          setModexpPricing(prcModexpPricingOobCall);
        }
        case OOB_INST_MODEXP_EXTRACT -> {
          final ModexpExtractOobCall prcModexpExtractOobCall = (ModexpExtractOobCall) oobCall;
          prcModexpExtractOobCall.setCds(cds);
          prcModexpExtractOobCall.setBbs(bbs);
          prcModexpExtractOobCall.setEbs(ebs);
          prcModexpExtractOobCall.setMbs(mbs);
          setModexpExtract(prcModexpExtractOobCall);
        }
      }
    }
  }

  // Support method for MODEXP
  public static int computeExponentLog(Bytes paddedCallData, int cds, int bbs, int ebs) {
    Preconditions.checkArgument(paddedCallData.size() >= 96);

    // pad paddedCallData to 96 + bbs + ebs
    final Bytes doublePaddedCallData =
        cds < 96 + bbs + ebs ? rightPadTo(paddedCallData, 96 + bbs + ebs) : paddedCallData;

    final BigInteger leadingBytesOfExponent =
        doublePaddedCallData.slice(96 + bbs, min(ebs, 32)).toUnsignedBigInteger();

    if (ebs <= 32 && leadingBytesOfExponent.signum() == 0) {
      return 0;
    } else if (ebs <= 32 && leadingBytesOfExponent.signum() != 0) {
      return log2(leadingBytesOfExponent, RoundingMode.FLOOR);
    } else if (ebs > 32 && leadingBytesOfExponent.signum() != 0) {
      return 8 * (ebs - 32) + log2(leadingBytesOfExponent, RoundingMode.FLOOR);
    } else {
      return 8 * (ebs - 32);
    }
  }

  private void setModexpCds(ModexpCallDataSizeOobCall prcModexpCdsCall) {
    // row i
    final boolean extractBbs =
        callToLT(0, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, prcModexpCdsCall.getCds());

    // row i + 1
    final boolean extractEbs =
        callToLT(
            1, BigInteger.ZERO, BigInteger.valueOf(32), BigInteger.ZERO, prcModexpCdsCall.getCds());

    // row i + 2
    final boolean extractMbs =
        callToLT(
            2, BigInteger.ZERO, BigInteger.valueOf(64), BigInteger.ZERO, prcModexpCdsCall.getCds());

    // Set extractBbs
    prcModexpCdsCall.setExtractBbs(extractBbs);

    // Set extractEbs
    prcModexpCdsCall.setExtractEbs(extractEbs);

    // Set extractMbs
    prcModexpCdsCall.setExtractMbs(extractMbs);
  }

  private void setModexpXbs(ModexpXbsOobCall prcModexpXbsOobCall) {
    // row i
    final boolean compTo512 =
        callToLT(
            0,
            prcModexpXbsOobCall.getXbsHi(),
            prcModexpXbsOobCall.getXbsLo(),
            BigInteger.ZERO,
            BigInteger.valueOf(513));

    // row i + 1
    final boolean comp =
        callToLT(
            1,
            BigInteger.ZERO,
            prcModexpXbsOobCall.getXbsLo(),
            BigInteger.ZERO,
            prcModexpXbsOobCall.getYbsLo());

    // row i + 2
    callToISZERO(2, BigInteger.ZERO, prcModexpXbsOobCall.getXbsLo());

    // Set maxXbsYbs and xbsNonZero
    if (!prcModexpXbsOobCall.isComputeMax()) {
      prcModexpXbsOobCall.setMaxXbsYbs(BigInteger.ZERO);
      prcModexpXbsOobCall.setXbsNonZero(false);
    } else {
      prcModexpXbsOobCall.setMaxXbsYbs(
          comp ? prcModexpXbsOobCall.getYbsLo() : prcModexpXbsOobCall.getXbsLo());
      prcModexpXbsOobCall.setXbsNonZero(!bigIntegerToBoolean(outgoingResLo[2]));
    }
  }

  private void setModexpLead(ModexpLeadOobCall prcModexpLeadOobCall) {
    // row i
    final boolean ebsIsZero = callToISZERO(0, BigInteger.ZERO, prcModexpLeadOobCall.getEbs());

    // row i + 1
    final boolean ebsLessThan32 =
        callToLT(
            1,
            BigInteger.ZERO,
            prcModexpLeadOobCall.getEbs(),
            BigInteger.ZERO,
            BigInteger.valueOf(32));

    // row i + 2
    final boolean callDataContainsExponentBytes =
        callToLT(
            2,
            BigInteger.ZERO,
            BigInteger.valueOf(96).add(prcModexpLeadOobCall.getBbs()),
            BigInteger.ZERO,
            prcModexpLeadOobCall.getCds());

    // row i + 3
    boolean comp = false;
    if (callDataContainsExponentBytes) {
      comp =
          callToLT(
              3,
              BigInteger.ZERO,
              prcModexpLeadOobCall
                  .getCds()
                  .subtract(BigInteger.valueOf(96).add(prcModexpLeadOobCall.getBbs())),
              BigInteger.ZERO,
              BigInteger.valueOf(32));
    } else {
      noCall(3);
      // Note: this noCall is not explicitly indicated in the specs since not necessary
      // Here it is done only to initialize the corresponding array elements to fill the trace
    }

    // Set loadLead
    final boolean loadLead = callDataContainsExponentBytes && !ebsIsZero;
    prcModexpLeadOobCall.setLoadLead(loadLead);

    // Set cdsCutoff
    if (!callDataContainsExponentBytes) {
      prcModexpLeadOobCall.setCdsCutoff(0);
    } else {
      prcModexpLeadOobCall.setCdsCutoff(
          comp
              ? (prcModexpLeadOobCall
                  .getCds()
                  .subtract(BigInteger.valueOf(96).add(prcModexpLeadOobCall.getBbs()))
                  .intValue())
              : 32);
    }
    // Set ebsCutoff
    prcModexpLeadOobCall.setEbsCutoff(
        ebsLessThan32 ? prcModexpLeadOobCall.getEbs().intValue() : 32);

    // Set subEbs32
    prcModexpLeadOobCall.setSubEbs32(
        ebsLessThan32 ? 0 : prcModexpLeadOobCall.getEbs().intValue() - 32);
  }

  private void setModexpPricing(ModexpPricingOobCall prcModexpPricingOobCall) {
    // row i
    final boolean returnAtCapacityIsZero =
        callToISZERO(0, BigInteger.ZERO, prcModexpPricingOobCall.getReturnAtCapacity());

    // row i + 1
    final boolean exponentLogIsZero =
        callToISZERO(1, BigInteger.ZERO, prcModexpPricingOobCall.getExponentLog());

    // row i + 2
    final BigInteger ceilingOfMaxDividedBy8 =
        callToDIV(
            2,
            BigInteger.ZERO,
            BigInteger.valueOf((long) prcModexpPricingOobCall.getMaxMbsBbs() + 7),
            BigInteger.ZERO,
            BigInteger.valueOf(8));
    final BigInteger fOfMax = ceilingOfMaxDividedBy8.multiply(ceilingOfMaxDividedBy8);

    // row i + 3
    BigInteger bigNumerator;
    if (!exponentLogIsZero) {
      bigNumerator = fOfMax.multiply(prcModexpPricingOobCall.getExponentLog());
    } else {
      bigNumerator = fOfMax;
    }
    final BigInteger bigQuotient =
        callToDIV(
            3, BigInteger.ZERO, bigNumerator, BigInteger.ZERO, BigInteger.valueOf(G_QUADDIVISOR));

    // row i + 4
    final boolean bigQuotientLT200 =
        callToLT(4, BigInteger.ZERO, bigQuotient, BigInteger.ZERO, BigInteger.valueOf(200));

    // row i + 5
    precompileCost = bigQuotientLT200 ? BigInteger.valueOf(200) : bigQuotient;

    final boolean ramSuccess =
        !callToLT(
            5,
            BigInteger.ZERO,
            prcModexpPricingOobCall.getCallGas(),
            BigInteger.ZERO,
            precompileCost);
    insufficientGasForPrecompile = !ramSuccess;

    // Set ramSuccess
    prcModexpPricingOobCall.setRamSuccess(ramSuccess);

    // Set returnGas
    final BigInteger returnGas =
        ramSuccess
            ? prcModexpPricingOobCall.getCallGas().subtract(precompileCost)
            : BigInteger.ZERO;
    prcModexpPricingOobCall.setReturnGas(returnGas);

    // Set returnAtCapacityNonZero
    prcModexpPricingOobCall.setReturnAtCapacityNonZero(!returnAtCapacityIsZero);
  }

  private void setModexpExtract(ModexpExtractOobCall prcModexpExtractOobCall) {
    // row i
    final boolean bbsIsZero = callToISZERO(0, BigInteger.ZERO, prcModexpExtractOobCall.getBbs());

    // row i + 1
    final boolean ebsIsZero = callToISZERO(1, BigInteger.ZERO, prcModexpExtractOobCall.getEbs());

    // row i + 2
    final boolean mbsIsZero = callToISZERO(2, BigInteger.ZERO, prcModexpExtractOobCall.getMbs());

    // row i + 3
    final boolean callDataExtendsBeyondExponent =
        callToLT(
            3,
            BigInteger.ZERO,
            BigInteger.valueOf(BASE_MIN_OFFSET)
                .add(prcModexpExtractOobCall.getBbs().add(prcModexpExtractOobCall.getEbs())),
            BigInteger.ZERO,
            prcModexpExtractOobCall.getCds());

    // Set extractModulus
    final boolean extractModulus = callDataExtendsBeyondExponent && !mbsIsZero;
    prcModexpExtractOobCall.setExtractModulus(extractModulus);

    // Set extractBase
    final boolean extractBase = extractModulus && !bbsIsZero;
    prcModexpExtractOobCall.setExtractBase(extractBase);

    // Set extractExponent
    final boolean extractExponent = extractModulus && !ebsIsZero;
    prcModexpExtractOobCall.setExtractExponent(extractExponent);
  }

  @Override
  protected int computeLineCount() {
    return nRows();
  }
}
