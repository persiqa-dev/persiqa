# ADR-0000: Persiqa Design Principles

**Status:** Accepted
**Date:** 2026-07-25

## Purpose

This document defines the fundamental design principles of Persiqa.

Unlike other ADRs, this document is not a single architectural decision. It
is a living record of the principles that guide architectural decisions and
the evolution of the project.

ADR-0000 does not have authority above the normative Persiqa specifications.
It provides architectural context and principles; the current normative
specifications define the resulting architecture.

Every future ADR should either reinforce these principles or explicitly
justify why an exception is necessary.

> **Persist infrastructure knowledge.**

------------------------------------------------------------------------

## Principle 1 -- Knowledge First

Persiqa is not an inventory system.

Persiqa is an infrastructure knowledge platform.

Its purpose is not to describe perfect reality, but to preserve and
continuously improve our understanding of it.

The objective is not to store data.

The objective is to preserve knowledge.

------------------------------------------------------------------------

## Principle 2 -- Reality Is a Graph

Infrastructure is inherently a graph.

Persiqa models that reality instead of transforming it into hierarchical
or relational structures.

The graph is a domain concept, not a storage or implementation decision.

Every other representation is a projection of this graph.

There is never a second authoritative model.

------------------------------------------------------------------------

## Principle 3 -- Relationships Matter More Than Attributes

Entities gain meaning through their relationships.

Relations are first-class citizens of the domain model.

Context is created by connections rather than isolated metadata.

------------------------------------------------------------------------

## Principle 4 -- Knowledge Is Progressive

Knowledge is discovered incrementally.

Incomplete knowledge is valid knowledge.

Unknown is a valid state.

The system must never require complete information before knowledge can
be stored.

Each new discovery enriches the existing graph rather than replacing it.

Persiqa continuously refines its understanding of reality.

------------------------------------------------------------------------

## Principle 5 -- Minimal Required Information

Users should only provide information they actually know.

The platform must never require artificial placeholder values.

Descriptive information SHALL be considered optional unless it is required
by the applicable semantic model, Core invariant, or capability.

------------------------------------------------------------------------

## Principle 6 -- Everything Is Extensible

The model must support future discoveries without redesign.

New entities, relations and capabilities should enrich the model instead of
forcing migrations.

The architecture embraces evolution.

------------------------------------------------------------------------

## Principle 7 -- Views Are Projections

Floor plans.

Rack layouts.

Power trees.

Topology diagrams.

Dependency maps.

Inventories.

All are projections of the same graph.

Views never own authoritative knowledge.

------------------------------------------------------------------------

## Principle 8 -- Capabilities Own Their Knowledge

Persiqa is extended through capabilities.

Each capability introduces its own entities, relations and semantics.

Capabilities depend on the platform, not on each other.

The Core remains stable while capabilities evolve independently.

------------------------------------------------------------------------

## Principle 9 -- Stable Identity

Identity represents the continuity of an infrastructure concept.

Metadata is mutable.

Relations evolve.

Identity remains stable while knowledge grows.

------------------------------------------------------------------------

## Principle 10 -- Context Over Completeness

Context is more valuable than completeness.

A partially understood graph with accurate relations is more valuable than a
perfectly filled spreadsheet of disconnected assets.

The graph represents the best understanding available at a given point in
time.

------------------------------------------------------------------------

## Principle 11 -- Domain Before Technology

Architectural decisions should be expressed in domain language rather than
implementation language.

The domain model drives the architecture.

Technology serves the domain, never the other way around.

------------------------------------------------------------------------

## Principle 12 -- Architecture Before Implementation

Technology choices are temporary.

Frameworks, databases, storage engines, APIs and user interfaces may evolve.

The architectural principles defined in this document should remain stable
over the lifetime of the project.

------------------------------------------------------------------------

## Authority

ADR-0000 records architectural principles and provides guidance for
architectural decisions.

It does **not** override the normative Persiqa specifications.

The authority hierarchy is:

1. **Current normative specifications**
   - PAS — Persiqa Architecture Specification
   - PDS — Persiqa DSL Specification
   - PMS — Persiqa Meta Model Specification
   - PRS — Persiqa Reasoning Specification
   - PCS — Persiqa Conformance Specification
2. **Accepted ADRs**
3. **RAS rationale documents**
4. **PDR historical discovery records**
5. **Examples, reference models, and other supporting documentation**

If this ADR conflicts with a current normative specification, the normative
specification takes precedence.

ADR-0000 SHALL NOT be interpreted as a constitutional document with authority
above the normative specifications.

## Relationship to the PAS

The PAS defines the current normative architecture.

ADR-0000 explains the principles that guide that architecture but does not
duplicate its normative definitions.

When the architecture evolves, the PAS SHALL be updated as the authoritative
description of the resulting architecture.

This ADR MAY be updated when the architectural principles themselves change,
provided that the change is recorded explicitly.

## Status

**Accepted**
