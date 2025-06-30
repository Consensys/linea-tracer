package net.consensys.linea.zktracer.module.mxp.moduleCall;

import static net.consensys.linea.zktracer.Trace.GAS_CONST_G_MEMORY;
import static net.consensys.linea.zktracer.module.mxp.MxpUtils.isDoubleOffsetOpcode;
import static net.consensys.linea.zktracer.types.Conversions.*;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;

import java.math.BigInteger;

import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.mxp.MxpExoCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;

public abstract class CancunStateUpdateMxpCall extends CancunNotMSizeNorTrivialMxpCall {

  public CancunStateUpdateMxpCall(Hub hub, Wcp wcp, Euc euc) {
    super(hub, wcp);
    computeStateUpdt(wcp, euc);
  }

  public void computeStateUpdt(Wcp wcp, Euc euc) {
    final OpCode opCode = this.opCodeData.mnemonic();

    // We compute and assign the computation's result for each row

    // Row i + 7
    // Compute useParams1 and useParams2
    boolean useParams2 = false; // default value if opcode is single offset
    boolean useParams1 = true;
    // we filter the row i + 7 wcp call by double_offset to prevent unnecessary comparisons
    if (isDoubleOffsetOpcode(opCode)) {
      final var max1 = this.offset1.toUnsignedBigInteger().add(this.size1.toUnsignedBigInteger());
      final var max2 = this.offset2.toUnsignedBigInteger().add(this.size2.toUnsignedBigInteger());
      exoCalls[6] = MxpExoCall.callToLT(wcp, bigIntegerToBytes(max1), bigIntegerToBytes(max2));
      useParams2 = exoCalls[6].resultA(); // result of row i + 7
      useParams1 = !useParams2;
    }

    // Row i + 8
    // Compute floor and EYPa
    final var maxOffset1 =
        this.offset1
            .lo()
            .toUnsignedBigInteger()
            .add(this.size1.lo().toUnsignedBigInteger())
            .subtract(BigInteger.ONE);
    final var maxOffset2 =
        this.offset2
            .lo()
            .toUnsignedBigInteger()
            .add(this.size2.lo().toUnsignedBigInteger())
            .subtract(BigInteger.ONE);
    ;
    final var maxOffset =
        booleanToBigInteger(useParams1)
            .multiply(maxOffset1)
            .add(booleanToBigInteger(useParams2).multiply(maxOffset2));
    exoCalls[7] = MxpExoCall.callToEUC(euc, bigIntegerToBytes(maxOffset), Bytes.of(32));
    final var floor = exoCalls[7].resultB();
    final var EYPa = floor.toUnsignedBigInteger().add(BigInteger.ONE);

    // row i + 9
    // Compute cMemQuadPart
    exoCalls[8] = MxpExoCall.callToEUC(euc, bigIntegerToBytes(EYPa.multiply(EYPa)), Bytes.of(512));
    final var cMemQuadPart = exoCalls[8].resultB();

    // row i + 10
    // Compute updateInternalState
    exoCalls[9] = MxpExoCall.callToLT(wcp, longToBytes(words), bigIntegerToBytes(EYPa));
    final var updateInternalState = exoCalls[9].resultA();

    // Determine state update
    final var cMemLinearPart =
        bigIntegerToBytes(EYPa.multiply(BigInteger.valueOf(GAS_CONST_G_MEMORY)));
    this.isStateUpdate = updateInternalState;
    this.wordsNew = updateInternalState ? EYPa.longValue() : this.words;
    this.cMemNew =
        updateInternalState
            ? bigIntegerToBytes(
                    cMemQuadPart.toUnsignedBigInteger().add(cMemLinearPart.toUnsignedBigInteger()))
                .toLong()
            : this.cMem;
  }
}
