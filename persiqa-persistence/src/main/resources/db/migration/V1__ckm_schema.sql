CREATE TABLE model_scope (
  scope_id UUID PRIMARY KEY,
  name TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE canonical_object (
  object_id UUID PRIMARY KEY,
  scope_id UUID NOT NULL REFERENCES model_scope(scope_id),
  kind TEXT NOT NULL CHECK (kind IN ('ENTITY','CAPABILITY','RELATION','STATE','STATEMENT','CONCEPT','TYPED_VALUE')),
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  retired_at TIMESTAMPTZ
);
CREATE INDEX canonical_object_scope_kind_idx ON canonical_object(scope_id, kind);

CREATE TABLE relation_type (
  relation_type_id UUID PRIMARY KEY,
  semantic_identifier TEXT NOT NULL,
  semantic_version TEXT NOT NULL,
  source_profile JSONB NOT NULL,
  target_profile JSONB NOT NULL,
  inverse_identifier TEXT,
  symmetric BOOLEAN NOT NULL DEFAULT FALSE,
  inference_policy JSONB NOT NULL DEFAULT '{}'::jsonb,
  cardinality_policy JSONB NOT NULL DEFAULT '{}'::jsonb,
  conflict_policy JSONB NOT NULL DEFAULT '{}'::jsonb,
  lifecycle_status TEXT NOT NULL DEFAULT 'ACTIVE',
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

CREATE TABLE state (
  state_id UUID PRIMARY KEY REFERENCES canonical_object(object_id),
  owner_object_id UUID NOT NULL REFERENCES canonical_object(object_id),
  predicate TEXT NOT NULL,
  typed_value JSONB,
  context_key JSONB NOT NULL DEFAULT '{}'::jsonb
);
CREATE INDEX state_owner_predicate_idx ON state(owner_object_id, predicate);

CREATE TABLE statement (
  statement_id UUID PRIMARY KEY REFERENCES canonical_object(object_id),
  knowledge_kind TEXT NOT NULL CHECK (knowledge_kind IN ('EXPLICIT','DERIVED')),
  predicate TEXT NOT NULL,
  subject_object_id UUID NOT NULL REFERENCES canonical_object(object_id),
  object_object_id UUID REFERENCES canonical_object(object_id),
  typed_value JSONB,
  CHECK ((object_object_id IS NULL) <> (typed_value IS NULL))
);
CREATE INDEX statement_subject_predicate_idx ON statement(subject_object_id, predicate);

CREATE TABLE statement_context (
  context_id UUID PRIMARY KEY,
  statement_id UUID NOT NULL REFERENCES statement(statement_id),
  provenance_reference TEXT,
  confidence NUMERIC(5,4) CHECK (confidence IS NULL OR confidence BETWEEN 0 AND 1),
  observed_at TIMESTAMPTZ,
  valid_from TIMESTAMPTZ,
  valid_to TIMESTAMPTZ,
  scenario TEXT,
  CHECK (valid_to IS NULL OR valid_from IS NULL OR valid_from <= valid_to)
);
CREATE INDEX statement_context_time_idx ON statement_context(statement_id, valid_from, valid_to);

CREATE TABLE derivation (
  derived_statement_id UUID NOT NULL REFERENCES statement(statement_id),
  evidence_object_id UUID NOT NULL REFERENCES canonical_object(object_id),
  rule_identifier TEXT NOT NULL,
  PRIMARY KEY (derived_statement_id, evidence_object_id, rule_identifier)
);

CREATE TABLE canonicalization (
  statement_id UUID NOT NULL REFERENCES statement(statement_id),
  canonical_object_id UUID NOT NULL REFERENCES canonical_object(object_id),
  mode TEXT NOT NULL CHECK (mode IN ('ASSERTS','SUPPORTS','DERIVES','DISPUTES')),
  policy_identifier TEXT NOT NULL,
  PRIMARY KEY (statement_id, canonical_object_id, mode)
);

CREATE TABLE representation (
  representation_id UUID PRIMARY KEY,
  scope_id UUID NOT NULL REFERENCES model_scope(scope_id),
  name TEXT NOT NULL,
  selection_definition JSONB NOT NULL DEFAULT '{}'::jsonb,
  layout_metadata JSONB NOT NULL DEFAULT '{}'::jsonb
);
