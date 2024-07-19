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

package net.consensys.linea.zktracer.module.oob;

public enum OobDataChannel {
  DATA_1,
  DATA_2,
  DATA_3,
  DATA_4,
  DATA_5,
  DATA_6,
  DATA_7,
  DATA_8;

  public static OobDataChannel of(int i) {
    return switch (i) {
      case 0 -> DATA_1;
      case 1 -> DATA_2;
      case 2 -> DATA_3;
      case 3 -> DATA_4;
      case 4 -> DATA_5;
      case 5 -> DATA_6;
      case 6 -> DATA_7;
      case 7 -> DATA_8;
      default -> throw new IllegalArgumentException("unknown OOB data channel: %d".formatted(i));
    };
  }
}
