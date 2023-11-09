package linea.plugin.acc.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.util.List;

import org.hyperledger.besu.tests.acceptance.dsl.account.Accounts;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.RawTransactionManager;
import org.web3j.utils.Convert;

public class DeniedTransactionsTxPoolTest extends AbstractPluginTest {

  @Override
  public List<String> getTestCliOptions() {
    return new TestCliOptions()
        .set("--plugin-linea-deny-list-path=", getResourcePath("/denyList.txt"))
        .build();
  }

  @Test
  public void deniedTransactionNotAddedToTxPool() throws Exception {
    final Credentials notDenied = Credentials.create(Accounts.GENESIS_ACCOUNT_ONE_PRIVATE_KEY);
    final Credentials denied = Credentials.create(Accounts.GENESIS_ACCOUNT_TWO_PRIVATE_KEY);
    final Web3j miner = minerNode.nodeRequests().eth();

    BigInteger gasPrice = Convert.toWei("20", Convert.Unit.GWEI).toBigInteger();
    BigInteger gasLimit = BigInteger.valueOf(210000);

    // Make sure a sender on the deny list cannot add transactions to the pool
    RawTransactionManager transactionManager = new RawTransactionManager(miner, denied, CHAIN_ID);
    EthSendTransaction transactionResponse =
        transactionManager.sendTransaction(
            gasPrice, gasLimit, notDenied.getAddress(), "", BigInteger.ONE); // 1 wei

    assertThat(transactionResponse.getTransactionHash()).isNull();
    assertThat(transactionResponse.getError().getMessage())
        .isEqualTo(
            "sender 0x627306090abab3a6e1400e9345bc60c78a8bef57 is blocked as appearing on the SDN or other legally prohibited list");

    // Make sure a transaction with a recipient on the deny list cannot be added to the pool
    transactionManager = new RawTransactionManager(miner, notDenied, CHAIN_ID);
    transactionResponse =
        transactionManager.sendTransaction(
            gasPrice, gasLimit, denied.getAddress(), "", BigInteger.ONE); // 1 wei

    assertThat(transactionResponse.getTransactionHash()).isNull();
    assertThat(transactionResponse.getError().getMessage())
        .isEqualTo(
            "recipient 0x627306090abab3a6e1400e9345bc60c78a8bef57 is blocked as appearing on the SDN or other legally prohibited list");

    // Make sure a transaction calling a contract on the deny list (e.g. precompile) is not added to
    // the pool
    transactionResponse =
        transactionManager.sendTransaction(
            gasPrice,
            gasLimit,
            "0x000000000000000000000000000000000000000a",
            "0xdeadbeef",
            BigInteger.ONE); // 1 wei

    assertThat(transactionResponse.getTransactionHash()).isNull();
    assertThat(transactionResponse.getError().getMessage())
        .isEqualTo("destination address is a precompile address and cannot receive transactions");
  }
}
