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

package net.consensys.linea.zktracer.module.romLex;

import static net.consensys.linea.zktracer.module.rlpCommon.rlpRandEdgeCase.randBigInt;
import static net.consensys.linea.zktracer.module.rlpCommon.rlpRandEdgeCase.randData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.consensys.linea.zktracer.opcode.OpCodes;
import net.consensys.linea.zktracer.testing.ToyAccount;
import net.consensys.linea.zktracer.testing.ToyExecutionEnvironment;
import net.consensys.linea.zktracer.testing.ToyTransaction;
import net.consensys.linea.zktracer.testing.ToyWorld;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;

public class romLexTrigger {
  private final Random rnd = new Random(666);
  private final int nTrigger = 5;

  @Test
  void test() {
    OpCodes.load();
    ToyWorld.ToyWorldBuilder world = ToyWorld.builder();
    List<Transaction> txList = new ArrayList<>();

    for (int i = 0; i < nTrigger; i++) {
      KeyPair keyPair = new SECP256K1().generateKeyPair();
      Address senderAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
      ToyAccount senderAccount =
          ToyAccount.builder().balance(Wei.MAX_WEI).nonce(0).address(senderAddress).build();
      ;
      world.account(senderAccount);

      Transaction tx =
          ToyTransaction.builder()
              .sender(senderAccount)
              .keyPair(keyPair)
              .transactionType(TransactionType.FRONTIER)
              .gasLimit(rnd.nextLong(21000, 0xfffffffffffffL))
              .value(Wei.of(randBigInt(true)))
              .payload(randData(true))
              .build();
      txList.add(tx);
    }

    ToyExecutionEnvironment.builder()
        .toyWorld(world.build())
        .transactions(txList)
        .testValidator(x -> {})
        .build()
        .run();
  }
}
