package com.persiqa.persistence;

import com.persiqa.model.Ckm.Kind;
import com.persiqa.persistence.entity.CanonicalObjectEntity;
import com.persiqa.persistence.repository.CanonicalObjectRepository;
import com.persiqa.persistence.repository.RelationRepository;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

/** Loads only the canonical objects needed to reconstruct selected aggregate roots. */
final class JpaObjectGraphLoader {
  private final CanonicalObjectRepository objects;
  private final RelationRepository relations;

  JpaObjectGraphLoader(CanonicalObjectRepository objects, RelationRepository relations) {
    this.objects = objects;
    this.relations = relations;
  }

  List<CanonicalObjectEntity> load(UUID scopeId, Collection<UUID> rootIds) {
    var loaded = new LinkedHashMap<UUID, CanonicalObjectEntity>();
    var pending = new LinkedHashSet<>(rootIds);
    var processedRelations = new LinkedHashSet<UUID>();
    while (!pending.isEmpty()) {
      var requested = new LinkedHashSet<>(pending);
      pending.clear();
      objects
          .findByScopeIdAndIdIn(scopeId, requested)
          .forEach(object -> loaded.put(object.id(), object));
      var relationIds = loaded.values().stream()
          .filter(object -> Kind.RELATION.name().equals(object.kind()))
          .map(CanonicalObjectEntity::id)
          .filter(id -> !processedRelations.contains(id))
          .toList();
      if (relationIds.isEmpty()) {
        continue;
      }
      processedRelations.addAll(relationIds);
      relations.findAllById(relationIds).forEach(relation -> {
        if (!loaded.containsKey(relation.sourceObjectId())) {
          pending.add(relation.sourceObjectId());
        }
        if (!loaded.containsKey(relation.targetObjectId())) {
          pending.add(relation.targetObjectId());
        }
      });
    }
    return List.copyOf(loaded.values());
  }
}
