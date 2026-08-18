# PCS-001 --- Parser Conformance

**Document:** Persiqa Conformance Specification (PCS)

**Chapter:** PCS-001

**Title:** Parser Conformance

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the normative conformance requirements for Persiqa
DSL parsers.

A conforming parser SHALL transform valid DSL documents into
semantically equivalent Canonical Knowledge Models (CKMs).

------------------------------------------------------------------------

# 2. Scope

Parser conformance includes:

-   lexical analysis,
-   syntactic analysis,
-   semantic mapping,
-   CKM generation,
-   diagnostics.

Reasoning is outside the scope of parser conformance.

------------------------------------------------------------------------

# 3. Mandatory Capabilities

A conforming parser SHALL:

-   accept every valid construct defined by the PDS,
-   reject invalid lexical input,
-   reject syntactically invalid documents,
-   reject semantically invalid models,
-   generate a valid CKM for every valid document.

------------------------------------------------------------------------

# 4. Canonical Mapping

The generated CKM SHALL conform to the PMS.

Equivalent DSL documents SHALL produce semantically equivalent CKMs.

Parser implementation details SHALL NOT influence the resulting CKM.

------------------------------------------------------------------------

# 5. Determinism

Given identical DSL input, a parser SHALL produce:

-   equivalent CKMs,
-   equivalent validation outcomes,
-   equivalent diagnostics categories.

Diagnostic wording MAY differ.

Semantic outcome SHALL NOT.

------------------------------------------------------------------------

# 6. Diagnostic Requirements

Diagnostics SHALL identify:

-   validation stage,
-   violated rule,
-   affected source location,
-   severity.

Severity levels:

-   Error
-   Warning
-   Information

------------------------------------------------------------------------

# 7. Normative Test Categories

A conforming parser SHALL pass tests covering:

-   lexical correctness,
-   grammar conformance,
-   namespace resolution,
-   import handling,
-   local-reference parsing and resolution,
-   forward references,
-   direct and explicitly defined derived reference resolution,
-   reference scope and uniqueness,
-   refinement mapping,
-   statement mapping,
-   CKM construction,
-   invalid document rejection.

------------------------------------------------------------------------

# 8. Negative Tests

A conforming parser SHALL reject documents containing:

-   invalid UTF-8,
-   malformed grammar,
-   unresolved references,
-   ambiguous or out-of-scope references,
-   duplicate local references,
-   multiple local references bound to the same canonical object within one
    scope,
-   incompatible reference types,
-   cyclic semantic derivation,
-   refinement cycles,
-   duplicate canonical identities where prohibited.

------------------------------------------------------------------------

# 9. Certification Criteria

Parser conformance is achieved when all mandatory parser tests defined
by the Persiqa Conformance Test Suite pass successfully.

Partial parser implementations SHALL explicitly declare unsupported
language features.

------------------------------------------------------------------------

# 10. Conformance

An implementation MAY claim "Persiqa Parser Conformant" only if it
satisfies every mandatory requirement defined in this chapter and the
referenced PDS and PMS specifications.
