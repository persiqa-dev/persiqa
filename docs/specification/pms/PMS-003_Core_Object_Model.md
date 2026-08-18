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

A Capability represents a universal behaviour or domain-level ability.

Mandatory characteristics:

- Canonical Identity
- Exactly one owning Entity
- Optional Refinement

Capabilities SHALL be independent of implementation technology.

Capabilities SHALL NOT own State.

------------------------------------------------------------------------

# 6. Relation

A Relation represents a directed semantic connection between two Entities.

Mandatory characteristics:

- Canonical Identity
- Exactly one Source Entity
- Exactly one Target Entity
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

State SHALL NOT define an independent continuity identity. Within its owner context, State has a canonical identity determined by its semantic predicate. Changing the State value SHALL NOT create a new canonical State object.

------------------------------------------------------------------------

# 8. Statement

A Statement is the canonical assertion of knowledge.

A Statement SHALL contain:

- Subject
- Predicate
- Object

The Subject SHALL identify an Entity or Relation.

Statements reference Core objects.

Statements SHALL NOT own the objects they reference.

A Statement is a first-class CKM object, but it is not a Core ontology
concept.

------------------------------------------------------------------------

# 9. Object Relationships

```text
Entity
 ├── owns Capability
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

- Every first-class object has exactly one canonical identity.
- Every Capability has exactly one owning Entity.
- Every State has exactly one owner.
- Every Statement is semantically valid.
- Every Relation has exactly one source Entity.
- Every Relation has exactly one target Entity.
- Every refinement chain is acyclic.

------------------------------------------------------------------------

# 11. Conformance

A conforming implementation SHALL preserve the semantics, ownership rules
and invariants of the Core Object Model regardless of implementation
language or storage mechanism.

The Core Object Model defined in this chapter is normative for all Persiqa
implementations.
