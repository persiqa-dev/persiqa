package com.persiqa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

/** JPA persistence representation of an addressable CKM object. */
@Entity
@Table(name = "canonical_object")
public class CanonicalObjectEntity {
  @Id
  @Column(name = "object_id")
  private UUID id;

  @Column(name = "scope_id", nullable = false)
  private UUID scopeId;

  @Column(name = "identity_key", nullable = false)
  private String identityKey;

  @Column(nullable = false)
  private String kind;

  protected CanonicalObjectEntity() {}

  public CanonicalObjectEntity(UUID id, UUID scopeId, String identityKey, String kind) {
    this.id = id;
    this.scopeId = scopeId;
    this.identityKey = identityKey;
    this.kind = kind;
  }

  public UUID id() {
    return id;
  }

  public UUID scopeId() {
    return scopeId;
  }

  public String identityKey() {
    return identityKey;
  }

  public String kind() {
    return kind;
  }
}
