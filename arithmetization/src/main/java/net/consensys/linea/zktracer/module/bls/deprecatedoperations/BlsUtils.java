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

package net.consensys.linea.zktracer.module.bls.deprecatedoperations;

import java.math.BigInteger;

public class BlsUtils {
  // Reference: https://eips.ethereum.org/EIPS/eip-2537
  static final Fp B = new Fp(BigInteger.valueOf(4));

  // Reference: https://eips.ethereum.org/assets/eip-2537/fast_subgroup_checks
  static final BigInteger SEED = new BigInteger("-15132376222941642752");
  static final Fp BETA =
      new Fp(
          "793479390729215512621379701633421447060886740281060493010456487427281649075476305620758731620350");
  static final Fp2 R =
      new Fp2(
          new Fp("0"),
          new Fp(
              "4002409555221667392624310435006688643935503118305586438271171395842971157480381377015405980053539358417135540939437"));
  static final Fp2 S =
      new Fp2(
          new Fp(
              "2973677408986561043442465346520108879172042883009249989176415018091420807192182638567116318576472649347015917690530"),
          new Fp(
              "1028732146235106349975324479215795277384839936929757896155643118032610843298655225875571310552543014690878354869257"));
}
