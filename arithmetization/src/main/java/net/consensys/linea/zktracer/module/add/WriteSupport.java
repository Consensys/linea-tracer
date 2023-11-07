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

package net.consensys.linea.zktracer.module.add;

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
         required BINARY ACC_1;
         required BINARY ACC_2;
         required BINARY ARG_1_HI;
         required BINARY ARG_1_LO;
         required BINARY ARG_2_HI;
         required BINARY ARG_2_LO;
         required BINARY BYTE_1;
         required BINARY BYTE_2;
         required BINARY CT;
         required BINARY INST;
         required BOOLEAN OVERFLOW;
         required BINARY RES_HI;
         required BINARY RES_LO;
         required BINARY STAMP;
         }
""";
    public WriteSupport() {
        super(MessageTypeParser.parseMessageType(SCHEMA));
    }

    @Override
    public void write(Trace trace) {
        recordConsumer.startMessage();

        writeBigIntegerField("ACC_1", 0, trace.acc1());
        writeBigIntegerField("ACC_2", 1, trace.acc2());
        writeBigIntegerField("ARG_1_HI", 2, trace.arg1Hi());
        writeBigIntegerField("ARG_1_LO", 3, trace.arg1Lo());
        writeBigIntegerField("ARG_2_HI", 4, trace.arg2Hi());
        writeBigIntegerField("ARG_2_LO", 5, trace.arg2Lo());
        writeUnsignedByteField("BYTE_1", 6, trace.byte1());
        writeUnsignedByteField("BYTE_2", 7, trace.byte2());
        writeBigIntegerField("CT", 8, trace.ct());
        writeBigIntegerField("INST", 9, trace.inst());
        writeBooleanField("OVERFLOW", 10, trace.overflow());
        writeBigIntegerField("RES_HI", 11, trace.resHi());
        writeBigIntegerField("RES_LO", 12, trace.resLo());
        writeBigIntegerField("STAMP", 13, trace.stamp());
        recordConsumer.endMessage();
    }
}









