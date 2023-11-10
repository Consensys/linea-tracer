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

package net.consensys.linea.zktracer.module.romLex;

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
      .addField("ADDR_HI", getTypeDescription("BigInteger"))
      .addField("ADDR_LO", getTypeDescription("BigInteger"))
      .addField("CODE_FRAGMENT_INDEX", getTypeDescription("BigInteger"))
      .addField("CODE_FRAGMENT_INDEX_INFTY", getTypeDescription("BigInteger"))
      .addField("CODE_SIZE", getTypeDescription("BigInteger"))
      .addField("COMMIT_TO_STATE", getTypeDescription("Boolean"))
      .addField("DEP_NUMBER", getTypeDescription("BigInteger"))
      .addField("DEP_STATUS", getTypeDescription("Boolean"))
      .addField("READ_FROM_STATE", getTypeDescription("Boolean"))
    ;

    Writer writer = OrcFile.createWriter(new Path(fileName + "_romLex" +".orc"),
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









