package com.persiqa.persistence.repository;

import com.persiqa.persistence.entity.RepresentationEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Persistence operations for non-canonical presentation projections. */
public interface RepresentationRepository extends JpaRepository<RepresentationEntity, UUID> {}
