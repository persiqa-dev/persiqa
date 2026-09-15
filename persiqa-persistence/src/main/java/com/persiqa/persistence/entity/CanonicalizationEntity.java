package com.persiqa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

/** JPA representation of traceable Statement-to-canonical-object association. */
@Entity
@Table(name = "canonicalization")
@IdClass(CanonicalizationEntity.Key.class)
public class CanonicalizationEntity {
  @Id
  @Column(name = "statement_id")
  private UUID statementId;

  @Id
  @Column(name = "canonical_object_id")
  private UUID canonicalObjectId;

  @Id private String mode;

  @Column(name = "policy_identifier", nullable = false)
  private String policyIdentifier;

  protected CanonicalizationEntity() {}

  public CanonicalizationEntity(
      UUID statementId, UUID canonicalObjectId, String mode, String policyIdentifier) {
    this.statementId = statementId;
    this.canonicalObjectId = canonicalObjectId;
    this.mode = mode;
    this.policyIdentifier = policyIdentifier;
  }

  public static class Key implements Serializable {
    private UUID statementId;
    private UUID canonicalObjectId;
    private String mode;

    public Key() {}

    public Key(UUID statementId, UUID canonicalObjectId, String mode) {
      this.statementId = statementId;
      this.canonicalObjectId = canonicalObjectId;
      this.mode = mode;
    }

    @Override
    public boolean equals(Object other) {
      if (!(other instanceof Key key)) {
        return false;
      }
      return java.util.Objects.equals(statementId, key.statementId)
          && java.util.Objects.equals(canonicalObjectId, key.canonicalObjectId)
          && java.util.Objects.equals(mode, key.mode);
    }

    @Override
    public int hashCode() {
      return java.util.Objects.hash(statementId, canonicalObjectId, mode);
    }
  }
}
