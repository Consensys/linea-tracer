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

package net.consensys.linea.zktracer.opcode.gas;

public interface GasCost {
  default long staticGas() {
    return 0;
  }

  default long expGas() {
    return 0;
  }

  default long memoryExpansion() {
    return 0;
  }

  default long accountAccess() {
    return 0;
  }

  default long accountCreation() {
    return 0;
  }

  default long transferValue() {
    return 0;
  }

  default long linearPerWord() {
    return 0;
  }

  default long linearPerByte() {
    return 0;
  }

  default long storageWarmth() {
    return 0;
  }

  default long sStoreValue() {
    return 0;
  }

  default long rawStipend() {
    return 0;
  }

  default long extraStipend() {
    return 0;
  }

  default long codeReturn() {
    return 0;
  }

  default long refund() {
    return 0;
  }
}
