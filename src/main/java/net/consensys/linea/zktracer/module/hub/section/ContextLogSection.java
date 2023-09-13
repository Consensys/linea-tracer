package net.consensys.linea.zktracer.module.hub.section;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.chunks.TraceFragment;

public class ContextLogSection extends TraceSection {
  public ContextLogSection(Hub hub, TraceFragment... chunks) {
    this.addChunksAndStack(hub, chunks);
  }

  @Override
  public void seal(Hub hub) {}
}
