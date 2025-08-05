package net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.common.bls.msm;

import static net.consensys.linea.zktracer.Trace.OOB_INST_BLS_G1MSM;
import static net.consensys.linea.zktracer.Trace.PRC_BLS_G1MSM_MAX_DISCOUNT;
import static net.consensys.linea.zktracer.Trace.PRC_BLS_G1MSM_SIZE_MIN;
import static net.consensys.linea.zktracer.Trace.PRC_BLS_MULTIPLICATION_MULTIPLIER;
import static net.consensys.linea.zktracer.TraceCancun.Oob.CT_MAX_BLS_G1MSM;

import java.math.BigInteger;

import net.consensys.linea.zktracer.Trace;

public class BlsG1MsmOobCall extends BlsMsmOobCall {
  public BlsG1MsmOobCall(BigInteger calleeGas) {
    super(calleeGas);
  }

  @Override
  protected void traceOobInstructionInOob(Trace.Oob trace) {
    trace.isBlsG1Msm(true).oobInst(OOB_INST_BLS_G1MSM);
  }

  @Override
  protected void traceOobInstructionInHub(Trace.Hub trace) {
    trace.pMiscOobInst(OOB_INST_BLS_G1MSM);
  }

  @Override
  public int ctMax() {
    return CT_MAX_BLS_G1MSM;
  }

  @Override
  int minMsmSize() {
    return PRC_BLS_G1MSM_SIZE_MIN;
  }

  @Override
  int getOobInst() {
    return OOB_INST_BLS_G1MSM;
  }

  @Override
  int maxDiscount() {
    return PRC_BLS_G1MSM_MAX_DISCOUNT;
  }

  @Override
  int msmMultiplicationCost() {
    return PRC_BLS_MULTIPLICATION_MULTIPLIER;
  }
}
