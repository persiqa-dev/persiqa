package com.persiqa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/** Stores the next readable identity ordinal for one scope and identity prefix. */
@Entity
@Table(name = "identity_counter")
@IdClass(IdentityCounterEntity.Key.class)
public class IdentityCounterEntity {
  @Id
  @Column(name = "scope_id")
  private UUID scopeId;

  @Id
  @Column(name = "identity_prefix")
  private String identityPrefix;

  @Column(name = "next_ordinal", nullable = false)
  private long nextOrdinal;

  protected IdentityCounterEntity() {}

  public IdentityCounterEntity(UUID scopeId, String identityPrefix, long nextOrdinal) {
    this.scopeId = scopeId;
    this.identityPrefix = identityPrefix;
    this.nextOrdinal = nextOrdinal;
  }

  /** Returns the allocated ordinal and advances the counter for the next allocation. */
  public long allocate() {
    return nextOrdinal++;
  }

  /** Composite primary key for the counter. */
  public static final class Key implements Serializable {
    private UUID scopeId;
    private String identityPrefix;

    public Key() {}

    public Key(UUID scopeId, String identityPrefix) {
      this.scopeId = scopeId;
      this.identityPrefix = identityPrefix;
    }

    @Override
    public boolean equals(Object other) {
      if (this == other) {
        return true;
      }
      if (!(other instanceof Key key)) {
        return false;
      }
      return Objects.equals(scopeId, key.scopeId)
          && Objects.equals(identityPrefix, key.identityPrefix);
    }

    @Override
    public int hashCode() {
      return Objects.hash(scopeId, identityPrefix);
    }
  }
}
