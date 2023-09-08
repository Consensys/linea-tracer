package net.consensys.linea.zktracer.module.rlptxrcpt;

import lombok.Getter;
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.plugin.data.TransactionReceipt;

@Getter
public class RlpTxrcptChunk {
  TransactionReceipt txrcpt;
  TransactionType txType;
}
