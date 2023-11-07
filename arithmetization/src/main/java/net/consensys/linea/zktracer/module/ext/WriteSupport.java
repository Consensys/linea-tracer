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

package net.consensys.linea.zktracer.module.ext;

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
        import lombok.Builder;
        import lombok.Getter;
        import lombok.experimental.Accessors;
        /**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
        @Builder
        @Accessors(fluent = true)
        @Getter
public class WriteSupport extends LocalWriteSupport<Trace> {

    private final static String SCHEMA = """
        message TraceLine {
         required BINARY ACC_A_0;
         required BINARY ACC_A_1;
         required BINARY ACC_A_2;
         required BINARY ACC_A_3;
         required BINARY ACC_B_0;
         required BINARY ACC_B_1;
         required BINARY ACC_B_2;
         required BINARY ACC_B_3;
         required BINARY ACC_C_0;
         required BINARY ACC_C_1;
         required BINARY ACC_C_2;
         required BINARY ACC_C_3;
         required BINARY ACC_DELTA_0;
         required BINARY ACC_DELTA_1;
         required BINARY ACC_DELTA_2;
         required BINARY ACC_DELTA_3;
         required BINARY ACC_H_0;
         required BINARY ACC_H_1;
         required BINARY ACC_H_2;
         required BINARY ACC_H_3;
         required BINARY ACC_H_4;
         required BINARY ACC_H_5;
         required BINARY ACC_I_0;
         required BINARY ACC_I_1;
         required BINARY ACC_I_2;
         required BINARY ACC_I_3;
         required BINARY ACC_I_4;
         required BINARY ACC_I_5;
         required BINARY ACC_I_6;
         required BINARY ACC_J_0;
         required BINARY ACC_J_1;
         required BINARY ACC_J_2;
         required BINARY ACC_J_3;
         required BINARY ACC_J_4;
         required BINARY ACC_J_5;
         required BINARY ACC_J_6;
         required BINARY ACC_J_7;
         required BINARY ACC_Q_0;
         required BINARY ACC_Q_1;
         required BINARY ACC_Q_2;
         required BINARY ACC_Q_3;
         required BINARY ACC_Q_4;
         required BINARY ACC_Q_5;
         required BINARY ACC_Q_6;
         required BINARY ACC_Q_7;
         required BINARY ACC_R_0;
         required BINARY ACC_R_1;
         required BINARY ACC_R_2;
         required BINARY ACC_R_3;
         required BINARY ARG_1_HI;
         required BINARY ARG_1_LO;
         required BINARY ARG_2_HI;
         required BINARY ARG_2_LO;
         required BINARY ARG_3_HI;
         required BINARY ARG_3_LO;
         required BOOLEAN BIT_1;
         required BOOLEAN BIT_2;
         required BOOLEAN BIT_3;
         required BINARY BYTE_A_0;
         required BINARY BYTE_A_1;
         required BINARY BYTE_A_2;
         required BINARY BYTE_A_3;
         required BINARY BYTE_B_0;
         required BINARY BYTE_B_1;
         required BINARY BYTE_B_2;
         required BINARY BYTE_B_3;
         required BINARY BYTE_C_0;
         required BINARY BYTE_C_1;
         required BINARY BYTE_C_2;
         required BINARY BYTE_C_3;
         required BINARY BYTE_DELTA_0;
         required BINARY BYTE_DELTA_1;
         required BINARY BYTE_DELTA_2;
         required BINARY BYTE_DELTA_3;
         required BINARY BYTE_H_0;
         required BINARY BYTE_H_1;
         required BINARY BYTE_H_2;
         required BINARY BYTE_H_3;
         required BINARY BYTE_H_4;
         required BINARY BYTE_H_5;
         required BINARY BYTE_I_0;
         required BINARY BYTE_I_1;
         required BINARY BYTE_I_2;
         required BINARY BYTE_I_3;
         required BINARY BYTE_I_4;
         required BINARY BYTE_I_5;
         required BINARY BYTE_I_6;
         required BINARY BYTE_J_0;
         required BINARY BYTE_J_1;
         required BINARY BYTE_J_2;
         required BINARY BYTE_J_3;
         required BINARY BYTE_J_4;
         required BINARY BYTE_J_5;
         required BINARY BYTE_J_6;
         required BINARY BYTE_J_7;
         required BINARY BYTE_Q_0;
         required BINARY BYTE_Q_1;
         required BINARY BYTE_Q_2;
         required BINARY BYTE_Q_3;
         required BINARY BYTE_Q_4;
         required BINARY BYTE_Q_5;
         required BINARY BYTE_Q_6;
         required BINARY BYTE_Q_7;
         required BINARY BYTE_R_0;
         required BINARY BYTE_R_1;
         required BINARY BYTE_R_2;
         required BINARY BYTE_R_3;
         required BOOLEAN CMP;
         required BINARY CT;
         required BINARY INST;
         required BOOLEAN OF_H;
         required BOOLEAN OF_I;
         required BOOLEAN OF_J;
         required BOOLEAN OF_RES;
         required BOOLEAN OLI;
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

    writeBigIntegerField("ACC_A_0", 0, trace.accA0());
    writeBigIntegerField("ACC_A_1", 1, trace.accA1());
    writeBigIntegerField("ACC_A_2", 2, trace.accA2());
    writeBigIntegerField("ACC_A_3", 3, trace.accA3());
    writeBigIntegerField("ACC_B_0", 4, trace.accB0());
    writeBigIntegerField("ACC_B_1", 5, trace.accB1());
    writeBigIntegerField("ACC_B_2", 6, trace.accB2());
    writeBigIntegerField("ACC_B_3", 7, trace.accB3());
    writeBigIntegerField("ACC_C_0", 8, trace.accC0());
    writeBigIntegerField("ACC_C_1", 9, trace.accC1());
    writeBigIntegerField("ACC_C_2", 10, trace.accC2());
    writeBigIntegerField("ACC_C_3", 11, trace.accC3());
    writeBigIntegerField("ACC_DELTA_0", 12, trace.accDelta0());
    writeBigIntegerField("ACC_DELTA_1", 13, trace.accDelta1());
    writeBigIntegerField("ACC_DELTA_2", 14, trace.accDelta2());
    writeBigIntegerField("ACC_DELTA_3", 15, trace.accDelta3());
    writeBigIntegerField("ACC_H_0", 16, trace.accH0());
    writeBigIntegerField("ACC_H_1", 17, trace.accH1());
    writeBigIntegerField("ACC_H_2", 18, trace.accH2());
    writeBigIntegerField("ACC_H_3", 19, trace.accH3());
    writeBigIntegerField("ACC_H_4", 20, trace.accH4());
    writeBigIntegerField("ACC_H_5", 21, trace.accH5());
    writeBigIntegerField("ACC_I_0", 22, trace.accI0());
    writeBigIntegerField("ACC_I_1", 23, trace.accI1());
    writeBigIntegerField("ACC_I_2", 24, trace.accI2());
    writeBigIntegerField("ACC_I_3", 25, trace.accI3());
    writeBigIntegerField("ACC_I_4", 26, trace.accI4());
    writeBigIntegerField("ACC_I_5", 27, trace.accI5());
    writeBigIntegerField("ACC_I_6", 28, trace.accI6());
    writeBigIntegerField("ACC_J_0", 29, trace.accJ0());
    writeBigIntegerField("ACC_J_1", 30, trace.accJ1());
    writeBigIntegerField("ACC_J_2", 31, trace.accJ2());
    writeBigIntegerField("ACC_J_3", 32, trace.accJ3());
    writeBigIntegerField("ACC_J_4", 33, trace.accJ4());
    writeBigIntegerField("ACC_J_5", 34, trace.accJ5());
    writeBigIntegerField("ACC_J_6", 35, trace.accJ6());
    writeBigIntegerField("ACC_J_7", 36, trace.accJ7());
    writeBigIntegerField("ACC_Q_0", 37, trace.accQ0());
    writeBigIntegerField("ACC_Q_1", 38, trace.accQ1());
    writeBigIntegerField("ACC_Q_2", 39, trace.accQ2());
    writeBigIntegerField("ACC_Q_3", 40, trace.accQ3());
    writeBigIntegerField("ACC_Q_4", 41, trace.accQ4());
    writeBigIntegerField("ACC_Q_5", 42, trace.accQ5());
    writeBigIntegerField("ACC_Q_6", 43, trace.accQ6());
    writeBigIntegerField("ACC_Q_7", 44, trace.accQ7());
    writeBigIntegerField("ACC_R_0", 45, trace.accR0());
    writeBigIntegerField("ACC_R_1", 46, trace.accR1());
    writeBigIntegerField("ACC_R_2", 47, trace.accR2());
    writeBigIntegerField("ACC_R_3", 48, trace.accR3());
    writeBigIntegerField("ARG_1_HI", 49, trace.arg1Hi());
    writeBigIntegerField("ARG_1_LO", 50, trace.arg1Lo());
    writeBigIntegerField("ARG_2_HI", 51, trace.arg2Hi());
    writeBigIntegerField("ARG_2_LO", 52, trace.arg2Lo());
    writeBigIntegerField("ARG_3_HI", 53, trace.arg3Hi());
    writeBigIntegerField("ARG_3_LO", 54, trace.arg3Lo());
    writeBooleanField("BIT_1", 55, trace.bit1());
    writeBooleanField("BIT_2", 56, trace.bit2());
    writeBooleanField("BIT_3", 57, trace.bit3());
    writeUnsignedByteField("BYTE_A_0", 58, trace.byteA0());
    writeUnsignedByteField("BYTE_A_1", 59, trace.byteA1());
    writeUnsignedByteField("BYTE_A_2", 60, trace.byteA2());
    writeUnsignedByteField("BYTE_A_3", 61, trace.byteA3());
    writeUnsignedByteField("BYTE_B_0", 62, trace.byteB0());
    writeUnsignedByteField("BYTE_B_1", 63, trace.byteB1());
    writeUnsignedByteField("BYTE_B_2", 64, trace.byteB2());
    writeUnsignedByteField("BYTE_B_3", 65, trace.byteB3());
    writeUnsignedByteField("BYTE_C_0", 66, trace.byteC0());
    writeUnsignedByteField("BYTE_C_1", 67, trace.byteC1());
    writeUnsignedByteField("BYTE_C_2", 68, trace.byteC2());
    writeUnsignedByteField("BYTE_C_3", 69, trace.byteC3());
    writeUnsignedByteField("BYTE_DELTA_0", 70, trace.byteDelta0());
    writeUnsignedByteField("BYTE_DELTA_1", 71, trace.byteDelta1());
    writeUnsignedByteField("BYTE_DELTA_2", 72, trace.byteDelta2());
    writeUnsignedByteField("BYTE_DELTA_3", 73, trace.byteDelta3());
    writeUnsignedByteField("BYTE_H_0", 74, trace.byteH0());
    writeUnsignedByteField("BYTE_H_1", 75, trace.byteH1());
    writeUnsignedByteField("BYTE_H_2", 76, trace.byteH2());
    writeUnsignedByteField("BYTE_H_3", 77, trace.byteH3());
    writeUnsignedByteField("BYTE_H_4", 78, trace.byteH4());
    writeUnsignedByteField("BYTE_H_5", 79, trace.byteH5());
    writeUnsignedByteField("BYTE_I_0", 80, trace.byteI0());
    writeUnsignedByteField("BYTE_I_1", 81, trace.byteI1());
    writeUnsignedByteField("BYTE_I_2", 82, trace.byteI2());
    writeUnsignedByteField("BYTE_I_3", 83, trace.byteI3());
    writeUnsignedByteField("BYTE_I_4", 84, trace.byteI4());
    writeUnsignedByteField("BYTE_I_5", 85, trace.byteI5());
    writeUnsignedByteField("BYTE_I_6", 86, trace.byteI6());
    writeUnsignedByteField("BYTE_J_0", 87, trace.byteJ0());
    writeUnsignedByteField("BYTE_J_1", 88, trace.byteJ1());
    writeUnsignedByteField("BYTE_J_2", 89, trace.byteJ2());
    writeUnsignedByteField("BYTE_J_3", 90, trace.byteJ3());
    writeUnsignedByteField("BYTE_J_4", 91, trace.byteJ4());
    writeUnsignedByteField("BYTE_J_5", 92, trace.byteJ5());
    writeUnsignedByteField("BYTE_J_6", 93, trace.byteJ6());
    writeUnsignedByteField("BYTE_J_7", 94, trace.byteJ7());
    writeUnsignedByteField("BYTE_Q_0", 95, trace.byteQ0());
    writeUnsignedByteField("BYTE_Q_1", 96, trace.byteQ1());
    writeUnsignedByteField("BYTE_Q_2", 97, trace.byteQ2());
    writeUnsignedByteField("BYTE_Q_3", 98, trace.byteQ3());
    writeUnsignedByteField("BYTE_Q_4", 99, trace.byteQ4());
    writeUnsignedByteField("BYTE_Q_5", 100, trace.byteQ5());
    writeUnsignedByteField("BYTE_Q_6", 101, trace.byteQ6());
    writeUnsignedByteField("BYTE_Q_7", 102, trace.byteQ7());
    writeUnsignedByteField("BYTE_R_0", 103, trace.byteR0());
    writeUnsignedByteField("BYTE_R_1", 104, trace.byteR1());
    writeUnsignedByteField("BYTE_R_2", 105, trace.byteR2());
    writeUnsignedByteField("BYTE_R_3", 106, trace.byteR3());
    writeBooleanField("CMP", 107, trace.cmp());
    writeBigIntegerField("CT", 108, trace.ct());
    writeBigIntegerField("INST", 109, trace.inst());
    writeBooleanField("OF_H", 110, trace.ofH());
    writeBooleanField("OF_I", 111, trace.ofI());
    writeBooleanField("OF_J", 112, trace.ofJ());
    writeBooleanField("OF_RES", 113, trace.ofRes());
    writeBooleanField("OLI", 114, trace.oli());
    writeBigIntegerField("RES_HI", 115, trace.resHi());
    writeBigIntegerField("RES_LO", 116, trace.resLo());
    writeBigIntegerField("STAMP", 117, trace.stamp());
    recordConsumer.endMessage();
  }
}









