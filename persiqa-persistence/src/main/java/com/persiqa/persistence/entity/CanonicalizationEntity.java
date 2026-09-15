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

  public static class Key implements Serializable {
    UUID statementId;
    UUID canonicalObjectId;
    String mode;
  }
}
