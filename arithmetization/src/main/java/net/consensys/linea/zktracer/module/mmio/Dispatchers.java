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

import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.mmio.dispatchers.MmioDispatcher;
import net.consensys.linea.zktracer.module.mmu.MmuData;
import net.consensys.linea.zktracer.module.romLex.RomLex;

@Accessors(fluent = true)
public class Dispatchers {
  @Getter private final Map<Integer, MmioDispatcher> typeMap;

  public Dispatchers(MmuData mmuData, CallStackReader callStackReader, RomLex romLex) {
    this.typeMap =
        Map.ofEntries(
            ///////////////////////////
            //					     //
            //      TRANSPLANTS	     //
            //					     //
            ///////////////////////////
            //            entry(Trace.RamToRam, new RamToRamDispatcher(mmuData, callStackReader)),
            //            entry(Trace.ExoToRam, new ExoToRamDispatcher(mmuData, callStackReader,
            // romLex)),
            //            entry(Trace.RamIsExo, new RamIsExoDispatcher(mmuData, callStackReader)),
            //            entry(Trace.KillingOne, new KillingOneDispatcher(mmuData,
            // callStackReader)),
            //            entry(
            //                Trace.PushTwoRamToStack,
            //                new PushTwoRamToStackDispatcher(mmuData, callStackReader)),
            //            entry(
            //                Trace.PushOneRamToStack,
            //                new PushOneRamToStackDispatcher(mmuData, callStackReader)),
            //            entry(
            //                Trace.ExceptionalRamToStack3To2FullFast,
            //                new ExceptionalRamToStack3To2FullFastDispatcher(mmuData)),
            //            entry(
            //                Trace.PushTwoStackToRam,
            //                new PushTwoStackToRamDispatcher(mmuData, callStackReader)),
            //            entry(
            //                Trace.StoreXInAThreeRequired,
            //                new StoreXInAThreeRequiredDispatcher(mmuData, callStackReader,
            // romLex)),
            //            entry(Trace.StoreXInB, new StoreXInBDispatcher(mmuData, callStackReader)),
            //            entry(Trace.StoreXInC, new StoreXInCDispatcher(mmuData, callStackReader)),
            /////////////////////////
            //					   //
            //	    SURGERIES      //
            //					   //
            /////////////////////////
            // 6.3 RAM to RAM
            /////////////////
            //            entry(Trace.RamLimbExcision, new RamLimbExcisionDispatcher(mmuData,
            // callStackReader)),
            //            entry(
            //                Trace.RamToRamSlideChunk,
            //                new RamToRamSlideChunkDispatcher(mmuData, callStackReader)),
            //            entry(
            //                Trace.RamToRamSlideOverlappingChunk,
            //                new RamToRamSlideOverlappingChunkDispatcher(mmuData,
            // callStackReader)),
            //            // 6.4 Exo to RAM
            //            /////////////////
            //            entry(
            //                Trace.ExoToRamSlideChunk,
            //                new ExoToRamSlideOverlappingChunkDispatcher(mmuData, callStackReader,
            // romLex)),
            //            entry(
            //                Trace.ExoToRamSlideOverlappingChunk,
            //                new ExoToRamSlideOverlappingChunkDispatcher(mmuData, callStackReader,
            // romLex)),
            //            // 6.5 RAM to Exo
            //            /////////////////
            //            entry(
            //                Trace.PaddedExoFromOne, new PaddedExoFromOneDispatcher(mmuData,
            // callStackReader)),
            //            entry(
            //                Trace.PaddedExoFromTwo, new PaddedExoFromTwoDispatcher(mmuData,
            // callStackReader)),
            //            entry(Trace.FullExoFromTwo, new FullExoFromTwoDispatcher(mmuData,
            // callStackReader)),
            //            // 6.6 Stack to RAM
            //            ///////////////////
            //            entry(Trace.FullStackToRam, new FullStackToRamDispatcher(mmuData,
            // callStackReader)),
            //            entry(
            //                Trace.LsbFromStackToRAM,
            //                new LsbFromStackToRamDispatcher(mmuData, callStackReader)),
            //            // 6.7 RAM to Stack: aligned offsets
            //            ////////////////////////////////////
            //            entry(
            //                Trace.FirstFastSecondPadded,
            //                new FirstFastSecondPaddedDispatcher(mmuData, callStackReader)),
            //            entry(
            //                Trace.FirstPaddedSecondZero,
            //                new FirstPaddedSecondZeroDispatcher(mmuData, callStackReader)),
            //            // 6.8 RAM to Stack: non-aligned offsets
            //            ////////////////////////////////////////
            //            entry(
            //                Trace.Exceptional_RamToStack_3To2Full,
            //                new ExceptionalRamToStack3To2FullDispatcher(mmuData)),
            //            entry(
            //                Trace.NA_RamToStack_3To2Full,
            //                new NaRamToStack3To2FullDispatcher(mmuData, callStackReader)),
            //            entry(
            //                Trace.NA_RamToStack_3To2Padded,
            //                new NaRamToStack3To2PaddedDispatcher(mmuData, callStackReader)),
            //            entry(
            //                Trace.NA_RamToStack_2To2Padded,
            //                new NaRamToStack2To2PaddedDispatcher(mmuData, callStackReader)),
            //            entry(
            //                Trace.NA_RamToStack_2To1FullAndZero,
            //                new NaRamToStack2To1FullAndZeroDispatcher(mmuData, callStackReader)),
            //            entry(
            //                Trace.NA_RamToStack_2To1PaddedAndZero,
            //                new NaRamToStack2To1PaddedAndZeroDispatcher(mmuData,
            // callStackReader)),
            //            entry(
            //                Trace.NA_RamToStack_1To1PaddedAndZero,
            //                new NaRamToStack1To1PaddedAndZeroDispatcher(mmuData,
            // callStackReader)));
            );
  }
}
