package net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.common.bls.msm;

import static net.consensys.linea.zktracer.Trace.OOB_INST_BLS_G2MSM;
import static net.consensys.linea.zktracer.Trace.PRC_BLS_G2MSM_MAX_DISCOUNT;
import static net.consensys.linea.zktracer.Trace.PRC_BLS_G2MSM_SIZE_MIN;
import static net.consensys.linea.zktracer.Trace.PRC_BLS_MULTIPLICATION_MULTIPLIER;
import static net.consensys.linea.zktracer.TraceCancun.Oob.CT_MAX_BLS_G2MSM;

import java.math.BigInteger;

import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment;

public class BlsG2MsmOobCall extends BlsMsmOobCall {
  public BlsG2MsmOobCall(BigInteger calleeGas) {
    super(calleeGas);
  }

  @Override
  protected void traceOobInstructionInOob(Trace.Oob trace) {
    trace.isBlsG2Msm(true).oobInst(OOB_INST_BLS_G2MSM);
  }

  @Override
  protected void traceOobInstructionInHub(Trace.Hub trace) {
    trace.pMiscOobInst(OOB_INST_BLS_G2MSM);
  }

  @Override
  public int ctMax() {
    return CT_MAX_BLS_G2MSM;
  }

  @Override
  long minMsmSize() {
    return PRC_BLS_G2MSM_SIZE_MIN;
  }

  @Override
  PrecompileScenarioFragment.PrecompileFlag getPrecompileFlag() {
    return PrecompileScenarioFragment.PrecompileFlag.PRC_BLS_G2_MSM;
  }

  @Override
  long maxDiscount() {
    return PRC_BLS_G2MSM_MAX_DISCOUNT;
  }

  @Override
  long msmMultiplicationCost() {
    return PRC_BLS_MULTIPLICATION_MULTIPLIER;
  }
}
