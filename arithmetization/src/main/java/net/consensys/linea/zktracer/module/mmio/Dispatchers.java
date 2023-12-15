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
import net.consensys.linea.zktracer.module.mmio.dispatchers.ExceptionalRamToStack3To2FullDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.ExceptionalRamToStack3To2FullFastDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.ExoToRamDispatcher;
import net.consensys.linea.zktracer.module.mmio.dispatchers.ExoToRamSlideChunkDispatcher;
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
import net.consensys.linea.zktracer.module.mmu.MicroData;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;

public class Dispatchers {
  @Getter private final Map<Integer, MmioDispatcher> typeMap;

  public Dispatchers(MicroData microData, CallStack callStack) {
    this.typeMap =
        Map.ofEntries(
            ///////////////////////////
            //					   //
            //     TRANSPLANTS	   //
            //					   //
            ///////////////////////////
            entry(Trace.RamToRam, new RamToRamDispatcher(microData, callStack)),
            entry(Trace.ExoToRam, new ExoToRamDispatcher(microData, callStack)),
            entry(Trace.RamIsExo, new RamIsExoDispatcher(microData, callStack)),
            entry(Trace.KillingOne, new KillingOneDispatcher(microData, callStack)),
            entry(Trace.PushTwoRamToStack, new PushTwoRamToStackDispatcher(microData, callStack)),
            entry(Trace.PushOneRamToStack, new PushOneRamToStackDispatcher(microData, callStack)),
            entry(
                Trace.ExceptionalRamToStack3To2FullFast,
                new ExceptionalRamToStack3To2FullFastDispatcher(microData, callStack)),
            entry(Trace.PushTwoStackToRam, new PushTwoStackToRamDispatcher(microData, callStack)),
            entry(
                Trace.StoreXInAThreeRequired,
                new StoreXInAThreeRequiredDispatcher(microData, callStack)),
            entry(Trace.StoreXInB, new StoreXInBDispatcher(microData, callStack)),
            entry(Trace.StoreXInC, new StoreXInCDispatcher(microData, callStack)),
            /////////////////////////
            //					 //
            //	  SURGERIES      //
            //					 //
            /////////////////////////
            // 6.3 RAM to RAM
            /////////////////
            entry(Trace.RamLimbExcision, new RamLimbExcisionDispatcher(microData, callStack)),
            entry(Trace.RamToRamSlideChunk, new RamToRamSlideChunkDispatcher(microData, callStack)),
            entry(
                Trace.RamToRamSlideOverlappingChunk,
                new RamToRamSlideOverlappingChunkDispatcher(microData, callStack)),
            // 6.4 Exo to RAM
            /////////////////
            entry(Trace.ExoToRamSlideChunk, new ExoToRamSlideChunkDispatcher(microData, callStack)),
            entry(
                Trace.ExoToRamSlideOverlappingChunk,
                new ExoToRamSlideChunkDispatcher(microData, callStack)),
            // 6.5 RAM to Exo
            /////////////////
            entry(Trace.PaddedExoFromOne, new PaddedExoFromOneDispatcher(microData, callStack)),
            entry(Trace.PaddedExoFromTwo, new PaddedExoFromTwoDispatcher(microData, callStack)),
            entry(Trace.FullExoFromTwo, new FullExoFromTwoDispatcher(microData, callStack)),
            // 6.6 Stack to RAM
            ///////////////////
            entry(Trace.FullStackToRam, new FullStackToRamDispatcher(microData, callStack)),
            entry(Trace.LsbFromStackToRAM, new LsbFromStackToRamDispatcher(microData, callStack)),
            // 6.7 RAM to Stack: aligned offsets
            ////////////////////////////////////
            entry(
                Trace.FirstFastSecondPadded,
                new FirstFastSecondPaddedDispatcher(microData, callStack)),
            entry(
                Trace.FirstPaddedSecondZero,
                new FirstPaddedSecondZeroDispatcher(microData, callStack)),
            // 6.8 RAM to Stack: non-aligned offsets
            ////////////////////////////////////////
            entry(
                Trace.Exceptional_RamToStack_3To2Full,
                new ExceptionalRamToStack3To2FullDispatcher(microData, callStack)),
            entry(
                Trace.NA_RamToStack_3To2Full,
                new NaRamToStack3To2FullDispatcher(microData, callStack)),
            entry(
                Trace.NA_RamToStack_3To2Padded,
                new NaRamToStack3To2PaddedDispatcher(microData, callStack)),
            entry(
                Trace.NA_RamToStack_2To2Padded,
                new NaRamToStack2To2PaddedDispatcher(microData, callStack)),
            entry(
                Trace.NA_RamToStack_2To1FullAndZero,
                new NaRamToStack2To1FullAndZeroDispatcher(microData, callStack)),
            entry(
                Trace.NA_RamToStack_2To1PaddedAndZero,
                new NaRamToStack2To1PaddedAndZeroDispatcher(microData, callStack)),
            entry(
                Trace.NA_RamToStack_1To1PaddedAndZero,
                new NaRamToStack1To1PaddedAndZeroDispatcher(microData, callStack)));
  }
}
