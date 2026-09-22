package com.persiqa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/** Persisted, non-canonical declaration that a detailed path represents one coarse Relation. */
@Entity
@Table(name = "refinement_binding")
public class RefinementBindingEntity {
  @Id
  @Column(name = "binding_id")
  private UUID id;

  @Column(name = "scope_id", nullable = false)
  private UUID scopeId;

  @Column(name = "coarse_relation_id", nullable = false)
  private UUID coarseRelationId;

  @Column(name = "declared_by", nullable = false)
  private String declaredBy;

  @Column(name = "declared_at", nullable = false)
  private Instant declaredAt;

  @Column(name = "invalidated_at")
  private Instant invalidatedAt;

  @Column(name = "invalidated_reason")
  private String invalidatedReason;

  protected RefinementBindingEntity() {}

  public RefinementBindingEntity(
      UUID id, UUID scopeId, UUID coarseRelationId, String declaredBy, Instant declaredAt) {
    this.id = id;
    this.scopeId = scopeId;
    this.coarseRelationId = coarseRelationId;
    this.declaredBy = declaredBy;
    this.declaredAt = declaredAt;
  }

  public UUID id() {
    return id;
  }

  public UUID scopeId() {
    return scopeId;
  }

  public UUID coarseRelationId() {
    return coarseRelationId;
  }

  public boolean active() {
    return invalidatedAt == null;
  }

  public void invalidate(String reason) {
    if (invalidatedAt == null) {
      invalidatedAt = Instant.now();
      invalidatedReason = reason;
    }
  }

  /** Restores an automatically invalidated binding after ambiguity has been resolved. */
  public void reactivate() {
    invalidatedAt = null;
    invalidatedReason = null;
  }
}
