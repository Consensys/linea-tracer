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
package net.consensys.linea.zktracer.instructionprocessing.selfdestructTests;

import static net.consensys.linea.zktracer.instructionprocessing.utilities.Calls.simpleCall;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.zktracer.instructionprocessing.utilities.*;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.Wei;
import org.junit.jupiter.api.Test;

public class SelfDestructResetsStorageTest {
  Address selfDestructAddress = Address.fromHexString("ffc0de");
  Address multipleCallerAddress = Address.fromHexString("ca11e7");

  public static KeyPair keyPair = new SECP256K1().generateKeyPair();
  public static Address userAddress =
      Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
  public static ToyAccount userAccount =
      ToyAccount.builder().balance(Wei.fromEth(10)).nonce(99).address(userAddress).build();

  private ToyAccount storageModifyingAccountThatSelfDestructs =
      ToyAccount.builder()
          .balance(Wei.fromEth(1))
          .nonce(13)
          .address(selfDestructAddress)
          .code(SelfDestructs.storageTouchingSelfDestructorRewardsZeroAddress().compile())
          .build();

  private ToyAccount multipleCalls() {
    BytecodeCompiler multipleCalls = BytecodeCompiler.newProgram();
    simpleCall(multipleCalls, OpCode.CALL, 100_000, selfDestructAddress, 12, 0, 4, 0, 0);
    simpleCall(multipleCalls, OpCode.CALL, 100_000, selfDestructAddress, 19, 0, 3, 0, 0);
    simpleCall(multipleCalls, OpCode.CALL, 100_000, selfDestructAddress, 26, 0, 2, 0, 0);
    return ToyAccount.builder()
        .balance(Wei.of(1_000_000L))
        .nonce(420)
        .address(multipleCallerAddress)
        .code(multipleCalls.compile())
        .build();
  }

  @Test
  void storageRemainsResetsOnlyAtTransactionEndTest() {}
}
