package com.persiqa.web.dto;

import com.persiqa.model.Ckm.Kind;
import com.persiqa.model.Ckm.KnowledgeKind;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/** Stable HTTP representations that intentionally do not expose CKM records directly. */
public final class KnowledgeDtos {
  private KnowledgeDtos() {}

  public record ScopeResponse(UUID id, String name, String ownerSubject) {}

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
