package com.persiqa.web;

import com.persiqa.application.KnowledgeApplicationService;
import com.persiqa.application.PowerImpactAnalysisService;
import com.persiqa.application.ScopeAccessService;
import com.persiqa.application.SemanticTraversalService;
import com.persiqa.application.TopologyDiagnosticsService;
import com.persiqa.application.TopologyProjectionService;
import com.persiqa.application.TopologyProjectionService.DetailLevel;
import com.persiqa.application.TopologyProjectionService.Direction;
import com.persiqa.application.TopologyProjectionService.TopologyProfile;
import com.persiqa.core.PageQuery;
import com.persiqa.core.PageResult;
import com.persiqa.model.Ckm.Kind;
import com.persiqa.web.dto.KnowledgeDtos.NodeResponse;
import com.persiqa.web.dto.KnowledgeDtos.ObservationResponse;
import com.persiqa.web.dto.KnowledgeDtos.PageResponse;
import com.persiqa.web.dto.KnowledgeDtos.PowerImpactResponse;
import com.persiqa.web.dto.KnowledgeDtos.RelationResponse;
import com.persiqa.web.dto.KnowledgeDtos.ScopeKnowledgeSummaryResponse;
import com.persiqa.web.dto.KnowledgeDtos.SemanticTraversalResponse;
import com.persiqa.web.dto.KnowledgeDtos.StatementResponse;
import com.persiqa.web.dto.KnowledgeDtos.TopologyDiagnosticsResponse;
import com.persiqa.web.dto.KnowledgeDtos.TopologyProjectionResponse;
import com.persiqa.web.dto.KnowledgeMapper;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Read-only HTTP access to the knowledge held by one CKM scope. */
@RestController
@RequestMapping("/api/scopes/{scopeId}")
public class ScopeKnowledgeController {
  private final KnowledgeApplicationService knowledge;
  private final CurrentSubject currentSubject;
  private final KnowledgeMapper mapper;
  private final TopologyProjectionService topology;
  private final SemanticTraversalService semanticTraversal;
  private final ScopeAccessService scopeAccess;
  private final PowerImpactAnalysisService powerImpact;
  private final TopologyDiagnosticsService topologyDiagnostics;

  public ScopeKnowledgeController(
      KnowledgeApplicationService knowledge,
      CurrentSubject currentSubject,
      KnowledgeMapper mapper,
      TopologyProjectionService topology,
      SemanticTraversalService semanticTraversal,
      ScopeAccessService scopeAccess,
      PowerImpactAnalysisService powerImpact,
      TopologyDiagnosticsService topologyDiagnostics) {
    this.knowledge = knowledge;
    this.currentSubject = currentSubject;
    this.mapper = mapper;
    this.topology = topology;
    this.semanticTraversal = semanticTraversal;
    this.scopeAccess = scopeAccess;
    this.powerImpact = powerImpact;
    this.topologyDiagnostics = topologyDiagnostics;
  }

  /** Lists the canonical Relations in one scope. */
  @GetMapping("/relations")
  public PageResponse<RelationResponse> findRelations(
      @PathVariable("scopeId") UUID scopeId,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "50") int size,
      @RequestParam(value = "q", required = false) String query) {
    var subject = currentSubject.require();
    scopeAccess.requireOwned(scopeId, subject);
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
    scopeAccess.requireOwned(scopeId, subject);
    return pageResponse(
        knowledge.findNodes(scopeId, subject, new PageQuery(page, size, query)), mapper::toNode);
  }

  /** Returns scope metadata and collection counts without loading the full canonical graph. */
  @GetMapping("/knowledge/summary")
  public ScopeKnowledgeSummaryResponse findKnowledgeSummary(@PathVariable("scopeId") UUID scopeId) {
    var subject = currentSubject.require();
    scopeAccess.requireOwned(scopeId, subject);
    var summary = knowledge.findKnowledgeSummary(scopeId, subject);
    return new ScopeKnowledgeSummaryResponse(
        mapper.toScope(summary.scope()),
        summary.nodeCount(),
        summary.relationCount(),
        summary.statementCount());
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
    scopeAccess.requireOwned(scopeId, subject);
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
    scopeAccess.requireOwned(scopeId, subject);
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
    scopeAccess.requireOwned(scopeId, subject);
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
    scopeAccess.requireOwned(scopeId, subject);
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
    scopeAccess.requireOwned(scopeId, subject);
    return mapper.toSemanticTraversal(
        semanticTraversal.traverse(scopeId, subject, anchor, relationType, direction, maxHops));
  }

  /** Calculates which downstream Nodes lose supplied power in a temporary interruption. */
  @GetMapping("/analysis/power-impact")
  public PowerImpactResponse analyzePowerImpact(
      @PathVariable("scopeId") UUID scopeId,
      @RequestParam("interrupted") List<String> interrupted) {
    var subject = currentSubject.require();
    scopeAccess.requireOwned(scopeId, subject);
    return mapper.toPowerImpact(powerImpact.analyze(scopeId, subject, interrupted));
  }

  /** Lists non-destructive diagnostics for the physical electrical supply topology. */
  @GetMapping("/analysis/topology-diagnostics")
  public TopologyDiagnosticsResponse analyzeTopologyDiagnostics(
      @PathVariable("scopeId") UUID scopeId) {
    var subject = currentSubject.require();
    scopeAccess.requireOwned(scopeId, subject);
    return mapper.toTopologyDiagnostics(topologyDiagnostics.analyze(scopeId, subject));
  }

  /** Lists the Statements in one scope. */
  @GetMapping("/statements")
  public PageResponse<StatementResponse> findStatements(
      @PathVariable("scopeId") UUID scopeId,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "50") int size,
      @RequestParam(value = "q", required = false) String query) {
    var subject = currentSubject.require();
    scopeAccess.requireOwned(scopeId, subject);
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
    scopeAccess.requireOwned(scopeId, subject);
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
    scopeAccess.requireOwned(scopeId, subject);
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
    scopeAccess.requireOwned(scopeId, subject);
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
    scopeAccess.requireOwned(scopeId, subject);
    if (knowledge.findRelation(scopeId, subject, relationId) == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(
        mapper.toStatements(knowledge.findStatementsForRelation(scopeId, subject, relationId)));
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

}
