package net.consensys.linea.tracegeneration;

import java.util.HashMap;
import java.util.Map;

public class LineCountsByBlockCache {

  // TODO limit the size of this cache
  static final Map<Long, Map<String, Integer>> lineCountsByBlockNumber = new HashMap<>();

  public static void putBlockTraces(final long blockNumber, final Map<String, Integer> lineCounts) {
    lineCountsByBlockNumber.put(blockNumber, lineCounts);
  }

  public static Map<String, Integer> getBlockTraces(final long blockNumber) {
    return lineCountsByBlockNumber.get(blockNumber);
  }
}
