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

package net.consensys.linea.zktracer.module.mxp;

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
                TypeDescription schema = TypeDescription.createStruct()
                        .addField("ACC_1", getTypeDescription("BigInteger"))
                        .addField("ACC_2", getTypeDescription("BigInteger"))
                        .addField("ACC_3", getTypeDescription("BigInteger"))
                        .addField("ACC_4", getTypeDescription("BigInteger"))
                        .addField("ACC_A", getTypeDescription("BigInteger"))
                        .addField("ACC_Q", getTypeDescription("BigInteger"))
                        .addField("ACC_W", getTypeDescription("BigInteger"))
                        .addField("BYTE_1", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_2", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_3", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_4", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_A", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_Q", getTypeDescription("UnsignedByte"))
                        .addField("BYTE_QQ", getTypeDescription("BigInteger"))
                        .addField("BYTE_R", getTypeDescription("BigInteger"))
                        .addField("BYTE_W", getTypeDescription("UnsignedByte"))
                        .addField("CN", getTypeDescription("BigInteger"))
                        .addField("COMP", getTypeDescription("Boolean"))
                        .addField("CT", getTypeDescription("BigInteger"))
                        .addField("C_MEM", getTypeDescription("BigInteger"))
                        .addField("C_MEM_NEW", getTypeDescription("BigInteger"))
                        .addField("DEPLOYS", getTypeDescription("Boolean"))
                        .addField("EXPANDS", getTypeDescription("Boolean"))
                        .addField("GAS_MXP", getTypeDescription("BigInteger"))
                        .addField("GBYTE", getTypeDescription("BigInteger"))
                        .addField("GWORD", getTypeDescription("BigInteger"))
                        .addField("INST", getTypeDescription("BigInteger"))
                        .addField("LIN_COST", getTypeDescription("BigInteger"))
                        .addField("MAX_OFFSET", getTypeDescription("BigInteger"))
                        .addField("MAX_OFFSET_1", getTypeDescription("BigInteger"))
                        .addField("MAX_OFFSET_2", getTypeDescription("BigInteger"))
                        .addField("MXPX", getTypeDescription("Boolean"))
                        .addField("MXP_TYPE_1", getTypeDescription("Boolean"))
                        .addField("MXP_TYPE_2", getTypeDescription("Boolean"))
                        .addField("MXP_TYPE_3", getTypeDescription("Boolean"))
                        .addField("MXP_TYPE_4", getTypeDescription("Boolean"))
                        .addField("MXP_TYPE_5", getTypeDescription("Boolean"))
                        .addField("NOOP", getTypeDescription("Boolean"))
                        .addField("OFFSET_1_HI", getTypeDescription("BigInteger"))
                        .addField("OFFSET_1_LO", getTypeDescription("BigInteger"))
                        .addField("OFFSET_2_HI", getTypeDescription("BigInteger"))
                        .addField("OFFSET_2_LO", getTypeDescription("BigInteger"))
                        .addField("QUAD_COST", getTypeDescription("BigInteger"))
                        .addField("ROOB", getTypeDescription("Boolean"))
                        .addField("SIZE_1_HI", getTypeDescription("BigInteger"))
                        .addField("SIZE_1_LO", getTypeDescription("BigInteger"))
                        .addField("SIZE_2_HI", getTypeDescription("BigInteger"))
                        .addField("SIZE_2_LO", getTypeDescription("BigInteger"))
                        .addField("STAMP", getTypeDescription("BigInteger"))
                        .addField("WORDS", getTypeDescription("BigInteger"))
                        .addField("WORDS_NEW", getTypeDescription("BigInteger"))
                        ;

                Writer writer = OrcFile.createWriter(new Path(fileName + "_mxp" +".orc"),
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









