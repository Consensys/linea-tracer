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

package net.consensys.linea.zktracer.module.bls;

class Fp2 {
  Fp a; // real part
  Fp b; // imaginary part: coefficient of v

  Fp2(Fp a, Fp b) {
    this.a = a;
    this.b = b;
  }

  Fp2 add(Fp2 other) {
    return new Fp2(a.add(other.a), b.add(other.b));
  }

  Fp2 sub(Fp2 other) {
    return new Fp2(a.sub(other.a), b.sub(other.b));
  }

  Fp2 mul(Fp2 other) {
    // (a + b*v)(c + d*v) = (ac - bd) + (ad + bc)*v, since v^2 = -1
    Fp ac = a.mul(other.a);
    Fp bd = b.mul(other.b);
    Fp ad = a.mul(other.b);
    Fp bc = b.mul(other.a);
    return new Fp2(ac.sub(bd), ad.add(bc));
  }

  Fp2 conjugate() {
    // Conjugate of (a + b*v) is (a - b*v)
    return new Fp2(a, b.mul(new Fp("-1")));
  }

  Fp2 multiplicativeInverse() {
    // 1 / P = 1 / (a + b*v) = (a - b*v) / (a^2 + b^2)
    Fp inverseOfDenominator = (a.pow2().add(b.pow2())).multiplicativeInverse(); // (a^2 + b^2)^(-1)
    return new Fp2(a.mul(inverseOfDenominator), (this.conjugate()).b.mul(inverseOfDenominator));
  }

  Fp2 pow2() {
    return this.mul(this);
  }

  Fp2 pow3() {
    return this.mul(this).mul(this);
  }

  boolean equals(Fp2 other) {
    return a.equals(other.a) && b.equals(other.b);
  }

  public String toString() {
    return "(" + a + ") + (" + b + ")*v";
  }
}
