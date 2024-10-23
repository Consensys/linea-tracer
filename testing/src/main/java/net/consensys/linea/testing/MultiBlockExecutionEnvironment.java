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

package net.consensys.linea.testing;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import lombok.Builder;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.blockcapture.snapshots.*;
import net.consensys.linea.corset.CorsetValidator;
import net.consensys.linea.zktracer.ConflationAwareOperationTracer;
import net.consensys.linea.zktracer.ZkTracer;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.units.bigints.UInt256;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.*;
import org.hyperledger.besu.ethereum.mainnet.MainnetTransactionProcessor;
import org.hyperledger.besu.ethereum.processing.TransactionProcessingResult;
import org.hyperledger.besu.ethereum.referencetests.ReferenceTestWorldState;
import org.hyperledger.besu.evm.account.MutableAccount;
import org.hyperledger.besu.evm.internal.EvmConfiguration;
import org.hyperledger.besu.evm.internal.Words;
import org.hyperledger.besu.evm.operation.BlockHashOperation;
import org.hyperledger.besu.evm.tracing.OperationTracer;
import org.hyperledger.besu.evm.worldstate.WorldUpdater;

@Builder
@Slf4j
public class MultiBlockExecutionEnvironment {
  private static final CorsetValidator CORSET_VALIDATOR = new CorsetValidator();

  @Singular("addAccount")
  private final List<ToyAccount> accounts;

  private final List<BlockSnapshot> blocks;

  /**
   * A transaction validator of each transaction; by default, it asserts that the transaction was
   * successfully processed.
   */
  @Builder.Default
  private final TransactionProcessingResultValidator transactionProcessingResultValidator =
      TransactionProcessingResultValidator.DEFAULT_VALIDATOR;


  public static class MultiBlockExecutionEnvironmentBuilder {

    private List<BlockSnapshot> blocks = new ArrayList<>();

    public MultiBlockExecutionEnvironmentBuilder addBlock(List<Transaction> transactions) {
      BlockHeaderBuilder blockHeaderBuilder =
          this.blocks.isEmpty()
              ? ExecutionEnvironment.getLineaBlockHeaderBuilder(Optional.empty())
              : ExecutionEnvironment.getLineaBlockHeaderBuilder(
                  Optional.of(this.blocks.getLast().header().toBlockHeader()));
      blockHeaderBuilder.coinbase(ToyExecutionEnvironmentV2.DEFAULT_COINBASE_ADDRESS);
      BlockBody blockBody = new BlockBody(transactions, Collections.emptyList());
      this.blocks.add(BlockSnapshot.of(blockHeaderBuilder.buildBlockHeader(), blockBody));

      return this;
    }
  }

  public void run() {
    ReplayExecutionEnvironment.builder()
        .useCoinbaseAddressFromBlockHeader(true)
        .transactionProcessingResultValidator(this.transactionProcessingResultValidator)
        .build()
        .replay(ToyExecutionEnvironmentV2.CHAIN_ID, this.buildConflationSnapshot());
  }

  private ConflationSnapshot buildConflationSnapshot() {
    List<AccountSnapshot> accountSnapshots =
        accounts.stream()
            .map(
                toyAccount ->
                    new AccountSnapshot(
                        toyAccount.getAddress().toHexString(),
                        toyAccount.getNonce(),
                        toyAccount.getBalance().toHexString(),
                        toyAccount.getCode().toHexString()))
            .toList();

    List<StorageSnapshot> storageSnapshots =
        accounts.stream()
            .flatMap(
                account ->
                    account.storage.entrySet().stream()
                        .map(
                            storageEntry ->
                                new StorageSnapshot(
                                    account.getAddress().toHexString(),
                                    storageEntry.getKey().toHexString(),
                                    storageEntry.getValue().toHexString())))
            .toList();

    List<BlockHashSnapshot> blockHashSnapshots =
        blocks.stream()
            .map(
                blockSnapshot -> {
                  BlockHeader blockHeader = blockSnapshot.header().toBlockHeader();
                  return BlockHashSnapshot.of(blockHeader.getNumber(), blockHeader.getBlockHash());
                })
            .toList();

    return new ConflationSnapshot(
        this.blocks, accountSnapshots, storageSnapshots, blockHashSnapshots);
  }

  private void executeFrom(final BigInteger chainId, final ConflationSnapshot conflation) {
    BlockHashOperation.BlockHashLookup blockHashLookup = conflation.toBlockHashLookup();
    ReferenceTestWorldState world =
        ReferenceTestWorldState.create(new HashMap<>(), EvmConfiguration.DEFAULT);
    // Initialise world state from conflation
    initWorld(world.updater(), conflation);
    // Construct the transaction processor
    final MainnetTransactionProcessor transactionProcessor =
        ExecutionEnvironment.getProtocolSpec(chainId).getTransactionProcessor();
    // Begin
    tracer.traceStartConflation(conflation.blocks().size());
    //
    for (BlockSnapshot blockSnapshot : conflation.blocks()) {
      final BlockHeader header = blockSnapshot.header().toBlockHeader();

      final BlockBody body =
          new BlockBody(
              blockSnapshot.txs().stream().map(TransactionSnapshot::toTransaction).toList(),
              new ArrayList<>());
      tracer.traceStartBlock(header, body);

      for (TransactionSnapshot txs : blockSnapshot.txs()) {
        final Transaction tx = txs.toTransaction();
        final WorldUpdater updater = world.updater();
        // Process transaction leading to expected outcome
        final TransactionProcessingResult outcome =
            transactionProcessor.processTransaction(
                updater,
                header,
                tx,
                header.getCoinbase(),
                buildOperationTracer(tx, txs.getOutcome()),
                blockHashLookup,
                false,
                Wei.ZERO);
        // Commit transaction
        updater.commit();
      }
      tracer.traceEndBlock(header, body);
    }
    tracer.traceEndConflation(world.updater());
  }

  private OperationTracer buildOperationTracer(Transaction tx, TransactionResultSnapshot txs) {
    if (txs == null) {
      String hash = tx.getHash().toHexString();
      log.info("tx `{}` outcome not checked (missing)", hash);
      return tracer;
    } else {
      return ConflationAwareOperationTracer.sequence(txs.check(), tracer);
    }
  }

  private static void initWorld(WorldUpdater world, final ConflationSnapshot conflation) {
    WorldUpdater updater = world.updater();

    for (AccountSnapshot account : conflation.accounts()) {
      // Construct contract address
      Address addr = Address.fromHexString(account.address());
      // Create account
      MutableAccount acc =
          world.createAccount(
              Words.toAddress(addr), account.nonce(), Wei.fromHexString(account.balance()));
      // Update code
      acc.setCode(Bytes.fromHexString(account.code()));
    }
    // Initialise storage
    for (StorageSnapshot s : conflation.storage()) {
      world
          .getAccount(Words.toAddress(Bytes.fromHexString(s.address())))
          .setStorageValue(UInt256.fromHexString(s.key()), UInt256.fromHexString(s.value()));
    }
    //
    world.commit();
  }
}
