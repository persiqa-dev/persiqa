# PDR-002 --- Discovering Universal Core

**Document:** Persiqa Design Record (PDR)

**Chapter:** PDR-002

**Title:** Discovering Universal Core

**Status:** Accepted

------------------------------------------------------------------------

# 1. Problem

After establishing the Statement as the fundamental unit of knowledge,
another question emerged:

> What concepts belong in the Core?

Every new modelling exercise appeared to introduce new "fundamental"
concepts.

Without a clear principle, the Core would continuously expand.

------------------------------------------------------------------------

# 2. Initial Assumption

The initial assumption was pragmatic.

If a concept appeared frequently enough, it should probably become part
of the Core.

Examples included:

-   Switch
-   Router
-   Gateway
-   Pipe
-   Valve
-   Pump
-   Patch Panel
-   PoE

This seemed reasonable because each appeared essential within its own
domain.

------------------------------------------------------------------------

# 3. Observation

As additional domains were analysed, a surprising pattern appeared.

Every domain proposed a different set of "fundamental" concepts.

Networking proposed Switches.

Electrical systems proposed Breakers.

Water systems proposed Pipes.

HVAC proposed Boilers.

Healthcare proposed Monitors.

No candidate remained fundamental once the domain changed.

------------------------------------------------------------------------

# 4. Hypothesis

A new hypothesis emerged:

> Perhaps the Core should not contain domain concepts at all.

Instead, it should contain only concepts that survive across unrelated
domains.

------------------------------------------------------------------------

# 5. Experiments

The hypothesis was tested repeatedly.

For every proposed Core concept the same questions were asked:

1.  Can it be represented as an Entity?
2.  Does it require a new Capability?
3.  Is it simply a Relation semantic?
4.  Is it merely State?
5.  Can refinement express it?

Whenever the answer was "yes", the candidate was removed from the Core.

------------------------------------------------------------------------

# 6. Counterexamples

Numerous candidates challenged the hypothesis.

Examples:

-   Switch
-   Router
-   Pipe
-   Pump
-   PoE
-   Gateway

Each initially appeared indispensable.

Each ultimately proved to be representable through refinement of
existing Core concepts.

No validated counterexample survived.

------------------------------------------------------------------------

# 7. Discovery

The breakthrough was recognising that universality is determined across
domains rather than within a domain.

A concept belongs in the Core only if removing the domain context does
not remove its meaning.

This shifted the architectural goal from building a rich Core to
protecting a minimal one.

------------------------------------------------------------------------

# 8. Architectural Impact

This discovery introduced a permanent architectural discipline.

Core growth became exceptional.

Domain growth became expected.

Future modelling efforts were redirected toward refinement instead of
expansion.

This discovery later enabled the Universal Refinement Principle.

------------------------------------------------------------------------

# 9. Consequences

The Core became intentionally small and stable.

Every future proposal must justify Core inclusion through cross-domain
validation rather than popularity within a single domain.

This discovery established one of the central architectural
characteristics of Persiqa:

**A universal language is achieved by minimising the Core and maximising
refinement.**
