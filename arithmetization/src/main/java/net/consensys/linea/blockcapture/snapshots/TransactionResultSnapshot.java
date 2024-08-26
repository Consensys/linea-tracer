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
package net.consensys.linea.blockcapture.snapshots;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.units.bigints.UInt256;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.ExceptionalHaltReason;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.log.Log;
import org.hyperledger.besu.evm.operation.Operation.OperationResult;
import org.hyperledger.besu.evm.tracing.OperationTracer;
import org.hyperledger.besu.evm.worldstate.WorldView;

/**
 * Records details regarding the outcome of a given transaction, such as whether it suceeded or
 * failed.
 *
 * @param status true if the transaction was successful, false otherwise
 * @param output the bytes output from the transaction (i.e. return data).
 * @param logs the logs emitted by this transaction
 * @param gasUsed the gas used by the entire transaction
 * @param accounts accounts touched by this transaction.
 * @param storage storage locations touched by this transaction.
 * @param selfDestructs accounts which self-destructed during this transaction
 */
public record TransactionResultSnapshot(
    boolean status,
    String output,
    List<String> logs,
    long gasUsed,
    List<AccountSnapshot> accounts,
    List<StorageSnapshot> storage,
    List<String> selfDestructs) {

  /**
   * Construct a suitable checker for this result snapshot which can be used within a transaction
   * processor.
   *
   * @return An implementation of OperationTracer which checks the outcome of this transaction
   *     matches.
   */
  public OperationTracer check(OperationTracer tracer) {
    return new TransactionChecker(tracer);
  }

  /** Simple wrapper which checks the transaction result matches an expected outcome. */
  private class TransactionChecker implements OperationTracer {
    private final OperationTracer tracer;

    public TransactionChecker(OperationTracer tracer) {
      this.tracer = tracer;
    }

    @Override
    public void tracePreExecution(final MessageFrame frame) {
      this.tracer.tracePreExecution(frame);
    }

    @Override
    public void tracePostExecution(
        final MessageFrame frame, final OperationResult operationResult) {
      this.tracer.tracePostExecution(frame, operationResult);
    }

    @Override
    public void tracePrecompileCall(
        final MessageFrame frame, final long gasRequirement, final Bytes output) {
      this.tracer.tracePrecompileCall(frame, gasRequirement, output);
    }

    @Override
    public void traceAccountCreationResult(
        final MessageFrame frame, final Optional<ExceptionalHaltReason> haltReason) {
      this.tracer.traceAccountCreationResult(frame, haltReason);
    }

    @Override
    public void tracePrepareTransaction(final WorldView worldView, final Transaction transaction) {
      this.tracer.tracePrepareTransaction(worldView, transaction);
    }

    @Override
    public void traceStartTransaction(final WorldView worldView, final Transaction transaction) {
      this.tracer.traceStartTransaction(worldView, transaction);
    }

    @Override
    public void traceBeforeRewardTransaction(
        final WorldView worldView, final Transaction tx, final Wei miningReward) {
      this.tracer.traceBeforeRewardTransaction(worldView, tx, miningReward);
    }

    @Override
    public void traceEndTransaction(
        WorldView world,
        Transaction tx,
        boolean status,
        Bytes output,
        List<Log> logs,
        long gasUsed,
        Set<Address> selfDestructs,
        long timeNs) {
      String hash = tx.getHash().toHexString();
      // Check against expected result
      if (TransactionResultSnapshot.this.status() != status) {
        throw new RuntimeException("tx " + hash + " outcome does not match expected outcome");
      }
      if (TransactionResultSnapshot.this.gasUsed() != gasUsed) {
        throw new RuntimeException(
            "tx " + hash + " gas (estimated) used does not expected gas used");
      }
      if (!TransactionResultSnapshot.this.output().equals(output.toHexString())) {
        throw new RuntimeException("tx " + hash + " output does not match expected output");
      }
      // Convert logs into hex strings
      List<String> actualLogStrings = logs.stream().map(l -> l.getData().toHexString()).toList();
      if (!actualLogStrings.equals(TransactionResultSnapshot.this.logs())) {
        throw new RuntimeException("tx " + hash + " logs do not match expected logs");
      }
      // Check each account
      for (AccountSnapshot expAccount : TransactionResultSnapshot.this.accounts()) {
        Address address = Address.fromHexString(expAccount.address());
        Account actAccount = world.get(address);
        // Check balance
        Wei expBalance = Wei.fromHexString(expAccount.balance());
        Wei actBalance = actAccount.getBalance();
        if (!expBalance.equals(actBalance)) {
          throw new RuntimeException(
              "tx "
                  + hash
                  + " balance of account "
                  + address
                  + " ("
                  + actBalance.toDecimalString()
                  + ") does not match expected value ("
                  + expBalance.toDecimalString()
                  + ")");
        }
        // Check nonce
        long expNonce = expAccount.nonce();
        long actNonce = actAccount.getNonce();
        if (expNonce != actNonce) {
          throw new RuntimeException(
              "tx "
                  + hash
                  + " nonce of account "
                  + address
                  + " ("
                  + actNonce
                  + ") does not match expected value ("
                  + expNonce
                  + ")");
        }
        // Check code
        Bytes expCode = Bytes.fromHexString(expAccount.code());
        Bytes actCode = actAccount.getCode();
        if (!expCode.equals(actCode)) {
          throw new RuntimeException(
              "tx "
                  + hash
                  + " code of account "
                  + address
                  + " ("
                  + actCode
                  + ") does not match expected value ("
                  + expCode
                  + ")");
        }
      }
      // Check each storage location
      for (StorageSnapshot expStorage : TransactionResultSnapshot.this.storage()) {
        Address address = Address.fromHexString(expStorage.address());
        UInt256 key = UInt256.fromHexString(expStorage.key());
        UInt256 expValue = UInt256.fromHexString(expStorage.value());
        Account actAccount = world.get(address);
        UInt256 actValue = actAccount.getStorageValue(key);
        if (!actValue.equals(expValue)) {
          throw new RuntimeException(
              "tx "
                  + hash
                  + " storage at "
                  + address
                  + ":"
                  + key
                  + "("
                  + actValue
                  + ") does not match expected value ("
                  + expValue
                  + ")");
        }
      }
      // Finally, continue tracing
      this.tracer.traceEndTransaction(
          world, tx, status, output, logs, gasUsed, selfDestructs, timeNs);
    }

    public void traceContextEnter(final MessageFrame frame) {
      this.tracer.traceContextEnter(frame);
    }

    public void traceContextReEnter(final MessageFrame frame) {
      this.tracer.traceContextReEnter(frame);
    }

    public void traceContextExit(final MessageFrame frame) {
      this.tracer.traceContextExit(frame);
    }
  }
}
