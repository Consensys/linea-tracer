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

package net.consensys.linea.zktracer.module.rlptxrcpt;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.util.List;

import net.consensys.linea.zktracer.ZkTraceBuilder;
import net.consensys.linea.zktracer.corset.CorsetValidator;
import net.consensys.linea.zktracer.opcode.OpCodes;
import net.consensys.linea.zktracer.testing.ToyAccount;
import net.consensys.linea.zktracer.testing.ToyTransaction;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.hyperledger.besu.evm.log.Log;
import org.hyperledger.besu.evm.log.LogTopic;
import org.junit.jupiter.api.Test;

public class RandomTxrcptTests {
  @Test
  public void testRandomTxrcpt() {
    RlpTxrcpt rlpTxrcpt = new RlpTxrcpt();
    OpCodes.load();

    //
    // SET UP THE WORLD
    //
    KeyPair keyPair = new SECP256K1().generateKeyPair();
    Address senderAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));

    ToyAccount senderAccount =
        ToyAccount.builder().balance(Wei.of(5)).nonce(32).address(senderAddress).build();

    final Transaction tx =
        ToyTransaction.builder()
            .sender(senderAccount)
            .keyPair(keyPair)
            .transactionType(TransactionType.FRONTIER)
            .gasLimit(10_000_000L)
            .value(Wei.of(BigInteger.valueOf(2_500)))
            .payload(Bytes.EMPTY)
            .build();

    //
    // Create a mock test receipt
    //
    final Bytes output = Bytes.of(1, 2, 3, 4, 5);
    final boolean status = true;
    final List<Log> logs =
        List.of(
            new Log(
                Address.wrap(Bytes.random(20)),
                Bytes.random(16),
                List.of(LogTopic.of(Bytes.random(32)), LogTopic.of(Bytes.random(32)))));
    final long gasUsed = 2123006;

    //
    // Call the module
    //
    rlpTxrcpt.traceEndTx(null, tx, status, output, logs, gasUsed);

    //
    // Check the trace
    //
    assertThat(CorsetValidator.isValid(new ZkTraceBuilder().addTrace(rlpTxrcpt).build().toJson()))
        .isTrue();
  }
}
