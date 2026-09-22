package com.persiqa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

/** JPA representation of evidence for a derived Statement. */
@Entity
@Table(name = "derivation")
@IdClass(DerivationEntity.Key.class)
public class DerivationEntity {
  @jakarta.persistence.Id
  @Column(name = "derived_statement_id")
  private UUID statementId;

  @jakarta.persistence.Id
  @Column(name = "evidence_object_id")
  private UUID evidenceObjectId;

  @jakarta.persistence.Id
  @Column(name = "rule_identifier")
  private String ruleIdentifier;

  protected DerivationEntity() {}

  public DerivationEntity(UUID statementId, UUID evidenceObjectId, String ruleIdentifier) {
    this.statementId = statementId;
    this.evidenceObjectId = evidenceObjectId;
    this.ruleIdentifier = ruleIdentifier;
  }

  public UUID evidenceObjectId() {
    return evidenceObjectId;
  }

  public UUID statementId() {
    return statementId;
  }

  public static class Key implements Serializable {
    private UUID statementId;
    private UUID evidenceObjectId;
    private String ruleIdentifier;

    public Key() {}

    public Key(UUID statementId, UUID evidenceObjectId, String ruleIdentifier) {
      this.statementId = statementId;
      this.evidenceObjectId = evidenceObjectId;
      this.ruleIdentifier = ruleIdentifier;
    }

    @Override
    public boolean equals(Object other) {
      if (!(other instanceof Key key)) {
        return false;
      }
      return java.util.Objects.equals(statementId, key.statementId)
          && java.util.Objects.equals(evidenceObjectId, key.evidenceObjectId)
          && java.util.Objects.equals(ruleIdentifier, key.ruleIdentifier);
    }

    @Override
    public int hashCode() {
      return java.util.Objects.hash(statementId, evidenceObjectId, ruleIdentifier);
    }
  }
}
