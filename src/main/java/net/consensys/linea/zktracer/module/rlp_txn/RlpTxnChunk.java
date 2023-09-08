package net.consensys.linea.zktracer.module.rlp_txn;

import lombok.Getter;
import org.hyperledger.besu.datatypes.Transaction;

@Getter public class RlpTxnChunk {
  private Transaction tx;
  private boolean requireEvmExecution;

  public RlpTxnChunk(Transaction tx, boolean requireEvmExecution) {
  }
}
