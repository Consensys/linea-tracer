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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.orc.OrcFile;
import org.apache.orc.TypeDescription;
import org.apache.orc.Writer;

import java.io.IOException;
/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
public class ORCWriter {

        public static Writer getWriter(String fileName) throws IOException {
                Configuration conf = new Configuration();
                conf.set("fs.hdfs.impl","org.apache.hadoop.hdfs.DistributedFileSystem");
                conf.set("fs.file.impl", "org.apache.hadoop.fs.LocalFileSystem");
                TypeDescription schema = TypeDescription.createStruct()
                        .addField("ACC_A_0", getTypeDescription("BigInteger"))
                        .addField("ACC_A_1", getTypeDescription("BigInteger"))
                        .addField("ACC_A_2", getTypeDescription("BigInteger"))
                        .addField("ACC_A_3", getTypeDescription("BigInteger"))
                        .addField("ACC_B_0", getTypeDescription("BigInteger"))
                        .addField("ACC_B_1", getTypeDescription("BigInteger"))
                        .addField("ACC_B_2", getTypeDescription("BigInteger"))
                        .addField("ACC_B_3", getTypeDescription("BigInteger"))
                        .addField("ACC_C_0", getTypeDescription("BigInteger"))
                        .addField("ACC_C_1", getTypeDescription("BigInteger"))
                        .addField("ACC_C_2", getTypeDescription("BigInteger"))
                        .addField("ACC_C_3", getTypeDescription("BigInteger"))
                        .addField("ACC_H_0", getTypeDescription("BigInteger"))
                        .addField("ACC_H_1", getTypeDescription("BigInteger"))
                        .addField("ACC_H_2", getTypeDescription("BigInteger"))
                        .addField("ACC_H_3", getTypeDescription("BigInteger"))
                        .addField("ARG_1_HI", getTypeDescription("BigInteger"))
                        .addField("ARG_1_LO", getTypeDescription("BigInteger"))
                        .addField("ARG_2_HI", getTypeDescription("BigInteger"))
                        .addField("ARG_2_LO", getTypeDescription("BigInteger"))
                        .addField("BITS", getTypeDescription("Boolean"))
                        .addField("BIT_NUM", getTypeDescription("BigInteger"))
                        .addField("BYTE_A_0", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_A_1", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_A_2", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_A_3", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_B_0", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_B_1", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_B_2", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_B_3", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_C_0", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_C_1", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_C_2", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_C_3", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_H_0", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_H_1", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_H_2", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_H_3", getTypeDescription("UnsignedByte"))
                        .addField("COUNTER", getTypeDescription("BigInteger"))
                        .addField("EXPONENT_BIT", getTypeDescription("Boolean"))
                        .addField("EXPONENT_BIT_ACCUMULATOR", getTypeDescription("BigInteger"))
                        .addField("EXPONENT_BIT_SOURCE", getTypeDescription("Boolean"))
                        .addField("INSTRUCTION", getTypeDescription("BigInteger"))
                        .addField("MUL_STAMP", getTypeDescription("BigInteger"))
                        .addField("OLI", getTypeDescription("Boolean"))
                        .addField("RESULT_VANISHES", getTypeDescription("Boolean"))
                        .addField("RES_HI", getTypeDescription("BigInteger"))
                        .addField("RES_LO", getTypeDescription("BigInteger"))
                        .addField("SQUARE_AND_MULTIPLY", getTypeDescription("Boolean"))
                        .addField("TINY_BASE", getTypeDescription("Boolean"))
                        .addField("TINY_EXPONENT", getTypeDescription("Boolean"))
                        ;

                Writer writer = OrcFile.createWriter(new Path(fileName + "_mul" +".orc"),
                        OrcFile.writerOptions(conf).setSchema(schema));

                return writer;
        }

        private static TypeDescription getTypeDescription(String className) {
                return switch (className) {
                        case "BigInteger", "UnsignedByte" -> TypeDescription.createBinary();
                        case "Boolean" -> TypeDescription.createLong();
                        default -> throw new UnsupportedOperationException("Unsuported type " + className);
                };
        }

}









