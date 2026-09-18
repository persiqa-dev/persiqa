# PMS-011 — Semantic Query Contract

**Document:** Persiqa Meta Model Specification (PMS)  
**Chapter:** PMS-011  
**Title:** Semantic Query Contract  
**Status:** Draft  
**Scope:** read-only semantic traversal of canonical knowledge; not CKM ontology, inference, persistence, or representation layout

## 1. Purpose

This chapter defines a read-only way to ask a semantic reachability question of
a CKM scope. It formalizes questions such as:

```text
What supplies ElectricBoiler-01?
What depends on MCB-Boiler-01?
```

The query result SHALL be explainable through canonical Relations and
Statements. It SHALL NOT create a new Core primitive, canonical Relation,
Statement, Refinement Binding, or persistent Representation Profile.

## 2. Query input

A semantic traversal request SHALL identify:

| Field | Requirement |
|---|---|
| Scope | An existing scope accessible to the requesting subject. |
| Anchor | One existing canonical Node identity. |
| Relation Type | A registered Relation Type with an explicit composition contract. |
| Direction | `DOWNSTREAM` or `UPSTREAM`. |
| Maximum hops | A positive, bounded limit on witness-path length. |

`DOWNSTREAM` follows the declared source-to-target direction of the Relation.
`UPSTREAM` follows it in reverse. A query SHALL reject an unregistered or
non-composable Relation Type. In particular, `connectedTo` SHALL NOT be used
for transitive semantic traversal merely because its edges are connected.

The initial implementation exposes this as:

```text
GET /api/scopes/{scopeId}/semantic/traversal
    ?anchor=MCB-01
    &relationType=supplies
    &direction=DOWNSTREAM
    &maxHops=20
```

`maxHops` defaults to `20` and SHALL be between `1` and `100`.

## 3. Traversal semantics

The engine SHALL traverse only canonical Relations of the requested Relation
Type. It SHALL use the Relation Type Registry's composition contract as the
authority for whether traversal is permitted.

The engine SHALL terminate cyclic input safely. For every reachable Node within
the requested limit, it SHALL return one deterministic shortest witness path.
The witness path is selected by stable Relation identity ordering when equal
length alternatives exist.

One witness path proves reachability; it SHALL NOT be interpreted as a claim
that no other path exists. A later query form MAY expose all paths or ambiguity
metadata without changing this contract.

When further reachable Nodes exist beyond the hop limit, the result SHALL set
`truncated` to `true`. A non-truncated result SHALL include every Node reachable
within the requested Relation Type and direction.

## 4. Evidence and knowledge context

Each witness step SHALL include:

1. the canonical Relation identity and endpoints; and
2. every canonical Statement with the same subject, predicate, and object as
   that Relation.

The returned Statements retain their original `knowledgeKind`, `derivedFrom`,
and Context. A consumer can therefore distinguish an explicit observation from
a recorded derived assertion and continue to its evidence identities.

The traversal service SHALL return canonical knowledge in its entirety. It
SHALL NOT hide a coarse Relation because a Representation-level Refinement
Binding exists. Refinement affects selected display projections under PMS-010;
it does not change what is canonically known or what may evidence a query.

## 5. Relation to derived knowledge

Transitive reachability is a read-time conclusion, not a persisted derived
Statement. For example, a query may prove that `MCB-01` supplies
`ElectricBoiler-01` through intermediate Relations while no canonical
`MCB-01 supplies ElectricBoiler-01` Relation exists.

A future inference service MAY record such a derived Statement, but it SHALL
create it through the normal statement-first write path and SHALL preserve the
witness Statement identities as `derivedFrom` evidence. Query execution alone
SHALL NOT make that write.

### 5.1 Reviewable derivation proposals

The initial Persiqa derivation service exposes a reviewable transition between
read-time reachability and persisted derived knowledge:

```text
GET  /api/scopes/{scopeId}/semantic/derivation-proposals
POST /api/scopes/{scopeId}/semantic/derivations
```

The proposal endpoint SHALL return only conclusions that satisfy all of the
following:

1. the witness contains at least two canonical Relations;
2. every witness Relation has at least one supporting canonical Statement;
3. the requested Relation Type is composable; and
4. no canonical Relation already has the proposed type, source, and target.

A proposal SHALL identify the inferred source and target, its witness Relation
identities, and the complete list of supporting Statement identities. It is
application output, not a persisted CKM object.

Acceptance SHALL recompute the proposal from current canonical knowledge; it
SHALL NOT accept an evidence list supplied by the client. If the proposal is no
longer current, the request SHALL fail without writing knowledge. A successful
acceptance SHALL use the statement-first recording service to write one
`DERIVED` Statement with the proposal's Statement identities as `derivedFrom`.

Automatic Representation Refinement detection applies only to explicit
observations. A derived transitive conclusion SHALL NOT be reclassified as a
coarse observation and hidden by a Refinement Binding.

## 6. Initial conformance cases

Given:

```text
MCB-01 ─supplies─► JunctionBox-01 ─supplies─► ElectricBoiler-01
```

then a downstream `supplies` query anchored at `MCB-01` SHALL find:

| Target | Hops | Witness Relations |
|---|---:|---|
| `JunctionBox-01` | 1 | `MCB-01 → JunctionBox-01` |
| `ElectricBoiler-01` | 2 | both Relations in order |

An upstream query anchored at `ElectricBoiler-01` SHALL traverse the same
canonical Relations in reverse orientation. A `maxHops=1` request SHALL return
only `JunctionBox-01` and report a truncated result.
