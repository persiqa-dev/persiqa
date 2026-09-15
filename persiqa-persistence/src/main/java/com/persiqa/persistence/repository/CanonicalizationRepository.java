package com.persiqa.persistence.repository;

import com.persiqa.persistence.entity.CanonicalizationEntity;
import com.persiqa.persistence.entity.CanonicalizationEntity.Key;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Traceable Statement-to-canonical-object association persistence. */
public interface CanonicalizationRepository extends JpaRepository<CanonicalizationEntity, Key> {
  List<CanonicalizationEntity> findByStatementId(UUID statementId);
}
