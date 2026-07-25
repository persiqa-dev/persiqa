# ADR-0002: Entity Continuity Model

**Status:** Accepted

**Date:** 2026-07-25

## Context

Persiqa models meaningful infrastructure concepts rather than physical
assets.

These concepts evolve over time. Hardware may be replaced, names may
change, locations may change, and additional knowledge may be
discovered.

To preserve knowledge consistently, the platform requires a stable
notion of continuity that is independent of mutable properties and
implementation details.

------------------------------------------------------------------------

## Decision

Persiqa distinguishes **continuity** from **identity**.

Continuity is the domain concept.

Identity is the technical representation of that continuity.

The platform exists to preserve the continuity of meaningful
infrastructure concepts.

------------------------------------------------------------------------

## Principle 1 -- Continuity Represents Persistence

Continuity represents the ongoing existence of an infrastructure
concept.

It answers the question:

> *"Are we still talking about the same thing?"*

It does not attempt to describe the physical composition of that thing.

------------------------------------------------------------------------

## Principle 2 -- Identity Represents Continuity

Every entity has a stable identity.

Identity is immutable throughout the lifetime of an entity.

The concrete representation of identity (UUID, ULID, or another
mechanism) is an implementation detail.

------------------------------------------------------------------------

## Principle 3 -- Continuity Is a Domain Decision

Determining whether something is the same entity or a new one is a
domain decision.

It cannot always be inferred automatically from observed changes.

Capabilities and users may provide the knowledge required to establish
continuity.

------------------------------------------------------------------------

## Principle 4 -- Change Does Not Imply Replacement

The following changes do not necessarily create a new entity:

-   Renaming
-   Relocation
-   Hardware replacement
-   Metadata updates
-   Discovery of new relationships
-   Correction of previous knowledge

If the infrastructure concept continues to exist, its continuity is
preserved.

------------------------------------------------------------------------

## Principle 5 -- External Identifiers Are References

Serial numbers, asset identifiers, MAC addresses, hostnames, inventory
numbers, QR codes, and similar values are references.

They may help recognize an entity but they do not define its continuity.

------------------------------------------------------------------------

## Principle 6 -- Knowledge Evolves

Knowledge about an entity is expected to evolve.

New observations enrich existing entities whenever continuity is
preserved.

The platform preserves accumulated knowledge across those changes.

------------------------------------------------------------------------

## Consequences

The graph remains stable while infrastructure evolves.

Hardware can be replaced without unnecessarily fragmenting knowledge.

Imported information can enrich existing entities instead of creating
duplicates.

Capabilities may implement sophisticated matching strategies, but
continuity itself remains a domain concept rather than an implementation
concern.

------------------------------------------------------------------------

## Guiding Rule

> If the infrastructure concept continues to exist, its continuity
> should be preserved, even when its implementation, properties, or
> physical components evolve.
