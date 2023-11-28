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

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.units.bigints.UInt256;

@Getter
@Setter
@Accessors(fluent = true)
class MmioData {
  private int cnA;
  private int cnB;
  private int cnC;

  private int indexA;
  private int indexB;
  private int indexC;
  // pointer into exo data
  private int indexX;

  private UnsignedByte[] valA;
  private UnsignedByte[] valB;
  private UnsignedByte[] valC;

  private UnsignedByte[] valANew;
  private UnsignedByte[] valBNew;
  private UnsignedByte[] valCNew;

  private EWord val;
  private UnsignedByte[] valHi;
  private UnsignedByte[] valLo;
  private UnsignedByte[] valX;

  private boolean bin1;
  private boolean bin2;
  private boolean bin3;
  private boolean bin4;
  private boolean bin5;

  private UInt256 pow2561;
  private UInt256 pow2562;

  private UInt256 acc1;
  private UInt256 acc2;
  private UInt256 acc3;
  private UInt256 acc4;
  private UInt256 acc5;
  private UInt256 acc6;

  public void updateLimbsInMemory(final CallStack callStack) {}
}
