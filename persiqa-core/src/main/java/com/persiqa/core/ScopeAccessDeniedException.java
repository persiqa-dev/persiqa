package com.persiqa.core;

/** Thrown when an authenticated subject is not the owner of a ModelScope. */
public final class ScopeAccessDeniedException extends RuntimeException {
  public ScopeAccessDeniedException(String message) {
    super(message);
  }
}
