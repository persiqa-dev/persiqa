package com.persiqa.application;

import com.persiqa.application.SemanticTraversalService.SemanticMatch;
import com.persiqa.application.TopologyProjectionService.Direction;
import com.persiqa.model.Ckm.KnowledgeKind;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.Statement;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Read-only what-if analysis for loss of electrical supply at one topology Node. */
@Service
public class PowerImpactAnalysisService {
  private static final int MAX_HOPS = 100;

  private final KnowledgeApplicationService knowledge;
  private final SemanticTraversalService traversal;

  public PowerImpactAnalysisService(
      KnowledgeApplicationService knowledge, SemanticTraversalService traversal) {
    this.knowledge = knowledge;
    this.traversal = traversal;
  }

  /**
   * Assumes the selected device no longer conducts power and returns downstream Nodes that lose
   * every known physical supply path, with their shortest canonical witness from the interruption.
   *
   * <p>Derived supply shortcuts are intentionally excluded when establishing alternative physical
   * feeds: a conclusion such as {@code MainSwitch supplies Boiler} cannot keep the boiler powered
   * after an intermediate breaker on its evidence path has been removed.
   */
  @Transactional(readOnly = true)
  public PowerImpactAnalysis analyze(UUID scopeId, String subject, String interruptedNodeId) {
    var result =
        traversal.traverse(
            scopeId, subject, interruptedNodeId, "supplies", Direction.DOWNSTREAM, MAX_HOPS);
    var graph = knowledge.findRelationGraph(scopeId, subject, "supplies");
    var physicalRelations = physicalSupplyRelations(graph.relations(), graph.statements());
    var suppliedAfterInterruption =
        suppliedNodesAfterRemoving(
            interruptedNodeId, physicalRelations, physicalSupplyRoots(physicalRelations));
    var impacted = result.matches().stream()
        .filter(match -> !suppliedAfterInterruption.contains(match.target().id()))
        .toList();
    return new PowerImpactAnalysis(result.anchor(), impacted, result.truncated());
  }

  private static List<Relation> physicalSupplyRelations(
      List<Relation> relations, List<Statement> statements) {
    return relations.stream()
        .filter(relation -> relation.type().id().equals("supplies"))
        .filter(relation -> relation.type().composable())
        .filter(relation -> isExplicitlySupportedOrUnasserted(relation, statements))
        .sorted(Comparator.comparing(Relation::id))
        .toList();
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

  private static Set<String> suppliedNodesAfterRemoving(
      String interruptedNodeId, List<Relation> relations, Set<String> physicalSupplyRoots) {
    var remainingRelations = relations.stream()
        .filter(relation -> !relation.source().id().equals(interruptedNodeId))
        .filter(relation -> !relation.target().id().equals(interruptedNodeId))
        .toList();
    var nodes = new HashMap<String, Node>();
    remainingRelations.forEach(relation -> {
      nodes.put(relation.source().id(), relation.source());
      nodes.put(relation.target().id(), relation.target());
    });
    var suppliedNodes = new HashSet<String>();
    var queue = new ArrayDeque<String>();
    physicalSupplyRoots.stream()
        .filter(nodes::containsKey)
        .sorted()
        .forEach(nodeId -> {
          suppliedNodes.add(nodeId);
          queue.add(nodeId);
        });
    while (!queue.isEmpty()) {
      var current = queue.remove();
      remainingRelations.stream()
          .filter(relation -> relation.source().id().equals(current))
          .map(relation -> relation.target().id())
          .filter(suppliedNodes::add)
          .forEach(queue::add);
    }
    return Set.copyOf(suppliedNodes);
  }

  private static boolean hasNoPhysicalSupplyInput(String nodeId, List<Relation> relations) {
    return relations.stream().noneMatch(relation -> relation.target().id().equals(nodeId));
  }

  private static Set<String> physicalSupplyRoots(List<Relation> relations) {
    return relations.stream()
        .map(relation -> relation.source().id())
        .filter(nodeId -> hasNoPhysicalSupplyInput(nodeId, relations))
        .collect(java.util.stream.Collectors.toUnmodifiableSet());
  }

  /** A temporary analysis result; it is not canonical knowledge and is never persisted. */
  public record PowerImpactAnalysis(
      Node interruptedNode, List<SemanticMatch> impacted, boolean truncated) {}
}
