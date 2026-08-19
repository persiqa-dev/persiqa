# PDS-000 --- DSL Specification Introduction

**Document:** Persiqa DSL Specification (PDS)

**Chapter:** PDS-000

**Title:** Introduction

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

The Persiqa DSL (Domain Specific Language) defines the canonical textual
representation of a Persiqa knowledge model.

Its purpose is to provide a stable, human-readable and
machine-processable language for creating, exchanging and validating
Persiqa models.

------------------------------------------------------------------------

# 2. Position Within the Documentation

  Family   Purpose
  -------- -------------------------------------
  PAS      Defines the architecture
  RAS      Explains architectural decisions
  PDR      Preserves architectural discoveries
  PEX      Demonstrates focused examples
  REF      Defines complete reference models
  PDS      Defines the canonical language

------------------------------------------------------------------------

# 3. Design Goals

The DSL SHALL be:

-   Human-readable
-   Unambiguous
-   Deterministic
-   Stable across implementations
-   Independent of storage technology
-   Independent of programming language

------------------------------------------------------------------------

# 4. Scope

The DSL specifies lexical elements, grammar, canonical syntax, semantic
rules, validation rules and serialization.

Natural-language ingestion is outside the scope of the PDS specification.
Natural-language processing is a future, non-normative vision and is not
required to construct or consume the CKM.

It does not specify parser implementation details.

------------------------------------------------------------------------

# 5. Relationship to the Core

The DSL is a representation of the Persiqa Core.

Every valid DSL document SHALL map to a valid Persiqa knowledge model.

------------------------------------------------------------------------

# 6. Success Criteria

Two independent implementations parsing the same valid DSL SHALL produce
semantically equivalent Persiqa models.
