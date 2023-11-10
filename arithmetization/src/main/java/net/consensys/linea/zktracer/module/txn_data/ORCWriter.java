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

package net.consensys.linea.zktracer.module.txn_data;

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
      .addField("ABS_TX_NUM", getTypeDescription("BigInteger"))
      .addField("ABS_TX_NUM_MAX", getTypeDescription("BigInteger"))
      .addField("BASEFEE", getTypeDescription("BigInteger"))
      .addField("BTC_NUM", getTypeDescription("BigInteger"))
      .addField("BTC_NUM_MAX", getTypeDescription("BigInteger"))
      .addField("CALL_DATA_SIZE", getTypeDescription("BigInteger"))
      .addField("CODE_FRAGMENT_INDEX", getTypeDescription("BigInteger"))
      .addField("COINBASE_HI", getTypeDescription("BigInteger"))
      .addField("COINBASE_LO", getTypeDescription("BigInteger"))
      .addField("CT", getTypeDescription("BigInteger"))
      .addField("CUMULATIVE_CONSUMED_GAS", getTypeDescription("BigInteger"))
      .addField("FROM_HI", getTypeDescription("BigInteger"))
      .addField("FROM_LO", getTypeDescription("BigInteger"))
      .addField("GAS_LIMIT", getTypeDescription("BigInteger"))
      .addField("GAS_PRICE", getTypeDescription("BigInteger"))
      .addField("INITIAL_BALANCE", getTypeDescription("BigInteger"))
      .addField("INITIAL_GAS", getTypeDescription("BigInteger"))
      .addField("INIT_CODE_SIZE", getTypeDescription("BigInteger"))
      .addField("IS_DEP", getTypeDescription("Boolean"))
      .addField("LEFTOVER_GAS", getTypeDescription("BigInteger"))
      .addField("NONCE", getTypeDescription("BigInteger"))
      .addField("OUTGOING_HI", getTypeDescription("BigInteger"))
      .addField("OUTGOING_LO", getTypeDescription("BigInteger"))
      .addField("OUTGOING_RLP_TXNRCPT", getTypeDescription("BigInteger"))
      .addField("PHASE_RLP_TXN", getTypeDescription("BigInteger"))
      .addField("PHASE_RLP_TXNRCPT", getTypeDescription("BigInteger"))
      .addField("REFUND_AMOUNT", getTypeDescription("BigInteger"))
      .addField("REFUND_COUNTER", getTypeDescription("BigInteger"))
      .addField("REL_TX_NUM", getTypeDescription("BigInteger"))
      .addField("REL_TX_NUM_MAX", getTypeDescription("BigInteger"))
      .addField("REQUIRES_EVM_EXECUTION", getTypeDescription("Boolean"))
      .addField("STATUS_CODE", getTypeDescription("Boolean"))
      .addField("TO_HI", getTypeDescription("BigInteger"))
      .addField("TO_LO", getTypeDescription("BigInteger"))
      .addField("TYPE0", getTypeDescription("Boolean"))
      .addField("TYPE1", getTypeDescription("Boolean"))
      .addField("TYPE2", getTypeDescription("Boolean"))
      .addField("VALUE", getTypeDescription("BigInteger"))
      .addField("WCP_ARG_ONE_LO", getTypeDescription("BigInteger"))
      .addField("WCP_ARG_TWO_LO", getTypeDescription("BigInteger"))
      .addField("WCP_INST", getTypeDescription("BigInteger"))
      .addField("WCP_RES_LO", getTypeDescription("Boolean"))
    ;

    Writer writer = OrcFile.createWriter(new Path(fileName + "_txn_data" +".orc"),
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









