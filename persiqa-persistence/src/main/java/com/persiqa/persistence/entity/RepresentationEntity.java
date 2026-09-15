package com.persiqa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

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

  @Column(name = "selection_definition", nullable = false)
  private String selectionDefinition;

  @Column(name = "layout_metadata", nullable = false)
  private String layoutMetadata;

  protected RepresentationEntity() {}
}
