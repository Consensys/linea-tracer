package net.consensys.linea.zktracer.module.mxp;

import static net.consensys.linea.zktracer.Trace.GAS_CONST_G_MEMORY;
import static net.consensys.linea.zktracer.Trace.Mxp.MXPX_THRESHOLD;
import static net.consensys.linea.zktracer.module.euc.EucCall.eucCall;
import static net.consensys.linea.zktracer.module.mxp.MxpUtils.isDoubleOffsetOpcode;
import static net.consensys.linea.zktracer.module.wcp.WcpCall.*;
import static net.consensys.linea.zktracer.module.wcp.WcpCall.ltCall;
import static net.consensys.linea.zktracer.types.Conversions.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.euc.EucCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.module.wcp.WcpCall;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;

public class MxpComputation {
  private final Wcp wcp;
  private final Euc euc;
  public final List<WcpCall> wcpCalls;
  public final List<EucCall> eucCalls;
  public final boolean[] wcpFlags;
  public final boolean[] eucFlags;

  @Builder
  public MxpComputation(Wcp wcp, Euc euc, int nRows) {
    this.wcp = wcp;
    this.euc = euc;
    this.wcpCalls = new ArrayList<>(nRows);
    this.eucCalls = new ArrayList<>(nRows);
    this.wcpFlags = new boolean[nRows];
    this.eucFlags = new boolean[nRows];
  }

  public void computeForMSize() {
    // no computations takes place for MSIZE scenario
    wcpFlags[1] = false;
    eucFlags[1] = false;
  }

  public void computeForNotMSize(MxpCall mxpCall) {
    wcpFlags[1] = true;
    wcpFlags[2] = true;
    // testing zeroness of size parameters
    wcpCalls.add(1, isZeroCall(wcp, mxpCall.getOffset1()));
    wcpCalls.add(2, isZeroCall(wcp, mxpCall.getOffset2()));
  }

  public int computeForNotMSizeNorTrivial(MxpCall mxpCall) {
    wcpFlags[3] = true;
    wcpFlags[4] = true;
    wcpFlags[5] = true;
    wcpFlags[6] = true;
    // testing for small-ness of size and offset parameters
    wcpCalls.add(3, leqCall(wcp, mxpCall.getSize1(), Bytes.ofUnsignedLong(MXPX_THRESHOLD)));
    wcpCalls.add(4, leqCall(wcp, mxpCall.getSize2(), Bytes.ofUnsignedLong(MXPX_THRESHOLD)));
    wcpCalls.add(5, leqCall(wcp, mxpCall.getOffset1(), Bytes.ofUnsignedLong(MXPX_THRESHOLD)));
    wcpCalls.add(6, leqCall(wcp, mxpCall.getOffset2(), Bytes.ofUnsignedLong(MXPX_THRESHOLD)));
    // setting shorthands
    boolean size1IsZero = wcpCalls.get(1).result();
    boolean size1IsNonZero = !size1IsZero;
    boolean size2IsZero = wcpCalls.get(2).result();
    boolean size2IsNonZero = !size2IsZero;
    boolean size1IsSmall = wcpCalls.get(3).result();
    boolean size1IsLarge = !size1IsSmall;
    boolean size2IsSmall = wcpCalls.get(4).result();
    boolean size2IsLarge = !size2IsSmall;
    boolean offset1IsSmall = wcpCalls.get(5).result();
    boolean offset1IsLarge = !offset1IsSmall;
    boolean offset2IsSmall = wcpCalls.get(6).result();
    boolean offset2IsLarge = !offset2IsSmall;
    int mxpxExpression1 =
        booleanToInt(size1IsLarge) + booleanToInt(size1IsNonZero) * booleanToInt(offset1IsLarge);
    int mxpxExpression2 =
        booleanToInt(size2IsLarge) + booleanToInt(size2IsNonZero) * booleanToInt(offset2IsLarge);
    int mxpxExpression = mxpxExpression1 + mxpxExpression2;
    return mxpxExpression;
  }

  public long[] computeForStateUpdt(MxpCall mxpCall, long words, long cMem) {
    OpCode opCode = mxpCall.getOpCodeData().mnemonic();

    // we filter the wcp call by double_offset to prevent unnecessary comparisons
    if (isDoubleOffsetOpcode(opCode)) {
      // Row i + 7
      wcpFlags[7] = true;
      var max1 =
          mxpCall
              .getOffset1()
              .toUnsignedBigInteger()
              .add(mxpCall.getSize1().toUnsignedBigInteger());
      var max2 =
          mxpCall
              .getOffset2()
              .toUnsignedBigInteger()
              .add(mxpCall.getSize2().toUnsignedBigInteger());
      wcpCalls.add(7, ltCall(wcp, bigIntegerToBytes(max1), bigIntegerToBytes(max2)));
    }

    // Row i + 8
    eucFlags[8] = true;
    boolean useParams2 = wcpCalls.get(7).result();
    boolean useParams1 = !useParams2;
    var maxOffset1 =
        mxpCall
            .getOffset1()
            .lo()
            .toUnsignedBigInteger()
            .add(mxpCall.getSize1().lo().toUnsignedBigInteger())
            .subtract(BigInteger.ONE);
    var maxOffset2 =
        mxpCall
            .getOffset2()
            .lo()
            .toUnsignedBigInteger()
            .add(mxpCall.getSize2().lo().toUnsignedBigInteger())
            .subtract(BigInteger.ONE);
    ;
    var maxOffset =
        booleanToBigInteger(useParams1)
            .multiply(maxOffset1)
            .add(booleanToBigInteger(useParams2).multiply(maxOffset2));
    eucCalls.add(8, eucCall(euc, bigIntegerToBytes(maxOffset), Bytes.of(32)));

    // row i + 9
    eucFlags[9] = true;
    var floor = eucCalls.get(8).result();
    var EYPa = floor.toUnsignedBigInteger().add(BigInteger.ONE);
    eucCalls.add(9, eucCall(euc, bigIntegerToBytes(EYPa.multiply(EYPa)), Bytes.of(512)));

    // row i + 10
    wcpFlags[10] = true;
    wcpCalls.add(10, ltCall(wcp, longToBytes(words), bigIntegerToBytes(EYPa)));

    // Compute state updates
    var cMemQuadPart = eucCalls.get(9).result();
    var cMemLinearPart = bigIntegerToBytes(EYPa.multiply(BigInteger.valueOf(GAS_CONST_G_MEMORY)));
    boolean updateInternalState = wcpCalls.get(10).result();
    long wordsNewUpdate = updateInternalState ? EYPa.longValue() : words;
    long cMemNewUpdate =
        updateInternalState
            ? cMemQuadPart
                .toUnsignedBigInteger()
                .add(cMemLinearPart.toUnsignedBigInteger())
                .longValue()
            : cMem;
    return new long[] {wordsNewUpdate, cMemNewUpdate};
  }

  public long computeForUpdtW(MxpCall mxpCall, Bytes gWord) {
    // Row i + 11
    eucFlags[11] = true;
    eucCalls.add(11, eucCall(euc, mxpCall.getSize1().lo(), Bytes.of(32)));
    var numberOfWords = eucCalls.get(11).result();
    long extraWordCost =
        numberOfWords.toUnsignedBigInteger().multiply(gWord.toUnsignedBigInteger()).longValue();
    return extraWordCost;
  }

  public long computeForUpdtB(MxpCall mxpCall, Bytes gByte) {
    OpCode opCode = mxpCall.getOpCodeData().mnemonic();
    var gasPerByte =
        (opCode == OpCode.RETURN)
            ? bigIntegerToBytes(
                booleanToBigInteger(mxpCall.isDeploys()).multiply(gByte.toUnsignedBigInteger()))
            : gByte;
    var numberOfBytes = mxpCall.getSize1().lo();
    long extraByteCost =
        numberOfBytes
            .toUnsignedBigInteger()
            .multiply(gasPerByte.toUnsignedBigInteger())
            .longValue();
    return extraByteCost;
  }
}
