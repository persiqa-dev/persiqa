package com.persiqa.core;

import java.util.List;

/** One stable page of a persistence-independent collection query. */
public record PageResult<T>(
    List<T> content, int page, int size, long totalElements, int totalPages) {
  public PageResult {
    content = List.copyOf(content);
    if (page < 0 || size < 1 || totalElements < 0 || totalPages < 0) {
      throw new IllegalArgumentException("page result values must not be negative");
    }
  }
}
