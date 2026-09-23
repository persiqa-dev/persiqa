# PMS-012 — Power Impact Analysis Contract

**Document:** Persiqa Meta Model Specification (PMS)  
**Chapter:** PMS-012  
**Title:** Power Impact Analysis Contract  
**Status:** Draft  
**Scope:** Read-only, counterfactual electrical-supply analysis; not CKM ontology, State management, inference, persistence, or topology representation

## 1. Purpose

This chapter defines a read-only question over an electrical supply topology:

```text
If MCB-01 is switched off, which modeled devices lose every known power feed?
```

The result SHALL be a temporary analysis result. It SHALL NOT create, update,
or retire a `State`, `Relation`, `Statement`, Refinement Binding, or
Representation Profile. In particular, performing the analysis SHALL NOT
record `MCB-01` as `Off`.

This contract is deliberately narrower than PMS-011. A semantic traversal
proves that one path exists. A power-impact analysis determines whether any
known physical supply path remains after one selected Node is removed.

## 2. Query input

A request SHALL identify an accessible scope and one existing Node identity
called the *interrupted Node*.

The initial implementation exposes:

```text
GET /api/scopes/{scopeId}/analysis/power-impact?interrupted=MCB-01
```

The interrupted Node SHALL participate in a composable `supplies` topology.
An unknown Node SHALL cause the request to fail rather than be interpreted as
an empty impact.

## 3. Physical supply topology

The analysis SHALL consider only canonical `supplies` Relations with physical
support:

1. a Relation with at least one `EXPLICIT` supporting Statement is physical;
2. a Relation without a supporting Statement MAY be treated as physical to
   preserve compatibility with canonical knowledge imported before
   statement-level support was available;
3. a Relation supported only by `DERIVED` Statements SHALL NOT be treated as
   a physical feed for this analysis.

Rule 3 is essential. A derived relation such as
`MainSwitch-01 supplies ElectricBoiler-01` can be a valid semantic
conclusion, but it cannot bypass `MCB-01` when the conclusion's evidence path
passes through that MCB.

The set of physical supply roots SHALL be calculated from the original
physical topology. A root is a source endpoint with no physical `supplies`
input. The analysis SHALL NOT recalculate roots after removing the interrupted
Node: otherwise a disconnected downstream branch would incorrectly become a
new source.

## 4. Counterfactual semantics

The analysis SHALL remove the interrupted Node and every physical supply
Relation incident to it. Starting from every remaining original supply root,
it SHALL determine the Nodes still reachable through the remaining physical
`supplies` Relations.

A Node is *impacted* exactly when both conditions hold:

1. it is downstream-reachable from the interrupted Node in the canonical,
   composable `supplies` graph; and
2. it is not reachable from any remaining original physical supply root after
   the interruption.

The analysis SHALL return a deterministic shortest canonical `supplies`
witness from the interrupted Node for every impacted Node. The witness
explains why the Node was downstream of the interruption; it is not a claim
that this was the only original route.

No unrecorded backup feed SHALL be inferred. If a source, cable, transfer
switch, or supply Relation is unknown, the analysis SHALL only reflect the
knowledge that is currently canonical.

## 5. Example

Given:

```text
MainSupply-01 ─supplies─► MCB-01 ─supplies─► BoilerCircuit-01
BackupSupply-01 ─supplies──────────────────► BoilerCircuit-01
BoilerCircuit-01 ─supplies─► ElectricBoiler-01
MCB-01 ─supplies─► ShedLight-01
```

then interrupting `MCB-01` SHALL produce:

| Node | Result | Reason |
|---|---|---|
| `BoilerCircuit-01` | Not impacted | `BackupSupply-01` still supplies it. |
| `ElectricBoiler-01` | Not impacted | Its circuit remains supplied by the backup path. |
| `ShedLight-01` | Impacted | It has no remaining physical supply route. |

If the graph additionally contains a derived shortcut
`MainSupply-01 supplies BoilerCircuit-01`, that shortcut SHALL NOT change the
result unless a physically supported Relation establishes the alternate path.

## 6. Limits and non-goals

The initial contract models one interrupted Node and only the connectivity
meaning of `supplies`. It does not yet model:

- multiple simultaneous interruptions;
- switch, transfer-switch, RCD, RCBO, or breaker-specific operating rules;
- phase, voltage, load capacity, protection rating, or cable constraints;
- automatic discovery of unknown backup feeds; or
- a persistent event or State history.

Those capabilities MAY build on this contract later, but they SHALL remain
separate from the read-only counterfactual defined here.
