# RAS-006 --- Incremental Truth

**Document:** Persiqa Architecture Rationale (RAS)

**Chapter:** RAS-006

**Title:** Incremental Truth

**Status:** Accepted

------------------------------------------------------------------------

# 1. Problem

Traditional modelling approaches often assume that a model is useful
only when it is complete.

Real-world infrastructure modelling rarely begins with complete
knowledge.

The architectural question was:

> Can an incomplete model still be considered correct?

------------------------------------------------------------------------

# 2. Observation

During the design of Persiqa every use case started with partial
knowledge.

Example:

``` text
PC connected_to Switch.
```

Later refinements added:

``` text
PC composed_of Network Interface.
Network Interface connected_to Patch Cable.
Patch Cable connected_to Wall Outlet.
Wall Outlet connected_to Patch Panel.
Patch Panel connected_to Switch.
```

The original Statement never became false.

It simply became less precise.

------------------------------------------------------------------------

# 3. Alternatives Considered

## Alternative A --- Complete Model Required

Only complete models are considered valid.

**Advantages**

-   Single representation of reality.

**Disadvantages**

-   Unrealistic.
-   Discourages iterative modelling.
-   Prevents gradual discovery.

**Decision:** Rejected.

------------------------------------------------------------------------

## Alternative B --- Incremental Truth

A model is valid for the knowledge available at the time it is created.

Additional knowledge refines existing knowledge.

**Decision:** Accepted.

------------------------------------------------------------------------

# 4. Decision

Persiqa adopts the Incremental Truth principle.

Every model SHALL be considered valid for its current level of
knowledge.

Refinement SHALL increase precision without invalidating existing truth
unless a Statement is explicitly superseded.

------------------------------------------------------------------------

# 5. Rationale

Infrastructure knowledge is naturally discovered over time.

The architecture therefore models knowledge evolution rather than
requiring perfect knowledge from the beginning.

This aligns naturally with Statements, Refinement and Zoom.

------------------------------------------------------------------------

# 6. Trade-offs

**Benefits**

-   Supports iterative modelling.
-   Allows early modelling with limited information.
-   Preserves existing knowledge.
-   Enables collaborative model growth.

**Costs**

-   Multiple valid representations may coexist at different refinement
    levels.
-   Consumers must understand refinement semantics.

The benefits outweigh the additional semantic complexity.

------------------------------------------------------------------------

# 7. Consequences

Refinement SHALL be additive by default.

Views and Zoom levels SHALL expose different perspectives without
changing truth.

Historical knowledge MAY be preserved by implementations but is not
required by the Core.

------------------------------------------------------------------------

# 8. Validation Evidence

Incremental Truth remained consistent across all validated domains
including networking, Home Automation, electrical systems, water
systems, HVAC, PLC/OT, robotics, logistics, healthcare and building
modelling.

No validated scenario required replacing the principle.

------------------------------------------------------------------------

# 9. Future Implications

Incremental Truth enables Persiqa to support continuous discovery,
collaborative modelling and long-lived infrastructure knowledge bases
without destabilising the Core architecture.
