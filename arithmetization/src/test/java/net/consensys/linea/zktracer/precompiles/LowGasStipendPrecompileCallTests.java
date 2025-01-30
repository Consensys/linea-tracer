package net.consensys.linea.zktracer.precompiles;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class LowGasStipendPrecompileCallTests {

  @ParameterizedTest
  @MethodSource("lowGasStipendPrecompileCallTestSource")
  void lowGasStipendPrecompileCallTest(Address precompileAddress) {
    final Bytes bytecode =
        BytecodeCompiler.newProgram()
            .push(0) // retSize
            .push(0) // retOffset
            .push(0) // argsSize
            .push(0) // argsOffset
            .push(0) // value
            .push(precompileAddress) // address
            .push(1) // gas, that is deliberately insufficient
            .op(OpCode.CALL)
            .compile();
    final BytecodeRunner bytecodeRunner = BytecodeRunner.of(bytecode);
    bytecodeRunner.run(1_000_000L); // huge gas limit
    final Hub hub = bytecodeRunner.getHub();
    // Operation with index 1 is the processing of the precompile contract
    boolean insufficientGasForPrecompile = false;
    for (int i = 0; i < hub.oob().operations().size(); i++) {
      final OobOperation operation = hub.oob().operations().get(i);
      insufficientGasForPrecompile = insufficientGasForPrecompile || operation.isInsufficientGasForPrecompile();
    }
    assertTrue(insufficientGasForPrecompile);
  }

  static Stream<Arguments> lowGasStipendPrecompileCallTestSource() {
    List<Arguments> arguments = new ArrayList<>();
    arguments.add(Arguments.of(Address.ECREC));
    arguments.add(Arguments.of(Address.SHA256));
    arguments.add(Arguments.of(Address.RIPEMD160));
    arguments.add(Arguments.of(Address.ID));
    arguments.add(Arguments.of(Address.MODEXP));
    arguments.add(Arguments.of(Address.ALTBN128_ADD));
    arguments.add(Arguments.of(Address.ALTBN128_MUL));
    arguments.add(Arguments.of(Address.ALTBN128_PAIRING));
    arguments.add(Arguments.of(Address.BLAKE2B_F_COMPRESSION));
    return arguments.stream();
  }
}
