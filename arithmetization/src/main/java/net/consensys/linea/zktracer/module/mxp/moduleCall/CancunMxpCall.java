package net.consensys.linea.zktracer.module.mxp.moduleCall;

import static net.consensys.linea.zktracer.module.mxp.MxpUtils.memoryCost;

import java.util.ArrayList;
import java.util.List;

import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.module.mxp.MxpExoCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.gas.BillingRate;
import org.apache.tuweni.bytes.Bytes;

public abstract class CancunMxpCall extends MxpCall {

  public final long words;
  public final long cMem;
  public final Bytes gWord;
  public final Bytes gByte;

  public CancunMxpCall(Hub hub) {
    super(hub);
    this.words = this.memorySizeInWords;
    this.cMem = memoryCost(this.memorySizeInWords);
    this.gWord = getCostBy(BillingRate.BY_WORD);
    this.gByte = getCostBy(BillingRate.BY_BYTE);
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
}
