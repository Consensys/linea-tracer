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
package net.consensys.linea.zktracer.instructionprocessing.callTests.prc.ecpairing;

import static com.google.common.base.Preconditions.checkState;
import static net.consensys.linea.zktracer.instructionprocessing.callTests.prc.ecadd.MemoryContents.RND;
import static net.consensys.linea.zktracer.instructionprocessing.callTests.prc.ecadd.MemoryContents.WORD_HEX_SIZE;
import static net.consensys.linea.zktracer.instructionprocessing.callTests.prc.ecpairing.MemoryContents.C1_POINT_4;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;

public enum SmallPointCandidate {
  // valid points
  INFINITY,
  VALID_SMALL_POINT,
  // invalid points
  X_NOT_IN_FIELD,
  Y_NOT_IN_FIELD,
  NOT_ON_CURVE,
  RAND;

  public boolean isValid() {
    return this == INFINITY || this == VALID_SMALL_POINT;
  }

  public boolean isInfinity() {
    return this == INFINITY;
  }

  public String hexString() {
    final String result =
        switch (this) {
          case INFINITY -> SMALL_POINT_AT_INFINITY;
          case VALID_SMALL_POINT -> C1_POINT_4;
          case X_NOT_IN_FIELD -> IN_RANGE_SAVE_FOR_LARGE_X;
          case Y_NOT_IN_FIELD -> IN_RANGE_SAVE_FOR_LARGE_Y;
          case NOT_ON_CURVE -> IN_RANGE_BUT_NOT_ON_CURVE;
          case RAND -> RND.substring(147, 147 + 2 * WORD_HEX_SIZE);
          default -> throw new IllegalArgumentException("Invalid point candidate");
        };
    checkState(result.length() == 2 * WORD_HEX_SIZE, "Invalid hex string length");

    return result;
  }

  // All values below are obtained from Ivo's comment
  // https://github.com/Consensys/linea-tracer/issues/822#issuecomment-2260511164

  public static final String SMALL_POINT_AT_INFINITY = "00".repeat(2 * WORD_SIZE);
  public static final String IN_RANGE_SAVE_FOR_LARGE_X =
      "b3a1cca4c86cdc017597bb5e39705666199eccabc367f8c6aa5e713921c0886e"
          + "2018c3b9c5993f6e66a53edf1524775bd337cd82931b44760bdb4e0f557fcf55";
  public static final String IN_RANGE_SAVE_FOR_LARGE_Y =
      "29c5b47fbe82856ac08cfc5e72ee76f9ca909a4360ceafdbb058792b4dea2380"
          + "ced846e53d3564f9d059532c94207b64cba68393988bb0bd26711c828f68cd64";
  public static final String IN_RANGE_BUT_NOT_ON_CURVE =
      "1dac6eed25388228c5542dfc2c0b30bd5afbb4fb091791052ad8ca6a8e1c94fb"
          + "0b35150d9fe2b8cff4e81fd7445b6d529f68ca798d16618adf0f1d319e772987";
}
