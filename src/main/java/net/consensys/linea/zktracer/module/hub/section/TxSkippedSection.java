package net.consensys.linea.zktracer.module.hub.section;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.chunks.TraceFragment;

public class TxSkippedSection extends TraceSection {
  public TxSkippedSection(Hub hub, TraceFragment... chunks) {
    this.addChunks(hub, chunks);
  }

  @Override
  public void seal(Hub hub) {}
}
