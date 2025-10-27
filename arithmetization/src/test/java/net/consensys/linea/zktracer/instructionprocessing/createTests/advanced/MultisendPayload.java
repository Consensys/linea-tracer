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
package net.consensys.linea.zktracer.instructionprocessing.createTests.advanced;

import java.util.Arrays;
import java.util.Collections;

import net.consensys.linea.testing.generated.MultiSendCallOnly;
import org.apache.tuweni.bytes.Bytes;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;

public class MultisendPayload {

  public static Bytes multiSend(String payload) {
    final Function function =
        new Function(
            MultiSendCallOnly.FUNC_MULTISEND,
            Arrays.<Type>asList(
                new org.web3j.abi.datatypes.DynamicBytes(Bytes.fromHexString(payload).toArray())),
            Collections.<TypeReference<?>>emptyList());
    return Bytes.fromHexStringLenient(FunctionEncoder.encode(function));
  }
}
