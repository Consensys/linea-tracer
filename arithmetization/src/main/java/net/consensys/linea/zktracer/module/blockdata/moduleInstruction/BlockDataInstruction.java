package net.consensys.linea.zktracer.module.blockdata.moduleInstruction;

import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.blockdata.BlockDataExoCall;

public abstract class BlockDataInstruction {

  /** Store all wcp and euc computations with params and results */
  public final BlockDataExoCall[] exoCalls = new BlockDataExoCall[nbRows()];

  public abstract void handle();

  public abstract int nbRows();

  public abstract void traceInstruction(Trace.Blockdata trace);
}
