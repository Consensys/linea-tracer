/*
 * Copyright ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package net.consensys.linea.zktracer.module;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.reflect.FieldUtils;

/** Validates if a row is missing values corresponding to column types. */
public class RowValidator {
  /**
   * Executes validation logic to check for missing column values before committing the current row.
   *
   * @param clazz class object of the current trace
   * @param obj instance of the current trace
   * @param rowIndex index of the current row
   */
  public static void validate(Class<?> clazz, Object obj, int rowIndex) {
    Supplier<Stream<AbstractMap.SimpleEntry<String, Integer>>> columnEntries =
        () ->
            FieldUtils.getFieldsListWithAnnotation(clazz, JsonProperty.class).stream()
                .filter(f -> List.class.isAssignableFrom(f.getType()))
                .map(
                    f -> {
                      try {
                        f.setAccessible(true);
                        List<?> l = (List<?>) f.get(obj);

                        return new AbstractMap.SimpleEntry<>(f.getName(), l.size());
                      } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                      }
                    });

    List<String> columnsWithMissingValues =
        filterWronglyEnteredColumnValues(columnEntries.get(), columnSize -> columnSize < rowIndex);

    List<String> columnsWithDuplicatedValues =
        filterWronglyEnteredColumnValues(columnEntries.get(), columnSize -> columnSize > rowIndex);

    if (!columnsWithMissingValues.isEmpty()) {
      throw new IllegalArgumentException(
          "Cannot commit row at index %d due to missing column values for: %s"
              .formatted(rowIndex, columnsWithMissingValues));
    }

    if (!columnsWithDuplicatedValues.isEmpty()) {
      throw new IllegalArgumentException(
          "Cannot commit row at index %d due to duplicated column values for: %s"
              .formatted(rowIndex, columnsWithDuplicatedValues));
    }
  }

  private static List<String> filterWronglyEnteredColumnValues(
      Stream<AbstractMap.SimpleEntry<String, Integer>> columnEntries,
      Predicate<Integer> filterPredicate) {
    var columnMap =
        columnEntries
            .filter(e -> filterPredicate.test(e.getValue()))
            .collect(
                Collectors.toMap(
                    AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

    if (!columnMap.isEmpty()) {
      return columnMap.keySet().stream()
          .map(k -> CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, k))
          .toList();
    }

    return new ArrayList<>();
  }
}
