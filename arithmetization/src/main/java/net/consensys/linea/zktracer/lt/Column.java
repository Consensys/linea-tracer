package net.consensys.linea.zktracer.lt;

import net.consensys.linea.zktracer.Trace;

public interface Column extends Trace.Column {
  /**
   * Unqualified name of the column.
   *
   * @return
   */
  String name();

  /**
   * bitwidth of enclosing column.
   *
   * @return
   */
  int bitwidth();

  /**
   * Get an encoding of this column
   *
   * @return
   */
  Encoding toEncoding();

  static Column of(Trace.ColumnHeader header, BytesHeap heap) {
    if (header.bitwidth() <= 32) {
      return new Small(header);
    } else {
      return new Large(header, heap);
    }
  }

  class Base {
    private final String name;
    private final int bitwidth;

    public Base(Trace.ColumnHeader header) {
      final String[] split = header.name().split("\\.");
      this.bitwidth = header.bitwidth();
      // Unqualify the qualified name
      this.name =
          switch (split.length) {
            case 1 -> split[0];
            case 2 -> split[1];
            default -> {
              throw new IllegalArgumentException("invalid column name: " + header.name());
            }
          };
    }

    public String name() {
      return this.name;
    }

    public int bitwidth() {
      return this.bitwidth;
    }
  }

  /**
   * Provides a static encoding of column data, where each element is stored explicitly (i.e. not as
   * a pool index). This uses an optimised encoding (e.g. where binary colunms are stored as bits)
   * and, hence, is suitable only for the v2 file format.
   */
  class Small extends Base implements Column {
    private final long longMax;
    private final long[] buffer;
    private int index;

    public Small(Trace.ColumnHeader header) {
      super(header);
      this.longMax = 1L << header.bitwidth();
      this.buffer = new long[header.length()];
    }

    @Override
    public void write(boolean value) {
      this.buffer[index++] = value ? 1 : 0;
    }

    @Override
    public void write(long value) {
      // Sanity check
      if (longMax <= value) {
        throw new IllegalArgumentException(name() + " has invalid value (" + value + ")");
      }
      //
      this.buffer[index++] = value;
    }

    /**
     * Write element bytes
     *
     * @param bytes stored in big-endian form and already trimmed.
     */
    @Override
    public void write(byte[] bytes) {
      throw new UnsupportedOperationException();
    }

    /**
     * Convert this column into a given byte encoding.
     *
     * @return
     */
    public Encoding toEncoding() {
      return Encoding.of(buffer);
    }
  }

  class Large extends Base implements Column {
    private final BytesHeap heap;
    private final int[] buffer;
    private int index;

    public Large(Trace.ColumnHeader header, BytesHeap heap) {
      super(header);
      this.heap = heap;
      this.buffer = new int[header.length()];
    }

    @Override
    public void write(boolean value) {
      this.write(value ? 1L : 0L);
    }

    @Override
    public void write(long value) {
      byte b3 = (byte) (value >> 24);
      byte b2 = (byte) (value >> 16);
      byte b1 = (byte) (value >> 8);
      byte b0 = (byte) value;
      byte[] bytes;
      // ensure bytes in trimmed and in big endian form.
      if (value <= 0xffff) {
        if (value <= 0xff) {
          bytes = new byte[] {b0};
        } else {
          bytes = new byte[] {b1, b0};
        }
      } else if (value <= 0xffffff) {
        bytes = new byte[] {b2, b1, b0};
      } else {
        bytes = new byte[] {b3, b2, b1, b0};
      }
      //
      this.write(bytes);
    }

    /**
     * Write element bytes
     *
     * @param bytes stored in big-endian form and already trimmed.
     */
    @Override
    public void write(byte[] bytes) {
      this.buffer[index++] = heap.insert(bytes);
    }

    /**
     * Convert this column into a given byte encoding.
     *
     * @return
     */
    public Encoding toEncoding() {
      return Encoding.ofPool(buffer, bitwidth());
    }
  }
}
