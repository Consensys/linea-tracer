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

public interface FillCreateSection {
  void fillAccountFragment(
      final Hub hub,
      final boolean createSuccess,
      final int childRevertStamp,
      final RlpAddrSubFragment rlpAddrSubFragment,
      final AccountSnapshot oldCreatorSnapshot,
      final AccountSnapshot midCreatorSnapshot,
      final AccountSnapshot newCreatorSnapshot,
      final AccountSnapshot oldCreatedSnapshot,
      final AccountSnapshot midCreatedSnapshot,
      final AccountSnapshot newCreatedSnapshot);

  void fillReverting(
      final Hub hub,
      final boolean createSuccess,
      final int currentRevertStamp,
      final AccountSnapshot oldCreatorSnapshot,
      final AccountSnapshot midCreatorSnapshot,
      final AccountSnapshot newCreatorSnapshot,
      final AccountSnapshot oldCreatedSnapshot,
      final AccountSnapshot midCreatedSnapshot,
      final AccountSnapshot newCreatedSnapshot);

  void fillContextFragment(final ContextFragment contextFragment);
}
