package net.consensys.linea.zktracer.module.hub.section;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.chunks.CommonFragment;
import net.consensys.linea.zktracer.module.hub.chunks.TraceFragment;

/** A TraceSection gather the trace lines linked to a single operation */
public abstract class TraceSection {
  public record TraceChunk(CommonFragment common, TraceFragment specific) {
    public Trace.TraceBuilder trace(Trace.TraceBuilder trace) {
      if (common != null) {
        common.trace(trace);
      }
      if (specific != null) {
        specific.trace(trace);
      }

      return trace.fillAndValidateRow();
    }
  }

  @Getter List<TraceChunk> chunks = new ArrayList<>();

  public TraceSection(TraceFragment... chunks) {
    for (TraceFragment chunk : chunks) {
      this.chunks.add(new TraceChunk(null, chunk));
    }
  }

  public TraceSection(Hub hub, TraceFragment... chunks) {
    this.chunks = new ArrayList<>();
    for (var stackChunk : hub.makeStackChunks()) {
      this.addChunk(hub, stackChunk);
    }
    this.addChunks(hub, chunks);
  }

  public final void addChunk(Hub hub, TraceFragment chunk) {
    assert !(chunk instanceof CommonFragment);

    this.chunks.add(new TraceChunk(hub.traceCommon(), chunk));
  }

  public final void addChunks(Hub hub, TraceFragment... chunks) {
    for (TraceFragment chunk : chunks) {
      this.addChunk(hub, chunk);
    }
  }

  /**
   * Returns the context number associated with the operation encoded by this TraceChunk.
   *
   * @return the CN
   */
  public final int contextNumber() {
    return this.chunks.get(0).common.contextNumber();
  }

  /**
   * Set the new context number associated with the operation encoded by this TraceChunk.
   *
   * @param contextNumber the new CN
   */
  public final void setContextNumber(int contextNumber) {
    for (TraceChunk chunk : this.chunks) {
      chunk.common.newContextNumber(contextNumber);
    }
  }

  /**
   * Returns the program counter associated with the operation encoded by this TraceSection.
   *
   * @return the PC
   */
  public final int pc() {
    return this.chunks.get(0).common.pc();
  }

  /**
   * Set the newPC associated with the operation encoded by this TraceSection.
   *
   * @param contextNumber the new PC
   */
  public final void setNewPc(int contextNumber) {
    for (TraceChunk chunk : this.chunks) {
      chunk.common.newPc(contextNumber);
    }
  }

  /**
   * This method is called when the TraceChunk is finished to build required information post-hoc
   *
   * @param hub the linked {@link Hub} context
   */
  public void seal(Hub hub) {
    for (TraceChunk chunk : this.chunks) {
      chunk.common.txEndStamp(hub.getStamp());
    }
  }

  /**
   * This method is called when the conflation is finished to build required information post-hoc
   *
   * @param hub the linked {@link Hub} context
   *     <p>W A R N I N G - - - If overriden, super *MUST* be called
   */
  public void retcon(Hub hub) {
    for (TraceSection.TraceChunk chunk : chunks) {
      chunk.common().retcon(hub);
      chunk.specific().retcon(hub);
    }
  }
}
