package net.consensys.linea.generics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.consensys.linea.generics.hierarchy.Cat;
import org.junit.jupiter.params.provider.Arguments;

// This class defines tests for a cat, using the generic tests defined in AnimalTest
public class CatTest extends AnimalTest<Cat> {

  private static Stream<Arguments> source() {
    List<Arguments> arguments = new ArrayList<>();
    arguments.add(Arguments.of(new Cat("Alice")));
    arguments.add(Arguments.of(new Cat("Bob")));
    return arguments.stream();
  }
}
