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

package net.consensys.linea.zktracer.module.blockdata;

import static net.consensys.linea.zktracer.module.blockdata.Trace.MAX_CT;

import java.math.BigInteger;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.container.ModuleOperation;
import org.hyperledger.besu.datatypes.Address;

@RequiredArgsConstructor
public class BlockdataOperation extends ModuleOperation {
  private final Address coinbase;
  private final long timestamp;
  private final long absoluteBlockNumber;
  private final BigInteger difficulty;

  @Override
  protected int computeLineCount() {
    return MAX_CT + 1;
  }
}
