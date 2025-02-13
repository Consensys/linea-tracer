package net.consensys.linea.generics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.consensys.linea.generics.hierarchy.Dog;
import org.junit.jupiter.params.provider.Arguments;

// This class defines tests for a dog, using the generic tests defined in AnimalTest
public class DogTest extends AnimalTest<Dog> {

  private static Stream<Arguments> source() {
    List<Arguments> arguments = new ArrayList<>();
    arguments.add(Arguments.of(new Dog("Charlie")));
    arguments.add(Arguments.of(new Dog("David")));
    return arguments.stream();
  }
}
