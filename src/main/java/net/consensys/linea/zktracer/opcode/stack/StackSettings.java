package net.consensys.linea.zktracer.opcode.stack;

import net.consensys.linea.zktracer.opcode.Gas;

// TODO: maybe a builder?
public record StackSettings(
    Pattern pattern,
    int alpha,
    int delta,
    int nbAdded,
    int nbRemoved,
    Gas staticGas,
    boolean twoLinesInstruction,
    boolean staticInstruction,
    boolean addressTrimmingInstruction,
    boolean oobFlag,
    boolean flag1,
    boolean flag2,
    boolean flag3,
    boolean flag4) {}
