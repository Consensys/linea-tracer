package net.consensys.linea.zktracer.module.hub;

import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.testutils.TestCodeExecutor;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.frame.MessageFrame;

public class TwoPlusTwo extends TestCodeExecutor {
  @Override
  public void setupFrame(MessageFrame frame) {
    frame.pushStackItem(Bytes.of(2));
    frame.pushStackItem(Bytes.of(34));
  }

  public Bytes getBytecode() {
    return Bytes.of(
        OpCode.ADD.byteValue());
  }
}
