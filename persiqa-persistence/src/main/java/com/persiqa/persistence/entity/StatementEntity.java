package com.persiqa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

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

  @Column(name = "typed_value")
  private String typedValue;

  protected StatementEntity() {}
}
