package com.persiqa.persistence.repository;

import com.persiqa.persistence.entity.IdentityCounterEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Persists scope-local identity ordinal counters. */
public interface IdentityCounterRepository
    extends JpaRepository<IdentityCounterEntity, IdentityCounterEntity.Key> {
  default IdentityCounterEntity findOrCreate(UUID scopeId, String identityPrefix) {
    var key = new IdentityCounterEntity.Key(scopeId, identityPrefix);
    return findById(key)
        .orElseGet(() -> saveAndFlush(new IdentityCounterEntity(scopeId, identityPrefix, 1)));
  }
}
