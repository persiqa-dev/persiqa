# PAS-004 --- Statement Model

**Document:** Persiqa Architecture Specification (PAS)

**Chapter:** PAS-004

**Title:** Statement Model

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the Statement as the fundamental unit of knowledge
in Persiqa.

Every fact represented by the Persiqa Core SHALL be expressed as one or
more Statements.

------------------------------------------------------------------------

# 2. Definition

A Statement is a declarative assertion describing knowledge about the
model.

A Statement SHALL express a fact that is considered true within the
current knowledge of the model.

Statements are implementation independent.

------------------------------------------------------------------------

# 3. Statement Primacy

The Statement is the canonical representation of knowledge.

The ontology interprets Statements.

Reasoning consumes Statements and produces new Statements.

Persistence stores Statements.

Views present Statements.

No implementation SHALL bypass the Statement model.

------------------------------------------------------------------------

# 4. Canonical Form

The canonical representation of a Statement is:

```text
Subject Predicate Object
```

Where:

- Subject SHALL identify an Entity or Relation.
- Predicate SHALL define the semantic meaning of the Statement.
- Object SHALL represent either:
  - another Entity,
  - a Capability,
  - a State value,
  - or another value defined by the Predicate.

The Subject MAY identify a Relation when the Statement describes the
relationship itself.

A Statement SHALL NOT implicitly promote a Capability or State into a
Subject unless a future normative specification explicitly extends this
rule.

------------------------------------------------------------------------

# 5. Statement Categories

## Capability Statement

Describes what an Entity is capable of.

Examples:

```text
Pipe transports Water.
Battery stores Electrical Energy.
Sensor measures Temperature.
```

------------------------------------------------------------------------

## Relation Statement

Describes a relationship between Entities.

Examples:

```text
Camera connected_to Switch.
Room composed_of Wall.
Application runs_on Virtual Machine.
```

------------------------------------------------------------------------

## Relation State Statement

Describes the current condition or property of a Relation.

Examples:

```text
Link Speed = 1 Gbps.
Link RSSI = -58 dBm.
```

The Subject of such a Statement MAY identify the Relation whose state or
property is being described.

------------------------------------------------------------------------

## State Statement

Describes the current condition of an Entity.

Examples:

```text
Camera is Online.
Battery Charge = 82%.
```

------------------------------------------------------------------------

# 6. Statement Semantics

A Statement SHALL describe knowledge.

A Statement SHALL NOT describe implementation.

A Statement SHALL NOT describe user interface behaviour.

A Statement SHALL NOT encode storage details.

The semantic interpretation of a Statement SHALL remain independent of
its textual or serialized representation.

------------------------------------------------------------------------

# 7. Statement Lifecycle

A Statement MAY be:

- created,
- refined,
- superseded,
- retired.

Refinement SHALL preserve Incremental Truth.

Superseding a Statement SHALL be explicit.

------------------------------------------------------------------------

# 8. Statement Consistency

Two Statements MAY coexist if they describe different aspects of the
same Entity or Relation.

Example:

```text
Camera connected_to Switch.
Camera located_in Garage.
Camera captures Image.
Camera is Online.
```

These Statements are complementary rather than conflicting.

------------------------------------------------------------------------

# 9. Statement Validation

A valid Statement SHALL satisfy all of the following:

- The Subject SHALL reference an Entity or Relation.
- The Predicate SHALL be semantically defined.
- The Object SHALL be valid for the Predicate.
- The Statement SHALL comply with the Core Ontology.
- The Statement SHALL preserve the Core Laws.

------------------------------------------------------------------------

# 10. Rationale

The Statement model separates knowledge representation from
implementation.

By making Statements the single unit of knowledge, every layer of the
architecture shares the same semantic foundation.

This enables multiple implementations, reasoning engines, persistence
technologies and user interfaces to operate on the same model without
changing its meaning.
