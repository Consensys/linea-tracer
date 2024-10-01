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

package net.consensys.linea.testing;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tuweni.bytes.Bytes;
import org.web3j.tx.Contract;

public class SolidityUtils {
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final ClassLoader classLoader = SolidityUtils.class.getClassLoader();

  public static Bytes getContractByteCode(Class<? extends Contract> contractClass) {
    String contractResourcePath = String.format("solidity/%s.json", contractClass.getSimpleName());
    URL contractResourceURL = classLoader.getResource(contractResourcePath);
    try {
      JsonNode jsonRoot = objectMapper.readTree(contractResourceURL);
      Iterator<Map.Entry<String, JsonNode>> contracts = jsonRoot.get("contracts").fields();
      while (contracts.hasNext()) {
        Map.Entry<String, JsonNode> contract = contracts.next();
        if (contract.getKey().contains(contractClass.getSimpleName())) {
          return Bytes.fromHexStringLenient(contract.getValue().get("bin-runtime").asText());
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    throw new RuntimeException("Could not find contract bytecode");
  }
}
