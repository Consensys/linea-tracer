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
import net.consensys.linea.zktracer.module.FW;
import net.consensys.linea.zktracer.module.add.CompressedFileWriter;

import java.io.RandomAccessFile;
import java.io.IOException;

/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
public class ORCWriter {


        public static CompressedFileWriter<?>[] getWriter(String path) throws IOException {

                CompressedFileWriter<?>[] res = new CompressedFileWriter<?>[]{
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "ACC", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "CODESIZE_REACHED", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "CODE_FRAGMENT_INDEX", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "CODE_FRAGMENT_INDEX_INFTY", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "CODE_SIZE", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "COUNTER", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "COUNTER_MAX", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "COUNTER_PUSH", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "INDEX", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "IS_PUSH", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "IS_PUSH_DATA", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "LIMB", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "OPCODE", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "PADDED_BYTECODE_BYTE", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "PROGRAMME_COUNTER", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "PUSH_FUNNEL_BIT", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "PUSH_PARAMETER", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "PUSH_VALUE_ACC", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "PUSH_VALUE_HIGH", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "PUSH_VALUE_LOW", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "VALID_JUMP_DESTINATION", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "nBYTES", "rw"))),
                        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "nBYTES_ACC", "rw")))};

                return res;
        }
        }









