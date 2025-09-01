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

package net.consensys.linea.zktracer;

import static net.consensys.linea.zktracer.Fork.isPostCancun;

import java.util.List;

import net.consensys.linea.reporting.TracerTestBase;
import net.consensys.linea.testing.MultiBlockExecutionEnvironment;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.testing.ToyTransaction;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;

public class EmptyBlockTests extends TracerTestBase {

  @Test
  void mixOfEmptyAndNonEmptyBlocks() {
    // Empty block are allowed only after Cancun
    if (isPostCancun(testInfo.chainConfig.fork)) {

      final ToyAccount receiverAccount =
          ToyAccount.builder()
              .balance(Wei.fromEth(1))
              .nonce(116)
              .address(Address.fromHexString("0xdeadbeef0000000000000000000deadbeef"))
              .build();
      final KeyPair senderKeyPair = new SECP256K1().generateKeyPair();
      final Address senderAddress =
          Address.extract(Hash.hash(senderKeyPair.getPublicKey().getEncodedBytes()));
      final ToyAccount senderAccount =
          ToyAccount.builder().balance(Wei.fromEth(128)).nonce(5).address(senderAddress).build();
      final Transaction pureTransfer =
          ToyTransaction.builder()
              .sender(senderAccount)
              .to(receiverAccount)
              .keyPair(senderKeyPair)
              .value(Wei.of(123))
              .build();

      final MultiBlockExecutionEnvironment.MultiBlockExecutionEnvironmentBuilder builder =
          MultiBlockExecutionEnvironment.builder(testInfo)
              .accounts(List.of(senderAccount, receiverAccount));

      // Two blocks are empty
      builder.addBlock(List.of());
      builder.addBlock(List.of());

      // Third block has a single transaction
      builder.addBlock(List.of(pureTransfer));

      // One more empty block
      builder.addBlock(List.of());

      builder.build().run();
    }
  }

  @Test
  void onlyOneEmptyBlock() {
    // Empty block are allowed only after Cancun
    if (isPostCancun(testInfo.chainConfig.fork)) {

      final MultiBlockExecutionEnvironment.MultiBlockExecutionEnvironmentBuilder builder =
          MultiBlockExecutionEnvironment.builder(testInfo);

      // One empty block
      builder.addBlock(List.of());

      builder.build().run();
    }
  }
}
