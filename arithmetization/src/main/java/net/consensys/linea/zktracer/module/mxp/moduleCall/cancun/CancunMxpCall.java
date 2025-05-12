package net.consensys.linea.zktracer.module.mxp.moduleCall.cancun;

import static net.consensys.linea.zktracer.module.mxp.MxpUtils.isWordPricingOpcode;
import static net.consensys.linea.zktracer.module.mxp.MxpUtils.memoryCost;

import java.util.ArrayList;
import java.util.List;

import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.mxp.MxpExoCall;
import net.consensys.linea.zktracer.module.mxp.moduleCall.MxpCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;

public abstract class CancunMxpCall extends MxpCall {

  public final long words;
  public final long cMem;

  public CancunMxpCall(Hub hub) {
    super(hub);
    this.words = this.memorySizeInWords;
    this.cMem = memoryCost(this.memorySizeInWords);
  }

  /** Store all wcp and euc computations with params and results */
  public final List<MxpExoCall> exoCalls = new ArrayList<>(ctMax());

  /** Computed by TrivialMxpScenario */
  public boolean size1IsZero = false;

  public boolean size2IsZero = false;

  /** Computed by MxpxMxpScenario */
  public int mxpxExpression = 0;

  /**
   * Computed by State update scenarii (StateUpdtWPricingMxpScenario and
   * StateUpdtBPricingMxpScenario)
   */
  public boolean isStateUpdate = false;

  public long wordsNew = 0L;
  public long cMemNew = 0L;
  public long extraGasCost = 0L;

  public abstract void compute(Wcp wcp, Euc euc);

  public abstract int ctMax();

  public boolean isMSizeScenario() {
    return false;
  }

  public boolean isTrivialScenario() {
    return false;
  }

  public boolean isMxpxScenario() {
    return false;
  }

  public boolean isStateUpdtWPricingScenario() {
    return false;
  }

  public boolean isStateUpdtBPricingScenario() {
    return false;
  }

  /**
   * Get the MxpScenario for the given MxpCall.
   *
   * @param wcp module to compute the wcp in exoCalls
   * @param euc module to compute the euc in exoCalls
   * @return MxpScenario instance corresponding to the MxpCall
   */
  public CancunMxpCall getMxpScenario(Wcp wcp, Euc euc) {
    OpCode opCode = this.opCodeData.mnemonic();
    if (opCode == OpCode.MSIZE) {
      return new CancunMSizeMxpCall(hub);
    }
    if (this.size1.isZero() && this.size2.isZero()) {
      return new CancunTrivialMxpCall(hub);
    }
    if (this.mxpx) {
      return new CancunMxpxMxpCall(hub, wcp, euc);
    }
    if (isWordPricingOpcode(opCode)) {
      return new CancunStateUpdtWPricingMxpCall(hub, wcp, euc);
    }
    return new CancunStateUpdtBPricingMxpCall(hub, wcp, euc);
  }
}
