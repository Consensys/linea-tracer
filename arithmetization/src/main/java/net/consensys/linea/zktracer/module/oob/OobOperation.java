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
import static java.lang.Math.min;
import static net.consensys.linea.zktracer.Trace.*;
import static net.consensys.linea.zktracer.module.hub.precompiles.ModexpMetadata.BASE_MIN_OFFSET;
import static net.consensys.linea.zktracer.runtime.callstack.CallFrame.getOpCode;
import static net.consensys.linea.zktracer.types.Utils.rightPadTo;
import static org.hyperledger.besu.evm.internal.Words.clampedToLong;

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
import net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.ModexpExtractOobCall;
import net.consensys.linea.zktracer.module.hub.precompiles.ModexpMetadata;
import net.consensys.linea.zktracer.module.mod.Mod;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
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
    final OpCode opCode = getOpCode(frame);
    final long argsOffset =
        clampedToLong(
            opCode.callHasValueArgument()
                ? hub.messageFrame().getStackItem(3)
                : hub.messageFrame().getStackItem(2));
    final int cdsIndex = opCode.callHasValueArgument() ? 4 : 3;
    final int returnAtCapacityIndex = opCode.callHasValueArgument() ? 6 : 5;

    final BigInteger cds = EWord.of(frame.getStackItem(cdsIndex)).toUnsignedBigInteger();

    final BigInteger returnAtCapacity =
        EWord.of(frame.getStackItem(returnAtCapacityIndex)).toUnsignedBigInteger();

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
      switch (oobCall.oobInstruction) {
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
  public static int computeExponentLog(ModexpMetadata metadata, int cds) {
    final Bytes paddedCallData = metadata.callData();
    final int bbs = metadata.bbsInt();
    final int ebs = metadata.ebsInt();
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
