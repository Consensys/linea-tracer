package net.consensys.linea.zktracer.module.hub;

import java.util.Optional;

import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.AddressUtils;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.account.AccountState;
import org.hyperledger.besu.evm.frame.MessageFrame;

public final class FailureConditions {
  private boolean deploymentAddressHasNonZeroNonce;
  private boolean deploymentAddressHasNonEmptyCode;

  public void reset() {
    this.deploymentAddressHasNonEmptyCode = false;
    this.deploymentAddressHasNonZeroNonce = false;
  }

  public void prepare(MessageFrame frame) {
    OpCode instruction = OpCode.of(frame.getCurrentOperation().getOpcode());
    Address deploymentAddress;
    switch (instruction) {
      case CREATE -> {
        deploymentAddress = AddressUtils.getCreateAddress(frame);
      }
      case CREATE2 -> {
        deploymentAddress = AddressUtils.getCreate2Address(frame);
      }
      default -> {
        return;
      }
    }
    final Optional<Account> deploymentAccount =
        Optional.ofNullable(frame.getWorldUpdater().get(deploymentAddress));
    deploymentAddressHasNonZeroNonce = deploymentAccount.map(a -> a.getNonce() != 0).orElse(false);
    deploymentAddressHasNonEmptyCode = deploymentAccount.map(AccountState::hasCode).orElse(false);
  }

  public boolean any() {
    return deploymentAddressHasNonEmptyCode || deploymentAddressHasNonZeroNonce;
  }

  public boolean none() {
    return !any();
  }
}
