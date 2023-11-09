package linea.plugin.acc.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.util.List;

import linea.plugin.acc.test.tests.web3j.generated.SimpleStorage;
import org.hyperledger.besu.tests.acceptance.dsl.account.Accounts;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

public class MaxCallDataTest extends AbstractPluginTest {

  public static final int MAX_CALLDATA_SIZE = 1188; // contract has a call data size of 1160

  @Override
  public List<String> getTestCliOptions() {
    return new TestCliOptions()
        .set("--plugin-linea-max-tx-calldata-size=", String.valueOf(MAX_CALLDATA_SIZE))
        .set("--plugin-linea-max-block-calldata-size=", String.valueOf(MAX_CALLDATA_SIZE))
        .build();
  }

  @Test
  public void shouldMineTransactions() throws Exception {
    setup();
    final SimpleStorage simpleStorage = deploySimpleStorage();

    List<String> accounts =
        List.of(Accounts.GENESIS_ACCOUNT_ONE_PRIVATE_KEY, Accounts.GENESIS_ACCOUNT_TWO_PRIVATE_KEY);

    final Web3j web3j = minerNode.nodeRequests().eth();
    final List<Integer> numCaractersInStringList = List.of(150, 200, 400);

    numCaractersInStringList.forEach(
        num -> sendTransactionsWithGivenLengthPayload(simpleStorage, accounts, web3j, num));
  }

  @Test
  public void transactionIsNotMinedWhenTooBig() throws Exception {
    setup();
    final SimpleStorage simpleStorage = deploySimpleStorage();
    final Web3j web3j = minerNode.nodeRequests().eth();
    final String contractAddress = simpleStorage.getContractAddress();
    final Credentials credentials = Credentials.create(Accounts.GENESIS_ACCOUNT_ONE_PRIVATE_KEY);
    TransactionManager txManager = new RawTransactionManager(web3j, credentials, CHAIN_ID);

    final String txDataGood = simpleStorage.set("a".repeat(1200 - 80)).encodeFunctionCall();
    final String hashGood =
        txManager
            .sendTransaction(
                DefaultGasProvider.GAS_PRICE,
                DefaultGasProvider.GAS_LIMIT,
                contractAddress,
                txDataGood,
                BigInteger.ZERO)
            .getTransactionHash();

    final String txDataTooBig = simpleStorage.set("a".repeat(1200 - 79)).encodeFunctionCall();
    final String hashTooBig =
        txManager
            .sendTransaction(
                DefaultGasProvider.GAS_PRICE,
                DefaultGasProvider.GAS_LIMIT,
                contractAddress,
                txDataTooBig,
                BigInteger.ZERO)
            .getTransactionHash();

    TransactionReceiptProcessor receiptProcessor =
        new PollingTransactionReceiptProcessor(
            web3j, 4000L, TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH);

    // make sure that a transaction that is not too big was mined
    final TransactionReceipt transactionReceipt =
        receiptProcessor.waitForTransactionReceipt(hashGood);
    assertThat(transactionReceipt).isNotNull();

    final EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(hashTooBig).send();
    assertThat(receipt.getTransactionReceipt()).isEmpty();
  }
}
