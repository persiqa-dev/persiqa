package com.persiqa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

/** JPA representation of a contextual State. */
@Entity
@Table(name = "state")
public class StateEntity {
  @Id
  @Column(name = "state_id")
  private UUID id;

  @Column(name = "owner_object_id", nullable = false)
  private UUID ownerObjectId;

  @Column(nullable = false)
  private String predicate;

  @Column(name = "typed_value")
  private String typedValue;

  @Column(name = "context_key", nullable = false)
  private String contextKey;

  protected StateEntity() {}
}
