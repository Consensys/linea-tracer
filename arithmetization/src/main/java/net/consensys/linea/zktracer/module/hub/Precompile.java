/*
 * Copyright ConsenSys AG.
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

package net.consensys.linea.zktracer.module.hub;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.hyperledger.besu.datatypes.Address;

@RequiredArgsConstructor
public enum Precompile {
  EC_RECOVER(Address.ECREC),
  SHA2_256(Address.SHA256),
  RIPEMD_160(Address.RIPEMD160),
  IDENTITY(Address.ID),
  MODEXP(Address.MODEXP),
  EC_ADD(Address.ALTBN128_ADD),
  EC_MUL(Address.ALTBN128_MUL),
  EC_PAIRING(Address.ALTBN128_PAIRING),
  BLAKE2F(Address.BLAKE2B_F_COMPRESSION);

  public final Address address;

  public static Optional<Precompile> maybeOf(Address a) {
    if (a.equals(Address.ECREC)) {
      return Optional.of(Precompile.EC_RECOVER);
    } else {
      return Optional.empty();
    }
  }
}
