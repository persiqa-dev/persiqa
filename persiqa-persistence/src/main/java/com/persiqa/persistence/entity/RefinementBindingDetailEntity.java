package com.persiqa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

/** One ordered canonical Relation in a persisted refinement path. */
@Entity
@Table(name = "refinement_binding_detail")
@IdClass(RefinementBindingDetailEntity.Key.class)
public class RefinementBindingDetailEntity {
  @jakarta.persistence.Id
  @Column(name = "binding_id")
  private UUID bindingId;

  @jakarta.persistence.Id
  @Column(name = "ordinal")
  private int ordinal;

  @Column(name = "relation_id", nullable = false)
  private UUID relationId;

  protected RefinementBindingDetailEntity() {}

  public RefinementBindingDetailEntity(UUID bindingId, int ordinal, UUID relationId) {
    this.bindingId = bindingId;
    this.ordinal = ordinal;
    this.relationId = relationId;
  }

  public static class Key implements Serializable {
    private UUID bindingId;
    private int ordinal;

    public Key() {}

    public Key(UUID bindingId, int ordinal) {
      this.bindingId = bindingId;
      this.ordinal = ordinal;
    }

    @Override
    public boolean equals(Object other) {
      if (!(other instanceof Key key)) {
        return false;
      }
      return java.util.Objects.equals(bindingId, key.bindingId) && ordinal == key.ordinal;
    }

    @Override
    public int hashCode() {
      return java.util.Objects.hash(bindingId, ordinal);
    }
  }
}
