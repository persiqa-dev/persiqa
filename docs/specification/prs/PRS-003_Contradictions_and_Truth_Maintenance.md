# PRS-003 --- Contradictions and Truth Maintenance

**Document:** Persiqa Reasoning Specification (PRS)

**Chapter:** PRS-003

**Title:** Contradictions and Truth Maintenance

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the normative handling of contradictions and truth
maintenance within the Persiqa reasoning model.

Its objective is to preserve semantic consistency while allowing
knowledge to evolve over time.

------------------------------------------------------------------------

# 2. Incremental Truth

Persiqa follows the Incremental Truth principle.

Additional knowledge SHALL refine or extend existing knowledge unless an
explicit replacement is asserted.

Refinement SHALL NOT invalidate previously valid Facts.

------------------------------------------------------------------------

# 3. Contradiction

A contradiction exists when two Facts cannot simultaneously be true
within the same reasoning context.

Example:

``` text
Valve state = Open
Valve state = Closed
```

when both refer to the same point in time.

A reasoning engine SHALL report contradictions explicitly.

------------------------------------------------------------------------

# 4. Refinement vs. Replacement

Refinement:

-   increases precision,
-   preserves previous meaning,
-   maintains truth.

Replacement:

-   supersedes previously asserted knowledge,
-   represents change in reality or corrected knowledge,
-   SHALL be explicitly declared.

A reasoning engine SHALL NOT infer replacement from refinement.

------------------------------------------------------------------------

# 5. Truth Maintenance

Truth maintenance SHALL:

-   preserve asserted Facts,
-   preserve derivation provenance,
-   invalidate only Derived Facts that depend on replaced or withdrawn
    Facts,
-   recalculate Knowledge Closure when required.

------------------------------------------------------------------------

# 6. Source Consistency

Multiple sources MAY assert the same Fact.

Conflicting assertions SHALL remain traceable to their originating
sources.

Conflict resolution policy is implementation-specific unless explicitly
defined by the ontology.

------------------------------------------------------------------------

# 7. Derived Knowledge

Derived Facts SHALL remain valid only while all supporting Facts remain
valid.

If supporting Facts are replaced or withdrawn, affected Derived Facts
SHALL be re-evaluated.

------------------------------------------------------------------------

# 8. Temporal Change

A change in reality SHALL be represented explicitly.

Historical Facts and current Facts MAY coexist if distinguished by time
or context.

Temporal modelling SHALL NOT be inferred implicitly.

------------------------------------------------------------------------

# 9. Diagnostics

A conforming implementation SHALL distinguish between:

-   contradiction,
-   refinement,
-   replacement,
-   unsupported inference,
-   withdrawn knowledge.

Each diagnostic SHALL identify the originating Facts.

------------------------------------------------------------------------

# 10. Conformance

A conforming reasoning engine SHALL:

-   preserve Incremental Truth,
-   distinguish refinement from replacement,
-   maintain derivation provenance,
-   detect contradictions deterministically,
-   produce a semantically consistent Knowledge Closure after truth
    maintenance.
