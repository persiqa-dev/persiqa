package com.persiqa.web;

import com.persiqa.application.DerivationProposalService;
import com.persiqa.application.KnowledgeApplicationService;
import com.persiqa.application.ScopeAccessService;
import com.persiqa.application.TopologyProjectionService.Direction;
import com.persiqa.model.Ckm.Capability;
import com.persiqa.model.Ckm.Concept;
import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.Entity;
import com.persiqa.model.Ckm.Kind;
import com.persiqa.model.Ckm.KnowledgeKind;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.State;
import com.persiqa.web.dto.KnowledgeDtos.ContextRequest;
import com.persiqa.web.dto.KnowledgeDtos.DerivationProposalResponse;
import com.persiqa.web.dto.KnowledgeDtos.NodeResponse;
import com.persiqa.web.dto.KnowledgeDtos.RelationRecordResponse;
import com.persiqa.web.dto.KnowledgeDtos.ScopeResponse;
import com.persiqa.web.dto.KnowledgeMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/** HTTP commands for recording statement-first CKM knowledge. */
@RestController
@RequestMapping("/api")
public class KnowledgeWriteController {
  private final KnowledgeApplicationService knowledge;
  private final CurrentSubject currentSubject;
  private final KnowledgeMapper mapper;
  private final DerivationProposalService derivations;
  private final ScopeAccessService scopeAccess;

  public KnowledgeWriteController(
      KnowledgeApplicationService knowledge,
      CurrentSubject currentSubject,
      KnowledgeMapper mapper,
      DerivationProposalService derivations,
      ScopeAccessService scopeAccess) {
    this.knowledge = knowledge;
    this.currentSubject = currentSubject;
    this.mapper = mapper;
    this.derivations = derivations;
    this.scopeAccess = scopeAccess;
  }

  /** Creates a scope with a server-assigned persistence identity owned by the caller. */
  @PostMapping("/scopes")
  public ResponseEntity<ScopeResponse> createScope(@Valid @RequestBody CreateScopeRequest request) {
    var scopeId = UUID.randomUUID();
    var owner = currentSubject.require();
    var scope = knowledge.createScope(scopeId, request.name(), owner);
    return ResponseEntity.created(URI.create("/api/scopes/" + scopeId)).body(mapper.toScope(scope));
  }

  /** Creates a standalone Entity, Capability, or Concept with no invented surrounding knowledge. */
  @PostMapping("/scopes/{scopeId}/nodes")
  public ResponseEntity<NodeResponse> createNode(
      @PathVariable("scopeId") UUID scopeId, @Valid @RequestBody CreateNodeRequest request) {
    var subject = currentSubject.require();
    scopeAccess.requireOwned(scopeId, subject);
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
      @PathVariable("scopeId") UUID scopeId, @Valid @RequestBody RecordRelationRequest request) {
    var subject = currentSubject.require();
    scopeAccess.requireOwned(scopeId, subject);
    var source = request.source().resolve(scopeId, subject, knowledge);
    var target = request.target().resolve(scopeId, subject, knowledge);
    var context =
        request.context() == null ? Context.unspecified() : mapper.toContext(request.context());
    var record =
        knowledge.recordRelation(
            scopeId,
            subject,
            request.relationId(),
            request.statementId(),
            request.knowledgeKind(),
            request.relationType(),
            source,
            target,
            request.derivedFrom(),
            context);
    return ResponseEntity.created(
            URI.create("/api/scopes/" + scopeId + "/statements/" + record.statement().id()))
        .body(mapper.toRelationRecord(record));
  }

  /**
   * Records an Entity or Relation State without exposing State as a standalone authoring object.
   */
  @PostMapping("/scopes/{scopeId}/states")
  public ResponseEntity<RelationRecordResponse> recordState(
      @PathVariable("scopeId") UUID scopeId, @Valid @RequestBody RecordStateRequest request) {
    var subject = currentSubject.require();
    scopeAccess.requireOwned(scopeId, subject);
    var owner = request.owner().resolve(scopeId, subject, knowledge);
    var context =
        request.context() == null ? Context.unspecified() : mapper.toContext(request.context());
    var record =
        knowledge.recordState(
            scopeId,
            subject,
            request.statementId(),
            owner,
            request.predicate(),
            request.value(),
            context);
    return ResponseEntity.created(
            URI.create("/api/scopes/" + scopeId + "/statements/" + record.statement().id()))
        .body(mapper.toRelationRecord(record));
  }

  /** Lists reviewable transitive conclusions without writing derived knowledge. */
  @GetMapping("/scopes/{scopeId}/semantic/derivation-proposals")
  public List<DerivationProposalResponse> findDerivationProposals(
      @PathVariable("scopeId") UUID scopeId,
      @RequestParam("anchor") String anchor,
      @RequestParam("relationType") String relationType,
      @RequestParam(value = "direction", defaultValue = "DOWNSTREAM") Direction direction,
      @RequestParam(value = "maxHops", defaultValue = "20") int maxHops) {
    var subject = currentSubject.require();
    scopeAccess.requireOwned(scopeId, subject);
    return derivations.propose(scopeId, subject, anchor, relationType, direction, maxHops).stream()
        .map(mapper::toDerivationProposal)
        .toList();
  }

  /** Accepts one current proposal and records it as a normal derived Statement. */
  @PostMapping("/scopes/{scopeId}/semantic/derivations")
  public ResponseEntity<RelationRecordResponse> acceptDerivation(
      @PathVariable("scopeId") UUID scopeId,
      @Valid @RequestBody DerivationAcceptanceRequest request) {
    var subject = currentSubject.require();
    scopeAccess.requireOwned(scopeId, subject);
    var record =
        derivations.accept(
            scopeId,
            subject,
            request.anchor(),
            request.reachable(),
            request.relationType(),
            Objects.requireNonNullElse(request.direction(), Direction.DOWNSTREAM),
            Objects.requireNonNullElse(request.maxHops(), 20),
            request.context() == null
                ? Context.unspecified()
                : mapper.toContext(request.context()));
    return ResponseEntity.created(
            URI.create("/api/scopes/" + scopeId + "/statements/" + record.statement().id()))
        .body(mapper.toRelationRecord(record));
  }

  /** Appends an independent context record without changing the original assertion. */
  @PostMapping("/scopes/{scopeId}/statements/{statementId}/observations")
  public ResponseEntity<Void> appendObservation(
      @PathVariable("scopeId") UUID scopeId,
      @PathVariable("statementId") String statementId,
      @Valid @RequestBody ContextRequest context) {
    var subject = currentSubject.require();
    scopeAccess.requireOwned(scopeId, subject);
    knowledge.appendObservation(scopeId, subject, statementId, mapper.toContext(context));
    return ResponseEntity.noContent().build();
  }

  /** Request body for server-assigned CKM scope creation. */
  public record CreateScopeRequest(@NotBlank String name) {}

  /** Request body for a standalone canonical Node. */
  public record CreateNodeRequest(@NotBlank String id, @NotNull Kind kind) {
    public CreateNodeRequest {
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
      @NotNull KnowledgeKind knowledgeKind,
      @NotBlank String relationType,
      @NotNull @Valid NodeReference source,
      @NotNull @Valid NodeReference target,
      Set<String> derivedFrom,
      ContextRequest context) {
    public RecordRelationRequest {
      derivedFrom = Set.copyOf(Objects.requireNonNullElse(derivedFrom, Set.of()));
    }

    public RecordRelationRequest(
        String relationId,
        String statementId,
        KnowledgeKind knowledgeKind,
        String relationType,
        NodeReference source,
        NodeReference target,
        Set<String> derivedFrom,
        Context context) {
      this(
          relationId,
          statementId,
          knowledgeKind,
          relationType,
          source,
          target,
          derivedFrom,
          context == null
              ? null
              : new ContextRequest(
                  context.provenance(),
                  context.confidence(),
                  context.observedAt(),
                  context.validFrom(),
                  context.validTo(),
                  context.scenario()));
    }
  }

  /** Request body for one explicit State observation owned by an Entity or Relation. */
  public record RecordStateRequest(
      String statementId,
      @NotNull @Valid NodeReference owner,
      @NotBlank String predicate,
      @NotNull Object value,
      ContextRequest context) {}

  /** Identifies one currently reviewable semantic derivation proposal to accept. */
  public record DerivationAcceptanceRequest(
      @NotBlank String anchor,
      @NotBlank String reachable,
      @NotBlank String relationType,
      Direction direction,
      Integer maxHops,
      ContextRequest context) {
    public DerivationAcceptanceRequest(
        String anchor,
        String reachable,
        String relationType,
        Direction direction,
        Integer maxHops,
        Context context) {
      this(
          anchor,
          reachable,
          relationType,
          direction,
          maxHops,
          context == null
              ? null
              : new ContextRequest(
                  context.provenance(),
                  context.confidence(),
                  context.observedAt(),
                  context.validFrom(),
                  context.validTo(),
                  context.scenario()));
    }
  }

  /**
   * Endpoint reference used by a relation-recording command.
   *
   * <p>An existing canonical Node or Relation is resolved by identity on the server. Its kind may
   * be omitted; when supplied, it must match the canonical kind. A kind is required only when the
   * identity introduces a new Node.
   */
  public record NodeReference(@NotBlank String id, Kind kind) {

    private Node resolve(UUID scopeId, String subject, KnowledgeApplicationService knowledge) {
      var existingNode = knowledge.findNode(scopeId, subject, id);
      if (existingNode != null) {
        validateSuppliedKind(existingNode.kind());
        return existingNode;
      }
      var existingRelation = knowledge.findRelation(scopeId, subject, id);
      if (existingRelation != null) {
        validateSuppliedKind(Kind.RELATION);
        return existingRelation;
      }
      if (kind == null) {
        throw new IllegalArgumentException("node kind is required for a new endpoint: " + id);
      }
      return switch (kind) {
        case ENTITY -> new Entity(id);
        case CAPABILITY -> new Capability(id);
        case CONCEPT -> new Concept(id);
        case STATE -> new State(id);
        case RELATION -> throw new IllegalArgumentException("unknown Relation endpoint: " + id);
        case STATEMENT, TYPED_VALUE ->
            throw new IllegalArgumentException("unsupported endpoint kind: " + kind);
      };
    }

    private void validateSuppliedKind(Kind resolvedKind) {
      if (kind != null && kind != resolvedKind) {
        throw new IllegalArgumentException(
            "endpoint kind does not match canonical identity " + id + ": expected " + resolvedKind);
      }
    }
  }
}
