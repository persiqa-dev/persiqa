package com.persiqa.persistence.repository;

import com.persiqa.persistence.entity.StateEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Persistence operations for State ownership records. */
public interface StateRepository extends JpaRepository<StateEntity, UUID> {}
