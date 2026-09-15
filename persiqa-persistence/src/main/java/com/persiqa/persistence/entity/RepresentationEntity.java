package com.persiqa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Map;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/** JPA representation of a non-canonical knowledge projection. */
@Entity
@Table(name = "representation")
public class RepresentationEntity {
  @Id
  @Column(name = "representation_id")
  private UUID id;

  @Column(name = "scope_id", nullable = false)
  private UUID scopeId;

  @Column(nullable = false)
  private String name;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "selection_definition", nullable = false)
  private Map<String, Object> selectionDefinition;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "layout_metadata", nullable = false)
  private Map<String, Object> layoutMetadata;

  protected RepresentationEntity() {}

  public RepresentationEntity(
      UUID id,
      UUID scopeId,
      String name,
      Map<String, Object> selectionDefinition,
      Map<String, Object> layoutMetadata) {
    this.id = id;
    this.scopeId = scopeId;
    this.name = name;
    this.selectionDefinition = Map.copyOf(selectionDefinition);
    this.layoutMetadata = Map.copyOf(layoutMetadata);
  }
}
