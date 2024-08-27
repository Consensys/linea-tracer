package net.consensys.linea.zktracer.module.hub.section.halt;

import net.consensys.linea.zktracer.module.hub.Hub;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ReturnSectionTest {

  @Test
  public void childFrameShouldBeSetForRomLex(){
    Hub hub = new Hub(Mockito.mock(Address.class), Bytes.EMPTY);

    MessageFrame childFrame = Mockito.mock(MessageFrame.class);
    hub.callStack().callFrames();

    ReturnSection returnSection = new ReturnSection(hub);

    Assertions.assertNotNull(returnSection.getChildFrame());
    Assertions.assertEquals(childFrame, returnSection.getChildFrame());
  }
}