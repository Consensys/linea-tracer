# Linea plugins

### Line Counts - LineCountsEndpointServicePlugin

#### `linea_getBlockTracesCountersV2`

The `LineCountsEndpointServicePlugin` registers an RPC endpoint named `getBlockTracesCountersV2`
under the `linea` namespace. When this endpoint is called, returns trace counters based on the provided request
parameters.

#### Plugin Parameters

- Introduces the `plugin-linea-line-counts-modules-to-count-config-file-path` plugin CLI parameter to set the path of a
  module limits TOML file from which in can read all the included module keys and calculate line counts only for those modules.
- When `plugin-linea-line-counts-modules-to-count-config-file-path` is not provided it returns line counts for all
  modules that are intended for counting.

#### RPC Request Parameters

- `blockNumber`: _string_ - The block number
- `expectedTracesEngineVersion`: _string_ - The tracer version. It will return an error if the
  requested version is different from the tracer runtime

### Trace generation - TracesEndpointServicePlugin

#### `linea_generateConflatedTracesToFileV2`

This plugin registers an RPC endpoint named `generateConflatedTracesToFileV2` under the `linea` namespace.
The endpoint generates conflated file traces.

#### Plugin Parameters

- Introduces `plugin-linea-continuous-tracing-traces-output-path` plugin CLI parameter to determine the output
  directory of the generated trace `.lt` files.

#### RPC Request Parameters

- `startBlockNumber`: _string_ - the fromBlock number
- `endBlockNumber`: _string_ - The toBlock number
- `expectedTracesEngineVersion`: _string_ - The tracer version. It will return an error if the
  requested version is different from the tracer runtime

## Continuous Tracing

The continuous tracing plugin allows to trace every newly imported block and use Corset to check if the constraints are
valid. In case of an error a message will be sent to the configured Slack channel.

### Usage

The continuous tracing plugin is disabled by default. To enable it, use the `--plugin-linea-continuous-tracing-enabled`
flag. If the plugin is enabled it is mandatory to specify the location of `zkevm.bin` using
the `--plugin-linea-continuous-tracing-zkevm-bin` flag. The user with which the node is running needs to have the
appropriate permissions to access `zkevm.bin`.

In order to send a message to Slack a webhook URL needs to be specified by setting the `SLACK_SHADOW_NODE_WEBHOOK_URL`
environment variable. An environment variable was chosen instead of a command line flag to avoid leaking the webhook URL
in the process list.

The environment variable can be set via systemd using the following command:

```shell
Environment=SLACK_SHADOW_NODE_WEBHOOK_URL=https://hooks.slack.com/services/SECRET_VALUES
```

### Invalid trace handling

In the success case the trace file will simply be deleted.

In case of an error the trace file will be renamed to `trace_$BLOCK_NUMBER_$BLOCK_HASH.lt` and moved
to `$BESU_USER_HOME/invalid-traces`. The output of Corset will be saved in the same directory in a file
named `corset_output_$BLOCK_NUMBER_$BLOCK_HASH.txt`. After that an error message will be sent to Slack.

## Tracer Readiness

Tracer Readiness is a plugin that enables the existence of the `/tracer-readiness` REST endpoint that
ensures that the given node is able to accept new requests. Under the hood it performs request limiting via an
option configuring the number of allowed concurrent requests. Additionally in ensures that the your state is in sync.

The plugin supports the following options in the TOML configuration:

```toml
# Configures the number of allowed concurrent requests that the node can process.
plugin-linea-rpc-concurrent-requests-limit=1
# Configures the host of the Tracer Readiness plugin.
plugin-linea-tracer-readiness-server-host="0.0.0.0"
# Configures the port of the Tracer Readiness plugin.
plugin-linea-tracer-readiness-server-port=8548
```
