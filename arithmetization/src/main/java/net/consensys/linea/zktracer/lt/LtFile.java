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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.List;

import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.container.module.Module;
import net.consensys.linea.zktracer.module.ModuleName;

/** Provides a basic API for writing an LT trace file. */
public interface LtFile {
  /**
   * Access columns for writing where each column is indexed by its corresponding register id.
   * @return
   */
  Trace.Column[] columns();

  /**
   * Generate a binary encoding of the given trace.
   * @return
   */
  byte[] toBytes() throws IOException;

  /**
   * Construct the binary representation of a trace.
   *
   * @param header Header information to use
   * @param trace
   * @param modules
   * @return
   */
  static byte[] toBytes(LtFileHeader header, Trace trace, List<Module> modules)
      throws IOException {

    // Initialise trace file
    final LtFile ltf = switch (header.majorVersion()) {
      case 1 -> new V1(header, trace, modules);
      case 2 -> new V2(header, trace, modules);
      default -> throw new IllegalArgumentException(
        "unsupported lt trace file version (v" + header.majorVersion() + "." + header.minorVersion() + ")");
    };
    // Open trace for writing
    trace.open(ltf.columns());
    // Commit each module
    for (Module m : modules) {
      m.commit(trace);
    }
    // Convert to bytes
    return ltf.toBytes();
  }

  /**
   * Version 1 file format.
   */
  class V1 implements LtFile {
    private final LtFileHeader header;
    private final Trace.ColumnHeader[] columnHeaders;
    private final RawColumn[] columns;

    public V1(LtFileHeader header, Trace trace, List<Module> modules) {
      // Determine set of all columns headers
      final List<Trace.ColumnHeader> headers =
        modules.stream().flatMap(m -> m.columnHeaders(trace).stream()).toList();
      //
      this.header = header;
      this.columnHeaders = alignHeaders(headers);
      this.columns = new RawColumn[columnHeaders.length];

      for (int i = 0; i < columnHeaders.length; i++) {
        Trace.ColumnHeader colHeader = columnHeaders[i];
        if (colHeader != null) {
          columns[i] = new RawColumn(colHeader.name(), colHeader.bitwidth(), colHeader.length());
        }
      }
    }

    @Override
    public Trace.Column[] columns() {
      return columns;
    }

    @Override
    public byte[] toBytes() throws IOException {
      final ByteArrayOutputStream bout = new ByteArrayOutputStream();
      // Write header for LT file
      bout.writeBytes(header.toBytes());
      bout.writeBytes(Util.getColumnHeaderBytes(columnHeaders));
      // Write column data
      for (int i = 0; i != columns.length; i++) {
        if (columns[i] != null) {
          bout.writeBytes(columns[i].toBytes());
        }
      }
      //
      return bout.toByteArray();
    }

    /**
     * Align headers ensures that the order in which columns are seen matches the order found in the
     * trace schema.
     *
     * @param headers The headers to be aligned.
     * @return The aligned headers.
     */
    private static Trace.ColumnHeader[] alignHeaders(List<Trace.ColumnHeader> headers) {
      int maxRegister = 0;
      // Determine largest register
      //
      for (Trace.ColumnHeader header : headers) {
        maxRegister = Math.max(header.register(), maxRegister);
      }
      //
      Trace.ColumnHeader[] alignedHeaders = new Trace.ColumnHeader[maxRegister + 1];
      //
      for (Trace.ColumnHeader header : headers) {
        alignedHeaders[header.register()] = header;
      }
      //
      return alignedHeaders;
    }

  }

  /**
   * Version 2 file format.
   */
  class V2 implements LtFile {
    private final LtFileHeader header;
    private final ModuleHeader[] moduleHeaders;
    private final Column[] columns;
    private final BytesHeap heap;

    public V2(LtFileHeader header, Trace trace, List<Module> modules) {
      this.header = header;
      this.moduleHeaders = new ModuleHeader[modules.size()];
      this.heap = new BytesHeap();
      int numColumnHeaders = 0;
      // Initialise module headers
      for(int i=0; i<modules.size(); i++) {
        Module ith = modules.get(i);
        this.moduleHeaders[i] = new ModuleHeader(ith,trace,heap);
      }
      //
      this.columns = alignColumns(this.moduleHeaders);
    }

    @Override
    public Trace.Column[] columns() {
      return columns;
    }

    @Override
    public byte[] toBytes() throws IOException {
      final ByteArrayOutputStream bout = new ByteArrayOutputStream();
      final Encoding[][] encodings = getColumnEncodings(moduleHeaders);
      final byte[] heap = this.heap.toBytes();
      final byte[] headers = getModuleHeadersBytes(moduleHeaders, encodings);
      final ByteBuffer sizes = ByteBuffer.allocate(8);
      // Write header for LT file
      bout.write(header.toBytes());
      // Write section sizes
      sizes.putInt(headers.length);
      sizes.putInt(heap.length);
      bout.write(sizes.array());
      // Write section data
      bout.write(headers);
      bout.write(heap);
      // Write column data
      for(int i=0;i!=encodings.length;++i) {
        for(int j=0;j!=encodings[i].length;++j) {
          bout.writeBytes(encodings[i][j].data());
        }
      }
      // Done
      return bout.toByteArray();
    }

    /**
     * Determine all the column encodings.
     * @param headers
     * @return
     * @throws IOException
     */
    private static Encoding[][] getColumnEncodings(ModuleHeader[] headers) throws IOException {
      Encoding[][] encodings = new Encoding[headers.length][];
      //
      for(int i=0;i!=headers.length;i++) {
        ModuleHeader ith = headers[i];
        Encoding[] ithEncodings = new Encoding[ith.columns.length];
        for (int j = 0; j != ith.columns.length; j++) {
          Column col = ith.columns[j];
          ithEncodings[j] = col.toEncoding();
        }
        encodings[i] = ithEncodings;
      }
      //
      return encodings;
    }

    //
    private static byte[] getModuleHeadersBytes(ModuleHeader[] headers, Encoding[][] encodings) throws IOException {
     byte[][] headerBytes = new byte[headers.length][];
     int length = 0;

     for(int i=0;i!=headers.length;++i) {
       headerBytes[i] = headers[i].toBytes(encodings[i]);
       length += headerBytes[i].length;
     }
     // Flatten header bytes
     ByteBuffer buffer = ByteBuffer.allocate(length + 4);
     // Number of headers
      buffer.putInt(headers.length);
      //
     for(int i=0;i!=headerBytes.length;++i) {
       buffer.put(headerBytes[i]);
     }
      //
     return buffer.array();
    }

    private static void writeName(String name, ByteBuffer buf) {
      byte[] bytes = name.getBytes();
      buf.putShort((short) bytes.length);
      buf.put(bytes);
    }

    private static class ModuleHeader{
      final String name;
      final long height;
      final List<Trace.ColumnHeader> headers;
      final Column[] columns;

      ModuleHeader(Module m, Trace trace, BytesHeap heap) {
        this.headers = m.columnHeaders(trace);
        name = extractModuleName(m.moduleKey(), headers);
        this.height = m.lineCount();
        this.columns = new Column[headers.size()];
        // initialise columns
        for(int i=0;i!=columns.length;i++) {
          Trace.ColumnHeader header = headers.get(i);
          this.columns[i] = Column.of(header, heap);
        }
      }

      int byteLength() {
        int length = 0;
        //
        length += 2; // name length
        length += name.getBytes().length; // name bytes
        length += 4; // column height
        length += 4; // number of columns
        //
        for(Column column : columns) {
          length += 2; // name length
          length += column.name().getBytes().length; // name bytes
          length += 4; // data length
          length += 4; // data encoding
          length += 2; // bitwidth
        }
        //
        return length;
      }


    private byte[] toBytes(Encoding[] encodings) throws IOException {
      ByteBuffer buf = ByteBuffer.allocate(byteLength());
      //
      writeName(name,buf);
      buf.putInt((int) height);
      buf.putInt(columns.length);
      //
      for(int i=0;i!=columns.length;i++) {
        Column col = columns[i];
        writeName(col.name(), buf);
        buf.putInt(encodings[i].data().length);
        buf.putInt(encodings[i].encoding());
        buf.putShort((short) col.bitwidth());
      }
      //
      return buf.array();
    }

    }

    /**
     * Alignment ensures that the order in which columns are seen matches the order found in the
     * trace schema.
     *
     * @param headers The headers to be aligned.
     * @return The aligned headers.
     */
    private static Column[] alignColumns(ModuleHeader[] headers) {
      int maxRegister = 0;
      // Determine largest register
      for (ModuleHeader m : headers) {
        for (Trace.ColumnHeader header : m.headers) {
          maxRegister = Math.max(header.register(), maxRegister);
        }
      }
      //
      Column[] alignedHeaders = new Column[maxRegister + 1];
      //
      for (int i = 0; i != headers.length; ++i) {
        Column[] columns = headers[i].columns;
        List<Trace.ColumnHeader> columnHeaders = headers[i].headers;
        for (int c = 0; c != columnHeaders.size(); c++) {
          Trace.ColumnHeader header = columnHeaders.get(c);
          alignedHeaders[header.register()] = columns[c];
        }
      }
      //
      return alignedHeaders;
    }

    /**
     * Attempt to extract the module name from the given list of column headers.  This is really a kludge to work
     * around the fact that there is not enough information provided from the Trace API.  Eventually, this method
     * should be removed.
     *
     * @param name
     * @param headers
     * @return
     */
    private static String extractModuleName(ModuleName name, List<Trace.ColumnHeader> headers) {
      if(headers.isEmpty()) {
        throw new IllegalArgumentException("module has no columns (" + name + ")");
      }
      String[] split = headers.getFirst().name().split("\\.");
      return split[0];
    }
  }
}
