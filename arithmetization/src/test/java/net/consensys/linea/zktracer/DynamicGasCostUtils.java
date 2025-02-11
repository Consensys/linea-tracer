package net.consensys.linea.zktracer;

import static net.consensys.linea.zktracer.module.blockdata.Trace.GAS_LIMIT_MAXIMUM;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_TRANSACTION;

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
   * Method to run only the part of the evm that calculates the gas cost of a frame for a message
   * call. The total gas cost is the gas cost of the frame + the gas cost of the transaction
   */
  public static long getGasCostForMessageCall(Bytes compiledCode) {
    final EVM evm = MainnetEVMs.frontier(EvmConfiguration.DEFAULT);

    final MessageFrame.Builder messageFrameBuilderDefaultValues =
        MessageFrameBuilderDefaultValues();

    final MessageFrame initialMessageFrame =
        messageFrameBuilderDefaultValues
            .type(MessageFrame.Type.MESSAGE_CALL)
            .initialGas(GAS_LIMIT_MAXIMUM)
            .code(evm.getCodeUncached(compiledCode))
            .build();

    // To run, the frame needs to be in the CODE_EXECUTING state
    initialMessageFrame.setState(MessageFrame.State.CODE_EXECUTING);
    evm.runToHalt(initialMessageFrame, null);

    long remainingGasCost = initialMessageFrame.getRemainingGas();

    return GAS_CONST_G_TRANSACTION + GAS_LIMIT_MAXIMUM - remainingGasCost;
  }

  private static MessageFrame.Builder MessageFrameBuilderDefaultValues() {
    return MessageFrame.builder()
        .worldUpdater(new SimpleWorld())
        .originator(Address.ZERO)
        .gasPrice(Wei.ONE)
        .blobGasPrice(Wei.ONE)
        .worldUpdater(new SimpleWorld())
        .blockValues(new SimpleBlockValues())
        .miningBeneficiary(Address.ZERO)
        .blockHashLookup((__, ___) -> Hash.ZERO)
        .address(Address.ZERO)
        .contract(Address.ZERO)
        .inputData(Bytes32.ZERO)
        .sender(Address.ZERO)
        .value(Wei.ZERO)
        .apparentValue(Wei.ZERO)
        .completer(messageFrame -> {});
  }
}
