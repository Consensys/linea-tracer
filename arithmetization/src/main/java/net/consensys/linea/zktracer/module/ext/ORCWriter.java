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
                conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
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
                        .addField("ACC_DELTA_0", getTypeDescription("BigInteger"))
                        .addField("ACC_DELTA_1", getTypeDescription("BigInteger"))
                        .addField("ACC_DELTA_2", getTypeDescription("BigInteger"))
                        .addField("ACC_DELTA_3", getTypeDescription("BigInteger"))
                        .addField("ACC_H_0", getTypeDescription("BigInteger"))
                        .addField("ACC_H_1", getTypeDescription("BigInteger"))
                        .addField("ACC_H_2", getTypeDescription("BigInteger"))
                        .addField("ACC_H_3", getTypeDescription("BigInteger"))
                        .addField("ACC_H_4", getTypeDescription("BigInteger"))
                        .addField("ACC_H_5", getTypeDescription("BigInteger"))
                        .addField("ACC_I_0", getTypeDescription("BigInteger"))
                        .addField("ACC_I_1", getTypeDescription("BigInteger"))
                        .addField("ACC_I_2", getTypeDescription("BigInteger"))
                        .addField("ACC_I_3", getTypeDescription("BigInteger"))
                        .addField("ACC_I_4", getTypeDescription("BigInteger"))
                        .addField("ACC_I_5", getTypeDescription("BigInteger"))
                        .addField("ACC_I_6", getTypeDescription("BigInteger"))
                        .addField("ACC_J_0", getTypeDescription("BigInteger"))
                        .addField("ACC_J_1", getTypeDescription("BigInteger"))
                        .addField("ACC_J_2", getTypeDescription("BigInteger"))
                        .addField("ACC_J_3", getTypeDescription("BigInteger"))
                        .addField("ACC_J_4", getTypeDescription("BigInteger"))
                        .addField("ACC_J_5", getTypeDescription("BigInteger"))
                        .addField("ACC_J_6", getTypeDescription("BigInteger"))
                        .addField("ACC_J_7", getTypeDescription("BigInteger"))
                        .addField("ACC_Q_0", getTypeDescription("BigInteger"))
                        .addField("ACC_Q_1", getTypeDescription("BigInteger"))
                        .addField("ACC_Q_2", getTypeDescription("BigInteger"))
                        .addField("ACC_Q_3", getTypeDescription("BigInteger"))
                        .addField("ACC_Q_4", getTypeDescription("BigInteger"))
                        .addField("ACC_Q_5", getTypeDescription("BigInteger"))
                        .addField("ACC_Q_6", getTypeDescription("BigInteger"))
                        .addField("ACC_Q_7", getTypeDescription("BigInteger"))
                        .addField("ACC_R_0", getTypeDescription("BigInteger"))
                        .addField("ACC_R_1", getTypeDescription("BigInteger"))
                        .addField("ACC_R_2", getTypeDescription("BigInteger"))
                        .addField("ACC_R_3", getTypeDescription("BigInteger"))
                        .addField("ARG_1_HI", getTypeDescription("BigInteger"))
                        .addField("ARG_1_LO", getTypeDescription("BigInteger"))
                        .addField("ARG_2_HI", getTypeDescription("BigInteger"))
                        .addField("ARG_2_LO", getTypeDescription("BigInteger"))
                        .addField("ARG_3_HI", getTypeDescription("BigInteger"))
                        .addField("ARG_3_LO", getTypeDescription("BigInteger"))
                        .addField("BIT_1", getTypeDescription("Boolean"))
                        .addField("BIT_2", getTypeDescription("Boolean"))
                        .addField("BIT_3", getTypeDescription("Boolean"))
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
                        .addField("BYTE_DELTA_0", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_DELTA_1", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_DELTA_2", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_DELTA_3", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_H_0", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_H_1", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_H_2", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_H_3", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_H_4", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_H_5", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_I_0", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_I_1", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_I_2", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_I_3", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_I_4", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_I_5", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_I_6", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_J_0", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_J_1", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_J_2", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_J_3", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_J_4", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_J_5", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_J_6", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_J_7", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_Q_0", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_Q_1", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_Q_2", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_Q_3", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_Q_4", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_Q_5", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_Q_6", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_Q_7", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_R_0", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_R_1", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_R_2", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_R_3", getTypeDescription("UnsignedByte"))
                        .addField("CMP", getTypeDescription("Boolean"))
                        .addField("CT", getTypeDescription("BigInteger"))
                        .addField("INST", getTypeDescription("BigInteger"))
                        .addField("OF_H", getTypeDescription("Boolean"))
                        .addField("OF_I", getTypeDescription("Boolean"))
                        .addField("OF_J", getTypeDescription("Boolean"))
                        .addField("OF_RES", getTypeDescription("Boolean"))
                        .addField("OLI", getTypeDescription("Boolean"))
                        .addField("RES_HI", getTypeDescription("BigInteger"))
                        .addField("RES_LO", getTypeDescription("BigInteger"))
                        .addField("STAMP", getTypeDescription("BigInteger"))
                        ;

                Writer writer = OrcFile.createWriter(new Path(fileName + "_ext" +".orc"),
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









