package net.consensys.linea.zktracer.module.mxp.moduleCall;

import static net.consensys.linea.zktracer.TraceCancun.Mxp.MXPX_THRESHOLD;
import static net.consensys.linea.zktracer.types.Conversions.booleanToInt;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.mxp.MxpExoCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import org.apache.tuweni.bytes.Bytes;

public class CancunNotMSizeNorTrivialMxpCall extends CancunTrivialMxpCall {

  static final Bytes mxpxThreshold = Bytes.ofUnsignedLong(MXPX_THRESHOLD);

  public CancunNotMSizeNorTrivialMxpCall(Hub hub, Wcp wcp) {
    super(hub, wcp);
    computeMxpxExpression(wcp);
    setMxpxFromMxpxExpression();
  }

  public void computeMxpxExpression(Wcp wcp) {
    // We compute and assign the computation's result for each row

    // Row i + 3
    // Compute size1IsSmall
    exoCalls[2] = MxpExoCall.callToLEQ(wcp, this.size1, mxpxThreshold);
    final boolean size1IsSmall = exoCalls[2].resultA();

    // Row i + 4
    // Compute size2IsSmall
    exoCalls[3] = MxpExoCall.callToLEQ(wcp, this.size2, mxpxThreshold);
    final boolean size2IsSmall = exoCalls[3].resultA();

    // Row i + 5
    // Compute offset1IsSmall
    exoCalls[4] = MxpExoCall.callToLEQ(wcp, this.offset1, mxpxThreshold);
    final boolean offset1IsSmall = exoCalls[4].resultA();

    // Row i + 6
    // Compute offset2IsSmall
    exoCalls[5] = MxpExoCall.callToLEQ(wcp, this.offset2, mxpxThreshold);
    final boolean offset2IsSmall = exoCalls[5].resultA();

    final boolean size1IsNonZero = !this.size1IsZero;
    final boolean size2IsNonZero = !this.size2IsZero;
    final boolean size1IsLarge = !size1IsSmall;
    final boolean size2IsLarge = !size2IsSmall;
    final boolean offset1IsLarge = !offset1IsSmall;
    final boolean offset2IsLarge = !offset2IsSmall;
    final int mxpxExpression1 =
        booleanToInt(size1IsLarge) + booleanToInt(size1IsNonZero) * booleanToInt(offset1IsLarge);
    final int mxpxExpression2 =
        booleanToInt(size2IsLarge) + booleanToInt(size2IsNonZero) * booleanToInt(offset2IsLarge);

    this.mxpxExpression = mxpxExpression1 + mxpxExpression2;
  }
}
