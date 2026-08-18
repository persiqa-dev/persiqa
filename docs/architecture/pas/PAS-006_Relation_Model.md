# PAS-006 — Relation Model

**Document:** Persiqa Architecture Specification (PAS)  
**Chapter:** PAS-006  
**Title:** Relation Model  
**Status:** Accepted

---

# 1. Purpose

This chapter defines the normative architectural model for Relations in
Persiqa.

A Relation expresses a semantic connection between two Entities.

Relations are first-class concepts of the Persiqa Core ontology.

---

# 2. Definition

A Relation represents a directed semantic connection from a source Entity to a
target Entity.

The fundamental structure is:

```text
Relation
    source → Entity
    target → Entity
```

A Relation SHALL therefore have exactly one source Entity and exactly one
target Entity.

This binary representation is normative.

---

# 3. Direction

Relations are directed.

The direction of a Relation is determined by its source and target.

For example:

```text
Server
    runs
Application
```

is distinct from:

```text
Application
    runs
Server
```

A Relation predicate SHALL define the semantic meaning of the directed
connection.

If a domain requires bidirectional semantics, the model MAY represent the
relationship using two explicitly defined Relations or another valid
higher-level domain construction.

---

# 4. First-Class Status

Relations SHALL be first-class model elements.

A Relation SHALL have an identity within the Canonical Knowledge Model and MAY
be referenced by other model elements.

A Relation SHALL NOT be treated merely as an implementation-level edge or
database foreign key.

This allows a Relation to carry its own semantics and State.

---

# 5. Relation State

A Relation MAY own State.

Examples include:

```text
NetworkLink
    bandwidth = 1 Gbps

WirelessLink
    signal_strength = -58 dBm

PipeConnection
    flow_rate = 18 l/min
```

Relation State describes the condition of the relationship itself and SHALL
NOT be interpreted as the State of either endpoint Entity.

---

# 6. Relation Semantics

The semantic meaning of a Relation is determined by its relation predicate.

Examples include:

- `connected_to`
- `composed_of`
- `located_in`
- `mounted_on`
- `runs_on`
- `supplies`
- `monitored_by`

These are semantic Relations, not additional Core ontology concepts.

The Core recognizes only the concept of Relation; domain-specific Relation
predicates MAY be introduced through refinement.

---

# 7. Relation Identity

A Relation SHALL be independently identifiable within the Canonical
Knowledge Model.

Its identity SHALL distinguish the Relation from its source and target
Entities.

Two Relations connecting the same pair of Entities MAY coexist when their
semantics differ.

For example:

```text
Server
    connects_to
Switch

Server
    monitored_by
MonitoringSystem
```

The existence of a common endpoint SHALL NOT collapse semantically distinct
Relations.

---

# 8. Refinement

Relations MAY be refined.

A refined Relation SHALL preserve the semantics of its parent Relation while
adding domain-specific precision.

For example:

```text
connected_to
    ↓
network_connected_to
    ↓
ethernet_connected_to
```

Refinement SHALL NOT change the fundamental binary source/target structure of
a Relation.

---

# 9. Relation Invariants

**REL-001**

Every Relation SHALL have exactly one source Entity.

**REL-002**

Every Relation SHALL have exactly one target Entity.

**REL-003**

The source and target SHALL be Entities.

**REL-004**

A Relation SHALL have explicit semantic meaning.

**REL-005**

A Relation MAY own State.

**REL-006**

A Relation SHALL remain independently identifiable.

**REL-007**

Refinement SHALL preserve the source/target structure.

**REL-008**

A Relation SHALL NOT be interpreted as an Entity.

**REL-009**

A Relation SHALL NOT be interpreted as an implicit n-ary relationship.

---

# 10. N-ary Relationships

The Core Relation model is intentionally binary.

A domain that appears to require an n-ary relationship SHOULD first be
evaluated for decomposition into Entities and binary Relations.

For example, a relationship involving multiple participants MAY be modeled by
introducing an Entity representing the relationship context and connecting
that Entity to the participants using explicit Relations.

This preserves the universal binary Relation model while allowing complex
domain structures to be represented.

An n-ary relation construct SHALL NOT be introduced into the Core solely for
the convenience of a single domain.

---

# 11. Canonical Representation

The Canonical Knowledge Model SHALL preserve:

- Relation identity,
- source Entity,
- target Entity,
- semantic meaning,
- optional Relation State,
- refinement information.

Serialization formats MAY represent these elements differently, but the
resulting Canonical Knowledge Model SHALL remain semantically equivalent.

---

# 12. Implementation Independence

The Relation model SHALL remain independent of:

- graph databases,
- relational databases,
- object references,
- API resources,
- network links,
- UI edges,
- serialization-specific identifiers.

Implementations MAY use any internal representation that preserves the
normative semantics defined in this chapter.

---

# 13. Rationale

A binary Relation provides a small and deterministic primitive that is
sufficient to represent arbitrary graph structures when combined with
Entities.

Making Relations first-class preserves semantic information that would be
lost if relationships were treated only as implementation-level links.

The binary model also provides a stable foundation for reasoning, canonical
serialization, refinement, and interoperability.

Complex relationships remain representable without expanding the universal
Core ontology.

---

# 14. Conformance

An implementation conforms to the Persiqa Relation Model only if its
Canonical Knowledge Model preserves the normative Relation invariants defined
in this chapter.

In particular, an implementation SHALL NOT represent a Core Relation as an
implicit n-ary relationship or omit its explicit source or target Entity.


### Canonical Relation Identity

A Relation SHALL have a canonical identity determined by its relation type, source Entity, and target Entity. The canonical identity is the tuple (relation type, source Entity, target Entity) and SHALL distinguish the Relation from other Relations.
