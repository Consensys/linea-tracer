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

package net.consensys.linea.zktracer.module.mmio;

import java.util.Map;

import net.consensys.linea.zktracer.module.mmio.dispatchers.MmioDispatcher;
import net.consensys.linea.zktracer.module.romLex.RomLex;
import net.consensys.linea.zktracer.runtime.microdata.MicroData;

class MmioDataProcessor {
  private final MicroData microData;
  private final Dispatchers dispatchers;

  MmioDataProcessor(
      final RomLex romLex, final MicroData microData, final CallStackReader callStackReader) {
    this.microData = microData;
    this.dispatchers = new Dispatchers(microData, callStackReader, romLex);
  }

  MmioData dispatchMmioData() {
    for (Map.Entry<Integer, MmioDispatcher> e : dispatchers.typeMap().entrySet()) {
      if (e.getKey().equals(microData.microOp())) {
        return e.getValue().dispatch();
      }
    }

    throw new RuntimeException(
        "Micro instruction not supported: %s".formatted(microData.microOp()));
  }

  void updateMmioData(MmioData mmioData, int counter) {
    for (Map.Entry<Integer, MmioDispatcher> e : dispatchers.typeMap().entrySet()) {
      if (e.getKey().equals(microData.microOp())) {
        e.getValue().update(mmioData, counter);
      }
    }
  }

  static int maxCounter(final MicroData microData) {
    return microData.isFast() ? 0 : 15;
  }
}
