package org.jane.gtelinternship.common.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {
  public static <T> List<List<T>> batchList(List<T> list, int batchSize) {
    if (list == null || list.isEmpty() || batchSize <= 0) {
      return List.of();
    }

    List<List<T>> batches = new ArrayList<>();
    for (int i = 0; i < list.size(); i += batchSize) {
      int end = Math.min(i + batchSize, list.size());
      batches.add(list.subList(i, end));
    }
    return batches;
  }
}
