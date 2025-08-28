package net.consensys.linea.zktracer.module.blsdata.deprecatedoperations;

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

import static net.consensys.linea.zktracer.module.blsdata.deprecatedoperations.BlsUtils.B;
import static net.consensys.linea.zktracer.module.blsdata.deprecatedoperations.BlsUtils.BETA;
import static net.consensys.linea.zktracer.module.blsdata.deprecatedoperations.BlsUtils.SEED;

public class SmallPoint extends Point<Fp, SmallPoint> {
  static final SmallPoint POINT_AT_INFINITY = new SmallPoint(new Fp("0"), new Fp("0"));
  static final Fp ZERO = new Fp("0");
  static final Fp TWO = new Fp("2");
  static final Fp THREE = new Fp("3");

  SmallPoint(Fp x, Fp y) {
    this.x = x;
    this.y = y;
  }

  @Override
  SmallPoint createPoint(Fp x, Fp y) {
    return new SmallPoint(x, y);
  }

  @Override
  boolean isOnCurve() {
    // Curve Fp equation: Y^2 = X^3+B (mod p)
    Fp left = y.pow2();
    Fp right = x.pow3().add(B);
    return left.equals(right);
  }

  @Override
  boolean isInSubGroup() {
    // Reference: https://eips.ethereum.org/assets/eip-2537/fast_subgroup_checks
    // Verify phi(P) + SEED^2*P = 0
    return (this.phi().add(this.mul(SEED).mul(SEED))).equals(POINT_AT_INFINITY);
  }

  SmallPoint phi() {
    return new SmallPoint(BETA.mul(x), y);
  }
}
