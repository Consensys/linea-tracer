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
package net.consensys.linea.continoustracing;

import java.util.Optional;

import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.corset.CorsetValidator;
import net.consensys.linea.zktracer.ZkTracer;
import org.hyperledger.besu.plugin.BesuContext;
import org.hyperledger.besu.plugin.BesuPlugin;
import org.hyperledger.besu.plugin.services.BesuEvents;
import org.hyperledger.besu.plugin.services.PicoCLIOptions;
import org.hyperledger.besu.plugin.services.TraceService;

@Slf4j
@AutoService(BesuPlugin.class)
public class ContinuousTracingPlugin implements BesuPlugin {
  public static final String NAME = "linea-continuous";
  public static final String ENV_WEBHOOK_URL = "SLACK_SHADOW_NODE_WEBHOOK_URL";

  private final ContinuousTracingCliOptions options;
  private BesuContext context;

  public ContinuousTracingPlugin() {
    options = ContinuousTracingCliOptions.create();
  }

  @Override
  public Optional<String> getName() {
    return Optional.of(NAME);
  }

  @Override
  public void register(final BesuContext context) {
    final PicoCLIOptions cmdlineOptions =
        context
            .getService(PicoCLIOptions.class)
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        "Expecting a PicoCLI options to register CLI options with, but none found."));

    cmdlineOptions.addPicoCLIOptions(getName().get(), options);

    this.context = context;
  }

  @Override
  public void start() {
    log.info("Starting {} with configuration: {}", NAME, options);

    // BesuEvents can only be requested after the plugin has been registered.
    final BesuEvents besuEvents =
        context
            .getService(BesuEvents.class)
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        "Expecting a BesuEvents to register events with, but none found."));

    final TraceService traceService =
        context
            .getService(TraceService.class)
            .orElseThrow(
                () -> new IllegalStateException("Expecting a TraceService, but none found."));

    final ContinuousTracingConfiguration tracingConfiguration = options.toDomainObject();

    if (tracingConfiguration.continuousTracing() && (tracingConfiguration.zkEvmBin() == null)) {
      log.error("zkEvmBin must be specified when continuousTracing is enabled");
      System.exit(1);
    }

    final String webHookUrl = System.getenv(ENV_WEBHOOK_URL);
    if (tracingConfiguration.continuousTracing() && (webHookUrl == null)) {
      log.error(
          "Webhook URL must be specified as environment variable {} when continuousTracing is enabled",
          ENV_WEBHOOK_URL);
      System.exit(1);
    }

    if (tracingConfiguration.continuousTracing()) {
      //      addLogAppender();

      besuEvents.addBlockAddedListener(
          new ContinuousTracingBlockAddedListener(
              new ContinuousTracer(traceService, new CorsetValidator(), ZkTracer::new),
              new TraceFailureHandler(SlackNotificationService.create(webHookUrl)),
              tracingConfiguration.zkEvmBin()));
    }
  }

  @Override
  public void stop() {}

  //  private void addLogAppender() {
  //    //    final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
  //    //    final Configuration config = ctx.getConfiguration();
  //    //
  //    //    final var existingAppenders = config.getAppenders();
  //    //    final ContinuousTracerLogAppender continuousTracerAppender =
  //    //        new ContinuousTracerLogAppender("ContinuousTracerLogAppender", null);
  //    //    continuousTracerAppender.start();
  //    //    config.addAppender(continuousTracerAppender);
  //    //
  //    //    final AppenderRef continuousTracerRef =
  //    //        AppenderRef.createAppenderRef("ContinuousTracerLogAppender", null, null);
  //    //    final List<AppenderRef> refs = new ArrayList<>();
  //    //    for (final Appender appender : existingAppenders.values()) {
  //    //      log.info("Found appender: {}", appender.getName());
  //    //      refs.add(AppenderRef.createAppenderRef(appender.getName(), null, null));
  //    //    }
  //    //    refs.add(continuousTracerRef);
  //    //
  //    //    final AppenderRef[] refsArray = refs.toArray(AppenderRef[]::new);
  //    //
  //    //    final LoggerConfig loggerConfig =
  //    //        LoggerConfig.newBuilder()
  //    //            .withAdditivity(true)
  //    //            .withLevel(Level.INFO)
  //    //            .withLoggerName(Logger.ROOT_LOGGER_NAME)
  //    //            .withRefs(refsArray)
  //    //            .withConfig(config)
  //    //            .build();
  //    //
  //    //    loggerConfig.addAppender(continuousTracerAppender, null, null);
  //    //    for (final Appender appender : existingAppenders.values()) {
  //    //      loggerConfig.addAppender(appender, null, null);
  //    //    }
  //    //
  //    //    config.removeLogger(Logger.ROOT_LOGGER_NAME);
  //    //    config.addLogger(Logger.ROOT_LOGGER_NAME, loggerConfig);
  //    //
  //    //    Configuration.initialize();
  //    //
  //    //    ctx.reconfigure(config);
  //    //    log.info("\nAdded ContinuousTracerLogAppender to log4j2 configuration\n");
  //
  //    final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
  //    //    final Configuration configAtStartup = ctx.getConfiguration();
  //    //    Configurator.initialize(configAtStartup);
  //    //    ctx.reconfigure();
  //
  //    final ConfigurationBuilder<BuiltConfiguration> builder =
  //        ConfigurationBuilderFactory.newConfigurationBuilder();
  //
  //    builder.setStatusLevel(Level.INFO);
  //    builder.setConfigurationName("ContinuousTracerLogConfiguration");
  //    // create a console appender
  //    final AppenderComponentBuilder consoleAppenderBuilder =
  //        builder
  //            .newAppender("stdout", "Console")
  //            .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
  //    consoleAppenderBuilder.add(
  //        builder
  //            .newLayout("PatternLayout")
  //            .addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable"));
  //    builder.add(consoleAppenderBuilder);
  //
  //    final AppenderComponentBuilder notificationAppenderBuilder =
  //        builder
  //            .newAppender("slack", "NotificationAppender")
  //            .addAttribute(
  //                "slackNotificationService",
  //                SlackNotificationService.create(System.getenv(ENV_WEBHOOK_URL)));
  //    builder.add(notificationAppenderBuilder);
  //    // create a rolling file appender
  //    //    LayoutComponentBuilder layoutBuilder =
  //    //        builder.newLayout("PatternLayout").addAttribute("pattern", "%d [%t] %-5level:
  //    // %msg%n");
  //    //    ComponentBuilder<?> triggeringPolicy =
  //    //        builder
  //    //            .newComponent("Policies")
  //    //            .addComponent(
  //    //                builder
  //    //                    .newComponent("CronTriggeringPolicy")
  //    //                    .addAttribute("schedule", "0 0 0 * * ?"))
  //    //            .addComponent(
  //    //                builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size",
  //    // "100M"));
  //    //    appenderBuilder =
  //    //        builder
  //    //            .newAppender("rolling", "RollingFile")
  //    //            .addAttribute("fileName", "target/rolling.log")
  //    //            .addAttribute("filePattern", "target/archive/rolling-%d{MM-dd-yy}.log.gz")
  //    //            .add(layoutBuilder)
  //    //            .addComponent(triggeringPolicy);
  //    //    builder.add(appenderBuilder);
  //
  //    builder.add(
  //        builder
  //            .newLogger("ContinuousTracerLogger", Level.INFO)
  //            .add(builder.newAppenderRef("slack"))
  //            .addAttribute("additivity", true));
  //
  //    builder.add(builder.newRootLogger(Level.INFO).add(builder.newAppenderRef("stdout")));
  //
  //    //    builder.add(
  //    //        builder
  //    //            .newLogger("NotificationLogger", Level.INFO)
  //    //            .add(builder.newAppenderRef("NotificationAppender")));
  //
  //    final Configuration config = builder.build();
  //
  //    final NotificationAppender continuousTracerAppender =
  //        new NotificationAppender(
  //            "NotificationAppender",
  //            SlackNotificationService.create(System.getenv(ENV_WEBHOOK_URL)),
  //            null);
  //    //    continuousTracerAppender.start();
  //    //    config.addAppender(continuousTracerAppender);
  //    //    Configurator.initialize(continuousTracerAppender.getClass().getClassLoader(), config);
  //
  //    ctx.reconfigure(config);
  //  }

  //  private Path pluginsDir() {
  //    final String pluginsDir = System.getProperty("besu.plugins.dir");
  //    if (pluginsDir == null) {
  //      return new File(System.getProperty("besu.home", "."), "plugins").toPath();
  //    } else {
  //      return new File(pluginsDir).toPath();
  //    }
  //  }
  //
  //  private Optional<ClassLoader> pluginDirectoryLoader(final Path pluginsDir) {
  //    if (pluginsDir != null && pluginsDir.toFile().isDirectory()) {
  //      log.debug("Searching for plugins in {}", pluginsDir.toAbsolutePath().toString());
  //
  //      try (final Stream<Path> pluginFilesList = Files.list(pluginsDir)) {
  //        final URL[] pluginJarURLs =
  //            pluginFilesList
  //                .filter(p -> p.getFileName().toString().endsWith(".jar"))
  //                .map(ContinuousTracingPlugin::pathToURIOrNull)
  //                .toArray(URL[]::new);
  //        return Optional.of(new URLClassLoader(pluginJarURLs, this.getClass().getClassLoader()));
  //      } catch (final MalformedURLException e) {
  //        log.error("Error converting files to URLs, could not load plugins", e);
  //      } catch (final IOException e) {
  //        log.error("Error enumerating plugins, could not load plugins", e);
  //      }
  //    } else {
  //      log.debug("Plugin directory does not exist, skipping registration. - {}", pluginsDir);
  //    }
  //
  //    return Optional.empty();
  //  }
  //
  //  private static URL pathToURIOrNull(final Path p) {
  //    try {
  //      return p.toUri().toURL();
  //    } catch (final MalformedURLException e) {
  //      return null;
  //    }
  //  }
}
