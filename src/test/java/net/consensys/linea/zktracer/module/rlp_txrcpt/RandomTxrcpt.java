package net.consensys.linea.zktracer.module.rlp_txrcpt;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import net.consensys.linea.zktracer.ZkTracer;
import net.consensys.linea.zktracer.corset.CorsetValidator;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.ethereum.core.TransactionReceipt;
import org.hyperledger.besu.evm.log.Log;
import org.hyperledger.besu.evm.log.LogTopic;
import org.junit.jupiter.api.Test;

public class RandomTxrcpt {
  @Test
  public void testRandomTxrcpt() {
    ZkTracer tracer = new ZkTracer();
    TransactionReceipt randomTxrcpt =
        new TransactionReceipt(
            TransactionType.of(2),
            0,
            2123006,
            List.of(
                new Log(
                    Address.wrap(Bytes.random(20)),
                    Bytes.random(134),
                 // Bytes.ofUnsignedInt(234),
                    List.of(LogTopic.of(Bytes.random(32)), LogTopic.of(Bytes.random(32))))),
            Optional.empty());
    // TransactionReceipt randomTxrcpt = new
    // TransactionReceipt(TransactionType.FRONTIER,0,21000,List.of(),Optional.empty());
    tracer.rlpTxrcpt.traceTransaction(randomTxrcpt, TransactionType.FRONTIER);
    tracer.traceEndConflation();
    assertThat(CorsetValidator.isValid(tracer.getTrace().toJson())).isTrue();
  }
}
