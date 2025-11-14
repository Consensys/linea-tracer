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
package net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.modexp.xbsOobCall;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.blake2fmodexpdata.OsakaBlakeModexpOperation;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.modexp.ModexpXbsCase;
import net.consensys.linea.zktracer.module.hub.precompiles.modexpMetadata.OsakaModexpMetadata;
import org.hyperledger.besu.evm.frame.MessageFrame;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class OsakaModexpXbsOobCall extends LondonModexpXbsOobCall {

  public OsakaModexpXbsOobCall(OsakaModexpMetadata modexpMetaData, ModexpXbsCase modexpXbsCase) {
    super(modexpMetaData, modexpXbsCase);
  }

  protected OsakaModexpMetadata getForkAppropriateModexpMetadata() {
    return (OsakaModexpMetadata) modexpMetadata;
  }

  public int modexpComponentByteSize() {
    return OsakaBlakeModexpOperation.modexpComponentByteSize();
  }

  @Override
  public void setInputData(MessageFrame frame, Hub hub) {}

  @Override
  public Trace.Oob trace(Trace.Oob trace) {
    return null;
  }

  @Override
  protected boolean xbsIsWithinBounds() {
    return getForkAppropriateModexpMetadata().tracedIsWithinBounds(modexpXbsCase);
  }

  @Override
  protected boolean xbsIsOutOfBounds() {
    return getForkAppropriateModexpMetadata().tracedIsOutOfBounds(modexpXbsCase);
  }

  @Override
  short xbsNormalized() {
    return (short) getForkAppropriateModexpMetadata().normalize(modexpXbsCase).toInt();
  }

  @Override
  short ybsReNormalized() {
    return xbsIsWithinBounds() ? (short) ybsLo().toInt() : 0;
  }

  @Override
  short maxXbsYbs() {
    return computeMax() && xbsIsWithinBounds()
        ? (short) Math.max(xbsNormalized(), this.ybsReNormalized())
        : 0;
  }

  @Override
  boolean xbsNormalizedIsNonZero() {
    return xbsIsWithinBounds() && xbsNormalized() != 0;
  }
}
