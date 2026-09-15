ALTER TABLE statement_context
  ADD COLUMN recorded_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP;

CREATE INDEX statement_context_recorded_idx
  ON statement_context(statement_id, recorded_at, context_id);
