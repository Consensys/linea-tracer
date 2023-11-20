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


import net.consensys.linea.zktracer.module.*;
import net.consensys.linea.zktracer.types.UnsignedByte;

import java.math.BigInteger;
import java.util.BitSet;
import java.io.IOException;
/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
@SuppressWarnings({"unchecked"})
public class TraceBuilder extends AbstractTraceBuilder {
  private final BitSet filled = new BitSet();

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

  public TraceBuilder(CompressedFileWriter writer) {
    this.acc1 = writer.acc1;
    this.acc2 = writer.acc2;
    this.arg1Hi = writer.arg1Hi;
    this.arg1Lo = writer.arg1Lo;
    this.arg2Hi = writer.arg2Hi;
    this.arg2Lo = writer.arg2Lo;
    this.byte1 = writer.byte1;
    this.byte2 = writer.byte2;
    this.ct = writer.ct;
    this.inst = writer.inst;
    this.overflow = writer.overflow;
    this.resHi = writer.resHi;
    this.resLo = writer.resLo;
    this.stamp = writer.stamp;
  }

  public TraceBuilder acc1(BigInteger b) {

    if (filled.get(0)) {
      throw new IllegalStateException("ACC_1 already set");
    } else {
      filled.set(0);
    }
    processBigInteger(b, this.acc1 );
    return this;
  }
  public TraceBuilder acc2(BigInteger b) {

    if (filled.get(1)) {
      throw new IllegalStateException("ACC_2 already set");
    } else {
      filled.set(1);
    }
    processBigInteger(b, this.acc2 );
    return this;
  }
  public TraceBuilder arg1Hi(BigInteger b) {

    if (filled.get(2)) {
      throw new IllegalStateException("ARG_1_HI already set");
    } else {
      filled.set(2);
    }
    processBigInteger(b, this.arg1Hi );
    return this;
  }
  public TraceBuilder arg1Lo(BigInteger b) {

    if (filled.get(3)) {
      throw new IllegalStateException("ARG_1_LO already set");
    } else {
      filled.set(3);
    }
    processBigInteger(b, this.arg1Lo );
    return this;
  }
  public TraceBuilder arg2Hi(BigInteger b) {

    if (filled.get(4)) {
      throw new IllegalStateException("ARG_2_HI already set");
    } else {
      filled.set(4);
    }
    processBigInteger(b, this.arg2Hi );
    return this;
  }
  public TraceBuilder arg2Lo(BigInteger b) {

    if (filled.get(5)) {
      throw new IllegalStateException("ARG_2_LO already set");
    } else {
      filled.set(5);
    }
    processBigInteger(b, this.arg2Lo );
    return this;
  }
  public TraceBuilder byte1(UnsignedByte b) {

    if (filled.get(6)) {
      throw new IllegalStateException("BYTE_1 already set");
    } else {
      filled.set(6);
    }
    processUnsignedByte(b, this.byte1 );
    return this;
  }
  public TraceBuilder byte2(UnsignedByte b) {

    if (filled.get(7)) {
      throw new IllegalStateException("BYTE_2 already set");
    } else {
      filled.set(7);
    }
    processUnsignedByte(b, this.byte2 );
    return this;
  }
  public TraceBuilder ct(BigInteger b) {

    if (filled.get(8)) {
      throw new IllegalStateException("CT already set");
    } else {
      filled.set(8);
    }
    processBigInteger(b, this.ct );
    return this;
  }
  public TraceBuilder inst(BigInteger b) {

    if (filled.get(9)) {
      throw new IllegalStateException("INST already set");
    } else {
      filled.set(9);
    }
    processBigInteger(b, this.inst );
    return this;
  }
  public TraceBuilder overflow(Boolean b) {

    if (filled.get(10)) {
      throw new IllegalStateException("OVERFLOW already set");
    } else {
      filled.set(10);
    }
    processBoolean(b, this.overflow );
    return this;
  }
  public TraceBuilder resHi(BigInteger b) {

    if (filled.get(11)) {
      throw new IllegalStateException("RES_HI already set");
    } else {
      filled.set(11);
    }
    processBigInteger(b, this.resHi );
    return this;
  }
  public TraceBuilder resLo(BigInteger b) {

    if (filled.get(12)) {
      throw new IllegalStateException("RES_LO already set");
    } else {
      filled.set(12);
    }
    processBigInteger(b, this.resLo );
    return this;
  }
  public TraceBuilder stamp(BigInteger b) {

    if (filled.get(13)) {
      throw new IllegalStateException("STAMP already set");
    } else {
      filled.set(13);
    }
    processBigInteger(b, this.stamp );
    return this;
  }

  public void validateRowAndFlush()  throws IOException {
    if (!filled.get(0)) {
      throw new IllegalStateException("ACC_1 has not been filled");
    }
    if (!filled.get(1)) {
      throw new IllegalStateException("ACC_2 has not been filled");
    }
    if (!filled.get(2)) {
      throw new IllegalStateException("ARG_1_HI has not been filled");
    }
    if (!filled.get(3)) {
      throw new IllegalStateException("ARG_1_LO has not been filled");
    }
    if (!filled.get(4)) {
      throw new IllegalStateException("ARG_2_HI has not been filled");
    }
    if (!filled.get(5)) {
      throw new IllegalStateException("ARG_2_LO has not been filled");
    }
    if (!filled.get(6)) {
      throw new IllegalStateException("BYTE_1 has not been filled");
    }
    if (!filled.get(7)) {
      throw new IllegalStateException("BYTE_2 has not been filled");
    }
    if (!filled.get(8)) {
      throw new IllegalStateException("CT has not been filled");
    }
    if (!filled.get(9)) {
      throw new IllegalStateException("INST has not been filled");
    }
    if (!filled.get(10)) {
      throw new IllegalStateException("OVERFLOW has not been filled");
    }
    if (!filled.get(11)) {
      throw new IllegalStateException("RES_HI has not been filled");
    }
    if (!filled.get(12)) {
      throw new IllegalStateException("RES_LO has not been filled");
    }
    if (!filled.get(13)) {
      throw new IllegalStateException("STAMP has not been filled");
    }

    filled.clear();
  }
}
