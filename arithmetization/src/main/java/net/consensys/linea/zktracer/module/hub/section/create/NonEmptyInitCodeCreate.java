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

package net.consensys.linea.zktracer.module.hub.section.create;

import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.RlpAddrSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;

public class NonEmptyInitCodeCreate extends TraceSection implements FillCreateSection {
  public NonEmptyInitCodeCreate(
      final Hub hub, final ContextFragment commonContext, final ImcFragment imcFragment) {
    super(hub, (short) 11);
    hub.addTraceSection(this);
  }

  @Override
  public void fillAccountFragment(
      Hub hub,
      RlpAddrSubFragment rlpAddrSubFragment,
      AccountSnapshot oldCreatorSnapshot,
      AccountSnapshot midCreatorSnapshot,
      AccountSnapshot newCreatorSnapshot,
      AccountSnapshot oldCreatedSnapshot,
      AccountSnapshot midCreatedSnapshot,
      AccountSnapshot newCreatedSnapshot) {}

  @Override
  public void fillReverting(
      Hub hub,
      int childRevertStamp,
      int currentRevertStamp,
      AccountSnapshot oldCreatorSnapshot,
      AccountSnapshot midCreatorSnapshot,
      AccountSnapshot newCreatorSnapshot,
      AccountSnapshot oldCreatedSnapshot,
      AccountSnapshot midCreatedSnapshot,
      AccountSnapshot newCreatedSnapshot) {}

  @Override
  public void fillContextFragment(ContextFragment contextFragment) {}
}
