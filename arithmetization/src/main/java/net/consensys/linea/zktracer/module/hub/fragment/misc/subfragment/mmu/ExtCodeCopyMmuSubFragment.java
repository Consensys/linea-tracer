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

package net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.mmu;

import static net.consensys.linea.zktracer.module.mmu.Trace.MMU_INST_ANY_TO_RAM_WITH_PADDING;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.types.EWord;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.internal.Words;

public class ExtCodeCopyMmuSubFragment extends GenericMmuSubFragment {
  private final Hub hub;
  private int cfi = 0;

  public ExtCodeCopyMmuSubFragment(final Hub hub) {
    super(MMU_INST_ANY_TO_RAM_WITH_PADDING);
    this.hub = hub;
    this.cfi = 0;
    final Address sourceAddress = Words.toAddress(hub.messageFrame().getStackItem(0));
    try {
      this.cfi =
          hub.romLex()
              .getCfiByMetadata(
                  sourceAddress,
                  hub.conflation().deploymentInfo().number(sourceAddress),
                  hub.conflation().deploymentInfo().isDeploying(sourceAddress));
    } catch (Exception ignored) {
      // CFI is already equal to 0 anyways
    }

    this.targetId(hub.currentFrame().contextNumber())
        .sourceOffset(EWord.of(hub.messageFrame().getStackItem(2)))
        .targetOffset(EWord.of(hub.messageFrame().getStackItem(1)))
        .size(Words.clampedToLong(hub.messageFrame().getStackItem(3)))
        .referenceSize(
            hub.romLex()
                .getChunkByMetadata(
                    sourceAddress,
                    hub.conflation().deploymentInfo().number(sourceAddress),
                    hub.conflation().deploymentInfo().isDeploying(sourceAddress))
                .map(c -> c.byteCode().size())
                .orElse(0))
        .setRom();
  }

  @Override
  protected int sourceId() {
    return this.hub.romLex().getSortedCfiByCfi(this.cfi);
  }
}
