package com.persiqa.core;

import com.persiqa.model.Ckm.KnowledgeKind;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.Statement;
import java.util.Set;

/** In-memory CKM operations with deliberately declared, never generic, inference. */
public final class KnowledgeBase {
  public Statement deriveSupply(String id, Relation upstream, Relation downstream) {
    if (!upstream.type().id().equals("supplies") || !downstream.type().id().equals("supplies")) {
      throw new IllegalArgumentException("supply derivation requires supplies relations");
    }
    if (!upstream.target().equals(downstream.source())) {
      throw new IllegalArgumentException("supply path endpoints do not compose");
    }
    var result =
        new Relation(id + ":relation", upstream.type(), upstream.source(), downstream.target());
    return new Statement(
        id,
        KnowledgeKind.DERIVED,
        "supplies",
        result,
        result,
        Set.of(upstream.id(), downstream.id()));
  }
}
