package net.consensys.linea.zktracer.module.hub.section;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.chunks.TraceChunk;

public class TxSkippedSection extends TraceSection {
  public TxSkippedSection(TraceChunk... chunks) {
    super(chunks);
  }

  @Override
  void retcon(Hub hub) {}
}
