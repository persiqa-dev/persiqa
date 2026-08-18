# RAS-002 --- Universal Core

**Document:** Persiqa Architecture Rationale (RAS)

**Chapter:** RAS-002

**Title:** Universal Core

**Status:** Accepted

------------------------------------------------------------------------

# 1. Problem

Every modelling framework faces the same architectural pressure:

As new domains are introduced, new concepts appear to require inclusion
in the Core.

Without discipline, the Core continuously grows until it becomes
domain-specific.

The design question for Persiqa was therefore:

> How can the Core remain stable while supporting unlimited domains?

------------------------------------------------------------------------

# 2. Observation

During modelling, many candidates appeared to deserve Core status:

-   Switch
-   Router
-   Gateway
-   Pipe
-   Pump
-   Valve
-   PoE
-   Patch Panel

Initially these concepts appeared fundamental.

Repeated analysis showed that each was only fundamental within a
particular domain.

------------------------------------------------------------------------

# 3. Alternatives Considered

## Alternative A --- Domain-Rich Core

Promote commonly used domain concepts into the Core.

**Advantages**

-   Convenient for early implementations.
-   Rich domain vocabulary.

**Disadvantages**

-   Core continuously grows.
-   Strong coupling to domains.
-   New domains require Core changes.
-   Long-term architectural instability.

**Decision:** Rejected.

------------------------------------------------------------------------

## Alternative B --- Universal Core

Keep the Core limited to concepts that remain valid across independent
domains.

Domain knowledge is expressed through refinement.

**Advantages**

-   Stable architecture.
-   Domain independence.
-   Long-term maintainability.
-   Predictable evolution.

**Disadvantages**

-   More domain concepts must be expressed through refinement.
-   Requires stronger modelling discipline.

**Decision:** Accepted.

------------------------------------------------------------------------

# 4. Decision

The Persiqa Core SHALL contain only universal concepts.

Domain-specific concepts SHALL NOT become Core concepts solely because
they are frequently used.

A concept belongs to the Core only if it survives validation across
multiple unrelated domains.

------------------------------------------------------------------------

# 5. Rationale

Validation repeatedly demonstrated that seemingly fundamental concepts
could always be represented using the existing Core ontology.

Examples:

-   Switch → Entity refinement
-   Router → Entity refinement
-   Pipe → Entity refinement
-   PoE → Capability + Relation semantics
-   Gateway → Entity refinement

The ontology remained unchanged while the vocabulary expanded.

This separation proved to be both expressive and stable.

------------------------------------------------------------------------

# 6. Trade-offs

**Benefits**

-   Small and understandable Core.
-   Stable long-term architecture.
-   Independent evolution of domains.
-   Reduced implementation complexity.

**Costs**

-   Domain models require explicit refinement.
-   Less immediate convenience for domain-specific users.

The long-term architectural benefits outweigh the additional modelling
effort.

------------------------------------------------------------------------

# 7. Consequences

Every future proposal introducing a new Core concept SHALL first
demonstrate:

1.  Why the concept cannot be represented as:
    -   Entity
    -   Capability
    -   Relation
    -   State
2.  Why refinement is insufficient.
3.  Why the concept remains universal across unrelated domains.

Failure to satisfy any criterion rejects the proposal.

------------------------------------------------------------------------

# 8. Validation Evidence

The Universal Core principle was validated across:

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

In every case the same Core ontology remained sufficient.

------------------------------------------------------------------------

# 9. Future Implications

The Universal Core principle protects Persiqa against uncontrolled
architectural growth.

Future domains are expected to enrich the model through refinement
rather than expansion.

This principle is considered one of the primary mechanisms ensuring the
long-term stability of the Persiqa architecture.
