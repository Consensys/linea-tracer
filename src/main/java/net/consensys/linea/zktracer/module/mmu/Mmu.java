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

import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.opcode.OpCode;

public class Mmu implements Module {
  private Module mmio;
  private OpCode opCode;
  private int ramStamp;
  private int microStamp;
  private boolean isMicro;

  private final Trace.TraceBuilder trace = Trace.builder();

  @Override
  public String jsonKey() {
    return "mmu";
  }

  @Override
  public int lineCount() {
    return 0;
  }

  @Override
  public Object commit() {

    return new MmuTrace(trace.build());
  }
}
