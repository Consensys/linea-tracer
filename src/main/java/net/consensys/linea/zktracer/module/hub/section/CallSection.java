package net.consensys.linea.zktracer.module.hub.section;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.chunks.TraceFragment;

public class CallSection extends TraceSection {
  public CallSection(Hub hub, TraceFragment... chunks) {
    this.addChunksAndStack(hub, chunks);
  }

  @Override
  public void seal(Hub hub) {}
}
