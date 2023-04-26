package net.consensys.linea.zktracer.module.trm;

import net.consensys.linea.zktracer.bytestheta.BaseBytes;
import org.apache.tuweni.bytes.Bytes32;

public class TrmData {
  private final BaseBytes arg1;

  public TrmData(final Bytes32 arg1) {
    this.arg1 = BaseBytes.fromBytes32(arg1);
  }

  public BaseBytes getArg1() {
    return arg1;
  }
}
