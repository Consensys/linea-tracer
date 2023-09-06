package net.consensys.linea.zktracer.module.hub.defer;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.chunks.AccountFragment;
import net.consensys.linea.zktracer.module.hub.chunks.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.chunks.ContextFragment;
import net.consensys.linea.zktracer.module.hub.section.CallSection;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.operation.Operation;

public record CallDefer(
    AccountSnapshot oldCallerSnapshot,
    AccountSnapshot oldCalledSnapshot,
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
    Address callerAddress = oldCallerSnapshot.address();
    AccountSnapshot newCallerSnapshot =
        AccountSnapshot.fromAccount(
            frame.getWorldUpdater().getAccount(callerAddress),
            true,
            hub.deploymentNumber(callerAddress),
            hub.isDeploying(callerAddress));

    Address calledAddress = oldCalledSnapshot.address();
    AccountSnapshot newCalledSnapshot =
        AccountSnapshot.fromAccount(
            frame.getWorldUpdater().getAccount(calledAddress),
            true,
            hub.deploymentNumber(calledAddress),
            hub.isDeploying(calledAddress));

    hub.addTraceSection(
        new CallSection(
            hub,
            preContext,
            // 2× own account
            new AccountFragment(oldCallerSnapshot, newCallerSnapshot, false, 0, false),
            new AccountFragment(oldCallerSnapshot, newCallerSnapshot, false, 0, false),
            // 2× target code account
            new AccountFragment(oldCalledSnapshot, newCalledSnapshot, false, 0, false),
            new AccountFragment(oldCalledSnapshot, newCalledSnapshot, false, 0, false),
            // context -- only if not precompile
            new ContextFragment(hub.getCallStack(), hub.currentFrame(), false)));
  }
}
