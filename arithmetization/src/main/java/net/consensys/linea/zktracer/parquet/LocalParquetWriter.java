package net.consensys.linea.zktracer.parquet;

// Generic Avro dependencies
import net.consensys.linea.zktracer.AvroAddTrace;
import org.apache.avro.Schema;

// Generic Parquet dependencies
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.io.OutputFile;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;

// Avro->Parquet dependencies
import org.apache.parquet.avro.AvroParquetWriter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@SuppressWarnings({"deprecation"})
public class LocalParquetWriter implements AutoCloseable{

    private final Schema avroSchema;
    final private org.apache.parquet.hadoop.ParquetWriter<Object> parquetWriter;
    public LocalParquetWriter(String moduleName) throws IOException {
        this(moduleName, 10_000);
    }

    public LocalParquetWriter(String moduleName, int size) throws IOException {

        OutputFile filePath = new LocalOutputFile(Paths.get(moduleName+"Traces.parquet"));

        avroSchema = AvroAddTrace.getClassSchema();
//        GroupWriteSupport.setSchema(schema, configuration);
//        SimpleGroupFactory f = new SimpleGroupFactory(schema);
//        ParquetWriter<Group> writer = new ParquetWriter<Group>(outFile, new GroupWriteSupport(), codec, blockSize,
//                pageSize, DICT_PAGE_SIZE, true, false, version, configuration);
        parquetWriter = AvroParquetWriter.builder(filePath)
                .withWriteMode(ParquetFileWriter.Mode.OVERWRITE)
//                        .withDataModel(GenericData.get()) <- likely to use to add BigInteger on 124 bits
                //https://stackoverflow.com/questions/35789412/spark-sql-difference-between-gzip-vs-snappy-vs-lzo-compression-formats#:~:text=GZIP%20compresses%20data%2030%25%20more,GZip%20compression%20is%20still%20better
                .withCompressionCodec(CompressionCodecName.SNAPPY)
                .withSchema(avroSchema)
                .withRowGroupSize(size)
                .build();
    }

    public void write(List<AvroAddTrace> traces){
       try{

            for(AvroAddTrace obj : traces){
                parquetWriter.write(obj);
                System.out.println(parquetWriter.getDataSize());
            }
        }catch(java.io.IOException e){
            System.out.printf("Error writing parquet file %s%n", e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void close() throws IOException {
        parquetWriter.close();
    }
}
