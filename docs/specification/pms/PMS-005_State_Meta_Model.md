# PMS-005 --- State Meta Model

**Document:** Persiqa Meta Model Specification (PMS)

**Chapter:** PMS-005

**Title:** State Meta Model

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the normative meta model for State within the
Persiqa Canonical Knowledge Model (CKM).

State represents a semantic condition associated with an Entity or Relation
in context while preserving the owner's identity.

------------------------------------------------------------------------

# 2. Definition

State is a first-class CKM object representing the semantic condition of an
owning model element.

State SHALL be mutable.

State SHALL NOT define an independent continuity identity. Its identity is
contextual to owner, semantic predicate, and applicable context; a State value
change SHALL NOT by itself create Entity-like continuity.

------------------------------------------------------------------------

# 3. Ownership

Every State SHALL have exactly one owner.

A valid owner SHALL be either:

-   Entity
-   Relation

State SHALL NOT exist independently.

Capability and Statement SHALL NOT own State.

------------------------------------------------------------------------

# 4. State Evolution

State MAY change over time. Current, historical, planned, asserted, observed,
and derived States MAY coexist when their contexts are distinguished.

A State change:

-   SHALL preserve the owner's identity,
-   SHALL preserve semantic consistency,
-   SHALL remain attributable to the same owner.

Changing State SHALL NOT create a new canonical element.

------------------------------------------------------------------------

# 5. Entity State

Entity State represents mutable knowledge about an Entity.

Examples:

-   Online
-   Running
-   Temperature
-   Occupancy
-   Fault

Entity State describes a contextual condition of the Entity, including current,
historical, planned, asserted, observed, or derived condition where declared.

------------------------------------------------------------------------

# 6. Relation State

Relation State represents mutable knowledge about a Relation.

Examples:

-   Link Speed
-   Latency
-   RSSI
-   Water Flow
-   Pressure
-   Packet Loss

Relation State describes a contextual condition of the connection rather than
either endpoint.

------------------------------------------------------------------------

# 7. Temporal Semantics

Current and historical State MAY coexist when explicitly represented.

Temporal information SHALL be modelled explicitly.

Reasoning SHALL NOT infer historical State implicitly.

------------------------------------------------------------------------

# 8. State During Reasoning

Reasoning MAY derive new State according to the PRS.

Derived State SHALL remain distinguishable from asserted State.

Reasoning SHALL NOT modify canonical identity.

------------------------------------------------------------------------

# 9. State Invariants

The following invariants SHALL always hold:

-   Every State has exactly one owner.
-   Every owner MAY possess zero or more States.
-   State is mutable.
-   Identity is immutable.
-   State SHALL NOT exist without its owner.

------------------------------------------------------------------------

# 10. Conformance

A conforming implementation SHALL preserve State ownership, mutability
and semantics across parsing, validation, reasoning, serialization and
interoperability.

Entity State and Relation State SHALL receive equivalent normative
treatment throughout the implementation.
