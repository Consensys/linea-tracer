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

import lombok.extern.slf4j.Slf4j;

import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.module.FW;

import java.nio.file.Path;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.Map;

import net.consensys.linea.zktracer.module.*;
/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
@Slf4j
public class CompressedFileWriter{

        final public BigIntegerCompressedFileWriter acc;
        final public BooleanCompressedFileWriter codesizeReached;
        final public BigIntegerCompressedFileWriter codeFragmentIndex;
        final public BigIntegerCompressedFileWriter codeFragmentIndexInfty;
        final public BigIntegerCompressedFileWriter codeSize;
        final public BigIntegerCompressedFileWriter counter;
        final public BigIntegerCompressedFileWriter counterMax;
        final public BigIntegerCompressedFileWriter counterPush;
        final public BigIntegerCompressedFileWriter index;
        final public BooleanCompressedFileWriter isPush;
        final public BooleanCompressedFileWriter isPushData;
        final public BigIntegerCompressedFileWriter limb;
        final public UnsignedByteCompressedFileWriter opcode;
        final public UnsignedByteCompressedFileWriter paddedBytecodeByte;
        final public BigIntegerCompressedFileWriter programmeCounter;
        final public BooleanCompressedFileWriter pushFunnelBit;
        final public BigIntegerCompressedFileWriter pushParameter;
        final public BigIntegerCompressedFileWriter pushValueAcc;
        final public BigIntegerCompressedFileWriter pushValueHigh;
        final public BigIntegerCompressedFileWriter pushValueLow;
        final public BooleanCompressedFileWriter validJumpDestination;
        final public BigIntegerCompressedFileWriter nBytes;
        final public BigIntegerCompressedFileWriter nBytesAcc;

        public CompressedFileWriter(Path path, String pattern, Map<String, ColumnHeader> traceMap) throws IOException {
                log.info("Creating writer for {}.", "rom");

                this.acc = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("ACC" + pattern).toString(), "rw"), traceMap.get("ACC").dataSize()));
                this.codesizeReached = new BooleanCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("CODESIZE_REACHED" + pattern).toString(), "rw"), traceMap.get("CODESIZE_REACHED").dataSize()));
                this.codeFragmentIndex = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("CODE_FRAGMENT_INDEX" + pattern).toString(), "rw"), traceMap.get("CODE_FRAGMENT_INDEX").dataSize()));
                this.codeFragmentIndexInfty = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("CODE_FRAGMENT_INDEX_INFTY" + pattern).toString(), "rw"), traceMap.get("CODE_FRAGMENT_INDEX_INFTY").dataSize()));
                this.codeSize = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("CODE_SIZE" + pattern).toString(), "rw"), traceMap.get("CODE_SIZE").dataSize()));
                this.counter = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("COUNTER" + pattern).toString(), "rw"), traceMap.get("COUNTER").dataSize()));
                this.counterMax = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("COUNTER_MAX" + pattern).toString(), "rw"), traceMap.get("COUNTER_MAX").dataSize()));
                this.counterPush = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("COUNTER_PUSH" + pattern).toString(), "rw"), traceMap.get("COUNTER_PUSH").dataSize()));
                this.index = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("INDEX" + pattern).toString(), "rw"), traceMap.get("INDEX").dataSize()));
                this.isPush = new BooleanCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("IS_PUSH" + pattern).toString(), "rw"), traceMap.get("IS_PUSH").dataSize()));
                this.isPushData = new BooleanCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("IS_PUSH_DATA" + pattern).toString(), "rw"), traceMap.get("IS_PUSH_DATA").dataSize()));
                this.limb = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("LIMB" + pattern).toString(), "rw"), traceMap.get("LIMB").dataSize()));
                this.opcode = new UnsignedByteCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("OPCODE" + pattern).toString(), "rw"), traceMap.get("OPCODE").dataSize()));
                this.paddedBytecodeByte = new UnsignedByteCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("PADDED_BYTECODE_BYTE" + pattern).toString(), "rw"), traceMap.get("PADDED_BYTECODE_BYTE").dataSize()));
                this.programmeCounter = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("PROGRAMME_COUNTER" + pattern).toString(), "rw"), traceMap.get("PROGRAMME_COUNTER").dataSize()));
                this.pushFunnelBit = new BooleanCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("PUSH_FUNNEL_BIT" + pattern).toString(), "rw"), traceMap.get("PUSH_FUNNEL_BIT").dataSize()));
                this.pushParameter = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("PUSH_PARAMETER" + pattern).toString(), "rw"), traceMap.get("PUSH_PARAMETER").dataSize()));
                this.pushValueAcc = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("PUSH_VALUE_ACC" + pattern).toString(), "rw"), traceMap.get("PUSH_VALUE_ACC").dataSize()));
                this.pushValueHigh = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("PUSH_VALUE_HIGH" + pattern).toString(), "rw"), traceMap.get("PUSH_VALUE_HIGH").dataSize()));
                this.pushValueLow = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("PUSH_VALUE_LOW" + pattern).toString(), "rw"), traceMap.get("PUSH_VALUE_LOW").dataSize()));
                this.validJumpDestination = new BooleanCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("VALID_JUMP_DESTINATION" + pattern).toString(), "rw"), traceMap.get("VALID_JUMP_DESTINATION").dataSize()));
                this.nBytes = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("nBYTES" + pattern).toString(), "rw"), traceMap.get("nBYTES").dataSize()));
                this.nBytesAcc = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("nBYTES_ACC" + pattern).toString(), "rw"), traceMap.get("nBYTES_ACC").dataSize()));
        }


        public void close() {
                this.acc.close();
                this.codeFragmentIndex.close();
                this.codeFragmentIndexInfty.close();
                this.codeSize.close();
                this.codesizeReached.close();
                this.counter.close();
                this.counterMax.close();
                this.counterPush.close();
                this.index.close();
                this.isPush.close();
                this.isPushData.close();
                this.limb.close();
                this.nBytes.close();
                this.nBytesAcc.close();
                this.opcode.close();
                this.paddedBytecodeByte.close();
                this.programmeCounter.close();
                this.pushFunnelBit.close();
                this.pushParameter.close();
                this.pushValueAcc.close();
                this.pushValueHigh.close();
                this.pushValueLow.close();
                this.validJumpDestination.close();
        }
}









