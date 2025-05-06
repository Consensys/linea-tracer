package net.consensys.linea.zktracer.module.mxp.scenario;

import static net.consensys.linea.zktracer.Trace.Mxp.CT_MAX_TRIV;

import net.consensys.linea.zktracer.module.hub.Hub;
import org.hyperledger.besu.evm.frame.MessageFrame;

public class TrivialMxpScenario extends MxpScenario {

  @Override
  public boolean isTrivialScenario() {
    return true;
  }

  @Override
  public void setInputData(MessageFrame frame, Hub hub) {
    // No input data to set for trivial scenario
  }

  @Override
  public void callExoModules() {
    // No exo modules to call for trivial scenario
  }

  @Override
  public void compute() {
    // No computation needed for trivial scenario
  }

  @Override
  public int ctMax() {
    return CT_MAX_TRIV;
  }
}
