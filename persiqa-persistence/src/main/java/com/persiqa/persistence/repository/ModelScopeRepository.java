package com.persiqa.persistence.repository;

import com.persiqa.persistence.entity.ModelScopeEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/** Standard persistence operations for model scopes. */
public interface ModelScopeRepository extends JpaRepository<ModelScopeEntity, UUID> {
  List<ModelScopeEntity> findByOwnerSubjectOrderByNameAscIdAsc(String ownerSubject);

  Page<ModelScopeEntity> findByOwnerSubjectOrderByNameAscIdAsc(
      String ownerSubject, Pageable pageable);

  Page<ModelScopeEntity> findByOwnerSubjectAndNameContainingIgnoreCaseOrderByNameAscIdAsc(
      String ownerSubject, String name, Pageable pageable);
}
