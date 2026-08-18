# PDS-003 — Semantic Model

**Document:** Persiqa DSL Specification (PDS)  
**Chapter:** PDS-003  
**Title:** Semantic Model  
**Status:** Accepted

---

# 1. Purpose

This chapter defines the normative semantics of the Persiqa DSL.

While the grammar specifies how a document is written, this chapter
specifies what a valid document means.

---

# 2. Canonical Knowledge Model

Every valid DSL document SHALL be transformed into a Canonical Knowledge
Model (CKM).

The CKM is independent of:

- parser implementation,
- programming language,
- storage technology,
- serialization format.

Semantics are defined exclusively on the CKM.

---

# 3. Core Ontology and Semantic Objects

The Persiqa Core ontology consists of exactly four ontological concepts:

- Entity
- Capability
- Relation
- State

The CKM contains five first-class object types:

- Entity
- Capability
- Relation
- State
- Statement

Statement is a first-class CKM object because it is the canonical unit of
expressed knowledge.

Statement is not a fifth Core ontology concept.

```text
Core Ontology
├── Entity
├── Capability
├── Relation
└── State

First-Class CKM Objects
├── Entity
├── Capability
├── Relation
├── State
└── Statement
```

No additional Core ontology concepts or first-class CKM object types SHALL be
introduced by the semantic model.

---

# 4. Entity Semantics

An Entity represents an identifiable element of reality.

Entities MAY participate in:

- Statements,
- Relations,
- Refinement,
- State ownership.

An Entity SHALL NOT derive its meaning from implementation details.

---

# 5. Capability Semantics

A Capability represents a universal behaviour or domain-level ability that an
Entity can exhibit.

Capabilities are semantic concepts. They are not methods, APIs or executable
code.

A concrete Capability SHALL have exactly one owning Entity.

A `capability` declaration introduces a Capability semantic identifier. A
concrete owned Capability is established when a valid Statement asserts the
applicable ownership relationship, for example:

```text
transport: HeatPump has Transport
```

The resulting Capability has canonical semantic identity derived from its
owner and Capability semantic type. The local reference `transport` does not
contribute to that canonical identity.

Domain-specific capabilities SHALL be expressed through refinement.

---

# 6. Relation Semantics

A Relation represents a directed semantic connection between two Entities.

A Relation SHALL have:

- exactly one source Entity,
- exactly one target Entity.

Relations are first-class CKM objects.

A `relation` declaration introduces a Relation semantic identifier. A concrete
Relation instance is derived from a valid Statement whose predicate resolves
to that Relation semantic identifier and whose subject and object resolve to
the source and target Entities.

For example:

```text
connection: HeatPump connected_to BufferTank
```

produces a Relation with canonical semantic identity derived from:

```text
(relation type, source Entity, target Entity)
```

A Relation MAY own:

- identity,
- refinement,
- State,
- semantic attributes.

Relations SHALL NOT be interpreted as implementation references.

---

# 7. State Semantics

State represents mutable knowledge about an Entity or Relation.

State SHALL have exactly one owner:

- Entity, or
- Relation.

The owner reference in a State declaration SHALL resolve directly to an Entity
or Relation, or through an explicitly defined semantic derivation from a
referenced CKM object.

For example:

```text
state HeatPump Temperature = 52.3
```

creates a State owned by `HeatPump`, while:

```text
connection: HeatPump connected_to BufferTank
state connection RSSI = -58
```

creates a State owned by the Relation expressed by `connection`.

State SHALL NOT define an independent continuity identity. Within its owner context, a State is identified by its semantic predicate; changing its value SHALL NOT create a new canonical State object.

State MAY change without changing the identity of the model element that owns
it.

---

# 8. Statement Semantics

A Statement is the canonical unit of knowledge.

Semantically, a Statement asserts a fact within the model.

Every Statement SHALL preserve:

- subject,
- predicate,
- object,
- meaning.

A Statement Subject SHALL identify an Entity or Relation.

A Statement predicate MAY identify a Relation semantic type, a Capability
semantic type, or another semantically defined predicate.

A Statement MAY have a DSL-local reference. The local reference is a lookup
name only and SHALL NOT contribute to the canonical identity or semantic
meaning of the Statement.

For example:

```text
connection: HeatPump connected_to BufferTank
```

The local reference `connection` identifies the resulting Statement within
its scope. The Statement semantics derive the corresponding Relation as
defined by the Relation Model.

Equivalent semantic representations SHALL produce equivalent Statements.

---

# 9. Reference Resolution

Reference resolution maps DSL identifiers and local references to canonical
CKM objects.

### DSL-local Reference Binding

Within a scope, a DSL-local reference SHALL identify exactly one
canonical CKM object or Statement. A DSL-local reference SHALL only be bound by
a declaration that creates or identifies such a referenceable object.

## 9.1 Local Reference

A DSL-local reference is an optional label attached to a declaration.

```text
connection: HeatPump connected_to BufferTank
```

The reference `connection` is scoped to the containing lexical scope and does
not contribute to canonical identity.

Within a scope, a local reference SHALL identify exactly one canonical CKM
object. A canonical CKM object SHALL NOT be bound to more than one local
reference within the same scope.

## 9.2 Resolution Order

Semantic processing SHALL resolve references after syntactic parsing. A
conforming implementation MAY collect declarations and local references in a
first pass and resolve their uses in a subsequent pass.

Forward references are permitted.

## 9.3 Direct Resolution

A local reference MAY resolve directly to the CKM object produced by the
labeled declaration.

Examples:

```text
pump: entity HeatPump
rssi: state HeatPump Temperature = 52.3
```

`pump` resolves to an Entity and `rssi` resolves to a State.

## 9.4 Semantic Derivation

A construct MAY explicitly define a semantic derivation from a referenced
object to another CKM object.

For example, a State owner may resolve:

```text
connection
    ↓
Statement
    ↓
Relation expressed by the Statement
```

The derivation SHALL be explicitly defined by the applicable semantic rule.
Implementations SHALL NOT perform arbitrary dereferencing until a requested
type happens to match.

## 9.5 Type Validation

The semantic context determines which CKM object kinds are valid.

For example:

```text
Statement Subject → Entity | Relation
State Owner       → Entity | Relation
Capability Owner  → Entity
Relation Source   → Entity
Relation Target   → Entity
```

A resolved reference of an incompatible kind SHALL be rejected.

## 9.6 Scope

Local references SHALL be visible only within their containing lexical scope
unless explicitly imported or qualified by namespace rules.

An implementation SHALL NOT treat a local reference as globally visible by
default.

## 9.7 Qualified References

When namespaces or nested scopes are supported, an implementation MAY provide
qualified references. Qualified reference syntax and import semantics SHALL
be defined by PDS-002 and PDS-006.

## 9.8 Unresolved and Cyclic Resolution

An unresolved reference SHALL be a semantic error.

Ambiguous references SHALL be rejected.

Semantic derivation SHALL terminate. Cyclic derivation paths SHALL be rejected.

---

# 10. Semantic Equivalence

Two DSL documents are semantically equivalent if they produce equivalent
Canonical Knowledge Models.

Differences in:

- declaration order,
- formatting,
- whitespace,
- comments,
- DSL-local reference names,

affect semantic meaning only when explicitly defined by another specification.

Changing or removing a local reference without changing the referenced
semantic declaration SHALL NOT change CKM meaning.

---

# 11. Conformance

A conforming implementation SHALL produce the same semantic interpretation
for every valid DSL document.

Parsing SHALL produce syntax-level structures; semantic processing SHALL
resolve references and construct the CKM.

Reasoning, validation and serialization SHALL operate on the Canonical
Knowledge Model rather than directly on the parsed syntax.

---

# 12. Relationship to Other Specifications

PDS-001 defines lexical structure.

PDS-002 defines syntax.

PDS-003 defines semantic interpretation and reference resolution.

PDS-004 defines canonical mapping.

PDS-005 defines validation rules.

PDS-006 defines serialization and versioning.

The PMS defines the canonical CKM object model.

The PAS defines the normative architecture.

These specifications SHALL remain semantically consistent.


### DSL-local Reference Applicability

A DSL-local reference SHALL identify exactly one canonical CKM object or
Statement within its lexical scope. It SHALL only be permitted on declarations
that create or identify such a referenceable object.
