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

  public static class Key implements Serializable {
    UUID statementId;
    UUID evidenceObjectId;
    String ruleIdentifier;
  }
}
