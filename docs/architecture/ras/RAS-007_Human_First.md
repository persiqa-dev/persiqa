# RAS-007 --- Human First

**Document:** Persiqa Architecture Rationale (RAS)

**Chapter:** RAS-007

**Title:** Human First

**Status:** Accepted

------------------------------------------------------------------------

# 1. Problem

Many modelling languages are designed primarily for software
implementations.

They expose concepts that are convenient for computers but difficult for
domain experts to understand.

The architectural question was:

> Should Persiqa optimise for implementation or for communication?

------------------------------------------------------------------------

# 2. Observation

Throughout the design process, every architectural discussion started
with natural language.

Typical examples included:

``` text
The lamp produces light.
The pipe transports water.
The camera measures an image.
The battery stores electrical energy.
```

The technical model consistently emerged from these statements rather
than the other way around.

------------------------------------------------------------------------

# 3. Alternatives Considered

## Alternative A --- Implementation First

Design the language around programming constructs.

**Advantages**

-   Straightforward implementation.
-   Familiar to software developers.

**Disadvantages**

-   Domain experts become dependent on developers.
-   Vocabulary drifts toward implementation details.
-   Poor communication across disciplines.

**Decision:** Rejected.

------------------------------------------------------------------------

## Alternative B --- Human First

Design the language around concepts that people naturally understand.

Implementations become derived representations.

**Advantages**

-   Shared vocabulary across domains.
-   Easier communication.
-   Implementation independence.
-   Better longevity.

**Decision:** Accepted.

------------------------------------------------------------------------

# 4. Decision

Persiqa SHALL optimise for human understanding while preserving precise
machine interpretation.

The modelling language SHALL describe reality using universal concepts
rather than implementation-specific constructs.

------------------------------------------------------------------------

# 5. Rationale

Persiqa is intended to be used by people from different disciplines.

A network engineer, electrician, automation engineer, facility manager
and software developer should be able to discuss the same model without
translating between different conceptual languages.

Universal concepts such as Entity, Capability, Relation and State proved
sufficient to establish this common vocabulary.

------------------------------------------------------------------------

# 6. Trade-offs

**Benefits**

-   Readable models.
-   Shared understanding.
-   Lower learning curve across domains.
-   Technology-independent architecture.

**Costs**

-   Some implementation details remain implicit.
-   Domain terminology is expressed through refinement rather than Core
    concepts.

The long-term communication benefits outweigh the implementation
convenience of a software-centric model.

------------------------------------------------------------------------

# 7. Consequences

Implementations SHALL preserve the semantic meaning of the modelling
language.

Implementation-specific terminology SHALL NOT influence the Core
ontology.

When a choice exists between implementation convenience and conceptual
clarity, conceptual clarity SHOULD be preferred.

------------------------------------------------------------------------

# 8. Validation Evidence

The Human First principle was continuously validated while modelling:

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
-   Building Modelling

Participants from different technical backgrounds were able to reason
about the same concepts using the same vocabulary.

------------------------------------------------------------------------

# 9. Future Implications

Persiqa is intended to become a common modelling language rather than a
framework-specific API.

Future implementations should remain interchangeable because they all
derive from the same human-centred semantic model.
