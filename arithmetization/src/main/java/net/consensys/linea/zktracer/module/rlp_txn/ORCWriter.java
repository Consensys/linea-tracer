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

package net.consensys.linea.zktracer.module.rlp_txn;

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
      .addField("ABS_TX_NUM", getTypeDescription("BigInteger"))
      .addField("ABS_TX_NUM_INFINY", getTypeDescription("BigInteger"))
      .addField("ACCESS_TUPLE_BYTESIZE", getTypeDescription("BigInteger"))
      .addField("ACC_1", getTypeDescription("BigInteger"))
      .addField("ACC_2", getTypeDescription("BigInteger"))
      .addField("ACC_BYTESIZE", getTypeDescription("BigInteger"))
      .addField("ADDR_HI", getTypeDescription("BigInteger"))
      .addField("ADDR_LO", getTypeDescription("BigInteger"))
      .addField("BIT", getTypeDescription("Boolean"))
      .addField("BIT_ACC", getTypeDescription("BigInteger"))
      .addField("BYTE_1", getTypeDescription("UnsignedByte"))
      .addField("BYTE_2", getTypeDescription("UnsignedByte"))
      .addField("CODE_FRAGMENT_INDEX", getTypeDescription("BigInteger"))
      .addField("COUNTER", getTypeDescription("BigInteger"))
      .addField("DATAGASCOST", getTypeDescription("BigInteger"))
      .addField("DATA_HI", getTypeDescription("BigInteger"))
      .addField("DATA_LO", getTypeDescription("BigInteger"))
      .addField("DEPTH_1", getTypeDescription("Boolean"))
      .addField("DEPTH_2", getTypeDescription("Boolean"))
      .addField("DONE", getTypeDescription("Boolean"))
      .addField("INDEX_DATA", getTypeDescription("BigInteger"))
      .addField("INDEX_LT", getTypeDescription("BigInteger"))
      .addField("INDEX_LX", getTypeDescription("BigInteger"))
      .addField("INPUT_1", getTypeDescription("BigInteger"))
      .addField("INPUT_2", getTypeDescription("BigInteger"))
      .addField("IS_PREFIX", getTypeDescription("Boolean"))
      .addField("LC_CORRECTION", getTypeDescription("Boolean"))
      .addField("LIMB", getTypeDescription("BigInteger"))
      .addField("LIMB_CONSTRUCTED", getTypeDescription("Boolean"))
      .addField("LT", getTypeDescription("Boolean"))
      .addField("LX", getTypeDescription("Boolean"))
      .addField("PHASE_0", getTypeDescription("Boolean"))
      .addField("PHASE_1", getTypeDescription("Boolean"))
      .addField("PHASE_10", getTypeDescription("Boolean"))
      .addField("PHASE_11", getTypeDescription("Boolean"))
      .addField("PHASE_12", getTypeDescription("Boolean"))
      .addField("PHASE_13", getTypeDescription("Boolean"))
      .addField("PHASE_14", getTypeDescription("Boolean"))
      .addField("PHASE_2", getTypeDescription("Boolean"))
      .addField("PHASE_3", getTypeDescription("Boolean"))
      .addField("PHASE_4", getTypeDescription("Boolean"))
      .addField("PHASE_5", getTypeDescription("Boolean"))
      .addField("PHASE_6", getTypeDescription("Boolean"))
      .addField("PHASE_7", getTypeDescription("Boolean"))
      .addField("PHASE_8", getTypeDescription("Boolean"))
      .addField("PHASE_9", getTypeDescription("Boolean"))
      .addField("PHASE_END", getTypeDescription("Boolean"))
      .addField("PHASE_SIZE", getTypeDescription("BigInteger"))
      .addField("POWER", getTypeDescription("BigInteger"))
      .addField("REQUIRES_EVM_EXECUTION", getTypeDescription("Boolean"))
      .addField("RLP_LT_BYTESIZE", getTypeDescription("BigInteger"))
      .addField("RLP_LX_BYTESIZE", getTypeDescription("BigInteger"))
      .addField("TYPE", getTypeDescription("BigInteger"))
      .addField("nADDR", getTypeDescription("BigInteger"))
      .addField("nBYTES", getTypeDescription("BigInteger"))
      .addField("nKEYS", getTypeDescription("BigInteger"))
      .addField("nKEYS_PER_ADDR", getTypeDescription("BigInteger"))
      .addField("nSTEP", getTypeDescription("BigInteger"))
    ;

    Writer writer = OrcFile.createWriter(new Path(fileName + "_rlp_txn" +".orc"),
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









