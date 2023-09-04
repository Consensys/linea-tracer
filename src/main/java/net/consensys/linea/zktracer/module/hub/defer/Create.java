package net.consensys.linea.zktracer.module.hub.defer;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.chunks.AccountChunk;
import net.consensys.linea.zktracer.module.hub.chunks.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.chunks.ContextChunk;
import net.consensys.linea.zktracer.module.hub.section.CreateSection;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.operation.Operation;

public record Create(
    Address creatorAddress,
    Address createdAddress,
    AccountSnapshot oldCreatorSnapshot,
    AccountSnapshot oldCreatedSnapshot,
    ContextChunk contextChunk)
    implements PostExecDefer, NextContextDefer {
  // PostExec defer
  @Override
  public void run(Hub hub, MessageFrame frame, Operation.OperationResult operationResult) {
    if (operationResult.getHaltReason() != null) {
      // TODO Handle different failure states

      AccountSnapshot newCreatorSnapshot =
          AccountSnapshot.fromAccount(
              frame.getWorldUpdater().getAccount(creatorAddress),
              true,
              hub.deploymentNumber(creatorAddress),
              hub.isDeploying(creatorAddress));

      AccountSnapshot newCreatedSnapshot =
          AccountSnapshot.fromAccount(
              frame.getWorldUpdater().getAccount(createdAddress),
              true,
              hub.deploymentNumber(createdAddress),
              hub.isDeploying(createdAddress));

      hub.addTraceSection(
          new CreateSection(
              hub,
              contextChunk,
              // 3× own account
              new AccountChunk(
                  creatorAddress, oldCreatorSnapshot, newCreatorSnapshot, false, 0, false),
              new AccountChunk(
                  creatorAddress, oldCreatorSnapshot, newCreatorSnapshot, false, 0, false),
              new AccountChunk(
                  creatorAddress, oldCreatorSnapshot, newCreatorSnapshot, false, 0, false),
              // 2×created account
              new AccountChunk(
                  createdAddress, oldCreatedSnapshot, newCreatedSnapshot, false, 0, false),
              new AccountChunk(
                  createdAddress, oldCreatedSnapshot, newCreatedSnapshot, false, 0, false)));
    }
  }

  // Context defer
  @Override
  public void run(Hub hub, MessageFrame frame) {
    AccountSnapshot newCreatorSnapshot =
        AccountSnapshot.fromAccount(
            frame.getWorldUpdater().getAccount(creatorAddress),
            true,
            hub.deploymentNumber(creatorAddress),
            hub.isDeploying(creatorAddress));

    AccountSnapshot newCreatedSnapshot =
        AccountSnapshot.fromAccount(
            frame.getWorldUpdater().getAccount(createdAddress),
            true,
            hub.deploymentNumber(createdAddress),
            hub.isDeploying(createdAddress));

    hub.addTraceSection(
        new CreateSection(
            hub,
            contextChunk,
            // 3× own account
            new AccountChunk(
                creatorAddress, oldCreatorSnapshot, newCreatorSnapshot, false, 0, false),
            new AccountChunk(
                creatorAddress, oldCreatorSnapshot, newCreatorSnapshot, false, 0, false),
            new AccountChunk(
                creatorAddress, oldCreatorSnapshot, newCreatorSnapshot, false, 0, false),
            // 2×created account
            new AccountChunk(
                createdAddress, oldCreatedSnapshot, newCreatedSnapshot, false, 0, false),
            new AccountChunk(
                createdAddress, oldCreatedSnapshot, newCreatedSnapshot, false, 0, false)));
  }
}
