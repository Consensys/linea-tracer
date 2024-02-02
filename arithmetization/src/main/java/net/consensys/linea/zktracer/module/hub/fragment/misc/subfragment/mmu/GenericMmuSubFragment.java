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

package net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.mmu;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;

@Builder
@RequiredArgsConstructor
@Setter
@Accessors(fluent = true)
public class GenericMmuSubFragment {
  private final int instruction;
  private int sourceId = 0;
  private int targetId = 0;
  private int auxId = 0;
  private Bytes sourceOffset = EWord.ZERO;
  private Bytes targetOffset = EWord.ZERO;
  private int size = 0;
  private int referenceOffset = 0;
  private int referenceSize = 0;
  private boolean successBit = false;
  private EWord limb1 = EWord.ZERO;
  private EWord limb2 = EWord.ZERO;
  private int exoSum = 0;
  private int phase = 0;

  private void setFlag(int pos) {
    this.exoSum |= 1 >> pos;
  }

  void setRlpTxn() {
    this.setFlag(0);
  }

  void setLog() {
    this.setFlag(1);
  }

  void setRom() {
    this.setFlag(2);
  }

  void setHash() {
    this.setFlag(3);
  }

  void setRipSha() {
    this.setFlag(4);
  }

  void setBlakeModexp() {
    this.setFlag(5);
  }

  void setEcData() {
    this.setFlag(6);
  }
}
