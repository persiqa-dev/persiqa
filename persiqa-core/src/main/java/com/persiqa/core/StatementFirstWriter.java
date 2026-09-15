package com.persiqa.core;

import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Explicit Statements are the write boundary; canonical Relations remain separately addressable.
 */
public final class StatementFirstWriter {
  private final CanonicalKnowledgeModel model;
  private final StatementFirstRecording recording;
  private final Map<String, Relation> canonicalRelations = new HashMap<>();

  public StatementFirstWriter(CanonicalKnowledgeModel model, RelationRegistry registry) {
    this.model = model;
    this.recording = new StatementFirstRecording(registry);
  }

  public Canonicalization assertRelation(
      String relationId,
      String statementId,
      String predicate,
      Node source,
      Node target,
      Context context) {
    var prepared =
        recording.assertRelation(relationId, statementId, predicate, source, target, context);
    model.add(prepared.statement());
    return new Canonicalization(prepared.statement(), retainCanonical(prepared.relation()));
  }

  public Canonicalization deriveRelation(
      String relationId,
      String statementId,
      String predicate,
      Node source,
      Node target,
      Set<String> evidence,
      Context context) {
    var prepared =
        recording.deriveRelation(
            relationId, statementId, predicate, source, target, evidence, context);
    model.add(prepared.statement());
    return new Canonicalization(prepared.statement(), retainCanonical(prepared.relation()));
  }

  private Relation retainCanonical(Relation requested) {
    var existing = canonicalRelations.putIfAbsent(requested.id(), requested);
    if (existing == null) {
      model.add(requested);
      return requested;
    }
    if (!existing.equals(requested)) {
      throw new IllegalArgumentException(
          "canonical Relation identity cannot change: " + requested.id());
    }
    return existing;
  }

  public record Canonicalization(Statement statement, Relation relation) {}
}
