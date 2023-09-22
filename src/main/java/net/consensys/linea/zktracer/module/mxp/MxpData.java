/*
 * Copyright ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package net.consensys.linea.zktracer.module.mxp;

import static net.consensys.linea.zktracer.module.Util.max;

import java.math.BigInteger;
import java.util.Arrays;

import lombok.Getter;
import net.consensys.linea.zktracer.opcode.OpCodeData;
import net.consensys.linea.zktracer.opcode.gas.MxpType;
import org.apache.tuweni.bytes.Bytes32;
import org.apache.tuweni.units.bigints.UInt256;

public class MxpData {
  public static final BigInteger TWO_POW_128 = BigInteger.ONE.shiftLeft(128);
  public static final BigInteger TWO_POW_32 = BigInteger.ONE.shiftLeft(32);

  // constants from protocol_params.go

  // Times the address of the (highest referenced byte in memory + 1). NOTE: referencing
  // happens on read, write and in instructions such as RETURN and CALL.
  public static final int MEMORY_GAS = 3;
  // Divisor for the quadratic particle of the memory cost equation.
  public static final int QUAD_COEFF_DIV = 512;

  private OpCodeData opCodeData;
  private long op;
  private int cn;
  private BigInteger words;
  private BigInteger wordsNew;
  @Getter private BigInteger offset1;
  @Getter private BigInteger size1;
  @Getter private BigInteger offset2;
  @Getter private BigInteger size2;
  @Getter private BigInteger maxOffset1;
  @Getter private BigInteger maxOffset2;
  @Getter private BigInteger maxOffset;
  @Getter private boolean mxpx;
  @Getter private boolean roob;
  @Getter private boolean noop;
  @Getter private boolean comp;
  @Getter private boolean mxpe;
  @Getter private BigInteger mxpc;
  @Getter private BigInteger mxpcNew;
  @Getter private BigInteger mxpcDelta;
  @Getter private BigInteger acc1;
  @Getter private BigInteger acc2;
  @Getter private BigInteger acc3;
  @Getter private BigInteger acc4;
  @Getter private BigInteger accA;
  @Getter private BigInteger accW;
  @Getter private BigInteger accQ;
  private byte[] byte1 = new byte[17];
  private byte[] byte2 = new byte[17];
  private byte[] byte3 = new byte[17];
  private byte[] byte4 = new byte[17];
  private byte[] byteA = new byte[17];
  private byte[] byteW = new byte[17];
  private byte[] byteQ = new byte[17];
  private byte[] byteQQ = new byte[17];
  private byte[] byteR = new byte[17];
  private BigInteger quadCost; // for debug only
  private BigInteger linearCost; // for debug only
  private MxpType typeMxp;

  public MxpData(final OpCodeData opCodeData) {
    this.opCodeData = opCodeData;
    this.op = opCodeData.value();
    this.typeMxp = opCodeData.billing().type();
    this.words = BigInteger.ZERO;
    this.mxpcNew = BigInteger.ZERO;
    this.accW = BigInteger.ZERO;

    // TODO set
    //      cn = passed in param;
    //      words = divRoundedUpBigInteger(scope.memory.len(), BigInteger.ZERO);

    setSizesAndOffsets();
    setMaxOffset1and2();
    setRoob();
    setNoop();
    setMaxOffsetAndMxpx();
    setAccAAndByteR();
    setWordsNew();
    setMxpe();
    setComp();
    setMxpc();
    setMxpcNew();
    setAcc1and2();
    setAcc3();
    setAcc4();
    setAccWAndByteR();
    setAccQAndByteQQ();
    setMxpcDelta();
    setBytes();
  }

  /* set sizes and offsets. */
  protected void setSizesAndOffsets() {
    // TODO use stack param

    offset1 = BigInteger.ZERO;
    offset2 = BigInteger.ZERO;
    size1 = BigInteger.ZERO;
    size2 = BigInteger.ZERO;

    maxOffset1 = BigInteger.ZERO;
    maxOffset2 = BigInteger.ZERO;
    maxOffset = BigInteger.ZERO;
  }
  /** get ridiculously out of bounds. */
  protected boolean setRoob() {
    return switch (typeMxp) {
      case TYPE_2, TYPE_3 -> offset1.compareTo(TWO_POW_128) >= 0;
      case TYPE_4 -> size1.compareTo(TWO_POW_128) >= 0
          || (offset1.compareTo(TWO_POW_128) >= 0 && !size1.equals(BigInteger.ZERO));
      case TYPE_5 -> size1.compareTo(TWO_POW_128) >= 0
          || (offset1.compareTo(TWO_POW_128) >= 0 && !size1.equals(BigInteger.ZERO))
          || (size2.compareTo(TWO_POW_128) >= 0
              || (offset2.compareTo(TWO_POW_128) >= 0 && !size2.equals(BigInteger.ZERO)));
      default -> false;
    };
  }

  /** get no op. */
  protected boolean setNoop() {
    if (roob) {
      return false;
    }
    return switch (typeMxp) {
      case TYPE_1 -> true;
      case TYPE_4 -> size1.equals(BigInteger.ZERO);
      case TYPE_5 -> size1.equals(BigInteger.ZERO) && size2.equals(BigInteger.ZERO);
      default -> false;
    };
  }

  /** set max offsets 1 and 2. */
  protected void setMaxOffset1and2() {
    switch (typeMxp) {
      case TYPE_2 -> maxOffset1 = offset1.add(BigInteger.valueOf(31));
      case TYPE_3 -> maxOffset1 = offset1;
      case TYPE_4 -> maxOffset1 = offset1.add(size1).subtract(BigInteger.ONE);
      case TYPE_5 -> {
        if (!size1.equals(BigInteger.ZERO)) {
          maxOffset1 = offset1.add(size1).subtract(BigInteger.ONE);
        }
        if (!size2.equals(BigInteger.ZERO)) {
          maxOffset2 = offset2.add(size2).subtract(BigInteger.ONE);
        }
      }
      default -> {
        maxOffset1 = BigInteger.ZERO;
        maxOffset2 = BigInteger.ZERO;
      }
    }
  }

  /** set max offset and mxpx. */
  protected void setMaxOffsetAndMxpx() {
    if (roob || noop) {
      mxpx = roob;
    } else {
      // choose the max value
      maxOffset = max(maxOffset1, maxOffset2);
      mxpx = maxOffset.compareTo(TWO_POW_32) >= 0;
    }
  }

  protected void setComp() {
    comp = maxOffset1.compareTo(offset2) >= 0;
  }

  protected void setAccAAndByteR() {
    BigInteger maxOffsetPlusOne = BigInteger.ZERO;
    maxOffsetPlusOne.add(maxOffset.add(BigInteger.ONE));
    accA = divRoundedUpBigInteger(maxOffsetPlusOne, BigInteger.valueOf(32));
    BigInteger diff = BigInteger.ZERO;
    diff.multiply(accA.multiply(BigInteger.valueOf(32)));
    diff.subtract(maxOffset);
    diff.subtract(BigInteger.ONE);
    Bytes32 diffBytes = Bytes32.leftPad(UInt256.valueOf(diff));
    byteR[1] = diffBytes.get(31);
    byteR[0] = (byte) (byteR[1] + 224);
  }

  public BigInteger divRoundedUpBigInteger(BigInteger a, BigInteger b) {
    return a.add(b.subtract(BigInteger.ONE)).divide(b);
  }

  protected void setWordsNew() {
    if (!mxpx) {
      if (noop) {
        wordsNew = words;
      } else {
        wordsNew = max(words, accA);
      }
    }
  }

  public static BigInteger memoryGasCost(BigInteger words) {
    BigInteger square = words.multiply(words);
    BigInteger linCoef = words.multiply(BigInteger.valueOf(MEMORY_GAS));
    BigInteger quadCoef = square.divide(BigInteger.valueOf(QUAD_COEFF_DIV));
    BigInteger totalFee = linCoef.add(quadCoef);
    return totalFee;
  }

  protected void setMxpc() {
    mxpc = memoryGasCost(words);
  }

  protected void setMxpcNew() {
    mxpc = memoryGasCost(wordsNew);
  }

  protected void setMxpe() {
    mxpe = words.compareTo(wordsNew) < 0;
  }

  protected void setMxpcDelta() {
    // TODO billing
    //      final BigInteger term1 = size1.multiply(opCodeData.billing().billingRate());
    //      final BigInteger term2 = accW.multiply(opCodeData.billing().billingRate());
    final BigInteger term1 = BigInteger.ONE;
    final BigInteger term2 = BigInteger.ZERO;

    linearCost = term1;
    linearCost = linearCost.add(term2);

    quadCost = mxpcNew.subtract(mxpc);

    mxpcDelta = quadCost;
    mxpcDelta = mxpcDelta.add(linearCost);
  }

  protected void setAcc1and2() {
    if (roob) {
      return;
    }
    if (mxpx) {
      if (maxOffset1.compareTo(TWO_POW_32) >= 0) {
        acc1 = maxOffset1.subtract(TWO_POW_32);
      } else {
        assert maxOffset2.compareTo(TWO_POW_32) >= 0;
        acc2 = maxOffset2.subtract(TWO_POW_32);
      }
    } else {
      acc1 = maxOffset1;
      acc2 = maxOffset2;
    }
  }

  protected void setAcc3() {
    if (comp) {
      acc3 = maxOffset1.subtract(maxOffset2);
    } else {
      acc3 = maxOffset2.subtract(maxOffset1);
      acc3 = acc3.subtract(BigInteger.ONE);
    }
  }

  protected void setAcc4() {
    if (mxpe) {
      acc4 = accA.subtract(words);
      acc4 = acc4.subtract(BigInteger.ONE);
    } else {
      acc4 = words.subtract(accA);
    }
  }

  protected void setAccWAndByteR() {
    if (typeMxp != MxpType.TYPE_4) {
      return;
    }

    accW = size1.add(BigInteger.valueOf(31)).divide(BigInteger.valueOf(32));

    BigInteger r = accW.multiply(BigInteger.valueOf(32)).subtract(size1);
    byteR[3] = r.toByteArray()[r.toByteArray().length - 1];
    byteR[2] = (byte) (byteR[3] + 224);
  }

  protected void setAccQAndByteQQ() {
    BigInteger square = wordsNew.multiply(wordsNew);

    BigInteger quotientBy512 = square.divide(BigInteger.valueOf(512));

    BigInteger remainderBy512 = quotientBy512.multiply(BigInteger.valueOf(512));
    remainderBy512 = square.subtract(remainderBy512);

    accQ = quotientBy512.mod(TWO_POW_32);

    Bytes32 quotientBytes = UInt256.valueOf(quotientBy512);
    Bytes32 remainderBytes = UInt256.valueOf(remainderBy512);

    byteQQ[0] = quotientBytes.get(quotientBytes.size() - 6);
    byteQQ[1] = quotientBytes.get(quotientBytes.size() - 5);
    byteQQ[2] = remainderBytes.get(quotientBytes.size() - 2);
    byteQQ[3] = remainderBytes.get(quotientBytes.size() - 1);
  }

  protected void setBytes() {
    int maxCounter = mxpx ? 16 : 3;
    Bytes32 b1 = UInt256.valueOf(acc1);
    Bytes32 b2 = UInt256.valueOf(acc2);
    Bytes32 b3 = UInt256.valueOf(acc3);
    Bytes32 b4 = UInt256.valueOf(acc4);
    Bytes32 bA = UInt256.valueOf(accA);
    Bytes32 bW = UInt256.valueOf(accW);
    Bytes32 bQ = UInt256.valueOf(accQ);
    for (int i = 0; i <= maxCounter; i++) {

      byte1[i] = b1.get(b1.size() - 1 - maxCounter + i);
      byte2[i] = b2.get(b2.size() - 1 - maxCounter + i);
      byte3[i] = b3.get(b3.size() - 1 - maxCounter + i);
      byte4[i] = b4.get(b4.size() - 1 - maxCounter + i);
      byteA[i] = bA.get(bA.size() - 1 - maxCounter + i);
      byteW[i] = bW.get(bW.size() - 1 - maxCounter + i);
      byteQ[i] = bQ.get(bQ.size() - 1 - maxCounter + i);
    }
  }

  protected BigInteger sliceAcc(BigInteger acc, int ct) {
    int maxCounter = mxpx ? 16 : 3;
    byte[] accBytes = acc.toByteArray();
    int start = accBytes.length - 1 - maxCounter;
    int end = start + ct;
    byte[] resBytes = Arrays.copyOfRange(accBytes, start, end);
    return new BigInteger(resBytes);
  }
}
