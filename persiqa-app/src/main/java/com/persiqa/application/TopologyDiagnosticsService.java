package com.persiqa.application;

import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Reports noteworthy physical-topology structures without changing canonical knowledge. */
@Service
public class TopologyDiagnosticsService {
  private final KnowledgeApplicationService knowledge;

  public TopologyDiagnosticsService(KnowledgeApplicationService knowledge) {
    this.knowledge = knowledge;
  }

  @Transactional(readOnly = true)
  public TopologyDiagnostics analyze(UUID scopeId, String subject) {
    var graph = knowledge.findRelationGraph(scopeId, subject, "supplies");
    var relations = PhysicalSupplyTopology.relations(graph.relations(), graph.statements());
    var diagnostics = new ArrayList<TopologyDiagnostic>();
    multipleFeeds(relations)
        .forEach(
            (node, sources) ->
                diagnostics.add(
                    new TopologyDiagnostic(
                        "MULTIPLE_PHYSICAL_FEEDS", Severity.INFO, node, sources)));
    var cycle = firstCycle(relations);
    if (!cycle.isEmpty()) {
      diagnostics.add(
          new TopologyDiagnostic("DIRECTED_SUPPLY_CYCLE", Severity.WARNING, cycle.get(0), cycle));
    }
    return new TopologyDiagnostics(
        diagnostics.stream()
            .sorted(
                Comparator.comparing(TopologyDiagnostic::code)
                    .thenComparing(diagnostic -> diagnostic.node().id()))
            .toList());
  }

  private static Map<Node, List<Node>> multipleFeeds(List<Relation> relations) {
    var sourcesByTarget = new HashMap<String, List<Node>>();
    var targets = new HashMap<String, Node>();
    relations.forEach(relation -> {
      sourcesByTarget.computeIfAbsent(relation.target().id(), ignored -> new ArrayList<>())
          .add(relation.source());
      targets.put(relation.target().id(), relation.target());
    });
    var result = new HashMap<Node, List<Node>>();
    sourcesByTarget.forEach((targetId, sources) -> {
      if (sources.size() > 1) {
        result.put(
            targets.get(targetId),
            sources.stream().sorted(Comparator.comparing(Node::id)).toList());
      }
    });
    return result;
  }

  private static List<Node> firstCycle(List<Relation> relations) {
    var outgoing = new HashMap<String, List<Relation>>();
    var nodes = new HashMap<String, Node>();
    relations.forEach(relation -> {
      outgoing.computeIfAbsent(relation.source().id(), ignored -> new ArrayList<>()).add(relation);
      nodes.put(relation.source().id(), relation.source());
      nodes.put(relation.target().id(), relation.target());
    });
    outgoing.values().forEach(edges -> edges.sort(Comparator.comparing(Relation::id)));
    var states = new HashMap<String, VisitState>();
    var path = new ArrayList<String>();
    for (var nodeId : nodes.keySet().stream().sorted().toList()) {
      var cycle = visit(nodeId, outgoing, nodes, states, path);
      if (!cycle.isEmpty()) {
        return cycle;
      }
    }
    return List.of();
  }

  private static List<Node> visit(
      String nodeId,
      Map<String, List<Relation>> outgoing,
      Map<String, Node> nodes,
      Map<String, VisitState> states,
      List<String> path) {
    states.put(nodeId, VisitState.VISITING);
    path.add(nodeId);
    for (var relation : outgoing.getOrDefault(nodeId, List.of())) {
      var targetId = relation.target().id();
      if (states.get(targetId) == VisitState.VISITING) {
        return path.subList(path.indexOf(targetId), path.size()).stream().map(nodes::get).toList();
      }
      if (states.get(targetId) == null) {
        var cycle = visit(targetId, outgoing, nodes, states, path);
        if (!cycle.isEmpty()) {
          return cycle;
        }
      }
    }
    path.removeLast();
    states.put(nodeId, VisitState.VISITED);
    return List.of();
  }

  private enum VisitState {
    VISITING,
    VISITED
  }

  public enum Severity {
    INFO,
    WARNING
  }

  /** One non-destructive observation about the modeled physical supply graph. */
  public record TopologyDiagnostic(
      String code, Severity severity, Node node, List<Node> relatedNodes) {}

  /** Read-only diagnostics for one scope's physical electrical supply topology. */
  public record TopologyDiagnostics(List<TopologyDiagnostic> diagnostics) {}
}
