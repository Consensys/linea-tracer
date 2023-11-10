package net.consensys.linea;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.ColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.orc.OrcFile;
import org.apache.orc.Reader;
import org.apache.orc.RecordReader;
import org.apache.orc.TypeDescription;
import org.apache.orc.impl.ReaderImpl;

import java.io.IOException;

//public class ORCReader {

//    public static void main(String[] args) throws IOException {
//        OrcFile.ReaderOptions conf = new OrcFile.ReaderOptions(new Configuration());
//        Reader reader = new ReaderImpl(new Path("C:\\Users\\huc_c\\IdeaProjects\\linea-besu-plugin\\arithmetization\\traces.orc"), conf);
//
//        System.out.println(reader.getNumberOfRows());
//        System.out.println(reader.getSchema());
//        VectorizedRowBatch batch = reader.getSchema().createRowBatch();
//        reader.rows().nextBatch(batch);
//        for(ColumnVector col:batch.cols){
//            col.
//        }
//    }

public class ORCReader {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Path orcFilePath = new Path("C:\\Users\\huc_c\\IdeaProjects\\linea-besu-plugin\\arithmetization\\traces.orc");

        Reader reader = OrcFile.createReader(orcFilePath, OrcFile.readerOptions(conf));
        TypeDescription schema = reader.getSchema();
        System.out.println("Schema: " + schema.toString());

        RecordReader rows = reader.rows();
        VectorizedRowBatch batch = schema.createRowBatch();

        int count = 0;
//        while (count < 2 && rows.nextBatch(batch)) {
//            count += 1;
//            for (int row = 0; row < batch.size; row++) {
//                for (int col = 0; col < batch.numCols; col++) {
//                    ColumnVector colVector = batch.cols[col];
//                    System.out.println(colVector.toString());
//                }
//            }
//        }

        rows.close();
    }
}
