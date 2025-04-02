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

package net.consensys.linea.zktracer.module.hub.state;

import java.util.ArrayList;
import java.util.List;

import net.consensys.linea.zktracer.module.hub.transients.Block;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.plugin.data.ProcessableBlockHeader;

public class BlockStack {
  private final List<Block> stack = new ArrayList<>();

  public void newBlock(
      final ProcessableBlockHeader processableBlockHeader, final Address miningBeneficiary) {
    final int relBlockNumber = currentBlockNumber();
    stack.add(
        new Block(
            relBlockNumber, miningBeneficiary, (Wei) processableBlockHeader.getBaseFee().get()));
  }

  public Block currentBlock() {
    return stack.getLast();
  }

  public int currentBlockNumber() {
    return stack.size();
  }
}
