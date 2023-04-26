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
package net.consensys.linea.zktracer.module.trm;

import static org.assertj.core.api.Assertions.assertThat;

import org.hyperledger.besu.datatypes.Address;

import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.junit.jupiter.api.Test;

public class TrmDataTest {

  @Test
  public void isPrecompile() {
    assertThat(new TrmData(Bytes32.ZERO).getIsPrec()).isFalse();
    assertThat(new TrmData(Bytes32.rightPad(Bytes.fromHexString("0x1234"))).getIsPrec()).isFalse();
    // 0x06
    assertThat(new TrmData(Bytes32.leftPad(Address.ALTBN128_ADD)).getIsPrec()).isTrue();
    assertThat(new TrmData(Bytes32.leftPad(Address.MODEXP)).getIsPrec()).isTrue();
    // 0x09
    assertThat(new TrmData(Bytes32.leftPad(Address.BLAKE2B_F_COMPRESSION)).getIsPrec()).isTrue();
    // 0x01
    assertThat(new TrmData(Bytes32.leftPad(Address.ECREC)).getIsPrec()).isTrue();
    // 0x0A
    assertThat(new TrmData(Bytes32.leftPad(Address.BLS12_G1ADD)).getIsPrec())
        .isFalse(); // only true for 1-9
  }
}
