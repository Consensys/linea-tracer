package net.consensys.linea.zktracer.module.hub.stack;

/**
 * An operation within a {@link StackLine}. This structure is useful because stack lines may be
 * sparse. TODO: replace with a map[int->StackOperation] within StackLine?
 *
 * @param i the index of the stack item within a stack line -- within [[1, 4]
 * @param it the details of the {@link StackOperation} to apply to a column of a stack line
 */
record IndexedStackOperation(int i, StackOperation it) {
  /**
   * For the sake of homogeneity with the zkEVM spec, {@param i} is set with 1-based indices.
   * However, these indices are used to index 0-based array; hence this sneaky conversion.
   *
   * @return the actual stack operation index
   */
  public int i() {
    return this.i - 1;
  }
}
