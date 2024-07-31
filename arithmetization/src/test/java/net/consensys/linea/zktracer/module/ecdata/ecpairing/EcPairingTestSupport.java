package net.consensys.linea.zktracer.module.ecdata.ecpairing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.params.provider.Arguments;

public class EcPairingTestSupport {
  private static final String DELIMITER_CSV = ",";
  static final String DELIMITER_PAIRINGS = "_";
  private static final Random random = new Random(1);

  static String info(int totalPairings, int r) {
    // r represents the number of the repetition of a scenario
    return "TotalPairings" + totalPairings + "(" + r + ")";
  }

  static Arguments generatePairings(
      final String description,
      final int totalPairings,
      List<Arguments> smallPointsType,
      List<Arguments> largePointsType) {
    List<Arguments> pairings = new ArrayList<>();
    for (int i = 0; i < totalPairings; i++) {
      pairings.add(pair(rnd(smallPointsType), rnd(largePointsType)));
    }
    return Arguments.of(description, argumentsListToPairingsAsString(pairings));
  }

  static Arguments generatePairingsSmallPointsMixed(
      final String description,
      final int totalPairings,
      List<Arguments> smallPointsType1,
      List<Arguments> smallPointsType2,
      List<Arguments> largePointsType) {
    List<Arguments> pairings = new ArrayList<>();
    for (int i = 0; i < totalPairings; i++) {
      if (i % 2 == 0) {
        pairings.add(pair(rnd(smallPointsType1), rnd(largePointsType)));
      } else {
        pairings.add(pair(rnd(smallPointsType2), rnd(largePointsType)));
      }
    }
    return Arguments.of(description, argumentsListToPairingsAsString(pairings));
  }

  static Arguments generatePairingsLargePointsMixed(
      final String description,
      final int totalPairings,
      List<Arguments> smallPointsType,
      List<Arguments> largePointsType1,
      List<Arguments> largePointsType2) {
    List<Arguments> pairings = new ArrayList<>();
    for (int i = 0; i < totalPairings; i++) {
      if (i % 2 == 0) {
        pairings.add(pair(rnd(smallPointsType), rnd(largePointsType1)));
      } else {
        pairings.add(pair(rnd(smallPointsType), rnd(largePointsType2)));
      }
    }
    return Arguments.of(description, argumentsListToPairingsAsString(pairings));
  }

  static Arguments generatePairingsMixed(
      final String description,
      final int totalPairings,
      List<Arguments> smallPointsType1,
      List<Arguments> smallPointsType2,
      List<Arguments> largePointsType1,
      List<Arguments> largePointsType2) {
    List<Arguments> pairings = new ArrayList<>();
    for (int i = 0; i < totalPairings; i++) {
      switch (i % 4) {
        case 0 -> {
          pairings.add(pair(rnd(smallPointsType1), rnd(largePointsType1)));
        }
        case 1 -> {
          pairings.add(pair(rnd(smallPointsType1), rnd(largePointsType2)));
        }
        case 2 -> {
          pairings.add(pair(rnd(smallPointsType2), rnd(largePointsType1)));
        }
        case 3 -> {
          pairings.add(pair(rnd(smallPointsType2), rnd(largePointsType2)));
        }
      }
    }
    return Arguments.of(description, argumentsListToPairingsAsString(pairings));
  }

  public static Arguments pair(Arguments smallPointArgs, Arguments largePointArgs) {
    assertEquals(2, smallPointArgs.get().length);
    assertEquals(4, largePointArgs.get().length);
    return Arguments.of(
        smallPointArgs.get()[0],
        smallPointArgs.get()[1],
        largePointArgs.get()[0],
        largePointArgs.get()[1],
        largePointArgs.get()[2],
        largePointArgs.get()[3]);
  }

  static String argumentsListToPairingsAsString(List<Arguments> pairings) {
    StringBuilder sb = new StringBuilder();
    for (Arguments pair : pairings) {
      assertEquals(6, pair.get().length);
      for (Object coordinate : pair.get()) {
        sb.append(coordinate.toString()).append(DELIMITER_PAIRINGS);
      }
    }
    return !sb.isEmpty() ? sb.substring(0, sb.length() - 1) : "";
  }

  static List<Arguments> pairingsAsStringToArgumentsList(String pairingsAsString) {
    final String[] pairingsAsArray = pairingsAsString.split(DELIMITER_PAIRINGS);
    // Each pair is composed by 6 coordinates
    final int totalPairings = pairingsAsArray.length / 6;
    List<Arguments> pairings = new ArrayList<>();
    for (int i = 0; i < totalPairings; i++) {
      pairings.add(
          Arguments.of(
              pairingsAsArray[6 * i],
              pairingsAsArray[6 * i + 1],
              pairingsAsArray[6 * i + 2],
              pairingsAsArray[6 * i + 3],
              pairingsAsArray[6 * i + 4],
              pairingsAsArray[6 * i + 5]));
    }
    return pairings;
  }

  public static List<Arguments> csvToArgumentsList(String filePath) throws IOException {
    List<Arguments> argumentsList = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      boolean isFirstLine = true;
      while ((line = br.readLine()) != null) {
        if (isFirstLine) {
          isFirstLine = false;
          continue; // Skip the first line (column names)
        }
        String[] values = line.split(DELIMITER_CSV);
        argumentsList.add(Arguments.of((Object[]) values));
      }
    }
    return argumentsList;
  }

  private static Arguments rnd(List<Arguments> points) {
    // If there is only one point, return it
    if (points.size() == 1) {
      return points.getFirst();
    }
    int index = random.nextInt(points.size());
    return points.get(index);
  }
}
