package com.persiqa.core;

import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.KnowledgeKind;
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
  private final RelationRegistry registry;
  private final Map<String, Relation> canonicalRelations = new HashMap<>();

  public StatementFirstWriter(CanonicalKnowledgeModel model, RelationRegistry registry) {
    this.model = model;
    this.registry = registry;
  }

  public Canonicalization assertRelation(
      String relationId,
      String statementId,
      String predicate,
      Node source,
      Node target,
      Context context) {
    var statement =
        new Statement(
            statementId, KnowledgeKind.EXPLICIT, predicate, source, target, Set.of(), context);
    model.add(statement);
    var relation = canonicalRelation(relationId, predicate, source, target);
    return new Canonicalization(statement, relation);
  }

  public Canonicalization deriveRelation(
      String relationId,
      String statementId,
      String predicate,
      Node source,
      Node target,
      Set<String> evidence,
      Context context) {
    var statement =
        new Statement(
            statementId, KnowledgeKind.DERIVED, predicate, source, target, evidence, context);
    model.add(statement);
    var relation = canonicalRelation(relationId, predicate, source, target);
    return new Canonicalization(statement, relation);
  }

  private Relation canonicalRelation(
      String relationId, String predicate, Node source, Node target) {
    var requested = registry.create(relationId, predicate, source, target);
    var existing = canonicalRelations.putIfAbsent(relationId, requested);
    if (existing == null) {
      model.add(requested);
      return requested;
    }
    if (!existing.equals(requested)) {
      throw new IllegalArgumentException(
          "canonical Relation identity cannot change: " + relationId);
    }
    return existing;
  }

  public record Canonicalization(Statement statement, Relation relation) {}
}
