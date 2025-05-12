package net.consensys.linea.zktracer.module.mxp.moduleCall;

import static net.consensys.linea.zktracer.Trace.Mxpcan.CT_MAX_TRIV;

import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.mxp.MxpExoCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;

public class CancunTrivialMxpCall extends CancunMSizeMxpCall {

  public CancunTrivialMxpCall(Hub hub) {
    super(hub);
    this.gasMxp = 0L;
    this.mxpx = this.mxpxExpression != 0;
    this.mayTriggerNontrivialMmuOperation = !this.size1.isZero() && !this.mxpx;
  }

  @Override
  public boolean isTrivialScenario() {
    return true;
  }

  public void computeSize1Size2IsZero(Wcp wcp) {
    // Row i + 1
    exoCalls.add(MxpExoCall.callToIsZero(wcp, this.offset1));
    // Row i + 2
    exoCalls.add(MxpExoCall.callToIsZero(wcp, this.offset2));

    this.size1IsZero = exoCalls.get(0).resultA(); // result of row i + 1
    this.size2IsZero = exoCalls.get(1).resultA(); // result of row i + 2
  }

  @Override
  public void compute(Wcp wcp, Euc euc) {
    computeSize1Size2IsZero(wcp);
  }

  @Override
  public int ctMax() {
    return CT_MAX_TRIV;
  }
}
