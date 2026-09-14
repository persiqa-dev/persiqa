# PAS-003 — Core Ontology

**Document:** Persiqa Architecture Specification (PAS)  
**Chapter:** PAS-003  
**Title:** Core Ontology  
**Status:** Accepted

---

# 1. Purpose

This chapter defines the normative ontology of the Persiqa Core.

The Persiqa Core ontology consists of exactly four fundamental ontological concepts:

- **Entity**
- **Capability**
- **Relation**
- **State**

These four concepts define what may exist as an ontological element in the
Persiqa domain model.

**Statement is not a fifth ontological concept.** A Statement is the
fundamental unit of expressed knowledge about the ontology and is defined by
the Knowledge Model and formalized by the Persiqa Meta Model and DSL
specifications.

No additional ontological concepts are defined by the Core.

This distinction is intentional:

```text
Core Ontology
├── Entity
├── Capability
├── Relation
└── State

Knowledge Representation
└── Statement
```

The distinction SHALL be preserved by all conforming implementations.

---

# 2. Ontology Overview

The ontology intentionally separates:

- **identity** — Entity
- **ability** — Capability
- **relationship** — Relation
- **condition** — State

Each ontological concept has a distinct responsibility.

A Statement expresses knowledge involving these concepts but does not add a
new category to the Core ontology.

This separation SHALL be preserved by every implementation.

---

# 3. Entity

## Definition

An Entity is an independently identifiable model element.

An Entity represents something that exists within the scope of the model and
has its own identity.

## Characteristics

An Entity:

- SHALL have identity.
- MAY be explicitly associated with Capabilities.
- MAY own States.
- MAY participate in Relations.
- MAY be refined.

## Non-characteristics

An Entity is NOT:

- an implementation class,
- a database record,
- a UI widget,
- a protocol,
- a technology.

## Examples

- Camera
- Pipe
- Valve
- Room
- Rack
- Virtual Machine
- Home Assistant

---

# 4. Capability

## Definition

A Capability describes what an Entity is capable of.

Capabilities are timeless.

They describe potential, not current behaviour.

## Characteristics

A Capability:

- SHALL be explicitly associated with one or more Entities when a concrete
  capability association is modeled; reusable capability concepts MAY be
  associated with multiple Entities.
- SHALL describe ability.
- SHALL NOT describe current execution.
- MAY be refined.

## Core Capability Families

The current universal Capability families are:

- Transport
- Store
- Transform
- Measure
- Control

Domain-specific capabilities SHALL refine these families rather than replace
them.

---

# 5. Relation

## Definition

A Relation describes a semantic relationship between endpoints permitted by
its declared Relation Type.

Relations are first-class model elements.

A Relation has one defined source endpoint and one defined target endpoint.
The Relation Type declares the permitted endpoint kinds and semantic rules.

## Characteristics

A Relation:

- SHALL connect declared, valid endpoints.
- SHALL have semantics.
- MAY own State.
- MAY be refined.
- SHALL be independently addressable.

## Examples

- connected_to
- composed_of
- located_in
- mounted_on
- runs_on

Relation names are domain semantics.

The Core recognizes only the concept of Relation; individual relation names
are not additional Core ontology concepts.

---

# 6. State

## Definition

A State represents a contextual condition of an Entity or Relation.

State is temporal.

## Characteristics

A State:

- MAY change over time.
- SHALL NOT define an independent continuity identity. Its identity is
  contextual to owner, semantic predicate, and applicable context.
- SHALL NOT define Capability.
- MAY belong to an Entity.
- MAY belong to a Relation.

## Examples

Entity State:

- Online
- Running
- Temperature = 21°C
- Charge = 82%

Relation State:

- Link Speed = 1 Gbps
- RSSI = -58 dBm
- Flow Rate = 18 l/min

---

# 7. Statement

Statement is the fundamental unit of expressed knowledge in Persiqa.

A Statement describes a fact or piece of knowledge involving the Core ontology.

Statement is deliberately distinguished from the ontology itself:

- Entities, Capabilities, Relations, and States define the ontological
  elements being modeled.
- Statements express knowledge about those elements.

A Statement therefore SHALL NOT be interpreted as an additional ontological
category.

The normative structure and serialization of Statements are defined by the
Persiqa Meta Model and Persiqa DSL specifications.

---

# 8. Relationships Between Core Concepts

The Core concepts interact according to the following rules.

```text
Entity
 ├── associates with Capability
 ├── owns State
 └── participates in Relation

Relation
 ├── connects Entity
 └── may own State

Capability
 └── is explicitly associated with Entity

State
 └── belongs to Entity or Relation

Statement
 └── expresses knowledge about the above concepts
```

Statement provides the knowledge representation layer without expanding the
ontology.

---

# 9. Ontological Constraints

**OC-001**

Every Entity SHALL have identity.

**OC-002**

Every concrete Capability association SHALL be explicit.

**OC-003**

Every Relation SHALL connect endpoints permitted by its Relation Type.

**OC-004**

Every State SHALL belong to either an Entity or a Relation.

**OC-005**

Capabilities SHALL remain timeless.

**OC-006**

States SHALL remain temporal.

**OC-007**

Refinement SHALL preserve ontology consistency.

**OC-008**

Statement SHALL NOT be treated as an additional ontological concept.

**OC-009**

Domain-specific concepts SHALL be introduced through refinement and
Capabilities rather than by extending the Core ontology.

---

# 10. Rationale

The ontology intentionally remains minimal.

The four ontological concepts provide the universal vocabulary required to
describe identity, ability, relationship, and condition across domains.

Statement is separated from the ontology because it represents expressed
knowledge rather than another kind of modeled thing.

This distinction allows Persiqa to preserve the Statement-first knowledge
model without turning Statement into an additional ontological category.

All domain knowledge SHALL be expressed by refining the four Core ontology
concepts and by composing them into Statements rather than by extending the
Core ontology with domain-specific concepts.

This rule has been validated across multiple independent domains including
IT, OT, electrical systems, water systems, HVAC, robotics, logistics,
healthcare, and building modelling.


### State Identity

State SHALL NOT define an independent continuity identity. Its identity is
contextual to owner, semantic predicate, and applicable context.
