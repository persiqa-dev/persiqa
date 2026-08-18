# PRS-001 --- Reasoning Model

**Document:** Persiqa Reasoning Specification (PRS)

**Chapter:** PRS-001

**Title:** Reasoning Model

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the normative conceptual model for reasoning within
Persiqa.

It establishes the semantic foundation upon which all inference rules
are defined.

------------------------------------------------------------------------

# 2. Reasoning Scope

Reasoning operates exclusively on the Canonical Knowledge Model (CKM).

Inputs:

-   Canonical Statements
-   Refinement hierarchy
-   Entity, Capability, Relation and State definitions

Outputs:

-   Derived Statements
-   Validation results
-   Contradiction reports

Reasoning SHALL NOT modify the ontology.

------------------------------------------------------------------------

# 3. Fact

A Fact is a Statement explicitly present in the CKM.

Facts are considered authoritative unless explicitly replaced.

Example:

``` text
Pump transports Water.
```

------------------------------------------------------------------------

# 4. Derived Fact

A Derived Fact is a Statement produced by applying one or more inference
rules.

Derived Facts SHALL remain traceable to their originating Facts.

A reasoning engine SHOULD preserve derivation provenance.

------------------------------------------------------------------------

# 5. Inference Rule

An Inference Rule defines a deterministic semantic transformation.

An inference rule:

-   consumes one or more Facts,
-   produces zero or more Derived Facts,
-   SHALL preserve semantic consistency.

Inference rules SHALL be implementation-independent.

------------------------------------------------------------------------

# 6. Inference Context

Every inference SHALL execute within an explicit reasoning context.

The context consists of:

-   the CKM,
-   active refinement hierarchy,
-   applicable reasoning rules.

Implementations SHALL NOT infer knowledge outside the active context.

------------------------------------------------------------------------

# 7. Knowledge Closure

Reasoning reaches Knowledge Closure when no additional Derived Facts can
be produced by the active rule set.

Repeated execution after closure SHALL produce no new knowledge.

------------------------------------------------------------------------

# 8. Reasoning Boundary

Reasoning SHALL derive knowledge.

Reasoning SHALL NOT:

-   invent ontology,
-   redefine Core concepts,
-   violate refinement,
-   contradict validated Facts without an explicit replacement rule.

------------------------------------------------------------------------

# 9. Determinism

Given:

-   the same CKM,
-   the same reasoning rules,
-   the same reasoning context,

every conforming reasoning engine SHALL produce semantically equivalent
Derived Facts.

Execution order MAY differ.

Semantic outcome SHALL NOT.

------------------------------------------------------------------------

# 10. Conformance

A conforming implementation SHALL distinguish between:

-   explicit Facts,
-   Derived Facts,
-   contradictory Facts,
-   unsupported hypotheses.

Only Facts and Derived Facts become part of the resulting reasoning
closure.

Hypotheses SHALL remain outside the Canonical Knowledge Model until
explicitly asserted or validated.
