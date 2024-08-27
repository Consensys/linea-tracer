/*
 * Copyright Consensys Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

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
