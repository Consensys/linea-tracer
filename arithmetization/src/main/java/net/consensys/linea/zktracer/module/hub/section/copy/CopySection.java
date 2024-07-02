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

package net.consensys.linea.zktracer.module.hub.section.copy;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.opcode.OpCode;

public class CopySection extends TraceSection {

  // TODO: this constructor is not really necessary
  public CopySection(Hub hub) {
    super(hub);
  }

  /*
  public CopySection(Hub hub, TraceFragment... chunks) {
    super(hub);
    this.addFragmentsAndStack(hub, chunks);
  }
   */

  public static void appendToTrace(Hub hub) {
    switch (hub.opCode()) {
      case OpCode.CALLDATACOPY -> new CallDataCopySection(hub);
      case OpCode.RETURNDATACOPY -> new ReturnDataCopySection(hub);
      case OpCode.CODECOPY -> new CodeCopySection(hub);
      case OpCode.EXTCODECOPY -> new ExtCodeCopySection(hub);
    }
  }
}
