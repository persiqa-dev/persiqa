# PDS-004 — Canonical Model Mapping

**Document:** Persiqa DSL Specification (PDS)  
**Chapter:** PDS-004  
**Title:** Canonical Model Mapping  
**Status:** Accepted

---

# 1. Purpose

This chapter defines how semantic Persiqa DSL declarations map to the
Canonical Knowledge Model (CKM).

The mapping establishes the boundary between DSL syntax and the canonical
semantic model.

The DSL is an authoring and serialization representation. The CKM is the
authoritative semantic representation produced by semantic processing.

---

# 2. Mapping Principle

DSL declarations SHALL map to CKM objects or semantic relationships according
to their semantic role.

The normative Core ontology consists of:

```text
Entity
Capability
Relation
State
```

Statement is the fundamental unit of expressed knowledge and maps to the
corresponding CKM Statement object. Statement is not an additional Core
ontology concept.

The primary mappings are:

```text
entity       → Entity
capability   → Capability semantic identifier
a relation   → Relation semantic identifier
state        → State
statement    → Statement
```

Capability and Relation declarations introduce semantic identifiers. Concrete
Capability and Relation CKM instances are materialized by semantically valid
Statements and their applicable ownership/endpoint rules.

---

# 3. Entity Mapping

A valid `entity` declaration SHALL produce exactly one CKM Entity.

Example:

```text
entity HeatPump
```

maps to:

```text
Entity("HeatPump")
```

The Entity identity SHALL be preserved during semantic processing and
serialization.

---

# 4. Capability Mapping

A valid `capability` declaration SHALL introduce exactly one Capability
semantic identifier.

Example:

```text
capability Transport
```

introduces the semantic type:

```text
CapabilityType("Transport")
```

A concrete Capability CKM object is established by a valid ownership
Statement, for example:

```text
transport: HeatPump has Transport
```

which maps to:

```text
Capability
    type  = Transport
    owner = HeatPump
```

The local reference `transport` is not part of the canonical Capability
identity.

A Capability SHALL have exactly one owning Entity.

---

# 5. Relation Mapping

A valid `relation` declaration SHALL introduce exactly one Relation semantic
identifier.

Example:

```text
relation connected_to
```

introduces the semantic type:

```text
RelationType("connected_to")
```

A concrete Relation CKM object is derived from a valid Statement whose
predicate resolves to that Relation type and whose subject and object resolve
to Entity objects.

Example:

```text
connection: HeatPump connected_to BufferTank
```

maps to:

```text
Statement
    subject  = HeatPump
    predicate = connected_to
    object   = BufferTank

Relation
    type   = connected_to
    source = HeatPump
    target = BufferTank
```

The canonical Relation identity SHALL be derived from:

```text
(relation type, source Entity, target Entity)
```

The local reference `connection` SHALL NOT contribute to canonical Relation
identity.

Exactly one source and one target SHALL be preserved in the CKM.

---

# 6. State Mapping

A valid `state` declaration SHALL produce exactly one CKM State.

Examples:

```text
state HeatPump Temperature = 22.8
state connection RSSI = -58
```

map respectively to States owned by:

```text
Entity(HeatPump)
Relation(connected_to, HeatPump, BufferTank)
```

The State declaration SHALL resolve its owner to an Entity or Relation either
directly or through an explicitly defined semantic derivation.

A State SHALL belong to exactly one owner.

State identity is not defined by its value. A new value updates the existing
State identified by its owner and semantic predicate.

---

### Statement Canonical Identity

A Statement SHALL have a canonical identity determined by its subject,
predicate, and object. The canonical identity is the tuple
(subject, predicate, object). A DSL-local reference or authoring label SHALL
NOT contribute to Statement canonical identity.

# 7. Statement Mapping

A valid Statement declaration SHALL produce exactly one CKM Statement.

Example:

```text
connection: HeatPump connected_to BufferTank
```

maps to a Statement representing:

```text
subject  = HeatPump
predicate = connected_to
object   = BufferTank
```

The local reference `connection` is a DSL-local lookup name only. It SHALL
NOT contribute to canonical Statement identity or semantic meaning.

When the predicate identifies Relation semantics, the Statement additionally
establishes the corresponding Relation instance as defined by PDS-003.

When the Statement expresses Capability ownership, it establishes the
corresponding Capability instance as defined by PDS-003.

---

# 8. Refinement Mapping

A refinement declaration:

```text
FluidTransport refines Transport
```

does not create an additional Core ontology concept.

Instead, it establishes a refinement relationship between the corresponding
semantic model elements.

The referenced identifiers SHALL resolve to compatible concepts.

Refinement SHALL preserve the constraints of the parent concept.

The resulting CKM SHALL retain enough information to determine the refinement
relationship.

---

# 9. View Mapping

A `view` declaration is a derived modeling or presentation construct.

It SHALL NOT create an additional Core ontology element.

A View MAY reference existing CKM concepts or Statements.

The View itself SHALL NOT become an Entity, Capability, Relation, or State.

---

# 10. Zoom Mapping

A `zoom` declaration is a derived modeling operation.

It SHALL NOT create an additional Core ontology element.

A Zoom MAY select or constrain the representation of existing model
information.

The underlying CKM SHALL remain unchanged by the existence of a Zoom.

---

# 11. Namespace Mapping

A namespace declaration establishes the lexical and semantic scope for
identifiers and local references.

Example:

```text
namespace home.energy
```

Namespace information SHALL be preserved during semantic processing whenever
required to resolve identity or references.

---

# 12. Import Mapping

An import declaration introduces definitions from an external model.

Imports SHALL participate in identifier resolution.

An implementation MAY:

- preserve imports explicitly, or
- resolve imported content into the resulting CKM.

Either approach is valid if semantic equivalence is preserved.

---

# 13. One-to-One Semantic Mapping

The phrase "one-to-one mapping" applies only to declarations that directly
produce a CKM object.

It SHALL NOT be interpreted as requiring every syntactic construct to become
a CKM ontology element.

Specifically:

```text
entity       → CKM Entity
state        → CKM State
statement    → CKM Statement
```

while:

```text
capability   → Capability semantic identifier
relation     → Relation semantic identifier
refinement   → semantic relationship
view         → derived representation
zoom         → derived modeling operation
namespace    → identifier scope
import       → external model resolution
version      → language version metadata
```

Capability and Relation instances are derived from valid Statements and their
applicable semantic constraints.


### DSL-local Reference Applicability

A DSL-local reference may be attached only to a declaration that creates or
identifies a canonical CKM object or a Statement. Semantic type declarations,
refinements, views, and zoom declarations are not referenceable through the
generic local-object reference mechanism unless their applicable specification
explicitly makes them referenceable.
