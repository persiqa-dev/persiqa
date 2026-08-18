# ADR-0004 — Core Domain Model Consolidation

**Status:** Accepted  
**Date:** 2026-08-18

## Context

Persiqa's Core Domain Model was developed incrementally through several
architectural decisions and subsequent refinement.

Earlier ADR material described an earlier form of the model using concepts
such as `Relationship` and `Projection`. The current Persiqa architecture
has since converged on a stable Core model consisting of:

- Entity
- Capability
- Relation
- State

with Statement serving as the fundamental unit of expressed knowledge.

The current PAS is the normative source for the resulting architecture.
This ADR records the consolidation decision that established the current
terminology and boundaries.

## Decision

Persiqa SHALL use the current Core Domain Model defined by the PAS.

The authoritative Core ontology consists of:

```text
Entity
Capability
Relation
State
```

Statement SHALL be treated as the fundamental unit of expressed knowledge,
not as a fifth ontological concept.

The following historical terminology is superseded:

```text
Relationship → Relation
Projection   → derived/modeling representation
```

No implementation SHALL introduce these superseded concepts as independent
Core ontology concepts.

## Core Model Boundary

The distinction between ontology and knowledge representation SHALL be
preserved:

```text
Core Ontology
├── Entity
├── Capability
├── Relation
└── State

Knowledge Representation
└── Statement
```

The normative definitions are maintained by the PAS and related formal
specifications.

This ADR records the decision and its historical rationale; it SHALL NOT
duplicate or override the normative specification.

## Relations

Relation is the canonical Core term.

A Relation is a first-class semantic connection between exactly one source
Entity and exactly one target Entity.

Relation State MAY represent the condition of the relationship itself.

The detailed Relation model is defined by PAS-006 and the corresponding
formal specifications.

## Derived Representations

Projection, View, Zoom, diagrams, inventories, and similar constructs are
derived representations or modeling operations.

They SHALL NOT be treated as independent Core ontology concepts.

They do not create a competing authoritative knowledge model.

## Consequences

### Positive

- Core terminology is consistent across the architecture and formal
  specifications.
- The Core remains minimal and domain-independent.
- Historical terminology is explicitly accounted for.
- The PAS remains the single normative source of truth.
- Existing implementations can map older terminology to the current model.

### Trade-offs

- Historical documents may contain terminology that differs from the current
  model.
- Implementations based on superseded terminology may require migration.
- The distinction between ontology and expressed knowledge must remain clear.

## Relationship to Other ADRs

This ADR consolidates the current Core terminology and model boundaries.

Earlier decisions remain valuable as historical records of the architectural
evolution. They SHALL NOT be interpreted as alternative current definitions
when they conflict with the current PAS.

The current PAS and later accepted ADRs take precedence over superseded
terminology.

## Migration Rule

Where an older document, example, or implementation uses:

```text
Relationship
```

it SHALL be interpreted as:

```text
Relation
```

where the semantic context is the Persiqa Core.

Where an older document uses:

```text
Projection
```

the term SHALL be interpreted according to its current role as a derived
representation or modeling operation unless that document explicitly records
a different historical decision.

## Authority

The authority hierarchy is:

1. Current normative PAS/PDS/PMS/PRS/PCS specifications.
2. Accepted current ADRs.
3. RAS rationale documents.
4. PDR historical discovery records.
5. Historical or superseded terminology.

If an ADR conflicts with a normative specification, the specification is
authoritative.

## Status

**Accepted**

This ADR establishes the consolidated Core terminology for the current
Persiqa architecture.
