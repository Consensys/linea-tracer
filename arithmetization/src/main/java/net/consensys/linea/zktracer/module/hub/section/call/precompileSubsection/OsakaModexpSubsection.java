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
package net.consensys.linea.zktracer.module.hub.section.call.precompileSubsection;

import static com.google.common.base.Preconditions.checkState;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.modexp.ModexpXbsCase;
import net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.modexp.xbsOobCall.OsakaModexpXbsOobCall;
import net.consensys.linea.zktracer.module.hub.precompiles.modexpMetadata.OsakaModexpMetadata;
import net.consensys.linea.zktracer.module.hub.section.call.CallSection;

public class OsakaModexpSubsection extends LondonModexpSubsection {
  public OsakaModexpSubsection(
      Hub hub, CallSection callSection, OsakaModexpMetadata modexpMetadata) {
    super(hub, callSection, modexpMetadata);
  }

  @Override
  public OsakaModexpMetadata getForkAppropriateModexpMetadata() {
    checkState(
        modexpMetadata instanceof OsakaModexpMetadata,
        "modexpMetadata must be OsakaModexpMetadata");
    return (OsakaModexpMetadata) modexpMetadata;
  }

  @Override
  public OsakaModexpXbsOobCall getForkAppropriateModexpXbsOobCall(ModexpXbsCase modexpXbsCase) {

    checkState(
        modexpMetadata instanceof OsakaModexpMetadata,
        "modexpMetadata must be OsakaModexpMetadata");
    return new OsakaModexpXbsOobCall((OsakaModexpMetadata) modexpMetadata, modexpXbsCase);
  }
}
