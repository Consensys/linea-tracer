package net.consensys.linea.zktracer.module.mxp.moduleScenario;

import static net.consensys.linea.zktracer.Trace.Mxp.CT_MAX_MXPX;
import static net.consensys.linea.zktracer.Trace.Mxp.MXPX_THRESHOLD;
import static net.consensys.linea.zktracer.types.Conversions.booleanToInt;
import static net.consensys.linea.zktracer.types.Conversions.bytesToBoolean;

import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.module.mxp.MxpExoCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import org.apache.tuweni.bytes.Bytes;

public class MxpxMxpScenario extends TrivialMxpScenario {

  @Override
  public boolean isMxpxScenario() {
    return true;
  }

  public void computeMxpxExpression(MxpCall mxpCall, Wcp wcp) {
    // Row i + 3
    exoCalls.add(
        MxpExoCall.callToLEQ(wcp, mxpCall.getSize1(), Bytes.ofUnsignedLong(MXPX_THRESHOLD)));
    // Row i + 4
    exoCalls.add(
        MxpExoCall.callToLEQ(wcp, mxpCall.getSize2(), Bytes.ofUnsignedLong(MXPX_THRESHOLD)));
    // Row i + 5
    exoCalls.add(
        MxpExoCall.callToLEQ(wcp, mxpCall.getOffset1(), Bytes.ofUnsignedLong(MXPX_THRESHOLD)));
    // Row i + 6
    exoCalls.add(
        MxpExoCall.callToLEQ(wcp, mxpCall.getOffset2(), Bytes.ofUnsignedLong(MXPX_THRESHOLD)));

    boolean size1IsNonZero = !this.size1IsZero;
    boolean size2IsNonZero = !this.size2IsZero;
    boolean size1IsSmall = bytesToBoolean(exoCalls.get(2).resultA()); // result of row i + 3
    boolean size1IsLarge = !size1IsSmall;
    boolean size2IsSmall = bytesToBoolean(exoCalls.get(3).resultA());
    boolean size2IsLarge = !size2IsSmall;
    boolean offset1IsSmall = bytesToBoolean(exoCalls.get(4).resultA());
    boolean offset1IsLarge = !offset1IsSmall;
    boolean offset2IsSmall = bytesToBoolean(exoCalls.get(5).resultA());
    boolean offset2IsLarge = !offset2IsSmall;
    int mxpxExpression1 =
        booleanToInt(size1IsLarge) + booleanToInt(size1IsNonZero) * booleanToInt(offset1IsLarge);
    int mxpxExpression2 =
        booleanToInt(size2IsLarge) + booleanToInt(size2IsNonZero) * booleanToInt(offset2IsLarge);

    this.mxpxExpression = mxpxExpression1 + mxpxExpression2;
  }

  @Override
  public void compute(MxpCall mxpCall, Wcp wcp, Euc euc) {
    computeSize1Size2IsZero(mxpCall, wcp);
    computeMxpxExpression(mxpCall, wcp);
  }

  @Override
  public int ctMax() {
    return CT_MAX_MXPX;
  }
}
