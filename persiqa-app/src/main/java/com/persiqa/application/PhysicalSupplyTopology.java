package com.persiqa.application;

import com.persiqa.model.Ckm.KnowledgeKind;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.Statement;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/** Shared physical-topology interpretation for read-only electrical analyses. */
final class PhysicalSupplyTopology {
  private PhysicalSupplyTopology() {}

  static List<Relation> relations(List<Relation> relations, List<Statement> statements) {
    return relations.stream()
        .filter(relation -> relation.type().id().equals("supplies"))
        .filter(relation -> relation.type().composable())
        .filter(relation -> isExplicitlySupportedOrUnasserted(relation, statements))
        .sorted(Comparator.comparing(Relation::id))
        .toList();
  }

  static Set<String> roots(List<Relation> relations) {
    return relations.stream()
        .map(relation -> relation.source().id())
        .filter(nodeId -> hasNoSupplyInput(nodeId, relations))
        .collect(java.util.stream.Collectors.toUnmodifiableSet());
  }

  private static boolean isExplicitlySupportedOrUnasserted(
      Relation relation, List<Statement> statements) {
    var support = statements.stream().filter(statement -> supports(statement, relation)).toList();
    return support.isEmpty()
        || support.stream()
            .anyMatch(statement -> statement.knowledgeKind() == KnowledgeKind.EXPLICIT);
  }

  private static boolean supports(Statement statement, Relation relation) {
    return statement.predicate().equals(relation.type().id())
        && statement.subject().id().equals(relation.source().id())
        && statement.object() instanceof Node target
        && target.id().equals(relation.target().id());
  }

  private static boolean hasNoSupplyInput(String nodeId, List<Relation> relations) {
    return relations.stream().noneMatch(relation -> relation.target().id().equals(nodeId));
  }
}
