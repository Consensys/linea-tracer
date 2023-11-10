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

package net.consensys.linea.zktracer.module.mod;

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
            .addField("ACC_1_2", getTypeDescription("BigInteger"))
            .addField("ACC_1_3", getTypeDescription("BigInteger"))
            .addField("ACC_2_2", getTypeDescription("BigInteger"))
            .addField("ACC_2_3", getTypeDescription("BigInteger"))
            .addField("ACC_B_0", getTypeDescription("BigInteger"))
            .addField("ACC_B_1", getTypeDescription("BigInteger"))
            .addField("ACC_B_2", getTypeDescription("BigInteger"))
            .addField("ACC_B_3", getTypeDescription("BigInteger"))
            .addField("ACC_DELTA_0", getTypeDescription("BigInteger"))
            .addField("ACC_DELTA_1", getTypeDescription("BigInteger"))
            .addField("ACC_DELTA_2", getTypeDescription("BigInteger"))
            .addField("ACC_DELTA_3", getTypeDescription("BigInteger"))
            .addField("ACC_H_0", getTypeDescription("BigInteger"))
            .addField("ACC_H_1", getTypeDescription("BigInteger"))
            .addField("ACC_H_2", getTypeDescription("BigInteger"))
            .addField("ACC_Q_0", getTypeDescription("BigInteger"))
            .addField("ACC_Q_1", getTypeDescription("BigInteger"))
            .addField("ACC_Q_2", getTypeDescription("BigInteger"))
            .addField("ACC_Q_3", getTypeDescription("BigInteger"))
            .addField("ACC_R_0", getTypeDescription("BigInteger"))
            .addField("ACC_R_1", getTypeDescription("BigInteger"))
            .addField("ACC_R_2", getTypeDescription("BigInteger"))
            .addField("ACC_R_3", getTypeDescription("BigInteger"))
            .addField("ARG_1_HI", getTypeDescription("BigInteger"))
            .addField("ARG_1_LO", getTypeDescription("BigInteger"))
            .addField("ARG_2_HI", getTypeDescription("BigInteger"))
            .addField("ARG_2_LO", getTypeDescription("BigInteger"))
            .addField("BYTE_1_2", getTypeDescription("UnsignedByte"))
            .addField("BYTE_1_3", getTypeDescription("UnsignedByte"))
            .addField("BYTE_2_2", getTypeDescription("UnsignedByte"))
            .addField("BYTE_2_3", getTypeDescription("UnsignedByte"))
            .addField("BYTE_B_0", getTypeDescription("UnsignedByte"))
            .addField("BYTE_B_1", getTypeDescription("UnsignedByte"))
            .addField("BYTE_B_2", getTypeDescription("UnsignedByte"))
            .addField("BYTE_B_3", getTypeDescription("UnsignedByte"))
            .addField("BYTE_DELTA_0", getTypeDescription("UnsignedByte"))
            .addField("BYTE_DELTA_1", getTypeDescription("UnsignedByte"))
            .addField("BYTE_DELTA_2", getTypeDescription("UnsignedByte"))
            .addField("BYTE_DELTA_3", getTypeDescription("UnsignedByte"))
            .addField("BYTE_H_0", getTypeDescription("UnsignedByte"))
            .addField("BYTE_H_1", getTypeDescription("UnsignedByte"))
            .addField("BYTE_H_2", getTypeDescription("UnsignedByte"))
            .addField("BYTE_Q_0", getTypeDescription("UnsignedByte"))
            .addField("BYTE_Q_1", getTypeDescription("UnsignedByte"))
            .addField("BYTE_Q_2", getTypeDescription("UnsignedByte"))
            .addField("BYTE_Q_3", getTypeDescription("UnsignedByte"))
            .addField("BYTE_R_0", getTypeDescription("UnsignedByte"))
            .addField("BYTE_R_1", getTypeDescription("UnsignedByte"))
            .addField("BYTE_R_2", getTypeDescription("UnsignedByte"))
            .addField("BYTE_R_3", getTypeDescription("UnsignedByte"))
            .addField("CMP_1", getTypeDescription("Boolean"))
            .addField("CMP_2", getTypeDescription("Boolean"))
            .addField("CT", getTypeDescription("BigInteger"))
            .addField("DEC_OUTPUT", getTypeDescription("Boolean"))
            .addField("DEC_SIGNED", getTypeDescription("Boolean"))
            .addField("INST", getTypeDescription("BigInteger"))
            .addField("MSB_1", getTypeDescription("Boolean"))
            .addField("MSB_2", getTypeDescription("Boolean"))
            .addField("OLI", getTypeDescription("Boolean"))
            .addField("RES_HI", getTypeDescription("BigInteger"))
            .addField("RES_LO", getTypeDescription("BigInteger"))
            .addField("STAMP", getTypeDescription("BigInteger"))
            ;

    Writer writer = OrcFile.createWriter(new Path(fileName + "_mod" +".orc"),
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









