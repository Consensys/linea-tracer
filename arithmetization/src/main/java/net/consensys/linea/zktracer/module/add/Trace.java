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

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.module.ParquetTrace;
import net.consensys.linea.zktracer.types.UnsignedByte;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.function.Consumer;

/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
@Builder
@Accessors(fluent = true)
@Getter
public class Trace implements ParquetTrace {
        BigInteger acc1;
        BigInteger acc2;
        BigInteger arg1Hi;
        BigInteger arg1Lo;
        BigInteger arg2Hi;
        BigInteger arg2Lo;
        UnsignedByte byte1;
        UnsignedByte byte2;
        BigInteger ct;
        BigInteger inst;
        Boolean overflow;
        BigInteger resHi;
        BigInteger resLo;
        BigInteger stamp;


        public static List<ColumnHeader> headers(int size) {
                return List.of(
                        new ColumnHeader("ACC_1", 32, size),
                        new ColumnHeader("ACC_2", 32, size),
                        new ColumnHeader("ARG_1_HI", 32, size),
                        new ColumnHeader("ARG_1_LO", 32, size),
                        new ColumnHeader("ARG_2_HI", 32, size),
                        new ColumnHeader("ARG_2_LO", 32, size),
                        new ColumnHeader("BYTE_1", 1, size),
                        new ColumnHeader("BYTE_2", 1, size),
                        new ColumnHeader("CT", 32, size),
                        new ColumnHeader("INST", 32, size),
                        new ColumnHeader("OVERFLOW", 1, size),
                        new ColumnHeader("RES_HI", 32, size),
                        new ColumnHeader("RES_LO", 32, size),
                        new ColumnHeader("STAMP", 32, size));
        }

}
