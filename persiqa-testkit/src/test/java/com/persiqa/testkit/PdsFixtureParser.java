package com.persiqa.testkit;

import com.persiqa.core.CanonicalKnowledgeModel;
import com.persiqa.core.RelationRegistry;
import com.persiqa.model.Ckm.Entity;

/** Deliberately small test fixture reader; it is not a production PDS parser. */
final class PdsFixtureParser {
  private final RelationRegistry registry = new RelationRegistry();

  CanonicalKnowledgeModel parse(String text) {
    var model = new CanonicalKnowledgeModel();
    var entities = new java.util.HashMap<String, Entity>();
    int relation = 0;
    for (var raw : text.lines().toList()) {
      var line = raw.trim();
      if (line.isEmpty() || line.startsWith("#")) {
        continue;
      }
      var parts = line.split("\\s+");
      if (parts.length == 2 && parts[0].equals("entity")) {
        var entity = new Entity(parts[1]);
        entities.put(parts[1], entity);
        model.add(entity);
      } else if (parts.length == 3) {
        var source = entities.computeIfAbsent(parts[0], Entity::new);
        var target = entities.computeIfAbsent(parts[2], Entity::new);
        model.add(registry.create("r" + (++relation), parts[1], source, target));
      } else {
        throw new IllegalArgumentException("unsupported fixture line: " + line);
      }
    }
    return model;
  }
}
