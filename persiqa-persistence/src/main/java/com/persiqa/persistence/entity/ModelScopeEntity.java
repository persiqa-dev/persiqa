package com.persiqa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

/** JPA representation of a canonical-model boundary. */
@Entity
@Table(name = "model_scope")
public class ModelScopeEntity {
  @Id
  @Column(name = "scope_id")
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(name = "owner_subject", nullable = false)
  private String ownerSubject;

  protected ModelScopeEntity() {}

  public ModelScopeEntity(UUID id, String name, String ownerSubject) {
    this.id = id;
    this.name = name;
    this.ownerSubject = ownerSubject;
  }

  public UUID id() {
    return id;
  }

  public String name() {
    return name;
  }

  public String ownerSubject() {
    return ownerSubject;
  }
}
