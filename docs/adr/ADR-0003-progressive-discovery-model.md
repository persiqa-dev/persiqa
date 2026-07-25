# ADR-0003: Progressive Discovery Model

**Status:** Accepted

**Date:** 2026-07-25

## Context

Infrastructure is rarely understood completely from the beginning.

Knowledge is acquired progressively through observation, inspection,
measurement, documentation, imports, and human experience.

Persiqa exists to preserve and continuously refine this knowledge
without requiring complete understanding before modeling can begin.

------------------------------------------------------------------------

## Decision

Persiqa models the current best understanding of an infrastructure.

Knowledge is expected to evolve.

New discoveries enrich the existing graph rather than replacing it.

Unknown information is a valid state.

------------------------------------------------------------------------

## Principle 1 -- Discovery Is Incremental

Infrastructure is discovered step by step.

Each discovery contributes additional knowledge to the graph.

The model is expected to become more complete over time.

------------------------------------------------------------------------

## Principle 2 -- Unknown Is Valid

The absence of knowledge does not imply the absence of reality.

Unknown values are expected and must not prevent an entity or
relationship from existing.

------------------------------------------------------------------------

## Principle 3 -- Discovery Enriches Knowledge

New discoveries extend existing knowledge.

Existing entities and relationships should be enriched whenever
continuity is preserved.

Discovery should not unnecessarily replace or duplicate existing
concepts.

------------------------------------------------------------------------

## Principle 4 -- The Graph Represents Current Understanding

The graph represents the best understanding available at a given point
in time.

It is not expected to be a perfect representation of reality.

As knowledge improves, the graph evolves accordingly.

------------------------------------------------------------------------

## Principle 5 -- Discovery Comes From Many Sources

Knowledge may originate from:

-   Manual inspection
-   Documentation
-   Measurements
-   External system imports
-   Capability-specific discovery
-   Future automated discovery mechanisms

All sources contribute to the same knowledge graph.

------------------------------------------------------------------------

## Principle 6 -- Progressive Refinement

Previously simplified models remain valid.

As new information becomes available, the graph is refined rather than
reconstructed.

For example:

-   A breaker may initially appear to be connected directly to an
    outlet.
-   Later, a junction box may be discovered between them.

The original model was not wrong; it represented the best understanding
available at that time.

------------------------------------------------------------------------

## Consequences

The platform encourages modeling from the first available knowledge
instead of waiting for complete documentation.

Capabilities can continuously improve the graph.

Incomplete infrastructure models are considered valuable.

The graph evolves together with the user's understanding.

------------------------------------------------------------------------

## Guiding Rule

> Every discovery should increase understanding while preserving as much
> existing knowledge as possible.
