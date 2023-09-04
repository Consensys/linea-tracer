package net.consensys.linea.zktracer.module.hub.defer;

import net.consensys.linea.zktracer.module.hub.Hub;
import org.hyperledger.besu.evm.frame.MessageFrame;

public interface NextContextDefer {
  void run(Hub hub, MessageFrame frame);
}
