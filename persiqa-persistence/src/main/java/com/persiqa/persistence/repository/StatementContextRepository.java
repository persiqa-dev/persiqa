package com.persiqa.persistence.repository;

import com.persiqa.persistence.entity.StatementContextEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Append-preserving persistence operations for Statement context records. */
public interface StatementContextRepository extends JpaRepository<StatementContextEntity, UUID> {
  List<StatementContextEntity> findByStatementIdOrderById(UUID statementId);
}
