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
import static net.consensys.linea.zktracer.module.blsdata.deprecatedoperations.BlsUtils.R;
import static net.consensys.linea.zktracer.module.blsdata.deprecatedoperations.BlsUtils.S;
import static net.consensys.linea.zktracer.module.blsdata.deprecatedoperations.BlsUtils.SEED;

public class LargePoint extends Point<Fp2, LargePoint> {
  static final LargePoint POINT_AT_INFINITY =
      new LargePoint(new Fp2(new Fp("0"), new Fp("0")), new Fp2(new Fp("0"), new Fp("0")));
  static final Fp2 ZERO = new Fp2(new Fp("0"), new Fp("0"));
  static final Fp2 TWO = new Fp2(new Fp("2"), new Fp("0"));
  static final Fp2 THREE = new Fp2(new Fp("3"), new Fp("0"));

  LargePoint(Fp2 x, Fp2 y) {
    this.x = x;
    this.y = y;
  }

  @Override
  LargePoint createPoint(Fp2 x, Fp2 y) {
    return new LargePoint(x, y);
  }

  @Override
  boolean isOnCurve() {
    // Curve Fp2 equation: Y^2 = X^3 + B*(v+1) where v is the square root of nr2
    Fp2 twistCurveCoeff = new Fp2(B, B); // B*(1+v)
    Fp2 left = y.pow2();
    Fp2 right = x.pow3().add(twistCurveCoeff);
    return left.equals(right);
  }

  @Override
  boolean isInSubGroup() {
    // Reference: https://eips.ethereum.org/assets/eip-2537/fast_subgroup_checks
    // Verify psi(P) + SEED*P = 0
    return this.psi().add(this.mul(SEED)).equals(POINT_AT_INFINITY);
  }

  LargePoint psi() {
    return new LargePoint((x.conjugate()).mul(R), (y.conjugate()).mul(S));
  }
}
