package com.persiqa.persistence.repository;

import com.persiqa.persistence.entity.RefinementBindingEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Persistence operations for non-canonical refinement bindings. */
public interface RefinementBindingRepository extends JpaRepository<RefinementBindingEntity, UUID> {
  List<RefinementBindingEntity> findByScopeId(UUID scopeId);

  List<RefinementBindingEntity> findByScopeIdAndInvalidatedAtIsNull(UUID scopeId);

  boolean existsByScopeIdAndCoarseRelationIdAndInvalidatedAtIsNull(
      UUID scopeId, UUID coarseRelationId);
}
