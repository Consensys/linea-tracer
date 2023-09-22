package net.consensys.linea.zktracer.module.rlptxrcpt;

import java.util.List;

import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.evm.log.Log;

record RlpTxrcptChunk(TransactionType txType, Boolean status, Long gasUsed, List<Log> logs) {}
