# PDR-004 --- Discovering First-Class Relations

**Document:** Persiqa Design Record (PDR)

**Chapter:** PDR-004

**Title:** Discovering First-Class Relations

**Status:** Accepted

------------------------------------------------------------------------

# 1. Problem

Early versions of the model treated relationships as simple links
between Entities.

The working assumption was:

> A Relation exists only to connect two Entities.

The question was whether this assumption would remain valid as the model
became richer.

------------------------------------------------------------------------

# 2. Initial Assumption

Relations were expected to behave like references in an object model.

They had:

-   no identity,
-   no lifecycle,
-   no State,
-   no independent semantics beyond connecting two Entities.

This approach appeared sufficient for simple diagrams.

------------------------------------------------------------------------

# 3. Observation

As increasingly realistic use cases were modelled, important information
repeatedly appeared that belonged neither to the source Entity nor to
the target Entity.

Examples included:

-   Ethernet Link
    -   Link Speed
    -   RSSI
    -   PoE Active
-   Water Connection
    -   Flow Rate
    -   Pressure
-   Hosting Relationship
    -   Allocated CPU
    -   Allocated Memory

The information clearly described the connection itself.

------------------------------------------------------------------------

# 4. Hypothesis

A new hypothesis emerged:

> Perhaps a Relation is not merely a connection.

> Perhaps it is an independent model element that connects Entities.

If true, Relations should be able to own identity, State and semantics.

------------------------------------------------------------------------

# 5. Experiments

The hypothesis was tested across multiple domains.

For every relationship-specific property the same question was asked:

"Does this describe one of the connected Entities, or does it describe
the connection itself?"

Whenever the answer was "the connection", modelling became significantly
simpler when the Relation owned the information.

The pattern repeated consistently across networking, electrical systems,
water systems, HVAC and virtual infrastructure.

------------------------------------------------------------------------

# 6. Counterexamples

Alternative approaches were evaluated.

## Relation as Attribute

The relationship remained a simple property of an Entity.

Result:

Connection-specific information became fragmented and difficult to
reason about.

------------------------------------------------------------------------

## Relation as Graph Edge Only

The relationship existed only as topology.

Result:

No natural place existed for connection State or refinement.

------------------------------------------------------------------------

Neither alternative remained satisfactory when realistic infrastructure
examples were introduced.

------------------------------------------------------------------------

# 7. Discovery

The decisive insight was that relationships participate in reality just
as Entities do.

Although a Relation cannot exist without its participating Entities, it
possesses its own semantics and may possess its own State.

The model therefore became cleaner when Relations were promoted to
first-class model elements.

------------------------------------------------------------------------

# 8. Architectural Impact

This discovery changed several parts of the architecture.

Relations became capable of:

-   owning State,
-   participating in refinement,
-   carrying semantic meaning,
-   evolving independently of implementation details.

Reasoning also became more expressive because Statements could describe
knowledge about Relations directly.

------------------------------------------------------------------------

# 9. Consequences

From this point onward, Relations were treated as first-class citizens
of the Persiqa ontology.

Future relationship types are expected to emerge through semantic
refinement rather than by introducing new Core concepts.

This discovery established one of the strongest distinctions between
Persiqa and traditional object-oriented modelling approaches.
