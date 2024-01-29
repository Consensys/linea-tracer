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

package net.consensys.linea.zktracer.module.exp;

import lombok.Getter;
import net.consensys.linea.zktracer.container.ModuleOperation;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.frame.MessageFrame;

@Getter
public class ExpChunk extends ModuleOperation {

  public ExpChunk(final MessageFrame frame) {}

  @Override
  protected int computeLineCount() {
    return 0; // maxCt
  }

  final void trace(int stamp, Trace trace) {
    for (int i = 0; i < 0; i++) { // TODO
      trace.stamp(Bytes.ofUnsignedLong(stamp)).validateRow();
    }
  }
}
