package net.consensys.linea.zktracer.module.mxp.moduleCall;

import static net.consensys.linea.zktracer.Trace.Mxpcan.CT_MAX_MSIZE;

import net.consensys.linea.zktracer.module.hub.Hub;

public class CancunMSizeMxpCall extends CancunMxpCall {

  public CancunMSizeMxpCall(Hub hub) {
    super(hub);
    // Nothing to compute for MSize scenario
  }

  @Override
  public boolean isMSizeScenario() {
    return true;
  }

  @Override
  public int ctMax() {
    return CT_MAX_MSIZE;
  }
}
