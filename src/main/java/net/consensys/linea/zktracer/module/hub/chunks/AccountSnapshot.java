package net.consensys.linea.zktracer.module.hub.chunks;

import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.evm.Code;
import org.hyperledger.besu.evm.account.Account;

public record AccountSnapshot(
    long nonce,
    Wei balance,
    boolean warm,
    Hash codeHash,
    int codeSize,
    int deploymentNumber,
    boolean deploymentStatus) {
  public static AccountSnapshot fromAccount(
      Account account, Code code, boolean warm, int deploymentNumber, boolean deploymentStatus) {
    if (account == null) {
      return new AccountSnapshot(
          0, Wei.ZERO, warm, Hash.EMPTY, 0, deploymentNumber, deploymentStatus);
    }

    return new AccountSnapshot(
        account.getNonce(),
        account.getBalance(),
        warm,
        code.getCodeHash(),
        code.getSize(),
        deploymentNumber,
        deploymentStatus);
  }
}
