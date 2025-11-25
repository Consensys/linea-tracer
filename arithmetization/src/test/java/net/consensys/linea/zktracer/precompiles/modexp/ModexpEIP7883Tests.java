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
package net.consensys.linea.zktracer.precompiles.modexp;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.consensys.linea.reporting.TracerTestBase;
import org.apache.commons.math3.util.Pair;
import org.junit.jupiter.api.Test;

public class ModexpEIP7883Tests extends TracerTestBase {

  // See https://github.com/Consensys/linea-tracer/issues/2496
  static final List<Pair<Integer, Integer>> bbsMbsPairs =
      List.of(Pair.create(0, 0), Pair.create(0, 3), Pair.create(21, 23), Pair.create(56, 55));

  static final List<Integer> ebs = List.of(0, 1, 16, 27, 32, 39, 173);

  // Pre-computed exponents for each ebs value
  static final Map<Integer, List<BigInteger>> exponents =
      ebs.stream()
          .collect(
              Collectors.toMap(
                  ebsItem -> ebsItem,
                  ebsItem -> {
                    final int minEbs32 = Math.min(ebsItem, 32);
                    List<BigInteger> exponents = new ArrayList<>();
                    for (int z = 0; z <= 8 * minEbs32; z++) {
                      final String exponent = "0".repeat(8 * minEbs32 - z) + "1".repeat(z);
                      exponents.add(new BigInteger(exponent, 2));
                    }
                    return exponents;
                  }));

  // Support method to compute cds given bbs, ebs, mbs
  static List<Integer> cds(Integer bbs, Integer ebs, Integer mbs) {
    List<Integer> cds = new ArrayList<>();
    for (Integer extra : List.of(ebs / 2, ebs, ebs + mbs)) {
      cds.add(bbs + extra);
    }
    return cds;
  }

  // TODO: tests
  @Test
  void test() {}
}
