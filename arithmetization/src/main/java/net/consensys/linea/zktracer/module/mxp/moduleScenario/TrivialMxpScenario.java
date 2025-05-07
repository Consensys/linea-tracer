package net.consensys.linea.zktracer.module.mxp.moduleScenario;

import static net.consensys.linea.zktracer.Trace.Mxp.CT_MAX_TRIV;
import static net.consensys.linea.zktracer.types.Conversions.bytesToBoolean;

import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.module.mxp.MxpExoCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;

public class TrivialMxpScenario extends MSizeMxpScenario {

  @Override
  public boolean isTrivialScenario() {
    return true;
  }

  public void computeSize1Size2IsZero(MxpCall mxpCall, Wcp wcp) {
    // Row i + 1
    exoCalls.add(MxpExoCall.callToIsZero(wcp, mxpCall.getOffset1()));
    // Row i + 2
    exoCalls.add(MxpExoCall.callToIsZero(wcp, mxpCall.getOffset2()));

    this.size1IsZero = bytesToBoolean(exoCalls.get(0).resultA()); // result of row i + 1
    this.size2IsZero = bytesToBoolean(exoCalls.get(1).resultA()); // result of row i + 2
  }

  @Override
  public void compute(MxpCall mxpCall, Wcp wcp, Euc euc) {
    computeSize1Size2IsZero(mxpCall, wcp);
  }

  @Override
  public int ctMax() {
    return CT_MAX_TRIV;
  }
}
