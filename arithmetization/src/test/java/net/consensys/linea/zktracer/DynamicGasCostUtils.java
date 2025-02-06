package net.consensys.linea.zktracer;

import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.evm.EVM;
import org.hyperledger.besu.evm.MainnetEVMs;
import org.hyperledger.besu.evm.fluent.SimpleBlockValues;
import org.hyperledger.besu.evm.fluent.SimpleWorld;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.EvmConfiguration;

public class DynamicGasCostUtils {
  /**
   * Try method to run only the part of the evm that calculates the gas cost of a frame Assumption:
   * the total gas cost of the frame is < 10000 gas - this does not include the gas cost of the
   * transction (21000)
   */
  public static long getDynamicGasCost(Bytes compiledCode) {
    final EVM evm = MainnetEVMs.frontier(EvmConfiguration.DEFAULT);

    final MessageFrame initialMessageFrame =
        MessageFrame.builder()
            .worldUpdater(new SimpleWorld())
            .originator(Address.ZERO)
            .gasPrice(Wei.ONE)
            .blobGasPrice(Wei.ONE)
            .worldUpdater(new SimpleWorld())
            .blockValues(new SimpleBlockValues())
            .miningBeneficiary(Address.ZERO)
            .blockHashLookup((__, ___) -> Hash.ZERO)
            .type(MessageFrame.Type.MESSAGE_CALL)
            .initialGas(10000)
            .address(Address.ZERO)
            .contract(Address.ZERO)
            .inputData(Bytes32.ZERO)
            .sender(Address.ZERO)
            .value(Wei.ZERO)
            .apparentValue(Wei.ZERO)
            .code(evm.getCodeUncached(compiledCode))
            .completer(messageFrame -> {})
            .build();
    initialMessageFrame.setState(MessageFrame.State.CODE_EXECUTING);
    evm.runToHalt(initialMessageFrame, null);
    long remaining = initialMessageFrame.getRemainingGas();
    return 10000 - remaining;
  }
}
