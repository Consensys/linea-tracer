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

package net.consensys.linea.zktracer.module.hub.chunks;

import java.math.BigInteger;

import net.consensys.linea.zktracer.EWord;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.Trace;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;

public record AccountChunk(
    Address who,
    AccountSnapshot oldState,
    AccountSnapshot newState,
    boolean debit,
    long cost,
    boolean createAddress,
    int deploymentNumberInfnty,
    boolean existsInfinity)
    implements TraceChunk {
  @Override
  public Trace.TraceBuilder trace(Trace.TraceBuilder trace) {
    final EWord eWho = EWord.of(who);
    final EWord eCodeHash = EWord.of(oldState.codeHash());
    final EWord eCodeHashNew = EWord.of(newState.codeHash());

    return trace
        .peekAtAccount(true)
        .pAccountAddressHi(eWho.hiBigInt())
        .pAccountAddressLo(eWho.loBigInt())
        .pAccountIsPrecompile(Hub.isPrecompile(who))
        .pAccountNonce(BigInteger.valueOf(oldState.nonce()))
        .pAccountNonceNew(BigInteger.valueOf(newState.nonce()))
        .pAccountBalance(oldState.balance().getAsBigInteger())
        .pAccountBalanceNew(newState.balance().getAsBigInteger())
        .pAccountCodeSize(BigInteger.valueOf(oldState().codeSize()))
        .pAccountCodeSizeNew(BigInteger.valueOf(newState.codeSize()))
        .pAccountCodeHashHi(eCodeHash.hiBigInt())
        .pAccountCodeHashLo(eCodeHash.loBigInt())
        .pAccountCodeHashHiNew(eCodeHashNew.hiBigInt())
        .pAccountCodeHashLoNew(eCodeHashNew.loBigInt())
        .pAccountHasCode(oldState.codeHash() != Hash.EMPTY)
        .pAccountHasCodeNew(newState.codeHash() != Hash.EMPTY)
        .pAccountExists(
            oldState.nonce() > 0
                || oldState.codeHash() != Hash.EMPTY
                || !oldState().balance().isZero())
        .pAccountExistsNew(
            newState.nonce() > 0
                || newState.codeHash() != Hash.EMPTY
                || !newState().balance().isZero())
        .pAccountWarm(oldState.warm())
        .pAccountWarmNew(newState.warm())
        .pAccountDeploymentNumber(BigInteger.valueOf(oldState().deploymentNumber()))
        .pAccountDeploymentNumberNew(BigInteger.valueOf(newState().deploymentNumber()))
        .pAccountDeploymentStatus(oldState.deploymentStatus() ? BigInteger.ONE : BigInteger.ZERO)
        .pAccountDeploymentStatusNew(newState.deploymentStatus() ? BigInteger.ONE : BigInteger.ZERO)
        //      .pAccountDebit(debit)
        //      .pAccountCost(cost)
        .pAccountSufficientBalance(!debit || cost <= oldState.balance().toLong()) //
        //      .pAccountCreateAddress(createAddress)
        .pAccountDeploymentNumberInfty(BigInteger.valueOf(deploymentNumberInfnty))
    //    .pAccountExistsInfty(existsInfinity)
    ;
  }
}
