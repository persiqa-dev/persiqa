package com.persiqa.web;

import com.persiqa.application.KnowledgeApplicationService;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.Statement;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Read-only HTTP access to the knowledge held by one CKM scope. */
@RestController
@RequestMapping("/api/scopes/{scopeId}")
public class ScopeKnowledgeController {
  private final KnowledgeApplicationService knowledge;

  public ScopeKnowledgeController(KnowledgeApplicationService knowledge) {
    this.knowledge = knowledge;
  }

  /** Lists the canonical Relations in one scope. */
  @GetMapping("/relations")
  public List<Relation> findRelations(@PathVariable("scopeId") UUID scopeId) {
    return knowledge.findRelations(scopeId);
  }

  /** Lists the Statements in one scope. */
  @GetMapping("/statements")
  public List<Statement> findStatements(@PathVariable("scopeId") UUID scopeId) {
    return knowledge.findStatements(scopeId);
  }

  /** Lists every explicit or derived Statement canonically associated with one Relation. */
  @GetMapping("/relations/{relationId}/statements")
  public ResponseEntity<List<Statement>> findRelationStatements(
      @PathVariable("scopeId") UUID scopeId, @PathVariable("relationId") String relationId) {
    if (knowledge.findRelation(scopeId, relationId) == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(knowledge.findStatementsForRelation(scopeId, relationId));
  }
}
