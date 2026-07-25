# ADR-0001: The Graph Domain Model

**Status:** Accepted

**Date:** 2026-07-25

## Context

Infrastructure is inherently composed of interconnected concepts.

Persiqa aims to preserve infrastructure knowledge, support progressive
discovery, and generate multiple projections from a single source of
truth.

To achieve this, the platform requires a technology-independent domain
model that remains stable regardless of storage engine, API, or user
interface.

------------------------------------------------------------------------

## Decision

The Persiqa domain model is built on four fundamental concepts.

### Entity

An Entity represents a meaningful infrastructure concept whose existence
and relationships are worth preserving.

Entities are not limited to physical assets. Physical devices, logical
constructs, virtual resources, locations, and infrastructure concepts
are all represented as entities.

Examples include:

-   Breaker
-   Cable
-   Outlet
-   Room
-   Rack
-   Switch
-   VLAN
-   IP Subnet
-   DNS Zone
-   Virtual Machine
-   Firewall Rule

Entities have a stable identity and evolve as knowledge about them
grows.

Persiqa models infrastructure concepts rather than physical assets.

------------------------------------------------------------------------

### Relationship

A Relationship connects two entities with explicit semantic meaning.

Relationships are explicitly typed and are first-class citizens of the
domain model.

Context is established through relationships rather than isolated
metadata.

Examples include:

-   feeds
-   connectedTo
-   contains
-   mountedIn
-   belongsTo

------------------------------------------------------------------------

### Capability

A Capability extends the platform by introducing additional entity
types, relationship types, rules, and domain semantics.

Capabilities extend the platform without modifying its architectural
principles.

------------------------------------------------------------------------

### Projection

A Projection is a derived, read-only view of the graph.

Examples include:

-   Inventory
-   Floor Plan
-   Rack Layout
-   Power Tree
-   Dependency View

Projections never own or modify domain data.

They present the graph from a specific perspective.

------------------------------------------------------------------------

## Common Domain Language

The following concepts form the common language of the Persiqa platform:

-   Entity
-   Relationship
-   Capability
-   Projection

Architectural decisions should be expressed using these concepts before
introducing implementation-specific terminology.

------------------------------------------------------------------------

## Explicitly Not Part of the Core Domain Model

The core domain model intentionally has no knowledge of:

-   Storage technologies
-   Databases
-   REST or GraphQL APIs
-   User interfaces
-   Icons
-   Colors
-   Coordinates
-   Rendering
-   Persistence implementations

These are implementation concerns built on top of the domain model.

------------------------------------------------------------------------

## Consequences

The platform exposes a single, consistent domain language.

Capabilities can evolve independently while sharing the same
architectural foundation.

Different persistence technologies, APIs, and user interfaces can be
introduced without changing the core domain model.

Future projections are generated from the same graph instead of
maintaining independent representations.

The platform models meaningful infrastructure concepts rather than
inventories of physical assets.
