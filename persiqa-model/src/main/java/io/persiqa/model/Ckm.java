package io.persiqa.model;

import java.util.*;

/** Minimal persistence-independent CKM vocabulary; see PMS-008 and PMS-009. */
public final class Ckm {
  private Ckm() {}
  public enum Kind { ENTITY, CAPABILITY, RELATION, STATE, STATEMENT, CONCEPT, TYPED_VALUE }
  public interface Node { String id(); Kind kind(); }
  public record Entity(String id) implements Node { public Kind kind(){return Kind.ENTITY;} }
  public record Capability(String id) implements Node { public Kind kind(){return Kind.CAPABILITY;} }
  public record State(String id) implements Node { public Kind kind(){return Kind.STATE;} }
  public record Concept(String id) implements Node { public Kind kind(){return Kind.CONCEPT;} }
  public record RelationType(String id, Set<Kind> sources, Set<Kind> targets, boolean symmetric, String inverse, boolean composable) {}
  public record Relation(String id, RelationType type, Node source, Node target) implements Node { public Kind kind(){return Kind.RELATION;} }
  public enum KnowledgeKind { EXPLICIT, DERIVED }
  public record Context(String provenance, Double confidence, String validAt) { public static Context unspecified(){return new Context(null,null,null);} }
  public record Statement(String id, KnowledgeKind knowledgeKind, String predicate, Node subject, Object object, Set<String> derivedFrom, Context context) implements Node {
    public Statement { derivedFrom=Set.copyOf(derivedFrom); context=Objects.requireNonNullElseGet(context,Context::unspecified); if(knowledgeKind==KnowledgeKind.DERIVED && derivedFrom.isEmpty()) throw new IllegalArgumentException("derived statement needs provenance"); }
    public Statement(String id, KnowledgeKind kind, String predicate, Node subject, Object object, Set<String> derivedFrom){this(id,kind,predicate,subject,object,derivedFrom,Context.unspecified());}
    public Kind kind(){return Kind.STATEMENT;}
  }
  public static RelationType type(String id, Kind source, Kind target) { return new RelationType(id, Set.of(source), Set.of(target), false, "none", false); }
}
