package linea.plugin.acc.test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import linea.plugin.acc.test.tests.web3j.generated.SimpleStorage;
import org.assertj.core.api.Assertions;
import org.hyperledger.besu.tests.acceptance.dsl.account.Accounts;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Numeric;

public class ModuleLimitsTest extends AbstractPluginTest {
  @Override
  public List<String> getTestCliOptions() {
    return new TestCliOptions()
        .set("--plugin-linea-module-limit-file-path=", getResourcePath("/moduleLimits.json"))
        .build();
  }

  @Test
  public void transactionIsNotMinedWhenTooManyTraceLines() throws Exception {
    final SimpleStorage simpleStorage = deploySimpleStorage();
    final Web3j web3j = minerNode.nodeRequests().eth();
    final String contractAddress = simpleStorage.getContractAddress();
    final Credentials credentials = Credentials.create(Accounts.GENESIS_ACCOUNT_ONE_PRIVATE_KEY);
    final String txData = simpleStorage.add(BigInteger.valueOf(100)).encodeFunctionCall();

    final ArrayList<String> hashes = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      final RawTransaction transaction =
          RawTransaction.createTransaction(
              CHAIN_ID,
              BigInteger.valueOf(i + 1),
              DefaultGasProvider.GAS_LIMIT,
              contractAddress,
              BigInteger.ZERO,
              txData,
              BigInteger.ONE,
              BigInteger.ONE);
      final byte[] signedTransaction = TransactionEncoder.signMessage(transaction, credentials);
      final EthSendTransaction response =
          web3j.ethSendRawTransaction(Numeric.toHexString(signedTransaction)).send();
      hashes.add(response.getTransactionHash());
    }

    TransactionReceiptProcessor receiptProcessor =
        new PollingTransactionReceiptProcessor(
            web3j,
            TransactionManager.DEFAULT_POLLING_FREQUENCY,
            TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH);

    // make sure that there are no more than one transaction per block, because the limit for the
    // add module only allows for one of these transactions.
    final HashSet<Long> blockNumbers = new HashSet<>();
    for (String h : hashes) {
      Assertions.assertThat(
              blockNumbers.add(
                  receiptProcessor.waitForTransactionReceipt(h).getBlockNumber().longValue()))
          .isEqualTo(true);
    }
  }
}
