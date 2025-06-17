package net.consensys.linea.zktracer.module.mxp.moduleCall;

import static net.consensys.linea.zktracer.Trace.GAS_CONST_G_MEMORY;
import static net.consensys.linea.zktracer.TraceCancun.Mxp.CT_MAX_UPDT_W;
import static net.consensys.linea.zktracer.module.mxp.MxpUtils.isDoubleOffsetOpcode;
import static net.consensys.linea.zktracer.types.Conversions.*;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;

import java.math.BigInteger;

import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.mxp.MxpExoCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;

public class CancunStateUpdtWPricingMxpCall extends CancunMxpxMxpCall {

  public CancunStateUpdtWPricingMxpCall(Hub hub, Wcp wcp, Euc euc) {
    super(hub, wcp);
    computeStateUpdt(wcp, euc);
    computeExtraGasCost(euc);
    if (this.isStateUpdate) {
      // if state has changed, an extra gas cost is incurred
      setGasMpxFromExtraGasCost();
    }
  }

  @Override
  public boolean isStateUpdtWPricingScenario() {
    return true;
  }

  public void computeStateUpdt(Wcp wcp, Euc euc) {
    final OpCode opCode = this.opCodeData.mnemonic();

    // we filter the row i + 7 wcp call by double_offset to prevent unnecessary comparisons
    if (isDoubleOffsetOpcode(opCode)) {
      // Row i + 7
      final var max1 = this.offset1.toUnsignedBigInteger().add(this.size1.toUnsignedBigInteger());
      final var max2 = this.offset2.toUnsignedBigInteger().add(this.size2.toUnsignedBigInteger());
      exoCalls.add(MxpExoCall.callToLT(wcp, bigIntegerToBytes(max1), bigIntegerToBytes(max2)));
    }

    // Row i + 8
    final boolean useParams2 = exoCalls.get(6).resultA(); // result of row i + 7
    final boolean useParams1 = !useParams2;
    final var maxOffset1 =
        this.offset1
            .lo()
            .toUnsignedBigInteger()
            .add(this.size1.lo().toUnsignedBigInteger())
            .subtract(BigInteger.ONE);
    final var maxOffset2 =
        this.offset2
            .lo()
            .toUnsignedBigInteger()
            .add(this.size2.lo().toUnsignedBigInteger())
            .subtract(BigInteger.ONE);
    ;
    final var maxOffset =
        booleanToBigInteger(useParams1)
            .multiply(maxOffset1)
            .add(booleanToBigInteger(useParams2).multiply(maxOffset2));
    exoCalls.add(MxpExoCall.callToEUC(euc, bigIntegerToBytes(maxOffset), Bytes.of(32)));

    // row i + 9
    final var floor = exoCalls.get(7).resultB();
    final var EYPa = floor.toUnsignedBigInteger().add(BigInteger.ONE);
    exoCalls.add(MxpExoCall.callToEUC(euc, bigIntegerToBytes(EYPa.multiply(EYPa)), Bytes.of(512)));

    // row i + 10
    exoCalls.add(MxpExoCall.callToLT(wcp, longToBytes(words), bigIntegerToBytes(EYPa)));

    // Compute state update
    final var cMemQuadPart = exoCalls.get(8).resultB();
    final var cMemLinearPart =
        bigIntegerToBytes(EYPa.multiply(BigInteger.valueOf(GAS_CONST_G_MEMORY)));
    final var updateInternalState = exoCalls.get(9).resultA();
    this.isStateUpdate = updateInternalState;
    this.wordsNew = updateInternalState ? EYPa.longValue() : this.words;
    this.cMemNew =
        updateInternalState
            ? bigIntegerToBytes(
                    cMemQuadPart.toUnsignedBigInteger().add(cMemLinearPart.toUnsignedBigInteger()))
                .toLong()
            : this.cMem;
  }

  private void computeExtraGasCost(Euc euc) {
    // Row i + 11
    exoCalls.add(MxpExoCall.callToEUC(euc, this.size1.lo(), Bytes.of(32)));
    var numberOfWords = exoCalls.get(10).resultB(); // result of row i + 11
    this.extraGasCost =
        numberOfWords
            .toUnsignedBigInteger()
            .multiply(this.gWord.toUnsignedBigInteger())
            .longValue();
  }

  @Override
  public int ctMax() {
    return CT_MAX_UPDT_W;
  }
}
