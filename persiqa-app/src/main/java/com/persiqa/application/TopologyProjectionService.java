package com.persiqa.application;

import com.persiqa.model.Ckm.KnowledgeKind;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.Statement;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Calculates a display projection from canonical, composable relation paths. */
@Service
public class TopologyProjectionService {
  private final KnowledgeApplicationService knowledge;
  private final RefinementBindingService refinements;

  public TopologyProjectionService(
      KnowledgeApplicationService knowledge, RefinementBindingService refinements) {
    this.knowledge = knowledge;
    this.refinements = refinements;
  }

  /** Projects one same-type composable topology away from, or toward, an anchor Node. */
  @Transactional(readOnly = true)
  public TopologyProjection project(
      UUID scopeId,
      String subject,
      String anchorId,
      Direction direction,
      String relationType,
      DetailLevel detailLevel) {
    var graph = knowledge.findRelationGraph(scopeId, subject, relationType);
    var relations = topologyRelations(graph, relationType);
    var anchor = findAnchor(List.of(), relations, anchorId);
    var paths = discover(anchor, relations, direction);
    return switch (detailLevel) {
      case OVERVIEW -> overview(anchor, paths, relationType);
      case INTERMEDIATE -> intermediate(anchor, paths, relationType);
      case DETAIL -> detail(anchor, paths, relationType);
    };
  }

  /** Projects the complete forest for one system-defined topology profile. */
  @Transactional(readOnly = true)
  public TopologyProjection initial(
      UUID scopeId, String subject, TopologyProfile profile, Direction direction) {
    var graph = knowledge.findRelationGraph(scopeId, subject, profile.relationType());
    var relations = topologyRelations(graph, profile.relationType());
    return forest(withoutRefinedCoarseRelations(scopeId, relations), direction);
  }

  /** Lists the Nodes reachable from one source through a same-type composable topology. */
  @Transactional(readOnly = true)
  public List<Node> destinations(
      UUID scopeId, String subject, String sourceId, Direction direction, String relationType) {
    var graph = knowledge.findRelationGraph(scopeId, subject, relationType);
    var relations = topologyRelations(graph, relationType);
    var source = findAnchor(List.of(), relations, sourceId);
    return discover(source, relations, direction).values().stream()
        .map(Path::node)
        .filter(node -> !node.id().equals(sourceId))
        .sorted(Comparator.comparing(Node::id))
        .toList();
  }

  /** Projects every canonical relation that belongs to a path between two selected Nodes. */
  @Transactional(readOnly = true)
  public TopologyProjection projectPath(
      UUID scopeId,
      String subject,
      String sourceId,
      String destinationId,
      Direction direction,
      String relationType) {
    var graph = knowledge.findRelationGraph(scopeId, subject, relationType);
    var relations = topologyRelations(graph, relationType);
    var source = findAnchor(List.of(), relations, sourceId);
    var destination = findAnchor(List.of(), relations, destinationId);
    var detailedRelations = withoutRefinedCoarseRelations(scopeId, relations);
    var paths = discover(source, detailedRelations, direction);
    var selectedPath = paths.get(destination.id());
    if (selectedPath == null) {
      throw new IllegalArgumentException("destination is not reachable from source");
    }
    var path = orderedPath(selectedPath);
    var nodes = path.stream()
        .map(pathEntry -> new ProjectedNode(
            pathEntry.node(), pathEntry.depth(), pathEntry.node().id().equals(sourceId),
            pathEntry.node().id().equals(destinationId)))
        .toList();
    var edges = path.stream()
        .skip(1)
        .map(pathEntry -> ProjectedEdge.explicit(pathEntry.parent().node(), pathEntry))
        .toList();
    return new TopologyProjection(nodes, edges);
  }

  /**
   * Retains only canonical Relations with explicit support for topology display.
   *
   * <p>A derived Relation is a valid semantic conclusion, but rendering it as a physical topology
   * edge alongside the explicit witness chain would create a duplicate shortcut. The Relation and
   * its derived Statement remain canonical and queryable through the semantic API.
   */
  private static List<Relation> topologyRelations(
      KnowledgeApplicationService.RelationGraph graph, String relationType) {
    var candidates = matchingRelations(graph.relations(), relationType);
    var explicitRelations = candidates.stream()
        .filter(relation -> isExplicitlySupportedOrUnasserted(relation, graph.statements()))
        .toList();
    return candidates.stream()
        .filter(
            relation ->
                explicitRelations.contains(relation)
                    || !hasExplicitWitnessPath(relation, explicitRelations))
        .toList();
  }

  private static boolean hasExplicitWitnessPath(
      Relation relation, List<Relation> explicitRelations) {
    return discover(relation.source(), explicitRelations, Direction.DOWNSTREAM)
        .containsKey(relation.target().id());
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

  /**
   * Omits only coarse Relations for which an explicit persisted refinement binding exists.
   *
   * <p>This affects the detailed representation only: both the coarse and detailed canonical
   * Relations remain recorded and inspectable.
   */
  private List<Relation> withoutRefinedCoarseRelations(UUID scopeId, List<Relation> relations) {
    var refinedCoarseRelations = refinements.refinedCoarseRelationIds(scopeId);
    return relations.stream()
        .filter(relation -> !refinedCoarseRelations.contains(relation.id()))
        .toList();
  }

  private static List<Relation> matchingRelations(List<Relation> relations, String relationType) {
    return relations.stream()
        .filter(relation -> relation.type().id().equals(relationType))
        .filter(relation -> relation.type().composable())
        .sorted(Comparator.comparing(Relation::id))
        .toList();
  }

  private static Node findAnchor(List<Node> nodes, List<Relation> relations, String anchorId) {
    return allNodes(nodes, relations).get(anchorId) == null
        ? missingAnchor(anchorId)
        : allNodes(nodes, relations).get(anchorId);
  }

  private static Node missingAnchor(String anchorId) {
    throw new IllegalArgumentException("unknown topology anchor: " + anchorId);
  }

  private static Map<String, Node> allNodes(List<Node> nodes, List<Relation> relations) {
    var result = new HashMap<String, Node>();
    nodes.forEach(node -> result.put(node.id(), node));
    relations.forEach(relation -> result.put(relation.source().id(), relation.source()));
    relations.forEach(relation -> result.put(relation.target().id(), relation.target()));
    return result;
  }

  private static Map<String, Path> discover(
      Node anchor, List<Relation> relations, Direction direction) {
    var paths = new HashMap<String, Path>();
    var queue = new ArrayDeque<Node>();
    paths.put(anchor.id(), Path.anchor(anchor));
    queue.add(anchor);
    while (!queue.isEmpty()) {
      var current = queue.remove();
      var currentPath = paths.get(current.id());
      for (var relation : relations) {
        var next = nextNode(current, relation, direction);
        if (next != null && !paths.containsKey(next.id())) {
          paths.put(next.id(), currentPath.extend(next, relation));
          queue.add(next);
        }
      }
    }
    return paths;
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

  private static ProjectedEdge explicitEdge(Relation relation, Direction direction) {
    var source = orientedSource(relation, direction);
    var target = orientedTarget(relation, direction);
    return new ProjectedEdge(
        source, target, relation.type().id(), false, 1, 0, List.of(relation.id()));
  }

  private static Node orientedSource(Relation relation, Direction direction) {
    return direction == Direction.DOWNSTREAM ? relation.source() : relation.target();
  }

  private static Node orientedTarget(Relation relation, Direction direction) {
    return direction == Direction.DOWNSTREAM ? relation.target() : relation.source();
  }

  private static TopologyProjection overview(
      Node anchor, Map<String, Path> paths, String relationType) {
    var terminals = terminals(paths);
    var nodes = new ArrayList<ProjectedNode>();
    nodes.add(new ProjectedNode(anchor, 0, true, terminals.contains(anchor.id())));
    var edges = new ArrayList<ProjectedEdge>();
    for (var terminalId : terminals) {
      if (!terminalId.equals(anchor.id())) {
        var path = paths.get(terminalId);
        nodes.add(new ProjectedNode(path.node(), path.depth(), false, true));
        edges.add(
            path.depth() == 1
                ? ProjectedEdge.explicit(anchor, path)
                : ProjectedEdge.virtual(Path.anchor(anchor), path, relationType));
      }
    }
    return new TopologyProjection(nodes, edges);
  }

  private static List<Path> orderedPath(Path target) {
    var path = new ArrayList<Path>();
    for (var current = target; current != null; current = current.parent()) {
      path.add(current);
    }
    java.util.Collections.reverse(path);
    return path;
  }

  private static TopologyProjection forest(List<Relation> relations, Direction direction) {
    var nodes = allNodes(List.<Node>of(), relations);
    var roots = new HashSet<>(nodes.keySet());
    relations.forEach(relation -> roots.remove(orientedTarget(relation, direction).id()));
    var depths = new HashMap<String, Integer>();
    var rootIds = new HashSet<String>();
    roots.stream()
        .sorted()
        .forEach(root -> discoverForest(root, relations, direction, depths, rootIds));
    nodes.keySet().stream()
        .filter(nodeId -> !depths.containsKey(nodeId))
        .sorted()
        .forEach(nodeId -> discoverForest(nodeId, relations, direction, depths, rootIds));
    var terminals = new HashSet<>(nodes.keySet());
    relations.forEach(relation -> terminals.remove(orientedSource(relation, direction).id()));
    var projectedNodes = nodes.values().stream()
        .sorted(
            Comparator.comparingInt((Node node) -> depths.get(node.id())).thenComparing(Node::id))
        .map(
            node -> new ProjectedNode(
                node,
                depths.get(node.id()),
                rootIds.contains(node.id()),
                terminals.contains(node.id())))
        .toList();
    var projectedEdges =
        relations.stream().map(relation -> explicitEdge(relation, direction)).toList();
    return new TopologyProjection(projectedNodes, projectedEdges);
  }

  private static void discoverForest(
      String root,
      List<Relation> relations,
      Direction direction,
      Map<String, Integer> depths,
      Set<String> rootIds) {
    if (depths.containsKey(root)) {
      return;
    }
    rootIds.add(root);
    depths.put(root, 0);
    var queue = new ArrayDeque<String>();
    queue.add(root);
    while (!queue.isEmpty()) {
      var current = queue.remove();
      for (var relation : relations) {
        if (!orientedSource(relation, direction).id().equals(current)) {
          continue;
        }
        var next = orientedTarget(relation, direction).id();
        if (!depths.containsKey(next)) {
          depths.put(next, depths.get(current) + 1);
          queue.add(next);
        }
      }
    }
  }

  private static TopologyProjection intermediate(
      Node anchor, Map<String, Path> paths, String relationType) {
    var terminals = terminals(paths);
    var retained = new HashSet<String>();
    retained.add(anchor.id());
    paths.forEach((id, path) -> {
      if (path.depth() == 1 || terminals.contains(id)) {
        retained.add(id);
      }
    });
    return projection(anchor, paths, terminals, retained, relationType, true);
  }

  private static TopologyProjection detail(
      Node anchor, Map<String, Path> paths, String relationType) {
    return projection(anchor, paths, terminals(paths), paths.keySet(), relationType, false);
  }

  private static TopologyProjection projection(
      Node anchor,
      Map<String, Path> paths,
      Set<String> terminals,
      Set<String> retained,
      String relationType,
      boolean summarizeHiddenPaths) {
    var nodes = paths.values().stream()
        .filter(path -> retained.contains(path.node().id()))
        .sorted(Comparator.comparingInt(Path::depth).thenComparing(path -> path.node().id()))
        .map(path -> new ProjectedNode(
            path.node(), path.depth(), path.node().id().equals(anchor.id()),
            terminals.contains(path.node().id())))
        .toList();
    var edges = new ArrayList<ProjectedEdge>();
    for (var path : paths.values()) {
      if (path.parent() == null || !retained.contains(path.node().id())) {
        continue;
      }
      if (retained.contains(path.parent().node().id())) {
        edges.add(ProjectedEdge.explicit(path.parent().node(), path));
      } else if (summarizeHiddenPaths) {
        var visibleParent = nearestVisibleParent(path.parent(), retained);
        edges.add(ProjectedEdge.virtual(visibleParent, path, relationType));
      }
    }
    return new TopologyProjection(nodes, edges);
  }

  private static Path nearestVisibleParent(Path path, Set<String> retained) {
    var current = path;
    while (!retained.contains(current.node().id())) {
      current = current.parent();
    }
    return current;
  }

  private static Set<String> terminals(Map<String, Path> paths) {
    var parents = new HashSet<String>();
    paths.values().forEach(path -> {
      if (path.parent() != null) {
        parents.add(path.parent().node().id());
      }
    });
    var terminals = new HashSet<>(paths.keySet());
    terminals.removeAll(parents);
    return terminals;
  }

  /** Traversal orientation selected by the caller. */
  public enum Direction {
    DOWNSTREAM,
    UPSTREAM
  }

  /** Amount of canonical path detail exposed by a topology projection. */
  public enum DetailLevel {
    OVERVIEW,
    INTERMEDIATE,
    DETAIL
  }

  /** System-provided representation profiles; these are not CKM Core primitives. */
  public enum TopologyProfile {
    ELECTRICAL_SUPPLY("supplies", Direction.DOWNSTREAM),
    DEPENDENCY("dependsOn", Direction.DOWNSTREAM);

    private final String relationType;
    private final Direction direction;

    TopologyProfile(String relationType, Direction direction) {
      this.relationType = relationType;
      this.direction = direction;
    }

    public String relationType() {
      return relationType;
    }

    public Direction direction() {
      return direction;
    }
  }

  /** A server-calculated graph projection for visualisation only. */
  public record TopologyProjection(List<ProjectedNode> nodes, List<ProjectedEdge> edges) {}

  /** One canonical Node included in a topology projection. */
  public record ProjectedNode(Node node, int depth, boolean anchor, boolean terminal) {}

  /** One explicit or virtual path segment included in a topology projection. */
  public record ProjectedEdge(
      Node source,
      Node target,
      String relationType,
      boolean virtual,
      int hopCount,
      int hiddenNodeCount,
      List<String> supportingRelationIds) {
    private static ProjectedEdge explicit(Node source, Path targetPath) {
      return new ProjectedEdge(
          source,
          targetPath.node(),
          targetPath.relation().type().id(),
          false,
          1,
          0,
          List.of(targetPath.relation().id()));
    }

    private static ProjectedEdge virtual(Path sourcePath, Path targetPath, String relationType) {
      var relationIds = targetPath.relationIds().subList(sourcePath.depth(), targetPath.depth());
      return new ProjectedEdge(
          sourcePath.node(),
          targetPath.node(),
          relationType,
          true,
          relationIds.size(),
          Math.max(0, relationIds.size() - 1),
          relationIds);
    }
  }

  private record Path(Node node, Path parent, Relation relation, List<String> relationIds) {
    private static Path anchor(Node node) {
      return new Path(node, null, null, List.of());
    }

    private Path extend(Node next, Relation nextRelation) {
      var ids = new ArrayList<>(relationIds);
      ids.add(nextRelation.id());
      return new Path(next, this, nextRelation, List.copyOf(ids));
    }

    private int depth() {
      return relationIds.size();
    }
  }
}
