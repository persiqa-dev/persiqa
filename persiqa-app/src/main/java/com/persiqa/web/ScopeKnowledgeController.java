package com.persiqa.web;

import com.persiqa.application.KnowledgeApplicationService;
import com.persiqa.application.SemanticTraversalService;
import com.persiqa.application.TopologyProjectionService;
import com.persiqa.application.TopologyProjectionService.DetailLevel;
import com.persiqa.application.TopologyProjectionService.Direction;
import com.persiqa.application.TopologyProjectionService.TopologyProfile;
import com.persiqa.core.PageQuery;
import com.persiqa.core.PageResult;
import com.persiqa.core.ScopeAccessDeniedException;
import com.persiqa.model.Ckm.Kind;
import com.persiqa.web.dto.KnowledgeDtos.NodeResponse;
import com.persiqa.web.dto.KnowledgeDtos.ObservationResponse;
import com.persiqa.web.dto.KnowledgeDtos.PageResponse;
import com.persiqa.web.dto.KnowledgeDtos.RelationResponse;
import com.persiqa.web.dto.KnowledgeDtos.ScopeKnowledgeResponse;
import com.persiqa.web.dto.KnowledgeDtos.SemanticTraversalResponse;
import com.persiqa.web.dto.KnowledgeDtos.StatementResponse;
import com.persiqa.web.dto.KnowledgeDtos.TopologyProjectionResponse;
import com.persiqa.web.dto.KnowledgeMapper;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** Read-only HTTP access to the knowledge held by one CKM scope. */
@RestController
@RequestMapping("/api/scopes/{scopeId}")
public class ScopeKnowledgeController {
  private final KnowledgeApplicationService knowledge;
  private final CurrentSubject currentSubject;
  private final KnowledgeMapper mapper;
  private final TopologyProjectionService topology;
  private final SemanticTraversalService semanticTraversal;

  public ScopeKnowledgeController(
      KnowledgeApplicationService knowledge,
      CurrentSubject currentSubject,
      KnowledgeMapper mapper,
      TopologyProjectionService topology,
      SemanticTraversalService semanticTraversal) {
    this.knowledge = knowledge;
    this.currentSubject = currentSubject;
    this.mapper = mapper;
    this.topology = topology;
    this.semanticTraversal = semanticTraversal;
  }

  /** Lists the canonical Relations in one scope. */
  @GetMapping("/relations")
  public PageResponse<RelationResponse> findRelations(
      @PathVariable("scopeId") UUID scopeId,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "50") int size,
      @RequestParam(value = "q", required = false) String query) {
    var subject = currentSubject.require();
    requireScopeAccess(scopeId, subject);
    return pageResponse(
        knowledge.findRelations(scopeId, subject, new PageQuery(page, size, query)),
        mapper::toRelation);
  }

  /** Lists standalone canonical Nodes in one scope. */
  @GetMapping("/nodes")
  public PageResponse<NodeResponse> findNodes(
      @PathVariable("scopeId") UUID scopeId,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "50") int size,
      @RequestParam(value = "q", required = false) String query) {
    var subject = currentSubject.require();
    requireScopeAccess(scopeId, subject);
    return pageResponse(
        knowledge.findNodes(scopeId, subject, new PageQuery(page, size, query)), mapper::toNode);
  }

  /** Returns one client-loading projection of the scope's canonical graph. */
  @GetMapping("/knowledge")
  public ScopeKnowledgeResponse findKnowledgeSnapshot(@PathVariable("scopeId") UUID scopeId) {
    var subject = currentSubject.require();
    requireScopeAccess(scopeId, subject);
    var snapshot = knowledge.findKnowledgeSnapshot(scopeId, subject);
    return new ScopeKnowledgeResponse(
        mapper.toScope(snapshot.scope()),
        snapshot.nodes().stream().map(mapper::toNode).toList(),
        mapper.toRelations(snapshot.relations()),
        mapper.toStatements(snapshot.statements()));
  }

  /** Returns a server-calculated display projection of a composable relation topology. */
  @GetMapping("/topology")
  public TopologyProjectionResponse findTopology(
      @PathVariable("scopeId") UUID scopeId,
      @RequestParam("anchor") String anchor,
      @RequestParam(value = "direction", defaultValue = "DOWNSTREAM") Direction direction,
      @RequestParam(value = "relationType", defaultValue = "supplies") String relationType,
      @RequestParam(value = "detailLevel", defaultValue = "DETAIL") DetailLevel detailLevel) {
    var subject = currentSubject.require();
    requireScopeAccess(scopeId, subject);
    var projection =
        topology.project(scopeId, subject, anchor, direction, relationType, detailLevel);
    return new TopologyProjectionResponse(
        projection.nodes().stream().map(mapper::toTopologyNode).toList(),
        projection.edges().stream().map(mapper::toTopologyEdge).toList());
  }

  /** Returns the initial layered forest for one system-provided topology profile. */
  @GetMapping("/topology/initial")
  public TopologyProjectionResponse findInitialTopology(
      @PathVariable("scopeId") UUID scopeId,
      @RequestParam(value = "profile", defaultValue = "ELECTRICAL_SUPPLY")
          TopologyProfile profile,
      @RequestParam(value = "direction", defaultValue = "DOWNSTREAM") Direction direction) {
    var subject = currentSubject.require();
    requireScopeAccess(scopeId, subject);
    var projection = topology.initial(scopeId, subject, profile, direction);
    return new TopologyProjectionResponse(
        projection.nodes().stream().map(mapper::toTopologyNode).toList(),
        projection.edges().stream().map(mapper::toTopologyEdge).toList());
  }

  /** Lists valid destination Nodes for one selected source Node. */
  @GetMapping("/topology/destinations")
  public List<NodeResponse> findTopologyDestinations(
      @PathVariable("scopeId") UUID scopeId,
      @RequestParam("source") String source,
      @RequestParam(value = "direction", defaultValue = "DOWNSTREAM") Direction direction,
      @RequestParam(value = "relationType", defaultValue = "supplies") String relationType) {
    var subject = currentSubject.require();
    requireScopeAccess(scopeId, subject);
    return topology.destinations(scopeId, subject, source, direction, relationType).stream()
        .map(mapper::toNode)
        .toList();
  }

  /** Returns the canonical path graph between a selected source and destination Node. */
  @GetMapping("/topology/path")
  public TopologyProjectionResponse findTopologyPath(
      @PathVariable("scopeId") UUID scopeId,
      @RequestParam("source") String source,
      @RequestParam("destination") String destination,
      @RequestParam(value = "direction", defaultValue = "DOWNSTREAM") Direction direction,
      @RequestParam(value = "relationType", defaultValue = "supplies") String relationType) {
    var subject = currentSubject.require();
    requireScopeAccess(scopeId, subject);
    var projection =
        topology.projectPath(scopeId, subject, source, destination, direction, relationType);
    return new TopologyProjectionResponse(
        projection.nodes().stream().map(mapper::toTopologyNode).toList(),
        projection.edges().stream().map(mapper::toTopologyEdge).toList());
  }

  /** Returns auditable transitive reachability over one semantically composable Relation Type. */
  @GetMapping("/semantic/traversal")
  public SemanticTraversalResponse traverseSemantically(
      @PathVariable("scopeId") UUID scopeId,
      @RequestParam("anchor") String anchor,
      @RequestParam("relationType") String relationType,
      @RequestParam(value = "direction", defaultValue = "DOWNSTREAM") Direction direction,
      @RequestParam(value = "maxHops", defaultValue = "20") int maxHops) {
    var subject = currentSubject.require();
    requireScopeAccess(scopeId, subject);
    return mapper.toSemanticTraversal(
        semanticTraversal.traverse(scopeId, subject, anchor, relationType, direction, maxHops));
  }

  /** Lists the Statements in one scope. */
  @GetMapping("/statements")
  public PageResponse<StatementResponse> findStatements(
      @PathVariable("scopeId") UUID scopeId,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "50") int size,
      @RequestParam(value = "q", required = false) String query) {
    var subject = currentSubject.require();
    requireScopeAccess(scopeId, subject);
    return pageResponse(
        knowledge.findStatements(scopeId, subject, new PageQuery(page, size, query)),
        mapper::toStatement);
  }

  /** Returns a standalone canonical Node by its stable identity and kind. */
  @GetMapping("/nodes/{kind}/{nodeId}")
  public ResponseEntity<NodeResponse> findNode(
      @PathVariable("scopeId") UUID scopeId,
      @PathVariable("kind") Kind kind,
      @PathVariable("nodeId") String nodeId) {
    var subject = currentSubject.require();
    requireScopeAccess(scopeId, subject);
    var node = knowledge.findNode(scopeId, subject, nodeId);
    if (node == null || node.kind() != kind) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(mapper.toNode(node));
  }

  /** Returns one Statement assertion by its stable identity. */
  @GetMapping("/statements/{statementId}")
  public ResponseEntity<StatementResponse> findStatement(
      @PathVariable("scopeId") UUID scopeId, @PathVariable("statementId") String statementId) {
    var subject = currentSubject.require();
    requireScopeAccess(scopeId, subject);
    var statement = knowledge.findStatement(scopeId, subject, statementId);
    return statement == null
        ? ResponseEntity.notFound().build()
        : ResponseEntity.ok(mapper.toStatement(statement));
  }

  /** Lists every append-preserved context for one Statement in recording order. */
  @GetMapping("/statements/{statementId}/observations")
  public ResponseEntity<List<ObservationResponse>> findObservations(
      @PathVariable("scopeId") UUID scopeId, @PathVariable("statementId") String statementId) {
    var subject = currentSubject.require();
    requireScopeAccess(scopeId, subject);
    if (knowledge.findStatement(scopeId, subject, statementId) == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(
        mapper.toObservations(knowledge.findObservations(scopeId, subject, statementId)));
  }

  /** Lists every explicit or derived Statement canonically associated with one Relation. */
  @GetMapping("/relations/{relationId}/statements")
  public ResponseEntity<List<StatementResponse>> findRelationStatements(
      @PathVariable("scopeId") UUID scopeId, @PathVariable("relationId") String relationId) {
    var subject = currentSubject.require();
    requireScopeAccess(scopeId, subject);
    if (knowledge.findRelation(scopeId, subject, relationId) == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(
        mapper.toStatements(knowledge.findStatementsForRelation(scopeId, subject, relationId)));
  }

  @ExceptionHandler(ScopeAccessDeniedException.class)
  public ResponseEntity<ProblemDetail> forbidden(ScopeAccessDeniedException error) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, error.getMessage()));
  }

  /** Translates invalid list filters into HTTP 400. */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ProblemDetail> invalidRequest(IllegalArgumentException error) {
    return ResponseEntity.badRequest()
        .body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, error.getMessage()));
  }

  private static <T, R> PageResponse<R> pageResponse(
      PageResult<T> page, Function<T, R> mapper) {
    return new PageResponse<>(
        page.content().stream().map(mapper).toList(),
        page.page(),
        page.size(),
        page.totalElements(),
        page.totalPages());
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
}
