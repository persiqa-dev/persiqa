# PMS-002 --- Identity and Lifecycle

**Document:** Persiqa Meta Model Specification (PMS)

**Chapter:** PMS-002

**Title:** Identity and Lifecycle

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the normative concepts of Identity and Lifecycle
for all first-class elements within the Canonical Knowledge Model (CKM).

Identity determines *what an element is*.

Lifecycle determines *how that element evolves over time*.

------------------------------------------------------------------------

# 2. Identity

Every first-class element SHALL possess exactly one canonical identity.

Identity SHALL:

-   uniquely distinguish an element,
-   remain stable throughout its lifetime,
-   be independent of serialization,
-   be independent of implementation technology,
-   survive refinement.

Identity SHALL NOT be derived from State.

------------------------------------------------------------------------

# 3. Immutable Identity

Canonical identity is immutable.

The following SHALL NOT change the identity of an element:

-   refinement,
-   state changes,
-   reasoning,
-   serialization,
-   canonical normalization.

If identity changes, a new element SHALL exist.

------------------------------------------------------------------------

# 4. Lifecycle

Every first-class element progresses through a conceptual lifecycle.

``` text
Created
    ↓
Validated
    ↓
Referenced
    ↓
Reasoned
    ↓
Archived or Removed
```

Implementations MAY use different internal lifecycle states provided the
observable semantics remain equivalent.

------------------------------------------------------------------------

# 5. State Evolution

State MAY change during the lifecycle.

State evolution:

-   SHALL preserve identity,
-   SHALL preserve semantic meaning,
-   SHALL remain traceable.

State transitions SHALL NOT create new identities.

------------------------------------------------------------------------

# 6. Refinement

Refinement increases semantic precision.

Refinement SHALL:

-   preserve identity,
-   preserve previously valid knowledge,
-   maintain semantic continuity.

A refinement SHALL NOT terminate the lifecycle of the refined element.

------------------------------------------------------------------------

# 7. Replacement

Replacement differs from refinement.

Replacement introduces a new canonical element.

The replaced element MAY remain for historical traceability but SHALL
retain its original identity.

Replacement SHALL be explicit.

------------------------------------------------------------------------

# 8. Traceability

Implementations SHOULD preserve lifecycle provenance.

At minimum, it SHALL be possible to distinguish:

-   original Facts,
-   derived Facts,
-   refined elements,
-   replaced elements.

------------------------------------------------------------------------

# 9. Lifecycle Invariants

The following invariants SHALL always hold:

-   Identity is immutable.
-   State is mutable.
-   Refinement preserves identity.
-   Replacement creates a new identity.
-   Reasoning SHALL NOT modify identity.

------------------------------------------------------------------------

# 10. Conformance

A conforming implementation SHALL preserve canonical identity across
parsing, reasoning, validation, serialization and refinement.

Lifecycle management MAY differ internally, but SHALL remain
semantically equivalent to this specification.
