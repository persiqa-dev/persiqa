package io.persiqa.core;

import io.persiqa.model.Ckm.Entity;

/** Deliberately small fixture profile parser; full PDS remains specified separately. */
public final class PdsFixtureParser {
  private final RelationRegistry registry = new RelationRegistry();

  public CanonicalKnowledgeModel parse(String text) {
    var model = new CanonicalKnowledgeModel();
    var entities = new java.util.HashMap<String, Entity>();
    int relation = 0;
    for (var raw : text.lines().toList()) {
      var line = raw.trim();
      if (line.isEmpty() || line.startsWith("#")) {
        continue;
      }
      var p = line.split("\\s+");
      if (p.length == 2 && p[0].equals("entity")) {
        var e = new Entity(p[1]);
        entities.put(p[1], e);
        model.add(e);
      } else if (p.length == 3) {
        var source = entities.computeIfAbsent(p[0], Entity::new);
        var target = entities.computeIfAbsent(p[2], Entity::new);
        model.add(registry.create("r" + (++relation), p[1], source, target));
      } else {
        throw new IllegalArgumentException("unsupported fixture line: " + line);
      }
    }
    return model;
  }
}
