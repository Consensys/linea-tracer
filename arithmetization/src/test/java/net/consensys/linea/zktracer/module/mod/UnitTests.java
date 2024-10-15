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
package net.consensys.linea.zktracer.module.mod;

import java.math.BigInteger;

import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.apache.tuweni.units.bigints.UInt256;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.Preconditions;

public class UnitTests {

  @Test
  void absoluteValueTest() {

    String sixtyFourFs = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
    EWord sffEWord = EWord.ofHexString(sixtyFourFs);

    BigInteger negativeOne = BigInteger.valueOf(-1);
    Bytes neg = Bytes.of(negativeOne.toByteArray());
    Bytes32 neg32 = Bytes32.repeat((byte) 0xff);

    UInt256 sffUint256 = UInt256.fromHexString(sixtyFourFs);
    Bytes32 sffBytes32 = Bytes32.fromHexString(sixtyFourFs);
    ModOperation modOperation = new ModOperation(OpCode.SMOD, Bytes32.ZERO, Bytes32.ZERO);

    UInt256 neg32ToUbitoUI = UInt256.valueOf(neg32.toUnsignedBigInteger().abs());
    UInt256 neg32ToBItoUI = UInt256.valueOf(neg32.toBigInteger().abs());
    UInt256 negAbsoluteValue = modOperation.absoluteValueIfSignedInst(neg32);

    UInt256 b32ToUbitoUI = UInt256.valueOf(sffBytes32.toUnsignedBigInteger().abs());
    UInt256 b32ToBItoUI = UInt256.valueOf(sffBytes32.toBigInteger().abs());
    UInt256 b32AbsoluteValue = modOperation.absoluteValueIfSignedInst(sffBytes32);

    UInt256 ewToUbitoUI = UInt256.valueOf(sffEWord.toUnsignedBigInteger().abs());
    UInt256 ewToBItoUI = UInt256.valueOf(sffEWord.toBigInteger().abs());
    UInt256 ewAbsoluteValue = modOperation.absoluteValueIfSignedInst(sffEWord);

    UInt256 uiToUbiToUi = UInt256.valueOf(sffUint256.toUnsignedBigInteger().abs());
    UInt256 uiToBiToUi = UInt256.valueOf(sffUint256.toBigInteger().abs());
    UInt256 uiAbsoluteValue = modOperation.absoluteValueIfSignedInst(sffUint256);

    Preconditions.condition(
        b32AbsoluteValue.toLong() == 1, // this checks b32AbsoluteValue.fitsLong() implicitly
        "Absolute value test failed");

    int x = 42;
  }
}
