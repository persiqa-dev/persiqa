# PMS-008 --- CKM v0.1 Semantic Baseline

**Document:** Persiqa Meta Model Specification (PMS)  
**Chapter:** PMS-008  
**Title:** CKM v0.1 Semantic Baseline  
**Status:** Accepted  
**Scope:** semantic model; not a persistence, API, UI, or reasoning-engine specification  
**Version:** 0.1

PMS-008 is normative. Where an earlier PMS chapter conflicts with this chapter on Core-object endpoints, ownership, identity, Statement scope, knowledge context, refinement, or representation, this chapter SHALL take precedence.

## 1. Purpose

This specification defines the minimal canonical knowledge model used by Persiqa to represent model-relevant reality while knowledge is incomplete, changes over time, is supplied by multiple sources, or has competing interpretations.

CKM is graph-first. It distinguishes the modeled world from assertions made about that world, and it keeps the ontology deliberately small:

```text
Core ontology:       Entity, Capability, Relation, State
Knowledge primitive: Statement
```

`Type`, `classification`, `role`, product model, topology, measurement, identifier, source-system reference, and domain-specific concepts are **not** additional Core primitives. They are expressed using Core objects, semantic Relation Types, Statements, and typed contextual values.

This document uses the terms **MUST**, **MUST NOT**, **SHALL**, **SHALL NOT**, **SHOULD**, **SHOULD NOT**, and **MAY** as normative requirements. `MUST` and `SHALL` have equivalent force.

## 2. Conformance model

A conforming CKM implementation SHALL:

1. preserve the distinct semantics of Entity, Capability, Relation, State, and Statement;
2. support incomplete knowledge without manufacturing placeholder facts;
3. represent semantic relation types separately from Core primitives;
4. preserve explicit assertions and their knowledge context independently of derived conclusions; and
5. retain enough identity and provenance information to distinguish assertion, object continuity, and storage identity.

This specification defines semantics, invariants, and examples. It does **not** prescribe a database schema, an identifier format, a query language, a rule language, or a closed vocabulary of Relation Types.

## 3. Fundamental distinction: world model and knowledge model

CKM has two complementary layers:

| Layer | Concern | Principal objects |
|---|---|---|
| Canonical world model | What is modeled as existing, capable, connected, or in a condition | Entity, Capability, Relation, State |
| Knowledge model | What was asserted, observed, imported, inferred, disputed, or otherwise known about the model | Statement |

A Relation models a semantically meaningful association. A Statement models an assertion about a fact, object, value, or association. The same displayed edge MAY therefore correspond to a canonical Relation, one or more Statements about it, or both.

For example, `MCB-01 supplies Outlet-01` can be a canonical Relation. An inspection, imported source, or inference that supports that relation SHALL be representable as a distinct Statement with its own provenance and confidence.

## 4. Core ontology

### 4.1 Entity

An **Entity** is an independently identifiable, model-relevant object with continuity in the canonical model.

An Entity SHALL NOT be restricted to a physical object. A physical device, room, cable, GPIO pin, virtual machine, application, external-system object, conceptual reference object, or another model-relevant object MAY be an Entity when it needs independent identity, participation in relations, states, capabilities, or assertions.

An Entity MAY be created with no classification, identifier, location, capability, state, or known relationship. Its existence in the model does not imply that these facts are known.

Entity identity represents modeled continuity, not current lifecycle condition. A VM that changes from `Running` to `Stopped`, or changes its role from Kubernetes node to build runner, SHALL remain the same Entity unless continuity is intentionally broken. A replaced pump SHALL normally be a distinct Entity, even if it takes over the same functional role.

### 4.2 Capability

A **Capability** is a model-relevant ability or function that can be associated with an Entity.

Capability SHALL express what an Entity can do or is intended to do; it SHALL NOT by itself express whether that ability is currently active, reachable, healthy, or used. Those conditions belong to State or to Statements about State.

Examples include `OvercurrentProtection`, `ResidualCurrentProtection`, `GPIOControl`, `ContainerExecution`, and `TemperatureMeasurement`.

A Capability MAY be modeled as a reusable conceptual object or as an individually identifiable capability instance when that distinction matters. Its exact identity policy is defined in section 5.2.

### 4.3 Relation

A **Relation** is an independently identifiable, semantically meaningful association between endpoints in the canonical model.

Every Relation SHALL have:

- a declared Relation Type;
- the endpoints required by that Relation Type; and
- its own canonical identity.

A Relation's identity SHALL NOT be computed solely from `{Relation Type, source, target}`. Different physical links, contracts, logical routes, temporal associations, or other separately relevant associations MAY have the same type and endpoints and still be distinct Relations.

Relation Type provides the domain semantics: direction, endpoint admissibility, inverse, symmetry, transitivity, composition, cardinality, and other constraints. CKM itself SHALL NOT assume that all graph edges have the same inference behavior.

### 4.4 State

A **State** is a semantic condition of an Entity or Relation in a defined contextual scope.

State SHALL be distinguished from Capability and from knowledge status:

- `TemperatureMeasurement` is a Capability; `temperature = 23.7 °C` is a State or observation-derived state.
- `Running`, `Stopped`, `Open`, and `Closed` are States.
- `unknown` is not, by default, a domain State. Absence of knowledge SHALL be represented as absence of a Statement or State assertion.

State has contextual identity: it is identified by its owner, semantic predicate, and applicable context rather than by an independent continuity identity. An implementation MAY give a State a technical identifier, but that identifier SHALL NOT be interpreted as Entity-like continuity.

A State MAY be current, historical, planned, asserted, observed, or derived according to its associated Statements and temporal context.

### 4.5 Statement

A **Statement** is a first-class representation of an assertion about modeled knowledge.

A Statement SHALL have its own identity and SHALL support knowledge context such as provenance, time, confidence, derivation, and conflict status. A Statement MAY assert a relationship, a classification, a capability association, a state, a typed value, or an assertion about another Statement.

Statements make CKM non-destructive with respect to differing evidence. Two incompatible assertions SHALL be preservable without automatically deleting either assertion.

### 4.6 Contextual values and concepts

Typed values and semantic concepts are required by CKM but are not additional Core ontology primitives.

- A **typed value** is contextual information without independent model continuity, such as `16 A`, `23.7 °C`, `true`, a timestamp, or `garage-esp`.
- A **concept** is a semantic target used by a Relation Type, such as `CircuitBreaker`, `KubernetesNode`, `MainSwitch`, or `Acti9-iC60N-2P-C16`.

An implementation MAY represent a concept or a vocabulary term as an Entity when it must independently participate in the canonical graph. This representation choice SHALL NOT promote `Type`, `Role`, or `Classification` to a new Core primitive.

## 5. Identity

### 5.1 General rule

Identity answers **which modeled thing or assertion is this?** It SHALL be separate from human-readable labels, external identifiers, and persistence keys.

No system SHALL infer sameness merely because two objects currently share a name, serial-looking value, type, relation endpoints, or storage key. Identity resolution MAY use such evidence, but the resulting merge or continuity decision SHALL be explicit and auditable.

### 5.2 Identity by CKM object kind

| Object | Identity semantics | Normative rule |
|---|---|---|
| Entity | Canonical representation of continuity | SHALL survive non-identity changes such as state, role, label, or added identifiers. |
| Capability | Identity of the capability concept or separately relevant capability instance | SHALL be explicit when capability instances need individual lifecycle or provenance; otherwise reuse of a capability concept is permitted. |
| Relation | Identity of one semantic association | SHALL be independent of endpoint tuple alone. |
| State | Contextual identity | SHALL be interpreted through owner, predicate, and context; it does not denote an enduring independent object by default. |
| Statement | Identity of one assertion event/record | SHALL remain distinguishable from an equal-looking assertion supplied by a different source, time, or derivation. |

### 5.3 Identifier, identity, and persistence ID

These terms SHALL NOT be conflated.

| Term | Meaning | Examples |
|---|---|---|
| Canonical identity | The model's answer to continuity/sameness | `Pump-01` as the original installed pump |
| Identifier | A typed value playing an identifying role under a scheme and scope | serial number, asset tag, MAC address, hostname, local reference |
| Persistence ID | A technical key used to store a record | UUID, surrogate database key, row ID |

An identifier SHALL be modeled with at least a value, identification scheme, and scope when those are relevant. It MAY be missing, mutable, non-unique outside its scope, reassigned, or erroneous. It SHALL NOT by itself define Entity identity.

A persistence ID MAY change during migration, replication, import, or storage redesign without changing canonical identity. Conversely, two persistence records MAY be resolved to one canonical Entity, or one imported record MAY later be split into multiple Entities, subject to explicit provenance.

## 6. Endpoints, ownership, and assertions

### 6.1 Relation endpoints

The generic CKM Relation endpoint space SHALL permit CKM objects where a Relation Type declares them valid. Relation Type constraints, not the generic Relation class, decide admissibility.

Examples:

| Relation Type | Source | Target |
|---|---|---|
| `contains` | Entity | Entity |
| `connectedTo` | Entity | Entity |
| `hasCapability` | Entity | Capability |
| `hasState` | Entity or Relation | State |
| `supports` | Entity or Statement | Statement |
| `disputes` | Statement | Statement |

Every Relation Type SHALL declare its permitted endpoint kinds before production use. A conforming implementation SHALL reject, quarantine, or mark invalid a Relation that violates its declared endpoint constraints.

### 6.2 Capability ownership

An Entity MAY be associated with zero or more Capabilities using `hasCapability` or an equivalent declared Relation Type. The association MAY be explicit or derived.

Capability SHALL NOT be embedded as a mandatory intrinsic field of Entity. The same capability concept MAY be associated with many Entities. An Entity MAY retain a capability while a State says it is currently unavailable.

### 6.3 State ownership

State SHALL have an owner that is an Entity or Relation unless a future extension explicitly defines another owner kind. The `hasState` Relation Type SHALL express that ownership semantically.

Implementations SHOULD prevent simultaneous mutually exclusive current States for the same owner and state predicate unless the assertions are deliberately retained as a known conflict, differ by temporal scope, or belong to separate scenarios/contexts.

### 6.4 Statement subject, predicate, and object

A Statement SHALL minimally express an assertion subject, predicate, and object/value, either directly or by referring to a canonical object or Relation.

A Statement MAY target:

- an Entity, Capability, Relation, or State;
- another Statement;
- a typed value; or
- a semantic concept.

Statement predicates SHALL be defined by a vocabulary or Relation Type with documented semantics. A statement such as `BME280-01 measuredTemperature 23.7 °C` is not required to create a new Core primitive; it is an assertion with a typed value and appropriate temporal/provenance context.

## 7. Semantic Relation Types

### 7.1 General rules

A Relation Type SHALL be a declared semantic contract, not merely a display label. Each Relation Type SHOULD define:

- name and stable vocabulary identifier;
- allowed source and target kinds;
- directionality and inverse, if any;
- whether it is symmetric;
- whether and under which conditions it is transitive or composable;
- cardinality and containment constraints where relevant; and
- rules for deriving, validating, or conflicting relations.

No generic graph traversal SHALL be treated as semantic inference. In particular, a chain involving `controls`, `requiresPower`, and `supplies` SHALL NOT imply a new `controls` relation unless an explicit rule declares it.

### 7.2 Baseline Relation Type vocabulary

The following vocabulary is baseline CKM v0.1. It is extensible; domains MAY declare additional Relation Types without adding Core primitives.

| Relation Type | Meaning and endpoint intent | Direction / inference |
|---|---|---|
| `instanceOf` | An Entity is an instance of a specific product/model/type concept. | Directed: Entity → concept. Not generically transitive. |
| `classifiedAs` | An Entity or concept is assigned a general semantic classification. | Directed: subject → concept. Multiple classifications permitted. Taxonomic inference requires an explicit taxonomy rule. |
| `playsRole` | An Entity performs a contextual/system role. | Directed: Entity → role concept. Role MAY change over time and MAY be derived. |
| `hasCapability` | An Entity has or is associated with a Capability. | Directed: Entity → Capability. Does not imply active operation. |
| `hasState` | An Entity or Relation has a State in context. | Directed: Entity/Relation → State. Current-state selection is context-aware. |
| `contains` | One Entity structurally or spatially contains another. | Directed: container → contained Entity. Inverse MAY be `partOf`; transitivity and exclusivity SHALL be declared per containment profile. |
| `connectedTo` | Two Entities are connected in a declared sense. | Usually symmetric, but the connection modality and endpoint constraints SHALL be declared. Not generically transitive. |
| `supplies` | A source provides power, material, service, or another declared supply to a target. | Directed. A domain MAY declare controlled transitive derivation through compatible supply paths. |
| `dependsOn` | A subject depends on a target for a declared condition or function. | Directed: dependent → dependency. Transitivity is domain/rule-specific. |
| `hostedOn` | A hosted Entity is hosted by an infrastructure Entity. | Directed: hosted workload → host. Inverse MAY be `hosts`. |
| `runsIn` | An application or workload runs in a container/execution context. | Directed: workload → execution context. |
| `exposes` | A service/interface exposes an application or capability. | Directed. Domain semantics required. |
| `controls` | A controlling Entity can control a target Entity. | Directed. Not transitive by default. |
| `supports` | Evidence, an Entity, or a Statement supports a Statement. | Directed: supporter → supported Statement. |
| `disputes` | A Statement contests another Statement. | Directed: disputing Statement → disputed Statement. |
| `replacedBy` | An Entity was replaced by another Entity. | Directed: replaced → replacement. It SHALL NOT merge Entity identities. |

`locatedAt`, `partOf`, `communicatesWith`, `represents`, `upstreamOf`, and other domain relations MAY be declared using the same mechanism.

### 7.3 Type, classification, and role

The following distinctions SHALL be preserved:

```text
instanceOf     specific product/model/type membership
classifiedAs   general semantic category membership
playsRole      contextual function in a system
```

An RCBO can therefore simultaneously be an instance of `Acti9-iDPN-Vigi-C16`, classified as `CircuitBreaker`, `ResidualCurrentDevice`, and `ProtectiveDevice`, and play the role `BathroomCircuitProtection`. None of these statements shall require a `Type` or `Role` Core primitive.

## 8. Knowledge semantics

### 8.1 Explicit and derived knowledge

**Explicit knowledge** is directly asserted, observed, imported, or entered from a declared source. **Derived knowledge** is produced by a declared rule, computation, or inference over other knowledge.

Both explicit and derived knowledge SHALL be representable as Statements. A derived Statement SHALL identify its derivation method and the source Statements, Relations, or facts on which it depends. A derived result SHALL NOT silently overwrite, erase, or masquerade as an explicit assertion.

Example:

```text
explicit: MCB-01 supplies JunctionBox-03
explicit: JunctionBox-03 supplies Outlet-01
derived:  MCB-01 supplies Outlet-01
```

The derived relation MAY coexist with the two explicit relations. The derivation is valid only if the declared `supplies` rule permits that composition in the relevant context.

### 8.2 Provenance

Every Statement SHOULD carry provenance sufficient to answer where it came from. Provenance MAY include source system, person, sensor, inspection, import batch, document, image, algorithm, or source Statement.

Provenance SHALL be attached to Statements rather than treated as an intrinsic replacement for canonical object identity. A sensor observation and a technician's inspection can both concern the same Entity while remaining distinct assertions.

### 8.3 Temporal context

Statements and States MAY carry temporal context. A conforming implementation SHALL distinguish, where supplied:

- when a fact was observed or asserted;
- when the asserted fact is valid in the modeled world; and
- when the system recorded it.

Temporal context MAY be an instant, interval, or an explicitly unspecified time. Missing time SHALL mean “not known or not supplied”, not an invented timeless fact.

### 8.4 Confidence

Confidence expresses the strength, quality, or reliability attributed to a Statement; it SHALL NOT be interpreted as truth itself. Confidence MAY be numeric, ordinal, or source-policy derived, but its scale and combination method SHALL be documented by the implementation.

### 8.5 Conflict

Statements conflict when, under declared semantic constraints and overlapping context, they cannot all be accepted as simultaneously compatible.

Conflict detection MAY be derived. Conflict SHALL NOT cause automatic deletion of either assertion. Resolution policy MAY select a preferred current view, but it SHALL preserve the competing Statements and explain the policy or derivation used.

### 8.6 Unknown and absence of knowledge

The absence of a Statement, Relation, State, identifier, or value SHALL mean only that CKM does not currently contain that knowledge. It SHALL NOT mean false, disconnected, unavailable, or `unknown` as a domain fact.

An explicit unknown, withheld, not-applicable, or indeterminate assertion MAY be represented only when a source actually makes that assertion and its semantics are declared. Implementations SHALL NOT populate `UNKNOWN` values merely to satisfy a schema.

## 9. Progressive Knowledge Principle

CKM SHALL permit useful, valid knowledge to enter the model before it is complete.

An Entity such as `Outlet-17` MAY exist without known type, feeder, cable, location, status, or identifier. Later discoveries SHALL enrich the same canonical Entity when continuity is established; they SHALL NOT require speculative placeholder data.

```text
Initial:  Entity Outlet-17
Later:    Outlet-17 classifiedAs ElectricalOutlet
Later:    Outlet-17 connectedTo Cable-17
Later:    Cable-17 supplies Outlet-17
```

Progressive enrichment SHALL preserve the distinction between newly learned knowledge and a correction of earlier knowledge. If an earlier claim was wrong, the corrective Statement SHALL be representable alongside its supersession, dispute, or validity context.

## 10. Refinement and representation

### 10.1 Refinement

**Refinement** adds resolution, structure, constraints, provenance, or detail to knowledge already represented. It SHALL NOT be assumed to invalidate an earlier lower-resolution statement solely because more detail became available.

For example, `MCB-01 supplies Outlet-01` can be a valid high-level assertion. Later discovery of cables and a junction box can explain or derive it:

```text
MCB-01 → Cable-17 → JunctionBox-03 → Cable-21 → Outlet-01
```

The original high-level statement MAY remain explicit, be derived from the detailed path, gain a temporal limitation, or be disputed. The choice SHALL be recorded as knowledge semantics, not silently decided by a display layer.

### 10.2 Canonical knowledge and multiple representations

CKM SHALL have one canonical knowledge model for a modeled scope. A representation is a projection, traversal, layout, aggregation, filter, or view of canonical knowledge; it is not a separate model merely because it looks different.

```text
                    Canonical knowledge
                           |
          +----------------+----------------+
          |                |                |
       overview       electrical detail   physical layout
```

An overview such as `MCB → Outlet`, an electrical path through junction boxes and cables, and a floor-plan layout MAY all be valid representations of the same canonical knowledge. Presentation-specific coordinates, grouping, styling, and navigation MAY be maintained separately provided they do not silently change canonical semantics.

## 11. Validated examples

### 11.1 Electrical protection and circuit

```text
RCBO-01 instanceOf     Acti9-iDPN-Vigi-C16
RCBO-01 classifiedAs   CircuitBreaker
RCBO-01 classifiedAs   ResidualCurrentDevice
RCBO-01 classifiedAs   ProtectiveDevice
RCBO-01 playsRole      BathroomCircuitProtection
RCBO-01 hasCapability  OvercurrentProtection
RCBO-01 hasCapability  ResidualCurrentProtection
RCBO-01 hasState       Closed
```

This example validates that product/model membership, classification, contextual role, capability, and current state are separate facts about the same Entity. A single `type` field cannot preserve these distinctions.

A circuit can initially be asserted at low resolution:

```text
MCB-01 supplies Outlet-01
```

and later be refined:

```text
MCB-01 supplies Cable-17
Cable-17 connectedTo JunctionBox-03
JunctionBox-03 supplies Cable-21
Cable-21 connectedTo Outlet-01
```

Whether a supply path is inferred through each link SHALL follow a declared electrical `supplies` composition rule; `connectedTo` alone SHALL NOT generically imply supply.

### 11.2 Measurement and evidence

```text
BME280-01 hasCapability TemperatureMeasurement
BME280-01 hasState      Temperature(23.7 °C)

Statement S-101:
  subject      BME280-01
  predicate    measuredTemperature
  object       23.7 °C
  observedAt   2026-09-14T14:32:10+02:00
  provenance   BME280-01
```

The State answers the current modeled condition. The Statement preserves how that condition was observed. A later `24.1 °C` observation SHALL be retained as another Statement rather than overwriting the earlier evidence.

### 11.3 Conflicting electrical knowledge

```text
S-201: Breaker-01 supplies Outlet-01  [inspection A]
S-202: Breaker-01 supplies Outlet-02  [inspection B]
```

These assertions MAY coexist. If declared circuit constraints make them incompatible in a shared temporal and system context, the implementation MAY derive a conflict. It SHALL retain both Statements with their provenance and context.

### 11.4 Kubernetes infrastructure

```text
Server-01 hosts        VM-01
VM-01     playsRole    KubernetesNode
VM-01     hasCapability ContainerExecution
Pod-01    hostedOn     VM-01
Container-01 partOf    Pod-01
Application-01 runsIn  Container-01
Service-01 exposes     Application-01
VM-01     hasState     Running
```

When `VM-01` later has state `Stopped`, the VM remains the same Entity. When it later plays role `BuildRunner`, its identity remains the same; role is contextual rather than intrinsic type. These examples validate lifecycle/state separation and cross-domain application of the same Core.

### 11.5 Replacement continuity

```text
Pump-01 replacedBy Pump-02
```

`Pump-01` and `Pump-02` SHALL remain different Entities. A shared functional role such as `IrrigationPump` does not establish identical Entity continuity.

## 12. Invariants summary

1. CKM SHALL NOT introduce `Type`, `Role`, `Classification`, `Value`, or `Identifier` as new Core ontology primitives.
2. An Entity MAY be valid despite incomplete knowledge.
3. Entity identity SHALL be independent of lifecycle state and mutable identifiers.
4. Capability SHALL NOT imply active or healthy State.
5. Relation identity SHALL NOT be reduced to endpoints and type.
6. State SHALL be contextual to an Entity or Relation.
7. Statement SHALL be first-class and independently identifiable.
8. Explicit and derived knowledge SHALL remain distinguishable.
9. Conflicting statements SHALL be preservable.
10. Absence of knowledge SHALL NOT be converted into a fabricated domain value.
11. Semantic inference SHALL follow declared Relation Type rules only.
12. Multiple views SHALL project canonical knowledge rather than create parallel canonical models.

## 13. Open questions for a subsequent CKM revision

The following are intentionally unresolved by v0.1. They SHALL NOT be prematurely fixed by a persistence schema or a convenience field.

1. **Relation identity and qualifiers.** Define the precise minimum identity and qualifier model for relations that share type and endpoints but differ by physical path, valid time, port, phase, medium, or other context.
2. **Concept representation.** Define when a taxonomy/product/role concept must be an Entity versus a vocabulary item, and how concept identity/versioning is governed.
3. **Value type system.** Define the minimal standard scalar, quantity, unit, temporal, and structured-value set, including unit interoperability. CKM v0.1 deliberately does not prescribe a unit engine.
4. **Identifier schemes and resolution.** Define a standard expression for scheme, scope, issuer, validity period, uniqueness claims, aliases, and evidence-based merge/split workflow.
5. **State profile rules.** Define standard mechanisms for mutually exclusive states, current-state selection, state history, scenario/context separation, and relation-specific state qualifiers.
6. **Relation Type registry.** Define vocabulary governance, stable identifiers, validation profiles, inverses, transitivity/composition declarations, and compatibility/versioning.
7. **Provenance and confidence profiles.** Define interoperable source, observation, derivation, confidence, and conflict-resolution shapes without collapsing the semantic distinction established here.
8. **Canonicalization workflow.** Define how explicit Statements create, update, support, dispute, or retire canonical Relations and States while preserving auditability.
9. **Representation contract.** Define a formal view/projection model and which layout or aggregation metadata is canonical versus presentation-only.

## 14. Non-goals

CKM v0.1 does not define:

- a PostgreSQL, Flyway, ORM, JSON, or API schema;
- a mandatory graph database;
- a universal ontology or all domain Relation Types;
- automatic truth resolution or a universal confidence arithmetic;
- a replacement for source-system records; or
- a UI-specific graph layout.

Those concerns SHALL build on this semantic specification, not redefine it implicitly.
