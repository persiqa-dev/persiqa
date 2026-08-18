# PMS-006 --- Model Invariants

**Document:** Persiqa Meta Model Specification (PMS)

**Chapter:** PMS-006

**Title:** Model Invariants

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the global invariants that SHALL hold for every
valid Persiqa Canonical Knowledge Model (CKM).

These invariants are normative and apply regardless of implementation
language or storage technology.

------------------------------------------------------------------------

# 2. Global Invariants

Every valid CKM SHALL satisfy all invariants defined in this chapter.

Violation of any SHALL invalidate the model.

------------------------------------------------------------------------

# 3. Identity Invariants

The following SHALL always hold:

-   Every first-class element has exactly one canonical identity.
-   Canonical identity is immutable.
-   No two first-class elements share the same canonical identity.

------------------------------------------------------------------------

# 4. Reference Invariants

Every canonical reference SHALL:

-   resolve to exactly one canonical element,
-   remain valid after normalization,
-   preserve canonical identity.

Dangling or ambiguous references SHALL invalidate the CKM.

------------------------------------------------------------------------

# 5. Ownership Invariants

Ownership SHALL always be explicit.

The following rules apply:

-   Every State has exactly one owner.
-   Statements own no Core objects.
-   Relations connect model elements but do not own them.
-   Ownership SHALL NOT be inferred implicitly.

------------------------------------------------------------------------

# 6. Refinement Invariants

Refinement hierarchies SHALL satisfy:

-   acyclic structure,
-   exactly one immediate parent,
-   preserved identity,
-   preserved semantic compatibility,
-   termination at a Core concept.

------------------------------------------------------------------------

# 7. Statement Invariants

Every Statement SHALL:

-   have exactly one Subject,
-   have exactly one Predicate,
-   have exactly one Object,
-   reference only valid canonical elements,
-   remain semantically valid.

------------------------------------------------------------------------

# 8. State Invariants

State SHALL satisfy:

-   exactly one owner,
-   mutable semantics,
-   immutable ownership identity,
-   explicit temporal interpretation when required.

State SHALL NOT exist independently.

------------------------------------------------------------------------

# 9. Consistency Invariants

After successful validation and reasoning:

-   the CKM SHALL be internally consistent,
-   all references SHALL resolve,
-   no unresolved contradictions SHALL remain,
-   knowledge closure SHALL satisfy the active reasoning rules.

------------------------------------------------------------------------

# 10. Conformance

A conforming implementation SHALL enforce these invariants during
parsing, validation, reasoning and serialization.

Equivalent CKMs SHALL satisfy the same invariant set regardless of
implementation technology.
