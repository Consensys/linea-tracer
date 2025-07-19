package net.consensys.linea.zktracer.module.bls;

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

import static net.consensys.linea.zktracer.module.bls.SmallPoint.B;
import static net.consensys.linea.zktracer.module.bls.SmallPoint.SEED;

import java.math.BigInteger;

public class LargePoint {
  // Reference: https://eips.ethereum.org/assets/eip-2537/fast_subgroup_checks
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

  static final LargePoint POINT_AT_INFINITY =
      new LargePoint(new Fp2(new Fp("0"), new Fp("0")), new Fp2(new Fp("0"), new Fp("0")));

  Fp2 x;
  Fp2 y;

  LargePoint(Fp2 x, Fp2 y) {
    this.x = x;
    this.y = y;
  }

  boolean isOnCurve() {
    // Curve Fp2 equation: Y^2 = X^3 + B*(v+1) where v is the square root of nr2
    Fp2 twistCurveCoeff = new Fp2(B, B); // B*(1+v)
    Fp2 left = y.pow2();
    Fp2 right = x.pow3().add(twistCurveCoeff);
    return left.equals(right);
  }

  boolean isInSubGroup() {
    // Verify psi(P) + SEED*P = 0
    return this.psi().add(this.mul(SEED)).equals(POINT_AT_INFINITY);
  }

  LargePoint psi() {
    return new LargePoint((x.conjugate()).mul(R), (y.conjugate()).mul(S));
  }

  // TODO: double check
  LargePoint add(LargePoint other) {
    if (this == POINT_AT_INFINITY) {
      return other;
    }
    if (other == POINT_AT_INFINITY) {
      return this;
    }
    Fp2 slope;
    if (this.x.equals(other.x) && this.y.equals(other.y)) {
      // Point doubling
      Fp2 numerator = (new Fp2(new Fp("3"), new Fp("0"))).mul(this.x.pow2());
      Fp2 denominator = (new Fp2(new Fp("2"), new Fp("0"))).mul(this.y);
      slope = numerator.mul(denominator.multiplicativeInverse());
    } else {
      // Point multiplication
      Fp2 numerator = other.y.sub(this.y);
      Fp2 denominator = other.x.sub(this.x);
      slope = numerator.mul(denominator.multiplicativeInverse());
    }
    Fp2 xRes = slope.pow2().sub(this.x).sub(other.x);
    Fp2 yRes = slope.mul(xRes.sub(this.x)).sub(this.y);
    return new LargePoint(xRes, yRes);
  }

  LargePoint mul(BigInteger scalar) {
    if (scalar.equals(BigInteger.ZERO)) {
      return POINT_AT_INFINITY;
    }
    if (scalar.equals(BigInteger.ONE)) {
      return this;
    }
    LargePoint result = POINT_AT_INFINITY;
    LargePoint addend = this;
    // Double-and-add algorithm
    while (scalar.signum() > 0) {
      if (scalar.testBit(0)) {
        result = result.add(addend);
      }
      addend = addend.add(addend);
      scalar = scalar.shiftRight(1);
    }
    return result;
  }
}
