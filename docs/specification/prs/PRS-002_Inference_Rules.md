# PRS-002 --- Inference Rules

**Document:** Persiqa Reasoning Specification (PRS)

**Chapter:** PRS-002

**Title:** Inference Rules

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the normative inference rules of Persiqa.

Inference rules describe how new knowledge MAY be derived from existing
Facts while preserving the semantics of the Canonical Knowledge Model
(CKM).

------------------------------------------------------------------------

# 2. Rule Properties

Every inference rule SHALL be:

-   deterministic,
-   monotonic unless an explicit replacement rule applies,
-   ontology-preserving,
-   traceable to its source Facts.

------------------------------------------------------------------------

# 3. Direct Inference

A rule MAY derive a new Statement directly from one Fact when explicitly
defined by the ontology.

Example:

``` text
Valve controls Water Flow.
```

may derive:

``` text
Valve influences Water Transport.
```

if such a semantic rule exists.

------------------------------------------------------------------------

# 4. Transitive Inference

If a Relation is defined as transitive, inference MAY derive indirect
relationships.

Example:

``` text
A connected_to B.
B connected_to C.
```

may derive:

``` text
A indirectly_connected_to C.
```

The original Facts SHALL remain unchanged.

------------------------------------------------------------------------

# 5. Refinement-Aware Inference

Inference SHALL honor refinement.

Knowledge valid for a parent concept is also valid for refinements
unless explicitly restricted.

Example:

``` text
Pump transports Water.
HeatPump refines Pump.
```

may derive:

``` text
HeatPump transports Water.
```

------------------------------------------------------------------------

# 6. Composition Inference

Composition MAY propagate knowledge between a whole and its parts only
when defined by the ontology.

No implicit propagation SHALL occur without a normative rule.

------------------------------------------------------------------------

# 7. Relation-Based Inference

Relations MAY participate in inference as first-class concepts.

Relation State and Relation refinement SHALL be considered during
inference.

------------------------------------------------------------------------

# 8. State Propagation

State propagation SHALL follow explicit semantic rules.

Changing State SHALL NOT modify identity or ontology.

Derived State SHALL remain distinguishable from asserted State.

------------------------------------------------------------------------

# 9. Rule Ordering

Implementations MAY execute rules in any internal order.

The resulting Knowledge Closure SHALL be semantically equivalent
regardless of execution order.

------------------------------------------------------------------------

# 10. Conformance

A conforming reasoning engine SHALL:

-   apply only normative inference rules,
-   preserve source Facts,
-   record derivation provenance,
-   produce deterministic Knowledge Closure,
-   never invent ontology beyond the Persiqa Core.
