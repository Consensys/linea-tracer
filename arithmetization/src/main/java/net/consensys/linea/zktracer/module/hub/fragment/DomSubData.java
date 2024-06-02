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
package net.consensys.linea.zktracer.module.hub.fragment;

import static net.consensys.linea.zktracer.module.hub.Trace.MULTIPLIER___DOM_SUB_STAMPS;

import com.google.common.base.Preconditions;

public record DomSubData(DomSubType type, int domOffset, int subOffset) {
  public int domStamp(final int hubStamp) {
    Preconditions.checkArgument(this.type == DomSubType.STANDARD);
    return MULTIPLIER___DOM_SUB_STAMPS * hubStamp + this.domOffset;
  }

  public enum DomSubType {
    STANDARD,
    REVERTS_WITH_CURRENT,
    REVERTS_WITH_CHILD,
    REVERTS_WITH_SELFDESTRUCT
  }
}
