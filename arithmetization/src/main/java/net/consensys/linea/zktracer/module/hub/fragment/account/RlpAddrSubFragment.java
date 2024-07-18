/*
 * Copyright ConsenSys Inc.
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

package net.consensys.linea.zktracer.module.hub.fragment.account;

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.LLARGE;
import static net.consensys.linea.zktracer.types.AddressUtils.highPart;
import static net.consensys.linea.zktracer.types.AddressUtils.lowPart;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.TraceSubFragment;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;

@RequiredArgsConstructor
public class RlpAddrSubFragment implements TraceSubFragment {
  private final short recipe;
  private final Address depAddress;
  private final Bytes32 salt;
  private final Bytes32 keccak;

  public static RlpAddrSubFragment rlpAddrSubFragmentForCreate2(
      final Address depAddress, final Bytes32 Salt, final Bytes32 Keccak) {
    return new RlpAddrSubFragment((short) 2, depAddress, Salt, Keccak);
  }

  public static RlpAddrSubFragment rlpAddrSubFragmentForCreate(final Address depAddress) {
    return new RlpAddrSubFragment(
        (short) 1, depAddress, (Bytes32) Bytes32.EMPTY, (Bytes32) Bytes32.EMPTY);
  }

  @Override
  public Trace trace(Trace trace) {
    return trace
        .pAccountRlpaddrFlag(true)
        .pAccountRlpaddrRecipe(recipe == 2)
        .pAccountRlpaddrDepAddrHi(highPart(depAddress))
        .pAccountRlpaddrDepAddrLo(lowPart(depAddress))
        .pAccountRlpaddrSaltHi(salt.slice(0, LLARGE))
        .pAccountRlpaddrSaltLo(salt.slice(LLARGE, LLARGE))
        .pAccountRlpaddrKecHi(keccak.slice(0, LLARGE))
        .pAccountRlpaddrKecLo(keccak.slice(LLARGE, LLARGE));
  }
}
