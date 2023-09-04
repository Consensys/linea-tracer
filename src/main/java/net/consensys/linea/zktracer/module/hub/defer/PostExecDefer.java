package net.consensys.linea.zktracer.module.hub.defer;

import net.consensys.linea.zktracer.module.hub.Hub;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.operation.Operation;

public interface PostExecDefer {
  void run(Hub hub, MessageFrame frame, Operation.OperationResult operationResult);
}
