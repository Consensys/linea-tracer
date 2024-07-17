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
package net.consensys.linea.zktracer.module.hub.fragment.scenario;

public enum ScenarioEnum {
  /** scenarios related to CALL-type instructions */
  CALL,
  /** scenarios related to CREATE(2) instructions */
  CREATE,
  /** scenarios related to RETURN */
  RETURN,
  RETURN_EXCEPTION,
  RETURN_FROM_MESSAGE_CALL_WILL_TOUCH_RAM,
  RETURN_FROM_MESSAGE_CALL_WONT_TOUCH_RAM,
  RETURN_FROM_DEPLOYMENT_EMPTY_CODE_WILL_REVERT,
  RETURN_FROM_DEPLOYMENT_EMPTY_CODE_WONT_REVERT,
  RETURN_FROM_DEPLOYMENT_NONEMPTY_CODE_WILL_REVERT,
  RETURN_FROM_DEPLOYMENT_NONEMPTY_CODE_WONT_REVERT,
  /** scenarios related to SELFDESTRUCT */
  SELFDESTRUCT,
  /** describes the second scenario line required by a call to a precompile */
  PRECOMPILE;

  boolean isCall() {
    return this == CALL;
  }

  boolean isPrecompile() {
    return this == PRECOMPILE;
  }

  boolean isCreate() {
    return this == CREATE;
  }

  boolean isReturn() {
    return this.isAnyOf(
        RETURN_EXCEPTION,
        RETURN_FROM_MESSAGE_CALL_WILL_TOUCH_RAM,
        RETURN_FROM_MESSAGE_CALL_WONT_TOUCH_RAM,
        RETURN_FROM_DEPLOYMENT_EMPTY_CODE_WILL_REVERT,
        RETURN_FROM_DEPLOYMENT_EMPTY_CODE_WONT_REVERT,
        RETURN_FROM_DEPLOYMENT_NONEMPTY_CODE_WILL_REVERT,
        RETURN_FROM_DEPLOYMENT_NONEMPTY_CODE_WONT_REVERT);
  }

  public boolean isAnyOf(ScenarioEnum... scenarios) {
    for (ScenarioEnum scenario : scenarios) {
      if (scenario.equals(this)) {
        return true;
      }
    }

    return false;
  }
}
