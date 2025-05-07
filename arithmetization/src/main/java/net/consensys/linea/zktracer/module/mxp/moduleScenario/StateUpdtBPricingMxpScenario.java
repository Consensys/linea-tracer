package net.consensys.linea.zktracer.module.mxp.moduleScenario;

import static net.consensys.linea.zktracer.Trace.Mxp.CT_MAX_UPDT_B;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;
import static net.consensys.linea.zktracer.types.Conversions.booleanToBigInteger;

import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.gas.BillingRate;

public class StateUpdtBPricingMxpScenario extends StateUpdtWPricingMxpScenario {

  @Override
  public boolean isStateUpdtBPricingScenario() {
    return true;
  }

  public void computeExtraGasCost(MxpCall mxpCall) {
    var gByte = mxpCall.getCostBy(BillingRate.BY_BYTE);
    var opCode = mxpCall.getOpCodeData().mnemonic();
    var gasPerByte =
        (opCode == OpCode.RETURN)
            ? bigIntegerToBytes(
                booleanToBigInteger(mxpCall.isDeploys()).multiply(gByte.toUnsignedBigInteger()))
            : gByte;
    var numberOfBytes = mxpCall.getSize1().lo();
    this.extraGasCost =
        numberOfBytes
            .toUnsignedBigInteger()
            .multiply(gasPerByte.toUnsignedBigInteger())
            .longValue();
  }

  @Override
  public void compute(MxpCall mxpCall, Wcp wcp, Euc euc) {
    computeSize1Size2IsZero(mxpCall, wcp);
    computeMxpxExpression(mxpCall, wcp);
    computeStateUpdt(mxpCall, wcp, euc);
    computeExtraGasCost(mxpCall);
  }

  @Override
  public int ctMax() {
    return CT_MAX_UPDT_B;
  }
}
