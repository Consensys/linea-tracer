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

import net.consensys.linea.testing.generated.OdosRouterV2;
import org.apache.tuweni.bytes.Bytes;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;

public class OdosRouterV2Payload {

  public static Bytes swapCompact() {
    final Function function =
        new Function(OdosRouterV2.FUNC_SWAPCOMPACT, Arrays.asList(), Collections.emptyList());
    return Bytes.fromHexStringLenient(FunctionEncoder.encode(function));
  }
}
