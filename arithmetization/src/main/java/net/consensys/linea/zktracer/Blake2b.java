package net.consensys.linea.zktracer;

import java.io.*;
import java.util.HexFormat;

import lombok.extern.slf4j.Slf4j;
import org.apache.tuweni.bytes.Bytes;

@Slf4j
public class Blake2b {

  // TestVectors taken from EIP152

  private static String[] TestVector4 = {
    // Input
    "48c9bdf267e6096a3ba7ca8485ae67bb2bf894fe72f36e3cf1361d5f3af54fa5d182e6ad7f520e511f6c3e2b8c68059b6bbd41fbabd9831f79217e1319cde05b61626300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000300000000000000000000000000000001",
    // Output
    "08c9bcf367e6096a3ba7ca8485ae67bb2bf894fe72f36e3cf1361d5f3af54fa5d282e6ad7f520e511f6c3e2b8c68059b9442be0454267ce079217e1319cde05b"
  };

  private static String[] TestVector5 = {
    // Input
    "48c9bdf267e6096a3ba7ca8485ae67bb2bf894fe72f36e3cf1361d5f3af54fa5d182e6ad7f520e511f6c3e2b8c68059b6bbd41fbabd9831f79217e1319cde05b61626300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000300000000000000000000000000000001",
    // Output
    "ba80a53f981c4d0d6a2797b69f12f6e94c212f14685ac4b74b12bb6fdbffa2d17d87c5392aab792dc252d5de4533cc9518d38aa8dbf1925ab92386edd4009923"
  };

  private static String[] TestVector6 = {
    // Input
    "48c9bdf267e6096a3ba7ca8485ae67bb2bf894fe72f36e3cf1361d5f3af54fa5d182e6ad7f520e511f6c3e2b8c68059b6bbd41fbabd9831f79217e1319cde05b61626300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000300000000000000000000000000000000",
    // Output
    "75ab69d3190a562c51aef8d88f1c2775876944407270c42c9844252c26d2875298743e7f6d5ea2f2d3e8d226039cd31b4e426ac4f2d3d666a610c2116fde4735"
  };

  private static String[] TestVector7 = {
    // Input
    "48c9bdf267e6096a3ba7ca8485ae67bb2bf894fe72f36e3cf1361d5f3af54fa5d182e6ad7f520e511f6c3e2b8c68059b6bbd41fbabd9831f79217e1319cde05b61626300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000300000000000000000000000000000001",
    // Output
    "b63a380cb2897d521994a85234ee2c181b5f844d2c624c002677e9703449d2fba551b3a8333bcdf5f2f7e08993d53923de3d64fcc68c034e717b9293fed7a421"
  };

  private static String[] TestVector8 = {
    // Input
    "48c9bdf267e6096a3ba7ca8485ae67bb2bf894fe72f36e3cf1361d5f3af54fa5d182e6ad7f520e511f6c3e2b8c68059b6bbd41fbabd9831f79217e1319cde05b61626300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000300000000000000000000000000000001",
    // Output
    "fc59093aafa9ab43daae0e914c57635c5402d8e3d2130eb9b3cc181de7f0ecf9b22bf99a7815ce16419e200e01846e6b5df8cc7703041bbceb571de6631d2615"
  };

  private static String[] TestVectorUnitWith1 = {
    // Input
    "00000000000000000000000000000000000000000000000000000000000badb0770000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
    // Output
    "bdceb326589cb7914462bb3386084cc377c7bab9ae1c372dfc56f0db9daa13287e422376a5bb2c8f08da0754f21c8162e2f99e1af32da005c990b34eba681c92"
  };

  private static String[] TestVectorUnitWith0 = {
    // Input
    "00000000000000000000000000000000000000000000000000000000000badb0770000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
    // Output
    "08c9bcf367e6096a3ba7ca8485ae67bb2bf894fe72f36e3cf1361d5f3af54fa5d182e6ad7f520e511f6c3e2b8c68059b6bbd41fbabd9831f79217e1319cde05b"
  };

  private static String[] TestVectorUnitWith10 = {
    // Input
    "0000000a00000000000000000000000000000000000000000000000000000000000badb0770000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
    "1a3913310df40cceb2646207c069ba2e51e51b0262c75091808b53eed49241594e045c57973a41e16a1c36c99af1a02d5060a8388a3f2e347c5e5a2f625a2659"
  };

  private static String[][] TestVectors = {
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8,
    TestVector4,
    TestVector5,
    TestVector6,
    TestVector7,
    TestVector8
  };

  // private static String[][] TestVectors = { TestVector4};

  public static void main(String[] args) {
    if (args.length == 0) {
      for (int i = 0; i != TestVectors.length; i++) {
        System.out.print("Test vector " + i + " ... ");
        double rounds = (Math.random() * ((500 - 0) + 1)) + 0;
        Bytes roundsL = Bytes.ofUnsignedLong((long) rounds);
        String begin = roundsL.toString().substring(10);
        String input = begin + TestVectors[i][0];
        String output = TestVectors[i][1];
        if (!run(input, false).equals(output)) {
          System.out.println("[FAILURE] (" + output + ")");
        } else {
          System.out.println("[OK]");
        }
      }
    } else {
      System.out.println(run(args[0], true));
    }
  }

  private static String run(String input, boolean debug) {
    HexFormat hex = HexFormat.of();
    byte[] bytes = hex.parseHex(input);
    // Sanity check bytes
    if (bytes.length != 213) {
      throw new IllegalArgumentException("expected 213 bytes, got " + bytes.length + ".");
    } else if (bytes[212] != 0 && bytes[212] != 1) {
      throw new IllegalArgumentException("malformed block indicator flag: " + bytes[212]);
    }
    //
    byte f = bytes[212];
    long r = fromBigEndian32(bytes, 0) & 0xFFFFFFFFL;
    long[] h = fromLittleEndian64s(bytes, 4, 8);
    long[] m = fromLittleEndian64s(bytes, 68, 16);
    long[] t = fromLittleEndian64s(bytes, 196, 2);
    //
    if (debug) {
      System.out.println("rounds = " + r);
      for (int i = 0; i < 8; i++) {
        System.out.println("h[" + i + "]=" + hex.toHexDigits(h[i]));
      }
      for (int i = 0; i < 16; i++) {
        System.out.println("m[" + i + "]=" + hex.toHexDigits(m[i]));
      }
      for (int i = 0; i < 2; i++) {
        System.out.println("t[" + i + "]=" + hex.toHexDigits(t[i]));
      }
    }
    //
    F(r, h, m, t, f == 1);
    //
    return hex.formatHex(toLittleEndianBytes(h));
  }

  /**
   * Convert a 64bit word into a sequence of 8 bytes in little endian order.
   *
   * @param w The word to convert
   * @param offset Offset into the byte array to begin writing bytes.
   * @param bytes Byte array into which result is placed (must be large enough)
   */
  public static void toLittleEndianBytes(long w, int offset, byte[] bytes) {
    bytes[offset] = (byte) w;
    bytes[offset + 1] = (byte) (w >> 8);
    bytes[offset + 2] = (byte) (w >> 16);
    bytes[offset + 3] = (byte) (w >> 24);
    bytes[offset + 4] = (byte) (w >> 32);
    bytes[offset + 5] = (byte) (w >> 40);
    bytes[offset + 6] = (byte) (w >> 48);
    bytes[offset + 7] = (byte) (w >> 56);
  }

  /**
   * Convert an array of words into an array of bytes, such that each word is stored in little
   * endian order. Thus, the first word is stored at offset <code>0</code> in the resulting array,
   * whilst the second word is at offset <code>8</code>, etc.
   *
   * @param words
   * @return
   */
  public static byte[] toLittleEndianBytes(long[] words) {
    byte[] bytes = new byte[words.length * 8];
    int offset = 0;
    for (int i = 0; i != words.length; ++i) {
      long w = words[i];
      toLittleEndianBytes(w, offset, bytes);
      offset = offset + 8;
    }
    return bytes;
  }

  /**
   * Read a 32-bit word from a byte array stored in big endian representation.
   *
   * @param bytes Byte array containing bytes to read.
   * @param offset Offset into byte array where bytes are stored.
   * @return
   */
  public static int fromBigEndian32(byte[] bytes, int offset) {
    int b1 = (bytes[offset + 3] & 0xff);
    int b2 = (bytes[offset + 2] & 0xff) << 8;
    int b3 = (bytes[offset + 1] & 0xff) << 16;
    int b4 = (bytes[offset + 0] & 0xff) << 24;
    return b1 | b2 | b3 | b4;
  }

  /**
   * Read a 64-bit word from a byte array begining at a given offset.
   *
   * @param bytes Byte array to read word from.
   * @param offset Offset into array where bytes are stored.
   * @return
   */
  public static long fromLittleEndian64(byte[] bytes, int offset) {
    long b1 = (bytes[offset + 0] & 0xffL);
    long b2 = (bytes[offset + 1] & 0xffL) << 8;
    long b3 = (bytes[offset + 2] & 0xffL) << 16;
    long b4 = (bytes[offset + 3] & 0xffL) << 24;
    long b5 = (bytes[offset + 4] & 0xffL) << 32;
    long b6 = (bytes[offset + 5] & 0xffL) << 40;
    long b7 = (bytes[offset + 6] & 0xffL) << 48;
    long b8 = (bytes[offset + 7] & 0xffL) << 56;
    return b1 | b2 | b3 | b4 | b5 | b6 | b7 | b8;
  }

  /**
   * Convert a sequence of bytes representing <code>n</code> 64bit words store in little endian
   * order. Thus, the first word is stored at offset <code>0</code> in the resulting array, whilst
   * the second word is at offset <code>8</code>, etc.
   *
   * @param bytes Byte array holding words to be read.
   * @param offset Offset into byte array to begin reading from.
   * @param length Number of words to read.
   * @return
   */
  public static long[] fromLittleEndian64s(byte[] bytes, int offset, int length) {
    long[] words = new long[length];
    for (int i = 0; i != length; ++i) {
      words[i] = fromLittleEndian64(bytes, offset);
      offset += 8;
    }
    return words;
  }

  // =============================================================================
  // Blake Compression
  // =============================================================================

  public static long[] F(long r, long[] h, long[] m, long[] t, boolean f) {
    long[] v = new long[16];
    long[] hh = new long[16];
    System.arraycopy(h, 0, v, 0, 8);
    System.arraycopy(IV, 0, v, 8, 8);
    System.arraycopy(h, 0, hh, 0, 8);
    v[12] = v[12] ^ t[0];
    v[13] = v[13] ^ t[1];
    //
    if (f) {
      // Invert all bits
      v[14] = ~v[14];
    }
    // log.info(" { \"F\" : { \"r\" : [{}], \"h0_input\" : [{}],  \"h1_input\" : [{}], \"h2_input\"
    // : [{}], \"h3_input\" : [{}], \"h4_input\" : [{}], \"h5_input\" : [{}],\"h6_input\" : [{}],
    // \"h7_input\" : [{}],  \"m0\" : [{}], " +
    //                 "\"m1\" : [{}], \"m2\" : [{}], \"m3\" : [{}], \"m4\" : [{}], \"m5\" : [{}],
    // \"m6\" : [{}], \"m7\" : [{}], \"m8\" : [{}], \"m9\" : [{}], \"m10\" : [{}], \"m11\" : [{}],
    // \"m12\" : [{}], \"m13\" : [{}], \"m14\" : [{}], \"m15\" : [{}], " +
    //                "\"t0\" : [{}], \"t1\" : [{}],\"f\" : [{}],  \"v0\" : [{}],  \"v1\" : [{}],
    // \"v2\" : [{}], \"v3\" : [{}], \"v4\" : [{}], \"v5\" : [{}],\"v6\" : [{}], \"v7\" : [{}],
    // \"v8\" : [{}], \"v9\" : [{}], \"v10\" : [{}] , \"v11\" : [{}], \"v12\" : [{}] , \"v13\" :
    // [{}] , \"v14\" : [{}] , \"v15\" : [{}]}}",
    //        Long.toUnsignedString(r),
    // Long.toUnsignedString(hh[0]),Long.toUnsignedString(hh[1]),Long.toUnsignedString(hh[2]),Long.toUnsignedString(hh[3]), Long.toUnsignedString(hh[4]), Long.toUnsignedString(hh[5]) , Long.toUnsignedString(hh[6]), Long.toUnsignedString(hh[7]), Long.toUnsignedString(m[0]),
    //        Long.toUnsignedString(m[1]), Long.toUnsignedString(m[2]), Long.toUnsignedString(m[3]),
    // Long.toUnsignedString(m[4]), Long.toUnsignedString(m[5]), Long.toUnsignedString(m[6]),
    // Long.toUnsignedString(m[7]), Long.toUnsignedString(m[8]), Long.toUnsignedString(m[9]),
    // Long.toUnsignedString(m[10]), Long.toUnsignedString(m[11])
    //        , Long.toUnsignedString(m[12]), Long.toUnsignedString(m[13]),
    // Long.toUnsignedString(m[14]), Long.toUnsignedString(m[15]), Long.toUnsignedString(t[0]),
    // Long.toUnsignedString(t[1]), f ? 1 : 0,
    // Long.toUnsignedString(v[0]),Long.toUnsignedString(v[1]),Long.toUnsignedString(v[2]),Long.toUnsignedString(v[3]), Long.toUnsignedString(v[4]), Long.toUnsignedString(v[5]) , Long.toUnsignedString(v[6]), Long.toUnsignedString(v[7]), Long.toUnsignedString(v[8]), Long.toUnsignedString(v[9]), Long.toUnsignedString(v[10]), Long.toUnsignedString(v[11]), Long.toUnsignedString(v[12]), Long.toUnsignedString(v[13]), Long.toUnsignedString(v[14]), Long.toUnsignedString(v[15])
    // );
    // Cryptographic mixing
    for (long i = 0; i < r; ++i) {
      int[] s = SIGMA[(int) (i % 10)];
      // NOTE: this differs from RFC7693 but follows the same code provided with
      // EIP-152. Its not clear to me why the EIP deviates from the RFC.
      G1G2(v, 0, 4, 8, 12, m[s[0]], m[s[4]]);
      G1G2(v, 1, 5, 9, 13, m[s[1]], m[s[5]]);
      G1G2(v, 2, 6, 10, 14, m[s[2]], m[s[6]]);
      G1G2(v, 3, 7, 11, 15, m[s[3]], m[s[7]]);

      G1G2(v, 0, 5, 10, 15, m[s[8]], m[s[12]]);
      G1G2(v, 1, 6, 11, 12, m[s[9]], m[s[13]]);
      G1G2(v, 2, 7, 8, 13, m[s[10]], m[s[14]]);
      G1G2(v, 3, 4, 9, 14, m[s[11]], m[s[15]]);
      /*            log.info(" { \"F\" : { \"r\" : [{}], \"h0_input\" : [{}],  \"h1_input\" : [{}], \"h2_input\" : [{}], \"h3_input\" : [{}], \"h4_input\" : [{}], \"h5_input\" : [{}],\"h6_input\" : [{}], \"h7_input\" : [{}],  \"m0\" : [{}], " +
                      "\"m1\" : [{}], \"m2\" : [{}], \"m3\" : [{}], \"m4\" : [{}], \"m5\" : [{}], \"m6\" : [{}], \"m7\" : [{}], \"m8\" : [{}], \"m9\" : [{}], \"m10\" : [{}], \"m11\" : [{}], \"m12\" : [{}], \"m13\" : [{}], \"m14\" : [{}], \"m15\" : [{}], " +
                      "\"t0\" : [{}], \"t1\" : [{}],\"f\" : [{}],  \"ms0\" : [{}],  \"ms1\" : [{}], \"ms2\" : [{}], \"ms3\" : [{}], \"ms4\" : [{}], \"ms5\" : [{}],\"ms6\" : [{}], \"ms7\" : [{}], \"ms8\" : [{}], \"ms9\" : [{}], \"ms10\" : [{}] , \"ms11\" : [{}], \"ms12\" : [{}] , \"ms13\" : [{}] , \"ms14\" : [{}] , \"ms15\" : [{}]}}",
              Long.toUnsignedString(r), Long.toUnsignedString(hh[0]),Long.toUnsignedString(hh[1]),Long.toUnsignedString(hh[2]),Long.toUnsignedString(hh[3]), Long.toUnsignedString(hh[4]), Long.toUnsignedString(hh[5]) , Long.toUnsignedString(hh[6]), Long.toUnsignedString(hh[7]), Long.toUnsignedString(m[0]),
              Long.toUnsignedString(m[1]), Long.toUnsignedString(m[2]), Long.toUnsignedString(m[3]), Long.toUnsignedString(m[4]), Long.toUnsignedString(m[5]), Long.toUnsignedString(m[6]), Long.toUnsignedString(m[7]), Long.toUnsignedString(m[8]), Long.toUnsignedString(m[9]), Long.toUnsignedString(m[10]), Long.toUnsignedString(m[11])
              , Long.toUnsignedString(m[12]), Long.toUnsignedString(m[13]), Long.toUnsignedString(m[14]), Long.toUnsignedString(m[15]), Long.toUnsignedString(t[0]), Long.toUnsignedString(t[1]), f ? 1 : 0, Long.toUnsignedString(m[s[0]]),Long.toUnsignedString(m[s[1]]),Long.toUnsignedString(m[s[2]]),Long.toUnsignedString(m[s[3]]), Long.toUnsignedString(m[s[4]]), Long.toUnsignedString(m[s[5]]) , Long.toUnsignedString(m[s[6]]), Long.toUnsignedString(m[s[7]]), Long.toUnsignedString(m[s[8]]), Long.toUnsignedString(m[s[9]]), Long.toUnsignedString(m[s[10]]), Long.toUnsignedString(m[s[11]]), Long.toUnsignedString(m[s[12]]), Long.toUnsignedString(m[s[13]]), Long.toUnsignedString(m[s[14]]), Long.toUnsignedString(m[s[15]])
      );*/
    }

    /*        log.info(" { \"F\" : { \"r\" : [{}], \"h0_input\" : [{}],  \"h1_input\" : [{}], \"h2_input\" : [{}], \"h3_input\" : [{}], \"h4_input\" : [{}], \"h5_input\" : [{}],\"h6_input\" : [{}], \"h7_input\" : [{}],  \"m0\" : [{}], " +
                    "\"m1\" : [{}], \"m2\" : [{}], \"m3\" : [{}], \"m4\" : [{}], \"m5\" : [{}], \"m6\" : [{}], \"m7\" : [{}], \"m8\" : [{}], \"m9\" : [{}], \"m10\" : [{}], \"m11\" : [{}], \"m12\" : [{}], \"m13\" : [{}], \"m14\" : [{}], \"m15\" : [{}], " +
                    "\"t0\" : [{}], \"t1\" : [{}],\"f\" : [{}],  \"v0\" : [{}],  \"v1\" : [{}], \"v2\" : [{}], \"v3\" : [{}], \"v4\" : [{}], \"v5\" : [{}],\"v6\" : [{}], \"v7\" : [{}], \"v8\" : [{}], \"v9\" : [{}], \"v10\" : [{}] , \"v11\" : [{}], \"v12\" : [{}] , \"v13\" : [{}] , \"v14\" : [{}] , \"v15\" : [{}]}}",
            Long.toUnsignedString(r), Long.toUnsignedString(hh[0]),Long.toUnsignedString(hh[1]),Long.toUnsignedString(hh[2]),Long.toUnsignedString(hh[3]), Long.toUnsignedString(hh[4]), Long.toUnsignedString(hh[5]) , Long.toUnsignedString(hh[6]), Long.toUnsignedString(hh[7]), Long.toUnsignedString(m[0]),
            Long.toUnsignedString(m[1]), Long.toUnsignedString(m[2]), Long.toUnsignedString(m[3]), Long.toUnsignedString(m[4]), Long.toUnsignedString(m[5]), Long.toUnsignedString(m[6]), Long.toUnsignedString(m[7]), Long.toUnsignedString(m[8]), Long.toUnsignedString(m[9]), Long.toUnsignedString(m[10]), Long.toUnsignedString(m[11])
            , Long.toUnsignedString(m[12]), Long.toUnsignedString(m[13]), Long.toUnsignedString(m[14]), Long.toUnsignedString(m[15]), Long.toUnsignedString(t[0]), Long.toUnsignedString(t[1]), f ? 1 : 0, Long.toUnsignedString(v[0]),Long.toUnsignedString(v[1]),Long.toUnsignedString(v[2]),Long.toUnsignedString(v[3]), Long.toUnsignedString(v[4]), Long.toUnsignedString(v[5]) , Long.toUnsignedString(v[6]), Long.toUnsignedString(v[7]), Long.toUnsignedString(v[8]), Long.toUnsignedString(v[9]), Long.toUnsignedString(v[10]), Long.toUnsignedString(v[11]), Long.toUnsignedString(v[12]), Long.toUnsignedString(v[13]), Long.toUnsignedString(v[14]), Long.toUnsignedString(v[15])
    );*/
    // Xor two halves
    for (int i = 0; i != 8; ++i) {
      h[i] ^= v[i] ^ v[i + 8];
    }
    log.info(
        " { \"F\" : { \"r\" : [{}], \"h0h1_be_input\" : [{}],  \"h2h3_be_input\" : [{}], \"h4h5_be_input\" : [{}], \"h6h7_be_input\" : [{}], \"m0m1_be\" : [{}], "
            + "\"m2m3_be\" : [{}], \"m4m5_be\" : [{}], \"m6m7_be\" : [{}], \"m8m9_be\" : [{}], \"m10m11_be\" : [{}], \"m12m13_be\" : [{}], \"m14m15_be\" : [{}] "
            + "\"t0t1_be\" : [{}], \"f\" : [{}],  \"h0h1_be\" : [{}],  \"h2h3_be\" : [{}], \"h4h5_be\" : [{}], \"h6h7_be\" : [{}]",
        Long.toUnsignedString(r),
        Bytes.fromHexString(
                "0x"
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {hh[0]})).toString()
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {hh[1]})).toString())
            .toUnsignedBigInteger(),
        Bytes.fromHexString(
                "0x"
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {hh[2]})).toString()
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {hh[3]})).toString())
            .toUnsignedBigInteger(),
        Bytes.fromHexString(
                "0x"
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {hh[4]})).toString()
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {hh[5]})).toString())
            .toUnsignedBigInteger(),
        Bytes.fromHexString(
                "0x"
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {hh[6]})).toString()
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {hh[7]})).toString())
            .toUnsignedBigInteger(),
        Bytes.fromHexString(
                "0x"
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {m[0]})).toString()
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {m[1]})).toString())
            .toUnsignedBigInteger(),
        Bytes.fromHexString(
                "0x"
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {m[2]})).toString()
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {m[3]})).toString())
            .toUnsignedBigInteger(),
        Bytes.fromHexString(
                "0x"
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {m[4]})).toString()
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {m[5]})).toString())
            .toUnsignedBigInteger(),
        Bytes.fromHexString(
                "0x"
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {m[6]})).toString()
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {m[7]})).toString())
            .toUnsignedBigInteger(),
        Bytes.fromHexString(
                "0x"
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {m[8]})).toString()
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {m[9]})).toString())
            .toUnsignedBigInteger(),
        Bytes.fromHexString(
                "0x"
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {m[10]})).toString()
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {m[11]})).toString())
            .toUnsignedBigInteger(),
        Bytes.fromHexString(
                "0x"
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {m[12]})).toString()
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {m[13]})).toString())
            .toUnsignedBigInteger(),
        Bytes.fromHexString(
                "0x"
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {m[14]})).toString()
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {m[15]})).toString())
            .toUnsignedBigInteger(),
        Bytes.fromHexString(
                "0x"
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {t[0]})).toString()
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {t[1]})).toString())
            .toUnsignedBigInteger(),
        f ? 1 : 0,
        Bytes.fromHexString(
                "0x"
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {h[0]})).toString()
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {hh[1]})).toString())
            .toUnsignedBigInteger(),
        Bytes.fromHexString(
                "0x"
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {h[2]})).toString()
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {hh[3]})).toString())
            .toUnsignedBigInteger(),
        Bytes.fromHexString(
                "0x"
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {h[4]})).toString()
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {hh[5]})).toString())
            .toUnsignedBigInteger(),
        Bytes.fromHexString(
                "0x"
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {h[6]})).toString()
                    + HexFormat.of().formatHex(toLittleEndianBytes(new long[] {hh[7]})).toString())
            .toUnsignedBigInteger());
    String ff = f ? "1" : "0";
    String message =
        " { \"F\" : { \"r\" : ["
            + Long.toUnsignedString(r)
            + "], \"h0h1_be_input\" : ["
            + Bytes.fromHexString(
                    "0x"
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {hh[0]}))
                            .toString()
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {hh[1]}))
                            .toString())
                .toUnsignedBigInteger()
            + "],  \"h2h3_be_input\" : ["
            + Bytes.fromHexString(
                    "0x"
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {hh[2]}))
                            .toString()
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {hh[3]}))
                            .toString())
                .toUnsignedBigInteger()
            + "], \"h4h5_be_input\" : ["
            + Bytes.fromHexString(
                    "0x"
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {hh[4]}))
                            .toString()
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {hh[5]}))
                            .toString())
                .toUnsignedBigInteger()
            + "], \"h6h7_be_input\" : ["
            + Bytes.fromHexString(
                    "0x"
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {hh[6]}))
                            .toString()
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {hh[7]}))
                            .toString())
                .toUnsignedBigInteger()
            + "],  \"m0m1_be\" : ["
            + Bytes.fromHexString(
                    "0x"
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {m[0]}))
                            .toString()
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {m[1]}))
                            .toString())
                .toUnsignedBigInteger()
            + "], "
            + "\"m2m3_be\" : ["
            + Bytes.fromHexString(
                    "0x"
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {m[2]}))
                            .toString()
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {m[3]}))
                            .toString())
                .toUnsignedBigInteger()
            + "], \"m4m5_be\" : ["
            + Bytes.fromHexString(
                    "0x"
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {m[4]}))
                            .toString()
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {m[5]}))
                            .toString())
                .toUnsignedBigInteger()
            + "], \"m6m7_be\" : ["
            + Bytes.fromHexString(
                    "0x"
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {m[6]}))
                            .toString()
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {m[7]}))
                            .toString())
                .toUnsignedBigInteger()
            + "], \"m8m9_be\" : ["
            + Bytes.fromHexString(
                    "0x"
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {m[8]}))
                            .toString()
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {m[9]}))
                            .toString())
                .toUnsignedBigInteger()
            + "], \"m10m11_be\" : ["
            + Bytes.fromHexString(
                    "0x"
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {m[10]}))
                            .toString()
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {m[11]}))
                            .toString())
                .toUnsignedBigInteger()
            + "], \"m12m13_be\" : ["
            + Bytes.fromHexString(
                    "0x"
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {m[12]}))
                            .toString()
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {m[13]}))
                            .toString())
                .toUnsignedBigInteger()
            + "], \"m14m15_be\" : ["
            + Bytes.fromHexString(
                    "0x"
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {m[14]}))
                            .toString()
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {m[15]}))
                            .toString())
                .toUnsignedBigInteger()
            + "], "
            + "\"t0t1_be\" : ["
            + Bytes.fromHexString(
                    "0x"
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {t[0]}))
                            .toString()
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {t[1]}))
                            .toString())
                .toUnsignedBigInteger()
            + "],\"f\" : ["
            + ff
            + "],  \"h0h1_be\" : ["
            + Bytes.fromHexString(
                    "0x"
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {h[0]}))
                            .toString()
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {h[1]}))
                            .toString())
                .toUnsignedBigInteger()
            + "],  \"h2h3_be\" : ["
            + Bytes.fromHexString(
                    "0x"
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {h[2]}))
                            .toString()
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {h[3]}))
                            .toString())
                .toUnsignedBigInteger()
            + "], \"h4h5_be\" : ["
            + Bytes.fromHexString(
                    "0x"
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {h[4]}))
                            .toString()
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {h[5]}))
                            .toString())
                .toUnsignedBigInteger()
            + "], \"h6h7_be\" : [ "
            + Bytes.fromHexString(
                    "0x"
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {h[6]}))
                            .toString()
                        + HexFormat.of()
                            .formatHex(toLittleEndianBytes(new long[] {h[7]}))
                            .toString())
                .toUnsignedBigInteger()
            + "] }}";
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("testG1G1.text", true))) {
      writer.write(message);
      writer.newLine();
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    for (int i = 0; i < 250; i++) {
      long generatedLong = 0 + (long) (Math.random() * (Math.pow(2, 64) - 0));
      Bytes bb = Bytes.ofUnsignedLong(generatedLong);
      HexFormat hex = HexFormat.of();
      byte[] bytes = hex.parseHex(bb.toString().substring(2));
      long leLong = fromLittleEndian64(bytes, 0);
      String mm =
          " { \"BE_to_LE_u64\" : { \"input\" : ["
              + Long.toUnsignedString(generatedLong)
              + "], \"output\" : ["
              + Long.toUnsignedString(leLong)
              + "] }}";
      if (generatedLong != 9223372036854775807L) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("BEtoLE.text", true))) {
          writer.write(mm);
          writer.newLine();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    }

    //
    return h;
  }

  private static void G1G2(long[] v, int a, int b, int c, int d, long x, long y) {
    long vA_input = v[a];
    long vB_input = v[b];
    long vC_input = v[c];
    long vD_input = v[d];
    G1(v, a, b, c, d, x);
    G2(v, a, b, c, d, y);
    String message =
        " { \"g1g2\" : { \"vA_input\" : [ "
            + Long.toUnsignedString(vA_input)
            + "], \"vB_input\" : ["
            + Long.toUnsignedString(vB_input)
            + "],  \"vC_input\" : [ "
            + Long.toUnsignedString(vC_input)
            + "], \"vD_input\" : ["
            + Long.toUnsignedString(vD_input)
            + "], \"x\" : ["
            + Long.toUnsignedString(x)
            + "], \"y\" : ["
            + Long.toUnsignedString(y)
            + "], \"vA\" : ["
            + Long.toUnsignedString(v[a])
            + "],\"vB\" : ["
            + Long.toUnsignedString(v[b])
            + "], \"vC\" : ["
            + Long.toUnsignedString(v[c])
            + "],  \"vD\" : [ "
            + Long.toUnsignedString(v[d])
            + "] }}";
    //  log.info(" { \"g1g2\" : { \"vA_input\" : [{}], \"vB_input\" : [{}],  \"vC_input\" : [{}],
    // \"vD_input\" : [{}], \"x\" : [{}], \"y\" : [{}], \"vA\" : [{}],\"vB\" : [{}], \"vC\" : [{}],
    // \"vD\" : [{}] }}",
    //     Long.toUnsignedString(vA_input),
    // Long.toUnsignedString(vB_input),Long.toUnsignedString(vC_input),Long.toUnsignedString(vD_input),Long.toUnsignedString(x), Long.toUnsignedString(y), Long.toUnsignedString(v[a]) , Long.toUnsignedString(v[b]), Long.toUnsignedString(v[c]), Long.toUnsignedString(v[d]));
    // try (BufferedWriter writer = new BufferedWriter(new FileWriter("testG1G1.text", true))) {
    // writer.write(message);
    // writer.newLine();
    // }
    // catch (IOException ex) {
    //  ex.printStackTrace();
    // }
  }

  private static void G1(long[] v, int a, int b, int c, int d, long x) {

    v[a] = (v[a] + v[b] + x);
    v[d] = Long.rotateRight(v[d] ^ v[a], 32);
    v[c] = (v[c] + v[d]);
    v[b] = Long.rotateRight(v[b] ^ v[c], 24);
  }

  private static void G2(long[] v, int a, int b, int c, int d, long y) {
    v[a] = (v[a] + v[b] + y);
    v[d] = Long.rotateRight(v[d] ^ v[a], 16);
    v[c] = (v[c] + v[d]);
    v[b] = Long.rotateRight(v[b] ^ v[c], 63);
  }

  // Blake2b Initialisation Vector
  private static final long[] IV = {
    0x6A09E667F3BCC908L,
    0xBB67AE8584CAA73BL,
    0x3C6EF372FE94F82BL,
    0xA54FF53A5F1D36F1L,
    0x510E527FADE682D1L,
    0x9B05688C2B3E6C1FL,
    0x1F83D9ABFB41BD6BL,
    0x5BE0CD19137E2179L
  };

  private static final int[][] SIGMA =
      new int[][] {
        {0, 2, 4, 6, 1, 3, 5, 7, 8, 10, 12, 14, 9, 11, 13, 15},
        {14, 4, 9, 13, 10, 8, 15, 6, 1, 0, 11, 5, 12, 2, 7, 3},
        {11, 12, 5, 15, 8, 0, 2, 13, 10, 3, 7, 9, 14, 6, 1, 4},
        {7, 3, 13, 11, 9, 1, 12, 14, 2, 5, 4, 15, 6, 10, 0, 8},
        {9, 5, 2, 10, 0, 7, 4, 15, 14, 11, 6, 3, 1, 12, 8, 13},
        {2, 6, 0, 8, 12, 10, 11, 3, 4, 7, 15, 1, 13, 5, 14, 9},
        {12, 1, 14, 4, 5, 15, 13, 10, 0, 6, 9, 8, 7, 3, 2, 11},
        {13, 7, 12, 3, 11, 14, 1, 9, 5, 15, 8, 2, 0, 4, 6, 10},
        {6, 14, 11, 0, 15, 9, 3, 8, 12, 13, 1, 10, 2, 7, 4, 5},
        {10, 8, 7, 1, 2, 4, 6, 5, 15, 9, 3, 13, 11, 14, 12, 0},
      };
}
