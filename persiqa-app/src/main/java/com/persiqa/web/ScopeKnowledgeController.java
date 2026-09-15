package com.persiqa.web;

import com.persiqa.application.KnowledgeApplicationService;
import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.Kind;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.Statement;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    requireScope(scopeId);
    return knowledge.findRelations(scopeId);
  }

  /** Lists the Statements in one scope. */
  @GetMapping("/statements")
  public List<Statement> findStatements(@PathVariable("scopeId") UUID scopeId) {
    requireScope(scopeId);
    return knowledge.findStatements(scopeId);
  }

  /** Returns a standalone canonical Node by its stable identity and kind. */
  @GetMapping("/nodes/{kind}/{nodeId}")
  public ResponseEntity<Node> findNode(
      @PathVariable("scopeId") UUID scopeId,
      @PathVariable("kind") Kind kind,
      @PathVariable("nodeId") String nodeId) {
    requireScope(scopeId);
    var node = knowledge.findNode(scopeId, nodeId);
    if (node == null || node.kind() != kind) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(node);
  }

  /** Returns one Statement by its stable identity. */
  @GetMapping("/statements/{statementId}")
  public ResponseEntity<Statement> findStatement(
      @PathVariable("scopeId") UUID scopeId, @PathVariable("statementId") String statementId) {
    requireScope(scopeId);
    var statement = knowledge.findStatement(scopeId, statementId);
    return statement == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(statement);
  }

  /** Lists the append-preserved observation contexts for one Statement. */
  @GetMapping("/statements/{statementId}/observations")
  public ResponseEntity<List<Context>> findObservations(
      @PathVariable("scopeId") UUID scopeId, @PathVariable("statementId") String statementId) {
    requireScope(scopeId);
    if (knowledge.findStatement(scopeId, statementId) == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(knowledge.findObservations(scopeId, statementId));
  }

  /** Lists every explicit or derived Statement canonically associated with one Relation. */
  @GetMapping("/relations/{relationId}/statements")
  public ResponseEntity<List<Statement>> findRelationStatements(
      @PathVariable("scopeId") UUID scopeId, @PathVariable("relationId") String relationId) {
    requireScope(scopeId);
    if (knowledge.findRelation(scopeId, relationId) == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(knowledge.findStatementsForRelation(scopeId, relationId));
  }

  private void requireScope(UUID scopeId) {
    if (!knowledge.scopeExists(scopeId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "unknown scope: " + scopeId);
    }
  }
}
