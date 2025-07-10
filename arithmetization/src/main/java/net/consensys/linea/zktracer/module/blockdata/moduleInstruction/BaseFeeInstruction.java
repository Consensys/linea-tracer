package net.consensys.linea.zktracer.module.blockdata.moduleInstruction;

import static net.consensys.linea.zktracer.TraceCancun.Blockdata.nROWS_BL;

public class BaseFeeInstruction extends BlockDataInstruction {

  public void handle() {
    // Implementation for handling the GasLimit instruction
  }

  public int nbRows() {
    return nROWS_BL;
  }
}
