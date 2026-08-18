# RAS-005 --- Capability Families

**Document:** Persiqa Architecture Rationale (RAS)

**Chapter:** RAS-005

**Title:** Capability Families

**Status:** Accepted

------------------------------------------------------------------------

# 1. Problem

As new domains were analysed, an increasing number of candidate
Capabilities emerged.

Examples included:

-   Capture
-   Filter
-   Authenticate
-   Route
-   Assemble
-   Verify
-   Identify

The architectural question became:

> Should each become a new Core Capability, or is there a more universal
> model?

------------------------------------------------------------------------

# 2. Observation

Every candidate Capability initially appeared unique.

Further analysis consistently revealed that each represented either:

-   a refinement of an existing Capability family,
-   or a composition of multiple Capability families.

The apparent diversity came from domain vocabulary rather than
architectural differences.

------------------------------------------------------------------------

# 3. Alternatives Considered

## Alternative A --- Large Capability Catalogue

Introduce a dedicated Core Capability for every commonly used action.

**Advantages**

-   Familiar domain terminology.
-   Minimal refinement required.

**Disadvantages**

-   Unbounded Core growth.
-   Inconsistent abstraction level.
-   Domain coupling.

**Decision:** Rejected.

------------------------------------------------------------------------

## Alternative B --- Universal Capability Families

Define a small number of universal Capability families.

Express domain behaviour through refinement.

**Advantages**

-   Stable Core.
-   Consistent modelling.
-   Unlimited extensibility.
-   Cross-domain applicability.

**Decision:** Accepted.

------------------------------------------------------------------------

# 4. Decision

The Persiqa Core defines only the following universal Capability
families:

-   Transport
-   Store
-   Transform
-   Measure
-   Control

All domain-specific Capabilities SHALL be represented as refinements or
compositions of these families.

------------------------------------------------------------------------

# 5. Rationale

The Capability families were not selected a priori.

They emerged through repeated validation across unrelated domains.

Examples:

-   Capture Image → Measure
-   Measure Temperature → Measure
-   Transport Water → Transport
-   Route Packet → Transport + Control
-   Authenticate User → Measure + Control
-   Filter Traffic → Measure + Control
-   Assemble Product → Transform

The same small set repeatedly proved sufficient.

------------------------------------------------------------------------

# 6. Trade-offs

**Benefits**

-   Small and stable Core.
-   Consistent mental model.
-   Predictable refinement.
-   Domain independence.

**Costs**

-   Some domain concepts require decomposition.
-   Initial modelling requires architectural discipline.

The long-term benefits outweigh the additional modelling effort.

------------------------------------------------------------------------

# 7. Consequences

Future Capability proposals SHALL first demonstrate why they cannot be
expressed using the existing Capability families.

Only after refinement and composition have demonstrably failed MAY a new
Core Capability family be considered.

------------------------------------------------------------------------

# 8. Validation Evidence

The Capability families were validated across:

-   IT Infrastructure
-   Networking
-   Home Automation
-   Electrical Systems
-   Water Systems
-   HVAC
-   PLC / OT
-   Robotics
-   Logistics
-   Healthcare
-   Banking
-   Building Modelling

No validated Capability required expansion of the Core.

------------------------------------------------------------------------

# 9. Future Implications

Capability Families provide a stable semantic foundation for future
domains.

As new technologies emerge, the vocabulary is expected to grow through
refinement while the universal Capability families remain unchanged.
