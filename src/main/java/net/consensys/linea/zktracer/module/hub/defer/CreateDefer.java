package net.consensys.linea.zktracer.module.hub.defer;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.chunks.AccountFragment;
import net.consensys.linea.zktracer.module.hub.chunks.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.chunks.ContextFragment;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.operation.Operation;

public record CreateDefer(
    AccountSnapshot oldCreatorSnapshot,
    AccountSnapshot oldCreatedSnapshot,
    ContextFragment preContext)
    implements PostExecDefer, NextContextDefer {
  // PostExec defer
  @Override
  public void run(Hub hub, MessageFrame frame, Operation.OperationResult operationResult) {
    // The post-exec behaves identically to the new context defer; albeit with different global
    // state
    this.run(hub, frame);
  }

  // Context defer
  @Override
  public void run(Hub hub, MessageFrame frame) {
    Address creatorAddress = oldCreatorSnapshot.address();
    AccountSnapshot newCreatorSnapshot =
        AccountSnapshot.fromAccount(
            frame.getWorldUpdater().getAccount(creatorAddress),
            true,
            hub.deploymentNumber(creatorAddress),
            hub.isDeploying(creatorAddress));

    Address createdAddress = oldCreatedSnapshot.address();
    AccountSnapshot newCreatedSnapshot =
        AccountSnapshot.fromAccount(
            frame.getWorldUpdater().getAccount(createdAddress),
            true,
            hub.deploymentNumber(createdAddress),
            hub.isDeploying(createdAddress));

    hub.addTraceSection(
        new net.consensys.linea.zktracer.module.hub.section.CreateSection(
            hub,
            preContext,
            // 3× own account
            new AccountFragment(oldCreatorSnapshot, newCreatorSnapshot, false, 0, false),
            new AccountFragment(oldCreatorSnapshot, newCreatorSnapshot, false, 0, false),
            new AccountFragment(oldCreatorSnapshot, newCreatorSnapshot, false, 0, false),
            // 2×created account
            new AccountFragment(oldCreatedSnapshot, newCreatedSnapshot, false, 0, true),
            new AccountFragment(oldCreatedSnapshot, newCreatedSnapshot, false, 0, false)));
  }
}
