package net.consensys.linea.zktracer.module.rlptxrcpt;

import java.util.List;

import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.log.LogTopic;
import org.hyperledger.besu.plugin.data.Log;

public record LogWrapper(Address logger, Bytes data, List<LogTopic> topics) implements Log {
  @Override
  public Address getLogger() {
    return logger;
  }

  @Override
  public List<? extends Bytes32> getTopics() {
    return topics;
  }

  @Override
  public Bytes getData() {
    return data;
  }
}
