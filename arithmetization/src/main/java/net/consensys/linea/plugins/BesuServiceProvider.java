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

package net.consensys.linea.plugins;

import org.hyperledger.besu.plugin.BesuContext;
import org.hyperledger.besu.plugin.services.BesuService;
import org.hyperledger.besu.plugin.services.PicoCLIOptions;
import org.hyperledger.besu.plugin.services.RpcEndpointService;
import org.hyperledger.besu.plugin.services.TraceService;

public class BesuServiceProvider {

  /**
   * Initialize a service of type {@link BesuService}.
   *
   * @return the initialized {@link BesuService}.
   */
  public static <T extends BesuService> T getBesuService(
      final BesuContext context, final Class<T> clazz) {
    return context
        .getService(clazz)
        .orElseThrow(
            () ->
                new RuntimeException(
                    "Unable to find given Besu service. Please ensure %s is registered."
                        .formatted(clazz.getName())));
  }

  public static TraceService getTraceService(final BesuContext context) {
    return getBesuService(context, TraceService.class);
  }

  public static PicoCLIOptions getPicoCLIOptionsService(final BesuContext context) {
    return getBesuService(context, PicoCLIOptions.class);
  }

  public static RpcEndpointService getRpcEndpointService(final BesuContext context) {
    return getBesuService(context, RpcEndpointService.class);
  }
}
