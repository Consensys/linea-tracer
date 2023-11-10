//package net.consensys.linea.zktracer.persistence;
//
//import net.tlabs.tablesaw.parquet.TablesawParquetWriteOptions.CompressionCodec;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.common.base.Stopwatch;
//import net.consensys.linea.zktracer.AvroAddTrace;
//import net.tlabs.tablesaw.parquet.TablesawParquetReadOptions;
//import net.tlabs.tablesaw.parquet.TablesawParquetReader;
//import net.tlabs.tablesaw.parquet.TablesawParquetWriteOptions;
//import net.tlabs.tablesaw.parquet.TablesawParquetWriter;
//import org.apache.spark.sql.Dataset;
//import org.apache.spark.sql.Row;
//import org.apache.spark.sql.SaveMode;
//import org.apache.spark.sql.SparkSession;
//import org.jetbrains.annotations.NotNull;
//import org.junit.jupiter.api.Test;
//
//import java.io.File;
//import java.io.IOException;
//import java.math.BigInteger;
//import java.nio.ByteBuffer;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.LongStream;
//
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.hive.ql.exec.vector.BytesColumnVector;
//import org.apache.hadoop.hive.ql.exec.vector.LongColumnVector;
//import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
//import org.apache.orc.OrcFile;
//import org.apache.orc.TypeDescription;
//import org.apache.orc.Writer;
//
//import java.util.Random;
//
//import tech.tablesaw.api.LongColumn;
//import tech.tablesaw.api.Table;
//import tech.tablesaw.columns.Column;
//
//@SuppressWarnings("rawtypes")
//public class ParquetWritterTest {
//    @Test
//    public void orcTest() throws IOException {
//        var type = """
//                struct<ACC_1:binary,ACC_2:binary,ARG_1_HI:binary,ARG_1_LO:binary,ARG_2_HI:binary,ARG_2_LO:binary,BYTE_1:binary,BYTE_2:binary,CT:binary,INST:binary,OVERFLOW:boolean,RES_HI:binary,RES_LO:binary,STAMP:binary>""";
//        Configuration conf = new Configuration();
//        TypeDescription schema = TypeDescription.fromString(type);
//
//        Writer writer = OrcFile.createWriter(new Path("/tmp/my-file.orc"),
//                OrcFile.writerOptions(conf)
//                        .setSchema(schema));
//
//        VectorizedRowBatch batch = schema.createRowBatch();
//        BytesColumnVector acc1 = (BytesColumnVector) batch.cols[0];
//        BytesColumnVector acc2 = (BytesColumnVector) batch.cols[1];
//        BytesColumnVector arg1Hi = (BytesColumnVector) batch.cols[2];
//        BytesColumnVector arg1Lo = (BytesColumnVector) batch.cols[3];
//        BytesColumnVector arg2Hi = (BytesColumnVector) batch.cols[4];
//        BytesColumnVector arg2Lo = (BytesColumnVector) batch.cols[5];
//        BytesColumnVector byte1 = (BytesColumnVector) batch.cols[6];
//        BytesColumnVector byte2 = (BytesColumnVector) batch.cols[7];
//        BytesColumnVector ct = (BytesColumnVector) batch.cols[8];
//        BytesColumnVector inst = (BytesColumnVector) batch.cols[9];
//        LongColumnVector overflow = (LongColumnVector) batch.cols[10];
//        BytesColumnVector resHi = (BytesColumnVector) batch.cols[11];
//        BytesColumnVector resLo = (BytesColumnVector) batch.cols[12];
//        BytesColumnVector stamp = (BytesColumnVector) batch.cols[13];
//
//        Random rand = new Random();
//        Stopwatch sw = Stopwatch.createStarted();
//        for (int r = 0; r < 1_000_000; ++r) {
//            int row = batch.size++;
//            acc1.setVal(row, BigInteger.valueOf(rand.nextLong()).toByteArray());
//            acc2.setVal(row, BigInteger.valueOf(rand.nextLong()).toByteArray());
//            arg1Hi.setVal(row, BigInteger.valueOf(rand.nextLong()).toByteArray());
//            arg1Lo.setVal(row, BigInteger.valueOf(rand.nextLong()).toByteArray());
//            arg2Hi.setVal(row, BigInteger.valueOf(rand.nextLong()).toByteArray());
//            arg2Lo.setVal(row, BigInteger.valueOf(rand.nextLong()).toByteArray());
//            byte1.setVal(row, BigInteger.valueOf(rand.nextLong()).toByteArray());
//            byte2.setVal(row, BigInteger.valueOf(rand.nextLong()).toByteArray());
//            ct.setVal(row, BigInteger.valueOf(rand.nextLong()).toByteArray());
//            inst.setVal(row, BigInteger.valueOf(rand.nextLong()).toByteArray());
//            overflow.vector[row] = 0L;
//            resHi.setVal(row, BigInteger.valueOf(rand.nextLong()).toByteArray());
//            resLo.setVal(row, BigInteger.valueOf(rand.nextLong()).toByteArray());
//            stamp.setVal(row, BigInteger.valueOf(rand.nextLong()).toByteArray());
//
//            if (batch.size == batch.getMaxSize()) {
//                writer.addRowBatch(batch);
//                batch.reset();
//            }
//        }
//        if (batch.size != 0) {
//            writer.addRowBatch(batch);
//            batch.reset();
//        }
//        writer.close();
//        System.out.println(sw.elapsed());
//    }
//    private static final TablesawParquetWriter PARQUET_WRITER = new TablesawParquetWriter();
//
//    private static final TablesawParquetReader PARQUET_READER = new TablesawParquetReader();
//
//    @Test
//    public void tableSaw() throws IOException {
//        // using the file name
//        String FILENAME = "blablaTraces.parquet";
//        File FILE = Paths.get("tablesaw.parquet").toFile();
//        if (FILE.exists()) {
//            FILE.delete();
//        }
//        FILE.createNewFile();
//
////        final Table orig = PARQUET_READER
////                .read(TablesawParquetReadOptions.builder(FILENAME).build());
//
//        System.out.println("----------------");
//
//        Column c1 = LongColumn.create("testing1", LongStream.range(1L, 1_000_000));
//        Column c2 = LongColumn.create("testing2", LongStream.range(1L, 1_000_000));
//        Column c3 = LongColumn.create("testing3", LongStream.range(1L, 1_000_000));
//        Column c4 = LongColumn.create("testing4", LongStream.range(1L, 1_000_000));
//        Column c5 = LongColumn.create("testing5", LongStream.range(1L, 1_000_000));
//        Column c6 = LongColumn.create("testing6", LongStream.range(1L, 1_000_000));
//        var toBePersisted = Table.create(c1, c2, c3, c4, c5, c6);
//
//        Stopwatch sw = Stopwatch.createStarted();
//        PARQUET_WRITER.write(toBePersisted, TablesawParquetWriteOptions.builder(FILE).withCompressionCode(CompressionCodec.UNCOMPRESSED)
//                .build());
//        System.out.println("Writing Parquet took " + sw);
//        final Table dest = PARQUET_READER.read(TablesawParquetReadOptions.builder(FILE).build());
////        Assertions.assertEquals(orig.columnCount(), dest.columnCount());
//        sw.reset();
//        toBePersisted.write().csv("summary.csv");
//        System.out.println("Writing csv took " + sw);
//    }
//
////    @Test
////    public void writeParquetFile() throws IOException {
////        LocalParquetWriter parquetWriter = new LocalParquetWriter("test", 10000);
////
////        List<AvroAddTrace> addTraces = getAvroAddTraces();
////        parquetWriter.write(addTraces);
////        parquetWriter.close();
////    }
//
//
//    @NotNull
//    private List<AvroAddTrace> getAvroAddTraces() throws IOException {
//        var addJson = Paths.get("./src/test/resources/trace_samples/addTrace_large.json").toFile();
//        ObjectMapper o = new ObjectMapper();
//        var addMapTrace = o.readValue(addJson, Map.class);
//        int size = ((List) addMapTrace.get("ACC_1")).size();
//        List<AvroAddTrace> addTraces = new ArrayList<>();
//
//        for (int i = 0; i < size; i++) {
//            addTraces.add(
//                    new AvroAddTrace(
//                            toByteBuffer(((List) addMapTrace.get("ACC_1")).get(i)),
//                            toByteBuffer(((List) addMapTrace.get("ACC_2")).get(i)),
//                            toByteBuffer(((List) addMapTrace.get("ARG_1_HI")).get(i)),
//                            toByteBuffer(((List) addMapTrace.get("ARG_1_LO")).get(i)),
//                            toByteBuffer(((List) addMapTrace.get("ARG_2_HI")).get(i)),
//                            toByteBuffer(((List) addMapTrace.get("ARG_2_LO")).get(i)),
//                            ByteBuffer.wrap(new byte[]{((Integer) ((List) addMapTrace.get("BYTE_1")).get(i)).byteValue()}),
//                            ByteBuffer.wrap(new byte[]{((Integer) ((List) addMapTrace.get("BYTE_2")).get(i)).byteValue()}),
//                            toByteBuffer(((List) addMapTrace.get("CT")).get(i)),
//                            toByteBuffer(((List) addMapTrace.get("INST")).get(i)),
//                            ((List) addMapTrace.get("OVERFLOW")).get(i).equals(1),
//                            toByteBuffer(((List) addMapTrace.get("RES_HI")).get(i)),
//                            toByteBuffer(((List) addMapTrace.get("RES_LO")).get(i)),
//                            toByteBuffer(((List) addMapTrace.get("STAMP")).get(i))
//                    )
//            );
//
//        }
//        return addTraces;
//    }
//
////    public void writeThreeLines() throws IOException {
////        List<AvroAddTrace> addTraces = new ArrayList<>();
////        LocalParquetWriter parquetWriter = new LocalParquetWriter("test3Lines");
////        for (int i = 0; i < 3; i++) {
////            addTraces.add(
////                    new AvroAddTrace(
////                            ((Integer) i).longValue(),
////                            3L - ((Integer) i).longValue(),
////                            1L,
////                            1L,
////                            1L,
////                            1L,
////                            ByteBuffer.wrap(new byte[]{0}),
////                            ByteBuffer.wrap(new byte[]{0}),
////                            1L,
////                            1L,
////                            true,
////                            1L,
////                            1L,
////                            1L
////                    )
////            );
////            parquetWriter.write(addTraces);
////            addTraces.clear();
////        }
////        parquetWriter.write(addTraces);
////        parquetWriter.close();
////
////        InputFile inputFile = new LocalInputFile(Paths.get("C:\\Users\\huc_c\\IdeaProjects\\linea-besu-plugin\\arithmetization\\test3LinesTraces.parquet"));
////        ParquetReader<GenericRecord> reader = AvroParquetReader.<GenericRecord>builder(inputFile).build();
////        Assertions.assertEquals(0L, reader.read().get("ACC1"));
////        Assertions.assertEquals(1L, reader.read().get("ACC1"));
////        Assertions.assertEquals(2L, reader.read().get("ACC1"));
////    }
//
//
//    private ByteBuffer toByteBuffer(Object acc2) {
//        if (acc2 instanceof Integer) {
//            return ByteBuffer.wrap(BigInteger.valueOf((Integer) acc2).toByteArray());
//        } else if (acc2 instanceof String) {
//            return ByteBuffer.wrap(new BigInteger((String) acc2).toByteArray());
//        }
//        throw new RuntimeException("Unsupported type " + acc2);
//    }
//
////    public void hiveTest() throws IOException {
////
////
////
////        FileSystem fs = FileSystem.getLocal(new Configuration()).getRawFileSystem();
////        //Read the data file from the file system
//////        Path inputfile = new LocalInputFile("file:///C:/RAW_DATA_IN_JSON_FORMAT.txt") ;
////        //Path of the output Parquet file
//////        Path outputPath = new Path("file:///C:/OUTPUT_PARQUET_FILE") ;
////
////
////
////
////        // The AVRO schema can be infered by looking at the first record of the data file.
////        //If you already have an AVRO schema file please uncomment the schema parser below to read that instead.
//////        Schema jsonSchema = JsonUtil.inferSchema(fs.open(inputfile), "RecordName", 1);
////         Schema jsonSchema = new Schema.Parser().parse(new File("C:\\Users\\huc_c\\IdeaProjects\\linea-besu-plugin\\arithmetization\\src\\main\\avro\\avroFormat.avsc"));
////
////        //Prints the AVRO schema being used
////        System.out.println( "jsonSchema.getDoc():"  +jsonSchema.toString(true) ) ;
////
////        OutputFile outputPath = new LocalOutputFile(Paths.get("blablaTraces.parquet"));
////
////
////            try (ParquetWriter<GenericRecord> writer = AvroParquetWriter
////                    .<GenericRecord>builder(outputPath)
////                    .withCompressionCodec(CompressionCodecName.SNAPPY)
////                    .withSchema(jsonSchema)
////                    .withPageSize(64 * 1024)
////                    .build()) {
////                List<AvroAddTrace> addTraces = getAvroAddTraces();
////                for (GenericRecord record : addTraces) {
////
////                    writer.write(record);
////
////
////                }
////            }
////        }
//
//
//    //    @Test
//    public void sparkTest() {
//        SparkSession spark = SparkSession.builder()
//                .appName("parquet-test")
//                .config("spark.master", "local")
//                .getOrCreate();
////        var df = spark.read().format("avro")
////                .load("C:\\Users\\huc_c\\IdeaProjects\\linea-besu-plugin\\arithmetization\\src\\main\\avro\\avroFormat.avro");
////        df.show();
////        df.printSchema();
////
////        df.write().mode(SaveMode.Overwrite)
////                .parquet("sparkTest.parquet");
//
//        Dataset<Row> usersDF = spark.read().load("blablaTraces.parquet");
////        Dataset<Row> usersDF = Dataset.toDF("e");
//        usersDF.printSchema();
//        usersDF.show();
//
//        Stopwatch sw = Stopwatch.createStarted();
//        usersDF.write().format("parquet").mode(SaveMode.Overwrite).parquet("copy.parquet");
//        System.out.println("---------" + sw.elapsed());
////
//
////        sw.reset();
////        usersDF.write().orc("copy2.orc");
////        System.out.println("---------" + sw.elapsed());
//
//    }
//}
