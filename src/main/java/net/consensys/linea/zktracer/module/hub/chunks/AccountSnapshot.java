package net.consensys.linea.zktracer.module.hub.chunks;

import net.consensys.linea.zktracer.module.hub.Bytecode;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.evm.account.Account;

public record AccountSnapshot(
    Address address,
    long nonce,
    Wei balance,
    boolean warm,
    Bytecode code,
    int deploymentNumber,
    boolean deploymentStatus) {
  public static AccountSnapshot fromAccount(
      Account account,
      Bytecode code,
      boolean warm,
      int deploymentNumber,
      boolean deploymentStatus) {
    if (account == null) {
      return new AccountSnapshot(
          Address.ZERO, 0, Wei.ZERO, warm, Bytecode.EMPTY, deploymentNumber, deploymentStatus);
    }

    return new AccountSnapshot(
        account.getAddress(),
        account.getNonce(),
        account.getBalance(),
        warm,
        code,
        deploymentNumber,
        deploymentStatus);
  }
}
