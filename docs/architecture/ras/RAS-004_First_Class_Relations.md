# RAS-004 --- First-Class Relations

**Document:** Persiqa Architecture Rationale (RAS)

**Chapter:** RAS-004

**Title:** First-Class Relations

**Status:** Accepted

------------------------------------------------------------------------

# 1. Problem

Most modelling approaches treat relationships as attributes, references
or graph edges.

During the design of Persiqa an important question emerged:

> Should a relationship merely connect Entities, or should it be a model
> element in its own right?

------------------------------------------------------------------------

# 2. Observation

Repeated modelling exercises revealed that relationships often possess
their own information.

Examples:

-   Ethernet connection
    -   Link Speed
    -   RSSI
    -   PoE Active
-   Water connection
    -   Flow Rate
    -   Pressure
-   Hosting relationship
    -   Allocated CPU
    -   Allocated Memory

These properties describe the relationship itself rather than either
connected Entity.

------------------------------------------------------------------------

# 3. Alternatives Considered

## Alternative A --- Relation as Attribute

Represent relationships as simple references.

**Advantages**

-   Simple implementation.
-   Familiar object-oriented design.

**Disadvantages**

-   No independent identity.
-   Cannot naturally own State.
-   Difficult to refine.

**Decision:** Rejected.

------------------------------------------------------------------------

## Alternative B --- Relation as First-Class Model Element

Treat every Relation as an independent model element.

**Advantages**

-   Own identity.
-   Own lifecycle.
-   Own State.
-   Supports refinement.
-   Uniform reasoning model.

**Decision:** Accepted.

------------------------------------------------------------------------

# 4. Decision

A Relation is a first-class model element.

A Relation SHALL:

-   connect Entities,
-   own semantics,
-   optionally own State,
-   participate in refinement,
-   remain independent from implementation details.

------------------------------------------------------------------------

# 5. Rationale

The decision was driven by modelling requirements rather than
implementation preferences.

As soon as relationship-specific knowledge appeared, treating Relations
as simple references became insufficient.

Promoting Relations to first-class model elements solved this
consistently across every evaluated domain.

------------------------------------------------------------------------

# 6. Trade-offs

**Benefits**

-   Natural representation of connection-specific knowledge.
-   Cleaner ontology.
-   Better support for reasoning.
-   Independent refinement.

**Costs**

-   Slightly richer internal model.
-   Additional identity management for Relations.

The architectural benefits clearly outweighed the implementation cost.

------------------------------------------------------------------------

# 7. Consequences

Implementations SHALL preserve Relation identity.

State describing a connection SHALL belong to the Relation rather than
either endpoint.

Future relation types SHALL be expressed through semantic refinement
rather than new Core concepts.

------------------------------------------------------------------------

# 8. Validation Evidence

The first-class Relation model was validated in:

-   Networking
-   Home Automation
-   Electrical Systems
-   Water Systems
-   HVAC
-   PLC / OT
-   Building Modelling

Every analysed domain benefited from relation-owned State and
refinement.

No counterexample requiring a different architectural approach was
found.

------------------------------------------------------------------------

# 9. Future Implications

Treating Relations as first-class model elements enables richer
reasoning while keeping the Core ontology unchanged.

This decision is considered one of the defining characteristics
distinguishing Persiqa from traditional object-centric modelling
frameworks.
