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

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.modexp.ModexpXbsCase;
import net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.modexp.xbsOobCall.ModexpXbsOobCall;
import net.consensys.linea.zktracer.module.hub.precompiles.modexpMetadata.ModexpMetadata;
import net.consensys.linea.zktracer.module.hub.section.call.CallSection;

public abstract class ModexpSubsection extends PrecompileSubsection {
  // 13 = 1 + 12 (scenario row + up to 12 miscellaneous fragments)
  public static final short NB_ROWS_HUB_PRC_MODEXP = 13;
  public final ModexpMetadata modexpMetadata;

  public ModexpSubsection(
      final Hub hub, final CallSection callSection, ModexpMetadata modexpMetadata) {
    super(hub, callSection);
    this.modexpMetadata = modexpMetadata;
  }

  public abstract ModexpMetadata getForkAppropriateModexpMetadata();

  // 13 = 1 + 12 (scenario row + up to 12 miscellaneous fragments)
  protected short maxNumberOfLines() {
    return NB_ROWS_HUB_PRC_MODEXP;
    // Note: we don't have the successBit available at the moment
    // and can't provide the "real" value (8 in case of failure.)
  }

  public abstract ModexpXbsOobCall getForkAppropriateModexpXbsOobCall(ModexpXbsCase modexpXbsCase);
}
