package net.consensys.linea;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ModuleToConstraints(String moduleName, Map<String, List<String>> constraints) {

  public ModuleToConstraints(
      @JsonProperty("moduleName") String moduleName,
      @JsonProperty("constraints") Map<String, List<String>> constraints) {
    this.moduleName = moduleName;
    this.constraints = constraints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || moduleName.getClass() != o.getClass()) {
      return false;
    }
    return moduleName.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(moduleName);
  }
}
