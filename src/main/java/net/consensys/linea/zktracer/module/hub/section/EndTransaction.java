package net.consensys.linea.zktracer.module.hub.section;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.chunks.TraceFragment;

public class EndTransaction extends TraceSection {
  public EndTransaction(Hub hub, TraceFragment... fragments) {
    this.addChunks(hub, fragments);
  }

  @Override
  public void seal(Hub hub) {}
}
