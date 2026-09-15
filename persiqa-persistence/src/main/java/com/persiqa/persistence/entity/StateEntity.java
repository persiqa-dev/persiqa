package com.persiqa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Map;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/** JPA representation of a contextual State. */
@Entity
@Table(name = "state")
public class StateEntity {
  @Id
  @Column(name = "state_id")
  private UUID id;

  @Column(name = "owner_object_id", nullable = false)
  private UUID ownerObjectId;

  @Column(nullable = false)
  private String predicate;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "typed_value")
  private Object typedValue;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "context_key", nullable = false)
  private Map<String, Object> contextKey;

  protected StateEntity() {}

  public StateEntity(
      UUID id,
      UUID ownerObjectId,
      String predicate,
      Object typedValue,
      Map<String, Object> contextKey) {
    this.id = id;
    this.ownerObjectId = ownerObjectId;
    this.predicate = predicate;
    this.typedValue = typedValue;
    this.contextKey = Map.copyOf(contextKey);
  }
}
