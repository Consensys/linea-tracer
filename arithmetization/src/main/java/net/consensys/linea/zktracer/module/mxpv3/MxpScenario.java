package net.consensys.linea.zktracer.module.mxpv3;

import static net.consensys.linea.zktracer.module.mxpv3.MxpUtils.isWordPricingOpcode;

import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.opcode.OpCode;

public enum MxpScenario {
  MSIZE,
  TRIVIAL,
  MXPX,
  UPDT_W,
  UPDT_B;

  public static MxpScenario getMxpScenario(MxpCall mxpCall) {
    OpCode opCode = mxpCall.getOpCodeData().mnemonic();
    if (opCode == OpCode.MSIZE) {
      return MxpScenario.MSIZE;
    }
    if (mxpCall.getSize1().isZero() && mxpCall.getSize2().isZero()) {
      return MxpScenario.TRIVIAL;
    }
    if (mxpCall.isMxpx()) {
      return MxpScenario.MXPX;
    }
    if (isWordPricingOpcode(opCode)) {
      return MxpScenario.UPDT_W;
    }
    return MxpScenario.UPDT_B;
  }
}
