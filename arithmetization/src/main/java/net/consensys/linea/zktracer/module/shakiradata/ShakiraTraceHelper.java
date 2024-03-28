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

import java.util.Map;
import java.util.function.BiConsumer;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.commons.lang3.function.TriFunction;

@Builder
@Accessors(fluent = true)
public class ShakiraTraceHelper {
  private static final int EMPTY_EXTRA_LIMB_SIZE = -1;
  private final int currentHubStamp;
  @Getter private int prevHubStamp;
  private final int startPhaseIndex;
  private final int endPhaseIndex;
  private final int extraLimbSize;

  private final Map<Integer, PhaseInfo> phaseInfoMap;
  private final Trace trace;
  private final long stamp;
  private final BiConsumer<Integer, Integer> traceLimbConsumer;
  private final TriFunction<PhaseInfo, Integer, Integer, Integer> currentRowIndexFunction;
  private UnsignedByte[] hubStampDiffBytes;

  void trace() {
    boolean[] phaseFlags = new boolean[6];

    boolean isExtra = false;

    for (int phaseIndex = startPhaseIndex; phaseIndex <= endPhaseIndex; phaseIndex++) {
      if (phaseIndex == 0) {
        isExtra = true;
      } else {
        phaseFlags[phaseIndex - phaseFlags.length - 1] = true;
      }

      final PhaseInfo phaseInfo = phaseInfoMap.get(phaseIndex);

      final int indexMax = phaseInfo.indexMax();
      for (int index = 0; index <= indexMax; index++) {
        int rowIndex = currentRowIndexFunction.apply(phaseInfo, phaseIndex, index);

        trace
            .phase(UnsignedByte.of(phaseInfo.id()))
            .deltaByte(hubStampDiffBytes[rowIndex])
            .id(currentHubStamp)
            .index(index)
            .indexMax(indexMax)
            .totalSize(
                extraLimbSize == EMPTY_EXTRA_LIMB_SIZE ? phaseInfo.limbSize() : extraLimbSize)
            .ripshaStamp(stamp);

        traceLimbConsumer.accept(rowIndex, phaseIndex);

        trace
            .isKeccakData(phaseFlags[0])
            .isKeccakResult(phaseFlags[1])
            .isSha2Data(phaseFlags[2])
            .isSha2Result(phaseFlags[3])
            .isRipemdData(phaseFlags[4])
            .isRipemdResult(phaseFlags[5])
            .isExtra(isExtra)
            .validateRow();
      }

      if (!isExtra) {
        phaseFlags[phaseIndex - phaseFlags.length - 1] = false;
      }
    }

    prevHubStamp = currentHubStamp;
  }
}
