package com.persiqa.model;

import java.util.Objects;
import java.util.Set;

/** Minimal persistence-independent CKM vocabulary; see PMS-008 and PMS-009. */
public final class Ckm {
  private Ckm() {}

  /** CKM object kinds that have independent canonical addressing. */
  public enum Kind {
    ENTITY,
    CAPABILITY,
    RELATION,
    STATE,
    STATEMENT,
    CONCEPT,
    TYPED_VALUE
  }

  /** An addressable CKM object. */
  public interface Node {
    String id();

    Kind kind();
  }

  /** An entity with continuity identity. */
  public record Entity(String id) implements Node {
    /** Returns the Entity object kind. */
    public Kind kind() {
      return Kind.ENTITY;
    }
  }

  /** A reusable capability concept or instance. */
  public record Capability(String id) implements Node {
    public Kind kind() {
      return Kind.CAPABILITY;
    }
  }

  /** A contextual state object. */
  public record State(String id) implements Node {
    public Kind kind() {
      return Kind.STATE;
    }
  }

  /** A concept used by semantic relation types. */
  public record Concept(String id) implements Node {
    public Kind kind() {
      return Kind.CONCEPT;
    }
  }

  /** The contract defining valid Relation endpoints and semantics. */
  public record RelationType(
      String id,
      Set<Kind> sources,
      Set<Kind> targets,
      boolean symmetric,
      String inverse,
      boolean composable) {}

  /** A canonical semantic association with independent identity. */
  public record Relation(String id, RelationType type, Node source, Node target) implements Node {
    public Kind kind() {
      return Kind.RELATION;
    }
  }

  /** Whether a Statement was asserted or inferred. */
  public enum KnowledgeKind {
    EXPLICIT,
    DERIVED
  }

  /** Provenance and temporal context carried by a Statement. */
  public record Context(String provenance, Double confidence, String validAt) {
    /** Returns context with no asserted qualifiers. */
    public static Context unspecified() {
      return new Context(null, null, null);
    }
  }

  /** A first-class explicit or derived knowledge assertion. */
  public record Statement(
      String id,
      KnowledgeKind knowledgeKind,
      String predicate,
      Node subject,
      Object object,
      Set<String> derivedFrom,
      Context context)
      implements Node {
    /** Validates the derived Statement provenance requirement. */
    public Statement {
      derivedFrom = Set.copyOf(derivedFrom);
      context = Objects.requireNonNullElseGet(context, Context::unspecified);
      if (knowledgeKind == KnowledgeKind.DERIVED && derivedFrom.isEmpty()) {
        throw new IllegalArgumentException("derived statement needs provenance");
      }
    }

    /** Creates a Statement with unspecified context. */
    public Statement(
        String id,
        KnowledgeKind kind,
        String predicate,
        Node subject,
        Object object,
        Set<String> derivedFrom) {
      this(id, kind, predicate, subject, object, derivedFrom, Context.unspecified());
    }

    public Kind kind() {
      return Kind.STATEMENT;
    }
  }

  /** Creates a directed Relation Type with one source and target kind. */
  public static RelationType type(String id, Kind source, Kind target) {
    return new RelationType(id, Set.of(source), Set.of(target), false, "none", false);
  }
}
