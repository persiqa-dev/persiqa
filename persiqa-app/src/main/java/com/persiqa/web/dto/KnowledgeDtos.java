package com.persiqa.web.dto;

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

  /** A client-loading projection of one scope's canonical graph, not a saved Representation. */
  public record ScopeKnowledgeResponse(
      ScopeResponse scope,
      List<NodeResponse> nodes,
      List<RelationResponse> relations,
      List<StatementResponse> statements) {}

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

  public record RelationRecordResponse(StatementResponse statement, RelationResponse relation) {}
}
