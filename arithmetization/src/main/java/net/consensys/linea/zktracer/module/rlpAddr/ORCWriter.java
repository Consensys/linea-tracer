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

package net.consensys.linea.zktracer.module.rlpAddr;

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
      .addField("ACC", getTypeDescription("BigInteger"))
      .addField("ACC_BYTESIZE", getTypeDescription("BigInteger"))
      .addField("ADDR_HI", getTypeDescription("BigInteger"))
      .addField("ADDR_LO", getTypeDescription("BigInteger"))
      .addField("BIT1", getTypeDescription("Boolean"))
      .addField("BIT_ACC", getTypeDescription("UnsignedByte"))
      .addField("BYTE1", getTypeDescription("UnsignedByte"))
      .addField("COUNTER", getTypeDescription("BigInteger"))
      .addField("DEP_ADDR_HI", getTypeDescription("BigInteger"))
      .addField("DEP_ADDR_LO", getTypeDescription("BigInteger"))
      .addField("INDEX", getTypeDescription("BigInteger"))
      .addField("KEC_HI", getTypeDescription("BigInteger"))
      .addField("KEC_LO", getTypeDescription("BigInteger"))
      .addField("LC", getTypeDescription("Boolean"))
      .addField("LIMB", getTypeDescription("BigInteger"))
      .addField("NONCE", getTypeDescription("BigInteger"))
      .addField("POWER", getTypeDescription("BigInteger"))
      .addField("RECIPE", getTypeDescription("BigInteger"))
      .addField("RECIPE_1", getTypeDescription("Boolean"))
      .addField("RECIPE_2", getTypeDescription("Boolean"))
      .addField("SALT_HI", getTypeDescription("BigInteger"))
      .addField("SALT_LO", getTypeDescription("BigInteger"))
      .addField("STAMP", getTypeDescription("BigInteger"))
      .addField("TINY_NON_ZERO_NONCE", getTypeDescription("Boolean"))
      .addField("nBYTES", getTypeDescription("BigInteger"))
    ;

    Writer writer = OrcFile.createWriter(new Path(fileName + "_rlpAddr" +".orc"),
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









