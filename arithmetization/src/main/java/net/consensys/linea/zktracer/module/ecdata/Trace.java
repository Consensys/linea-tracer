/*
 * Copyright ConsenSys Inc.
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

package net.consensys.linea.zktracer.module.ecdata;

import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.util.BitSet;
import java.util.List;

import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

/**
 * WARNING: This code is generated automatically.
 *
 * <p>Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
public class Trace {
  public static final int ADDMOD = 0x8;
  public static final int CT_MAX_LARGE_POINT = 0x7;
  public static final int CT_MAX_SMALL_POINT = 0x3;
  public static final int ECADD = 0x6;
  public static final int ECMUL = 0x7;
  public static final int ECPAIRING = 0x8;
  public static final int ECRECOVER = 0x1;
  public static final int INDEX_MAX_ECADD_DATA = 0x7;
  public static final int INDEX_MAX_ECADD_RESULT = 0x3;
  public static final int INDEX_MAX_ECMUL_DATA = 0x5;
  public static final int INDEX_MAX_ECMUL_RESULT = 0x3;
  public static final int INDEX_MAX_ECPAIRING_DATA_MIN = 0xb;
  public static final int INDEX_MAX_ECPAIRING_RESULT = 0x1;
  public static final int INDEX_MAX_ECRECOVER_DATA = 0x7;
  public static final int INDEX_MAX_ECRECOVER_RESULT = 0x1;
  public static final int MULMOD = 0x9;
  public static final BigInteger P_BN_HI = new BigInteger("64323764613183177041862057485226039389");
  public static final BigInteger P_BN_LO =
      new BigInteger("201385395114098847380338600778089168199");
  public static final BigInteger SECP256K1N_HI =
      new BigInteger("340282366920938463463374607431768211455");
  public static final BigInteger SECP256K1N_LO =
      new BigInteger("340282366920938463463374607427473243183");
  public static final int TOTAL_SIZE_ECADD_DATA = 0x80;
  public static final int TOTAL_SIZE_ECADD_RESULT = 0x40;
  public static final int TOTAL_SIZE_ECMUL_DATA = 0x60;
  public static final int TOTAL_SIZE_ECMUL_RESULT = 0x40;
  public static final int TOTAL_SIZE_ECPAIRING_DATA_MIN = 0xc0;
  public static final int TOTAL_SIZE_ECPAIRING_RESULT = 0x20;
  public static final int TOTAL_SIZE_ECRECOVER_DATA = 0x80;
  public static final int TOTAL_SIZE_ECRECOVER_RESULT = 0x20;

  private final BitSet filled = new BitSet();
  private int currentLine = 0;

  private final MappedByteBuffer accPairings;
  private final MappedByteBuffer acceptablePairOfPointForPairingCircuit;
  private final MappedByteBuffer byteDelta;
  private final MappedByteBuffer circuitSelectorEcadd;
  private final MappedByteBuffer circuitSelectorEcmul;
  private final MappedByteBuffer circuitSelectorEcpairing;
  private final MappedByteBuffer circuitSelectorEcrecover;
  private final MappedByteBuffer circuitSelectorG2Membership;
  private final MappedByteBuffer ct;
  private final MappedByteBuffer ctMax;
  private final MappedByteBuffer extArg1Hi;
  private final MappedByteBuffer extArg1Lo;
  private final MappedByteBuffer extArg2Hi;
  private final MappedByteBuffer extArg2Lo;
  private final MappedByteBuffer extArg3Hi;
  private final MappedByteBuffer extArg3Lo;
  private final MappedByteBuffer extFlag;
  private final MappedByteBuffer extInst;
  private final MappedByteBuffer extResHi;
  private final MappedByteBuffer extResLo;
  private final MappedByteBuffer g2MembershipTestRequired;
  private final MappedByteBuffer hurdle;
  private final MappedByteBuffer id;
  private final MappedByteBuffer index;
  private final MappedByteBuffer indexMax;
  private final MappedByteBuffer internalChecksPassed;
  private final MappedByteBuffer isEcaddData;
  private final MappedByteBuffer isEcaddResult;
  private final MappedByteBuffer isEcmulData;
  private final MappedByteBuffer isEcmulResult;
  private final MappedByteBuffer isEcpairingData;
  private final MappedByteBuffer isEcpairingResult;
  private final MappedByteBuffer isEcrecoverData;
  private final MappedByteBuffer isEcrecoverResult;
  private final MappedByteBuffer isInfinity;
  private final MappedByteBuffer isLargePoint;
  private final MappedByteBuffer isSmallPoint;
  private final MappedByteBuffer limb;
  private final MappedByteBuffer notOnG2;
  private final MappedByteBuffer notOnG2Acc;
  private final MappedByteBuffer notOnG2AccMax;
  private final MappedByteBuffer overallTrivialPairing;
  private final MappedByteBuffer phase;
  private final MappedByteBuffer stamp;
  private final MappedByteBuffer successBit;
  private final MappedByteBuffer totalPairings;
  private final MappedByteBuffer totalSize;
  private final MappedByteBuffer wcpArg1Hi;
  private final MappedByteBuffer wcpArg1Lo;
  private final MappedByteBuffer wcpArg2Hi;
  private final MappedByteBuffer wcpArg2Lo;
  private final MappedByteBuffer wcpFlag;
  private final MappedByteBuffer wcpInst;
  private final MappedByteBuffer wcpRes;

  static List<ColumnHeader> headers(int length) {
    return List.of(
        new ColumnHeader("ecdata.ACC_PAIRINGS", 32, length),
        new ColumnHeader("ecdata.ACCEPTABLE_PAIR_OF_POINT_FOR_PAIRING_CIRCUIT", 1, length),
        new ColumnHeader("ecdata.BYTE_DELTA", 1, length),
        new ColumnHeader("ecdata.CIRCUIT_SELECTOR_ECADD", 1, length),
        new ColumnHeader("ecdata.CIRCUIT_SELECTOR_ECMUL", 1, length),
        new ColumnHeader("ecdata.CIRCUIT_SELECTOR_ECPAIRING", 1, length),
        new ColumnHeader("ecdata.CIRCUIT_SELECTOR_ECRECOVER", 1, length),
        new ColumnHeader("ecdata.CIRCUIT_SELECTOR_G2_MEMBERSHIP", 1, length),
        new ColumnHeader("ecdata.CT", 2, length),
        new ColumnHeader("ecdata.CT_MAX", 2, length),
        new ColumnHeader("ecdata.EXT_ARG1_HI", 32, length),
        new ColumnHeader("ecdata.EXT_ARG1_LO", 32, length),
        new ColumnHeader("ecdata.EXT_ARG2_HI", 32, length),
        new ColumnHeader("ecdata.EXT_ARG2_LO", 32, length),
        new ColumnHeader("ecdata.EXT_ARG3_HI", 32, length),
        new ColumnHeader("ecdata.EXT_ARG3_LO", 32, length),
        new ColumnHeader("ecdata.EXT_FLAG", 1, length),
        new ColumnHeader("ecdata.EXT_INST", 1, length),
        new ColumnHeader("ecdata.EXT_RES_HI", 32, length),
        new ColumnHeader("ecdata.EXT_RES_LO", 32, length),
        new ColumnHeader("ecdata.G2_MEMBERSHIP_TEST_REQUIRED", 1, length),
        new ColumnHeader("ecdata.HURDLE", 1, length),
        new ColumnHeader("ecdata.ID", 8, length),
        new ColumnHeader("ecdata.INDEX", 1, length),
        new ColumnHeader("ecdata.INDEX_MAX", 32, length),
        new ColumnHeader("ecdata.INTERNAL_CHECKS_PASSED", 1, length),
        new ColumnHeader("ecdata.IS_ECADD_DATA", 1, length),
        new ColumnHeader("ecdata.IS_ECADD_RESULT", 1, length),
        new ColumnHeader("ecdata.IS_ECMUL_DATA", 1, length),
        new ColumnHeader("ecdata.IS_ECMUL_RESULT", 1, length),
        new ColumnHeader("ecdata.IS_ECPAIRING_DATA", 1, length),
        new ColumnHeader("ecdata.IS_ECPAIRING_RESULT", 1, length),
        new ColumnHeader("ecdata.IS_ECRECOVER_DATA", 1, length),
        new ColumnHeader("ecdata.IS_ECRECOVER_RESULT", 1, length),
        new ColumnHeader("ecdata.IS_INFINITY", 1, length),
        new ColumnHeader("ecdata.IS_LARGE_POINT", 1, length),
        new ColumnHeader("ecdata.IS_SMALL_POINT", 1, length),
        new ColumnHeader("ecdata.LIMB", 32, length),
        new ColumnHeader("ecdata.NOT_ON_G2", 1, length),
        new ColumnHeader("ecdata.NOT_ON_G2_ACC", 1, length),
        new ColumnHeader("ecdata.NOT_ON_G2_ACC_MAX", 1, length),
        new ColumnHeader("ecdata.OVERALL_TRIVIAL_PAIRING", 1, length),
        new ColumnHeader("ecdata.PHASE", 4, length),
        new ColumnHeader("ecdata.STAMP", 8, length),
        new ColumnHeader("ecdata.SUCCESS_BIT", 1, length),
        new ColumnHeader("ecdata.TOTAL_PAIRINGS", 32, length),
        new ColumnHeader("ecdata.TOTAL_SIZE", 32, length),
        new ColumnHeader("ecdata.WCP_ARG1_HI", 32, length),
        new ColumnHeader("ecdata.WCP_ARG1_LO", 32, length),
        new ColumnHeader("ecdata.WCP_ARG2_HI", 32, length),
        new ColumnHeader("ecdata.WCP_ARG2_LO", 32, length),
        new ColumnHeader("ecdata.WCP_FLAG", 1, length),
        new ColumnHeader("ecdata.WCP_INST", 1, length),
        new ColumnHeader("ecdata.WCP_RES", 1, length));
  }

  public Trace(List<MappedByteBuffer> buffers) {
    this.accPairings = buffers.get(0);
    this.acceptablePairOfPointForPairingCircuit = buffers.get(1);
    this.byteDelta = buffers.get(2);
    this.circuitSelectorEcadd = buffers.get(3);
    this.circuitSelectorEcmul = buffers.get(4);
    this.circuitSelectorEcpairing = buffers.get(5);
    this.circuitSelectorEcrecover = buffers.get(6);
    this.circuitSelectorG2Membership = buffers.get(7);
    this.ct = buffers.get(8);
    this.ctMax = buffers.get(9);
    this.extArg1Hi = buffers.get(10);
    this.extArg1Lo = buffers.get(11);
    this.extArg2Hi = buffers.get(12);
    this.extArg2Lo = buffers.get(13);
    this.extArg3Hi = buffers.get(14);
    this.extArg3Lo = buffers.get(15);
    this.extFlag = buffers.get(16);
    this.extInst = buffers.get(17);
    this.extResHi = buffers.get(18);
    this.extResLo = buffers.get(19);
    this.g2MembershipTestRequired = buffers.get(20);
    this.hurdle = buffers.get(21);
    this.id = buffers.get(22);
    this.index = buffers.get(23);
    this.indexMax = buffers.get(24);
    this.internalChecksPassed = buffers.get(25);
    this.isEcaddData = buffers.get(26);
    this.isEcaddResult = buffers.get(27);
    this.isEcmulData = buffers.get(28);
    this.isEcmulResult = buffers.get(29);
    this.isEcpairingData = buffers.get(30);
    this.isEcpairingResult = buffers.get(31);
    this.isEcrecoverData = buffers.get(32);
    this.isEcrecoverResult = buffers.get(33);
    this.isInfinity = buffers.get(34);
    this.isLargePoint = buffers.get(35);
    this.isSmallPoint = buffers.get(36);
    this.limb = buffers.get(37);
    this.notOnG2 = buffers.get(38);
    this.notOnG2Acc = buffers.get(39);
    this.notOnG2AccMax = buffers.get(40);
    this.overallTrivialPairing = buffers.get(41);
    this.phase = buffers.get(42);
    this.stamp = buffers.get(43);
    this.successBit = buffers.get(44);
    this.totalPairings = buffers.get(45);
    this.totalSize = buffers.get(46);
    this.wcpArg1Hi = buffers.get(47);
    this.wcpArg1Lo = buffers.get(48);
    this.wcpArg2Hi = buffers.get(49);
    this.wcpArg2Lo = buffers.get(50);
    this.wcpFlag = buffers.get(51);
    this.wcpInst = buffers.get(52);
    this.wcpRes = buffers.get(53);
  }

  public int size() {
    if (!filled.isEmpty()) {
      throw new RuntimeException("Cannot measure a trace with a non-validated row.");
    }

    return this.currentLine;
  }

  public Trace accPairings(final Bytes b) {
    if (filled.get(1)) {
      throw new IllegalStateException("ecdata.ACC_PAIRINGS already set");
    } else {
      filled.set(1);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      accPairings.put((byte) 0);
    }
    accPairings.put(b.toArrayUnsafe());

    return this;
  }

  public Trace acceptablePairOfPointForPairingCircuit(final Boolean b) {
    if (filled.get(0)) {
      throw new IllegalStateException(
          "ecdata.ACCEPTABLE_PAIR_OF_POINT_FOR_PAIRING_CIRCUIT already set");
    } else {
      filled.set(0);
    }

    acceptablePairOfPointForPairingCircuit.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace byteDelta(final UnsignedByte b) {
    if (filled.get(2)) {
      throw new IllegalStateException("ecdata.BYTE_DELTA already set");
    } else {
      filled.set(2);
    }

    byteDelta.put(b.toByte());

    return this;
  }

  public Trace circuitSelectorEcadd(final Boolean b) {
    if (filled.get(3)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_ECADD already set");
    } else {
      filled.set(3);
    }

    circuitSelectorEcadd.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace circuitSelectorEcmul(final Boolean b) {
    if (filled.get(4)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_ECMUL already set");
    } else {
      filled.set(4);
    }

    circuitSelectorEcmul.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace circuitSelectorEcpairing(final Boolean b) {
    if (filled.get(5)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_ECPAIRING already set");
    } else {
      filled.set(5);
    }

    circuitSelectorEcpairing.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace circuitSelectorEcrecover(final Boolean b) {
    if (filled.get(6)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_ECRECOVER already set");
    } else {
      filled.set(6);
    }

    circuitSelectorEcrecover.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace circuitSelectorG2Membership(final Boolean b) {
    if (filled.get(7)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_G2_MEMBERSHIP already set");
    } else {
      filled.set(7);
    }

    circuitSelectorG2Membership.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace ct(final short b) {
    if (filled.get(8)) {
      throw new IllegalStateException("ecdata.CT already set");
    } else {
      filled.set(8);
    }

    ct.putShort(b);

    return this;
  }

  public Trace ctMax(final short b) {
    if (filled.get(9)) {
      throw new IllegalStateException("ecdata.CT_MAX already set");
    } else {
      filled.set(9);
    }

    ctMax.putShort(b);

    return this;
  }

  public Trace extArg1Hi(final Bytes b) {
    if (filled.get(10)) {
      throw new IllegalStateException("ecdata.EXT_ARG1_HI already set");
    } else {
      filled.set(10);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      extArg1Hi.put((byte) 0);
    }
    extArg1Hi.put(b.toArrayUnsafe());

    return this;
  }

  public Trace extArg1Lo(final Bytes b) {
    if (filled.get(11)) {
      throw new IllegalStateException("ecdata.EXT_ARG1_LO already set");
    } else {
      filled.set(11);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      extArg1Lo.put((byte) 0);
    }
    extArg1Lo.put(b.toArrayUnsafe());

    return this;
  }

  public Trace extArg2Hi(final Bytes b) {
    if (filled.get(12)) {
      throw new IllegalStateException("ecdata.EXT_ARG2_HI already set");
    } else {
      filled.set(12);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      extArg2Hi.put((byte) 0);
    }
    extArg2Hi.put(b.toArrayUnsafe());

    return this;
  }

  public Trace extArg2Lo(final Bytes b) {
    if (filled.get(13)) {
      throw new IllegalStateException("ecdata.EXT_ARG2_LO already set");
    } else {
      filled.set(13);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      extArg2Lo.put((byte) 0);
    }
    extArg2Lo.put(b.toArrayUnsafe());

    return this;
  }

  public Trace extArg3Hi(final Bytes b) {
    if (filled.get(14)) {
      throw new IllegalStateException("ecdata.EXT_ARG3_HI already set");
    } else {
      filled.set(14);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      extArg3Hi.put((byte) 0);
    }
    extArg3Hi.put(b.toArrayUnsafe());

    return this;
  }

  public Trace extArg3Lo(final Bytes b) {
    if (filled.get(15)) {
      throw new IllegalStateException("ecdata.EXT_ARG3_LO already set");
    } else {
      filled.set(15);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      extArg3Lo.put((byte) 0);
    }
    extArg3Lo.put(b.toArrayUnsafe());

    return this;
  }

  public Trace extFlag(final Boolean b) {
    if (filled.get(16)) {
      throw new IllegalStateException("ecdata.EXT_FLAG already set");
    } else {
      filled.set(16);
    }

    extFlag.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace extInst(final UnsignedByte b) {
    if (filled.get(17)) {
      throw new IllegalStateException("ecdata.EXT_INST already set");
    } else {
      filled.set(17);
    }

    extInst.put(b.toByte());

    return this;
  }

  public Trace extResHi(final Bytes b) {
    if (filled.get(18)) {
      throw new IllegalStateException("ecdata.EXT_RES_HI already set");
    } else {
      filled.set(18);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      extResHi.put((byte) 0);
    }
    extResHi.put(b.toArrayUnsafe());

    return this;
  }

  public Trace extResLo(final Bytes b) {
    if (filled.get(19)) {
      throw new IllegalStateException("ecdata.EXT_RES_LO already set");
    } else {
      filled.set(19);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      extResLo.put((byte) 0);
    }
    extResLo.put(b.toArrayUnsafe());

    return this;
  }

  public Trace g2MembershipTestRequired(final Boolean b) {
    if (filled.get(20)) {
      throw new IllegalStateException("ecdata.G2_MEMBERSHIP_TEST_REQUIRED already set");
    } else {
      filled.set(20);
    }

    g2MembershipTestRequired.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace hurdle(final Boolean b) {
    if (filled.get(21)) {
      throw new IllegalStateException("ecdata.HURDLE already set");
    } else {
      filled.set(21);
    }

    hurdle.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace id(final long b) {
    if (filled.get(22)) {
      throw new IllegalStateException("ecdata.ID already set");
    } else {
      filled.set(22);
    }

    id.putLong(b);

    return this;
  }

  public Trace index(final UnsignedByte b) {
    if (filled.get(23)) {
      throw new IllegalStateException("ecdata.INDEX already set");
    } else {
      filled.set(23);
    }

    index.put(b.toByte());

    return this;
  }

  public Trace indexMax(final Bytes b) {
    if (filled.get(24)) {
      throw new IllegalStateException("ecdata.INDEX_MAX already set");
    } else {
      filled.set(24);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      indexMax.put((byte) 0);
    }
    indexMax.put(b.toArrayUnsafe());

    return this;
  }

  public Trace internalChecksPassed(final Boolean b) {
    if (filled.get(25)) {
      throw new IllegalStateException("ecdata.INTERNAL_CHECKS_PASSED already set");
    } else {
      filled.set(25);
    }

    internalChecksPassed.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcaddData(final Boolean b) {
    if (filled.get(26)) {
      throw new IllegalStateException("ecdata.IS_ECADD_DATA already set");
    } else {
      filled.set(26);
    }

    isEcaddData.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcaddResult(final Boolean b) {
    if (filled.get(27)) {
      throw new IllegalStateException("ecdata.IS_ECADD_RESULT already set");
    } else {
      filled.set(27);
    }

    isEcaddResult.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcmulData(final Boolean b) {
    if (filled.get(28)) {
      throw new IllegalStateException("ecdata.IS_ECMUL_DATA already set");
    } else {
      filled.set(28);
    }

    isEcmulData.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcmulResult(final Boolean b) {
    if (filled.get(29)) {
      throw new IllegalStateException("ecdata.IS_ECMUL_RESULT already set");
    } else {
      filled.set(29);
    }

    isEcmulResult.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcpairingData(final Boolean b) {
    if (filled.get(30)) {
      throw new IllegalStateException("ecdata.IS_ECPAIRING_DATA already set");
    } else {
      filled.set(30);
    }

    isEcpairingData.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcpairingResult(final Boolean b) {
    if (filled.get(31)) {
      throw new IllegalStateException("ecdata.IS_ECPAIRING_RESULT already set");
    } else {
      filled.set(31);
    }

    isEcpairingResult.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcrecoverData(final Boolean b) {
    if (filled.get(32)) {
      throw new IllegalStateException("ecdata.IS_ECRECOVER_DATA already set");
    } else {
      filled.set(32);
    }

    isEcrecoverData.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcrecoverResult(final Boolean b) {
    if (filled.get(33)) {
      throw new IllegalStateException("ecdata.IS_ECRECOVER_RESULT already set");
    } else {
      filled.set(33);
    }

    isEcrecoverResult.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isInfinity(final Boolean b) {
    if (filled.get(34)) {
      throw new IllegalStateException("ecdata.IS_INFINITY already set");
    } else {
      filled.set(34);
    }

    isInfinity.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isLargePoint(final Boolean b) {
    if (filled.get(35)) {
      throw new IllegalStateException("ecdata.IS_LARGE_POINT already set");
    } else {
      filled.set(35);
    }

    isLargePoint.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isSmallPoint(final Boolean b) {
    if (filled.get(36)) {
      throw new IllegalStateException("ecdata.IS_SMALL_POINT already set");
    } else {
      filled.set(36);
    }

    isSmallPoint.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace limb(final Bytes b) {
    if (filled.get(37)) {
      throw new IllegalStateException("ecdata.LIMB already set");
    } else {
      filled.set(37);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      limb.put((byte) 0);
    }
    limb.put(b.toArrayUnsafe());

    return this;
  }

  public Trace notOnG2(final Boolean b) {
    if (filled.get(38)) {
      throw new IllegalStateException("ecdata.NOT_ON_G2 already set");
    } else {
      filled.set(38);
    }

    notOnG2.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace notOnG2Acc(final Boolean b) {
    if (filled.get(39)) {
      throw new IllegalStateException("ecdata.NOT_ON_G2_ACC already set");
    } else {
      filled.set(39);
    }

    notOnG2Acc.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace notOnG2AccMax(final Boolean b) {
    if (filled.get(40)) {
      throw new IllegalStateException("ecdata.NOT_ON_G2_ACC_MAX already set");
    } else {
      filled.set(40);
    }

    notOnG2AccMax.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace overallTrivialPairing(final Boolean b) {
    if (filled.get(41)) {
      throw new IllegalStateException("ecdata.OVERALL_TRIVIAL_PAIRING already set");
    } else {
      filled.set(41);
    }

    overallTrivialPairing.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace phase(final int b) {
    if (filled.get(42)) {
      throw new IllegalStateException("ecdata.PHASE already set");
    } else {
      filled.set(42);
    }

    phase.putInt(b);

    return this;
  }

  public Trace stamp(final long b) {
    if (filled.get(43)) {
      throw new IllegalStateException("ecdata.STAMP already set");
    } else {
      filled.set(43);
    }

    stamp.putLong(b);

    return this;
  }

  public Trace successBit(final Boolean b) {
    if (filled.get(44)) {
      throw new IllegalStateException("ecdata.SUCCESS_BIT already set");
    } else {
      filled.set(44);
    }

    successBit.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace totalPairings(final Bytes b) {
    if (filled.get(45)) {
      throw new IllegalStateException("ecdata.TOTAL_PAIRINGS already set");
    } else {
      filled.set(45);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      totalPairings.put((byte) 0);
    }
    totalPairings.put(b.toArrayUnsafe());

    return this;
  }

  public Trace totalSize(final Bytes b) {
    if (filled.get(46)) {
      throw new IllegalStateException("ecdata.TOTAL_SIZE already set");
    } else {
      filled.set(46);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      totalSize.put((byte) 0);
    }
    totalSize.put(b.toArrayUnsafe());

    return this;
  }

  public Trace wcpArg1Hi(final Bytes b) {
    if (filled.get(47)) {
      throw new IllegalStateException("ecdata.WCP_ARG1_HI already set");
    } else {
      filled.set(47);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      wcpArg1Hi.put((byte) 0);
    }
    wcpArg1Hi.put(b.toArrayUnsafe());

    return this;
  }

  public Trace wcpArg1Lo(final Bytes b) {
    if (filled.get(48)) {
      throw new IllegalStateException("ecdata.WCP_ARG1_LO already set");
    } else {
      filled.set(48);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      wcpArg1Lo.put((byte) 0);
    }
    wcpArg1Lo.put(b.toArrayUnsafe());

    return this;
  }

  public Trace wcpArg2Hi(final Bytes b) {
    if (filled.get(49)) {
      throw new IllegalStateException("ecdata.WCP_ARG2_HI already set");
    } else {
      filled.set(49);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      wcpArg2Hi.put((byte) 0);
    }
    wcpArg2Hi.put(b.toArrayUnsafe());

    return this;
  }

  public Trace wcpArg2Lo(final Bytes b) {
    if (filled.get(50)) {
      throw new IllegalStateException("ecdata.WCP_ARG2_LO already set");
    } else {
      filled.set(50);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      wcpArg2Lo.put((byte) 0);
    }
    wcpArg2Lo.put(b.toArrayUnsafe());

    return this;
  }

  public Trace wcpFlag(final Boolean b) {
    if (filled.get(51)) {
      throw new IllegalStateException("ecdata.WCP_FLAG already set");
    } else {
      filled.set(51);
    }

    wcpFlag.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace wcpInst(final UnsignedByte b) {
    if (filled.get(52)) {
      throw new IllegalStateException("ecdata.WCP_INST already set");
    } else {
      filled.set(52);
    }

    wcpInst.put(b.toByte());

    return this;
  }

  public Trace wcpRes(final Boolean b) {
    if (filled.get(53)) {
      throw new IllegalStateException("ecdata.WCP_RES already set");
    } else {
      filled.set(53);
    }

    wcpRes.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace validateRow() {
    if (!filled.get(1)) {
      throw new IllegalStateException("ecdata.ACC_PAIRINGS has not been filled");
    }

    if (!filled.get(0)) {
      throw new IllegalStateException(
          "ecdata.ACCEPTABLE_PAIR_OF_POINT_FOR_PAIRING_CIRCUIT has not been filled");
    }

    if (!filled.get(2)) {
      throw new IllegalStateException("ecdata.BYTE_DELTA has not been filled");
    }

    if (!filled.get(3)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_ECADD has not been filled");
    }

    if (!filled.get(4)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_ECMUL has not been filled");
    }

    if (!filled.get(5)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_ECPAIRING has not been filled");
    }

    if (!filled.get(6)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_ECRECOVER has not been filled");
    }

    if (!filled.get(7)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_G2_MEMBERSHIP has not been filled");
    }

    if (!filled.get(8)) {
      throw new IllegalStateException("ecdata.CT has not been filled");
    }

    if (!filled.get(9)) {
      throw new IllegalStateException("ecdata.CT_MAX has not been filled");
    }

    if (!filled.get(10)) {
      throw new IllegalStateException("ecdata.EXT_ARG1_HI has not been filled");
    }

    if (!filled.get(11)) {
      throw new IllegalStateException("ecdata.EXT_ARG1_LO has not been filled");
    }

    if (!filled.get(12)) {
      throw new IllegalStateException("ecdata.EXT_ARG2_HI has not been filled");
    }

    if (!filled.get(13)) {
      throw new IllegalStateException("ecdata.EXT_ARG2_LO has not been filled");
    }

    if (!filled.get(14)) {
      throw new IllegalStateException("ecdata.EXT_ARG3_HI has not been filled");
    }

    if (!filled.get(15)) {
      throw new IllegalStateException("ecdata.EXT_ARG3_LO has not been filled");
    }

    if (!filled.get(16)) {
      throw new IllegalStateException("ecdata.EXT_FLAG has not been filled");
    }

    if (!filled.get(17)) {
      throw new IllegalStateException("ecdata.EXT_INST has not been filled");
    }

    if (!filled.get(18)) {
      throw new IllegalStateException("ecdata.EXT_RES_HI has not been filled");
    }

    if (!filled.get(19)) {
      throw new IllegalStateException("ecdata.EXT_RES_LO has not been filled");
    }

    if (!filled.get(20)) {
      throw new IllegalStateException("ecdata.G2_MEMBERSHIP_TEST_REQUIRED has not been filled");
    }

    if (!filled.get(21)) {
      throw new IllegalStateException("ecdata.HURDLE has not been filled");
    }

    if (!filled.get(22)) {
      throw new IllegalStateException("ecdata.ID has not been filled");
    }

    if (!filled.get(23)) {
      throw new IllegalStateException("ecdata.INDEX has not been filled");
    }

    if (!filled.get(24)) {
      throw new IllegalStateException("ecdata.INDEX_MAX has not been filled");
    }

    if (!filled.get(25)) {
      throw new IllegalStateException("ecdata.INTERNAL_CHECKS_PASSED has not been filled");
    }

    if (!filled.get(26)) {
      throw new IllegalStateException("ecdata.IS_ECADD_DATA has not been filled");
    }

    if (!filled.get(27)) {
      throw new IllegalStateException("ecdata.IS_ECADD_RESULT has not been filled");
    }

    if (!filled.get(28)) {
      throw new IllegalStateException("ecdata.IS_ECMUL_DATA has not been filled");
    }

    if (!filled.get(29)) {
      throw new IllegalStateException("ecdata.IS_ECMUL_RESULT has not been filled");
    }

    if (!filled.get(30)) {
      throw new IllegalStateException("ecdata.IS_ECPAIRING_DATA has not been filled");
    }

    if (!filled.get(31)) {
      throw new IllegalStateException("ecdata.IS_ECPAIRING_RESULT has not been filled");
    }

    if (!filled.get(32)) {
      throw new IllegalStateException("ecdata.IS_ECRECOVER_DATA has not been filled");
    }

    if (!filled.get(33)) {
      throw new IllegalStateException("ecdata.IS_ECRECOVER_RESULT has not been filled");
    }

    if (!filled.get(34)) {
      throw new IllegalStateException("ecdata.IS_INFINITY has not been filled");
    }

    if (!filled.get(35)) {
      throw new IllegalStateException("ecdata.IS_LARGE_POINT has not been filled");
    }

    if (!filled.get(36)) {
      throw new IllegalStateException("ecdata.IS_SMALL_POINT has not been filled");
    }

    if (!filled.get(37)) {
      throw new IllegalStateException("ecdata.LIMB has not been filled");
    }

    if (!filled.get(38)) {
      throw new IllegalStateException("ecdata.NOT_ON_G2 has not been filled");
    }

    if (!filled.get(39)) {
      throw new IllegalStateException("ecdata.NOT_ON_G2_ACC has not been filled");
    }

    if (!filled.get(40)) {
      throw new IllegalStateException("ecdata.NOT_ON_G2_ACC_MAX has not been filled");
    }

    if (!filled.get(41)) {
      throw new IllegalStateException("ecdata.OVERALL_TRIVIAL_PAIRING has not been filled");
    }

    if (!filled.get(42)) {
      throw new IllegalStateException("ecdata.PHASE has not been filled");
    }

    if (!filled.get(43)) {
      throw new IllegalStateException("ecdata.STAMP has not been filled");
    }

    if (!filled.get(44)) {
      throw new IllegalStateException("ecdata.SUCCESS_BIT has not been filled");
    }

    if (!filled.get(45)) {
      throw new IllegalStateException("ecdata.TOTAL_PAIRINGS has not been filled");
    }

    if (!filled.get(46)) {
      throw new IllegalStateException("ecdata.TOTAL_SIZE has not been filled");
    }

    if (!filled.get(47)) {
      throw new IllegalStateException("ecdata.WCP_ARG1_HI has not been filled");
    }

    if (!filled.get(48)) {
      throw new IllegalStateException("ecdata.WCP_ARG1_LO has not been filled");
    }

    if (!filled.get(49)) {
      throw new IllegalStateException("ecdata.WCP_ARG2_HI has not been filled");
    }

    if (!filled.get(50)) {
      throw new IllegalStateException("ecdata.WCP_ARG2_LO has not been filled");
    }

    if (!filled.get(51)) {
      throw new IllegalStateException("ecdata.WCP_FLAG has not been filled");
    }

    if (!filled.get(52)) {
      throw new IllegalStateException("ecdata.WCP_INST has not been filled");
    }

    if (!filled.get(53)) {
      throw new IllegalStateException("ecdata.WCP_RES has not been filled");
    }

    filled.clear();
    this.currentLine++;

    return this;
  }

  public Trace fillAndValidateRow() {
    if (!filled.get(1)) {
      accPairings.position(accPairings.position() + 32);
    }

    if (!filled.get(0)) {
      acceptablePairOfPointForPairingCircuit.position(
          acceptablePairOfPointForPairingCircuit.position() + 1);
    }

    if (!filled.get(2)) {
      byteDelta.position(byteDelta.position() + 1);
    }

    if (!filled.get(3)) {
      circuitSelectorEcadd.position(circuitSelectorEcadd.position() + 1);
    }

    if (!filled.get(4)) {
      circuitSelectorEcmul.position(circuitSelectorEcmul.position() + 1);
    }

    if (!filled.get(5)) {
      circuitSelectorEcpairing.position(circuitSelectorEcpairing.position() + 1);
    }

    if (!filled.get(6)) {
      circuitSelectorEcrecover.position(circuitSelectorEcrecover.position() + 1);
    }

    if (!filled.get(7)) {
      circuitSelectorG2Membership.position(circuitSelectorG2Membership.position() + 1);
    }

    if (!filled.get(8)) {
      ct.position(ct.position() + 2);
    }

    if (!filled.get(9)) {
      ctMax.position(ctMax.position() + 2);
    }

    if (!filled.get(10)) {
      extArg1Hi.position(extArg1Hi.position() + 32);
    }

    if (!filled.get(11)) {
      extArg1Lo.position(extArg1Lo.position() + 32);
    }

    if (!filled.get(12)) {
      extArg2Hi.position(extArg2Hi.position() + 32);
    }

    if (!filled.get(13)) {
      extArg2Lo.position(extArg2Lo.position() + 32);
    }

    if (!filled.get(14)) {
      extArg3Hi.position(extArg3Hi.position() + 32);
    }

    if (!filled.get(15)) {
      extArg3Lo.position(extArg3Lo.position() + 32);
    }

    if (!filled.get(16)) {
      extFlag.position(extFlag.position() + 1);
    }

    if (!filled.get(17)) {
      extInst.position(extInst.position() + 1);
    }

    if (!filled.get(18)) {
      extResHi.position(extResHi.position() + 32);
    }

    if (!filled.get(19)) {
      extResLo.position(extResLo.position() + 32);
    }

    if (!filled.get(20)) {
      g2MembershipTestRequired.position(g2MembershipTestRequired.position() + 1);
    }

    if (!filled.get(21)) {
      hurdle.position(hurdle.position() + 1);
    }

    if (!filled.get(22)) {
      id.position(id.position() + 8);
    }

    if (!filled.get(23)) {
      index.position(index.position() + 1);
    }

    if (!filled.get(24)) {
      indexMax.position(indexMax.position() + 32);
    }

    if (!filled.get(25)) {
      internalChecksPassed.position(internalChecksPassed.position() + 1);
    }

    if (!filled.get(26)) {
      isEcaddData.position(isEcaddData.position() + 1);
    }

    if (!filled.get(27)) {
      isEcaddResult.position(isEcaddResult.position() + 1);
    }

    if (!filled.get(28)) {
      isEcmulData.position(isEcmulData.position() + 1);
    }

    if (!filled.get(29)) {
      isEcmulResult.position(isEcmulResult.position() + 1);
    }

    if (!filled.get(30)) {
      isEcpairingData.position(isEcpairingData.position() + 1);
    }

    if (!filled.get(31)) {
      isEcpairingResult.position(isEcpairingResult.position() + 1);
    }

    if (!filled.get(32)) {
      isEcrecoverData.position(isEcrecoverData.position() + 1);
    }

    if (!filled.get(33)) {
      isEcrecoverResult.position(isEcrecoverResult.position() + 1);
    }

    if (!filled.get(34)) {
      isInfinity.position(isInfinity.position() + 1);
    }

    if (!filled.get(35)) {
      isLargePoint.position(isLargePoint.position() + 1);
    }

    if (!filled.get(36)) {
      isSmallPoint.position(isSmallPoint.position() + 1);
    }

    if (!filled.get(37)) {
      limb.position(limb.position() + 32);
    }

    if (!filled.get(38)) {
      notOnG2.position(notOnG2.position() + 1);
    }

    if (!filled.get(39)) {
      notOnG2Acc.position(notOnG2Acc.position() + 1);
    }

    if (!filled.get(40)) {
      notOnG2AccMax.position(notOnG2AccMax.position() + 1);
    }

    if (!filled.get(41)) {
      overallTrivialPairing.position(overallTrivialPairing.position() + 1);
    }

    if (!filled.get(42)) {
      phase.position(phase.position() + 4);
    }

    if (!filled.get(43)) {
      stamp.position(stamp.position() + 8);
    }

    if (!filled.get(44)) {
      successBit.position(successBit.position() + 1);
    }

    if (!filled.get(45)) {
      totalPairings.position(totalPairings.position() + 32);
    }

    if (!filled.get(46)) {
      totalSize.position(totalSize.position() + 32);
    }

    if (!filled.get(47)) {
      wcpArg1Hi.position(wcpArg1Hi.position() + 32);
    }

    if (!filled.get(48)) {
      wcpArg1Lo.position(wcpArg1Lo.position() + 32);
    }

    if (!filled.get(49)) {
      wcpArg2Hi.position(wcpArg2Hi.position() + 32);
    }

    if (!filled.get(50)) {
      wcpArg2Lo.position(wcpArg2Lo.position() + 32);
    }

    if (!filled.get(51)) {
      wcpFlag.position(wcpFlag.position() + 1);
    }

    if (!filled.get(52)) {
      wcpInst.position(wcpInst.position() + 1);
    }

    if (!filled.get(53)) {
      wcpRes.position(wcpRes.position() + 1);
    }

    filled.clear();
    this.currentLine++;

    return this;
  }

  public void build() {
    if (!filled.isEmpty()) {
      throw new IllegalStateException("Cannot build trace with a non-validated row.");
    }
  }
}
