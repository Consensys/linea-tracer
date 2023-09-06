package net.consensys.linea.zktracer.module.hub.section;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.chunks.TraceFragment;

public class StackOnlySection extends TraceSection {
  public StackOnlySection(Hub hub, TraceFragment... chunks) {
    super(hub, chunks);
  }

  @Override
  void seal(Hub hub) {}
}
