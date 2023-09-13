package net.consensys.linea.zktracer.module.hub.section;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.chunks.TraceFragment;

public class StackOnlySection extends TraceSection {
  public StackOnlySection(Hub hub, TraceFragment... chunks) {
    this.addChunksAndStack(hub, chunks);
  }

  @Override
  public void seal(Hub hub) {}
}
