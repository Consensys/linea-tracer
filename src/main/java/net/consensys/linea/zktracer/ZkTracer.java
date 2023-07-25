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

package net.consensys.linea.zktracer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.alu.add.Add;
import net.consensys.linea.zktracer.module.alu.mod.Mod;
import net.consensys.linea.zktracer.module.alu.mul.Mul;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.shf.Shf;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.plugin.services.tracer.BlockAwareOperationTracer;

public class ZkTracer implements BlockAwareOperationTracer {
  private final List<Module> tracers;
  private final Map<OpCode, List<Module>> opCodeTracerMap = new HashMap<>();

  private final ZkTraceBuilder zkTraceBuilder;

  public ZkTracer(final ZkTraceBuilder zkTraceBuilder, final List<Module> tracers) {
    this.tracers = tracers;
    this.zkTraceBuilder = zkTraceBuilder;
    setupTracers();
  }

  public ZkTracer(final ZkTraceBuilder zkTraceBuilder) {
    this(
        zkTraceBuilder,
        List.of(
            new Hub(),
            new Mul(),
            new Shf(),
            new Wcp(),
            new Add(),
            new Mod()));
  }

  @Override
  public void tracePreExecution(final MessageFrame frame) {
    for (Module tracer :
        opCodeTracerMap.get(OpCode.of(frame.getCurrentOperation().getOpcode()))) {
      if (tracer != null) {
        zkTraceBuilder.addTrace(tracer.jsonKey(), tracer.trace(frame));
      }
    }
  }

  private void setupTracers() {
    for (Module tracer : tracers) {
      for (OpCode opCode : tracer.supportedOpCodes()) {
        List<Module> modules = opCodeTracerMap.get(opCode);
        if (modules == null) {
          modules = List.of(tracer);
        } else {
          modules.add(tracer);
        }

        opCodeTracerMap.put(opCode, modules);
      }
    }
  }
}
