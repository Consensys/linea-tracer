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

package net.consensys.linea.zktracer.module.mmu.values;

import java.math.BigInteger;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu.MmuCall;
import net.consensys.linea.zktracer.types.Bytes16;

@Getter
@Accessors(fluent = true)
public class HubToMmuValues {
  private int mmuInstruction;
  private int sourceId;
  private int targetId;
  private int auxId;
  private BigInteger sourceOffsetHi;
  private BigInteger sourceOffsetLo;
  private long targetOffset;
  private long size;
  private long referenceOffset;
  private long referenceSize;
  private boolean successBit;
  private Bytes16 limb1;
  private Bytes16 limb2;
  private long phase;
  private int exoSum;

  private HubToMmuValues(final MmuCall mmuCall) {
    this.mmuInstruction = mmuCall.instruction();
    this.sourceId = mmuCall.sourceId();
    this.targetId = mmuCall.targetId();
    this.auxId = mmuCall.auxId();
    this.sourceOffsetHi = mmuCall.sourceOffset().hiBigInt();
    this.sourceOffsetLo = mmuCall.sourceOffset().loBigInt();
    this.targetOffset = mmuCall.targetOffset().toLong();
    this.size = mmuCall.size();
    this.referenceOffset = mmuCall.referenceOffset();
    this.referenceSize = mmuCall.referenceSize();
    this.successBit = mmuCall.successBit();
    this.limb1 = Bytes16.wrap(mmuCall.limb1());
    this.limb2 = Bytes16.wrap(mmuCall.limb2());
    this.phase = mmuCall.phase();
    this.exoSum = mmuCall.exoSum();
  }

  public static HubToMmuValues fromMmuCall(final MmuCall mmuCall) {
    return new HubToMmuValues(mmuCall);
  }
}
