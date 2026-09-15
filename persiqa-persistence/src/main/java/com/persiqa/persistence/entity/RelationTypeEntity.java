package com.persiqa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

/** JPA representation of a versioned Relation Type contract. */
@Entity
@Table(name = "relation_type")
public class RelationTypeEntity {
  @Id
  @Column(name = "relation_type_id")
  private UUID id;

  @Column(name = "semantic_identifier", nullable = false)
  private String identifier;

  @Column(name = "semantic_version", nullable = false)
  private String version;

  @Column(name = "source_profile", nullable = false)
  private String sourceProfile;

  @Column(name = "target_profile", nullable = false)
  private String targetProfile;

  protected RelationTypeEntity() {}
}
