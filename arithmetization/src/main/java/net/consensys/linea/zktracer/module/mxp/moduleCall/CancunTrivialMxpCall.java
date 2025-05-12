package net.consensys.linea.zktracer.module.mxp.moduleCall;

import static net.consensys.linea.zktracer.Trace.Mxpcan.CT_MAX_TRIV;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.mxp.MxpExoCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;

public class CancunTrivialMxpCall extends CancunMSizeMxpCall {

  public CancunTrivialMxpCall(Hub hub, Wcp wcp) {
    super(hub);
    computeSize1Size2IsZero(wcp);
  }

  @Override
  public boolean isTrivialScenario() {
    return true;
  }

  private void computeSize1Size2IsZero(Wcp wcp) {
    // Row i + 1
    exoCalls.add(MxpExoCall.callToIsZero(wcp, this.offset1));
    // Row i + 2
    exoCalls.add(MxpExoCall.callToIsZero(wcp, this.offset2));

    this.size1IsZero = exoCalls.get(0).resultA(); // result of row i + 1
    this.size2IsZero = exoCalls.get(1).resultA(); // result of row i + 2
  }

  @Override
  public int ctMax() {
    return CT_MAX_TRIV;
  }
}
