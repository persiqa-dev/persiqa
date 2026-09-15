package com.persiqa.persistence.repository;

import com.persiqa.persistence.entity.RelationTypeEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Standard persistence operations for Relation Type contracts. */
public interface RelationTypeRepository extends JpaRepository<RelationTypeEntity, UUID> {
  Optional<RelationTypeEntity> findByIdentifierAndVersion(String identifier, String version);
}
