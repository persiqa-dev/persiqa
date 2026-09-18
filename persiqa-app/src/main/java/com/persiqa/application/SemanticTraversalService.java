package com.persiqa.application;

import com.persiqa.application.TopologyProjectionService.Direction;
import com.persiqa.core.RelationRegistry;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.Statement;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Answers auditable reachability questions over a canonical, composable CKM Relation topology.
 *
 * <p>The service returns a deterministic shortest witness path for every reachable Node. It does
 * not create derived Statements, nor does it apply presentation-level refinement hiding.
 */
@Service
public class SemanticTraversalService {
  private final KnowledgeApplicationService knowledge;
  private final RelationRegistry registry;

  public SemanticTraversalService(
      KnowledgeApplicationService knowledge, RelationRegistry registry) {
    this.knowledge = knowledge;
    this.registry = registry;
  }

  /**
   * Finds Nodes transitively reachable through one composable Relation Type.
   *
   * @param maxHops maximum witness-path length, from one through one hundred
   */
  @Transactional(readOnly = true)
  public SemanticTraversal traverse(
      UUID scopeId,
      String subject,
      String anchorId,
      String relationType,
      Direction direction,
      int maxHops) {
    if (maxHops < 1 || maxHops > 100) {
      throw new IllegalArgumentException("maxHops must be between 1 and 100");
    }
    var snapshot = knowledge.findKnowledgeSnapshot(scopeId, subject);
    var relations = matchingComposableRelations(snapshot.relations(), relationType);
    var anchor = allNodes(snapshot.nodes(), relations).get(anchorId);
    if (anchor == null) {
      throw new IllegalArgumentException("unknown semantic query anchor: " + anchorId);
    }
    var statementsByRelation = statementsByRelation(relations, snapshot.statements());
    var paths = new HashMap<String, WitnessPath>();
    var queue = new ArrayDeque<WitnessPath>();
    var anchorPath = WitnessPath.anchor(anchor);
    paths.put(anchor.id(), anchorPath);
    queue.add(anchorPath);
    var truncated = false;
    while (!queue.isEmpty()) {
      var current = queue.remove();
      for (var relation : relations) {
        var next = nextNode(current.node(), relation, direction);
        if (next == null || paths.containsKey(next.id())) {
          continue;
        }
        if (current.hops() == maxHops) {
          truncated = true;
          continue;
        }
        var nextPath = current.extend(relation, next, statementsByRelation.get(relation.id()));
        paths.put(next.id(), nextPath);
        queue.add(nextPath);
      }
    }
    var matches = paths.values().stream()
        .filter(path -> !path.node().id().equals(anchor.id()))
        .sorted(Comparator.comparingInt(WitnessPath::hops).thenComparing(path -> path.node().id()))
        .map(path -> new SemanticMatch(path.node(), path.hops(), path.steps()))
        .toList();
    return new SemanticTraversal(anchor, relationType, direction, maxHops, truncated, matches);
  }

  private List<Relation> matchingComposableRelations(
      List<Relation> relations, String relationType) {
    var type = registry.findAll().stream()
        .filter(candidate -> candidate.id().equals(relationType))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("unknown relation type: " + relationType));
    if (!type.composable()) {
      throw new IllegalArgumentException(
          "relation type is not semantically composable: " + relationType);
    }
    return relations.stream()
        .filter(relation -> relation.type().id().equals(relationType))
        .sorted(Comparator.comparing(Relation::id))
        .toList();
  }

  private static Map<String, Node> allNodes(List<Node> nodes, List<Relation> relations) {
    var result = new HashMap<String, Node>();
    nodes.forEach(node -> result.put(node.id(), node));
    relations.forEach(relation -> {
      result.put(relation.source().id(), relation.source());
      result.put(relation.target().id(), relation.target());
    });
    return result;
  }

  private static Map<String, List<Statement>> statementsByRelation(
      List<Relation> relations, List<Statement> statements) {
    var result = new HashMap<String, List<Statement>>();
    relations.forEach(relation -> result.put(relation.id(), new ArrayList<>()));
    for (var statement : statements) {
      if (!(statement.object() instanceof Node object)) {
        continue;
      }
      for (var relation : relations) {
        if (statement.predicate().equals(relation.type().id())
            && statement.subject().id().equals(relation.source().id())
            && object.id().equals(relation.target().id())) {
          result.get(relation.id()).add(statement);
        }
      }
    }
    result.replaceAll(
        (relationId, supportingStatements) ->
            supportingStatements.stream().sorted(Comparator.comparing(Statement::id)).toList());
    return result;
  }

  private static Node nextNode(Node current, Relation relation, Direction direction) {
    if (direction == Direction.DOWNSTREAM && relation.source().id().equals(current.id())) {
      return relation.target();
    }
    if (direction == Direction.UPSTREAM && relation.target().id().equals(current.id())) {
      return relation.source();
    }
    return null;
  }

  /** Read-only semantic answer, including the selected witness paths. */
  public record SemanticTraversal(
      Node anchor,
      String relationType,
      Direction direction,
      int maxHops,
      boolean truncated,
      List<SemanticMatch> matches) {}

  /** One reachable Node and a shortest canonical witness path from the query anchor. */
  public record SemanticMatch(Node target, int hops, List<SemanticStep> witness) {}

  /** One canonical Relation in a witness path and every Statement that supports it. */
  public record SemanticStep(Relation relation, List<Statement> supportingStatements) {}

  private record WitnessPath(Node node, List<SemanticStep> steps) {
    private static WitnessPath anchor(Node node) {
      return new WitnessPath(node, List.of());
    }

    private WitnessPath extend(Relation relation, Node next, List<Statement> supportingStatements) {
      var nextSteps = new ArrayList<>(steps);
      nextSteps.add(new SemanticStep(relation, List.copyOf(supportingStatements)));
      return new WitnessPath(next, List.copyOf(nextSteps));
    }

    private int hops() {
      return steps.size();
    }
  }
}
