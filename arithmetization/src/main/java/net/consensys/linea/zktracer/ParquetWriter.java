package net.consensys.linea.zktracer;

// Generic Avro dependencies
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.add.AvroAddTrace;
import org.apache.avro.Schema;

// Hadoop stuff
import org.apache.hadoop.fs.Path;

// Generic Parquet dependencies
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;

// Avro->Parquet dependencies
import org.apache.parquet.avro.AvroSchemaConverter;
import org.apache.parquet.avro.AvroParquetWriter;

import java.util.List;

@SuppressWarnings({"rawtypes", "deprecation", "unchecked"})
public class ParquetWriter {
    public static void write(Module m, List<AvroAddTrace> traces){
        Schema avroSchema = AvroAddTrace.getClassSchema();
        MessageType parquetSchema = new AvroSchemaConverter().convert(avroSchema);

        Path filePath = new Path(m.jsonKey()+"Traces.parquet");
        int blockSize = 1024;
        int pageSize = 65535;
        try(
                var parquetWriter = AvroParquetWriter.builder(filePath)
                        .withWriteMode(ParquetFileWriter.Mode.OVERWRITE)
                        .withSchema(avroSchema)
                        .build()
        ){
            for(AvroAddTrace obj : traces){
                parquetWriter.write(obj);
            }
        }catch(java.io.IOException e){
            System.out.printf("Error writing parquet file %s%n", e.getMessage());
            throw new RuntimeException(e);
        }

    }

}
