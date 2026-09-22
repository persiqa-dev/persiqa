package com.persiqa.core;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/** Persistence-independent storage boundary for presentation-level refinement bindings. */
public interface RefinementBindingStore {
  Set<String> activeCoarseRelationIds(UUID scopeId);

  Map<String, Binding> findAll(UUID scopeId);

  void save(
      UUID scopeId,
      String coarseRelationId,
      List<String> detailRelationIds,
      String declarer,
      Instant declaredAt);

  void invalidate(Binding binding, String reason);

  /** Restores an invalidated binding when one unambiguous detailed path exists again. */
  void reactivate(Binding binding, List<String> detailRelationIds);

  /** Durable metadata for one automatic or user-declared refinement. */
  record Binding(UUID id, UUID scopeId, String coarseRelationId, boolean active) {}
}
