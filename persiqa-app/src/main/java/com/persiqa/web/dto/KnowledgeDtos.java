package com.persiqa.web.dto;

import com.persiqa.application.TopologyProjectionService.Direction;
import com.persiqa.model.Ckm.Kind;
import com.persiqa.model.Ckm.KnowledgeKind;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/** Stable HTTP representations that intentionally do not expose CKM records directly. */
public final class KnowledgeDtos {
  private KnowledgeDtos() {}

  public record ScopeResponse(UUID id, String name, String ownerSubject) {}

  /** Stable envelope for a server-side paged API collection. */
  public record PageResponse<T>(
      List<T> content, int page, int size, long totalElements, int totalPages) {}

  public record NodeResponse(String id, Kind kind) {}

  public record RelationTypeResponse(
      String id,
      Set<Kind> sources,
      Set<Kind> targets,
      boolean symmetric,
      String inverse,
      boolean composable) {}

  public record RelationResponse(
      String id, RelationTypeResponse type, NodeResponse source, NodeResponse target) {}

  /** Compact scope overview; canonical collections are obtained through their paged endpoints. */
  public record ScopeKnowledgeSummaryResponse(
      ScopeResponse scope, long nodeCount, long relationCount, long statementCount) {}

  /** Server-calculated display projection; it does not create canonical CKM knowledge. */
  public record TopologyProjectionResponse(
      List<TopologyNodeResponse> nodes, List<TopologyEdgeResponse> edges) {}

  public record TopologyNodeResponse(
      NodeResponse node, int depth, boolean anchor, boolean terminal) {}

  public record TopologyEdgeResponse(
      NodeResponse source,
      NodeResponse target,
      String relationType,
      boolean virtual,
      int hopCount,
      int hiddenNodeCount,
      List<String> supportingRelationIds) {}

  /** Auditable result of a read-only semantic traversal. */
  public record SemanticTraversalResponse(
      NodeResponse anchor,
      String relationType,
      Direction direction,
      int maxHops,
      boolean truncated,
      List<SemanticMatchResponse> matches) {}

  public record SemanticMatchResponse(
      NodeResponse target, int hops, List<SemanticStepResponse> witness) {}

  public record SemanticStepResponse(
      RelationResponse relation, List<StatementResponse> supportingStatements) {}

  /** Read-only impact of temporarily removing supply from one selected topology Node. */
  public record PowerImpactResponse(
      NodeResponse interruptedNode, boolean truncated, List<SemanticMatchResponse> impacted) {}

  /** A reviewable, non-persisted derived-knowledge conclusion. */
  public record DerivationProposalResponse(
      NodeResponse reachable,
      NodeResponse source,
      NodeResponse target,
      String relationType,
      int hops,
      List<String> evidenceStatementIds,
      List<String> supportingRelationIds) {}

  /**
   * Statement assertion including its original knowledge context.
   *
   * <p>{@code context} is the immutable assertion context. Later observations are listed under
   * {@code /observations} and do not replace it.
   */
  public record StatementResponse(
      String id,
      KnowledgeKind knowledgeKind,
      String predicate,
      NodeResponse subject,
      Object object,
      Set<String> derivedFrom,
      ObservationResponse context) {}

  public record ObservationResponse(
      String provenance,
      BigDecimal confidence,
      Instant observedAt,
      Instant validFrom,
      Instant validTo,
      String scenario) {}

  /** HTTP input for statement provenance and temporal context. */
  public record ContextRequest(
      String provenance,
      BigDecimal confidence,
      Instant observedAt,
      Instant validFrom,
      Instant validTo,
      String scenario) {}

  public record RelationRecordResponse(StatementResponse statement, RelationResponse relation) {}
}
