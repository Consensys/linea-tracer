package net.consensys.linea.zktracer.module.hub.section;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.chunks.TraceChunk;

public abstract class TraceSection {
  @Getter List<TraceChunk> chunks;

  public TraceSection(TraceChunk... chunks) {
    this.chunks = List.of(chunks);
  }

  public TraceSection(Hub hub, TraceChunk... chunks) {
    this.chunks = new ArrayList<>();
    for (var stackChunk : hub.makeStackChunks()) {
      this.addChunk(hub, stackChunk);
    }
    this.addChunks(hub, chunks);
  }

  public void addChunk(Hub hub, TraceChunk chunk) {
    this.chunks.add(hub.traceCommon());
    this.chunks.add(chunk);
  }

  public void addChunks(Hub hub, TraceChunk... chunks) {
    for (TraceChunk chunk : chunks) {
      this.chunks.add(hub.traceCommon());
      this.chunks.add(chunk);
    }
  }

  abstract void retcon(Hub hub);
}
