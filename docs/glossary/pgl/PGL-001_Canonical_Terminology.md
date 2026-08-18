# PGL-001 --- Canonical Terminology

**Document:** Persiqa Glossary (PGL)

**Chapter:** PGL-001

**Title:** Canonical Terminology

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the canonical terminology used throughout the Persiqa
specifications.

Each term has exactly one canonical terminology definition. Detailed
semantics remain authoritative in the applicable normative specification.

------------------------------------------------------------------------

# 2. Canonical Knowledge Model (CKM)

**Definition**

The implementation-independent semantic representation of a Persiqa model.

**Related Terms**

Entity, Statement, Reasoning

**Normative Source**

PMS

------------------------------------------------------------------------

# 3. Entity

**Definition**

A first-class CKM object representing an identifiable element of reality.

Entity is one of the four Core ontology concepts.

**Related Terms**

Capability, Relation, State

**Normative Source**

PAS, PMS

------------------------------------------------------------------------

# 4. Capability

**Definition**

A first-class CKM object representing universal behaviour or a domain-level
ability that an Entity may exhibit.

Capability is one of the four Core ontology concepts.

**Related Terms**

Entity, Refinement

**Normative Source**

PAS, PMS

------------------------------------------------------------------------

# 5. Relation

**Definition**

A first-class CKM object representing a directed semantic connection between
exactly one source Entity and exactly one target Entity.

Relation is one of the four Core ontology concepts.

Relations may own State.

**Related Terms**

Entity, State, Statement

**Normative Source**

PAS, PMS

------------------------------------------------------------------------

# 6. State

**Definition**

A first-class CKM object representing mutable knowledge owned by exactly one
Entity or Relation.

State is one of the four Core ontology concepts.

State does not define an independent continuity identity. Within its owner context, State has a canonical identity determined by its semantic predicate.

**Related Terms**

Identity, Relation

**Normative Source**

PAS, PMS

------------------------------------------------------------------------

# 7. Statement

**Definition**

The canonical unit of knowledge expressing a semantic fact.

Statement is a first-class CKM object, but it is not a Core ontology concept.

**Related Terms**

Fact, Derived Fact

**Normative Source**

PAS, PDS, PMS

------------------------------------------------------------------------

# 8. Refinement

**Definition**

A mechanism for increasing semantic precision while preserving the applicable
meaning and constraints of the parent concept.

Refinement is not a Core ontology concept or a first-class CKM object type.

**Related Terms**

Replacement, Incremental Truth

**Normative Source**

PAS, PMS

------------------------------------------------------------------------

# 9. Replacement

**Definition**

An explicit semantic operation that supersedes previously asserted knowledge
by introducing a new canonical element or fact.

Replacement is distinct from Refinement.

**Normative Source**

PRS

------------------------------------------------------------------------

# 10. Incremental Truth

**Definition**

The architectural principle that additional knowledge refines or extends
existing truth unless explicit replacement occurs.

**Sources**

RAS, PRS

------------------------------------------------------------------------

# 11. Canonical Identity

**Definition**

The immutable identity of a first-class CKM object within the CKM.

Identity survives reasoning, refinement and serialization.

**Normative Source**

PMS

------------------------------------------------------------------------

# 12. Knowledge Closure

**Definition**

The stable reasoning state in which no additional Derived Facts can be
produced by the active rule set.

**Normative Source**

PRS

------------------------------------------------------------------------

# 13. Derived Fact

**Definition**

A Statement produced by applying normative inference rules to existing Facts.

**Normative Source**

PRS

------------------------------------------------------------------------

# 14. Semantic Equivalence

**Definition**

The property whereby two representations express the same Canonical
Knowledge Model regardless of syntax, formatting or implementation.

**Normative Source**

PDS, PMS

------------------------------------------------------------------------

# 15. Core Ontology Concept

**Definition**

One of the four ontological concepts that constitute the universal Persiqa
Core:

- Entity
- Capability
- Relation
- State

A Core ontology concept defines a category in the universal domain model.

------------------------------------------------------------------------

# 16. First-Class CKM Object

**Definition**

An object explicitly represented in the CKM with canonical identity and
semantics.

The CKM contains five first-class object types:

- Entity
- Capability
- Relation
- State
- Statement

The term **first-class CKM object** SHALL NOT be used interchangeably with
**Core ontology concept**.

Statement is a first-class CKM object but is not a Core ontology concept.

**Normative Source**

PMS

------------------------------------------------------------------------

# 17. First-Class Relation

**Definition**

A Relation treated as a canonical CKM object with its own identity,
source, target, refinement and optional State rather than as a simple
implementation reference.

**Normative Source**

PAS, PMS

------------------------------------------------------------------------

# 18. Deprecated Terminology

### Relationship

**Status:** Deprecated

`Relationship` is historical terminology for the current Core concept
**Relation**.

Current Persiqa documents SHALL use **Relation**.

Historical documents MAY retain `Relationship` when preserving the historical
record.

### Projection

**Status:** Deprecated as a generic Core term

`Projection` is historical terminology for a derived representation of the
knowledge model.

Current documents SHOULD use the more specific term **View**, **Zoom**, or
another appropriate derived-representation term.

Projection SHALL NOT be treated as a Core ontology concept or a first-class
CKM object type.

------------------------------------------------------------------------

# 19. Terminology Classification

The canonical classification is:

```text
Core Ontology Concepts
├── Entity
├── Capability
├── Relation
└── State

First-Class CKM Objects
├── Entity
├── Capability
├── Relation
├── State
└── Statement

Modeling / Semantic Constructs
└── Refinement

Derived / Presentation Constructs
├── View
└── Zoom
```

This classification is terminology guidance and does not replace the detailed
definitions in PAS, PMS, PDS, PRS or PCS.


### State Identity

State SHALL NOT define an independent continuity identity. Within its owner context, State has a canonical identity determined by its semantic predicate. Changing the State value SHALL NOT create a new canonical State object.
