package net.consensys.linea.zktracer.instructionprocessing.createTests.advanced;

public enum AdvancedCreate2ScenarioValue {
  NONE(0L),
  MODIFY_STORAGE(1L),
  CREATE2_WITH_IMMEDIATE_REDEPLOYMENT(2L),
  SELFDESTRUCT(3L),
  REVERT(4L);

  private final long value;

  AdvancedCreate2ScenarioValue(long value) {
    this.value = value;
  }

  public long getAdvancedCreate2ScenarioValue() {
    return value;
  }
}
