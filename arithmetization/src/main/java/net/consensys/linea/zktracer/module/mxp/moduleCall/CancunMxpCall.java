package net.consensys.linea.zktracer.module.mxp.moduleCall;

import static net.consensys.linea.zktracer.module.mxp.MxpUtils.memoryCost;

import java.util.ArrayList;
import java.util.List;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.module.mxp.MxpExoCall;
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
    // Initialization of the computed values of MxpCall
    this.gasMxp = 0L;
    setMxpxFromMxpxExpression();
    setMayTriggerNontrivialMmuOperationFromMxpx();
  }

  /** Store all wcp and euc computations with params and results */
  public final List<MxpExoCall> exoCalls = new ArrayList<>(ctMax());

  /** Computed by CancunTrivialMxpCall */
  public boolean size1IsZero = false;

  public boolean size2IsZero = false;

  /** Computed by CancunMxpxMxpCall */
  public int mxpxExpression = 0;

  /**
   * Computed by State update scenarii (CancunStateUpdtWPricingMxpCall and
   * CancunStateUpdtBPricingMxpCall)
   */
  public boolean isStateUpdate = false;

  public long wordsNew = 0L;
  public long cMemNew = 0L;
  public long extraGasCost = 0L;

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

  public void setMxpxFromMxpxExpression() {
    this.mxpx = this.mxpxExpression != 0;
  }

  public void setMayTriggerNontrivialMmuOperationFromMxpx() {
    this.mayTriggerNontrivialMmuOperation = !this.size1.isZero() && !this.mxpx;
  }

  public void setGasMpxFromExtraGasCost() {
    this.gasMxp = this.cMemNew - this.cMem + this.extraGasCost;
  }
}
