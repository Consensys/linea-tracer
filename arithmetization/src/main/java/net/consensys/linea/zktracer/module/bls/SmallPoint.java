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

import static net.consensys.linea.zktracer.module.bls.BlsUtils.B;
import static net.consensys.linea.zktracer.module.bls.BlsUtils.BETA;
import static net.consensys.linea.zktracer.module.bls.BlsUtils.SEED;
import static net.consensys.linea.zktracer.module.bls.BlsUtils.SMALL_POINT_AT_INFINITY;

import java.math.BigInteger;

public class SmallPoint {
  Fp x;
  Fp y;

  SmallPoint(Fp x, Fp y) {
    this.x = x;
    this.y = y;
  }

  boolean isOnCurve() {
    // Curve Fp equation: Y^2 = X^3+B (mod p)
    Fp left = y.pow2();
    Fp right = x.pow3().add(B);
    return left.equals(right);
  }

  boolean isInSubGroup() {
    // Reference: https://eips.ethereum.org/assets/eip-2537/fast_subgroup_checks
    // Verify phi(P) + SEED^2*P = 0
    return (this.phi().add(this.mul(SEED).mul(SEED))).equals(SMALL_POINT_AT_INFINITY);
  }

  SmallPoint phi() {
    return new SmallPoint(BETA.mul(x), y);
  }

  // TODO: double check
  SmallPoint add(SmallPoint other) {
    if (this.equals(SMALL_POINT_AT_INFINITY)) {
      return other;
    }
    if (other.equals(SMALL_POINT_AT_INFINITY)) {
      return this;
    }
    if (this.x.equals(other.x) && !this.y.equals(other.y)) {
      return SMALL_POINT_AT_INFINITY;
    }
    Fp slope;
    if (this.x.equals(other.x) && this.y.equals(other.y)) {
      // Point doubling
      Fp numerator = (new Fp("3")).mul(this.x.pow2());
      Fp denominator = (new Fp("2")).mul(this.y);
      slope = numerator.mul(denominator.multiplicativeInverse());
    } else {
      // Point multiplication
      Fp numerator = other.y.sub(this.y);
      Fp denominator = other.x.sub(this.x);
      slope = numerator.mul(denominator.multiplicativeInverse());
    }
    Fp xRes = slope.pow2().sub(this.x).sub(other.x);
    Fp yRes = slope.mul(this.x.sub(xRes)).sub(this.y);
    return new SmallPoint(xRes, yRes);
  }

  SmallPoint mul(BigInteger scalar) {
    if (scalar.equals(BigInteger.ZERO)) {
      return SMALL_POINT_AT_INFINITY;
    }
    if (scalar.equals(BigInteger.ONE)) {
      return this;
    }
    SmallPoint result = SMALL_POINT_AT_INFINITY;
    SmallPoint addend = this;
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
