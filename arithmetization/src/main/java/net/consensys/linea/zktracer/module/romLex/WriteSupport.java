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

import net.consensys.linea.zktracer.parquet.LocalWriteSupport;
import org.apache.parquet.schema.MessageTypeParser;

/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
public class WriteSupport extends LocalWriteSupport<Trace> {

    private final static String SCHEMA = """
        message TraceLine {
         required BINARY ADDR_HI;
         required BINARY ADDR_LO;
         required BINARY CODE_FRAGMENT_INDEX;
         required BINARY CODE_FRAGMENT_INDEX_INFTY;
         required BINARY CODE_SIZE;
         required BOOLEAN COMMIT_TO_STATE;
         required BINARY DEP_NUMBER;
         required BOOLEAN DEP_STATUS;
         required BOOLEAN READ_FROM_STATE;
         }
""";
    public WriteSupport() {
        super(MessageTypeParser.parseMessageType(SCHEMA));
}

  @Override
  public void write(Trace trace) {
    recordConsumer.startMessage();

    writeBigIntegerField("ADDR_HI", 0, trace.addrHi());
    writeBigIntegerField("ADDR_LO", 1, trace.addrLo());
    writeBigIntegerField("CODE_FRAGMENT_INDEX", 2, trace.codeFragmentIndex());
    writeBigIntegerField("CODE_FRAGMENT_INDEX_INFTY", 3, trace.codeFragmentIndexInfty());
    writeBigIntegerField("CODE_SIZE", 4, trace.codeSize());
    writeBooleanField("COMMIT_TO_STATE", 5, trace.commitToState());
    writeBigIntegerField("DEP_NUMBER", 6, trace.depNumber());
    writeBooleanField("DEP_STATUS", 7, trace.depStatus());
    writeBooleanField("READ_FROM_STATE", 8, trace.readFromState());
    recordConsumer.endMessage();
  }
}









