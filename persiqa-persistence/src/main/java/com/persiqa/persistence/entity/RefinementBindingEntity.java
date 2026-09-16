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

  protected RefinementBindingEntity() {}

  public RefinementBindingEntity(
      UUID id, UUID scopeId, UUID coarseRelationId, String declaredBy, Instant declaredAt) {
    this.id = id;
    this.scopeId = scopeId;
    this.coarseRelationId = coarseRelationId;
    this.declaredBy = declaredBy;
    this.declaredAt = declaredAt;
  }
}
