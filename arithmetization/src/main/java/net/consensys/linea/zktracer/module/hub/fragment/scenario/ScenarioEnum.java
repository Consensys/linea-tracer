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
  /** unset values, to be defined */
  UNDEFINED,
  /** scenarios related to CALL-type instructions */
  CALL,
  /** scenarios related to CREATE(2) instructions */
  CREATE_EXCEPTION,
  CREATE_ABORT,
  CREATE_FAILURE_CONDITION_WILL_REVERT,
  CREATE_FAILURE_CONDITION_WONT_REVERT,
  CREATE_EMPTY_INIT_CODE_WILL_REVERT,
  CREATE_EMPTY_INIT_CODE_WONT_REVERT,
  CREATE_NON_EMPTY_INIT_CODE_FAILURE_WILL_REVERT,
  CREATE_NON_EMPTY_INIT_CODE_FAILURE_WONT_REVERT,
  CREATE_NON_EMPTY_INIT_CODE_SUCCESS_WILL_REVERT,
  CREATE_NON_EMPTY_INIT_CODE_SUCCESS_WONT_REVERT,
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
  SELFDESTRUCT, // duplicated logic?
  SELFDESTRUCT_EXCEPTION,
  SELFDESTRUCT_WILL_REVERT,
  SELFDESTRUCT_WONT_REVERT_ALREADY_MARKED,
  SELFDESTRUCT_WONT_REVERT_NOT_YET_MARKED,
  /** describes the second scenario line required by a call to a precompile */
  PRECOMPILE;

  boolean isCall() {
    return this == CALL;
  }

  boolean isPrecompile() {
    return this == PRECOMPILE;
  }

  public boolean isCreate() {
    return this.isAnyOf(
        CREATE_EXCEPTION,
        CREATE_ABORT,
        CREATE_FAILURE_CONDITION_WILL_REVERT,
        CREATE_FAILURE_CONDITION_WONT_REVERT,
        CREATE_EMPTY_INIT_CODE_WILL_REVERT,
        CREATE_EMPTY_INIT_CODE_WONT_REVERT,
        CREATE_NON_EMPTY_INIT_CODE_FAILURE_WILL_REVERT,
        CREATE_NON_EMPTY_INIT_CODE_FAILURE_WONT_REVERT,
        CREATE_NON_EMPTY_INIT_CODE_SUCCESS_WILL_REVERT,
        CREATE_NON_EMPTY_INIT_CODE_SUCCESS_WONT_REVERT);
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

  boolean isSelfDestruct() {
    return this.isAnyOf(
        SELFDESTRUCT,
        SELFDESTRUCT_EXCEPTION,
        SELFDESTRUCT_WILL_REVERT,
        SELFDESTRUCT_WONT_REVERT_ALREADY_MARKED,
        SELFDESTRUCT_WONT_REVERT_NOT_YET_MARKED);
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
