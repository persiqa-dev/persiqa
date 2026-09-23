package com.persiqa.model;

import java.math.BigDecimal;
import java.time.Instant;
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

  /**
   * A contextual State owned by an Entity or Relation.
   *
   * <p>The identifier is an implementation address, not an Entity-like continuity identity. A
   * State is identified in context by its owner and predicate; its scalar value may change while
   * that contextual identity remains stable.
   */
  public record State(String id, String predicate, Object value) implements Node {
    /**
     * Creates a legacy value-only State.
     *
     * <p>New callers SHOULD provide a semantic predicate and a typed value explicitly.
     */
    public State(String id) {
      this(id, "state", id);
    }

    /** Validates the scalar State value independently from any storage representation. */
    public State {
      if (predicate == null || predicate.isBlank()) {
        throw new IllegalArgumentException("state predicate must not be blank");
      }
      if (value != null
          && !(value instanceof String)
          && !(value instanceof Boolean)
          && !(value instanceof Number)) {
        throw new IllegalArgumentException("state value must be String, Number, or Boolean");
      }
    }

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

  /** Provenance, confidence, and temporal/scenario context carried by a Statement. */
  public record Context(
      String provenance,
      BigDecimal confidence,
      Instant observedAt,
      Instant validFrom,
      Instant validTo,
      String scenario) {
    /**
     * Creates context for the common case where the final value identifies a scenario.
     *
     * @param provenance the source of the assertion
     * @param confidence the optional confidence in the range zero through one
     * @param scenario the scenario or legacy validity-context key
     */
    public Context(String provenance, BigDecimal confidence, String scenario) {
      this(provenance, confidence, null, null, null, scenario);
    }

    /** Validates context invariants independently from any persistence representation. */
    public Context {
      if (confidence != null
          && (confidence.compareTo(BigDecimal.ZERO) < 0
              || confidence.compareTo(BigDecimal.ONE) > 0)) {
        throw new IllegalArgumentException("confidence must be between zero and one");
      }
      if (validFrom != null && validTo != null && validFrom.isAfter(validTo)) {
        throw new IllegalArgumentException("validFrom must not be after validTo");
      }
    }

    /**
     * Returns the legacy context key used by the initial in-memory conformance suite.
     *
     * @deprecated use {@link #scenario()} or the explicit validity interval instead
     */
    @Deprecated(forRemoval = false)
    public String validAt() {
      return scenario;
    }

    /** Returns context with no asserted qualifiers. */
    public static Context unspecified() {
      return new Context(null, null, null, null, null, null);
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
