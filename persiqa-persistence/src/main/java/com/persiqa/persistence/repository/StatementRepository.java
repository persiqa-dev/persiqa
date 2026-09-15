package com.persiqa.persistence.repository;

import com.persiqa.persistence.entity.StatementEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Standard persistence operations and subject lookup for Statements. */
public interface StatementRepository extends JpaRepository<StatementEntity, UUID> {
  List<StatementEntity> findBySubjectObjectIdAndPredicate(UUID subjectObjectId, String predicate);
}
