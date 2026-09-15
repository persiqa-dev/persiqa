package io.persiqa.core;

import io.persiqa.model.Ckm.*;
import java.util.Set;

/**
 * Explicit Statements are the write boundary; canonical Relations remain separately addressable.
 */
public final class StatementFirstWriter {
  private final CanonicalKnowledgeModel model;
  private final RelationRegistry registry;

  public StatementFirstWriter(CanonicalKnowledgeModel model, RelationRegistry registry) {
    this.model = model;
    this.registry = registry;
  }

  public Canonicalization assertRelation(
      String statementId, String predicate, Node source, Node target, Context context) {
    var statement =
        new Statement(
            statementId, KnowledgeKind.EXPLICIT, predicate, source, target, Set.of(), context);
    var relation = registry.create("relation:" + statementId, predicate, source, target);
    model.add(statement);
    model.add(relation);
    return new Canonicalization(statement, relation);
  }

  public Canonicalization deriveRelation(
      String statementId,
      String predicate,
      Node source,
      Node target,
      Set<String> evidence,
      Context context) {
    var statement =
        new Statement(
            statementId, KnowledgeKind.DERIVED, predicate, source, target, evidence, context);
    var relation = registry.create("relation:" + statementId, predicate, source, target);
    model.add(statement);
    model.add(relation);
    return new Canonicalization(statement, relation);
  }

  public record Canonicalization(Statement statement, Relation relation) {}
}
