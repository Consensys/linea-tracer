package net.consensys.linea.zktracer.module.rlp_txn;

import org.hyperledger.besu.datatypes.Transaction;

public record RlpTxnChunk(Transaction tx, boolean requireEvmExecution, int absTxNum) {}
