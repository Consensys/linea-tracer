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

package net.consensys.linea.zktracer.module.hub;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.hyperledger.besu.datatypes.AccessListEntry;
import org.junit.jupiter.params.provider.Arguments;

public class AddressColisionWarmingAndDeploymentTests {

  private static final List<List<AccessListEntry>> ACCESS_LISTS = new ArrayList<>();

  private static Stream<Arguments> inputs() {
    final List<Arguments> arguments = new ArrayList<>();

    for (int skip = 0; skip <= 1; skip++) {
      for (AddressCollisions collision : AddressCollisions.values()) {
        for (int isDeployment = 0; isDeployment <= 1; isDeployment++) {
          for (List<AccessListEntry> accessList : ACCESS_LISTS) {
            arguments.add(Arguments.of(skip == 1, collision, isDeployment == 1, accessList));
          }
        }
      }
    }

    return arguments.stream();
  }
}
