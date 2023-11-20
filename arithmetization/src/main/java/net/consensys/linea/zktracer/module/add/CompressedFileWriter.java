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

package net.consensys.linea.zktracer.module.add;

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

  final public BigIntegerCompressedFileWriter acc1;
  final public BigIntegerCompressedFileWriter acc2;
  final public BigIntegerCompressedFileWriter arg1Hi;
  final public BigIntegerCompressedFileWriter arg1Lo;
  final public BigIntegerCompressedFileWriter arg2Hi;
  final public BigIntegerCompressedFileWriter arg2Lo;
  final public UnsignedByteCompressedFileWriter byte1;
  final public UnsignedByteCompressedFileWriter byte2;
  final public BigIntegerCompressedFileWriter ct;
  final public BigIntegerCompressedFileWriter inst;
  final public BooleanCompressedFileWriter overflow;
  final public BigIntegerCompressedFileWriter resHi;
  final public BigIntegerCompressedFileWriter resLo;
  final public BigIntegerCompressedFileWriter stamp;

  public CompressedFileWriter(Path path, String pattern, Map<String, ColumnHeader> traceMap) throws IOException {
    log.info("[TRACING] Creating writer for {} at {}.", "add", path.toAbsolutePath().toString());

    this.acc1 = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("ACC_1" + pattern).toString(), "rw"), traceMap.get("ACC_1").dataSize()));
    this.acc2 = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("ACC_2" + pattern).toString(), "rw"), traceMap.get("ACC_1").dataSize()));
    this.arg1Hi = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("ARG_1_HI" + pattern).toString(), "rw"), traceMap.get("ACC_1").dataSize()));
    this.arg1Lo = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("ARG_1_LO" + pattern).toString(), "rw"), traceMap.get("ACC_1").dataSize()));
    this.arg2Hi = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("ARG_2_HI" + pattern).toString(), "rw"), traceMap.get("ACC_1").dataSize()));
    this.arg2Lo = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("ARG_2_LO" + pattern).toString(), "rw"), traceMap.get("ACC_1").dataSize()));
    this.byte1 = new UnsignedByteCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("BYTE_1" + pattern).toString(), "rw"), traceMap.get("ACC_1").dataSize()));
    this.byte2 = new UnsignedByteCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("BYTE_2" + pattern).toString(), "rw"), traceMap.get("ACC_1").dataSize()));
    this.ct = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("CT" + pattern).toString(), "rw"), traceMap.get("ACC_1").dataSize()));
    this.inst = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("INST" + pattern).toString(), "rw"), traceMap.get("ACC_1").dataSize()));
    this.overflow = new BooleanCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("OVERFLOW" + pattern).toString(), "rw"), traceMap.get("ACC_1").dataSize()));
    this.resHi = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("RES_HI" + pattern).toString(), "rw"), traceMap.get("ACC_1").dataSize()));
    this.resLo = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("RES_LO" + pattern).toString(), "rw"), traceMap.get("ACC_1").dataSize()));
    this.stamp = new BigIntegerCompressedFileWriter(new FW(new RandomAccessFile(path.resolve("STAMP" + pattern).toString(), "rw"), traceMap.get("ACC_1").dataSize()));
  }

  public void close() {
    acc1.close();
  }
}









