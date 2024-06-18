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

package net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.precompiles;

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_ECADD;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_ECMUL;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_ECPAIRING;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_ECRECOVER;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_IDENTITY;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_RIPEMD;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_SHA2;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.OobCall;
import net.consensys.linea.zktracer.module.hub.precompiles.PrecompileInvocation;

@RequiredArgsConstructor
public class SimplePrecompileCall extends OobCall {
  final PrecompileInvocation p;

  @Override
  public int oobInstruction() {
    return switch (p.precompile()) {
      case EC_RECOVER -> OOB_INST_ECRECOVER;
      case SHA2_256 -> OOB_INST_SHA2;
      case RIPEMD_160 -> OOB_INST_RIPEMD;
      case IDENTITY -> OOB_INST_IDENTITY;
      case EC_ADD -> OOB_INST_ECADD;
      case EC_MUL -> OOB_INST_ECMUL;
      case EC_PAIRING -> OOB_INST_ECPAIRING;
      default -> throw new IllegalArgumentException("unexpected complex precompile");
    };
  }
}
