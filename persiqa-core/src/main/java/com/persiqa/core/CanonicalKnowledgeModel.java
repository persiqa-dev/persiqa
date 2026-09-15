package com.persiqa.core;

import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.Statement;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/** Canonical in-memory store. Views are projections and never duplicate model objects. */
public final class CanonicalKnowledgeModel {
  private final Map<String, Node> nodes = new LinkedHashMap<>();
  private final Map<String, Relation> relations = new LinkedHashMap<>();
  private final Map<String, Statement> statements = new LinkedHashMap<>();

  public <T extends Node> T add(T node) {
    put(nodes, node.id(), node);
    return node;
  }

  public Relation add(Relation relation) {
    put(relations, relation.id(), relation);
    ensure(relation.source());
    ensure(relation.target());
    return relation;
  }

  public Statement add(Statement statement) {
    put(statements, statement.id(), statement);
    add(statement.subject());
    return statement;
  }

  public Collection<Node> nodes() {
    return List.copyOf(nodes.values());
  }

  public Collection<Relation> relations() {
    return List.copyOf(relations.values());
  }

  public Collection<Statement> statements() {
    return List.copyOf(statements.values());
  }

  public View view(String name, Predicate<Relation> selection) {
    return new View(name, relations.values().stream().filter(selection).toList());
  }

  private static <T> void put(Map<String, T> map, String id, T value) {
    if (map.putIfAbsent(id, value) != null) {
      throw new IllegalArgumentException("duplicate canonical id: " + id);
    }
  }

  private void ensure(Node node) {
    nodes.putIfAbsent(node.id(), node);
  }

  public record View(String name, List<Relation> relations) {
    public View {
      relations = List.copyOf(relations);
    }
  }
}
