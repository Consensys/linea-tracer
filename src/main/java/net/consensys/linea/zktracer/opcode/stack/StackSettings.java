package net.consensys.linea.zktracer.opcode.stack;

public record StackSettings(
    Pattern pattern,
    int alpha,
    int delta,
    int nbAdded,
    int nbRemoved,
    int staticGas,
    boolean twoLinesInstruction,
    boolean staticInstruction,
    boolean oobFlag,
    boolean flag1,
    boolean flag2,
    boolean flag3,
    boolean flag4) {}
