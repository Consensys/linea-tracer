package net.consensys.linea.zktracer.module.mxp.moduleCall;

import static net.consensys.linea.zktracer.Trace.Mxpcan.CT_MAX_MXPX;
import static net.consensys.linea.zktracer.Trace.Mxpcan.MXPX_THRESHOLD;
import static net.consensys.linea.zktracer.types.Conversions.booleanToInt;

import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.mxp.MxpExoCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import org.apache.tuweni.bytes.Bytes;

public class CancunMxpxMxpCall extends CancunTrivialMxpCall {

  static final Bytes mxpxThreshold = Bytes.ofUnsignedLong(MXPX_THRESHOLD);

  public CancunMxpxMxpCall(Hub hub, Wcp wcp, Euc euc) {
    super(hub, wcp);
    computeMxpxExpression(wcp);
    setMxpxFromMxpxExpression();
    setMayTriggerNontrivialMmuOperationFromMxpx();
  }

  @Override
  public boolean isMxpxScenario() {
    return true;
  }

  public void computeMxpxExpression(Wcp wcp) {
    // Row i + 3
    exoCalls.add(MxpExoCall.callToLEQ(wcp, this.size1, mxpxThreshold));
    // Row i + 4
    exoCalls.add(MxpExoCall.callToLEQ(wcp, this.size2, mxpxThreshold));
    // Row i + 5
    exoCalls.add(MxpExoCall.callToLEQ(wcp, this.offset1, mxpxThreshold));
    // Row i + 6
    exoCalls.add(MxpExoCall.callToLEQ(wcp, this.offset2, mxpxThreshold));

    final boolean size1IsNonZero = !this.size1IsZero;
    final boolean size2IsNonZero = !this.size2IsZero;
    final boolean size1IsSmall = exoCalls.get(2).resultA(); // result of row i + 3
    final boolean size1IsLarge = !size1IsSmall;
    final boolean size2IsSmall = exoCalls.get(3).resultA();
    final boolean size2IsLarge = !size2IsSmall;
    final boolean offset1IsSmall = exoCalls.get(4).resultA();
    final boolean offset1IsLarge = !offset1IsSmall;
    final boolean offset2IsSmall = exoCalls.get(5).resultA();
    final boolean offset2IsLarge = !offset2IsSmall;
    final int mxpxExpression1 =
        booleanToInt(size1IsLarge) + booleanToInt(size1IsNonZero) * booleanToInt(offset1IsLarge);
    final int mxpxExpression2 =
        booleanToInt(size2IsLarge) + booleanToInt(size2IsNonZero) * booleanToInt(offset2IsLarge);

    this.mxpxExpression = mxpxExpression1 + mxpxExpression2;
  }

  @Override
  public int ctMax() {
    return CT_MAX_MXPX;
  }
}
