package com.persiqa.web;

import com.persiqa.application.KnowledgeApplicationService;
import com.persiqa.core.RelationRegistry;
import com.persiqa.model.Ckm.Capability;
import com.persiqa.model.Ckm.Concept;
import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.Entity;
import com.persiqa.model.Ckm.Kind;
import com.persiqa.model.Ckm.KnowledgeKind;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.RelationType;
import com.persiqa.model.Ckm.State;
import java.net.URI;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** HTTP commands for recording statement-first CKM knowledge. */
@RestController
@RequestMapping("/api")
public class KnowledgeWriteController {
  private final KnowledgeApplicationService knowledge;
  private final RelationRegistry relationTypes = new RelationRegistry();

  public KnowledgeWriteController(KnowledgeApplicationService knowledge) {
    this.knowledge = knowledge;
  }

  /** Creates a scope with a server-assigned persistence identity. */
  @PostMapping("/scopes")
  public ResponseEntity<ScopeResponse> createScope(@RequestBody CreateScopeRequest request) {
    var scopeId = UUID.randomUUID();
    knowledge.createScope(scopeId, request.name());
    return ResponseEntity.created(URI.create("/api/scopes/" + scopeId))
        .body(new ScopeResponse(scopeId, request.name()));
  }

  /** Records one explicit or derived Relation assertion and its canonical Relation. */
  @PostMapping("/scopes/{scopeId}/statements")
  public ResponseEntity<KnowledgeApplicationService.RelationRecord> recordRelation(
      @PathVariable("scopeId") UUID scopeId, @RequestBody RecordRelationRequest request) {
    var source = request.source().resolve(scopeId, knowledge);
    var target = request.target().resolve(scopeId, knowledge);
    var type =
        relationTypes
            .create(request.relationId(), request.relationType(), source, target)
            .type();
    var context = Objects.requireNonNullElseGet(request.context(), Context::unspecified);
    var record = recordRelation(scopeId, request, type, source, target, context);
    return ResponseEntity.created(
            URI.create("/api/scopes/" + scopeId + "/statements/" + request.statementId()))
        .body(record);
  }

  /** Appends an independent context record without changing the original assertion. */
  @PostMapping("/scopes/{scopeId}/statements/{statementId}/observations")
  public ResponseEntity<Void> appendObservation(
      @PathVariable("scopeId") UUID scopeId,
      @PathVariable("statementId") String statementId,
      @RequestBody Context context) {
    knowledge.appendObservation(scopeId, statementId, context);
    return ResponseEntity.noContent().build();
  }

  /** Translates client input and CKM validation errors into a stable HTTP response. */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ProblemDetail> invalidRequest(IllegalArgumentException error) {
    return ResponseEntity.badRequest()
        .body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, error.getMessage()));
  }

  private KnowledgeApplicationService.RelationRecord recordRelation(
      UUID scopeId,
      RecordRelationRequest request,
      RelationType type,
      Node source,
      Node target,
      Context context) {
    if (request.knowledgeKind() == KnowledgeKind.EXPLICIT) {
      if (!request.derivedFrom().isEmpty()) {
        throw new IllegalArgumentException(
            "explicit statement cannot declare derivedFrom evidence");
      }
      return knowledge.assertRelation(
          scopeId, request.relationId(), request.statementId(), type, source, target, context);
    }
    return knowledge.recordDerivedRelation(
        scopeId,
        request.relationId(),
        request.statementId(),
        type,
        source,
        target,
        request.derivedFrom(),
        context);
  }

  /** Request body for server-assigned CKM scope creation. */
  public record CreateScopeRequest(String name) {
    public CreateScopeRequest {
      if (name == null || name.isBlank()) {
        throw new IllegalArgumentException("scope name must not be blank");
      }
    }
  }

  /** Scope identity returned after creation. */
  public record ScopeResponse(UUID id, String name) {}

  /** Request body for one explicit or derived relation assertion. */
  public record RecordRelationRequest(
      String relationId,
      String statementId,
      KnowledgeKind knowledgeKind,
      String relationType,
      NodeReference source,
      NodeReference target,
      Set<String> derivedFrom,
      Context context) {
    public RecordRelationRequest {
      if (relationId == null || relationId.isBlank()) {
        throw new IllegalArgumentException("relationId must not be blank");
      }
      if (statementId == null || statementId.isBlank()) {
        throw new IllegalArgumentException("statementId must not be blank");
      }
      if (knowledgeKind == null) {
        throw new IllegalArgumentException("knowledgeKind is required");
      }
      if (relationType == null || relationType.isBlank()) {
        throw new IllegalArgumentException("relationType must not be blank");
      }
      source = Objects.requireNonNull(source, "source is required");
      target = Objects.requireNonNull(target, "target is required");
      derivedFrom = Set.copyOf(Objects.requireNonNullElse(derivedFrom, Set.of()));
    }
  }

  /** Typed endpoint reference used by a relation-recording command. */
  public record NodeReference(String id, Kind kind) {
    public NodeReference {
      if (id == null || id.isBlank()) {
        throw new IllegalArgumentException("node id must not be blank");
      }
      if (kind == null) {
        throw new IllegalArgumentException("node kind is required");
      }
    }

    private Node resolve(UUID scopeId, KnowledgeApplicationService knowledge) {
      return switch (kind) {
        case ENTITY -> new Entity(id);
        case CAPABILITY -> new Capability(id);
        case CONCEPT -> new Concept(id);
        case STATE -> new State(id);
        case RELATION -> relation(scopeId, knowledge);
        case STATEMENT, TYPED_VALUE ->
            throw new IllegalArgumentException("unsupported endpoint kind: " + kind);
      };
    }

    private Relation relation(UUID scopeId, KnowledgeApplicationService knowledge) {
      var relation = knowledge.findRelation(scopeId, id);
      if (relation == null) {
        throw new IllegalArgumentException("unknown Relation endpoint: " + id);
      }
      return relation;
    }
  }
}
