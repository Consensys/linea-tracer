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

import org.apache.orc.TypeDescription;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
public class ORCWriter {



    public static Map<String, FileWriter> getWriter(String path) throws IOException {

        List<String> files = new ArrayList<>();

        files.add("ACC_1");
        files.add("ACC_2");
        files.add("ARG_1_HI");
        files.add("ARG_1_LO");
        files.add("ARG_2_HI");
        files.add("ARG_2_LO");
        files.add("BYTE_1");
        files.add("BYTE_2");
        files.add("CT");
        files.add("INST");
        files.add("OVERFLOW");
        files.add("RES_HI");
        files.add("RES_LO");
        files.add("STAMP");

        Map<String, FileWriter> f = new HashMap<>();
        for(String module: files){
            f.put(module, new FileWriter(Paths.get(path+module+".txt").toFile()));
        }
        return f;
    }

}









