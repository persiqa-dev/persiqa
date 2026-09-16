# PMS-010 — Representation Contract

**Document:** Persiqa Meta Model Specification (PMS)  
**Chapter:** PMS-010  
**Title:** Representation Contract  
**Status:** Draft  
**Scope:** derived views and projection rules; not CKM ontology, canonicalization, persistence, or UI layout  

## 1. Purpose

This chapter defines how a client MAY derive multiple consistent representations
from one CKM scope. It resolves the representation-contract open question in
PMS-008 without introducing a Core ontology primitive or a parallel canonical
model.

It is especially intended for progressive-refinement cases. A coarse assertion
and the more detailed path discovered later MAY both remain canonical knowledge,
while a selected representation shows only the resolution appropriate to the
reader's current view. The first projection strategy SHALL be runtime
aggregation over declared Relation Type composition rules.

## 2. Separation from canonical knowledge

A representation SHALL be derived from canonical Relations and Statements. It
SHALL NOT create, delete, weaken, replace, or otherwise alter canonical
knowledge.

The following separation is mandatory:

| Concern | Canonical layer | Representation layer |
|---|---|---|
| What is known | Nodes, Relations, Statements, context | Selected nodes and edges |
| Why it is known | Statement provenance, evidence, confidence | Optional display of that evidence |
| Refinement fact | Explicitly declared refinement binding | Which resolution is visible |
| Coordinates, pan, zoom | Not canonical | Presentation-only user preference |

An implementation SHALL NOT treat a saved graph coordinate, filter, zoom level,
or edge aggregation as Entity, Relation, State, or Statement semantics.

## 3. Representation profile

A **Representation Profile** is a named, non-canonical set of projection rules.
It MAY select Relation Types, object kinds, context filters, and a requested
resolution. Profiles MAY be system-provided or user-defined.

Each profile SHALL identify:

1. its stable profile identifier within its owner and scope;
2. the source CKM scope;
3. its projection filters; and
4. the requested resolution.

The profile itself is not a new Core object. A persistence implementation MAY
store it as application configuration. It SHALL keep it separate from
canonical Relation and Statement storage.

The initial resolution vocabulary is:

| Resolution | Meaning |
|---|---|
| `OVERVIEW` | Prefer an eligible coarse Relation. |
| `DETAIL` | Prefer the declared detailed path for an eligible coarse Relation. |
| `COMPLETE` | Show all selected canonical Relations, including both coarse and detailed ones. |

`COMPLETE` is the conservative default when no binding or profile rule applies.

## 4. Runtime aggregation

A projection engine MAY calculate a virtual overview edge from an ordered path
only when the effective Relation Type contract explicitly permits that
composition. The virtual edge is presentation output; it SHALL NOT create a
canonical Relation or Statement.

For a composable `supplies` path:

```text
MCB-01 supplies JunctionBox-01
JunctionBox-01 supplies BoilerCircuit-01
```

an overview MAY show `MCB-01 supplies BoilerCircuit-01` while hiding the
intermediate Entity. At detail, the client SHALL reveal the canonical path.

The engine SHALL NOT aggregate a path merely because graph edges connect. It
SHALL apply the Relation Type composition policy, selected context, direction,
and profile filters. It SHALL avoid cycles and SHALL fall back to `COMPLETE`
when several admissible paths make an overview ambiguous.

`connectedTo`, `dependsOn`, and any Relation Type without an explicit
composition contract SHALL NOT be aggregated generically.

## 5. Explicit refinement binding

A **Refinement Binding** declares that one coarse canonical Relation has one
or more ordered detailed paths that may represent it at a higher resolution.
It is representation metadata, not an inferred CKM Relation and not a new Core
primitive.

A binding SHALL contain:

| Field | Requirement |
|---|---|
| Coarse relation identity | One existing canonical Relation. |
| Detailed relation identities | One or more existing canonical Relations in traversal order. |
| Scope identity | The scope containing every referenced Relation. |
| Declared by | Auditable actor or system identity. |
| Declared at | Timestamp of the declaration. |

The detailed path SHALL start at the coarse Relation's source and end at its
target. Each adjacent detailed Relation SHALL share an endpoint in the declared
direction. The path SHALL be semantically admissible according to the relevant
Relation Type composition policy.

A binding for `supplies` SHALL be accepted only when the registry declares the
specific detailed path composable to `supplies`. A graph path alone SHALL NOT
be sufficient evidence of refinement.

The binding declares display substitutability only. It SHALL NOT assert that the
coarse Relation is false, obsolete, deleted, derived, or identical to any
detailed Relation.

## 6. Projection rules

For every Relation selected by a profile, a conforming projection SHALL apply
these rules in order:

1. If the resolution is `COMPLETE`, include every selected canonical Relation.
2. At `OVERVIEW`, the engine MAY replace an eligible composable path with a
   virtual summary edge and omit its intermediate nodes.
3. At `DETAIL`, include the canonical path Relations and intermediate nodes.
4. An applicable Refinement Binding MAY override the runtime path selection.
5. If multiple bindings or paths apply, the implementation SHALL either select a
   user-declared binding or show `COMPLETE`; it SHALL NOT choose silently from
   ambiguous alternatives.

Omitted topology SHALL remain reachable through the Relation and Statement
inspectors. A representation SHOULD indicate that more or less detail is
available, without fabricating semantic facts.

## 7. Zoom interaction

Zoom is a presentation control. It MAY request a resolution from the active
Representation Profile, but it SHALL NOT itself infer a refinement binding.

An initial graph client MAY use the following mapping:

| View scale | Requested resolution |
|---:|---|
| below `0.75` | `OVERVIEW` |
| `0.75` through below `1.35` | profile default, normally `COMPLETE` |
| `1.35` or above | `DETAIL` |

These thresholds are non-normative UI defaults. A client MAY expose explicit
resolution controls instead of, or in addition to, zoom-driven selection.

## 8. Electrical refinement example

Initially, an inspection records:

```text
R-01: MCB-Boiler-01 supplies BoilerCircuit-01
```

Later inspection establishes:

```text
R-02: MCB-Boiler-01 supplies JunctionBox-Boiler-01
R-03: JunctionBox-Boiler-01 supplies BoilerCircuit-01
```

The following binding is then declared:

```text
coarseRelation: R-01
detailedPath:   [R-02, R-03]
```

The result is:

```text
OVERVIEW:  MCB-Boiler-01 ──supplies──► BoilerCircuit-01
DETAIL:    MCB-Boiler-01 ──supplies──► JunctionBox-Boiler-01
                                      ──supplies──► BoilerCircuit-01
COMPLETE:  both canonical descriptions
```

All three views are projections of the same scope. The binding does not remove
the original statement or its provenance.

## 9. Validation and failure handling

An implementation SHALL reject a binding that references a missing Relation,
crosses scope boundaries, has a discontinuous path, or violates a declared
composition policy.

When canonical knowledge changes and makes a formerly valid binding invalid,
the implementation SHALL retain the canonical knowledge. It SHALL mark the
binding unusable and fall back to `COMPLETE` until a user or authorized process
repairs it.

## 10. Open questions

The following remain deliberately open:

1. persistence and REST contract for Representation Profiles and Refinement
   Bindings;
2. authorization and sharing rules for user, team, and system profiles;
3. support for branching or alternative detailed paths;
4. aggregation of several Relations into one overview edge;
5. visual treatment of Relation-owned State and contextual qualifiers.

These questions SHALL NOT change the CKM Core or weaken PMS-008 identity,
Statement, and Relation Type rules.
