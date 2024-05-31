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

package net.consensys.linea.zktracer.module.hub.signals;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.Hub;
import org.hyperledger.besu.evm.frame.MessageFrame;

/**
 * The platform controller handles external modules activation, exceptions, and aborts. It provides
 * a centralized way of accessing this information from other modules.
 */
@Accessors(fluent = true)
public class PlatformController {
  private final Hub hub;
  /** What other modules should be triggered for the current operation */
  @Getter private final Signals signals;
  /** The exceptions raised during the execution of the current operation */
  @Getter private final Exceptions exceptions;
  /** The aborting conditions raised during the execution of the current operation */
  @Getter private final AbortingConditions abortingConditions;

  @Getter private final FailureConditions failureConditions;

  public PlatformController(final Hub hub) {
    this.hub = hub;
    this.exceptions = new Exceptions(hub);
    this.abortingConditions = new AbortingConditions();
    this.signals = new Signals(this);
    this.failureConditions = new FailureConditions(hub);
  }

  /** Reset all information */
  public void reset() {
    this.signals.reset();
    this.exceptions.reset();
    this.abortingConditions.reset();
    this.failureConditions.reset();
  }

  /**
   * Fill all the values of the controller from the information pertaining to the execution of the
   * current operation.
   *
   * @param frame the current execution context
   */
  public void setup(MessageFrame frame) {
    this.reset();

    this.exceptions.prepare(frame, Hub.GAS_PROJECTOR);
    if (this.exceptions.none()) {
      this.abortingConditions.prepare(hub);
      if (abortingConditions.none()) {
        this.failureConditions.prepare(frame);
      }
    }
    this.signals.prepare(frame, this, this.hub);
  }
}
