package net.consensys.linea.zktracer.module.euc;

import static net.consensys.linea.zktracer.Trace.LLARGE;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;

@Getter()
@Accessors(fluent = true)
public class EucCall {
  private final UnsignedByte instruction;
  private final Bytes arg1Hi;
  private final Bytes arg1Lo;
  private final Bytes arg2Hi;
  private final Bytes arg2Lo;
  private final Bytes result;

  public EucCall(Euc euc, Bytes arg1, Bytes arg2) {
    this.instruction = UnsignedByte.of(0);
    final Bytes32 arg1Bytes32 = Bytes32.leftPad(arg1);
    final Bytes32 arg2Bytes32 = Bytes32.leftPad(arg2);
    this.arg1Hi = arg1Bytes32.slice(0, LLARGE);
    this.arg1Lo = arg1Bytes32.slice(LLARGE, LLARGE);
    this.arg2Hi = arg2Bytes32.slice(0, LLARGE);
    this.arg2Lo = arg2Bytes32.slice(LLARGE, LLARGE);
    this.result = euc.callEUC(arg1, arg2).quotient();
  }

  public static EucCall eucCall(Euc euc, Bytes arg1, Bytes arg2) {
    return new EucCall(euc, arg1, arg2);
  }
}
