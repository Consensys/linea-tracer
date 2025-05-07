package net.consensys.linea.zktracer.module.mxp.moduleScenario;

import static net.consensys.linea.zktracer.Trace.GAS_CONST_G_MEMORY;
import static net.consensys.linea.zktracer.Trace.Mxp.CT_MAX_UPDT_W;
import static net.consensys.linea.zktracer.module.mxp.MxpUtils.isDoubleOffsetOpcode;
import static net.consensys.linea.zktracer.module.mxp.MxpUtils.memoryCost;
import static net.consensys.linea.zktracer.types.Conversions.*;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;

import java.math.BigInteger;

import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.module.mxp.MxpExoCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.gas.BillingRate;
import org.apache.tuweni.bytes.Bytes;

public class StateUpdtWPricingMxpScenario extends MxpxMxpScenario {

  @Override
  public boolean isStateUpdtWPricingScenario() {
    return true;
  }

  public void computeStateUpdt(MxpCall mxpCall, Wcp wcp, Euc euc) {
    OpCode opCode = mxpCall.getOpCodeData().mnemonic();
    var words = mxpCall.getMemorySizeInWords();
    var cMem = memoryCost(words);

    // we filter the row i + 7 wcp call by double_offset to prevent unnecessary comparisons
    if (isDoubleOffsetOpcode(opCode)) {
      // Row i + 7
      var max1 =
          mxpCall
              .getOffset1()
              .toUnsignedBigInteger()
              .add(mxpCall.getSize1().toUnsignedBigInteger());
      var max2 =
          mxpCall
              .getOffset2()
              .toUnsignedBigInteger()
              .add(mxpCall.getSize2().toUnsignedBigInteger());
      exoCalls.add(MxpExoCall.callToLT(wcp, bigIntegerToBytes(max1), bigIntegerToBytes(max2)));
    }

    // Row i + 8
    boolean useParams2 = bytesToBoolean(exoCalls.get(6).resultA()); // result of row i + 7
    boolean useParams1 = !useParams2;
    var maxOffset1 =
        mxpCall
            .getOffset1()
            .lo()
            .toUnsignedBigInteger()
            .add(mxpCall.getSize1().lo().toUnsignedBigInteger())
            .subtract(BigInteger.ONE);
    var maxOffset2 =
        mxpCall
            .getOffset2()
            .lo()
            .toUnsignedBigInteger()
            .add(mxpCall.getSize2().lo().toUnsignedBigInteger())
            .subtract(BigInteger.ONE);
    ;
    var maxOffset =
        booleanToBigInteger(useParams1)
            .multiply(maxOffset1)
            .add(booleanToBigInteger(useParams2).multiply(maxOffset2));
    exoCalls.add(MxpExoCall.callToEUC(euc, bigIntegerToBytes(maxOffset), Bytes.of(32)));

    // row i + 9
    var floor = exoCalls.get(7).resultB();
    var EYPa = floor.toUnsignedBigInteger().add(BigInteger.ONE);
    exoCalls.add(MxpExoCall.callToEUC(euc, bigIntegerToBytes(EYPa.multiply(EYPa)), Bytes.of(512)));

    // row i + 10
    exoCalls.add(MxpExoCall.callToLT(wcp, longToBytes(words), bigIntegerToBytes(EYPa)));

    // Compute state update
    var cMemQuadPart = exoCalls.get(8).resultB();
    var cMemLinearPart = bigIntegerToBytes(EYPa.multiply(BigInteger.valueOf(GAS_CONST_G_MEMORY)));
    var updateInternalState = bytesToBoolean(exoCalls.get(9).resultA());
    this.wordsNew = updateInternalState ? EYPa.longValue() : words;
    this.cMemNew =
        updateInternalState
            ? bigIntegerToBytes(
                    cMemQuadPart.toUnsignedBigInteger().add(cMemLinearPart.toUnsignedBigInteger()))
                .toLong()
            : cMem;
  }

  public void computeExtraGasCost(MxpCall mxpCall, Euc euc) {
    var gWord = mxpCall.getCostBy(BillingRate.BY_WORD);
    // Row i + 11
    exoCalls.add(MxpExoCall.callToEUC(euc, mxpCall.getSize1().lo(), Bytes.of(32)));
    var numberOfWords = exoCalls.get(10).resultB(); // result of row i + 11
    this.extraGasCost =
        numberOfWords.toUnsignedBigInteger().multiply(gWord.toUnsignedBigInteger()).longValue();
  }

  @Override
  public void compute(MxpCall mxpCall, Wcp wcp, Euc euc) {
    computeSize1Size2IsZero(mxpCall, wcp);
    computeMxpxExpression(mxpCall, wcp);
    computeStateUpdt(mxpCall, wcp, euc);
    computeExtraGasCost(mxpCall, euc);
  }

  @Override
  public int ctMax() {
    return CT_MAX_UPDT_W;
  }
}
