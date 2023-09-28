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

package net.consensys.linea;

import org.hyperledger.besu.plugin.BesuContext;
import org.hyperledger.besu.plugin.BesuPlugin;

public abstract class LineaPlugin implements BesuPlugin {

  /**
   * Linea plugins extending this class will halt startup of Besu in case of exception.
   *
   * <p>If that's NOT desired, the plugin should implement {@link BesuPlugin} directly.
   *
   * @param context
   */
  @Override
  public void register(final BesuContext context) {
    try {
      System.out.println("Registering Linea plugin " + this.getClass().getName());

      doRegister(context);

    } catch (Exception e) {
      System.out.println("Halting Besu startup: exception in plugin registration");
      e.printStackTrace();
      // this will cause besu to exit
      System.exit(1);
    }
  }

  /**
   * Linea plugins need to implement this method. Called by {@link BesuPlugin} register method
   *
   * @param context
   */
  public abstract void doRegister(final BesuContext context);

  @Override
  public void start() {}

  @Override
  public void stop() {}
}
