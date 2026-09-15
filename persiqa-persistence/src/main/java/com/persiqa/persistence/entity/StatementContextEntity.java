package com.persiqa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

/** JPA representation of Statement provenance and context. */
@Entity
@Table(name = "statement_context")
public class StatementContextEntity {
  @Id
  @Column(name = "context_id")
  private UUID id;

  @Column(name = "statement_id", nullable = false)
  private UUID statementId;

  @Column(name = "provenance_reference")
  private String provenanceReference;

  private Double confidence;
  private String scenario;

  protected StatementContextEntity() {}
}
