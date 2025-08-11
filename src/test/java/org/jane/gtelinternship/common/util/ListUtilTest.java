package org.jane.gtelinternship.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListUtilTest {
  @Nested
  @DisplayName("When batching a list")
  class BatchListTests {
    @Test
    void shouldReturnEmptyBatchesForEmptyList() {
      List<List<Integer>> batches = ListUtil.batchList(List.of(), 2);
      assertTrue(batches.isEmpty());
    }

    @Test
    void shouldReturnEmptyBatchesForNullList() {
      List<List<Integer>> batches = ListUtil.batchList(null, 2);
      assertTrue(batches.isEmpty());
    }

    @Test
    void shouldReturnEmptyBatchesForZeroBatchSize() {
      List<Integer> input = Arrays.asList(1, 2, 3);
      List<List<Integer>> batches = ListUtil.batchList(input, 0);
      assertTrue(batches.isEmpty());
    }

    @Test
    void shouldReturnSingleElementIfBatchSizeBiggerThanList() {
      List<Integer> input = Arrays.asList(1, 2, 3);
      List<List<Integer>> batches = ListUtil.batchList(input, 10);

      assertEquals(1, batches.size());
      assertEquals(List.of(1, 2, 3), batches.getFirst());
    }

    @Test
    void shouldCutListIntoBatches() {
      List<Integer> input = Arrays.asList(1, 2, 3, 4, 5);
      List<List<Integer>> batches = ListUtil.batchList(input, 2);
      assertEquals(3, batches.size());
      assertEquals(List.of(1, 2), batches.get(0));
      assertEquals(List.of(3, 4), batches.get(1));
      assertEquals(List.of(5), batches.get(2));
    }
  }
}