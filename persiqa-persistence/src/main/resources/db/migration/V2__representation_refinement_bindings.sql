CREATE TABLE refinement_binding (
  binding_id UUID PRIMARY KEY,
  scope_id UUID NOT NULL REFERENCES model_scope(scope_id),
  coarse_relation_id UUID NOT NULL REFERENCES relation(relation_id),
  declared_by TEXT NOT NULL,
  declared_at TIMESTAMPTZ NOT NULL,
  UNIQUE (scope_id, coarse_relation_id)
);

CREATE TABLE refinement_binding_detail (
  binding_id UUID NOT NULL REFERENCES refinement_binding(binding_id) ON DELETE CASCADE,
  relation_id UUID NOT NULL REFERENCES relation(relation_id),
  ordinal INTEGER NOT NULL CHECK (ordinal >= 0),
  PRIMARY KEY (binding_id, ordinal),
  UNIQUE (binding_id, relation_id)
);
