package com.persiqa.persistence.repository;

import com.persiqa.persistence.entity.CanonicalObjectEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Standard persistence operations for canonical-object rows. */
public interface CanonicalObjectRepository extends JpaRepository<CanonicalObjectEntity, UUID> {
  Optional<CanonicalObjectEntity> findByScopeIdAndIdentityKey(UUID scopeId, String identityKey);
}
