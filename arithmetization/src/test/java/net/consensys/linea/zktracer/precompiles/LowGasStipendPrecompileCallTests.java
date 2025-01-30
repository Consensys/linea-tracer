package net.consensys.linea.zktracer.precompiles;

import static net.consensys.linea.zktracer.module.blake2fmodexpdata.BlakeModexpDataOperation.BLAKE2f_HASH_OUTPUT_SIZE;
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
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LowGasStipendPrecompileCallTests {

  @ParameterizedTest
  @MethodSource("lowGasStipendPrecompileCallTestSource")
  void lowGasStipendPrecompileCallTest(Address precompileAddress, boolean isZeroArgument) {
    int argsSize = 1;
    if (precompileAddress == ALTBN128_PAIRING) {
      argsSize = 192;
    } else if (precompileAddress == BLAKE2B_F_COMPRESSION) {
      argsSize = 213;
    } else {
      argsSize = isZeroArgument ? 0 : 1;
    }

    final BytecodeCompiler program = BytecodeCompiler.newProgram();

    if (precompileAddress == BLAKE2B_F_COMPRESSION) {
      // TODO: do we need to set the retSize to the correct value? Look at blakeTests? Do we need pass all arguments?
      program
          .push(10) // value = r for Blake call
          .push(isZeroArgument ? 0 : 1) // offset
          .op(OpCode.MSTORE8);
    }

    program
        .push(precompileAddress == Address.BLAKE2B_F_COMPRESSION ? BLAKE2f_HASH_OUTPUT_SIZE : (isZeroArgument ? 0 : 1)) // retSize
        .push(isZeroArgument ? 0 : 1) // retOffset
        .push(isZeroArgument ? 0 : argsSize) // argsSize
        .push(isZeroArgument ? 0 : 1) // argsOffset
        .push(0) // value
        .push(precompileAddress) // address
        .push(1) // gas, that is deliberately insufficient
        .op(OpCode.CALL)
        .compile();
    final BytecodeRunner bytecodeRunner = BytecodeRunner.of(program);
    bytecodeRunner.run(1_000_000L); // huge gas limit
    final Hub hub = bytecodeRunner.getHub();
    // Operation with index 1 is the processing of the precompile contract
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
