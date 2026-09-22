package com.persiqa.application;

import com.persiqa.core.ModelScope;
import com.persiqa.core.ScopeAccessDeniedException;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Centralizes scope existence and ownership checks for application entry points. */
@Service
public class ScopeAccessService {
  private final KnowledgeApplicationService knowledge;

  public ScopeAccessService(KnowledgeApplicationService knowledge) {
    this.knowledge = knowledge;
  }

  /** Returns a scope only when it exists and belongs to the supplied subject. */
  @Transactional(readOnly = true)
  public ModelScope requireOwned(UUID scopeId, String subject) {
    var scope = knowledge.findScope(scopeId);
    if (scope == null) {
      throw new UnknownScopeException(scopeId);
    }
    if (!scope.ownerSubject().equals(subject)) {
      throw new ScopeAccessDeniedException("subject is not the owner of scope " + scopeId);
    }
    return scope;
  }

  /** Signals a missing scope without conflating it with an authorization failure. */
  public static final class UnknownScopeException extends RuntimeException {
    public UnknownScopeException(UUID scopeId) {
      super("unknown scope: " + scopeId);
    }
  }
}
