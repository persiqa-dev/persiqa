# RAS-001 --- Statement First

**Document:** Persiqa Architecture Rationale (RAS)

**Chapter:** RAS-001

**Title:** Statement First

**Status:** Accepted

------------------------------------------------------------------------

# 1. Problem

Every modelling language needs a fundamental unit of knowledge.

The central design question for Persiqa was:

> What is the smallest meaningful unit that should be stored, reasoned
> upon and exchanged?

Many modelling approaches answer this question with "Entity". During the
design of Persiqa this assumption was deliberately challenged.

------------------------------------------------------------------------

# 2. Observation

Across all analysed domains, people naturally described reality as
facts:

``` text
Camera connected_to Switch.
Pipe transports Water.
Battery stores Electrical Energy.
Camera is Online.
```

People did not think in objects first. They thought in statements about
reality.

------------------------------------------------------------------------

# 3. Alternatives Considered

## Alternative A --- Entity First

Knowledge is centred around Entities.

**Advantages**

-   Familiar object-oriented design.
-   Straightforward implementation.

**Disadvantages**

-   Relationships become secondary.
-   Reasoning becomes object-centric.
-   Knowledge is fragmented.

**Decision:** Rejected.

------------------------------------------------------------------------

## Alternative B --- Relation First

Knowledge is centred around graph relationships.

**Advantages**

-   Naturally maps to graph databases.

**Disadvantages**

-   Entities lose their primary role.
-   Capabilities and States become artificial constructs.

**Decision:** Rejected.

------------------------------------------------------------------------

## Alternative C --- Statement First

Knowledge is centred around Statements.

**Advantages**

-   Human-readable.
-   Machine-processable.
-   Storage independent.
-   Programming language independent.
-   Naturally supports reasoning.
-   Compatible with incremental modelling.

**Decision:** Accepted.

------------------------------------------------------------------------

# 4. Decision

The Statement is the primary unit of knowledge in Persiqa.

Entities, Capabilities, Relations and States exist to provide semantics
for Statements.

------------------------------------------------------------------------

# 5. Rationale

This decision emerged through repeated validation rather than
theoretical design.

The same conclusion was reached across IT infrastructure, networking,
Home Assistant, electrical systems, water systems, HVAC, PLC/OT,
robotics, logistics, healthcare, banking and building modelling.

No analysed domain required a more fundamental abstraction than the
Statement.

------------------------------------------------------------------------

# 6. Trade-offs

**Benefits**

-   Natural language aligns with the model.
-   Uniform reasoning model.
-   Technology-independent architecture.
-   Stable semantic foundation.

**Costs**

-   Implementations must interpret Statements instead of relying solely
    on object structures.
-   Object-oriented APIs become derived representations rather than the
    primary model.

The benefits were judged to outweigh the costs.

------------------------------------------------------------------------

# 7. Consequences

The parser consumes Statements.

Reasoning consumes and produces Statements.

Persistence stores Statements.

Views present Statements.

Implementations derive their internal representations from Statements.

------------------------------------------------------------------------

# 8. Validation Evidence

The Statement-first approach remained valid throughout all design
iterations.

No counterexample was found that required replacing Statements as the
fundamental knowledge unit.

------------------------------------------------------------------------

# 9. Future Implications

Future extensions SHALL preserve Statement primacy.

Any proposal introducing a lower-level abstraction SHALL demonstrate why
Statements are insufficient before it can be considered.
