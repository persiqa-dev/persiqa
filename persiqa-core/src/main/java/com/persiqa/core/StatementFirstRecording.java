package com.persiqa.core;

import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.KnowledgeKind;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.Statement;
import java.util.Objects;
import java.util.Set;

/**
 * Shared statement-first write ritual used by in-memory and durable adapters.
 *
 * <p>Relation Type endpoint contracts are enforced exclusively through {@link RelationRegistry}.
 */
public final class StatementFirstRecording {
  private final RelationRegistry registry;

  public StatementFirstRecording(RelationRegistry registry) {
    this.registry = Objects.requireNonNull(registry);
  }

  /** Builds an explicit Statement and its separately addressable Relation. */
  public Record assertRelation(
      String relationId,
      String statementId,
      String relationType,
      Node source,
      Node target,
      Context context) {
    var relation = registry.create(relationId, relationType, source, target);
    var statement =
        new Statement(
            statementId,
            KnowledgeKind.EXPLICIT,
            relationType,
            source,
            target,
            Set.of(),
            context);
    return new Record(statement, relation);
  }

  /** Builds a derived Statement and its separately addressable Relation. */
  public Record deriveRelation(
      String relationId,
      String statementId,
      String relationType,
      Node source,
      Node target,
      Set<String> evidence,
      Context context) {
    var relation = registry.create(relationId, relationType, source, target);
    var statement =
        new Statement(
            statementId,
            KnowledgeKind.DERIVED,
            relationType,
            source,
            target,
            evidence,
            context);
    return new Record(statement, relation);
  }

  /** Result of preparing one statement-first relation recording. */
  public record Record(Statement statement, Relation relation) {}
}
