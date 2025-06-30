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

package net.consensys.linea.zktracer.module.rlpUtils;

import java.util.List;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.container.module.Module;
import net.consensys.linea.zktracer.module.wcp.Wcp;

@RequiredArgsConstructor
public class RlpUtils implements Module {
  private final Wcp wcp;

  @Override
  public String moduleKey() {
    return "RLP_UTILS";
  }

  @Override
  public void popTransactionBundle() {}

  @Override
  public void commitTransactionBundle() {}

  @Override
  public int lineCount() {
    return 0;
  }

  @Override
  public int spillage(Trace trace) {
    return 0;
  }

  @Override
  public List<Trace.ColumnHeader> columnHeaders(Trace trace) {
    return List.of();
  }

  @Override
  public void commit(Trace trace) {
    Module.super.commit(trace);
  }
}
