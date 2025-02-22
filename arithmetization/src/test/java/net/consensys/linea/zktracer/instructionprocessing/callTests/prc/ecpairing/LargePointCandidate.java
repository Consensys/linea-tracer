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

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;

public enum LargePointCandidate {
  // valid points
  INFINITY,
  VALID_LARGE_POINT,
  // invalid points
  RAND,
  RE_X_NOT_IN_FIELD,
  IM_X_NOT_IN_FIELD,
  RE_Y_NOT_IN_FIELD,
  IM_Y_NOT_IN_FIELD,
  NOT_ON_CURVE,
  NOT_IN_SUBGROUP;

  public boolean isValid() {
    return this == INFINITY || this == VALID_LARGE_POINT;
  }

  public boolean isInfinity() {
    return this == INFINITY;
  }

  public static String LARGE_POINT_AT_INFINITY = "00".repeat(4 * WORD_SIZE);

  public String hexString() {
    switch (this) {
        case INFINITY:
            return LARGE_POINT_AT_INFINITY;
        case VALID_LARGE_POINT:
            return "0x1" + "00".repeat(4 * WORD_SIZE - 1);
        case RAND:
            return "0x1" + "00".repeat(4 * WORD_SIZE - 1);
        case RE_X_NOT_IN_FIELD:
            return "0x1" + "00".repeat(4 * WORD_SIZE - 1);
        case IM_X_NOT_IN_FIELD:
            return "0x1" + "00".repeat(4 * WORD_SIZE - 1);
        case RE_Y_NOT_IN_FIELD:
            return "0x1" + "00".repeat(4 * WORD_SIZE - 1);
        case IM_Y_NOT_IN_FIELD:
            return "0x1" + "00".repeat(4 * WORD_SIZE - 1);
        case NOT_ON_CURVE:
            return "0x1" + "00".repeat(4 * WORD_SIZE - 1);
        case NOT_IN_SUBGROUP:
            return "0x1" + "00".repeat(4 * WORD_SIZE - 1);
        default:
            throw new RuntimeException("Invalid point candidate");
    }
  }
}
