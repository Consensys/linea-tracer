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
                        .addField("ACC", getTypeDescription("BigInteger"))
                        .addField("CODESIZE_REACHED", getTypeDescription("Boolean"))
                        .addField("CODE_FRAGMENT_INDEX", getTypeDescription("BigInteger"))
                        .addField("CODE_FRAGMENT_INDEX_INFTY", getTypeDescription("BigInteger"))
                        .addField("CODE_SIZE", getTypeDescription("BigInteger"))
                        .addField("COUNTER", getTypeDescription("BigInteger"))
                        .addField("COUNTER_MAX", getTypeDescription("BigInteger"))
                        .addField("COUNTER_PUSH", getTypeDescription("BigInteger"))
                        .addField("INDEX", getTypeDescription("BigInteger"))
                        .addField("IS_PUSH", getTypeDescription("Boolean"))
                        .addField("IS_PUSH_DATA", getTypeDescription("Boolean"))
                        .addField("LIMB", getTypeDescription("BigInteger"))
                        .addField("OPCODE", getTypeDescription("UnsignedByte"))
                        .addField("PADDED_BYTECODE_BYTE", getTypeDescription("UnsignedByte"))
                        .addField("PROGRAMME_COUNTER", getTypeDescription("BigInteger"))
                        .addField("PUSH_FUNNEL_BIT", getTypeDescription("Boolean"))
                        .addField("PUSH_PARAMETER", getTypeDescription("BigInteger"))
                        .addField("PUSH_VALUE_ACC", getTypeDescription("BigInteger"))
                        .addField("PUSH_VALUE_HIGH", getTypeDescription("BigInteger"))
                        .addField("PUSH_VALUE_LOW", getTypeDescription("BigInteger"))
                        .addField("VALID_JUMP_DESTINATION", getTypeDescription("Boolean"))
                        .addField("nBYTES", getTypeDescription("BigInteger"))
                        .addField("nBYTES_ACC", getTypeDescription("BigInteger"))
                        ;

                Writer writer = OrcFile.createWriter(new Path(fileName + "_rom" +".orc"),
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









