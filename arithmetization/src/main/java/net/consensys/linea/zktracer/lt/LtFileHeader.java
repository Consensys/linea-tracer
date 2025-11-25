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
package net.consensys.linea.zktracer.lt;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public record LtFileHeader(int majorVersion, int minorVersion, Map<String,Object> metadata) {

  /**
   * Construct trace file header containing the given metadata bytes.
   *
   * @return bytes making up the header.
   */
  public byte[] toBytes() throws IOException {
    byte[] metadataBytes = getMetadataBytes(metadata);
    //
    ByteBuffer buffer = ByteBuffer.allocate(16 + metadataBytes.length);
    // File identifier
    buffer.put(new byte[] {'z', 'k', 't', 'r', 'a', 'c', 'e', 'r'});
    // Major version
    buffer.putShort((short) majorVersion);
    // Minor version
    buffer.putShort((short) minorVersion);
    // Metadata length
    buffer.putInt(metadataBytes.length);
    // Metadata
    buffer.put(metadataBytes);
    // Done
    return buffer.array();
  }


  /** Object writer is used for generating JSON byte strings. */
  private static final ObjectWriter objectWriter = new ObjectMapper().writer();

  public static byte[] getMetadataBytes(Map<String, Object> metadata) throws IOException {
    return objectWriter.writeValueAsBytes(metadata);
  }



}
