package net.consensys.linea.zktracer.module.gas;

import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

public class GasChunk extends ModuleOperation {

  @Override
  protected int computeLineCount() {
    return 0;
  }

  public void trace(int stamp, Trace trace) {
    int maxCt = 7;
    for (short i = 0; i < maxCt + 1; i++) {
      trace
          .stamp(stamp)
          .ct(i)
          .gasActl(0)
          .gasCost(Bytes.of(0))
          .oogx(false)
          .byte1(UnsignedByte.ZERO)
          .byte2(UnsignedByte.ZERO)
          .acc1(Bytes.of(0))
          .acc2(Bytes.of(0))
          .validateRow();
    }
  }
}
