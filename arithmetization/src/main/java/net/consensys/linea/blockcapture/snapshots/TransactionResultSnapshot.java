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
    List<String> selfDestructs) {}
