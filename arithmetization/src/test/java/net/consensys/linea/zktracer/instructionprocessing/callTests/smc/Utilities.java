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
package net.consensys.linea.zktracer.instructionprocessing.callTests.smc;

import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.testing.ToyTransaction;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;

import java.util.List;

public class Utilities {

    public static KeyPair keyPair = new SECP256K1().generateKeyPair();
    public static Address userAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
    public static ToyAccount userAccount =
            ToyAccount.builder().balance(Wei.fromEth(10)).nonce(99).address(userAddress).build();

    public static ToyAccount accountWhoseByteCodeIsASingleStop =
            ToyAccount.builder()
                    .balance(Wei.fromEth(1))
                    .nonce(13)
                    .address(Address.fromHexString("c0de00"))
                    .code(Bytes.fromHexString("00"))
                    .build();

    public static ToyAccount accountWhoseByteCodeIsASingleJumpDest =
            ToyAccount.builder()
                    .balance(Wei.fromEth(1))
                    .nonce(19)
                    .address(Address.fromHexString("c0de5b"))
                    .code(Bytes.fromHexString("5b"))
                    .build();

    public static ToyAccount accountWhoseByteCodeIsASingleInvalid =
            ToyAccount.builder()
                    .balance(Wei.fromEth(1))
                    .nonce(13)
                    .address(Address.fromHexString("c0defe"))
                    .code(Bytes.fromHexString("fe"))
                    .build();

    public static List<ToyAccount> accounts = List.of(userAccount, accountWhoseByteCodeIsASingleStop, accountWhoseByteCodeIsASingleJumpDest, accountWhoseByteCodeIsASingleInvalid);


    public Transaction simpleTransaction(ToyAccount targetOfTransaction) {
        return ToyTransaction.builder()
                .sender(userAccount)
                .to(targetOfTransaction)
                .transactionType(TransactionType.FRONTIER)
                .value(Wei.of(7))
                .keyPair(keyPair)
                .gasLimit(100_000L)
                .gasPrice(Wei.of(8))
                .build();
    }
}
