package com.persiqa.persistence;

import com.persiqa.model.Ckm.Capability;
import com.persiqa.model.Ckm.Concept;
import com.persiqa.model.Ckm.Entity;
import com.persiqa.model.Ckm.Kind;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.RelationType;
import com.persiqa.model.Ckm.State;
import com.persiqa.persistence.entity.CanonicalObjectEntity;
import com.persiqa.persistence.entity.RelationEntity;
import com.persiqa.persistence.entity.RelationTypeEntity;
import com.persiqa.persistence.repository.RelationRepository;
import com.persiqa.persistence.repository.RelationTypeRepository;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/** Reconstructs a relation collection from preloaded JPA rows without per-relation queries. */
final class JpaRelationReader {
  private final RelationRepository relations;
  private final RelationTypeRepository relationTypes;
  private final JpaObjectGraphLoader objectGraphLoader;

  JpaRelationReader(
      RelationRepository relations,
      RelationTypeRepository relationTypes,
      JpaObjectGraphLoader objectGraphLoader) {
    this.relations = relations;
    this.relationTypes = relationTypes;
    this.objectGraphLoader = objectGraphLoader;
  }

  List<Relation> readSelected(UUID scopeId, Set<UUID> relationIds) {
    return read(scopeId, objectGraphLoader.load(scopeId, relationIds), relationIds);
  }

  List<Relation> read(UUID scopeId, List<CanonicalObjectEntity> objects) {
    return read(scopeId, objects, null);
  }

  List<Relation> read(
      UUID scopeId, List<CanonicalObjectEntity> objects, Set<UUID> selectedRelationIds) {
    var objectsById = objects.stream().collect(java.util.stream.Collectors.toMap(
        CanonicalObjectEntity::id, object -> object));
    var relationObjects = objects.stream()
        .filter(object -> Kind.RELATION.name().equals(object.kind()))
        .filter(object -> selectedRelationIds == null || selectedRelationIds.contains(object.id()))
        .toList();
    var relationsById = relations.findAllById(
        relationObjects.stream().map(CanonicalObjectEntity::id).toList()).stream()
        .collect(java.util.stream.Collectors.toMap(RelationEntity::id, relation -> relation));
    var typesById = relationTypes.findAllById(
        relationsById.values().stream().map(RelationEntity::relationTypeId).toList()).stream()
        .collect(java.util.stream.Collectors.toMap(RelationTypeEntity::id, type -> type));
    var resolver = new Resolver(scopeId, objectsById, relationsById, typesById);
    return relationObjects.stream()
        .map(object -> resolver.node(object.id(), new java.util.HashSet<>()))
        .map(Relation.class::cast)
        .toList();
  }

  private static final class Resolver {
    private final UUID scopeId;
    private final Map<UUID, CanonicalObjectEntity> objects;
    private final Map<UUID, RelationEntity> relations;
    private final Map<UUID, RelationTypeEntity> types;

    private Resolver(
        UUID scopeId,
        Map<UUID, CanonicalObjectEntity> objects,
        Map<UUID, RelationEntity> relations,
        Map<UUID, RelationTypeEntity> types) {
      this.scopeId = scopeId;
      this.objects = objects;
      this.relations = relations;
      this.types = types;
    }

    private Node node(UUID id, Set<UUID> resolving) {
      var object = objects.get(id);
      if (object == null || !object.scopeId().equals(scopeId)) {
        throw new IllegalStateException("cross-scope canonical object reference");
      }
      return switch (Kind.valueOf(object.kind())) {
        case ENTITY -> new Entity(object.identityKey());
        case CAPABILITY -> new Capability(object.identityKey());
        case CONCEPT -> new Concept(object.identityKey());
        case STATE -> new State(object.identityKey());
        case RELATION -> relation(object, resolving);
        case STATEMENT, TYPED_VALUE -> throw new IllegalStateException(
            "unsupported endpoint kind: " + object.kind());
      };
    }

    private Relation relation(CanonicalObjectEntity object, Set<UUID> resolving) {
      if (!resolving.add(object.id())) {
        throw new IllegalStateException("cyclic Relation endpoint");
      }
      try {
        var relation = relations.get(object.id());
        var type = types.get(relation.relationTypeId());
        return new Relation(
            object.identityKey(),
            relationType(type),
            node(relation.sourceObjectId(), resolving),
            node(relation.targetObjectId(), resolving));
      } finally {
        resolving.remove(object.id());
      }
    }

    private static RelationType relationType(RelationTypeEntity entity) {
      return new RelationType(
          entity.identifier(),
          Set.copyOf(entity.sourceProfile()),
          Set.copyOf(entity.targetProfile()),
          entity.symmetric(),
          entity.inverseIdentifier(),
          entity.inferencePolicy().composable());
    }
  }
}
