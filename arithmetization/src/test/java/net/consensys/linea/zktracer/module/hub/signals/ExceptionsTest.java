package net.consensys.linea.zktracer.module.hub.signals;

import java.util.Set;

import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.OpCodes;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

/** ensure that OpCode are configured properly */
class ExceptionsTest {

  @ParameterizedTest
  @EnumSource(OpCode.class)
  void notStaticContextNoFault(OpCode opCode) {
    MessageFrame frame = Mockito.mock(MessageFrame.class);
    Mockito.when(frame.isStatic()).thenReturn(false);
    Assertions.assertFalse(Exceptions.isStaticFault(frame, OpCodes.of(opCode)));
  }

  @ParameterizedTest
  @EnumSource(OpCode.class)
  void staticContextNoFaultOnCall(OpCode opCode) {
    MessageFrame frame = Mockito.mock(MessageFrame.class);
    Mockito.when(frame.isStatic()).thenReturn(true);
    Mockito.when(frame.stackSize()).thenReturn(7);
    Mockito.when(frame.getStackItem(2)).thenReturn(Bytes.ofUnsignedShort(0));
    Assertions.assertEquals(
        Set.of(
                OpCode.SSTORE,
                OpCode.LOG0,
                OpCode.LOG1,
                OpCode.LOG2,
                OpCode.LOG3,
                OpCode.LOG4,
                OpCode.CREATE,
                OpCode.CREATE2,
                OpCode.SELFDESTRUCT)
            .contains(opCode),
        Exceptions.isStaticFault(frame, OpCodes.of(opCode)));
  }

  @ParameterizedTest
  @EnumSource(OpCode.class)
  void staticContextFaultOnCall(OpCode opCode) {
    MessageFrame frame = Mockito.mock(MessageFrame.class);
    Mockito.when(frame.isStatic()).thenReturn(true);
    Mockito.when(frame.stackSize()).thenReturn(7);
    Mockito.when(frame.getStackItem(2)).thenReturn(Bytes.ofUnsignedShort(1));
    Assertions.assertEquals(
        Set.of(
                OpCode.SSTORE,
                OpCode.LOG0,
                OpCode.LOG1,
                OpCode.LOG2,
                OpCode.LOG3,
                OpCode.LOG4,
                OpCode.CREATE,
                OpCode.CREATE2,
                OpCode.SELFDESTRUCT,
                OpCode.CALL)
            .contains(opCode),
        Exceptions.isStaticFault(frame, OpCodes.of(opCode)));
  }
}
