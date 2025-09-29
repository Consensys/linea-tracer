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

package net.consensys.linea.replaytests;

import static net.consensys.linea.zktracer.ChainConfig.SEPOLIA_TESTCONFIG;
import static net.consensys.linea.zktracer.Trace.LINEA_SEPOLIA_CHAIN_ID;

import java.math.BigInteger;
import java.util.List;

import net.consensys.linea.reporting.TracerTestBase;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.testing.ToyExecutionEnvironmentV2;
import net.consensys.linea.testing.ToyTransaction;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

public class SepoliaIncidentShanghai20250929 extends TracerTestBase {

  @Test
  void sepoliaBroken(TestInfo testInfo) {
    final Address sender = Address.fromHexString("0x17e764Ba16C95815cA06Fb5d174f08D842E340df");
    final long nonce = 94;
    final long gasLimit = 67702;
    final long maxFeePerGas = 86837313;
    final long maxPriorityFee = 86837303;
    final Bytes32 r =
        Bytes32.fromHexString("0x829ddf058f7506b3a55184c02ab9b4e07690754687c7875e7fdc95ed75d9f126");
    final Bytes32 s =
        Bytes32.fromHexString("0x5de00edd521aef78b3710cb003dbdba50a57e5336d10d4366689709104a06912");
    final Bytes v = Bytes.of(1);
    final long value = 0;
    final Bytes payload =
        Bytes.fromHexString(
            "0x6080604052348015600e575f80fd5b50603e80601a5f395ff3fe60806040525f80fdfea2646970667358221220efe79e1e7d531be5f170d451c358bcde343b2b7a8bc35b84f0e8e0cbb00765a564736f6c634300081a0033");
    // "hash": "0x6ff06bf055274bcbe7750d84ce96a34a0687119b0324857763a3bc3cd7941b4c",

    final ToyAccount senderAccount =
        ToyAccount.builder().address(sender).nonce(nonce).balance(Wei.fromEth(10)).build();

    final Transaction tx =
        ToyTransaction.builder()
            .sender(senderAccount)
            .gasLimit(gasLimit)
            .maxFeePerGas(Wei.of(maxFeePerGas))
            .maxPriorityFeePerGas(Wei.of(maxPriorityFee))
            .value(Wei.of(value))
            .payload(payload)
            .transactionType(TransactionType.EIP1559)
            .accessList(List.of())
            .chainId(BigInteger.valueOf(LINEA_SEPOLIA_CHAIN_ID))
            .signature(
                Bytes.concatenate(
                    Bytes32.leftPad(r), // r
                    Bytes32.leftPad(s), // s
                    v // v
                    ))
            .build();

    ToyExecutionEnvironmentV2.builder(SEPOLIA_TESTCONFIG(chainConfig.fork), testInfo)
        .accounts(List.of(senderAccount))
        .transaction(tx)
        .zkTracerValidator(zkTracer -> {})
        .build()
        .run();
  }
}
