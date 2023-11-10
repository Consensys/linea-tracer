package net.consensys.linea.tracegeneration.rpc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.consensys.linea.tracegeneration.LineCountsByBlockCache;
import org.hyperledger.besu.plugin.BesuContext;
import org.hyperledger.besu.plugin.services.TraceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RollupGetTracesCountersByBlockNumberV0Test {

  BesuContext context = mock(BesuContext.class);
  TraceService traceService = mock(TraceService.class);
  RollupGetTracesCountersByBlockNumberV0 method =
      new RollupGetTracesCountersByBlockNumberV0(context);

  @BeforeEach
  void setup() {
    when(context.getService(TraceService.class)).thenReturn(Optional.of(traceService));
  }

  @Test
  void getNamespace() {
    assertThat(method.getNamespace()).isEqualTo("rollup");
  }

  @Test
  void getName() {
    assertThat(method.getName()).isEqualTo("getTracesCountersByBlockNumberV0");
  }

  @Test
  void getTracesCounters() {

    long blockNumber = 77;
    TracesCounters tracesCounters0 =
        method.getTracesCounters(new TracesCountersRequestParams(blockNumber, "v0"));
    verifyTraceBlockCallAndAddTraceCountsToCache(blockNumber);

    // now it should exist in the cache
    TracesCounters tracesCounters1 =
        method.getTracesCounters(new TracesCountersRequestParams(blockNumber, "v0"));
    TracesCounters tracesCounters2 =
        method.getTracesCounters(new TracesCountersRequestParams(blockNumber, "v0"));

    // no need to trace again
    verifyNoMoreInteractions(traceService);

    assertThat(tracesCounters1).isEqualTo(tracesCounters2);
  }

  private void verifyTraceBlockCallAndAddTraceCountsToCache(long blockNumber) {
    // cache is empty, traceBlock() will be called to generate the traces
    verify(traceService).traceBlock(eq(blockNumber), any());

    // normally putBlockTraces() would have been called via traceBlock() -> zkTracer.traceEndBlock()
    Map<String, Integer> lineCounts = getFakeLineCounts();
    LineCountsByBlockCache.putBlockTraces(blockNumber, lineCounts);
  }

  private Map<String, Integer> getFakeLineCounts() {
    Map<String, Integer> lineCounts = new HashMap<String, Integer>();
    lineCounts.put("add", 99);
    lineCounts.put("trm", 88);
    return lineCounts;
  }
}
