/*
 * Copyright ConsenSys AG.
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

package net.consensys.linea.zktracer.module.rom;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import net.consensys.linea.zktracer.types.UnsignedByte;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.consensys.linea.zktracer.parquet.LocalWriteSupport;
import org.apache.parquet.schema.MessageTypeParser;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.function.Consumer;

/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
public class WriteSupport extends LocalWriteSupport<Trace> {

    private final static String SCHEMA = """
        message TraceLine {
         required BINARY ACC;
         required BOOLEAN CODESIZE_REACHED;
         required BINARY CODE_FRAGMENT_INDEX;
         required BINARY CODE_FRAGMENT_INDEX_INFTY;
         required BINARY CODE_SIZE;
         required BINARY COUNTER;
         required BINARY COUNTER_MAX;
         required BINARY COUNTER_PUSH;
         required BINARY INDEX;
         required BOOLEAN IS_PUSH;
         required BOOLEAN IS_PUSH_DATA;
         required BINARY LIMB;
         required BINARY OPCODE;
         required BINARY PADDED_BYTECODE_BYTE;
         required BINARY PROGRAMME_COUNTER;
         required BOOLEAN PUSH_FUNNEL_BIT;
         required BINARY PUSH_PARAMETER;
         required BINARY PUSH_VALUE_ACC;
         required BINARY PUSH_VALUE_HIGH;
         required BINARY PUSH_VALUE_LOW;
         required BOOLEAN VALID_JUMP_DESTINATION;
         required BINARY nBYTES;
         required BINARY nBYTES_ACC;
         }
""";
    public WriteSupport() {
        super(MessageTypeParser.parseMessageType(SCHEMA));
}

  @Override
  public void write(Trace trace) {
    recordConsumer.startMessage();

    writeBigIntegerField("ACC", 0, trace.acc());
    writeBooleanField("CODESIZE_REACHED", 1, trace.codesizeReached());
    writeBigIntegerField("CODE_FRAGMENT_INDEX", 2, trace.codeFragmentIndex());
    writeBigIntegerField("CODE_FRAGMENT_INDEX_INFTY", 3, trace.codeFragmentIndexInfty());
    writeBigIntegerField("CODE_SIZE", 4, trace.codeSize());
    writeBigIntegerField("COUNTER", 5, trace.counter());
    writeBigIntegerField("COUNTER_MAX", 6, trace.counterMax());
    writeBigIntegerField("COUNTER_PUSH", 7, trace.counterPush());
    writeBigIntegerField("INDEX", 8, trace.index());
    writeBooleanField("IS_PUSH", 9, trace.isPush());
    writeBooleanField("IS_PUSH_DATA", 10, trace.isPushData());
    writeBigIntegerField("LIMB", 11, trace.limb());
    writeBigIntegerField("nBYTES", 21, trace.nBytes());
    writeBigIntegerField("nBYTES_ACC", 22, trace.nBytesAcc());
    writeUnsignedByteField("OPCODE", 12, trace.opcode());
    writeUnsignedByteField("PADDED_BYTECODE_BYTE", 13, trace.paddedBytecodeByte());
    writeBigIntegerField("PROGRAMME_COUNTER", 14, trace.programmeCounter());
    writeBooleanField("PUSH_FUNNEL_BIT", 15, trace.pushFunnelBit());
    writeBigIntegerField("PUSH_PARAMETER", 16, trace.pushParameter());
    writeBigIntegerField("PUSH_VALUE_ACC", 17, trace.pushValueAcc());
    writeBigIntegerField("PUSH_VALUE_HIGH", 18, trace.pushValueHigh());
    writeBigIntegerField("PUSH_VALUE_LOW", 19, trace.pushValueLow());
    writeBooleanField("VALID_JUMP_DESTINATION", 20, trace.validJumpDestination());
    recordConsumer.endMessage();
  }
}









