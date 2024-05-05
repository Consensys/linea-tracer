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

package net.consensys.linea.zktracer.module.shakiradata;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import lombok.Builder;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

@Builder
@Accessors(fluent = true)
public class ShakiraTraceHelper {
  private final int currentId;
  private final int startPhaseIndex;
  private final int endPhaseIndex;
  private final Bytes dataPhaseInputData = Bytes.EMPTY;

  private final Map<Integer, PhaseInfo> phaseInfoMap;
  private final Trace trace;
  private final long stamp;
  private final BiConsumer<Integer, Integer> traceLimbConsumer;

  void trace() {
    Map<Integer, Boolean> phaseFlags =
        new HashMap<>(
            Map.of(
                Trace.PHASE_SHA2_DATA, false,
                Trace.PHASE_SHA2_RESULT, false,
                Trace.PHASE_RIPEMD_DATA, false,
                Trace.PHASE_RIPEMD_RESULT, false,
                Trace.PHASE_KECCAK_DATA, false,
                Trace.PHASE_KECCAK_RESULT, false));

    boolean isExtra = false;

    for (int phaseIndex = startPhaseIndex; phaseIndex <= endPhaseIndex; phaseIndex++) {
      if (phaseIndex == 0) {
        isExtra = true;
      } else {
        phaseFlags.put(phaseIndex, true);
      }

      final PhaseInfo phaseInfo = phaseInfoMap.get(phaseIndex);

      final int indexMax = phaseInfo.indexMax();
      for (int rowIndex = 0; rowIndex <= indexMax; rowIndex++) {
        trace
            .phase(UnsignedByte.of(phaseInfo.id()))
            .deltaByte(phaseInfo.deltaBytes()[rowIndex])
            .id(currentId)
            .index(rowIndex)
            .indexMax(indexMax)
            .totalSize(phaseInfo.totalSize())
            .ripshaStamp(stamp);

        traceLimbConsumer.accept(rowIndex, phaseIndex);

        trace
            .isKeccakData(phaseFlags.get(Trace.PHASE_KECCAK_DATA))
            .isKeccakResult(phaseFlags.get(Trace.PHASE_KECCAK_RESULT))
            .isSha2Data(phaseFlags.get(Trace.PHASE_SHA2_DATA))
            .isSha2Result(phaseFlags.get(Trace.PHASE_SHA2_RESULT))
            .isRipemdData(phaseFlags.get(Trace.PHASE_RIPEMD_DATA))
            .isRipemdResult(phaseFlags.get(Trace.PHASE_RIPEMD_RESULT))
            .isExtra(isExtra);

        if (isExtra) {
          trace.fillAndValidateRow();
        } else {
          trace.validateRow();
        }
      }

      if (!isExtra) {
        phaseFlags.put(phaseIndex, false);
      }
    }
  }
}
