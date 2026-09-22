package com.persiqa.application;

import com.persiqa.core.CanonicalStore;
import com.persiqa.core.RefinementBindingStore;
import com.persiqa.model.Ckm.Relation;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Detects and persists unambiguous detailed paths for coarse composable Relations. */
@Service
public class RefinementBindingService {
  private static final String AUTOMATIC_DECLARER = "automatic-refinement-v0.1";

  private final CanonicalStore store;
  private final RefinementBindingStore bindings;

  public RefinementBindingService(
      CanonicalStore store,
      RefinementBindingStore bindings) {
    this.store = store;
    this.bindings = bindings;
  }

  /** Recomputes only one relation-type topology after recording a new explicit Relation. */
  @Transactional
  public void detectAndBind(UUID scopeId, String relationType) {
    detectAndBind(scopeId, store.findRelationsByType(scopeId, relationType));
  }

  private void detectAndBind(UUID scopeId, List<Relation> scopeRelations) {
    var relations = scopeRelations.stream()
        .filter(relation -> relation.type().composable())
        .toList();
    var existingBindings = bindings.findAll(scopeId);
    for (var coarse : relations) {
      reconcileBinding(scopeId, coarse, relations, existingBindings.get(coarse.id()));
    }
  }

  /** Returns coarse relation identities that a persisted refinement replaces in detailed views. */
  @Transactional(readOnly = true)
  public Set<String> refinedCoarseRelationIds(UUID scopeId) {
    return bindings.activeCoarseRelationIds(scopeId);
  }

  private void reconcileBinding(
      UUID scopeId,
      Relation coarse,
      List<Relation> relations,
      RefinementBindingStore.Binding existing) {
    var paths = detailedPaths(coarse, relations);
    if (existing != null) {
      reconcileExistingBinding(existing, paths);
      return;
    }
    if (paths.size() != 1) {
      return;
    }
    var path = paths.getFirst();
    bindings.save(
        scopeId,
        coarse.id(),
        path.stream().map(Relation::id).toList(),
        AUTOMATIC_DECLARER,
        Instant.now());
  }

  private void reconcileExistingBinding(
      RefinementBindingStore.Binding binding, List<List<Relation>> paths) {
    if (binding.active() && paths.size() != 1) {
      bindings.invalidate(binding, "automatic-refinement-ambiguous");
    } else if (!binding.active() && paths.size() == 1) {
      bindings.reactivate(binding, paths.getFirst().stream().map(Relation::id).toList());
    }
  }

  private List<List<Relation>> detailedPaths(Relation coarse, List<Relation> relations) {
    var candidates = relations.stream()
        .filter(relation -> relation.type().id().equals(coarse.type().id()))
        .filter(relation -> !relation.id().equals(coarse.id()))
        .toList();
    var paths = new ArrayList<List<Relation>>();
    collectPaths(
        coarse.source().id(),
        coarse.target().id(),
        candidates,
        new ArrayList<>(),
        new HashSet<>(Set.of(coarse.source().id())),
        paths);
    return paths;
  }

  private void collectPaths(
      String current,
      String target,
      List<Relation> relations,
      List<Relation> path,
      Set<String> visited,
      List<List<Relation>> paths) {
    if (paths.size() > 1) {
      return;
    }
    for (var relation : relations) {
      if (!relation.source().id().equals(current) || visited.contains(relation.target().id())) {
        continue;
      }
      path.add(relation);
      if (relation.target().id().equals(target)) {
        if (path.size() > 1) {
          paths.add(List.copyOf(path));
        }
      } else {
        visited.add(relation.target().id());
        collectPaths(relation.target().id(), target, relations, path, visited, paths);
        visited.remove(relation.target().id());
      }
      path.removeLast();
    }
  }
}
