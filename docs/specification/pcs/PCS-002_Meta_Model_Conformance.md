# PCS-002 --- Meta Model Conformance

**Document:** Persiqa Conformance Specification (PCS)

**Chapter:** PCS-002

**Title:** Meta Model Conformance

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the normative conformance requirements for
implementations of the Persiqa Meta Model.

A conforming implementation SHALL preserve the semantics of the
Canonical Knowledge Model (CKM) defined by the PMS.

------------------------------------------------------------------------

# 2. Scope

Meta Model Conformance includes:

- Canonical Knowledge Model structure,
- canonical identity,
- Core Object Model,
- ownership rules,
- refinement semantics,
- model invariants.

Parser behaviour and reasoning behaviour are outside the scope of this
chapter.

------------------------------------------------------------------------

# 3. Mandatory Capabilities

A conforming implementation SHALL:

- represent all five first-class CKM object types,
- preserve canonical identity,
- preserve ownership,
- preserve canonical references,
- preserve refinement semantics,
- enforce CKM invariants.

The five first-class CKM object types are:

- Entity
- Capability
- Relation
- State
- Statement

These five object types SHALL NOT be described as five Core ontology
concepts. The Core ontology consists of four concepts: Entity, Capability,
Relation, and State.

------------------------------------------------------------------------

# 4. Canonical Identity

The implementation SHALL demonstrate that:

- every first-class element has exactly one canonical identity,
- identity is immutable,
- identity survives serialization, validation and reasoning,
- identity is independent of persistence technology.

------------------------------------------------------------------------

# 5. Ownership and References

The implementation SHALL verify:

- every Capability has exactly one Entity owner,
- every State has exactly one owner,
- Statements reference but do not own Core objects,
- every Relation has exactly one source Entity,
- every Relation has exactly one target Entity,
- references resolve uniquely,
- no dangling references exist in a valid CKM.

------------------------------------------------------------------------

# 6. Refinement Conformance

The implementation SHALL enforce:

- acyclic refinement hierarchies,
- exactly one immediate parent,
- semantic compatibility,
- identity preservation across refinement.

------------------------------------------------------------------------

# 7. Model Invariants

The implementation SHALL satisfy every invariant defined by the PMS,
including:

- identity invariants,
- ownership invariants,
- reference invariants,
- refinement invariants,
- statement invariants,
- state invariants,
- Relation endpoint invariants.

------------------------------------------------------------------------

# 8. Normative Test Categories

A conforming implementation SHALL pass tests covering:

- CKM construction,
- identity preservation,
- ownership validation,
- Relation endpoint validation,
- refinement validation,
- invariant enforcement,
- persistence independence.

------------------------------------------------------------------------

# 9. Certification Criteria

Meta Model Conformance is achieved when every mandatory PMS requirement
is satisfied and the corresponding conformance tests pass successfully.

Unsupported normative features SHALL be explicitly declared.

------------------------------------------------------------------------

# 10. Conformance

An implementation MAY claim "Persiqa Meta Model Conformant" only if it
satisfies this chapter together with the referenced PMS specifications.

Equivalent internal implementations SHALL expose semantically equivalent
Canonical Knowledge Models.
