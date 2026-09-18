package com.persiqa.application;

import com.persiqa.core.CanonicalStore;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.persistence.entity.RefinementBindingDetailEntity;
import com.persiqa.persistence.entity.RefinementBindingEntity;
import com.persiqa.persistence.repository.CanonicalObjectRepository;
import com.persiqa.persistence.repository.RefinementBindingDetailRepository;
import com.persiqa.persistence.repository.RefinementBindingRepository;
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
  private final CanonicalObjectRepository objects;
  private final RefinementBindingRepository bindings;
  private final RefinementBindingDetailRepository details;

  public RefinementBindingService(
      CanonicalStore store,
      CanonicalObjectRepository objects,
      RefinementBindingRepository bindings,
      RefinementBindingDetailRepository details) {
    this.store = store;
    this.objects = objects;
    this.bindings = bindings;
    this.details = details;
  }

  /** Persists every newly discoverable, unambiguous refinement in the given scope. */
  @Transactional
  public void detectAndBind(UUID scopeId) {
    var relations = store.findRelations(scopeId).stream()
        .filter(relation -> relation.type().composable())
        .toList();
    for (var coarse : relations) {
      reconcileBinding(scopeId, coarse, relations);
    }
  }

  /** Returns coarse relation identities that a persisted refinement replaces in detailed views. */
  @Transactional(readOnly = true)
  public Set<String> refinedCoarseRelationIds(UUID scopeId) {
    return bindings.findByScopeIdAndInvalidatedAtIsNull(scopeId).stream()
        .map(RefinementBindingEntity::coarseRelationId)
        .map(objects::findById)
        .flatMap(java.util.Optional::stream)
        .map(object -> object.identityKey())
        .collect(java.util.stream.Collectors.toUnmodifiableSet());
  }

  private void reconcileBinding(UUID scopeId, Relation coarse, List<Relation> relations) {
    var coarseId = objectId(scopeId, coarse.id());
    var paths = detailedPaths(coarse, relations);
    var existing = bindings.findByScopeId(scopeId).stream()
        .filter(binding -> binding.coarseRelationId().equals(coarseId))
        .findFirst();
    if (existing.isPresent()) {
      invalidateWhenAmbiguous(existing.get(), paths);
      return;
    }
    if (paths.size() != 1) {
      return;
    }
    var bindingId = UUID.randomUUID();
    bindings.save(
        new RefinementBindingEntity(
            bindingId, scopeId, coarseId, AUTOMATIC_DECLARER, Instant.now()));
    var path = paths.getFirst();
    for (var ordinal = 0; ordinal < path.size(); ordinal++) {
      details.save(
          new RefinementBindingDetailEntity(
              bindingId, ordinal, objectId(scopeId, path.get(ordinal).id())));
    }
  }

  private void invalidateWhenAmbiguous(
      RefinementBindingEntity binding, List<List<Relation>> paths) {
    if (binding.active() && paths.size() != 1) {
      binding.invalidate("automatic-refinement-ambiguous");
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

  private UUID objectId(UUID scopeId, String identity) {
    return objects.findByScopeIdAndIdentityKey(scopeId, identity).orElseThrow().id();
  }
}
