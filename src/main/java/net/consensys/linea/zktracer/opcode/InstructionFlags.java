package net.consensys.linea.zktracer.opcode;

public enum InstructionFlags {
  Revert,
  Jump,
  InvalidInstruction,
  SpecialPCUpdate,
  Push,
  Call,
  Return,
  Stop,
  NonStatic,
}
