# PMS-000 --- Meta Model Introduction

**Document:** Persiqa Meta Model Specification (PMS)

**Chapter:** PMS-000

**Title:** Introduction

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

The Persiqa Meta Model Specification defines the canonical abstract
object model underlying every Persiqa implementation.

It specifies what a Canonical Knowledge Model (CKM) is, independently of
any programming language, storage technology or runtime.

------------------------------------------------------------------------

# 2. Position Within the Documentation

  Family   Purpose
  -------- --------------------------
  PAS      Architecture
  RAS      Architectural rationale
  PDR      Design discovery
  PEX      Example collection
  REF      Reference models
  PDS      DSL specification
  PRS      Reasoning specification
  PMS      Meta model specification

------------------------------------------------------------------------

# 3. Scope

The PMS specifies:

-   the Canonical Knowledge Model,
-   core object definitions,
-   identity,
-   lifecycle,
-   refinement semantics,
-   state ownership,
-   model invariants,
-   implementation independence.

It does not prescribe class hierarchies, APIs or persistence mechanisms.

------------------------------------------------------------------------

# 4. Design Principles

The meta model SHALL be:

-   implementation-independent,
-   deterministic,
-   minimal,
-   extensible through refinement,
-   stable across language implementations.

------------------------------------------------------------------------

# 5. Relationship to Other Specifications

The PMS provides the abstract model upon which:

-   the PDS maps DSL documents,
-   the PRS performs reasoning,
-   the PAS defines architectural concepts.

All normative semantics are expressed through the Canonical Knowledge
Model.

------------------------------------------------------------------------

# 6. Normative Requirement

Every conforming Persiqa implementation SHALL expose an internal
representation that is semantically equivalent to the Canonical
Knowledge Model defined by this specification.

The internal representation MAY differ structurally, but SHALL preserve
the normative semantics.

------------------------------------------------------------------------

# 7. Success Criteria

Two independent implementations conform to the PMS if they expose
semantically equivalent Canonical Knowledge Models for equivalent
Persiqa documents, regardless of implementation language or storage
technology.
