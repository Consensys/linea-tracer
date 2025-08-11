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

package net.consensys.linea.zktracer.cancunTests;

import net.consensys.linea.reporting.TestInfoWithChainConfig;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.consensys.linea.zktracer.MultiBlockUtils.multiBlocksTest;

public class SystemTransactionTests {
    public static TestInfoWithChainConfig testInfo = new TestInfoWithChainConfig();

    /**
     * This complete test is performing, for both EIP-2935 and 4788:
     * - one empty block (where system accounts are not yet deployed)
     * - one block where we call the system accounts (and system accounts are not yet deployed)
     * - one block where the system accounts are deployed
     * - one empty block (where system accounts exist)
     * - one block where we call the system accounts (and system accounts exist)
     * */
    @Test
    void systemTransactionTest() {

    }
}
