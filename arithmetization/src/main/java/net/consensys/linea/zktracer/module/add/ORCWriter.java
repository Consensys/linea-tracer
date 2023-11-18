
package net.consensys.linea.zktracer.module.add;

        import net.consensys.linea.zktracer.module.FW;

        import java.io.RandomAccessFile;
        import java.io.IOException;

/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
        public class ORCWriter {


        public static CompressedFileWriter<?>[] getWriter(Path folder, String pattern) throws IOException {

        CompressedFileWriter<?>[] res = new CompressedFileWriter<?>[]{
        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "ACC_1", "rw"))),
        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "ACC_2", "rw"))),
        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "ARG_1_HI", "rw"))),
        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "ARG_1_LO", "rw"))),
        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "ARG_2_HI", "rw"))),
        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "ARG_2_LO", "rw"))),
        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "BYTE_1", "rw"))),
        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "BYTE_2", "rw"))),
        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "CT", "rw"))),
        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "INST", "rw"))),
        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "OVERFLOW", "rw"))),
        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "RES_HI", "rw"))),
        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "RES_LO", "rw"))),
        new CompressedFileWriter<>(new FW(new RandomAccessFile(path + "STAMP", "rw")))
        };
        return res;
        }
        }









