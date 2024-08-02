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
package net.consensys.linea.zktracer.module.hub.fragment.imc.oob;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.consensys.linea.zktracer.module.constants.GlobalConstants;

@Getter
@AllArgsConstructor
public enum OobInstruction {
  OOB_INST_BLAKE_CDS(GlobalConstants.OOB_INST_BLAKE_CDS),
  OOB_INST_BLAKE_PARAMS(GlobalConstants.OOB_INST_BLAKE_PARAMS),
  OOB_INST_CALL(GlobalConstants.OOB_INST_CALL),
  OOB_INST_CDL(GlobalConstants.OOB_INST_CDL),
  OOB_INST_CREATE(GlobalConstants.OOB_INST_CREATE),
  OOB_INST_DEPLOYMENT(GlobalConstants.OOB_INST_DEPLOYMENT),
  OOB_INST_ECADD(GlobalConstants.OOB_INST_ECADD),
  OOB_INST_ECMUL(GlobalConstants.OOB_INST_ECMUL),
  OOB_INST_ECPAIRING(GlobalConstants.OOB_INST_ECPAIRING),
  OOB_INST_ECRECOVER(GlobalConstants.OOB_INST_ECRECOVER),
  OOB_INST_IDENTITY(GlobalConstants.OOB_INST_IDENTITY),
  OOB_INST_JUMP(GlobalConstants.OOB_INST_JUMP),
  OOB_INST_JUMPI(GlobalConstants.OOB_INST_JUMPI),
  OOB_INST_MODEXP_CDS(GlobalConstants.OOB_INST_MODEXP_CDS),
  OOB_INST_MODEXP_EXTRACT(GlobalConstants.OOB_INST_MODEXP_EXTRACT),
  OOB_INST_MODEXP_LEAD(GlobalConstants.OOB_INST_MODEXP_LEAD),
  OOB_INST_MODEXP_PRICING(GlobalConstants.OOB_INST_MODEXP_PRICING),
  OOB_INST_MODEXP_XBS(GlobalConstants.OOB_INST_MODEXP_XBS),
  OOB_INST_RDC(GlobalConstants.OOB_INST_RDC),
  OOB_INST_RIPEMD(GlobalConstants.OOB_INST_RIPEMD),
  OOB_INST_SHA2(GlobalConstants.OOB_INST_SHA2),
  OOB_INST_SSTORE(GlobalConstants.OOB_INST_SSTORE),
  OOB_INST_XCALL(GlobalConstants.OOB_INST_XCALL);

  public boolean isCommonPrecompile() {
    return this == OOB_INST_ECRECOVER
        || this == OOB_INST_SHA2
        || this == OOB_INST_RIPEMD
        || this == OOB_INST_IDENTITY
        || this == OOB_INST_ECADD
        || this == OOB_INST_ECMUL
        || this == OOB_INST_ECPAIRING;
  }

  // TODO: add checks for other families of precompiles, if necessary

  private final int value;
}
