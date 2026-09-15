package com.persiqa.persistence.repository;

import com.persiqa.persistence.entity.RelationEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Standard persistence operations and endpoint traversal for Relation rows. */
public interface RelationRepository extends JpaRepository<RelationEntity, UUID> {
  List<RelationEntity> findBySourceObjectId(UUID sourceObjectId);

  List<RelationEntity> findByTargetObjectId(UUID targetObjectId);
}
