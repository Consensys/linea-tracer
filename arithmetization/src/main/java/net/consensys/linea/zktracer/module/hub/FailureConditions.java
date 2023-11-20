package net.consensys.linea.zktracer.module.hub;

import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.AddressUtils;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.MessageFrame;


public final class FailureConditions {
    final private boolean deploymentAddressHasNonZeroNonce;
    final private boolean deploymentAddressHasNonEmptyCode;

    public FailureConditions(MessageFrame frame) {
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
                throw new RuntimeException("Asked for Failure condition for non CREATE/CREATE2 opcode");
            }
        }
        Account deploymentAccount = frame.getWorldUpdater().get(deploymentAddress);
        deploymentAddressHasNonZeroNonce = deploymentAccount.getNonce() != 0;
        deploymentAddressHasNonEmptyCode = deploymentAccount.hasCode();
    }

    public boolean any() { return deploymentAddressHasNonEmptyCode || deploymentAddressHasNonZeroNonce; }
    public boolean none() { return !any(); }
}
