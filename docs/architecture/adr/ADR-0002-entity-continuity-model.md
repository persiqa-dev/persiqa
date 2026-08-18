# ADR-0002: Entity Continuity Model

**Status:** Accepted

**Date:** 2026-07-25

## Context

Persiqa models meaningful infrastructure concepts rather than physical
assets.

These concepts evolve over time. Hardware may be replaced, names may
change, locations may change, and additional knowledge may be discovered.

To preserve knowledge consistently, the platform requires a stable notion
of continuity that is independent of mutable properties and implementation
details.

## Decision

Persiqa distinguishes **continuity** from **canonical identity**.

Continuity is the semantic persistence of a meaningful infrastructure
concept over time.

Canonical Identity is the stable model-level representation of that
continuity within the Canonical Knowledge Model.

The concrete representation of canonical identity, such as a UUID, ULID,
or another identifier format, is an implementation detail. The existence
and semantics of canonical identity are not implementation details.

The platform exists to preserve the continuity of meaningful infrastructure
concepts through stable canonical identity.

------------------------------------------------------------------------

## Principle 1 -- Continuity Represents Persistence

Continuity represents the ongoing existence of an infrastructure concept.

It answers the question:

> *"Are we still talking about the same thing?"*

It does not attempt to describe the physical composition of that thing.

------------------------------------------------------------------------

## Principle 2 -- Canonical Identity Represents Continuity

Every first-class CKM object has a stable canonical identity.

For an Entity, canonical identity represents the continuity of the modeled
Entity across changes.

Canonical identity is immutable throughout the lifetime of the modeled
object.

The concrete representation of identity, such as UUID or ULID, is an
implementation detail.

------------------------------------------------------------------------

## Principle 3 -- Continuity Is a Domain Decision

Determining whether something is the same Entity or a new one is a domain
decision.

It cannot always be inferred automatically from observed changes.

Capabilities and users may provide the knowledge required to establish
continuity.

------------------------------------------------------------------------

## Principle 4 -- Change Does Not Imply Replacement

The following changes do not necessarily create a new Entity:

- Renaming
- Relocation
- Hardware replacement
- Metadata updates
- Discovery of new relationships
- Correction of previous knowledge

If the infrastructure concept continues to exist, its continuity is
preserved and its canonical identity remains unchanged.

------------------------------------------------------------------------

## Principle 5 -- External Identifiers Are References

Serial numbers, asset identifiers, MAC addresses, hostnames, inventory
numbers, QR codes, and similar values are references.

They may help recognize an Entity but they do not define its canonical
identity or determine continuity by themselves.

------------------------------------------------------------------------

## Principle 6 -- Knowledge Evolves

Knowledge about an Entity is expected to evolve.

New observations enrich existing Entities whenever continuity is preserved.

The platform preserves accumulated knowledge across those changes.

------------------------------------------------------------------------

## Consequences

The graph remains stable while infrastructure evolves.

Hardware can be replaced without unnecessarily fragmenting knowledge.

Imported information can enrich existing Entities instead of creating
duplicates.

Capabilities may implement sophisticated matching strategies, but
continuity itself remains a domain concept rather than an implementation
concern.

Canonical identity remains part of the model semantics while its concrete
encoding remains an implementation concern.

------------------------------------------------------------------------

## Guiding Rule

> If the infrastructure concept continues to exist, its continuity should
> be preserved and its canonical identity should remain stable, even when
> its implementation, properties, or physical components evolve.
