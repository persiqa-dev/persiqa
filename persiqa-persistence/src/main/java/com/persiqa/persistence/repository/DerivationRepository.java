package com.persiqa.persistence.repository;

import com.persiqa.persistence.entity.DerivationEntity;
import com.persiqa.persistence.entity.DerivationEntity.Key;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Persistence operations for derivation evidence links. */
public interface DerivationRepository extends JpaRepository<DerivationEntity, Key> {
  List<DerivationEntity> findByStatementId(UUID statementId);

  List<DerivationEntity> findByStatementIdIn(List<UUID> statementIds);
}
