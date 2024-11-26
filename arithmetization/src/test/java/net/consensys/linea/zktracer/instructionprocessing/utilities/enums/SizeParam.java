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
package net.consensys.linea.zktracer.instructionprocessing.utilities.enums;

public enum SizeParam {
    // aligned
    ZERO("00"),
    SIXTEEN("10"),
    EVM_WORD("20"),
    // unaligned
    THREE("03"), // + 3
    SIXTY("3c"), // - 4
    FIFTY_FIVE("37"), // + 7
    HUGE("43565efefffeeabc54213576345234"),
    MAX("ff".repeat(32))
    ;

    private final String value;
    SizeParam(String s) {
        this.value = s;
    }
    public String value() {
        return value;
    }
}
