package net.consensys.linea.zktracer.module.gas;

import java.nio.MappedByteBuffer;
import java.util.List;

import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.stacked.list.StackedList;
import net.consensys.linea.zktracer.module.Module;

public class Gas implements Module {
  /** A list of the operations to trace */
  private final StackedList<GasChunk> chunks = new StackedList<>();

  @Override
  public String moduleKey() {
    return "GAS";
  }

  @Override
  public void enterTransaction() {
    this.chunks.enter();
  }

  @Override
  public void popTransaction() {
    this.chunks.pop();
  }

  @Override
  public int lineCount() {
    return this.chunks.lineCount();
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return null;
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    final Trace trace = new Trace(buffers);
    for (int i = 0; i < this.chunks.size(); i++) {
      GasChunk gasChunk = this.chunks.get(i);
      gasChunk.trace(i + 1, trace);
    }
  }
}
