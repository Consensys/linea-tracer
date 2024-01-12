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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.container.ModuleOperation;
import org.apache.tuweni.bytes.Bytes;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ModexpDataOperation extends ModuleOperation {
  private final Bytes base;
  private final int baseLength;
  private final Bytes exp;
  private final int expLength;
  private final Bytes mod;
  private final int modLength;
  @Getter private Bytes result;

  @Override
  protected int computeLineCount() {
    return 32 * 4;
  }

  void trace(Trace trace, int stamp) {
    for (int ct = 0; ct < 32; ct++) {
      //      trace
      //        .ct(UnsignedByte.of(ct))
      //        .bemr()
      //        .bytes()
      //        .limb()
      //        .index()
      //        .rdcn()
      //        .stamp(stamp)
      //        .validateRow();
    }
  }
}
