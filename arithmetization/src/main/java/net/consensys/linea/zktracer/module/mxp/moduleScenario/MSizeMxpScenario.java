package net.consensys.linea.zktracer.module.mxp.moduleScenario;

import static net.consensys.linea.zktracer.Trace.Mxpcan.CT_MAX_MSIZE;

import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;

public class MSizeMxpScenario extends MxpScenario {

  @Override
  public boolean isMSizeScenario() {
    return true;
  }

  @Override
  public void compute(MxpCall mxpCall, Wcp wcp, Euc euc) {
    // Nothing to compute for MSize scenario
  }

  @Override
  public int ctMax() {
    return CT_MAX_MSIZE;
  }
}
