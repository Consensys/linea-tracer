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

package net.consensys.linea.zktracer.opcode.gas.projector;

import static com.google.common.base.Preconditions.*;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;
import static org.hyperledger.besu.evm.internal.Words.*;

import net.consensys.linea.zktracer.ZkTracer;
import org.hyperledger.besu.evm.gascalculator.GasCalculator;

public abstract class GasProjection {
  GasCalculator gc = ZkTracer.gasCalculator;

  long linearCost(long a, long x, long unit) {
    checkArgument((unit == 1) || (unit == WORD_SIZE));
    return clampedMultiply(a, (clampedAdd(x, unit) - 1) / unit);
  }

  public long staticGas() {
    return 0;
  }

  public long expGas() {
    return 0;
  }

  public long memoryExpansion() {
    return 0;
  }

  public long accountAccess() {
    return 0;
  }

  public long accountCreation() {
    return 0;
  }

  public long transferValue() {
    return 0;
  }

  public long linearPerWord() {
    return 0;
  }

  public long linearPerByte() {
    return 0;
  }

  public long storageWarmth() {
    return 0;
  }

  public long sStoreValue() {
    return 0;
  }

  public long rawStipend() {
    return 0;
  }

  public long extraStipend() {
    return 0;
  }

  public long deploymentCost() {
    return 0;
  }

  public long refund() {
    return 0;
  }

  public long messageSize() {
    return 0;
  }

  /**
   * Returns the largest memory expansion offset generated by this instruction if applicable, 0
   * otherwise.
   *
   * @return how far in the memory this instruction reaches
   */
  public long largestOffset() {
    return 0;
  }

  public final long upfrontGasCost() {
    return staticGas()
        + expGas()
        + memoryExpansion()
        + accountAccess()
        + accountCreation()
        + transferValue()
        + linearPerWord()
        + linearPerByte()
        + storageWarmth()
        + sStoreValue()
        + deploymentCost();
  }

  public final long paidOutOfPocket() {
    return rawStipend();
  }

  public final long childContextAllowance() {
    return rawStipend() + extraStipend();
  }
}
