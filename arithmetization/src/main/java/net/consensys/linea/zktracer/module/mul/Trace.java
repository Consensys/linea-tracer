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

package net.consensys.linea.zktracer.module.mul;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.ParquetTrace;
import net.consensys.linea.zktracer.types.UnsignedByte;

import java.math.BigInteger;

/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
@Builder
@Accessors(fluent = true)
@Getter
public class Trace implements ParquetTrace {
  private final BigInteger accA0;
  private final BigInteger accA1;
  private final BigInteger accA2;
  private final BigInteger accA3;
  private final BigInteger accB0;
  private final BigInteger accB1;
  private final BigInteger accB2;
  private final BigInteger accB3;
  private final BigInteger accC0;
  private final BigInteger accC1;
  private final BigInteger accC2;
  private final BigInteger accC3;
  private final BigInteger accH0;
  private final BigInteger accH1;
  private final BigInteger accH2;
  private final BigInteger accH3;
  private final BigInteger arg1Hi;
  private final BigInteger arg1Lo;
  private final BigInteger arg2Hi;
  private final BigInteger arg2Lo;
  private final BigInteger bitNum;
  private final Boolean bits;
  private final UnsignedByte byteA0;
  private final UnsignedByte byteA1;
  private final UnsignedByte byteA2;
  private final UnsignedByte byteA3;
  private final UnsignedByte byteB0;
  private final UnsignedByte byteB1;
  private final UnsignedByte byteB2;
  private final UnsignedByte byteB3;
  private final UnsignedByte byteC0;
  private final UnsignedByte byteC1;
  private final UnsignedByte byteC2;
  private final UnsignedByte byteC3;
  private final UnsignedByte byteH0;
  private final UnsignedByte byteH1;
  private final UnsignedByte byteH2;
  private final UnsignedByte byteH3;
  private final BigInteger counter;
  private final Boolean exponentBit;
  private final BigInteger exponentBitAccumulator;
  private final Boolean exponentBitSource;
  private final BigInteger instruction;
  private final BigInteger mulStamp;
  private final Boolean oli;
  private final BigInteger resHi;
  private final BigInteger resLo;
  private final Boolean resultVanishes;
  private final Boolean squareAndMultiply;
  private final Boolean tinyBase;
  private final Boolean tinyExponent; 
}
