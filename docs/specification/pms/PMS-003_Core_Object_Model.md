# PMS-003 --- Core Object Model

**Document:** Persiqa Meta Model Specification (PMS)

**Chapter:** PMS-003

**Title:** Core Object Model

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the normative Core Object Model of Persiqa.

The Core Object Model specifies the five first-class CKM object types from
which every Canonical Knowledge Model (CKM) is constructed.

The four Core ontology concepts and the five first-class CKM object types
are distinct classifications and SHALL NOT be conflated.

------------------------------------------------------------------------

# 2. Core Ontology

The Core ontology consists exclusively of:

- Entity
- Capability
- Relation
- State

Statement is not a Core ontology concept.

------------------------------------------------------------------------

# 3. First-Class CKM Objects

The CKM contains the following five first-class object types:

- Entity
- Capability
- Relation
- State
- Statement

No additional first-class CKM object types SHALL exist.

------------------------------------------------------------------------

# 4. Entity

An Entity represents an identifiable element of reality.

Mandatory characteristics:

- Canonical Identity
- Zero or more States
- Zero or more Relations
- Zero or more Capabilities
- Optional Refinement

An Entity SHALL NOT own Statements.

------------------------------------------------------------------------

# 5. Capability

A Capability represents a model-relevant ability or function.

Mandatory characteristics:

- Canonical Identity
- Explicit association with zero or more Entities
- Optional Refinement

Capabilities SHALL be independent of implementation technology.

Capabilities SHALL NOT own State. A Capability association SHALL NOT imply
that the ability is active, reachable, healthy, or in use.

------------------------------------------------------------------------

# 6. Relation

A Relation represents a directed semantic connection between two endpoints
permitted by its declared Relation Type.

Mandatory characteristics:

- Canonical Identity
- Exactly one Source endpoint
- Exactly one Target endpoint
- Declared Relation Type
- Optional State
- Optional Refinement

Relations are first-class CKM model elements.

Relations MAY participate in reasoning.

The Core Relation model is binary. A Relation SHALL NOT have multiple
source or target Entities.

------------------------------------------------------------------------

# 7. State

State represents mutable knowledge about an Entity or Relation.

State SHALL belong to exactly one owner:

- Entity
- Relation

State SHALL NOT exist independently.

State SHALL NOT define an independent continuity identity. Its identity is
contextual to owner, semantic predicate, and applicable context; a State value
change SHALL NOT by itself create Entity-like continuity.

------------------------------------------------------------------------

# 8. Statement

A Statement is the canonical assertion of knowledge.

A Statement SHALL contain:

- Subject
- Predicate
- Object

The Subject MAY identify an Entity, Capability, Relation, State, Statement,
concept, or other object permitted by the predicate semantics.

Statements reference Core objects.

Statements SHALL NOT own the objects they reference.

A Statement is a first-class CKM object, but it is not a Core ontology
concept.

------------------------------------------------------------------------

# 9. Object Relationships

```text
Entity
 ├── associates with Capability
 ├── owns State
 └── participates in Relation

Relation
 └── owns State

Statement
 ├── Subject
 ├── Predicate
 └── Object
```

Ownership SHALL always be explicit.

------------------------------------------------------------------------

# 10. Object Invariants

The following SHALL always hold:

- Every first-class object is independently addressable according to its
  kind-specific identity semantics.
- Every Capability association is explicit.
- Every State has exactly one owner.
- Every Statement is semantically valid.
- Every Relation has exactly one source and one target endpoint permitted by
  its Relation Type.
- Every refinement chain is acyclic.

------------------------------------------------------------------------

# 11. Conformance

A conforming implementation SHALL preserve the semantics, ownership rules
and invariants of the Core Object Model regardless of implementation
language or storage mechanism.

The Core Object Model defined in this chapter is normative for all Persiqa
implementations.
