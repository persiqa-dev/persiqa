package com.persiqa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
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

  @Column(name = "recorded_at", nullable = false, updatable = false)
  private Instant recordedAt;

  @Column(name = "provenance_reference")
  private String provenanceReference;

  @Column(precision = 5, scale = 4)
  private BigDecimal confidence;
  @Column(name = "observed_at")
  private Instant observedAt;
  @Column(name = "valid_from")
  private Instant validFrom;
  @Column(name = "valid_to")
  private Instant validTo;
  private String scenario;

  protected StatementContextEntity() {}

  public StatementContextEntity(
      UUID id,
      UUID statementId,
      Instant recordedAt,
      String provenanceReference,
      BigDecimal confidence,
      Instant observedAt,
      Instant validFrom,
      Instant validTo,
      String scenario) {
    this.id = id;
    this.statementId = statementId;
    this.recordedAt = recordedAt;
    this.provenanceReference = provenanceReference;
    this.confidence = confidence;
    this.observedAt = observedAt;
    this.validFrom = validFrom;
    this.validTo = validTo;
    this.scenario = scenario;
  }

  public UUID id() {
    return id;
  }

  public UUID statementId() {
    return statementId;
  }

  public Instant recordedAt() {
    return recordedAt;
  }

  public String provenanceReference() {
    return provenanceReference;
  }

  public BigDecimal confidence() {
    return confidence;
  }

  public Instant observedAt() {
    return observedAt;
  }

  public Instant validFrom() {
    return validFrom;
  }

  public Instant validTo() {
    return validTo;
  }

  public String scenario() {
    return scenario;
  }
}
