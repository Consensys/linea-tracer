package net.consensys.linea.zktracer.module.mxp.scenario;

import static net.consensys.linea.zktracer.Trace.Mxp.CT_MAX_MSIZE;

import net.consensys.linea.zktracer.module.hub.Hub;
import org.hyperledger.besu.evm.frame.MessageFrame;

public class MSizeMxpScenario extends MxpScenario {

  @Override
  public boolean isMSizeScenario() {
    return true;
  }

  @Override
  public void setInputData(MessageFrame frame, Hub hub) {
    // Implementation for setting input data
  }

  @Override
  public void callExoModules() {
    // Implementation for calling exo modules
  }

  @Override
  public void compute() {
    // Implementation for compute logic
  }

  @Override
  public int ctMax() {
    return CT_MAX_MSIZE;
  }
}
