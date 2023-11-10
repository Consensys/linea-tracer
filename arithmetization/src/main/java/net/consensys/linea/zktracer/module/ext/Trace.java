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

package net.consensys.linea.zktracer.module.ext;

import com.fasterxml.jackson.annotation.JsonProperty;

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
@Builder
@Getter
@Accessors(fluent = true)
public class Trace implements ParquetTrace {
  BigInteger accA0;
  BigInteger accA1;
  BigInteger accA2;
  BigInteger accA3;
  BigInteger accB0;
  BigInteger accB1;
  BigInteger accB2;
  BigInteger accB3;
  BigInteger accC0;
  BigInteger accC1;
  BigInteger accC2;
  BigInteger accC3;
  BigInteger accDelta0;
  BigInteger accDelta1;
  BigInteger accDelta2;
  BigInteger accDelta3;
  BigInteger accH0;
  BigInteger accH1;
  BigInteger accH2;
  BigInteger accH3;
  BigInteger accH4;
  BigInteger accH5;
  BigInteger accI0;
  BigInteger accI1;
  BigInteger accI2;
  BigInteger accI3;
  BigInteger accI4;
  BigInteger accI5;
  BigInteger accI6;
  BigInteger accJ0;
  BigInteger accJ1;
  BigInteger accJ2;
  BigInteger accJ3;
  BigInteger accJ4;
  BigInteger accJ5;
  BigInteger accJ6;
  BigInteger accJ7;
  BigInteger accQ0;
  BigInteger accQ1;
  BigInteger accQ2;
  BigInteger accQ3;
  BigInteger accQ4;
  BigInteger accQ5;
  BigInteger accQ6;
  BigInteger accQ7;
  BigInteger accR0;
  BigInteger accR1;
  BigInteger accR2;
  BigInteger accR3;
  BigInteger arg1Hi;
  BigInteger arg1Lo;
  BigInteger arg2Hi;
  BigInteger arg2Lo;
  BigInteger arg3Hi;
  BigInteger arg3Lo;
  Boolean bit1;
  Boolean bit2;
  Boolean bit3;
  UnsignedByte byteA0;
  UnsignedByte byteA1;
  UnsignedByte byteA2;
  UnsignedByte byteA3;
  UnsignedByte byteB0;
  UnsignedByte byteB1;
  UnsignedByte byteB2;
  UnsignedByte byteB3;
  UnsignedByte byteC0;
  UnsignedByte byteC1;
  UnsignedByte byteC2;
  UnsignedByte byteC3;
  UnsignedByte byteDelta0;
  UnsignedByte byteDelta1;
  UnsignedByte byteDelta2;
  UnsignedByte byteDelta3;
  UnsignedByte byteH0;
  UnsignedByte byteH1;
  UnsignedByte byteH2;
  UnsignedByte byteH3;
  UnsignedByte byteH4;
  UnsignedByte byteH5;
  UnsignedByte byteI0;
  UnsignedByte byteI1;
  UnsignedByte byteI2;
  UnsignedByte byteI3;
  UnsignedByte byteI4;
  UnsignedByte byteI5;
  UnsignedByte byteI6;
  UnsignedByte byteJ0;
  UnsignedByte byteJ1;
  UnsignedByte byteJ2;
  UnsignedByte byteJ3;
  UnsignedByte byteJ4;
  UnsignedByte byteJ5;
  UnsignedByte byteJ6;
  UnsignedByte byteJ7;
  UnsignedByte byteQ0;
  UnsignedByte byteQ1;
  UnsignedByte byteQ2;
  UnsignedByte byteQ3;
  UnsignedByte byteQ4;
  UnsignedByte byteQ5;
  UnsignedByte byteQ6;
  UnsignedByte byteQ7;
  UnsignedByte byteR0;
  UnsignedByte byteR1;
  UnsignedByte byteR2;
  UnsignedByte byteR3;
  Boolean cmp;
  BigInteger ct;
  BigInteger inst;
  Boolean ofH;
  Boolean ofI;
  Boolean ofJ;
  Boolean ofRes;
  Boolean oli;
  BigInteger resHi;
  BigInteger resLo;
  BigInteger stamp;
}
