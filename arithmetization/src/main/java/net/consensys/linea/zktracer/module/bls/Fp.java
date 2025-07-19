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

import java.math.BigInteger;

class Fp {
  BigInteger value;

  Fp(BigInteger value) {
    this.value = value.mod(BlsOperation.BLS_PRIME);
  }

  // Fp constructor from a String representation of a BigInteger base 10
  Fp(String val) {
    this(new BigInteger(val));
  }

  Fp add(Fp other) {
    return new Fp(value.add(other.value));
  }

  Fp sub(Fp other) {
    return new Fp(value.subtract(other.value));
  }

  Fp mul(Fp other) {
    return new Fp(value.multiply(other.value));
  }

  Fp multiplicativeInverse() {
    // Fermat's little theorem: a^(p-1) ≡ 1 (mod p) implies a^(p-2) ≡ a^(-1) (mod p)
    return new Fp(
        value.modPow(BlsOperation.BLS_PRIME.subtract(BigInteger.TWO), BlsOperation.BLS_PRIME));
  }

  Fp pow2() {
    return this.mul(this);
  }

  Fp pow3() {
    return this.mul(this).mul(this);
  }

  boolean equals(Fp other) {
    return value.equals(other.value);
  }

  public String toString() {
    return value.toString();
  }
}
