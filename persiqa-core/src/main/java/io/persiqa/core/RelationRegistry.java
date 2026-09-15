package io.persiqa.core;

import io.persiqa.model.Ckm.Kind;
import io.persiqa.model.Ckm.Node;
import io.persiqa.model.Ckm.Relation;
import io.persiqa.model.Ckm.RelationType;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class RelationRegistry {
  private final Map<String, RelationType> types = new HashMap<>();

  public RelationRegistry() {
    register(
        new RelationType(
            "instanceOf", Set.of(Kind.ENTITY), Set.of(Kind.CONCEPT), false, "none", false));
    register(
        new RelationType(
            "classifiedAs",
            Set.of(Kind.ENTITY, Kind.CONCEPT),
            Set.of(Kind.CONCEPT),
            false,
            "none",
            false));
    register(
        new RelationType(
            "playsRole", Set.of(Kind.ENTITY), Set.of(Kind.CONCEPT), false, "none", false));
    register(
        new RelationType(
            "hasCapability", Set.of(Kind.ENTITY), Set.of(Kind.CAPABILITY), false, "none", false));
    register(
        new RelationType(
            "hasState",
            Set.of(Kind.ENTITY, Kind.RELATION),
            Set.of(Kind.STATE),
            false,
            "none",
            false));
    register(
        new RelationType(
            "contains", Set.of(Kind.ENTITY), Set.of(Kind.ENTITY), false, "partOf", false));
    register(
        new RelationType(
            "connectedTo", Set.of(Kind.ENTITY), Set.of(Kind.ENTITY), true, "connectedTo", false));
    register(
        new RelationType(
            "supplies", Set.of(Kind.ENTITY), Set.of(Kind.ENTITY), false, "none", true));
    register(
        new RelationType(
            "dependsOn", Set.of(Kind.ENTITY), Set.of(Kind.ENTITY), false, "none", false));
    register(
        new RelationType(
            "hostedOn", Set.of(Kind.ENTITY), Set.of(Kind.ENTITY), false, "hosts", false));
  }

  public void register(RelationType t) {
    types.put(t.id(), t);
  }

  public Relation create(String id, String type, Node s, Node t) {
    var r =
        Optional.ofNullable(types.get(type))
            .orElseThrow(() -> new IllegalArgumentException("unknown relation type: " + type));
    if (!r.sources().contains(s.kind()) || !r.targets().contains(t.kind())) {
      throw new IllegalArgumentException("invalid endpoints for " + type);
    }
    return new Relation(id, r, s, t);
  }
}
