# PAS-000 --- Preface

**Document:** Persiqa Architecture Specification (PAS)

**Chapter:** PAS-000

**Title:** Preface

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This specification defines the normative architecture of the Persiqa
Core.

It is the authoritative source describing the concepts, principles,
rules and constraints that govern the Persiqa modelling language.

Every implementation---including Java libraries, DSLs, APIs, persistence
layers, user interfaces and reasoning engines---SHALL conform to this
specification.

If an implementation contradicts this specification, the implementation
SHALL be considered incorrect.

------------------------------------------------------------------------

# 2. Scope

This specification defines:

-   the first principles of the Persiqa Core,
-   the Core ontology,
-   the Persiqa modelling language,
-   the architectural laws,
-   the invariants every implementation SHALL preserve.

Implementation details (programming languages, storage technologies,
protocols and user interfaces) are explicitly outside the scope of this
specification.

------------------------------------------------------------------------

# 3. Intended Audience

This specification is intended for:

-   Architects
-   Framework developers
-   DSL designers
-   Reasoning engine developers
-   Tooling developers
-   Contributors to the Persiqa ecosystem

It is not intended to serve as end-user documentation.

------------------------------------------------------------------------

# 4. Normative Language

The following keywords are normative.

  Keyword      Meaning
  ------------ -------------------------------
  SHALL        Mandatory requirement
  SHALL NOT    Forbidden behaviour
  SHOULD       Strong recommendation
  SHOULD NOT   Strong recommendation against
  MAY          Optional behaviour

Unless explicitly stated otherwise, every rule in this specification is
normative.

------------------------------------------------------------------------

# 5. Source of Truth

This specification is the single source of truth for the Persiqa
architecture.

All implementations SHALL be derived from this specification.

The specification SHALL NOT be derived from any implementation.

------------------------------------------------------------------------

# 6. Core Philosophy

The Persiqa Core is intentionally minimal.

Every Core concept SHALL justify its existence through repeated
validation across multiple independent domains.

Whenever possible, an existing concept SHALL be refined instead of
introducing a new Core concept.

Core simplicity has precedence over domain convenience.

------------------------------------------------------------------------

# 7. Specification Structure

The Persiqa Architecture Specification (PAS) consists of independent
chapters.

Each chapter defines a single architectural concern.

Chapters SHALL be:

-   independently reviewable,
-   independently versioned,
-   independently evolvable.

No normative chapter MAY contradict another normative chapter.

------------------------------------------------------------------------

# 8. Evolution

The Persiqa Core is expected to evolve slowly.

Changes SHALL only be introduced through explicit architectural
decisions.

Backward compatibility SHOULD be preserved whenever reasonably possible.

New domains SHALL be supported through refinement rather than expansion
of the Core.

------------------------------------------------------------------------

# 9. Success Criteria

The Persiqa Core is considered successful if it can describe multiple
independent infrastructure domains using the same universal concepts
without introducing domain-specific concepts into the Core ontology.
