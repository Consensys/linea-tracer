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

package net.consensys.linea.plugins.rpc;

import java.security.InvalidParameterException;

import org.hyperledger.besu.plugin.BesuContext;
import org.hyperledger.besu.plugin.services.TraceService;

public class Requests {

  /**
   * Initialize the TraceService.
   *
   * @return the initialized TraceService.
   */
  public static TraceService getTraceService(final BesuContext besuContext) {
    return besuContext
        .getService(TraceService.class)
        .orElseThrow(
            () ->
                new RuntimeException(
                    "Unable to find trace service. Please ensure TraceService is registered."));
  }

  public static void validatePluginRpcRequestParams(final Object[] rawParams) {
    // params size should be one, because we expect an object containing all the needed request
    // parameters.
    if (rawParams.length != 1) {
      throw new InvalidParameterException(
          "Expected a single params object in the params array but got %d"
              .formatted(rawParams.length));
    }
  }
}
