package com.persiqa.persistence.repository;

import com.persiqa.persistence.entity.ModelScopeEntity;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** Standard persistence operations for model scopes. */
public interface ModelScopeRepository extends JpaRepository<ModelScopeEntity, UUID> {
  /** Locks one scope while an identity ordinal is allocated inside its write transaction. */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select scope from ModelScopeEntity scope where scope.id = :scopeId")
  Optional<ModelScopeEntity> findByIdForIdentityAllocation(@Param("scopeId") UUID scopeId);

  List<ModelScopeEntity> findByOwnerSubjectOrderByNameAscIdAsc(String ownerSubject);

  Page<ModelScopeEntity> findByOwnerSubjectOrderByNameAscIdAsc(
      String ownerSubject, Pageable pageable);

  Page<ModelScopeEntity> findByOwnerSubjectAndNameContainingIgnoreCaseOrderByNameAscIdAsc(
      String ownerSubject, String name, Pageable pageable);
}
