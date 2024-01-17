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

package net.consensys.linea.zktracer.module.modexpdata;

import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;
import static net.consensys.linea.zktracer.types.Utils.leftPadTo;

import java.math.BigInteger;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Accessors(fluent = true)
public class ModexpDataOperation extends ModuleOperation {
  private static final int BEMR_LINE_COUNT = 32 * 4;
  private static final int COMPONENT_SIZE = 512;

  private final Hub hub;

  private final Bytes base;
  private final Bytes exp;
  private final Bytes mod;
  private Bytes result;

  @Override
  protected int computeLineCount() {
    return BEMR_LINE_COUNT;
  }

  public void computeResult() {
    BigInteger baseBigInt = base.toUnsignedBigInteger();
    BigInteger expBigInt = exp.toUnsignedBigInteger();
    BigInteger modBigInt = mod.toUnsignedBigInteger();

    result = bigIntegerToBytes(baseBigInt.modPow(expBigInt, modBigInt));
  }

  void trace(Trace trace, int stamp) {
    computeResult();
    final Bytes stampBytes = Bytes.ofUnsignedInt(stamp);
    final Bytes hubStamp = Bytes.ofUnsignedInt(hub.stamp() + 1);

    final Bytes basePadded = leftPadTo(base, COMPONENT_SIZE);
    final Bytes expPadded = leftPadTo(exp, COMPONENT_SIZE);
    final Bytes modPadded = leftPadTo(mod, COMPONENT_SIZE);
    final Bytes resultPadded = leftPadTo(result, COMPONENT_SIZE);
    final Bytes bemrLimb = Bytes.concatenate(basePadded, expPadded, modPadded, resultPadded);

    for (int bemr = 1; bemr <= 4; bemr++) {
      for (int index = 0; index < 32; index++) {
        int counter = 32 * (bemr - 1) + index;

        trace
            .ct(UnsignedByte.of(counter))
            .bemr(Bytes.ofUnsignedInt(bemr))
            //          .bytes()
            .limb(bemrLimb.slice(16 * counter, 16))
            .index(UnsignedByte.of(index))
            .rdcn(hubStamp)
            .stamp(stampBytes)
            .validateRow();
      }
    }
  }
}
