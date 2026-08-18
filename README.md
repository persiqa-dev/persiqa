# Persiqa

Persiqa is a technology-independent architecture and knowledge modeling
system for representing infrastructure and other complex systems.

Persiqa provides a small universal Core model that can be refined and extended
across domains while preserving a common semantic foundation.

## Repository Structure

```text
docs/
├── architecture/
│   ├── adr/        Architecture Decision Records
│   ├── pas/        Persiqa Architecture Specification
│   ├── pdr/        Persiqa Design Records
│   └── ras/        Rationale and architectural reasoning
│
├── specification/
│   ├── pcs/        Persiqa Conformance Specification
│   ├── pds/        Persiqa DSL Specification
│   ├── pms/        Persiqa Meta Model Specification
│   └── prs/        Persiqa Reasoning Specification
│
├── examples/
│   └── pex/        Persiqa Examples
│
├── reference/
│   └── ref/        Reference Models
│
├── glossary/
│   └── pgl/        Persiqa Glossary
│
└── vision/         Project vision and direction
```

## Documentation Authority

The repository is the authoritative documentation source for Persiqa.

The normative specifications are:

1. **PAS** — Persiqa Architecture Specification
2. **PDS** — Persiqa DSL Specification
3. **PMS** — Persiqa Meta Model Specification
4. **PRS** — Persiqa Reasoning Specification
5. **PCS** — Persiqa Conformance Specification

Other documentation has supporting or historical roles:

- **ADR** records architectural decisions.
- **RAS** records architectural rationale.
- **PDR** preserves historical discovery and design reasoning.
- **PGL** provides canonical terminology.
- **PEX** provides examples.
- **REF** provides reference models.
- **Vision** describes project direction.

Supporting documentation SHALL NOT override the normative specifications.

## Core Model

The current Persiqa Core ontology consists of four fundamental ontological
concepts:

```text
Entity
Capability
Relation
State
```

The Canonical Knowledge Model (CKM) contains five first-class object types:

```text
Entity
Capability
Relation
State
Statement
```

Statement is the fundamental unit of expressed knowledge. It is a first-class
CKM object, but it is not a fifth Core ontology concept.

## Design Principles

Persiqa is designed around the following principles:

- universal and domain-independent Core;
- technology-independent semantic model;
- first-class semantic Relations;
- progressive refinement;
- explicit preservation of infrastructure knowledge;
- one authoritative knowledge model;
- human-readable semantics.

## Status

Persiqa is under active development.

The repository documents the current architectural and specification state.
Historical ADR and PDR documents may describe earlier states of the model;
the current normative specifications define the current state.
