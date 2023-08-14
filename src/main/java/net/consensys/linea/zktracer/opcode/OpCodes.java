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

package net.consensys.linea.zktracer.opcode;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;

public class OpCodes {
  private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());

  private static Map<Long, OpCodeData> valueToOpCodeDataMap;
  private static Map<OpCode, OpCodeData> opCodeToOpCodeDataMap;

  @SneakyThrows(IOException.class)
  public static void load() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    JsonNode rootNode =
        MAPPER.readTree(classLoader.getResourceAsStream("opcodes.yml")).get("opcodes");

    CollectionType typeReference =
        TypeFactory.defaultInstance().constructCollectionType(List.class, OpCodeData.class);

    List<OpCodeData> opCodes = MAPPER.treeToValue(rootNode, typeReference);

    valueToOpCodeDataMap = opCodes.stream().collect(Collectors.toMap(OpCodeData::value, e -> e));
    opCodeToOpCodeDataMap =
        opCodes.stream().collect(Collectors.toMap(OpCodeData::mnemonic, e -> e));
  }

  public static OpCodeData of(final long value) {
    if (value < 0 || value > 255) {
      throw new IllegalArgumentException("No OpCode with value %s is defined.".formatted(value));
    }

    return valueToOpCodeDataMap.getOrDefault(value, of(OpCode.INVALID));
  }

  public static OpCodeData of(final OpCode code) {
    return Optional.ofNullable(opCodeToOpCodeDataMap.get(code))
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "No OpCode of mnemonic %s is defined.".formatted(code)));
  }
}
