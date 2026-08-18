# PDR-005 --- Discovering Capability Families

**Document:** Persiqa Design Record (PDR)

**Chapter:** PDR-005

**Title:** Discovering Capability Families

**Status:** Accepted

------------------------------------------------------------------------

# 1. Problem

Once Capabilities became part of the Core, a new question immediately
emerged:

> How many Capability families are required to model reality?

At first glance, every new domain appeared to introduce new kinds of
abilities.

Without a guiding principle, the Capability model would continuously
expand.

------------------------------------------------------------------------

# 2. Initial Assumption

The initial expectation was that each frequently occurring action would
eventually require its own Core Capability.

Examples included:

-   Capture
-   Route
-   Filter
-   Authenticate
-   Assemble
-   Verify
-   Encode
-   Decode

The assumption seemed reasonable because each represented a recognisable
behaviour.

------------------------------------------------------------------------

# 3. Observation

As more examples were analysed, a recurring pattern emerged.

Whenever a new Capability appeared, deeper analysis revealed that it
could be expressed as either:

-   a refinement of an existing Capability family,
-   or a composition of multiple Capability families.

The apparent novelty consistently disappeared after semantic analysis.

------------------------------------------------------------------------

# 4. Hypothesis

A new hypothesis emerged:

> Perhaps the architecture does not need more Capability families.

> Perhaps it needs better semantic refinement.

If true, the Capability model could remain permanently small while
supporting unlimited domains.

------------------------------------------------------------------------

# 5. Experiments

The hypothesis was deliberately challenged.

For every proposed Capability the following questions were asked:

1.  What is the essential behaviour?
2.  Can that behaviour be expressed by an existing family?
3.  Is the apparent difference merely domain vocabulary?
4.  Is the behaviour actually a composition of multiple Capabilities?

Representative experiments included:

  Candidate           Result
  ------------------- ---------------------
  Capture Image       Measure
  Route Packet        Transport + Control
  Authenticate User   Measure + Control
  Filter Traffic      Measure + Control
  Assemble Product    Transform
  Charge Battery      Transport + Store
  Encode Data         Transform

The same pattern repeated consistently.

------------------------------------------------------------------------

# 6. Counterexamples

Several candidates resisted simplification.

Examples included:

-   Capture
-   Route
-   Authenticate

Each appeared fundamental during initial analysis.

After repeated modelling exercises, every example collapsed into
refinement or composition.

No validated Capability required a new Core family.

------------------------------------------------------------------------

# 7. Discovery

The decisive insight was that Capability families describe universal
classes of behaviour rather than domain terminology.

Domains continuously invent new names.

Reality continuously reuses the same underlying behaviours.

The architecture therefore models behaviours instead of vocabulary.

------------------------------------------------------------------------

# 8. Architectural Impact

Capability growth changed fundamentally.

Instead of asking:

> "Which new Capability family should be introduced?"

the design process became:

> "Which existing family does this refine?"

This shifted architectural effort from expanding the ontology to
improving semantic precision.

------------------------------------------------------------------------

# 9. Consequences

The Capability model became both stable and extensible.

Future domains are expected to contribute refinements rather than new
Capability families.

This discovery completed the same architectural pattern already observed
for Entities and Relations, providing one of the strongest pieces of
evidence for the Universal Refinement Principle.
