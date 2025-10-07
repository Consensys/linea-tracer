/*
 * Copyright ConsenSys Inc.
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

package net.consensys.linea.zktracer.module.log2;

import static com.google.common.base.Preconditions.checkArgument;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.container.ModuleOperation;
import org.apache.tuweni.bytes.Bytes;

@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Log2Operation extends ModuleOperation {

  @EqualsAndHashCode.Include @Getter private final Bytes word;
  private final short log2;

  public Log2Operation(Bytes word) {
    this.word = word;
    final short bitLength = (short) word.bitLength();
    log2 = bitLength == 0 ? 0 : (short) (bitLength - 1);
    checkArgument(log2 <= 255, "log2(word) must be <= 255");
  }

  public void trace(Trace.Log2 trace) {
    trace.arg(word).res(log2).validateRow();
  }

  @Override
  protected int computeLineCount() {
    return 1;
  }

  public static class Comparator implements java.util.Comparator<Log2Operation> {
    public int compare(Log2Operation op1, Log2Operation op2) {
      return op1.word.compareTo(op2.word);
    }
  }
}
