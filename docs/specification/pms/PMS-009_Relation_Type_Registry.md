# PMS-009 --- Relation Type Registry

**Document:** Persiqa Meta Model Specification (PMS)  
**Chapter:** PMS-009  
**Title:** Relation Type Registry  
**Status:** Accepted  
**Version:** 0.1

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the Relation Type Registry for Persiqa CKM v0.1.

A Relation Type is the declared semantic contract governing a Relation or a Statement predicate that establishes a Relation. The registry makes endpoint admissibility, direction, inverse meaning, inference behaviour, cardinality, and validation explicit. It provides the semantic bridge between the minimal Core and domain-specific meaning.

This chapter implements the Relation Type Registry open question in PMS-008. It SHALL be interpreted consistently with PMS-008 and SHALL NOT introduce a Core ontology primitive.

------------------------------------------------------------------------

# 2. Conformance and scope

A conforming implementation SHALL validate every concrete Relation against a registered Relation Type. A Relation Type MAY be built in, imported, or domain-declared, but its effective contract SHALL be available during validation and reasoning.

This registry defines the CKM semantic contract. It does not prescribe DSL syntax, a database table, REST resources, or a universal domain ontology.

------------------------------------------------------------------------

# 3. Relation Type contract

Every registered Relation Type SHALL define:

| Field | Requirement |
|---|---|
| Stable identifier | Globally unique within the registry namespace. |
| Name | Human-readable, non-normative display name. |
| Semantic definition | Precise directed meaning of one asserted Relation. |
| Source endpoint profile | Permitted CKM kinds and constraints. |
| Target endpoint profile | Permitted CKM kinds and constraints. |
| Direction | Directed, symmetric, or explicitly paired inverse. |
| Inverse | Registered inverse identifier or `none`. |
| Symmetry | `true` or `false`. |
| Inference policy | `none`, declared composition, or declared rule set. |
| Cardinality policy | Endpoint multiplicity and exclusivity, if any. |
| Conflict policy | Conditions that may establish incompatibility. |
| Version | Semantic version or compatible registry revision. |

The registry SHOULD also declare temporal scope, context/qualifier profile, containment profile, provenance requirements, and deprecation/replacement information where applicable.

Absent declarations SHALL have conservative meaning:

```text
inverse             = none
symmetry            = false
inference policy    = none
cardinality         = unconstrained
conflict policy     = no generic conflict
```

No implementation SHALL infer symmetry, transitivity, inverse, exclusivity, or composition from a Relation Type name alone.

------------------------------------------------------------------------

# 4. Endpoint profiles

An endpoint profile SHALL state permitted object kinds and semantic restrictions:

```text
Entity | Capability | Relation | State | Statement | Concept | TypedValue
```

`Concept` and `TypedValue` are contextual semantic targets, not additional Core primitives. An implementation MAY represent a Concept as an Entity when independent graph participation is required; the effective endpoint profile SHALL remain semantically equivalent.

Every concrete Relation SHALL have exactly one source and one target. A Relation Type MAY permit more than one endpoint kind, but SHALL identify valid kind combinations. An invalid combination SHALL be rejected, quarantined, or retained only as explicitly invalid imported knowledge.

------------------------------------------------------------------------

# 5. Inference and inverse rules

Relations are not generically transitive. A reasoning engine SHALL derive a Relation only through an inference policy declared here or through an applicable explicit rule set. Derived Relations and Statements SHALL preserve derivation provenance and remain distinguishable from explicit knowledge.

An inverse declaration permits a reversed-direction Relation with the registered inverse Relation Type; it does not make the two Relations one identity. A symmetric Relation Type means reversing valid endpoints expresses the same association; it SHALL NOT imply transitivity.

A composition policy SHALL name its allowed input Relation Types, result Relation Type, endpoint constraints, applicable context, and provenance requirements. It SHALL NOT be implemented as unrestricted graph-path traversal.

------------------------------------------------------------------------

# 6. Baseline Relation Type Registry

The following entries form the baseline CKM v0.1 registry.

## RT-001 --- `instanceOf`

| Field | Contract |
|---|---|
| Source / target | Entity → Concept representing a specific product/model/type |
| Direction / inverse | Directed; no inverse |
| Symmetry / inference | false; none, except explicit taxonomy rules |
| Cardinality | Zero or more target concepts |
| Conflict | Only where a product profile declares exclusivity |

`instanceOf` SHALL NOT replace general classification or contextual role.

## RT-002 --- `classifiedAs`

| Field | Contract |
|---|---|
| Source / target | Entity or Concept → general category Concept |
| Direction / inverse | Directed; no inverse |
| Symmetry / inference | false; taxonomy closure only by explicit taxonomy rule |
| Cardinality | Zero or more target concepts |
| Conflict | Only for categories declared mutually exclusive |

Multiple classifications are valid by default.

## RT-003 --- `playsRole`

| Field | Contract |
|---|---|
| Source / target | Entity → contextual system-role Concept |
| Direction / inverse | Directed; no inverse |
| Symmetry / inference | false; may be derived by explicit topology/context rule |
| Cardinality | Zero or more roles |
| Conflict | Only for roles declared incompatible in shared context |

A role MAY change without changing Entity identity.

## RT-004 --- `hasCapability`

| Field | Contract |
|---|---|
| Source / target | Entity → Capability |
| Direction / inverse | Directed; no inverse |
| Symmetry / inference | false; may be derived by explicit product/type rule |
| Cardinality | Zero or more Capabilities |
| Conflict | None by default |

This Relation expresses association, not active operation, availability, or health.

## RT-005 --- `hasState`

| Field | Contract |
|---|---|
| Source / target | Entity or Relation → State |
| Direction / inverse | Directed; no inverse |
| Symmetry / inference | false; may be derived by explicit rule |
| Cardinality | Zero or more States; one owner per State |
| Conflict | State predicate, temporal/context scope, and declared exclusivity profile |

`hasState` establishes State ownership. It SHALL NOT collapse conflicting, historical, observed, planned, or scenario-specific States.

## RT-006 --- `contains`

| Field | Contract |
|---|---|
| Source / target | Container Entity → contained Entity |
| Direction / inverse | Directed; inverse `partOf` |
| Symmetry / inference | false; no default transitivity |
| Cardinality | Zero or more contained Entities |
| Conflict | Cycles or exclusive containment only where declared |

Containment transitivity requires a declared containment profile.

## RT-007 --- `connectedTo`

| Field | Contract |
|---|---|
| Source / target | Entity → Entity |
| Direction / inverse | Symmetric; inverse `connectedTo` |
| Symmetry / inference | true; none |
| Cardinality | Unconstrained |
| Conflict | None by default |

The connection modality SHOULD be declared by a context profile or more specific Relation Type. `connectedTo` SHALL NOT imply supply, dependency, reachability, or transitive connectivity.

## RT-008 --- `supplies`

| Field | Contract |
|---|---|
| Source / target | Supplying Entity → supplied Entity |
| Direction / inverse | Directed; no inverse |
| Symmetry / inference | false; declared, context-compatible supply composition only |
| Cardinality | Unconstrained |
| Conflict | Declared source/recipient exclusivity or incompatible supply context |

A composition rule MAY derive `A supplies C` from `A supplies B` and `B supplies C` only when both share compatible supply context and no declared boundary prevents propagation. `connectedTo` alone SHALL NOT participate in a `supplies` composition rule.

## RT-009 --- `dependsOn`

| Field | Contract |
|---|---|
| Source / target | Dependent Entity → dependency Entity |
| Direction / inverse | Directed; no inverse |
| Symmetry / inference | false; declared, context-compatible dependency composition only |
| Cardinality | Unconstrained |
| Conflict | Cycles or exclusivity only where declared |

A composition rule MAY derive `A dependsOn C` from `A dependsOn B` and `B dependsOn C` only when both share compatible dependency context and no declared boundary prevents propagation. `dependsOn` SHALL NOT be inferred merely because two Entities share a supply path, host, container, or controller.

## RT-010 --- `hostedOn`

| Field | Contract |
|---|---|
| Source / target | Hosted workload Entity → host/infrastructure Entity |
| Direction / inverse | Directed; inverse `hosts` |
| Symmetry / inference | false; none by default |
| Cardinality | One or more hosts only when a hosting profile permits placement/replication |
| Conflict | Mutually exclusive placement only where declared |

`hostedOn` models placement. It SHALL NOT by itself imply that the workload is running, available, or reachable.

------------------------------------------------------------------------

# 7. Registry governance and compatibility

A Relation Type identifier SHALL be stable after publication. A compatible revision MAY clarify examples or add non-conflicting metadata. A change to endpoint admissibility, direction, inverse, symmetry, inference, cardinality, or conflict semantics SHALL create a new compatible version or replacement identifier.

Deprecated Relation Types SHALL declare their successor where one exists. Existing canonical Relations retain their original Relation Type and version semantics unless an explicit migration produces traceable replacements.

Domain Relation Types MAY be registered without changing the Core. They SHALL declare all mandatory contract fields and SHALL NOT redefine a baseline identifier with incompatible semantics.

------------------------------------------------------------------------

# 8. Validation requirements

Validation SHALL verify:

1. a Relation Type identifier resolves to one effective registry entry;
2. source and target satisfy the endpoint profile;
3. direction, inverse, and symmetry claims agree with the contract;
4. derived Relations have valid declared provenance;
5. cardinality and containment constraints apply only when declared;
6. conflicts are detected only under declared conflict policy and overlapping relevant context; and
7. unknown Relation Types are rejected, quarantined, or explicitly retained as uninterpreted imported knowledge.

Validation SHALL NOT manufacture missing Relations, States, contexts, or conflicts to complete a graph.

------------------------------------------------------------------------

# 9. Open questions

The following remain intentionally open:

1. normative qualifier/context schema for ports, medium, phase, path, scenario, and validity interval;
2. standard taxonomy and containment profiles;
3. contracts for `partOf`, `hosts`, `runsIn`, `exposes`, `controls`, `supports`, `disputes`, and `replacedBy`; and
4. DSL syntax and interchange representation for registry declarations.
