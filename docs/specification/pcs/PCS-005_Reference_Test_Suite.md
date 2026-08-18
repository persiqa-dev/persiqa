# PCS-005 --- Reference Test Suite

**Document:** Persiqa Conformance Specification (PCS)

**Chapter:** PCS-005

**Title:** Reference Test Suite

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the normative Reference Test Suite used to verify
Persiqa conformance.

The suite provides a common, implementation-independent basis for
certification.

------------------------------------------------------------------------

# 2. Scope

The Reference Test Suite covers:

-   DSL parsing,
-   Canonical Knowledge Model generation,
-   Meta Model validation,
-   Reasoning,
-   Serialization,
-   Interoperability.

------------------------------------------------------------------------

# 3. Test Categories

Every conforming implementation SHALL execute the following categories:

1.  Parser Tests
2.  Meta Model Tests
3.  Reasoning Tests
4.  Serialization Tests
5.  Interoperability Tests

Each category SHALL be evaluated independently.

------------------------------------------------------------------------

# 4. Positive Test Corpus

Positive tests SHALL verify that valid models are accepted and
interpreted correctly.

The normative corpus SHALL be derived from:

-   PEX example collection,
-   REF reference models.

Equivalent implementations SHALL produce equivalent observable results.

------------------------------------------------------------------------

# 5. Negative Test Corpus

Negative tests SHALL verify rejection of invalid models.

They SHALL include, at minimum:

-   lexical errors,
-   grammar violations,
-   unresolved references,
-   invalid refinement,
-   CKM invariant violations,
-   reasoning contradictions,
-   invalid serialization.

------------------------------------------------------------------------

# 6. Expected Results

Every test SHALL define:

-   input,
-   expected outcome,
-   applicable specification,
-   pass/fail criteria.

Expected semantic behaviour SHALL be implementation-independent.

------------------------------------------------------------------------

# 7. Certification Rules

An implementation SHALL pass every mandatory test required by its
declared conformance level.

Optional features SHALL be evaluated separately.

Certification SHALL identify the supported specification version.

------------------------------------------------------------------------

# 8. Regression Requirements

The Reference Test Suite SHALL be executable repeatedly.

Previously passing mandatory tests SHALL continue to pass unless a new
specification version explicitly changes the expected behaviour.

------------------------------------------------------------------------

# 9. Maintenance

The Reference Test Suite SHALL evolve together with the Persiqa
specifications.

Each normative test SHALL reference the specification clause that it
verifies.

------------------------------------------------------------------------

# 10. Conformance

An implementation MAY claim "Persiqa Conformant" only after successfully
completing the applicable Reference Test Suite for its declared
conformance level.

The Reference Test Suite is the authoritative mechanism for
demonstrating objective compliance with the Persiqa specifications.
