package net.consensys.linea.zktracer.exceptions;

import static net.consensys.linea.zktracer.module.hub.signals.TracedException.OUT_OF_GAS_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Wei;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@ExtendWith(UnitTestWatcher.class)
public class OutOfGasMemExpExceptionTest {
  /**
   * Trigger out of gas exception for a MSTORE operation with a gas limit that is too low to cover
   * the memory expansion
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionMStore(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(Bytes.fromHexString("0xFF")) // value
        .push(0) // offset
        .op(OpCode.MSTORE);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);
    if (cornerCase == -1) {
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    } else {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    }
  }

  /**
   * Trigger out of gas exception for a MSTORE8 operation with a gas limit that is too low to cover
   * the memory expansion
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionMStore8(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(Bytes.fromHexString("0xFF")) // value
        .push(0) // offset
        .op(OpCode.MSTORE8);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);
    if (cornerCase == -1) {
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    } else {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    }
  }

  /**
   * Trigger out of gas exception for a MLOAD operation with a gas limit that is too low to cover
   * its memory expansion
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionMLoad(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(
            Bytes.fromHexString(
                "0x7FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) // value
        .push(0) // offset
        .op(OpCode.MSTORE);

    program
        .push(17) // value
        .op(OpCode.MLOAD);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);
    if (cornerCase == -1) {
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    } else {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionCallDataCopy(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    Bytes calldata =
        Bytes.fromHexString("0x7FFFFFFFFFFFFF00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
    program
        .push(31) // value
        .push(1) // offset
        .push(2) // offset
        .op(OpCode.CALLDATACOPY);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost =
        bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of(), calldata);

    bytecodeRunner.run(Wei.fromEth(1), gasCost + cornerCase, List.of(), calldata);
    if (cornerCase == -1) {
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    } else {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionCodeCopy(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(Bytes.fromHexString("0xFA")) // value
        .push(0) // offset
        .op(OpCode.MSTORE)
        .push(3) // size
        .push(0) // offset
        .push(32) // destoffset
        .op(OpCode.CODECOPY); // Should copy 60fa60 (first 3 bytes of the code)

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);
    if (cornerCase == -1) {
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    } else {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionExtCodeCopy(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        // constructor
        .push(
            Bytes.fromHexString(
                "0x7FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) // value
        .push(0) // offset
        .op(OpCode.MSTORE)
        .push(
            Bytes.fromHexString(
                "0xFF60005260206000F30000000000000000000000000000000000000000000000")) // value
        .push(32) // offset
        .op(OpCode.MSTORE)
        // Create the contract
        .push(41)
        .push(0)
        .push(0)
        .op(OpCode.CREATE)
        .push(32) // size
        .push(0) // offset
        .push(33) // destoffset
        .op(OpCode.DUP4) //
        .op(OpCode.EXTCODECOPY);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);
    if (cornerCase == -1) {
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    } else {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionReturn(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(
            Bytes.fromHexString(
                "0xF00000000000000000000000000000000000000000000000000000000000FFAA")) // value
        .push(0) // offset
        .op(OpCode.MSTORE)
        .push(3) // value
        .push(30) // offset
        .op(OpCode.RETURN);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);
    if (cornerCase == -1) {
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    } else {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionReturnDataCopy(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        // 1. Constructor
        .push(
            Bytes.fromHexString(
                "0x7F7FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) // value
        .push(0) // offset
        .op(OpCode.MSTORE)
        .push(
            Bytes.fromHexString(
                "0xFF6000527FFF60005260206000F3000000000000000000000000000000000000")) // value
        .push(32) // offset
        .op(OpCode.MSTORE)
        .push(
            Bytes.fromHexString(
                "0x000000000060205260296000F300000000000000000000000000000000000000")) // value
        .push(64) // offset
        .op(OpCode.MSTORE)
        // 2. Create the contract
        .push(77) // size
        .push(0)
        .push(0)
        .op(OpCode.CREATE)
        // 3. Execute static call
        .push(32) // byte size from return data
        .push(0) // retOffset
        .push(0) // byte size calldata
        .push(0) // argsOffset
        .op(OpCode.DUP5) // Address of the contract deployed above
        .push(Bytes.fromHexString("0xFFFFFFFF")) // gas
        .op(OpCode.STATICCALL)
        // 4. Clean the stack
        .op(OpCode.POP)
        .op(OpCode.POP)
        // 5. Return data copy
        .push(32)
        .push(0)
        .push(65)
        .op(OpCode.RETURNDATACOPY);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);
    if (cornerCase == -1) {
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    } else {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionRevert(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(
            Bytes.fromHexString(
                "0xF00000000000000000000000000000000000000000000000000000000000FFAA")) // value
        .push(0) // offset
        .op(OpCode.MSTORE)
        .push(3) // value
        .push(30) // offset
        .op(OpCode.REVERT);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);
    if (cornerCase == -1) {
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    } else {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {-6420, -6419, -6418, 100, 101, 102})
  /*
  Deployment code: "0x7F7EFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF60005260206000F3"
  1. OOGX for CREATE before deployment: remove 6400 (depositFee) + deployment code exec cost (18)
  2. OOGX for CREATE after deployment: enough gas for child creation, but not enough to complete deployment code or deposit
  3. No OOGX for CREATE after deployment: add 1/64th of 6418 to gasCost to account for gasAvailableForChildCreate
   */
  void outOfGasExceptionCreate(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        // constructor
        .push(
            Bytes.fromHexString(
                "0x7F7EFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) // value
        .push(0) // offset
        .op(OpCode.MSTORE)
        .push(
            Bytes.fromHexString(
                "0xFF60005260206000F30000000000000000000000000000000000000000000000")) // value
        .push(32) // offset
        .op(OpCode.MSTORE)
        // Create the contract
        .push(41)
        .push(0)
        .push(0)
        .op(OpCode.CREATE); // No constructor so code executed and runtime code set to return value

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);

    if (cornerCase <= -6419) {
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    } else if (cornerCase <= 100) {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection(2).commonValues.tracedException());
    } else {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection(2).commonValues.tracedException());
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionLog0(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(Bytes.fromHexString("0x7F")) // value
        .push(0) // offset
        .op(OpCode.MSTORE)
        .push(32) // size
        .push(1) // offset to trigger mem expansion
        .op(OpCode.LOG0);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);
    if (cornerCase == -1) {
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    } else {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    }
  }

  // TODO: check log with debug
  @ParameterizedTest
  @ValueSource(ints = {0})
  void outOfGasExceptionLog1(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(Bytes.fromHexString("0x7F")) // value
        .push(0) // offset
        .op(OpCode.MSTORE)
        .push(
            Bytes.fromHexString(
                "0x1FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) // Topic 1
        .push(32) // size
        .push(1) // offset to trigger mem expansion
        .op(OpCode.LOG1);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);
    if (cornerCase == -1) {
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    } else {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionLog2(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(Bytes.fromHexString("0x7F")) // value
        .push(0) // offset
        .op(OpCode.MSTORE)
        .push(
            Bytes.fromHexString(
                "0x2FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) // Topic 2
        .push(
            Bytes.fromHexString(
                "0x1FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) // Topic 1
        .push(32) // size
        .push(1) // offset to trigger mem expansion
        .op(OpCode.LOG2);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);
    if (cornerCase == -1) {
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    } else {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionLog3(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(Bytes.fromHexString("0x7F")) // value
        .push(0) // offset
        .op(OpCode.MSTORE)
        .push(
            Bytes.fromHexString(
                "0x3FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) // Topic 3
        .push(
            Bytes.fromHexString(
                "0x2FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) // Topic 2
        .push(
            Bytes.fromHexString(
                "0x1FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) // Topic 1
        .push(32) // size
        .push(1) // offset to trigger mem expansion
        .op(OpCode.LOG3);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);
    if (cornerCase == -1) {
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    } else {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionLog4(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(Bytes.fromHexString("0x7F")) // value
        .push(0) // offset
        .op(OpCode.MSTORE)
        .push(
            Bytes.fromHexString(
                "0x4FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) // Topic 4
        .push(
            Bytes.fromHexString(
                "0x3FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) // Topic 3
        .push(
            Bytes.fromHexString(
                "0x2FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) // Topic 2
        .push(
            Bytes.fromHexString(
                "0x1FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) // Topic 1
        .push(32) // size
        .push(1) // offset to trigger mem expansion
        .op(OpCode.LOG4);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);
    if (cornerCase == -1) {
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    } else {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    }
  }
}
