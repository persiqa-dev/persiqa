package com.persiqa.persistence.repository;

import com.persiqa.persistence.entity.CanonicalObjectEntity;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/** Standard persistence operations for canonical-object rows. */
public interface CanonicalObjectRepository extends JpaRepository<CanonicalObjectEntity, UUID> {
  Optional<CanonicalObjectEntity> findByScopeIdAndIdentityKey(UUID scopeId, String identityKey);

  boolean existsByScopeIdAndIdentityKey(UUID scopeId, String identityKey);

  List<CanonicalObjectEntity> findByScopeIdOrderByIdentityKey(UUID scopeId);

  List<CanonicalObjectEntity> findByScopeIdAndKindOrderByIdentityKey(UUID scopeId, String kind);

  Page<CanonicalObjectEntity> findByScopeIdAndKindInOrderByIdentityKey(
      UUID scopeId, Collection<String> kinds, Pageable pageable);

  Page<CanonicalObjectEntity>
      findByScopeIdAndKindInAndIdentityKeyContainingIgnoreCaseOrderByIdentityKey(
      UUID scopeId, Collection<String> kinds, String identityKey, Pageable pageable);

  Page<CanonicalObjectEntity>
      findByScopeIdAndKindAndIdentityKeyContainingIgnoreCaseOrderByIdentityKey(
      UUID scopeId, String kind, String identityKey, Pageable pageable);
}
