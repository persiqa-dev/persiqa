CREATE TABLE model_scope (
  scope_id UUID PRIMARY KEY,
  name VARCHAR NOT NULL,
  owner_subject VARCHAR NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE canonical_object (
  object_id UUID PRIMARY KEY,
  scope_id UUID NOT NULL REFERENCES model_scope(scope_id),
  identity_key VARCHAR NOT NULL,
  kind VARCHAR NOT NULL CHECK (kind IN ('ENTITY','CAPABILITY','RELATION','STATE','STATEMENT','CONCEPT','TYPED_VALUE')),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  retired_at TIMESTAMP WITH TIME ZONE
);
CREATE UNIQUE INDEX canonical_object_scope_identity_idx ON canonical_object(scope_id, identity_key);
CREATE INDEX canonical_object_scope_kind_idx ON canonical_object(scope_id, kind);

CREATE TABLE identity_counter (
  scope_id UUID NOT NULL REFERENCES model_scope(scope_id),
  identity_prefix VARCHAR NOT NULL,
  next_ordinal BIGINT NOT NULL CHECK (next_ordinal > 0),
  PRIMARY KEY (scope_id, identity_prefix)
);

CREATE TABLE relation_type (
  relation_type_id UUID PRIMARY KEY,
  semantic_identifier VARCHAR NOT NULL,
  semantic_version VARCHAR NOT NULL,
  source_profile JSON NOT NULL,
  target_profile JSON NOT NULL,
  inverse_identifier VARCHAR,
  is_symmetric BOOLEAN NOT NULL DEFAULT FALSE,
  inference_policy JSON NOT NULL DEFAULT '{}',
  cardinality_policy JSON NOT NULL DEFAULT '{}',
  conflict_policy JSON NOT NULL DEFAULT '{}',
  lifecycle_status VARCHAR NOT NULL DEFAULT 'ACTIVE',
  UNIQUE (semantic_identifier, semantic_version)
);

CREATE TABLE relation (
  relation_id UUID PRIMARY KEY REFERENCES canonical_object(object_id),
  relation_type_id UUID NOT NULL REFERENCES relation_type(relation_type_id),
  source_object_id UUID NOT NULL REFERENCES canonical_object(object_id),
  target_object_id UUID NOT NULL REFERENCES canonical_object(object_id)
);
CREATE INDEX relation_source_type_idx ON relation(source_object_id, relation_type_id);
CREATE INDEX relation_target_type_idx ON relation(target_object_id, relation_type_id);
CREATE INDEX relation_type_idx ON relation(relation_type_id);

CREATE TABLE state (
  state_id UUID PRIMARY KEY REFERENCES canonical_object(object_id),
  owner_object_id UUID NOT NULL REFERENCES canonical_object(object_id),
  predicate VARCHAR NOT NULL,
  typed_value JSON,
  context_key JSON NOT NULL DEFAULT '{}'
);
CREATE INDEX state_owner_predicate_idx ON state(owner_object_id, predicate);

CREATE TABLE statement (
  statement_id UUID PRIMARY KEY REFERENCES canonical_object(object_id),
  knowledge_kind VARCHAR NOT NULL CHECK (knowledge_kind IN ('EXPLICIT','DERIVED')),
  predicate VARCHAR NOT NULL,
  subject_object_id UUID NOT NULL REFERENCES canonical_object(object_id),
  object_object_id UUID REFERENCES canonical_object(object_id),
  typed_value JSON,
  CHECK ((object_object_id IS NULL) <> (typed_value IS NULL))
);
CREATE INDEX statement_subject_predicate_idx ON statement(subject_object_id, predicate);

CREATE TABLE statement_context (
  context_id UUID PRIMARY KEY,
  statement_id UUID NOT NULL REFERENCES statement(statement_id),
  recorded_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  provenance_reference VARCHAR,
  confidence DECIMAL(5,4) CHECK (confidence IS NULL OR confidence BETWEEN 0 AND 1),
  observed_at TIMESTAMP WITH TIME ZONE,
  valid_from TIMESTAMP WITH TIME ZONE,
  valid_to TIMESTAMP WITH TIME ZONE,
  scenario VARCHAR,
  CHECK (valid_to IS NULL OR valid_from IS NULL OR valid_from <= valid_to)
);
CREATE INDEX statement_context_time_idx ON statement_context(statement_id, valid_from, valid_to);
CREATE INDEX statement_context_recorded_idx ON statement_context(statement_id, recorded_at, context_id);

CREATE TABLE derivation (
  derived_statement_id UUID NOT NULL REFERENCES statement(statement_id),
  evidence_object_id UUID NOT NULL REFERENCES canonical_object(object_id),
  rule_identifier VARCHAR NOT NULL,
  PRIMARY KEY (derived_statement_id, evidence_object_id, rule_identifier)
);

CREATE TABLE canonicalization (
  statement_id UUID NOT NULL REFERENCES statement(statement_id),
  canonical_object_id UUID NOT NULL REFERENCES canonical_object(object_id),
  mode VARCHAR NOT NULL CHECK (mode IN ('ASSERTS','SUPPORTS','DERIVES','DISPUTES')),
  policy_identifier VARCHAR NOT NULL,
  PRIMARY KEY (statement_id, canonical_object_id, mode)
);
CREATE INDEX canonicalization_object_idx ON canonicalization(canonical_object_id);

CREATE TABLE representation (
  representation_id UUID PRIMARY KEY,
  scope_id UUID NOT NULL REFERENCES model_scope(scope_id),
  name VARCHAR NOT NULL,
  selection_definition JSON NOT NULL DEFAULT '{}',
  layout_metadata JSON NOT NULL DEFAULT '{}'
);

CREATE TABLE refinement_binding (
  binding_id UUID PRIMARY KEY,
  scope_id UUID NOT NULL REFERENCES model_scope(scope_id),
  coarse_relation_id UUID NOT NULL REFERENCES relation(relation_id),
  declared_by VARCHAR NOT NULL,
  declared_at TIMESTAMP WITH TIME ZONE NOT NULL,
  invalidated_at TIMESTAMP WITH TIME ZONE,
  invalidated_reason VARCHAR,
  UNIQUE (scope_id, coarse_relation_id)
);
CREATE INDEX refinement_binding_scope_active_idx ON refinement_binding(scope_id, invalidated_at);

CREATE TABLE refinement_binding_detail (
  binding_id UUID NOT NULL REFERENCES refinement_binding(binding_id) ON DELETE CASCADE,
  relation_id UUID NOT NULL REFERENCES relation(relation_id),
  ordinal INTEGER NOT NULL CHECK (ordinal >= 0),
  PRIMARY KEY (binding_id, ordinal),
  UNIQUE (binding_id, relation_id)
);
