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

package net.consensys.linea.rpc;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;

public class Converters {

  /**
   * Converts an object to a {@link Map} of {@link String} key and {@link Object} values.
   *
   * @param object object to be converted.
   * @return map containing the data of the converted object.
   */
  public static Map<String, Object> objectToMap(final Object object) {
    final Map<String, Object> map = new HashMap<>();
    final BeanMap beanMap = new BeanMap(object);
    for (Object key : beanMap.keySet()) {
      map.put(key.toString(), beanMap.get(key));
    }

    return map;
  }
}
