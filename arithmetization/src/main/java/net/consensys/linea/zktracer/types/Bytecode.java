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

package net.consensys.linea.zktracer.types;

import java.util.Objects;
import java.util.concurrent.*;

import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.evm.Code;

/** This class is intended to store a bytecode and its memoized hash. */
public final class Bytecode {
  /** initializing the executor service before creating the EMPTY bytecode. */
  private static final ExecutorService executorService = Executors.newCachedThreadPool();

  /** The empty bytecode. */
  public static Bytecode EMPTY = new Bytecode(Bytes.EMPTY);

  /** The bytecode. */
  private final Bytes bytecode;

  /** The bytecode hash; precomputed & memoized asynchronously. */
  private Future<Hash> hash;

  /**
   * Create an instance from {@link Bytes}.
   *
   * @param bytes the bytecode
   */
  public Bytecode(Bytes bytes) {
    this.bytecode = Objects.requireNonNullElse(bytes, Bytes.EMPTY);
    hash = executorService.submit(() -> computeCodeHash());
  }

  /**
   * Create an instance from Besu {@link Code}.
   *
   * @param code the bytecode
   */
  public Bytecode(Code code) {
    this.bytecode = code.getBytes();
    this.hash = CompletableFuture.completedFuture(code.getCodeHash());
  }

  /**
   * Get the size of the bytecode, in bytes.
   *
   * @return the bytecode size
   */
  public int getSize() {
    return this.bytecode.size();
  }

  /**
   * Expose the wrapped bytecode as {@link Bytes}.
   *
   * @return the bytecode
   */
  public Bytes getBytes() {
    return this.bytecode;
  }

  /**
   * Returns whether the bytecode is empty or not.
   *
   * @return true if the bytecode is empty
   */
  public boolean isEmpty() {
    return this.bytecode.isEmpty();
  }

  /**
   * Compute the bytecode hash if required, then return it.
   *
   * @return the bytecode hash
   */
  public Hash getCodeHash() {
    try {
      return hash.get();
    } catch (Exception e) {
      return computeCodeHash();
    }
  }

  private Hash computeCodeHash() {
    if (this.bytecode.isEmpty()) {
      return Hash.EMPTY;
    } else {
      return Hash.hash(this.bytecode);
    }
  }
}
