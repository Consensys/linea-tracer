package net.consensys.linea.zktracer.precompiles;

import static org.hyperledger.besu.datatypes.Address.ALTBN128_PAIRING;
import static org.hyperledger.besu.datatypes.Address.BLAKE2B_F_COMPRESSION;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.oob.OobOperation;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.datatypes.Address;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LowGasStipendPrecompileCallTests {

  @ParameterizedTest
  @MethodSource("lowGasStipendPrecompileCallTestSource")
  void lowGasStipendPrecompileCallTest(Address precompileAddress, boolean isZeroArgument) {
    final BytecodeCompiler program = BytecodeCompiler.newProgram();

    // In order to actually trigger the insufficient we need to:
    // - Set a specific args size from BLAKE2F AND EC_PAIRING
    // - Set the r value of BLAKE2F to something greater than the gas stipend
    int argsSize;
    if (precompileAddress == BLAKE2B_F_COMPRESSION) {
      program
          .push(0xab) // r (as r is 4 bytes, it is padded to 0xab000000)
          .push(isZeroArgument ? 0 : 1) // offset
          .op(OpCode.MSTORE8);
      argsSize = 213;
    } else if (precompileAddress == ALTBN128_PAIRING) {
      argsSize = 192;
    } else {
      argsSize = isZeroArgument ? 0 : 1;
    }

    // Common program for all precompile calls
    program
        .push(isZeroArgument ? 0 : 1) // retSize
        .push(isZeroArgument ? 0 : 1) // retOffset
        .push(argsSize) // argsSize
        .push(isZeroArgument ? 0 : 1) // argsOffset
        .push(0) // value
        .push(precompileAddress) // address
        .push(1) // gas, that is deliberately insufficient
        .op(OpCode.CALL)
        .compile();
    final BytecodeRunner bytecodeRunner = BytecodeRunner.of(program);
    bytecodeRunner.run(1_000_000L); // huge gas limit
    final Hub hub = bytecodeRunner.getHub();

    // Here we check if OOB detects the insufficient gas for the precompile call
    // As the number of OOB operation required is variable, we iterate over all the operations
    boolean insufficientGasForPrecompile = false;
    for (int i = 0; i < hub.oob().operations().size(); i++) {
      final OobOperation operation = hub.oob().operations().get(i);
      insufficientGasForPrecompile =
          insufficientGasForPrecompile || operation.isInsufficientGasForPrecompile();
    }
    assertTrue(insufficientGasForPrecompile);
  }

  static Stream<Arguments> lowGasStipendPrecompileCallTestSource() {
    List<Arguments> arguments = new ArrayList<>();
    for (boolean isZeroArgument : new boolean[] {true, false}) {
      arguments.add(Arguments.of(Address.ECREC, isZeroArgument));
      arguments.add(Arguments.of(Address.SHA256, isZeroArgument));
      arguments.add(Arguments.of(Address.RIPEMD160, isZeroArgument));
      arguments.add(Arguments.of(Address.ID, isZeroArgument));
      arguments.add(Arguments.of(Address.MODEXP, isZeroArgument));
      arguments.add(Arguments.of(Address.ALTBN128_ADD, isZeroArgument));
      arguments.add(Arguments.of(Address.ALTBN128_MUL, isZeroArgument));
      arguments.add(Arguments.of(Address.ALTBN128_PAIRING, isZeroArgument));
      arguments.add(Arguments.of(BLAKE2B_F_COMPRESSION, isZeroArgument));
    }
    return arguments.stream();
  }
}
