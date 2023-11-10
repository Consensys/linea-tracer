//package net.consensys.linea.zktracer;
//
//import com.google.common.base.Stopwatch;
//import net.tlabs.tablesaw.parquet.TablesawParquetReader;
//import net.tlabs.tablesaw.parquet.TablesawParquetWriteOptions;
//import net.tlabs.tablesaw.parquet.TablesawParquetWriter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import tech.tablesaw.api.LongColumn;
//import tech.tablesaw.api.Table;
//import tech.tablesaw.columns.Column;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Paths;
//import java.util.stream.LongStream;
//
//public class Maintest {
//
//    private static final TablesawParquetReader PARQUET_READER = new TablesawParquetReader();
//    private static final TablesawParquetWriter parquetWriter = new TablesawParquetWriter();
//    public static void main(String[] args) throws IOException {
//        System.setProperty("log4j.debug", "");
//        Logger logger = LoggerFactory.getLogger(Maintest.class);
//        logger.info("This is how you configure Log4J with SLF4J");
//        logger.debug("This is how you configure Log4J with SLF4J");
//
//
//      File file = Paths.get("tablesaw.parquet").toFile();
//      if (file.exists()) {
//          file.delete();
//      }
//      file.createNewFile();
//
//      Column<Long> c1 = LongColumn.create("testing1", LongStream.range(1L, 1_000_000));
//      Column<Long> c2 = LongColumn.create("testing2", LongStream.range(1L, 1_000_000));
//      Column<Long> c3 = LongColumn.create("testing3", LongStream.range(1L, 1_000_000));
//      Column<Long> c4 = LongColumn.create("testing4", LongStream.range(1L, 1_000_000));
//      Column<Long> c5 = LongColumn.create("testing5", LongStream.range(1L, 1_000_000));
//      Column<Long> c6 = LongColumn.create("testing6", LongStream.range(1L, 1_000_000));
//      var toBePersisted = Table.create(c1, c2, c3, c4, c5, c6);
//
//      Stopwatch sw = Stopwatch.createStarted();
//      parquetWriter.write(toBePersisted, TablesawParquetWriteOptions.builder(file).withCompressionCode(TablesawParquetWriteOptions.CompressionCodec.ZSTD)
//              .build());
//      System.out.println("Writing Parquet took " + sw);
//
//      sw.reset();
//      toBePersisted.write().csv("summary.csv");
//      System.out.println("Writing csv took " + sw);
//}
//}
