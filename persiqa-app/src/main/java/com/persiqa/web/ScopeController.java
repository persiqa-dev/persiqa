package com.persiqa.web;

import com.persiqa.application.KnowledgeApplicationService;
import com.persiqa.application.ScopeAccessService;
import com.persiqa.core.PageQuery;
import com.persiqa.core.PageResult;
import com.persiqa.web.dto.KnowledgeDtos.PageResponse;
import com.persiqa.web.dto.KnowledgeDtos.ScopeResponse;
import com.persiqa.web.dto.KnowledgeMapper;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** HTTP access to ModelScope metadata owned by the authenticated subject. */
@RestController
@RequestMapping("/api/scopes")
public class ScopeController {
  private final KnowledgeApplicationService knowledge;
  private final CurrentSubject currentSubject;
  private final KnowledgeMapper mapper;
  private final ScopeAccessService scopeAccess;

  public ScopeController(
      KnowledgeApplicationService knowledge,
      CurrentSubject currentSubject,
      KnowledgeMapper mapper,
      ScopeAccessService scopeAccess) {
    this.knowledge = knowledge;
    this.currentSubject = currentSubject;
    this.mapper = mapper;
    this.scopeAccess = scopeAccess;
  }

  /** Lists only scopes owned by the authenticated subject. */
  @GetMapping
  public PageResponse<ScopeResponse> findOwnedScopes(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "50") int size,
      @RequestParam(value = "q", required = false) String query) {
    var result =
        knowledge.findScopesOwnedBy(currentSubject.require(), new PageQuery(page, size, query));
    return pageResponse(result);
  }

  /** Returns one scope only when it is owned by the authenticated subject. */
  @GetMapping("/{scopeId}")
  public ResponseEntity<ScopeResponse> findScope(@PathVariable("scopeId") UUID scopeId) {
    var scope = scopeAccess.requireOwned(scopeId, currentSubject.require());
    return ResponseEntity.ok(mapper.toScope(scope));
  }

  private PageResponse<ScopeResponse> pageResponse(PageResult<com.persiqa.core.ModelScope> page) {
    return new PageResponse<>(
        page.content().stream().map(mapper::toScope).toList(),
        page.page(),
        page.size(),
        page.totalElements(),
        page.totalPages());
  }
}
