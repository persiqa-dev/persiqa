package com.persiqa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "source_profile", nullable = false)
  private String sourceProfile;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "target_profile", nullable = false)
  private String targetProfile;

  @Column(name = "inverse_identifier")
  private String inverseIdentifier;

  @Column(name = "is_symmetric", nullable = false)
  private boolean symmetric;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "inference_policy", nullable = false)
  private String inferencePolicy;

  protected RelationTypeEntity() {}

  public RelationTypeEntity(
      UUID id,
      String identifier,
      String version,
      String sourceProfile,
      String targetProfile,
      String inverseIdentifier,
      boolean symmetric,
      String inferencePolicy) {
    this.id = id;
    this.identifier = identifier;
    this.version = version;
    this.sourceProfile = sourceProfile;
    this.targetProfile = targetProfile;
    this.inverseIdentifier = inverseIdentifier;
    this.symmetric = symmetric;
    this.inferencePolicy = inferencePolicy;
  }

  public UUID id() {
    return id;
  }

  public String identifier() {
    return identifier;
  }

  public String version() {
    return version;
  }

  public String sourceProfile() {
    return sourceProfile;
  }

  public String targetProfile() {
    return targetProfile;
  }

  public String inverseIdentifier() {
    return inverseIdentifier;
  }

  public boolean symmetric() {
    return symmetric;
  }

  public String inferencePolicy() {
    return inferencePolicy;
  }
}
