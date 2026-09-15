package com.persiqa.web;

import com.persiqa.application.KnowledgeApplicationService;
import com.persiqa.core.ScopeAccessDeniedException;
import com.persiqa.model.Ckm.Capability;
import com.persiqa.model.Ckm.Concept;
import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.Entity;
import com.persiqa.model.Ckm.Kind;
import com.persiqa.model.Ckm.KnowledgeKind;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.State;
import com.persiqa.web.dto.KnowledgeDtos.NodeResponse;
import com.persiqa.web.dto.KnowledgeDtos.RelationRecordResponse;
import com.persiqa.web.dto.KnowledgeDtos.ScopeResponse;
import com.persiqa.web.dto.KnowledgeMapper;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

/** HTTP commands for recording statement-first CKM knowledge. */
@RestController
@RequestMapping("/api")
public class KnowledgeWriteController {
  private final KnowledgeApplicationService knowledge;
  private final CurrentSubject currentSubject;
  private final KnowledgeMapper mapper;

  public KnowledgeWriteController(
      KnowledgeApplicationService knowledge,
      CurrentSubject currentSubject,
      KnowledgeMapper mapper) {
    this.knowledge = knowledge;
    this.currentSubject = currentSubject;
    this.mapper = mapper;
  }

  /** Creates a scope with a server-assigned persistence identity owned by the caller. */
  @PostMapping("/scopes")
  public ResponseEntity<ScopeResponse> createScope(@RequestBody CreateScopeRequest request) {
    var scopeId = UUID.randomUUID();
    var owner = currentSubject.require();
    var scope = knowledge.createScope(scopeId, request.name(), owner);
    return ResponseEntity.created(URI.create("/api/scopes/" + scopeId)).body(mapper.toScope(scope));
  }

  /** Creates a standalone Entity, Capability, or Concept with no invented surrounding knowledge. */
  @PostMapping("/scopes/{scopeId}/nodes")
  public ResponseEntity<NodeResponse> createNode(
      @PathVariable("scopeId") UUID scopeId, @RequestBody CreateNodeRequest request) {
    var subject = currentSubject.require();
    requireScopeAccess(scopeId, subject);
    var node = request.toNode();
    knowledge.saveNode(scopeId, subject, node);
    var location =
        UriComponentsBuilder.fromPath("/api/scopes/{scopeId}/nodes/{kind}/{nodeId}")
            .buildAndExpand(scopeId, node.kind(), node.id())
            .toUri();
    return ResponseEntity.created(location).body(mapper.toNode(node));
  }

  /** Records one explicit or derived Relation assertion and its canonical Relation. */
  @PostMapping("/scopes/{scopeId}/statements")
  public ResponseEntity<RelationRecordResponse> recordRelation(
      @PathVariable("scopeId") UUID scopeId, @RequestBody RecordRelationRequest request) {
    var subject = currentSubject.require();
    requireScopeAccess(scopeId, subject);
    var source = request.source().resolve(scopeId, subject, knowledge);
    var target = request.target().resolve(scopeId, subject, knowledge);
    var context = Objects.requireNonNullElseGet(request.context(), Context::unspecified);
    var record = recordRelation(scopeId, subject, request, source, target, context);
    return ResponseEntity.created(
            URI.create("/api/scopes/" + scopeId + "/statements/" + request.statementId()))
        .body(mapper.toRelationRecord(record));
  }

  /** Appends an independent context record without changing the original assertion. */
  @PostMapping("/scopes/{scopeId}/statements/{statementId}/observations")
  public ResponseEntity<Void> appendObservation(
      @PathVariable("scopeId") UUID scopeId,
      @PathVariable("statementId") String statementId,
      @RequestBody Context context) {
    var subject = currentSubject.require();
    requireScopeAccess(scopeId, subject);
    knowledge.appendObservation(scopeId, subject, statementId, context);
    return ResponseEntity.noContent().build();
  }

  /** Translates client input and CKM validation errors into a stable HTTP response. */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ProblemDetail> invalidRequest(IllegalArgumentException error) {
    return ResponseEntity.badRequest()
        .body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, error.getMessage()));
  }

  /** Translates scope ownership failures into HTTP 403. */
  @ExceptionHandler(ScopeAccessDeniedException.class)
  public ResponseEntity<ProblemDetail> forbidden(ScopeAccessDeniedException error) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, error.getMessage()));
  }

  private void requireScopeAccess(UUID scopeId, String subject) {
    var scope = knowledge.findScope(scopeId);
    if (scope == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "unknown scope: " + scopeId);
    }
    if (!scope.ownerSubject().equals(subject)) {
      throw new ScopeAccessDeniedException("subject is not the owner of scope " + scopeId);
    }
  }

  private KnowledgeApplicationService.RelationRecord recordRelation(
      UUID scopeId,
      String subject,
      RecordRelationRequest request,
      Node source,
      Node target,
      Context context) {
    if (request.knowledgeKind() == KnowledgeKind.EXPLICIT) {
      if (!request.derivedFrom().isEmpty()) {
        throw new IllegalArgumentException(
            "explicit statement cannot declare derivedFrom evidence");
      }
      return knowledge.assertRelation(
          scopeId,
          subject,
          request.relationId(),
          request.statementId(),
          request.relationType(),
          source,
          target,
          context);
    }
    return knowledge.recordDerivedRelation(
        scopeId,
        subject,
        request.relationId(),
        request.statementId(),
        request.relationType(),
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

  /** Request body for a standalone canonical Node. */
  public record CreateNodeRequest(String id, Kind kind) {
    public CreateNodeRequest {
      if (id == null || id.isBlank()) {
        throw new IllegalArgumentException("node id must not be blank");
      }
      if (kind != Kind.ENTITY && kind != Kind.CAPABILITY && kind != Kind.CONCEPT) {
        throw new IllegalArgumentException(
            "standalone node kind must be ENTITY, CAPABILITY, or CONCEPT");
      }
    }

    private Node toNode() {
      return switch (kind) {
        case ENTITY -> new Entity(id);
        case CAPABILITY -> new Capability(id);
        case CONCEPT -> new Concept(id);
        case RELATION, STATE, STATEMENT, TYPED_VALUE -> throw new IllegalStateException();
      };
    }
  }

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

    private Node resolve(UUID scopeId, String subject, KnowledgeApplicationService knowledge) {
      return switch (kind) {
        case ENTITY -> new Entity(id);
        case CAPABILITY -> new Capability(id);
        case CONCEPT -> new Concept(id);
        case STATE -> new State(id);
        case RELATION -> relation(scopeId, subject, knowledge);
        case STATEMENT, TYPED_VALUE ->
            throw new IllegalArgumentException("unsupported endpoint kind: " + kind);
      };
    }

    private Relation relation(
        UUID scopeId, String subject, KnowledgeApplicationService knowledge) {
      var relation = knowledge.findRelation(scopeId, subject, id);
      if (relation == null) {
        throw new IllegalArgumentException("unknown Relation endpoint: " + id);
      }
      return relation;
    }
  }
}
