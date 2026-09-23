package com.persiqa.application;

import com.persiqa.application.SemanticTraversalService.SemanticMatch;
import com.persiqa.application.SemanticTraversalService.SemanticStep;
import com.persiqa.application.TopologyProjectionService.Direction;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Read-only what-if analysis for loss of electrical supply at selected topology Nodes. */
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
    return analyze(scopeId, subject, List.of(interruptedNodeId));
  }

  /** Analyzes a simultaneous interruption of one or more selected Nodes. */
  @Transactional(readOnly = true)
  public PowerImpactAnalysis analyze(
      UUID scopeId, String subject, List<String> interruptedNodeIds) {
    var interrupted = interruptedNodeIds.stream()
        .filter(java.util.Objects::nonNull)
        .map(String::trim)
        .filter(id -> !id.isEmpty())
        .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
    if (interrupted.isEmpty()) {
      throw new IllegalArgumentException("at least one interrupted Node is required");
    }
    var downstream = new HashMap<String, PowerImpactMatch>();
    var interruptedNodes = new java.util.ArrayList<Node>();
    var truncated = false;
    for (var interruptedNodeId : interrupted) {
      var result =
          traversal.traverse(
              scopeId, subject, interruptedNodeId, "supplies", Direction.DOWNSTREAM, MAX_HOPS);
      interruptedNodes.add(result.anchor());
      truncated = truncated || result.truncated();
      result.matches().forEach(match -> {
        var candidate = PowerImpactMatch.from(result.anchor(), match);
        downstream.merge(
            match.target().id(), candidate, PowerImpactAnalysisService::shorterWitness);
      });
    }
    var graph = knowledge.findRelationGraph(scopeId, subject, "supplies");
    var physicalRelations = PhysicalSupplyTopology.relations(graph.relations(), graph.statements());
    var suppliedAfterInterruption =
        suppliedNodesAfterRemoving(
            interrupted, physicalRelations, PhysicalSupplyTopology.roots(physicalRelations));
    var impacted = downstream.values().stream()
        .filter(match -> !interrupted.contains(match.target().id()))
        .filter(match -> !suppliedAfterInterruption.contains(match.target().id()))
        .sorted(
            Comparator.comparingInt(PowerImpactMatch::hops)
                .thenComparing(match -> match.target().id()))
        .toList();
    return new PowerImpactAnalysis(
        interruptedNodes.getFirst(), List.copyOf(interruptedNodes), impacted, truncated);
  }

  private static Set<String> suppliedNodesAfterRemoving(
      Set<String> interruptedNodeIds,
      List<Relation> relations,
      Set<String> physicalSupplyRoots) {
    var remainingRelations = relations.stream()
        .filter(relation -> !interruptedNodeIds.contains(relation.source().id()))
        .filter(relation -> !interruptedNodeIds.contains(relation.target().id()))
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

  private static PowerImpactMatch shorterWitness(PowerImpactMatch left, PowerImpactMatch right) {
    return Comparator.comparingInt(PowerImpactMatch::hops)
            .thenComparing(match -> match.target().id())
            .compare(left, right)
        <= 0
        ? left
        : right;
  }

  /** A temporary analysis result; it is not canonical knowledge and is never persisted. */
  public record PowerImpactAnalysis(
      Node interruptedNode,
      List<Node> interruptedNodes,
      List<PowerImpactMatch> impacted,
      boolean truncated) {}

  /** One affected Node, including the interrupted Node that establishes its witness path. */
  public record PowerImpactMatch(
      Node interruptionNode, Node target, int hops, List<SemanticStep> witness) {
    private static PowerImpactMatch from(Node interruptionNode, SemanticMatch match) {
      return new PowerImpactMatch(
          interruptionNode, match.target(), match.hops(), match.witness());
    }
  }
}
