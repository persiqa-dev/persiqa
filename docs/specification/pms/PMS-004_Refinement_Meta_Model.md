# PMS-004 --- Refinement Meta Model

**Document:** Persiqa Meta Model Specification (PMS)

**Chapter:** PMS-004

**Title:** Refinement Meta Model

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the normative meta model for Refinement within the
Persiqa Core.

Refinement is the semantic mechanism by which domain-specific concepts
specialize existing universal Core concepts.

------------------------------------------------------------------------

# 2. Definition

Refinement is a semantic relationship between two canonical elements.

The refined element provides a more precise interpretation while
preserving the meaning of its parent.

Refinement SHALL increase precision without changing identity.

------------------------------------------------------------------------

# 3. Applicable Elements

The following first-class elements MAY participate in refinement:

- Entity
- Capability
- Relation

State SHALL NOT participate in refinement directly.

Statements reference refined elements but are not themselves refined.

------------------------------------------------------------------------

# 4. Refinement Hierarchy

A refinement hierarchy forms a directed acyclic graph.

Each refinement SHALL reference exactly one immediate parent.

Multiple children MAY refine the same parent.

------------------------------------------------------------------------

# 5. Semantic Inheritance

A refined element inherits the semantic meaning of its parent.

The child MAY introduce additional precision.

The child SHALL NOT invalidate inherited semantics unless an explicit
replacement rule exists outside the refinement model.

------------------------------------------------------------------------

# 6. Identity Preservation

Refinement SHALL preserve canonical identity.

Refinement SHALL NOT:

- create a new semantic category,
- redefine Core concepts,
- modify inherited meaning.

Only semantic precision SHALL increase.

------------------------------------------------------------------------

# 7. Hierarchy Invariants

The following invariants SHALL always hold:

- No refinement cycles.
- Every parent exists.
- Every refinement chain terminates at a Core concept.
- Identity is preserved across refinement.
- Semantic compatibility is maintained.

Violation of any invariant SHALL invalidate the Canonical Knowledge
Model.

------------------------------------------------------------------------

# 8. Reasoning Behaviour

Reasoning engines SHALL interpret refinement consistently.

Knowledge asserted for a parent MAY be applicable to its refinements
according to the reasoning rules defined by the PRS.

Reasoning SHALL distinguish between inherited and explicitly asserted
knowledge.

------------------------------------------------------------------------

# 9. DSL Mapping

The DSL SHALL provide a canonical representation of refinement.

Every valid refinement declaration SHALL map to exactly one refinement
relationship in the Canonical Knowledge Model.

No implementation-specific refinement semantics SHALL be introduced.

------------------------------------------------------------------------

# 10. Conformance

A conforming implementation SHALL preserve refinement semantics across
parsing, validation, reasoning, serialization and interoperability.

Equivalent refinement hierarchies SHALL produce semantically equivalent
Canonical Knowledge Models regardless of implementation technology.
