package net.consensys.linea.zktracer.module.rlptxrcpt;

import java.util.List;

import lombok.Getter;
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.plugin.data.Log;

@Getter
public class RlpTxrcptChunk {
  TransactionType txType;
  Boolean status;
  Long GasUsed;
  List<Log> logs;

  public RlpTxrcptChunk(TransactionType type, Boolean status, Long gasUsed, List<Log> logs) {}
}
