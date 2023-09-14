package net.consensys.linea.zktracer.module.mmu;

import java.util.List;

import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.opcode.OpCode;

public class Mmu implements Module {
  @Override
  public String jsonKey() {
    return "mmu";
  }

  @Override
  public List<OpCode> supportedOpCodes() {
    return null;
  }

  @Override
  public Object commit() {
    return null;
  }
}
