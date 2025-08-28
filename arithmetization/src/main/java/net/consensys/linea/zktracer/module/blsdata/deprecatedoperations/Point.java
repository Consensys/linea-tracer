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

import java.math.BigInteger;

public abstract class Point<F extends Field<F>, P extends Point<F, P>> {
  F x;
  F y;
  P POINT_AT_INFINITY;
  F ZERO;
  F TWO;
  F THREE;

  abstract P createPoint(F x, F y);

  abstract boolean isOnCurve();

  abstract boolean isInSubGroup();

  P add(P other) {
    if (this.equals(POINT_AT_INFINITY)) {
      return other;
    }
    if (other.equals(POINT_AT_INFINITY)) {
      return createPoint(this.x, this.y);
    }
    if (this.x.equals(other.x) && this.y.equals(other.y.additiveInverse())) {
      return POINT_AT_INFINITY;
    }
    F slope;
    if (this.x.equals(other.x) && this.y.equals(other.y)) {
      // Point doubling
      F numerator = THREE.mul(this.x.pow2());
      F denominator = TWO.mul(this.y);
      if (denominator.equals(ZERO)) {
        return POINT_AT_INFINITY;
      }
      slope = numerator.mul(denominator.multiplicativeInverse());
    } else {
      // !this.x.equals(other.x)
      // Point multiplication
      F numerator = other.y.sub(this.y);
      F denominator = other.x.sub(this.x);
      slope = numerator.mul(denominator.multiplicativeInverse());
    }
    F xRes = slope.pow2().sub(this.x).sub(other.x);
    F yRes = slope.mul(this.x.sub(xRes)).sub(this.y);
    return createPoint(xRes, yRes);
  }

  P mul(BigInteger scalar) {
    if (scalar.equals(BigInteger.ZERO)) {
      return POINT_AT_INFINITY;
    }
    if (scalar.equals(BigInteger.ONE)) {
      return createPoint(this.x, this.y);
    }
    P result = POINT_AT_INFINITY;
    P addend = createPoint(this.x, this.y);
    int bitLength = scalar.bitLength();
    // Double-and-add algorithm
    for (int i = bitLength - 1; i >= 0; i--) {
      // Double the result
      result = result.add(result);
      if (scalar.testBit(i)) {
        // Add addend to the result if the i-th bit is 1
        result = result.add(addend);
      }
    }
    return result;
  }
}
