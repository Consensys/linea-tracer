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

import static java.util.Map.entry;

import java.util.Map;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.mmio.dispatchers.ExceptionalRamToStack3To2FullDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.ExceptionalRamToStack3To2FullFastDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.ExoToRamDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.ExoToRamSlideOverlappingChunkDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.FirstFastSecondPaddedDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.FirstPaddedSecondZeroDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.FullExoFromTwoDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.FullStackToRamDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.KillingOneDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.LsbFromStackToRamDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.MmioDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.NaRamToStack1To1PaddedAndZeroDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.NaRamToStack2To1FullAndZeroDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.NaRamToStack2To1PaddedAndZeroDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.NaRamToStack2To2PaddedDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.NaRamToStack3To2FullDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.NaRamToStack3To2PaddedDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.PaddedExoFromOneDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.PaddedExoFromTwoDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.PushOneRamToStackDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.PushTwoRamToStackDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.PushTwoStackToRamDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.RamIsExoDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.RamLimbExcisionDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.RamToRamDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.RamToRamSlideChunkDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.RamToRamSlideOverlappingChunkDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.StoreXInAThreeRequiredDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.StoreXInBDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.StoreXInCDispatcher;
import net.consensys.linea.zktracer.module.mmu.MmuData;
import net.consensys.linea.zktracer.module.romLex.RomLex;

@Accessors(fluent = true)
public class Dispatchers {
  @Getter private final Map<Integer, MmioDispatcher> typeMap;

  public Dispatchers(MmuData microData, CallStackReader callStackReader, RomLex romLex) {
    this.typeMap =
        Map.ofEntries(
            ///////////////////////////
            //					     //
            //      TRANSPLANTS	     //
            //					     //
            ///////////////////////////
            entry(Trace.RamToRam, new RamToRamDispatcher(microData, callStackReader)),
            entry(Trace.ExoToRam, new ExoToRamDispatcher(microData, callStackReader, romLex)),
            entry(Trace.RamIsExo, new RamIsExoDispatcher(microData, callStackReader)),
            entry(Trace.KillingOne, new KillingOneDispatcher(microData, callStackReader)),
            entry(
                Trace.PushTwoRamToStack,
                new PushTwoRamToStackDispatcher(microData, callStackReader)),
            entry(
                Trace.PushOneRamToStack,
                new PushOneRamToStackDispatcher(microData, callStackReader)),
            entry(
                Trace.ExceptionalRamToStack3To2FullFast,
                new ExceptionalRamToStack3To2FullFastDispatcher(microData)),
            entry(
                Trace.PushTwoStackToRam,
                new PushTwoStackToRamDispatcher(microData, callStackReader)),
            entry(
                Trace.StoreXInAThreeRequired,
                new StoreXInAThreeRequiredDispatcher(microData, callStackReader, romLex)),
            entry(Trace.StoreXInB, new StoreXInBDispatcher(microData, callStackReader)),
            entry(Trace.StoreXInC, new StoreXInCDispatcher(microData, callStackReader)),
            /////////////////////////
            //					   //
            //	    SURGERIES      //
            //					   //
            /////////////////////////
            // 6.3 RAM to RAM
            /////////////////
            entry(Trace.RamLimbExcision, new RamLimbExcisionDispatcher(microData, callStackReader)),
            entry(
                Trace.RamToRamSlideChunk,
                new RamToRamSlideChunkDispatcher(microData, callStackReader)),
            entry(
                Trace.RamToRamSlideOverlappingChunk,
                new RamToRamSlideOverlappingChunkDispatcher(microData, callStackReader)),
            // 6.4 Exo to RAM
            /////////////////
            entry(
                Trace.ExoToRamSlideChunk,
                new ExoToRamSlideOverlappingChunkDispatcher(microData, callStackReader, romLex)),
            entry(
                Trace.ExoToRamSlideOverlappingChunk,
                new ExoToRamSlideOverlappingChunkDispatcher(microData, callStackReader, romLex)),
            // 6.5 RAM to Exo
            /////////////////
            entry(
                Trace.PaddedExoFromOne, new PaddedExoFromOneDispatcher(microData, callStackReader)),
            entry(
                Trace.PaddedExoFromTwo, new PaddedExoFromTwoDispatcher(microData, callStackReader)),
            entry(Trace.FullExoFromTwo, new FullExoFromTwoDispatcher(microData, callStackReader)),
            // 6.6 Stack to RAM
            ///////////////////
            entry(Trace.FullStackToRam, new FullStackToRamDispatcher(microData, callStackReader)),
            entry(
                Trace.LsbFromStackToRAM,
                new LsbFromStackToRamDispatcher(microData, callStackReader)),
            // 6.7 RAM to Stack: aligned offsets
            ////////////////////////////////////
            entry(
                Trace.FirstFastSecondPadded,
                new FirstFastSecondPaddedDispatcher(microData, callStackReader)),
            entry(
                Trace.FirstPaddedSecondZero,
                new FirstPaddedSecondZeroDispatcher(microData, callStackReader)),
            // 6.8 RAM to Stack: non-aligned offsets
            ////////////////////////////////////////
            entry(
                Trace.Exceptional_RamToStack_3To2Full,
                new ExceptionalRamToStack3To2FullDispatcher(microData)),
            entry(
                Trace.NA_RamToStack_3To2Full,
                new NaRamToStack3To2FullDispatcher(microData, callStackReader)),
            entry(
                Trace.NA_RamToStack_3To2Padded,
                new NaRamToStack3To2PaddedDispatcher(microData, callStackReader)),
            entry(
                Trace.NA_RamToStack_2To2Padded,
                new NaRamToStack2To2PaddedDispatcher(microData, callStackReader)),
            entry(
                Trace.NA_RamToStack_2To1FullAndZero,
                new NaRamToStack2To1FullAndZeroDispatcher(microData, callStackReader)),
            entry(
                Trace.NA_RamToStack_2To1PaddedAndZero,
                new NaRamToStack2To1PaddedAndZeroDispatcher(microData, callStackReader)),
            entry(
                Trace.NA_RamToStack_1To1PaddedAndZero,
                new NaRamToStack1To1PaddedAndZeroDispatcher(microData, callStackReader)));
  }
}
