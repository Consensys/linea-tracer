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

package net.consensys.linea.plugins.readiness;

import static java.util.Collections.singletonMap;

import java.util.Map;
import java.util.Optional;

import com.google.auto.service.AutoService;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.plugins.AbstractLineaOptionsPlugin;
import net.consensys.linea.plugins.BesuServiceProvider;
import net.consensys.linea.plugins.LineaOptionsPluginConfiguration;
import net.consensys.linea.plugins.rpc.RequestLimiter;
import net.consensys.linea.plugins.rpc.RequestLimiterDispatcher;
import org.hyperledger.besu.plugin.BesuContext;
import org.hyperledger.besu.plugin.BesuPlugin;
import org.hyperledger.besu.plugin.services.sync.SynchronizationService;

@Slf4j
@AutoService(BesuPlugin.class)
public class TracerReadinessPlugin extends AbstractLineaOptionsPlugin {
  private static final String TRACER_READINESS_ENDPOINT_NAME = "/tracer-readiness";
  private SynchronizationService synchronizationService;
  private HttpServer server;
  private BesuContext besuContext;

  /**
   * Register the needed Besu services.
   *
   * @param context the BesuContext to be used.
   */
  @Override
  public void register(final BesuContext context) {
    super.register(context);
    this.besuContext = context;
  }

  @Override
  protected Map<String, LineaOptionsPluginConfiguration> getLineaPluginConfigMap() {
    final TracerReadinessCliOptions tracerReadinessCliOptions = TracerReadinessCliOptions.create();

    return Map.of(TracerReadinessCliOptions.CONFIG_KEY, tracerReadinessCliOptions.asPluginConfig());
  }

  @Override
  public void start() {
    // Initialize Vertx
    final Vertx vertx = Vertx.vertx();

    final Router router = Router.router(vertx);

    // Register the readiness check handler
    router
        .get(TRACER_READINESS_ENDPOINT_NAME)
        .handler(
            routingContext -> {
              if (isTracerReady()) {
                routingContext.response().setStatusCode(200).end(statusResponse("UP"));
              } else {
                routingContext.response().setStatusCode(503).end(statusResponse("DOWN"));
              }
            });

    TracerReadinessConfiguration configuration =
        (TracerReadinessConfiguration)
            getConfigurationByKey(TracerReadinessCliOptions.CONFIG_KEY).optionsConfig();

    // Start the Vertx HTTP server
    server =
        vertx
            .createHttpServer(httpServerOptions(configuration))
            .requestHandler(router)
            .listen(
                configuration.serverPort(),
                result -> {
                  final String pluginName = getClass().getSimpleName();
                  final int port = configuration.serverPort();

                  if (result.succeeded()) {
                    log.info("[{}] Started listening on port {}", pluginName, port);
                  } else {
                    log.error(
                        "[{}] Failed to start listening on port {}: {}",
                        pluginName,
                        port,
                        result.cause().getMessage());
                  }
                });
  }

  private String statusResponse(final String status) {
    return new JsonObject(singletonMap("status", status)).encodePrettily();
  }

  private HttpServerOptions httpServerOptions(final TracerReadinessConfiguration config) {
    return new HttpServerOptions().setHost(config.serverHost()).setPort(config.serverPort());
  }

  private boolean isTracerReady() {
    this.synchronizationService =
        Optional.ofNullable(synchronizationService)
            .orElse(BesuServiceProvider.getSynchronizationService(besuContext));

    final RequestLimiter requestLimiter =
        RequestLimiterDispatcher.getLimiter(
            RequestLimiterDispatcher.SINGLE_INSTANCE_REQUEST_LIMITER_KEY);

    return !requestLimiter.isNodeAtMaxCapacity() && synchronizationService.isInitialSyncPhaseDone();
  }

  @Override
  public void stop() {
    super.stop();
    try {
      server.close().toCompletionStage().toCompletableFuture().get();
    } catch (Exception e) {
      throw new RuntimeException("Error closing readiness endpoint HTTP server.", e);
    }
  }
}
