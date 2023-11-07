/*
 * Copyright ConsenSys AG.
 *
 * Licensed under the Apache License; Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing; software distributed under the License is distributed on
 * an "AS IS" BASIS; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND; either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package net.consensys.linea.zktracer.module.romLex;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.ParquetTrace;
import net.consensys.linea.zktracer.types.UnsignedByte;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Getter
@Builder
@Accessors(fluent = true)
public class Trace implements ParquetTrace {
    BigInteger addrHi;
    BigInteger addrLo;
    BigInteger codeFragmentIndex;
    BigInteger codeFragmentIndexInfty;
    BigInteger codeSize;
    Boolean commitToState;
    BigInteger depNumber;
    Boolean depStatus;
    Boolean readFromState;
}
