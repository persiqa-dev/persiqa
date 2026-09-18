package com.persiqa.persistence.repository;

import com.persiqa.persistence.entity.RefinementBindingDetailEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Reads the ordered canonical Relation members of persisted refinement paths. */
public interface RefinementBindingDetailRepository
    extends JpaRepository<RefinementBindingDetailEntity, RefinementBindingDetailEntity.Key> {
  List<RefinementBindingDetailEntity> findByBindingIdOrderByOrdinalAsc(UUID bindingId);
}
