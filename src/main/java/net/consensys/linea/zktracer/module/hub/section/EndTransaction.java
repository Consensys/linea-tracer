package net.consensys.linea.zktracer.module.hub.section;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.chunks.TraceFragment;

public class EndTransaction extends TraceSection {
  public EndTransaction(TraceFragment... chunks) {
    super(chunks);
  }

  @Override
  void seal(Hub hub) {}
}
