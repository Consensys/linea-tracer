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

package net.consensys.linea.zktracer.module.mul;

import net.consensys.linea.zktracer.parquet.LocalWriteSupport;
import org.apache.parquet.schema.MessageTypeParser;

/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
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
         required BINARY ACC_H_0;
         required BINARY ACC_H_1;
         required BINARY ACC_H_2;
         required BINARY ACC_H_3;
         required BINARY ARG_1_HI;
         required BINARY ARG_1_LO;
         required BINARY ARG_2_HI;
         required BINARY ARG_2_LO;
         required BINARY BIT_NUM;
         required BOOLEAN BITS;
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
         required BINARY BYTE_H_0;
         required BINARY BYTE_H_1;
         required BINARY BYTE_H_2;
         required BINARY BYTE_H_3;
         required BINARY COUNTER;
         required BOOLEAN EXPONENT_BIT;
         required BINARY EXPONENT_BIT_ACCUMULATOR;
         required BOOLEAN EXPONENT_BIT_SOURCE;
         required BINARY INSTRUCTION;
         required BINARY MUL_STAMP;
         required BOOLEAN OLI;
         required BOOLEAN RESULT_VANISHES;
         required BINARY RES_HI;
         required BINARY RES_LO;
         required BOOLEAN SQUARE_AND_MULTIPLY;
         required BOOLEAN TINY_BASE;
         required BOOLEAN TINY_EXPONENT;
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
    writeBigIntegerField("ACC_H_0", 12, trace.accH0());
    writeBigIntegerField("ACC_H_1", 13, trace.accH1());
    writeBigIntegerField("ACC_H_2", 14, trace.accH2());
    writeBigIntegerField("ACC_H_3", 15, trace.accH3());
    writeBigIntegerField("ARG_1_HI", 16, trace.arg1Hi());
    writeBigIntegerField("ARG_1_LO", 17, trace.arg1Lo());
    writeBigIntegerField("ARG_2_HI", 18, trace.arg2Hi());
    writeBigIntegerField("ARG_2_LO", 19, trace.arg2Lo());
    writeBigIntegerField("BIT_NUM", 20, trace.bitNum());
    writeBooleanField("BITS", 21, trace.bits());
    writeUnsignedByteField("BYTE_A_0", 22, trace.byteA0());
    writeUnsignedByteField("BYTE_A_1", 23, trace.byteA1());
    writeUnsignedByteField("BYTE_A_2", 24, trace.byteA2());
    writeUnsignedByteField("BYTE_A_3", 25, trace.byteA3());
    writeUnsignedByteField("BYTE_B_0", 26, trace.byteB0());
    writeUnsignedByteField("BYTE_B_1", 27, trace.byteB1());
    writeUnsignedByteField("BYTE_B_2", 28, trace.byteB2());
    writeUnsignedByteField("BYTE_B_3", 29, trace.byteB3());
    writeUnsignedByteField("BYTE_C_0", 30, trace.byteC0());
    writeUnsignedByteField("BYTE_C_1", 31, trace.byteC1());
    writeUnsignedByteField("BYTE_C_2", 32, trace.byteC2());
    writeUnsignedByteField("BYTE_C_3", 33, trace.byteC3());
    writeUnsignedByteField("BYTE_H_0", 34, trace.byteH0());
    writeUnsignedByteField("BYTE_H_1", 35, trace.byteH1());
    writeUnsignedByteField("BYTE_H_2", 36, trace.byteH2());
    writeUnsignedByteField("BYTE_H_3", 37, trace.byteH3());
    writeBigIntegerField("COUNTER", 38, trace.counter());
    writeBooleanField("EXPONENT_BIT", 39, trace.exponentBit());
    writeBigIntegerField("EXPONENT_BIT_ACCUMULATOR", 40, trace.exponentBitAccumulator());
    writeBooleanField("EXPONENT_BIT_SOURCE", 41, trace.exponentBitSource());
    writeBigIntegerField("INSTRUCTION", 42, trace.instruction());
    writeBigIntegerField("MUL_STAMP", 43, trace.mulStamp());
    writeBooleanField("OLI", 44, trace.oli());
    writeBooleanField("RESULT_VANISHES", 45, trace.resultVanishes());
    writeBigIntegerField("RES_HI", 46, trace.resHi());
    writeBigIntegerField("RES_LO", 47, trace.resLo());
    writeBooleanField("SQUARE_AND_MULTIPLY", 48, trace.squareAndMultiply());
    writeBooleanField("TINY_BASE", 49, trace.tinyBase());
    writeBooleanField("TINY_EXPONENT", 50, trace.tinyExponent());
    recordConsumer.endMessage();
  }
}









