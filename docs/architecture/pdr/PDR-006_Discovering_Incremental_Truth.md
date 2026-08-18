# PDR-006 --- Discovering Incremental Truth

**Document:** Persiqa Design Record (PDR)

**Chapter:** PDR-006

**Title:** Discovering Incremental Truth

**Status:** Accepted

------------------------------------------------------------------------

# 1. Problem

Early modelling discussions implicitly assumed that a model becomes
correct only after it is complete.

Real infrastructure projects immediately challenged this assumption.

The question became:

> Can an incomplete model still be correct?

------------------------------------------------------------------------

# 2. Initial Assumption

The initial expectation was that refinement replaces earlier knowledge.

As the model becomes more detailed, previous descriptions would become
obsolete.

This matched the intuition that there should be a single "correct"
model.

------------------------------------------------------------------------

# 3. Observation

Repeated refinement exercises produced an unexpected result.

Example:

Initial Statement:

``` text
PC connected_to Switch.
```

Later refinement:

``` text
PC composed_of Network Interface.
Network Interface connected_to Patch Cable.
Patch Cable connected_to Wall Outlet.
Wall Outlet connected_to Patch Panel.
Patch Panel connected_to Switch.
```

The original Statement did not become false.

It remained correct, but less precise.

The same behaviour appeared in every analysed domain.

------------------------------------------------------------------------

# 4. Hypothesis

A new hypothesis emerged:

> Truth and precision are different concepts.

Increasing precision does not necessarily invalidate previously known
truth.

If correct, a model could remain valid while continuously evolving.

------------------------------------------------------------------------

# 5. Experiments

The hypothesis was tested using increasingly detailed models.

The same experiment was repeated:

1.  Create a coarse model.
2.  Refine the model.
3.  Verify whether earlier Statements remain true.

The result was consistent.

Refinement added knowledge.

It did not remove truth.

Only explicit contradiction or replacement invalidated an earlier
Statement.

------------------------------------------------------------------------

# 6. Counterexamples

The hypothesis was challenged using multiple domains:

-   Networking
-   Water systems
-   Electrical systems
-   Home Automation
-   HVAC
-   PLC / OT
-   Building modelling

No valid example required ordinary refinement to invalidate existing
Statements.

When knowledge genuinely changed, it represented a replacement of
reality rather than refinement of understanding.

------------------------------------------------------------------------

# 7. Discovery

The decisive insight was that Persiqa models knowledge about reality.

Knowledge grows incrementally.

Reality does not need to be rediscovered from scratch whenever
additional detail becomes available.

This separated:

-   truth,
-   precision,
-   and temporal change

into independent architectural concepts.

------------------------------------------------------------------------

# 8. Architectural Impact

This discovery fundamentally changed the Knowledge Model.

Refinement became additive.

Views became alternative perspectives.

Zoom became an alternative level of detail.

The architecture no longer required a single "complete" representation
of reality.

------------------------------------------------------------------------

# 9. Consequences

Incremental Truth became one of the architectural foundations of
Persiqa.

It enables:

-   iterative modelling,
-   collaborative knowledge growth,
-   long-lived infrastructure models,
-   stable reasoning across multiple refinement levels.

This discovery connected Statement First, Universal Core and Universal
Refinement into a coherent knowledge architecture.
