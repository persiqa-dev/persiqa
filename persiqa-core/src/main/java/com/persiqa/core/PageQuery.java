package com.persiqa.core;

/** Validated pagination and optional text-search input for one collection query. */
public record PageQuery(int page, int size, String query) {
  public static final int DEFAULT_SIZE = 50;
  public static final int MAX_SIZE = 100;

  public PageQuery {
    if (page < 0) {
      throw new IllegalArgumentException("page must not be negative");
    }
    if (size < 1 || size > MAX_SIZE) {
      throw new IllegalArgumentException("size must be between one and " + MAX_SIZE);
    }
    query = query == null || query.isBlank() ? null : query.trim();
  }
}
