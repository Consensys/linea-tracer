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

package net.consensys.linea.zktracer.module.wcp;

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
      .addField("ACC_1", getTypeDescription("BigInteger"))
      .addField("ACC_2", getTypeDescription("BigInteger"))
      .addField("ACC_3", getTypeDescription("BigInteger"))
      .addField("ACC_4", getTypeDescription("BigInteger"))
      .addField("ACC_5", getTypeDescription("BigInteger"))
      .addField("ACC_6", getTypeDescription("BigInteger"))
      .addField("ARGUMENT_1_HI", getTypeDescription("BigInteger"))
      .addField("ARGUMENT_1_LO", getTypeDescription("BigInteger"))
      .addField("ARGUMENT_2_HI", getTypeDescription("BigInteger"))
      .addField("ARGUMENT_2_LO", getTypeDescription("BigInteger"))
      .addField("BITS", getTypeDescription("Boolean"))
      .addField("BIT_1", getTypeDescription("Boolean"))
      .addField("BIT_2", getTypeDescription("Boolean"))
      .addField("BIT_3", getTypeDescription("Boolean"))
      .addField("BIT_4", getTypeDescription("Boolean"))
      .addField("BYTE_1", getTypeDescription("UnsignedByte"))
      .addField("BYTE_2", getTypeDescription("UnsignedByte"))
      .addField("BYTE_3", getTypeDescription("UnsignedByte"))
      .addField("BYTE_4", getTypeDescription("UnsignedByte"))
      .addField("BYTE_5", getTypeDescription("UnsignedByte"))
      .addField("BYTE_6", getTypeDescription("UnsignedByte"))
      .addField("COUNTER", getTypeDescription("BigInteger"))
      .addField("INST", getTypeDescription("BigInteger"))
      .addField("NEG_1", getTypeDescription("Boolean"))
      .addField("NEG_2", getTypeDescription("Boolean"))
      .addField("ONE_LINE_INSTRUCTION", getTypeDescription("Boolean"))
      .addField("RESULT_HI", getTypeDescription("BigInteger"))
      .addField("RESULT_LO", getTypeDescription("BigInteger"))
      .addField("WORD_COMPARISON_STAMP", getTypeDescription("BigInteger"))
    ;

    Writer writer = OrcFile.createWriter(new Path(fileName + "_wcp" +".orc"),
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









