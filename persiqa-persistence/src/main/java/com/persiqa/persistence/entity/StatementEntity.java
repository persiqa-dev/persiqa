package com.persiqa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/** JPA representation of an explicit or derived knowledge Statement. */
@Entity
@Table(name = "statement")
public class StatementEntity {
  @Id
  @Column(name = "statement_id")
  private UUID id;

  @Column(name = "knowledge_kind", nullable = false)
  private String knowledgeKind;

  @Column(nullable = false)
  private String predicate;

  @Column(name = "subject_object_id", nullable = false)
  private UUID subjectObjectId;

  @Column(name = "object_object_id")
  private UUID objectObjectId;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "typed_value")
  private String typedValue;

  protected StatementEntity() {}

  public StatementEntity(
      UUID id,
      String knowledgeKind,
      String predicate,
      UUID subjectObjectId,
      UUID objectObjectId,
      String typedValue) {
    this.id = id;
    this.knowledgeKind = knowledgeKind;
    this.predicate = predicate;
    this.subjectObjectId = subjectObjectId;
    this.objectObjectId = objectObjectId;
    this.typedValue = typedValue;
  }

  public UUID id() {
    return id;
  }

  public String knowledgeKind() {
    return knowledgeKind;
  }

  public String predicate() {
    return predicate;
  }

  public UUID subjectObjectId() {
    return subjectObjectId;
  }

  public UUID objectObjectId() {
    return objectObjectId;
  }

  public String typedValue() {
    return typedValue;
  }
}
