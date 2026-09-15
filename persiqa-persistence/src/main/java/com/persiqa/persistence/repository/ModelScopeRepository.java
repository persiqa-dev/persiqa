package com.persiqa.persistence.repository;

import com.persiqa.persistence.entity.ModelScopeEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Standard persistence operations for model scopes. */
public interface ModelScopeRepository extends JpaRepository<ModelScopeEntity, UUID> {}
