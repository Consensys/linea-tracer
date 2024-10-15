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

package net.consensys.linea.zktracer.module.gas;

import java.math.BigInteger;

public record GasParameters(
    int ctMax, // TODO @Lorenzo this shouldn't be in gasParameters, because it shouldn't be in the
    // EqualAndHAsh, it's a consequence of oogx and xahoy
    BigInteger gasActual,
    BigInteger gasCost,
    boolean xahoy,
    boolean oogx) {

  public int compareTo(GasParameters other) {
    if (oogx() != other.oogx()) {
      return oogx() ? 1 : -1;
    }
    if (xahoy() != other.xahoy()) {
      return xahoy() ? 1 : -1;
    }
    final int gasActualComparison = gasActual().compareTo(other.gasActual());
    if (gasActualComparison != 0) {
      return gasActualComparison;
    }
    return gasCost().compareTo(other.gasCost());
  }
}
