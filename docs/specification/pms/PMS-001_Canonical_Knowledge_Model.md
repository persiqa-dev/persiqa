# PMS-001 --- Canonical Knowledge Model

**Document:** Persiqa Meta Model Specification (PMS)

**Chapter:** PMS-001

**Title:** Canonical Knowledge Model

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the Canonical Knowledge Model (CKM), the normative
abstract representation of every Persiqa model.

The CKM is the authoritative semantic representation for parsing,
reasoning, validation and serialization.

------------------------------------------------------------------------

# 2. Definition

A Canonical Knowledge Model is a finite collection of canonical model
elements together with their semantic relationships.

The CKM SHALL be implementation-independent.

The CKM SHALL NOT depend on:

- programming language,
- storage technology,
- graph database,
- object model,
- serialization format.

------------------------------------------------------------------------

# 3. Core Ontology and First-Class CKM Objects

The Persiqa Core ontology consists of exactly four ontological concepts:

- Entity
- Capability
- Relation
- State

The CKM contains five first-class object types:

- Entity
- Capability
- Relation
- State
- Statement

Statement is a first-class CKM object because it is the canonical
representation of expressed knowledge.

Statement is deliberately not a fifth Core ontology concept.

The distinction is normative:

```text
Core Ontology
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
```

No additional first-class CKM object types SHALL exist.

Additional domain-specific semantics SHALL be introduced through refinement
and the applicable extension mechanisms; they SHALL NOT silently create new
Core ontology concepts.

------------------------------------------------------------------------

# 4. Model Structure

A CKM consists of:

```text
Canonical Knowledge Model
│
├── Entities
├── Capabilities
├── Relations
├── States
└── Statements
```

Statements reference first-class elements but do not own them.

------------------------------------------------------------------------

# 5. Canonical Identity

Every first-class element SHALL possess a canonical identity.

Canonical identity:

- uniquely identifies the element,
- remains stable during reasoning,
- is independent of source order,
- is independent of serialization.

Identity SHALL survive refinement.

------------------------------------------------------------------------

# 6. References

Relationships between elements SHALL be expressed through canonical
references.

References SHALL:

- resolve uniquely,
- preserve identity,
- remain valid after canonical normalization.

Broken references SHALL invalidate the CKM.

------------------------------------------------------------------------

# 7. Ownership

Ownership within the CKM SHALL be explicit.

Rules:

- Statements reference elements.
- Relations connect source and target Entities.
- States belong to exactly one owning Entity or Relation.
- Capabilities belong to their owning Entity.
- Refinement links preserve ownership.

Implicit ownership SHALL NOT exist.

------------------------------------------------------------------------

# 8. Model Boundary

The CKM represents only explicitly modelled knowledge.

Knowledge outside the model boundary SHALL NOT be assumed by conforming
implementations.

Reasoning MAY derive new Statements, but SHALL remain inside the CKM
boundary.

------------------------------------------------------------------------

# 9. Canonical Invariants

The following invariants SHALL always hold:

- Every first-class element has exactly one canonical identity.
- Every reference resolves uniquely.
- Every Statement is semantically valid.
- Every State has exactly one owner.
- Every Capability has exactly one Entity owner.
- Every Relation has exactly one source Entity.
- Every Relation has exactly one target Entity.
- Every refinement chain is acyclic.
- Every CKM is internally consistent after successful validation.

------------------------------------------------------------------------

# 10. Conformance

A conforming implementation SHALL expose or internally maintain a
representation that is semantically equivalent to the Canonical Knowledge
Model defined by this specification.

All parser, validation, reasoning and serialization behaviour SHALL ultimately
operate on this canonical model rather than on implementation-specific
structures.
