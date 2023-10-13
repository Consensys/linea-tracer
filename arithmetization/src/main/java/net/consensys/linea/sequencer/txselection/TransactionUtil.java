package net.consensys.linea.sequencer.txselection;

import java.util.List;

import org.hyperledger.besu.datatypes.PendingTransaction;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.log.Log;

public class TransactionUtil {
  private static final int AVERAGE_TX_RLP_SIZE = 400;
  private static final int FROM_ADDRESS_SIZE = 20;

  /*
   l1Size := 64 + 63 + (self.TxCount+self.currentTxCount)*AVERAGE_TX_RLP_SIZE +
  32*(len(self.L2l1logs)+len(self.currentL2l1logs)) + 64 +
  20*(self.TxCount+self.currentTxCount) + 64
    */

  public static final int EMPTY_L1_BLOCK_SIZE = 64 + 63 + 64 + 64;

  public static int calculateL1TransactionSize(
      final PendingTransaction pendingTransaction, final List<Log> logs) {
    // TODO Replace AVERAGE_TX_RLP_SIZE with actual transaction size
    // return calculateTransactionSize(pendingTransaction.getTransaction())
    //    + FROM_ADDRESS_SIZE
    //    + calculateLogsSize(logs);
    return AVERAGE_TX_RLP_SIZE + FROM_ADDRESS_SIZE + calculateLogsSize(logs);
  }

  private static int calculateTransactionSize(final Transaction transaction) {
    // TODO Check calculation here
    // the size in bytes of the encoded transaction.
    return transaction.getSize();
  }

  private static int calculateLogsSize(final List<Log> logs) {
    return logs.size() * 32;
  }
}
