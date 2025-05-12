package net.consensys.linea.zktracer.module.mxp.moduleCall.cancun;

import static net.consensys.linea.zktracer.Trace.Mxpcan.CT_MAX_UPDT_B;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;
import static net.consensys.linea.zktracer.types.Conversions.booleanToBigInteger;

import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.gas.BillingRate;

public class CancunStateUpdtBPricingMxpCall extends CancunStateUpdtWPricingMxpCall {

  public CancunStateUpdtBPricingMxpCall(Hub hub, Wcp wcp, Euc euc) {
    super(hub, wcp, euc);
    compute(wcp, euc);
    this.mxpx = this.mxpxExpression != 0;
    this.mayTriggerNontrivialMmuOperation = !this.size1.isZero() && !this.mxpx;
    if (this.isStateUpdate) {
      // if state has changed, an extra gas cost is incurred
      this.gasMxp = this.cMemNew - this.cMem + this.extraGasCost;
    }
  }

  @Override
  public boolean isStateUpdtBPricingScenario() {
    return true;
  }

  public void computeExtraGasCost() {
    final var gByte = getCostBy(BillingRate.BY_BYTE);
    final var opCode = this.opCodeData.mnemonic();
    final var gasPerByte =
        (opCode == OpCode.RETURN)
            ? bigIntegerToBytes(
                booleanToBigInteger(this.deploys).multiply(gByte.toUnsignedBigInteger()))
            : gByte;
    final var numberOfBytes = this.size1.lo();
    this.extraGasCost =
        numberOfBytes
            .toUnsignedBigInteger()
            .multiply(gasPerByte.toUnsignedBigInteger())
            .longValue();
  }

  @Override
  public void compute(Wcp wcp, Euc euc) {
    computeSize1Size2IsZero(wcp);
    computeMxpxExpression(wcp);
    computeStateUpdt(wcp, euc);
    computeExtraGasCost();
  }

  @Override
  public int ctMax() {
    return CT_MAX_UPDT_B;
  }
}
