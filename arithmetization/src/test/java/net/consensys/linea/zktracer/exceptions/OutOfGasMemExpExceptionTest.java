package net.consensys.linea.zktracer.exceptions;

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.*;
import static net.consensys.linea.zktracer.module.hub.signals.TracedException.OUT_OF_GAS_EXCEPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;

import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UnitTestWatcher.class)
public class OutOfGasMemExpExceptionTest {
  /**
   * Trigger out of gas exception for a MSTORE operation with a gas limit that is too low to cover
   * the memory expansion
   */
  @Test
  void outOfGasExceptionMStore() {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(Bytes.fromHexString("0xFF")) // value
        .push(0) // offset
        .op(OpCode.MSTORE);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());

    long gasCostWithoutMStoreExpCost =
        (long) GAS_CONST_G_TRANSACTION
            + (long) 2 * GAS_CONST_G_VERY_LOW // 2 PUSH
            + GAS_CONST_G_MEMORY;

    bytecodeRunner.run(gasCostWithoutMStoreExpCost);
    assertEquals(
        OUT_OF_GAS_EXCEPTION,
        bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
  }

  /**
   * Trigger out of gas exception for a MSTORE8 operation with a gas limit that is too low to cover
   * the memory expansion
   */
  @Test
  void outOfGasExceptionMStore8() {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(Bytes.fromHexString("0xFFFF")) // value
        .push(0) // offset
        .op(OpCode.MSTORE8);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());

    long gasCostWithoutMStore8ExpCost =
        (long) GAS_CONST_G_TRANSACTION
            + (long) 2 * GAS_CONST_G_VERY_LOW // 2 PUSH
            + GAS_CONST_G_MEMORY;

    bytecodeRunner.run(gasCostWithoutMStore8ExpCost);
    assertEquals(
        OUT_OF_GAS_EXCEPTION,
        bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
  }

  /**
   * Trigger out of gas exception for a MLOAD operation with a gas limit that is too low to cover
   * its memory expansion
   */
  @Test
  void outOfGasExceptionMLoad() {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(Bytes.fromHexString("0xFF")) // value
        .push(0) // offset
        .op(OpCode.MSTORE);

    program
        .push(33) // value
        .op(OpCode.MLOAD);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());

    long gasCostWithoutMLoadExpCost =
        (long) GAS_CONST_G_TRANSACTION
            + (long) 2 * GAS_CONST_G_VERY_LOW // 2 PUSH
            + GAS_CONST_G_MEMORY // MSTORE
            + 3 // Dynamic cost for this MSTORE
            + GAS_CONST_G_VERY_LOW // 1 PUSH
            + GAS_CONST_G_MEMORY; // MLOAD

    bytecodeRunner.run(gasCostWithoutMLoadExpCost);
    assertEquals(
        OUT_OF_GAS_EXCEPTION,
        bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
  }
}
