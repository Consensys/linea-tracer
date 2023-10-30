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

package net.consensys.linea.zktracer;

public record ColumnHeader(String name, int bytesPerElement, int eltCount) {
  public int dataSize() {
    return this.eltCount() * this.bytesPerElement();
  }

  public int headerSize() {
    return this.name.length()
        + 2
        + // name encoding
        1
        + // bit per element
        4; // element count
  }

  public long cumulatedSize() {
    return this.headerSize() + this.dataSize();
  }

  public static ColumnHeader make(String name, int bitsPerElement, int length) {
    assert name.length() < Short.MAX_VALUE;
    return new ColumnHeader(name, bitsPerElement, length);
  }
}
