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

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
public class ORCWriter {




        public static Map<String, FileChannel> getWriter(String path) throws IOException {

                List<String> files = new ArrayList<>();

                files.add("ACC");
                files.add("CODESIZE_REACHED");
                files.add("CODE_FRAGMENT_INDEX");
                files.add("CODE_FRAGMENT_INDEX_INFTY");
                files.add("CODE_SIZE");
                files.add("COUNTER");
                files.add("COUNTER_MAX");
                files.add("COUNTER_PUSH");
                files.add("INDEX");
                files.add("IS_PUSH");
                files.add("IS_PUSH_DATA");
                files.add("LIMB");
                files.add("OPCODE");
                files.add("PADDED_BYTECODE_BYTE");
                files.add("PROGRAMME_COUNTER");
                files.add("PUSH_FUNNEL_BIT");
                files.add("PUSH_PARAMETER");
                files.add("PUSH_VALUE_ACC");
                files.add("PUSH_VALUE_HIGH");
                files.add("PUSH_VALUE_LOW");
                files.add("VALID_JUMP_DESTINATION");
                files.add("nBYTES");
                files.add("nBYTES_ACC");

                Map<String, FileChannel> f = new HashMap<>();
                for(String module: files){
                        var fos =new RandomAccessFile(path+module, "rw");
                        f.put(module, fos.getChannel());
                }
                return f;

        }

}









