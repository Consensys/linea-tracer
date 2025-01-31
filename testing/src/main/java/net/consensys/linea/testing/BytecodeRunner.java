/*
 * Copyright Consensys Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package net.consensys.linea.testing;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import lombok.Builder;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.ZkTracer;
import net.consensys.linea.zktracer.module.constants.GlobalConstants;
import net.consensys.linea.zktracer.module.hub.Hub;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;

/**
 * A BytecodeRunner takes bytecode, runs it in a single transaction in a single block, and ensures
 * that it executed correctly.
 */
@Accessors(fluent = true)
@Builder
public final class BytecodeRunner {
    public static final long DEFAULT_GAS_LIMIT = 61_000_000L;

    private final Bytes byteCode;
    private final KeyPair keyPair;
    private final ToyAccount senderAccount;
    private final ToyAccount receiverAccount;

    @Setter private Consumer<ZkTracer> zkTracerValidator = zkTracer -> {};
    private ToyExecutionEnvironmentV2 toyExecutionEnvironmentV2;

    /**
     * Creates a new BytecodeRunner instance.
     *
     * @param byteCode The bytecode to execute.
     */
    public BytecodeRunner(Bytes byteCode) {
        checkArgument(byteCode != null && !byteCode.isEmpty(), "byteCode cannot be null or empty");
        this.byteCode = byteCode;
        this.keyPair = new SECP256K1().generateKeyPair();
        this.senderAccount = createSenderAccount();
        this.receiverAccount = createReceiverAccount();
    }

    /**
     * Creates a BytecodeRunner from a BytecodeCompiler.
     *
     * @param program The BytecodeCompiler containing the bytecode.
     * @return A new BytecodeRunner instance.
     */
    public static BytecodeRunner of(BytecodeCompiler program) {
        return new BytecodeRunner(program.compile());
    }

    /**
     * Creates a BytecodeRunner from raw bytecode.
     *
     * @param byteCode The bytecode to execute.
     * @return A new BytecodeRunner instance.
     */
    public static BytecodeRunner of(Bytes byteCode) {
        return new BytecodeRunner(byteCode);
    }

    /**
     * Runs the bytecode with default parameters.
     */
    public void run() {
        this.run(Wei.fromEth(1), (long) GlobalConstants.LINEA_BLOCK_GAS_LIMIT, List.of());
    }

    /**
     * Runs the bytecode with a specified sender balance.
     *
     * @param senderBalance The balance of the sender account.
     */
    public void run(Wei senderBalance) {
        this.run(senderBalance, (long) GlobalConstants.LINEA_BLOCK_GAS_LIMIT, List.of());
    }

    /**
     * Runs the bytecode with a specified gas limit.
     *
     * @param gasLimit The gas limit for the transaction.
     */
    public void run(Long gasLimit) {
        this.run(Wei.fromEth(1), gasLimit, List.of());
    }

    /**
     * Runs the bytecode with a specified sender balance and gas limit.
     *
     * @param senderBalance The balance of the sender account.
     * @param gasLimit The gas limit for the transaction.
     */
    public void run(Wei senderBalance, Long gasLimit) {
        this.run(senderBalance, gasLimit, List.of());
    }

    /**
     * Runs the bytecode with additional accounts.
     *
     * @param additionalAccounts A list of additional accounts to include in the environment.
     */
    public void run(List<ToyAccount> additionalAccounts) {
        this.run(Wei.fromEth(1), (long) GlobalConstants.LINEA_BLOCK_GAS_LIMIT, additionalAccounts);
    }

    /**
     * Runs the bytecode with specified sender balance, gas limit, and additional accounts.
     *
     * @param senderBalance The balance of the sender account.
     * @param gasLimit The gas limit for the transaction.
     * @param additionalAccounts A list of additional accounts to include in the environment.
     */
    public void run(Wei senderBalance, Long gasLimit, List<ToyAccount> additionalAccounts) {
        checkArgument(byteCode != null && !byteCode.isEmpty(), "byteCode cannot be null or empty");

        final Long selectedGasLimit = Optional.ofNullable(gasLimit).orElse(DEFAULT_GAS_LIMIT);

        final Transaction tx =
            ToyTransaction.builder()
                .sender(senderAccount)
                .to(receiverAccount)
                .value(Wei.of(69))
                .keyPair(keyPair)
                .gasLimit(selectedGasLimit)
                .gasPrice(Wei.of(8))
                .build();

        List<ToyAccount> accounts = new ArrayList<>();
        accounts.add(senderAccount);
        accounts.add(receiverAccount);
        accounts.addAll(additionalAccounts);

        toyExecutionEnvironmentV2 =
            ToyExecutionEnvironmentV2.builder()
                .transactionProcessingResultValidator(
                    TransactionProcessingResultValidator.EMPTY_VALIDATOR)
                .accounts(accounts)
                .zkTracerValidator(zkTracerValidator)
                .transaction(tx)
                .build();

        try {
            toyExecutionEnvironmentV2.run();
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute bytecode", e);
        }
    }

    /**
     * Returns the Hub instance from the execution environment.
     *
     * @return The Hub instance.
     */
    public Hub getHub() {
        return toyExecutionEnvironmentV2.getHub();
    }

    private ToyAccount createSenderAccount() {
        Address senderAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
        return ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(5)
            .address(senderAddress)
            .build();
    }

    private ToyAccount createReceiverAccount() {
        return ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(6)
            .address(Address.fromHexString("0x1111111111111111111111111111111111111111"))
            .code(byteCode)
            .build();
    }
}

