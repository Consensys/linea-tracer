package net.consensys.linea.zktracer.module.mxp.moduleCall;

import static net.consensys.linea.zktracer.Trace.Mxpcan.CT_MAX_MSIZE;

import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.wcp.Wcp;

public class CancunMSizeMxpCall extends CancunMxpCall {

  public CancunMSizeMxpCall(Hub hub) {
    super(hub);
    this.gasMxp = 0L;
    this.mxpx = this.mxpxExpression != 0;
    this.mayTriggerNontrivialMmuOperation = !this.size1.isZero() && !this.mxpx;
  }

  @Override
  public boolean isMSizeScenario() {
    return true;
  }

  @Override
  public void compute(Wcp wcp, Euc euc) {
    // Nothing to compute for MSize scenario
  }

  @Override
  public int ctMax() {
    return CT_MAX_MSIZE;
  }
}
