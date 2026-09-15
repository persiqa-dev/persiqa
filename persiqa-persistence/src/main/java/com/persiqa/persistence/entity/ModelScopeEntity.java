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

  protected ModelScopeEntity() {}

  public ModelScopeEntity(UUID id, String name) {
    this.id = id;
    this.name = name;
  }

  public UUID id() {
    return id;
  }

  public String name() {
    return name;
  }
}
