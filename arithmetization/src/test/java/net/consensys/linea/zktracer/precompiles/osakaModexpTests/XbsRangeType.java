/*
 * Copyright Consensys Software Inc.
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
package net.consensys.linea.zktracer.precompiles.osakaModexpTests;

import java.util.List;

import org.apache.tuweni.bytes.Bytes;

public enum XbsRangeType {
  UNCONDITIONALLY_VALID,
  CONDITIONALLY_VALID,
  SMALL_INVALID,
  LARGE_INVALID;

  static final Bytes VALID_XBS_ZERO =
      Bytes.fromHexString("0x0000000000000000000000000000000000000000000000000000000000000000");
  static final Bytes VALID_XBS_ONE =
      Bytes.fromHexString("0x0000000000000000000000000000000000000000000000000000000000000001");
  static final Bytes VALID_XBS_WORD =
      Bytes.fromHexString("0x0000000000000000000000000000000000000000000000000000000000000020");
  static final Bytes VALID_XBS_RAND =
      Bytes.fromHexString("0x000000000000000000000000000000000000000000000000000000000000031e");
  static final Bytes VALID_XBS_MAX =
      Bytes.fromHexString("0x0000000000000000000000000000000000000000000000000000000000000400");

  static final Bytes CONDITIONALLY_VALID_XBS_MIN =
      Bytes.fromHexString("0x0000000000000000000000000000000000000000000000000000000000000401");
  static final Bytes CONDITIONALLY_VALID_XBS_MAX =
      Bytes.fromHexString("0x00000000000000000000000000000000000000000000000000000000000004ff");

  static final String INVALID_LEAD_LIMB_ZERO = "0x00000000000000000000000000000000";
  static final String INVALID_LEAD_LIMB_RAND = "0xdeadbeef00123400ffffffff00c0ffee";
  static final String INVALID_LEAD_LIMB_MAX = "0xffffffffffffffffffffffffffffffff";

  static final String INVALID_TAIL_LIMB_ZERO = "00000000000000000000000000000000";
  static final String INVALID_TAIL_LIMB_401 = "00000000000000000000000000000401";
  static final String INVALID_TAIL_LIMB_500 = "00000000000000000000000000000500";
  static final String INVALID_TAIL_LIMB_RAND = "0000aa00ff000000beef000000004321";
  static final String INVALID_TAIL_LIMB_MAX = "ffffffffffffffffffffffffffffffff";

  /**
   * Byte sizes (<b>xbs</b>) are unconditionally valid in OSAKA if they are ≤ 1024 ≡ 0x400 after
   * trimming. The {@link #unconditionallyValidByteSizes} are those that are valid without resorting
   * to any trimming.
   */
  static final List<Bytes> unconditionallyValidByteSizes =
      List.of(VALID_XBS_ZERO, VALID_XBS_ONE, VALID_XBS_WORD, VALID_XBS_RAND, VALID_XBS_MAX);

  /**
   * Byte sizes (<b>xbs</b>) are conditionally valid in OSAKA if they are ≤ 1024 ≡ 0x400 only after
   * trimming, which means we can accept byte sizes of the form <b>0x4??</b> as long as <b>cds</b>
   * is
   *
   * <ul>
   *   <li>32 - 1 for {@link #CONDITIONALLY_VALID} bbs
   *   <li>64 - 1 for {@link #CONDITIONALLY_VALID} ebs
   *   <li>96 - 1 for {@link #CONDITIONALLY_VALID} mbs
   * </ul>
   *
   * <b>Note.</b> 1024 is <b>EIP_7823_MODEXP_UPPER_BYTE_SIZE_BOUND</b>.
   */
  static final List<Bytes> conditionallyValidByteSizes =
      List.of(CONDITIONALLY_VALID_XBS_MIN, CONDITIONALLY_VALID_XBS_MAX);

  static final List<String> headLimbsForInvalidXbses =
      List.of(INVALID_LEAD_LIMB_RAND, INVALID_LEAD_LIMB_MAX);

  static final List<String> tailLimbsForInvalidXbses =
      List.of(
          INVALID_TAIL_LIMB_ZERO,
          INVALID_TAIL_LIMB_401,
          INVALID_TAIL_LIMB_500,
          INVALID_TAIL_LIMB_RAND,
          INVALID_TAIL_LIMB_MAX);

  static final List<String> nonzeroTailLimbsForInvalidXbses =
      List.of(
          INVALID_TAIL_LIMB_401,
          INVALID_TAIL_LIMB_500,
          INVALID_TAIL_LIMB_RAND,
          INVALID_TAIL_LIMB_MAX);

  static final List<Bytes> smallInvalidByteSizes =
      nonzeroTailLimbsForInvalidXbses.stream()
          .map(tail -> Bytes.fromHexString(INVALID_LEAD_LIMB_ZERO + tail))
          .toList();
  static final List<Bytes> largeInvalidByteSizes =
      headLimbsForInvalidXbses.stream()
          .flatMap(
              head ->
                  tailLimbsForInvalidXbses.stream().map(tail -> Bytes.fromHexString(head + tail)))
          .toList();
}
