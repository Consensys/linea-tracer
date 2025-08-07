package net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.common.bls.msm;

import static net.consensys.linea.zktracer.Trace.OOB_INST_BLS_G2_MSM;
import static net.consensys.linea.zktracer.Trace.PRC_BLS_G2_MSM_MAX_DISCOUNT;
import static net.consensys.linea.zktracer.Trace.PRC_BLS_G2_MSM_PAIR_SIZE;
import static net.consensys.linea.zktracer.Trace.PRC_BLS_MULTIPLICATION_MULTIPLIER;
import static net.consensys.linea.zktracer.TraceCancun.Oob.CT_MAX_BLS_G2_MSM;

import java.math.BigInteger;

import net.consensys.linea.zktracer.Trace;

public class BlsG2MsmOobCall extends BlsMsmOobCall {
  public BlsG2MsmOobCall(BigInteger calleeGas) {
    super(calleeGas);
  }

  @Override
  protected void traceOobInstructionInOob(Trace.Oob trace) {
    trace.isBlsG2Msm(true).oobInst(OOB_INST_BLS_G2_MSM);
  }

  @Override
  protected void traceOobInstructionInHub(Trace.Hub trace) {
    trace.pMiscOobInst(OOB_INST_BLS_G2_MSM);
  }

  @Override
  public int ctMax() {
    return CT_MAX_BLS_G2_MSM;
  }

  @Override
  int minMsmSize() {
    return PRC_BLS_G2_MSM_PAIR_SIZE;
  }

  @Override
  int getOobInst() {
    return OOB_INST_BLS_G2_MSM;
  }

  @Override
  int maxDiscount() {
    return PRC_BLS_G2_MSM_MAX_DISCOUNT;
  }

  @Override
  int msmMultiplicationCost() {
    return PRC_BLS_MULTIPLICATION_MULTIPLIER;
  }
}
