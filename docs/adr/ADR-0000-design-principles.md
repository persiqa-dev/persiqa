# ADR-0000: Persiqa Design Principles

**Status:** Accepted

**Date:** 2026-07-25

## Purpose

This document defines the fundamental design principles of Persiqa.

Unlike other ADRs, this document is not a single architectural decision.
It is the constitutional document of the project from which all future
architectural decisions derive.

ADR-0000 is intentionally a living document. As the understanding of the
platform matures, this document may be refined while preserving its core
philosophy.

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

Relationships are first-class citizens of the domain model.

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

Everything except identity should be considered optional unless a
capability explicitly requires it.

------------------------------------------------------------------------

## Principle 6 -- Everything Is Extensible

The model must support future discoveries without redesign.

New entities, relationships and capabilities should enrich the model
instead of forcing migrations.

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

Views never own data.

------------------------------------------------------------------------

## Principle 8 -- Capabilities Own Their Knowledge

Persiqa is extended through capabilities.

Each capability introduces its own entities, relationships and
semantics.

Capabilities depend on the platform, not on each other.

The core remains stable while capabilities evolve independently.

------------------------------------------------------------------------

## Principle 9 -- Stable Identity

Identity represents the continuity of an infrastructure concept.

Metadata is mutable.

Relationships evolve.

Identity remains stable while knowledge grows.

------------------------------------------------------------------------

## Principle 10 -- Context Over Completeness

Context is more valuable than completeness.

A partially understood graph with accurate relationships is more
valuable than a perfectly filled spreadsheet of disconnected assets.

The graph represents the best understanding available at a given point
in time.

------------------------------------------------------------------------

## Principle 11 -- Domain Before Technology

Architectural decisions should be expressed in domain language rather
than implementation language.

The domain model drives the architecture.

Technology serves the domain, never the other way around.

------------------------------------------------------------------------

## Principle 12 -- Architecture Before Implementation

Technology choices are temporary.

Frameworks, databases, storage engines, APIs and user interfaces may
evolve.

The architectural principles defined in this document should remain
stable over the lifetime of the project.
