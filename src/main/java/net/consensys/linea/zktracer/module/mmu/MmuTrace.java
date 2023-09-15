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

package net.consensys.linea.zktracer.module.mmu;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * WARNING: This code is generated automatically. Any modifications to this code may be overwritten
 * and could lead to unexpected behavior. Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
record MmuTrace(@JsonProperty("Trace") Trace trace) {
  static final BigInteger CALLDATACOPY = new BigInteger("55");
  static final BigInteger CALLDATALOAD = new BigInteger("53");
  static final BigInteger CODECOPY = new BigInteger("57");
  static final BigInteger EXTCODECOPY = new BigInteger("60");
  static final BigInteger ExceptionalRamToStack3To2FullFast = new BigInteger("607");
  static final BigInteger Exceptional_RamToStack_3To2Full = new BigInteger("627");
  static final BigInteger ExoToRam = new BigInteger("602");
  static final BigInteger ExoToRamSlideChunk = new BigInteger("616");
  static final BigInteger ExoToRamSlideOverlappingChunk = new BigInteger("618");
  static final BigInteger FirstFastSecondPadded = new BigInteger("625");
  static final BigInteger FirstPaddedSecondZero = new BigInteger("626");
  static final BigInteger FullExoFromTwo = new BigInteger("621");
  static final BigInteger FullStackToRam = new BigInteger("623");
  static final BigInteger KillingOne = new BigInteger("604");
  static final BigInteger LIMB_SIZE = new BigInteger("16");
  static final BigInteger LIMB_SIZE_MINUS_ONE = new BigInteger("15");
  static final BigInteger LsbFromStackToRAM = new BigInteger("624");
  static final BigInteger NA_RamToStack_1To1PaddedAndZero = new BigInteger("633");
  static final BigInteger NA_RamToStack_2To1FullAndZero = new BigInteger("631");
  static final BigInteger NA_RamToStack_2To1PaddedAndZero = new BigInteger("632");
  static final BigInteger NA_RamToStack_2To2Padded = new BigInteger("630");
  static final BigInteger NA_RamToStack_3To2Full = new BigInteger("628");
  static final BigInteger NA_RamToStack_3To2Padded = new BigInteger("629");
  static final BigInteger PaddedExoFromOne = new BigInteger("619");
  static final BigInteger PaddedExoFromTwo = new BigInteger("620");
  static final BigInteger PushOneRamToStack = new BigInteger("606");
  static final BigInteger PushTwoRamToStack = new BigInteger("605");
  static final BigInteger PushTwoStackToRam = new BigInteger("608");
  static final BigInteger RETURNDATACOPY = new BigInteger("62");
  static final BigInteger RamIsExo = new BigInteger("603");
  static final BigInteger RamLimbExcision = new BigInteger("613");
  static final BigInteger RamToRam = new BigInteger("601");
  static final BigInteger RamToRamSlideChunk = new BigInteger("614");
  static final BigInteger RamToRamSlideOverlappingChunk = new BigInteger("615");
  static final BigInteger SMALL_LIMB_SIZE = new BigInteger("4");
  static final BigInteger SMALL_LIMB_SIZE_MINUS_ONE = new BigInteger("3");
  static final BigInteger StoreXInAThreeRequired = new BigInteger("609");
  static final BigInteger StoreXInB = new BigInteger("610");
  static final BigInteger StoreXInC = new BigInteger("611");
  static final BigInteger tern0 = new BigInteger("0");
  static final BigInteger tern1 = new BigInteger("1");
  static final BigInteger tern2 = new BigInteger("2");
  static final BigInteger type1 = new BigInteger("100");
  static final BigInteger type2 = new BigInteger("200");
  static final BigInteger type3 = new BigInteger("300");
  static final BigInteger type4CC = new BigInteger("401");
  static final BigInteger type4CD = new BigInteger("402");
  static final BigInteger type4RD = new BigInteger("403");
  static final BigInteger type5 = new BigInteger("500");
}
