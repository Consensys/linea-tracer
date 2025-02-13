package net.consensys.linea.generics;

import net.consensys.linea.generics.hierarchy.Animal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

// This class defines generic tests for any animal, using Java Generics
public abstract class AnimalTest<T extends Animal> {

  @ParameterizedTest
  @MethodSource("source")
  public void genericsTest(T animal) {
    System.out.println(animal.getName() + " says: " + animal.talk());
  }
}
