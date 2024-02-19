package linea.plugin.acc.test.compress;

import net.consensys.linea.compress.LibCompress;
import org.hyperledger.besu.tests.acceptance.dsl.AcceptanceTestBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CompressTest extends AcceptanceTestBase {

    @Test
    public void testCompress() {
        final int compressedSize = LibCompress.CompressedSize(new byte[128], 128);

        // should not error
        assertThat(compressedSize).isGreaterThan(0);

        // should have compressed into 1 backref + header, must be less than 10
        assertThat(compressedSize).isLessThan(10);
    }

}
