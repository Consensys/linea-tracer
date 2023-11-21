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

package net.consensys.linea.zktracer.module.rom;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.ColumnHeader;
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
@Builder
@Getter
@Accessors(fluent = true)
public class Trace{
  BigInteger acc;
  BigInteger codeFragmentIndex;
  BigInteger codeFragmentIndexInfty;
  BigInteger codeSize;
  Boolean codesizeReached;
  BigInteger counter;
  BigInteger counterMax;
  BigInteger counterPush;
  BigInteger index;
  Boolean isPush;
  Boolean isPushData;
  BigInteger limb;
  BigInteger nBytes;
  BigInteger nBytesAcc;
  UnsignedByte opcode;
  UnsignedByte paddedBytecodeByte;
  BigInteger programmeCounter;
  Boolean pushFunnelBit;
  BigInteger pushParameter;
  BigInteger pushValueAcc;
  BigInteger pushValueHigh;
  BigInteger pushValueLow;
  Boolean validJumpDestination;

  public static List<ColumnHeader> headers(int size) {
    return List.of(
            new ColumnHeader("ACC", 32, size),
            new ColumnHeader("CODE_FRAGMENT_INDEX", 32, size),
            new ColumnHeader("CODE_FRAGMENT_INDEX_INFTY", 32, size),
            new ColumnHeader("CODE_SIZE", 32, size),
            new ColumnHeader("CODESIZE_REACHED", 1, size),
            new ColumnHeader("COUNTER", 32, size),
            new ColumnHeader("COUNTER_MAX", 32, size),
            new ColumnHeader("COUNTER_PUSH", 32, size),
            new ColumnHeader("INDEX", 32, size),
            new ColumnHeader("IS_PUSH", 1, size),
            new ColumnHeader("IS_PUSH_DATA", 1, size),
            new ColumnHeader("LIMB", 32, size),
            new ColumnHeader("nBYTES", 32, size),
            new ColumnHeader("nBYTES_ACC", 32, size),
            new ColumnHeader("OPCODE", 1, size),
            new ColumnHeader("PADDED_BYTECODE_BYTE", 1, size),
            new ColumnHeader("PROGRAMME_COUNTER", 32, size),
            new ColumnHeader("PUSH_FUNNEL_BIT", 1, size),
            new ColumnHeader("PUSH_PARAMETER", 32, size),
            new ColumnHeader("PUSH_VALUE_ACC", 32, size),
            new ColumnHeader("PUSH_VALUE_HIGH", 32, size),
            new ColumnHeader("PUSH_VALUE_LOW", 32, size),
            new ColumnHeader("VALID_JUMP_DESTINATION", 1, size));
  }


}
