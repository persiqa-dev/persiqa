# PDS-002 — Grammar

**Document:** Persiqa DSL Specification (PDS)  
**Chapter:** PDS-002  
**Title:** Grammar  
**Status:** Accepted

---

# 1. Purpose

This chapter defines the normative grammar of the Persiqa DSL.

The grammar specifies the syntactic structure of valid Persiqa documents
independently of parser implementation.

Semantic validity is defined by PDS-003 and the normative PAS/PMS
specifications.

---

# 2. Grammar Notation

The grammar uses simplified EBNF notation.

- `*` — zero or more
- `+` — one or more
- `?` — optional
- `|` — alternative
- quoted text — literal token

---

# 3. Document

```text
Document ::= VersionDecl?
             NamespaceDecl?
             ImportDecl*
             Declaration*
```

A document SHALL contain zero or one version declaration and zero or one
namespace declaration.

---

# 4. Version

```text
VersionDecl  ::= "version" VersionNumber
VersionNumber ::= Integer "." Integer
```

Example:

```text
version 1.0
```

---

# 5. Namespace and Import

```text
NamespaceDecl ::= "namespace" QualifiedName
ImportDecl    ::= "import" QualifiedName
QualifiedName ::= Identifier ("." Identifier)*
```

Examples:

```text
namespace SmartHome
import Common.Components
```

A namespace establishes the lexical namespace of the containing document.
Import semantics are defined by the applicable namespace/import rules.

---

# 6. Declarations and Local References

A declaration MAY be preceded syntactically by an optional DSL-local
reference. Whether a declaration is eligible to bind such a reference is a
semantic constraint defined by PDS-003.

```text
Declaration ::= LocalReference? DeclarationBody

LocalReference ::= Identifier ":"

A LocalReference MAY prefix only a declaration that creates or identifies a
canonical CKM object or a Statement. Semantic type declarations, refinement
declarations, views, and zoom declarations SHALL NOT be assigned local object
references unless their applicable specification explicitly defines them as
referenceable objects.

DeclarationBody ::= EntityDecl
                  | CapabilityDecl
                  | RelationDecl
                  | StateDecl
                  | StatementDecl
                  | RefinementDecl
                  | ViewDecl
                  | ZoomDecl
```

The local reference is a label used for subsequent semantic resolution. It
is not part of the canonical identity or semantic meaning of the declaration.

Examples:

```text
pump: entity HeatPump
connection: HeatPump connected_to BufferTank
rssi: state connection RSSI = -58
```

A local reference SHALL be unique within its lexical scope. Scope and
reference resolution are defined by PDS-003 and validated by PDS-005.

---

# 7. Entity Declaration

```text
EntityDecl ::= "entity" Identifier
```

Example:

```text
entity HeatPump
```

An Entity declaration introduces an Entity identifier into the document.

---

# 8. Capability Declaration

```text
CapabilityDecl ::= "capability" Identifier
```

Example:

```text
capability Transport
```

A Capability declaration introduces a Capability semantic identifier.

A concrete Capability owned by an Entity is established by a semantically
valid Statement expressing that ownership.

Example:

```text
transport: HeatPump has Transport
```

---

# 9. Relation Declaration

```text
RelationDecl ::= "relation" Identifier
```

Example:

```text
relation connected_to
```

A Relation declaration introduces a Relation semantic identifier.

A concrete Relation instance is established by a semantically valid
Statement whose predicate identifies the Relation semantics and whose subject
and object identify the source and target Entities.

Example:

```text
connection: HeatPump connected_to BufferTank
```

The resulting Relation SHALL conform to the binary Relation Model: exactly
one source Entity and exactly one target Entity.

---

# 10. Refinement

```text
RefinementDecl ::= Identifier "refines" Identifier
```

Example:

```text
FluidTransport refines Transport
```

The referenced identifiers SHALL resolve during semantic analysis.

Refinement SHALL preserve the semantic constraints defined by the applicable
Core concept.

---

# 11. Statement

A Statement is written directly as a subject-predicate-object expression.
There is no `statement` keyword.

```text
StatementDecl ::= Subject Predicate Object

Subject   ::= Identifier
Predicate ::= Identifier
Object    ::= Identifier | Literal

Literal ::= StringLiteral
          | IntegerLiteral
          | DecimalLiteral
          | BooleanLiteral

StatementRef ::= Identifier
```

Examples:

```text
HeatPump connected_to BufferTank
HeatPump has Transport
TemperatureSensor measures Temperature
```

A Statement SHALL be semantically validated after parsing.

The subject, predicate and object are syntactic identifiers or literals;
reference resolution determines their semantic kinds.

---

# 12. State

```text
StateDecl ::= "state"
              Identifier
              Identifier
              "="
              Literal
```

Examples:

```text
state HeatPump Temperature = 22.8
state HeatPump Online = true
state connection RSSI = -58
```

The first identifier identifies the State owner expression. It SHALL resolve
to an Entity or Relation, either directly or through an explicitly defined
semantic derivation.

State semantics are defined by PDS-003.

---

# 13. View

```text
ViewDecl ::= "view" Identifier "{"
             StatementRef*
             "}"
```

A View defines a derived representation of the knowledge model.

A View SHALL NOT create an additional Core ontology concept.

---

# 14. Zoom

```text
ZoomDecl ::= "zoom" Integer "{"
             StatementRef*
             "}"
```

Each Zoom SHALL describe the same modeled reality at a different level of
representation.
