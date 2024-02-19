package net.consensys.linea.zktracer.module.gas;

import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;
import static net.consensys.linea.zktracer.types.Conversions.booleanToInt;

import java.math.BigInteger;

import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

public class GasChunk extends ModuleOperation {

  final short maxCt = 7;
  GasParameters gasParameters;
  Bytes acc1;
  Bytes acc2;

  public GasChunk(GasParameters gasParameters) {
    this.gasParameters = gasParameters;
    acc1 = bigIntegerToBytes(gasParameters.gasActl());
    acc2 =
        bigIntegerToBytes(
            (BigInteger.valueOf((2L * booleanToInt(gasParameters.oogx()) - 1))
                    .multiply(gasParameters.gasCost().subtract(gasParameters.gasActl())))
                .subtract(BigInteger.valueOf(booleanToInt(gasParameters.oogx()))));
  }

  @Override
  protected int computeLineCount() {
    return maxCt + 1;
  }

  public void trace(int stamp, Trace trace) {
    for (short i = 0; i < maxCt + 1; i++) {
      trace
          .stamp(stamp)
          .ct(i)
          .gasActl(gasParameters.gasActl().longValue())
          .gasCost(bigIntegerToBytes(gasParameters.gasCost()))
          .oogx(gasParameters.oogx())
          .byte1(UnsignedByte.of(acc1.get(i)))
          .byte2(UnsignedByte.of(acc2.get(i)))
          .acc1(acc1.slice(0, i + 1))
          .acc2(acc2.slice(0, i + 1))
          .validateRow();
    }
  }
}
