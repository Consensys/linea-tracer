package net.consensys.linea.zktracer.module.hub.section;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.chunks.TraceChunk;

public class StorageSection extends TraceSection {
  public StorageSection(Hub hub, TraceChunk... chunks) {
    super(hub, chunks);
  }

  @Override
  void retcon(Hub hub) {}
}
