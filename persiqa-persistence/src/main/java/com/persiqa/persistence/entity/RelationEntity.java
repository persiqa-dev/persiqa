package com.persiqa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

/** JPA representation of a canonical Relation. */
@Entity
@Table(name = "relation")
public class RelationEntity {
  @Id
  @Column(name = "relation_id")
  private UUID id;

  @Column(name = "relation_type_id", nullable = false)
  private UUID relationTypeId;

  @Column(name = "source_object_id", nullable = false)
  private UUID sourceObjectId;

  @Column(name = "target_object_id", nullable = false)
  private UUID targetObjectId;

  protected RelationEntity() {}

  public RelationEntity(UUID id, UUID relationTypeId, UUID sourceObjectId, UUID targetObjectId) {
    this.id = id;
    this.relationTypeId = relationTypeId;
    this.sourceObjectId = sourceObjectId;
    this.targetObjectId = targetObjectId;
  }

  public UUID id() {
    return id;
  }

  public UUID relationTypeId() {
    return relationTypeId;
  }

  public UUID sourceObjectId() {
    return sourceObjectId;
  }

  public UUID targetObjectId() {
    return targetObjectId;
  }
}
