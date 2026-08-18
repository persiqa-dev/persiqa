# PDR-007 --- Designing a Human Language

**Document:** Persiqa Design Record (PDR)

**Chapter:** PDR-007

**Title:** Designing a Human Language

**Status:** Accepted

------------------------------------------------------------------------

# 1. Problem

Early design discussions naturally drifted toward software architecture.

Classes, APIs and persistence models appeared to be the natural starting
point.

A fundamental question emerged:

> Is Persiqa a framework, or is it a language?

------------------------------------------------------------------------

# 2. Initial Assumption

The initial assumption was that the modelling language would primarily
serve software developers.

Human readability was considered desirable, but secondary.

As long as the implementation was elegant, the language was expected to
follow.

------------------------------------------------------------------------

# 3. Observation

A turning point occurred during repeated review of real-world examples.

The design process changed from asking:

> "How should this be implemented?"

to asking:

> "How would a real person describe this?"

Examples included:

``` text
The lamp produces light.
The pipe transports water.
The battery stores electrical energy.
The camera measures an image.
```

The quality of the model consistently improved when the language became
more natural.

------------------------------------------------------------------------

# 4. Hypothesis

A new hypothesis emerged:

> If people naturally describe reality using universal concepts, the
> architecture should preserve that language rather than replace it.

This shifted the optimisation target from implementation simplicity to
semantic clarity.

------------------------------------------------------------------------

# 5. Experiments

The hypothesis was tested continuously.

Candidate names, concepts and sentence structures were evaluated using
questions such as:

-   Does this sound natural?
-   Would a domain expert understand it without software knowledge?
-   Does the same sentence work across unrelated domains?
-   Does improving readability reduce semantic precision?

Whenever readability and precision could coexist, both were preserved.

------------------------------------------------------------------------

# 6. Counterexamples

Several implementation-oriented alternatives were explored.

Examples included:

-   domain-specific terminology,
-   object-oriented naming,
-   technology-driven abstractions,
-   implementation-centric vocabulary.

Although technically valid, these approaches consistently reduced
cross-domain understanding.

They were rejected.

------------------------------------------------------------------------

# 7. Discovery

The decisive insight was that Persiqa is not primarily a programming
model.

It is a shared language for describing reality.

The implementation is derived from the language, not the other way
around.

This discovery influenced every subsequent architectural decision,
including naming, ontology and refinement.

------------------------------------------------------------------------

# 8. Architectural Impact

From this point onward:

-   natural language became the primary validation tool,
-   universal vocabulary became a design constraint,
-   implementation details were intentionally excluded from the Core,
-   semantic consistency became more important than programming
    convenience.

The architecture became language-first rather than implementation-first.

------------------------------------------------------------------------

# 9. Consequences

Persiqa is designed to be understood by people before it is processed by
software.

A network engineer, electrician, automation engineer, facility manager
and software developer should all be able to reason about the same model
without translation.

This discovery established one of the defining goals of Persiqa:

**A common language for modelling reality across domains.**
