 package net.consensys.linea.zktracer.container.stacked;

 import net.consensys.linea.zktracer.types.EWord;
 import org.apache.tuweni.bytes.Bytes32;
 import org.junit.jupiter.api.Assertions;
 import org.junit.jupiter.api.Test;
 import  org.apache.tuweni.bytes.*;
 import net.consensys.linea.zktracer.module.wcp.WcpOperation;
 import org.apache.tuweni.bytes.*;

 class StackedSetTest {

  @Test
  void add() {
    Bytes32 a = Bytes32.ZERO.copy().mutableCopy();
    EWord ew = EWord.ofHexString(a.toHexString());
    // Even though both are Bytes32, the equal method fails on them:
    // Assertions.assertTrue(ew.equals(a));

    WcpOperation wo1 = new WcpOperation(WcpOperation.LEQbv, a, a);
    WcpOperation wo2 = new WcpOperation(WcpOperation.LEQbv, ew, ew);
    Assertions.assertTrue(wo1.equals(wo2));
  }
 }
