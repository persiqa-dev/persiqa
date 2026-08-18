# PDR-001 --- Discovering Statement First

**Document:** Persiqa Design Record (PDR)

**Chapter:** PDR-001

**Title:** Discovering Statement First

**Status:** Accepted

------------------------------------------------------------------------

# 1. Problem

At the beginning of the project, the fundamental modelling unit was
unknown.

The central question was:

> What is the smallest meaningful element from which the entire model
> can be built?

------------------------------------------------------------------------

# 2. Initial Assumption

The initial assumption followed traditional modelling practice.

The model would be centred around **Entities**.

Capabilities, Relations and State would be attached to Entities.

This approach appeared natural because it closely matched
object-oriented programming.

------------------------------------------------------------------------

# 3. Observation

As real-world examples were analysed, discussions consistently started
with facts rather than objects.

Examples:

``` text
Camera connected_to Switch.
Pipe transports Water.
Battery stores Electrical Energy.
Lamp produces Light.
```

Participants naturally described reality using complete statements.

The Entity appeared only as part of a larger assertion.

------------------------------------------------------------------------

# 4. Hypothesis

A new hypothesis emerged:

> Perhaps the Statement, rather than the Entity, is the true atomic unit
> of knowledge.

If correct, every architectural layer should naturally operate on
Statements.

------------------------------------------------------------------------

# 5. Experiments

The hypothesis was tested across multiple domains.

For each domain:

1.  Describe the situation in natural language.
2.  Translate it into Statements.
3.  Verify whether the ontology can interpret the Statements.
4.  Check whether refinement preserves earlier Statements.

Domains included:

-   IT infrastructure
-   Networking
-   Home Automation
-   Electrical systems
-   Water systems
-   HVAC
-   PLC / OT
-   Robotics
-   Logistics
-   Healthcare
-   Banking
-   Building modelling

The same modelling approach succeeded in every case.

------------------------------------------------------------------------

# 6. Counterexamples

Several alternatives were evaluated.

## Entity First

Rejected because knowledge became fragmented across objects.

## Relation First

Rejected because Entities and Capabilities became secondary.

## Storage First

Rejected because persistence is an implementation concern rather than a
modelling concern.

No evaluated alternative provided a simpler or more universal foundation
than the Statement.

------------------------------------------------------------------------

# 7. Discovery

The decisive observation was not that Statements could describe the
model.

It was that **every other Core concept exists only to give semantic
meaning to Statements**.

Entities identify subjects.

Relations express connections.

Capabilities describe potential.

States describe current conditions.

Statements unite them into knowledge.

------------------------------------------------------------------------

# 8. Architectural Impact

The discovery fundamentally changed the architecture.

Instead of designing around objects, Persiqa became a language centred
on knowledge.

Consequently:

-   the parser processes Statements,
-   the ontology interprets Statements,
-   reasoning consumes and produces Statements,
-   persistence stores Statements,
-   implementations derive internal structures from Statements.

------------------------------------------------------------------------

# 9. Consequences

The Statement became the canonical representation of knowledge
throughout Persiqa.

Every subsequent architectural decision---including Universal Core,
Universal Refinement and Incremental Truth---was evaluated against this
discovery.

Statement First is therefore considered the foundational discovery upon
which the remainder of the Persiqa architecture was built.
