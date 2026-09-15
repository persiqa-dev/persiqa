package com.persiqa.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/** Resolves the authenticated subject used as ModelScope owner. */
@Component
public class CurrentSubject {
  /** Returns the authenticated principal name. */
  public String require() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null
        || !authentication.isAuthenticated()
        || authentication.getName() == null
        || authentication.getName().isBlank()
        || "anonymousUser".equals(authentication.getName())) {
      throw new IllegalStateException("authenticated subject is required");
    }
    return authentication.getName();
  }
}
