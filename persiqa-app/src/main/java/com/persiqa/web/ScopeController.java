package com.persiqa.web;

import com.persiqa.application.KnowledgeApplicationService;
import com.persiqa.core.ScopeAccessDeniedException;
import com.persiqa.web.dto.KnowledgeDtos.ScopeResponse;
import com.persiqa.web.dto.KnowledgeMapper;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** HTTP access to ModelScope metadata owned by the authenticated subject. */
@RestController
@RequestMapping("/api/scopes")
public class ScopeController {
  private final KnowledgeApplicationService knowledge;
  private final CurrentSubject currentSubject;
  private final KnowledgeMapper mapper;

  public ScopeController(
      KnowledgeApplicationService knowledge,
      CurrentSubject currentSubject,
      KnowledgeMapper mapper) {
    this.knowledge = knowledge;
    this.currentSubject = currentSubject;
    this.mapper = mapper;
  }

  /** Lists only scopes owned by the authenticated subject. */
  @GetMapping
  public List<ScopeResponse> findOwnedScopes() {
    return knowledge.findScopesOwnedBy(currentSubject.require()).stream()
        .map(mapper::toScope)
        .toList();
  }

  /** Returns one scope only when it is owned by the authenticated subject. */
  @GetMapping("/{scopeId}")
  public ResponseEntity<ScopeResponse> findScope(@PathVariable("scopeId") UUID scopeId) {
    var scope = knowledge.findScope(scopeId);
    if (scope == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "unknown scope: " + scopeId);
    }
    if (!scope.ownerSubject().equals(currentSubject.require())) {
      throw new ScopeAccessDeniedException("subject is not the owner of scope " + scopeId);
    }
    return ResponseEntity.ok(mapper.toScope(scope));
  }

  /** Translates scope ownership failures into HTTP 403. */
  @ExceptionHandler(ScopeAccessDeniedException.class)
  public ResponseEntity<ProblemDetail> forbidden(ScopeAccessDeniedException error) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, error.getMessage()));
  }
}
