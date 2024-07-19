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

package net.consensys.linea.zktracer.testing;

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.LINEA_BASE_FEE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.LINEA_BLOCK_GAS_LIMIT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.LINEA_DIFFICULTY;
import static net.consensys.linea.zktracer.runtime.stack.Stack.MAX_STACK_SIZE;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Consumer;

import lombok.Builder;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.corset.CorsetValidator;
import net.consensys.linea.zktracer.ZkTracer;
import net.consensys.linea.zktracer.module.constants.GlobalConstants;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.*;
import org.hyperledger.besu.ethereum.core.*;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.hyperledger.besu.ethereum.core.feemarket.CoinbaseFeePriceCalculator;
import org.hyperledger.besu.ethereum.mainnet.LondonTargetingGasLimitCalculator;
import org.hyperledger.besu.ethereum.mainnet.MainnetTransactionProcessor;
import org.hyperledger.besu.ethereum.mainnet.TransactionValidationParams;
import org.hyperledger.besu.ethereum.mainnet.TransactionValidatorFactory;
import org.hyperledger.besu.ethereum.mainnet.feemarket.FeeMarket;
import org.hyperledger.besu.ethereum.mainnet.feemarket.LondonFeeMarket;
import org.hyperledger.besu.ethereum.processing.TransactionProcessingResult;
import org.hyperledger.besu.ethereum.referencetests.ReferenceTestBlockchain;
import org.hyperledger.besu.ethereum.referencetests.ReferenceTestWorldState;
import org.hyperledger.besu.ethereum.rlp.RLP;
import org.hyperledger.besu.ethereum.vm.CachingBlockHashLookup;
import org.hyperledger.besu.evm.EVM;
import org.hyperledger.besu.evm.MainnetEVMs;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.gascalculator.GasCalculator;
import org.hyperledger.besu.evm.internal.EvmConfiguration;
import org.hyperledger.besu.evm.log.Log;
import org.hyperledger.besu.evm.precompile.MainnetPrecompiledContracts;
import org.hyperledger.besu.evm.precompile.PrecompileContractRegistry;
import org.hyperledger.besu.evm.processor.ContractCreationProcessor;
import org.hyperledger.besu.evm.processor.MessageCallProcessor;
import org.hyperledger.besu.evm.worldstate.WorldUpdater;

/** Fluent API for executing EVM transactions in tests. */
@Builder
@Slf4j
public class ToyExecutionEnvironmentV2 {
    public static final BigInteger CHAIN_ID = BigInteger.valueOf(1337);
    private static final CorsetValidator CORSET_VALIDATOR = new CorsetValidator();

    private static final Address DEFAULT_SENDER_ADDRESS = Address.fromHexString("0xe8f1b89");
    private static final Wei DEFAULT_VALUE = Wei.ZERO;
    private static final Bytes DEFAULT_INPUT_DATA = Bytes.EMPTY;
    private static final Bytes DEFAULT_BYTECODE = Bytes.EMPTY;
    private static final long DEFAULT_GAS_LIMIT = 1_000_000;
    private static final ToyWorld DEFAULT_TOY_WORLD = ToyWorld.empty();
    private static final Wei DEFAULT_BASE_FEE = Wei.of(LINEA_BASE_FEE);

    private static final GasCalculator gasCalculator = ZkTracer.gasCalculator;
    private static final Address minerAddress = Address.fromHexString("0x1234532342");
    private static final long DEFAULT_BLOCK_NUMBER = 6678980;
    private static final long DEFAULT_TIME_STAMP = 1347310;
    private static final Hash DEFAULT_HASH =
            Hash.fromHexStringLenient("0xdeadbeef123123666dead666dead666");
    private static final EVM evm = MainnetEVMs.london(EvmConfiguration.DEFAULT);


    private final ToyWorld toyWorld;
    @Singular private final List<Transaction> transactions;
    
    
    private static final FeeMarket feeMarket = FeeMarket.london(-1);

    public void run() {
        executeTest(this.buildGeneralStateTestCaseSpec());
    }
    private static MainnetTransactionProcessor getMainnetTransactionProcessor() {

        PrecompileContractRegistry precompileContractRegistry = new PrecompileContractRegistry();

        MainnetPrecompiledContracts.populateForIstanbul(
                precompileContractRegistry, evm.getGasCalculator());

        final MessageCallProcessor messageCallProcessor =
                new MessageCallProcessor(evm, precompileContractRegistry);

        final ContractCreationProcessor contractCreationProcessor =
                new ContractCreationProcessor(evm.getGasCalculator(), evm, false, List.of(), 0);

        return new MainnetTransactionProcessor(
                gasCalculator,
                new TransactionValidatorFactory(
                        gasCalculator,
                        new LondonTargetingGasLimitCalculator(0L, new LondonFeeMarket(0)),
                        new LondonFeeMarket(0L),
                        false,
                        Optional.of(CHAIN_ID),
                        Set.of(TransactionType.FRONTIER, TransactionType.ACCESS_LIST, TransactionType.EIP1559),
                        GlobalConstants.MAX_CODE_SIZE),
                contractCreationProcessor,
                messageCallProcessor,
                true,
                true,
                MAX_STACK_SIZE,
                feeMarket,
                CoinbaseFeePriceCalculator.eip1559());
    }

    public GeneralStateTestCaseEipSpec buildGeneralStateTestCaseSpec() {
        Map<String, ReferenceTestWorldState.AccountMock> accountMockMap = new HashMap<>();
        for(ToyAccount toyAccount: toyWorld.getAccounts()){
            accountMockMap.put(toyAccount.getAddress().toHexString(), toyAccount.toAccountMock());
        }
        ReferenceTestWorldState referenceTestWorldState = ReferenceTestWorldState.create(
                accountMockMap,
                evm.getEvmConfiguration()
        );
        BlockHeader blockHeader =
                BlockHeaderBuilder.createDefault()
                        .baseFee(DEFAULT_BASE_FEE)
                        .gasLimit(LINEA_BLOCK_GAS_LIMIT)
                        .difficulty(Difficulty.of(LINEA_DIFFICULTY))
                        .number(DEFAULT_BLOCK_NUMBER)
                        .coinbase(minerAddress)
                        .timestamp(DEFAULT_TIME_STAMP)
                        .parentHash(DEFAULT_HASH)
                        .buildBlockHeader();

        return new GeneralStateTestCaseEipSpec(
                /*fork*/evm.getEvmVersion().getName().toLowerCase(),
                /*transactionSupplier*/transactions::getFirst,
                /*initialWorldState*/referenceTestWorldState,
                /*expectedRootHash*/null,
                /*expectedLogsHash*/null,
                blockHeader,
                /*dataIndex*/ -1,
                /*gasIndex*/ -1,
                /*valueIndex*/ -1,
                /*expectException*/ null
                );
    }

    public static void executeTest(final GeneralStateTestCaseEipSpec spec) {
        final BlockHeader blockHeader = spec.getBlockHeader();
        final ReferenceTestWorldState initialWorldState = spec.getInitialWorldState();
        final Transaction transaction = spec.getTransaction();
        final BlockBody blockBody = new BlockBody(List.of(transaction), new ArrayList<>());

        // Sometimes the tests ask us assemble an invalid transaction.  If we have
        // no valid transaction then there is no test.  GeneralBlockChain tests
        // will handle the case where we receive the TXs in a serialized form.
        if (transaction == null) {
            assertThat(spec.getExpectException())
                    .withFailMessage("Transaction was not assembled, but no exception was expected")
                    .isNotNull();
            return;
        }

        final MutableWorldState worldState = initialWorldState.copy();
        // Several of the GeneralStateTests check if the transaction could potentially
        // consume more gas than is left for the block it's attempted to be included in.
        // This check is performed within the `BlockImporter` rather than inside the
        // `TransactionProcessor`, so these tests are skipped.
        if (transaction.getGasLimit() > blockHeader.getGasLimit() - blockHeader.getGasUsed()) {
            return;
        }

        final MainnetTransactionProcessor processor = getMainnetTransactionProcessor();
        final WorldUpdater worldStateUpdater = worldState.updater();
        final ReferenceTestBlockchain blockchain = new ReferenceTestBlockchain(blockHeader.getNumber());
        final Wei blobGasPrice = feeMarket
                        .blobGasPricePerGas(blockHeader.getExcessBlobGas().orElse(BlobGas.ZERO));

        final ZkTracer tracer = new ZkTracer();

        tracer.traceStartConflation(1);
        tracer.traceStartBlock(blockHeader, blockBody);

        final TransactionProcessingResult result =
                processor.processTransaction(
                        worldStateUpdater,
                        blockHeader,
                        transaction,
                        blockHeader.getCoinbase(),
                        tracer,
                        new CachingBlockHashLookup(blockHeader, blockchain),
                        false,
                        TransactionValidationParams.processingBlock(),
                        blobGasPrice);
        if (result.isInvalid()) {
            assertThat(spec.getExpectException())
                    .withFailMessage(() -> result.getValidationResult().getErrorMessage())
                    .isNotNull();
            return;
        }


        tracer.traceEndBlock(blockHeader, blockBody);
        tracer.traceEndConflation(worldStateUpdater);

        assertThat(spec.getExpectException())
                .withFailMessage("Exception was expected - " + spec.getExpectException())
                .isNull();

        final Account coinbase = worldStateUpdater.getOrCreate(spec.getBlockHeader().getCoinbase());
        if (coinbase != null && coinbase.isEmpty() && shouldClearEmptyAccounts(spec.getFork())) {
            worldStateUpdater.deleteAccount(coinbase.getAddress());
        }
        worldStateUpdater.commit();
        worldState.persist(blockHeader);

        // Check the world state root hash.
        final Hash expectedRootHash = spec.getExpectedRootHash();
        Optional.ofNullable(expectedRootHash).ifPresent(
                expected -> {
                    assertThat(worldState.rootHash())
                            .withFailMessage(
                                    "Unexpected world state root hash; expected state: %s, computed state: %s",
                                    spec.getExpectedRootHash(), worldState.rootHash())
                            .isEqualTo(expected);
                }
        );

        // Check the logs.
        final Hash expectedLogsHash = spec.getExpectedLogsHash();
        Optional.ofNullable(expectedLogsHash)
                .ifPresent(
                        expected -> {
                            final List<Log> logs = result.getLogs();

                            assertThat(Hash.hash(RLP.encode(out -> out.writeList(logs, Log::writeTo))))
                                    .withFailMessage("Unmatched logs hash. Generated logs: %s", logs)
                                    .isEqualTo(expected);
                        });

        assertThat(CORSET_VALIDATOR.validate(tracer.writeToTmpFile()).isValid()).isTrue();
    }

    private static final List<String> SPECS_PRIOR_TO_DELETING_EMPTY_ACCOUNTS =
            Arrays.asList("Frontier", "Homestead", "EIP150");

    private static boolean shouldClearEmptyAccounts(final String eip) {
        return !SPECS_PRIOR_TO_DELETING_EMPTY_ACCOUNTS.contains(eip);
    }

}
