# ADR-0001: Core Domain Model

**Status:** Accepted
**Date:** 2026-07-25

## Context

Infrastructure is inherently composed of interconnected concepts.

Persiqa exists to preserve infrastructure knowledge as a technology-independent
knowledge model. The model must remain stable across storage technologies,
APIs, user interfaces, and other implementation choices.

The Core must also remain universal. Domain-specific knowledge must be
expressible without continuously adding domain-specific concepts to the Core.

The architecture therefore requires a minimal domain model capable of
representing infrastructure concepts, their semantic relationships, their
capabilities, their state, and the statements that form the knowledge graph.

## Decision

Persiqa SHALL use a universal, graph-based Core Domain Model.

The model distinguishes between the **Core ontology** and the
**knowledge representation layer**.

The Core ontology consists of exactly four ontological concepts:

- **Entity**
- **Capability**
- **Relation**
- **State**

**Statement** is the fundamental unit of expressed knowledge about these
concepts. It is a first-class CKM object, but it is not a fifth Core
ontological concept.

The resulting distinction is:

```text
Core Ontology
├── Entity
├── Capability
├── Relation
└── State

Knowledge Representation
└── Statement
```

These concepts form the stable foundation of the Persiqa knowledge model.

The normative definitions of the resulting architecture are maintained by
the Persiqa Architecture Specification (PAS). The Canonical Knowledge Model
and its first-class object representation are defined by the Persiqa Meta
Model Specification (PMS).

This ADR records the architectural decision and its boundaries; it does not
duplicate the normative definitions maintained by those specifications.

## Core Domain Model

### Entity

An Entity represents a meaningful concept whose existence and relationships
are worth preserving in the infrastructure knowledge model.

Entities are not limited to physical assets. They may represent physical,
logical, virtual, spatial, or other infrastructure concepts.

Entity identity is stable while knowledge about the Entity evolves.

### Relation

A Relation connects Entities with explicit semantic meaning.

Relations are first-class domain concepts. They are not merely implementation
links between records and may themselves carry semantics and State.

### Capability

A Capability represents an extensible domain-level ability or semantic
extension of the Core.

Capabilities allow domain-specific knowledge to be introduced without
modifying the universal Core ontology.

A Capability may define domain-specific semantics while remaining dependent on
the stable Core.

### State

State represents the current condition of an Entity or Relation.

State is part of the Core ontology because infrastructure knowledge is not
limited to static topology; the current condition of modeled concepts is also
part of the knowledge being preserved.

### Statement

A Statement is the fundamental expression of a fact or piece of knowledge
about the modeled infrastructure.

Statements connect and describe the Core concepts in the knowledge model and
provide the basis for parsing, validation, reasoning, and serialization.

A Statement is a first-class CKM object because it has canonical semantic
representation, but it is deliberately not an additional Core ontology
concept.

## Universal Core

The Core ontology SHALL remain domain-independent.

Domain-specific concepts SHALL be introduced through refinement and
Capabilities rather than by adding domain-specific concepts to the Core
ontology.

A new Core ontological concept SHALL only be introduced when a real use case
cannot be expressed by refining or composing the existing universal Core
concepts.

This rule protects the Core from domain-specific growth while allowing the
model to evolve when genuinely universal requirements are discovered.

## Relations as First-Class Concepts

Relations SHALL be first-class concepts of the domain model.

Infrastructure meaning is frequently determined by how Entities are
connected, rather than by the isolated properties of individual Entities.

Consequently, the model SHALL preserve semantic relationships explicitly.

Relations MAY themselves carry State and additional semantics as defined by
the PAS and PMS.

## Capabilities as the Extension Mechanism

Capabilities SHALL provide the primary mechanism for extending Persiqa with
domain-specific knowledge.

A Capability SHALL build on the universal Core ontology rather than redefine
it.

## Derived Views Are Not Domain Models

Views, projections, inventories, diagrams, layouts, dependency maps, and
other presentations of the knowledge graph SHALL be derived from the
authoritative knowledge model.

They SHALL NOT constitute independent authoritative models.

View, Zoom, and similar operations are modeling or presentation operations,
not additional Core ontology concepts.

## Technology Independence

The Core Domain Model SHALL remain independent of implementation technology.

The Core SHALL NOT depend on storage technologies, databases, persistence
engines, APIs, user interfaces, rendering technologies, visualization
frameworks, or deployment platforms.

## Consequences

### Positive

- Persiqa has a stable and technology-independent domain language.
- New domains can be modeled through refinement and Capabilities.
- Relations remain explicit and semantically meaningful.
- State is part of the knowledge model rather than an implementation detail.
- Statements provide a first-class representation of expressed knowledge
  without expanding the Core ontology.
- Different views can be generated without creating competing models.

### Trade-offs

- The Core deliberately avoids domain-specific abstractions.
- Some advanced domain requirements may initially require refinement rather
  than a dedicated Core ontological concept.
- The distinction between ontology and CKM object types must remain explicit
  in the specifications.

## Relationship to the Specification

This ADR records the architectural decision behind the Core Domain Model.

The **Persiqa Architecture Specification (PAS)** is the normative source for
the resulting architecture.

The **Persiqa Meta Model Specification (PMS)** defines the canonical object
representation of the model, including Statement as a first-class CKM object.

The **Persiqa DSL Specification (PDS)** defines the language representation
of that model.

If this ADR and a normative specification differ in normative detail, the
normative specification takes precedence.

This ADR SHALL NOT be used as a second normative definition of the Core model.

## Related Decisions

This decision is complemented by subsequent architectural decisions covering
progressive discovery, refinement, and other aspects of the Persiqa model.

Those decisions SHALL build on the Core Domain Model defined here and SHALL
NOT silently redefine its fundamental concepts.

## Status

**Accepted**

The Core Domain Model is considered the stable architectural foundation of
Persiqa. Future changes to the Core ontology require evidence that the
existing universal model cannot adequately express a real requirement.
