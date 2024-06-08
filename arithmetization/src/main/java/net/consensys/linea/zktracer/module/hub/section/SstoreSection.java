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

package net.consensys.linea.zktracer.module.hub.section;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;

public class SstoreSection extends TraceSection {

  public static void appendTo(Hub hub) {

    if (hub.pch().exceptions().staticFault()) {
      // static exception SSTORE section
      hub.addTraceSection(staticxSstoreSection(hub));
    } else if (hub.pch().exceptions().outOfGas()) {
      // out of gas exception SSTORE section
      hub.addTraceSection(oogxSstoreSection(hub));
    } else if (hub.callStack().current().willRevert()) {
      // reverted SSTORE section
      hub.addTraceSection(revertedSstoreSection(hub));
    } else {
      // reverted SSTORE section
      hub.addTraceSection(unrevertedSstoreSection(hub));
    }
  }

  private SstoreSection() {}

  private static SstoreSection staticxSstoreSection(Hub hub) {
    return new SstoreSection();
  }

  private static SstoreSection oogxSstoreSection(Hub hub) {
    return new SstoreSection();
  }

  private static SstoreSection revertedSstoreSection(Hub hub) {
    return new SstoreSection();
  }

  private static SstoreSection unrevertedSstoreSection(Hub hub) {
    return new SstoreSection();
  }

  public SstoreSection(Hub hub, final TraceFragment... chunks) {
    this.addFragmentsAndStack(hub, chunks);
  }

  public void addFragment(Hub hub, final TraceFragment fragment) {
    this.addFragmentsWithoutStack(hub, fragment);
  }

  @Override
  public void seal(Hub hub) {}
}
