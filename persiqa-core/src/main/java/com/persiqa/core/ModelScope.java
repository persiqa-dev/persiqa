package com.persiqa.core;

import java.util.UUID;

/** One CKM model boundary with a single owning subject. */
public record ModelScope(UUID id, String name, String ownerSubject) {
  public ModelScope {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("scope name must not be blank");
    }
    if (ownerSubject == null || ownerSubject.isBlank()) {
      throw new IllegalArgumentException("scope owner must not be blank");
    }
  }
}
