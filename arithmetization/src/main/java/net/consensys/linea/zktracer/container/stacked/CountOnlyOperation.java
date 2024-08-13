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

package net.consensys.linea.zktracer.container.stacked;

public class CountOnlyOperation {

  private int countSinceBeginningOfTheConflation;
  private int thisTransactionCount;

  public CountOnlyOperation() {
    this.countSinceBeginningOfTheConflation = 0;
    this.thisTransactionCount = 0;
  }

  /**
   * when we enter a transaction, the previous transaction is definitely added to the block and
   * can't be pop
   */
  public void enterTransaction() {
    countSinceBeginningOfTheConflation += thisTransactionCount;
    thisTransactionCount = 0;
  }

  public void popTransaction() {
    thisTransactionCount = 0;
  }

  public void add(final int operationCount) {
    thisTransactionCount += operationCount;
  }

  public int lineCount() {
    return countSinceBeginningOfTheConflation + thisTransactionCount;
  }
}
