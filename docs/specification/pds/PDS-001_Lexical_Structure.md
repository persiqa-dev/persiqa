# PDS-001 — Lexical Structure

**Document:** Persiqa DSL Specification (PDS)

**Chapter:** PDS-001

**Title:** Lexical Structure

**Status:** Accepted

---

# 1. Purpose

This chapter defines the lexical building blocks of the Persiqa DSL.

Every conforming parser SHALL interpret the lexical structure identically.

---

# 2. Character Set

The DSL SHALL be encoded as UTF-8.

Unicode characters MAY be used in comments and string literals.

Identifiers SHOULD use ASCII letters, digits and underscore for maximum
interoperability.

---

# 3. Whitespace

Whitespace separates lexical elements.

The following characters are treated as whitespace:

- Space
- Horizontal Tab
- Line Feed
- Carriage Return

Multiple whitespace characters SHALL be equivalent to a single separator
unless they appear inside string literals.

---

# 4. Identifiers

Identifiers name model elements and local references.

Grammar:

```text
Identifier ::= Letter ( Letter | Digit | "_" )*
```

Examples:

```text
HeatPump
TemperatureSensor
connected_to
ThermalEnergy
hp_to_buffer
```

Identifiers are case-sensitive.

---

# 5. Reserved Keywords

The following keywords are reserved:

```text
entity
capability
relation
state
view
zoom
refines
extends
import
namespace
version
```

Reserved keywords SHALL NOT be used as identifiers or DSL-local references.

`statement` is intentionally not a reserved keyword. Statements are
represented directly by their semantic triple rather than by a dedicated
`statement` keyword.

---

# 6. Punctuation

The colon character is a label delimiter.

```text
:
```

A label has the lexical form:

```text
LocalReference ::= Identifier ":"
```

The label delimiter has no semantic meaning by itself. Its meaning is defined
by PDS-003 and PDS-005.

---

# 7. Literals

Supported literal types:

- String
- Integer
- Decimal
- Boolean

Example:

```text
state HeatPump Temperature = 23.5
state HeatPump Online = true
```

---

# 8. Comments

Single-line comments:

```text
// This is a comment
```

Block comments:

```text
/*
 Multi-line
 comment
*/
```

Comments SHALL have no semantic meaning.

---

# 9. File Structure

A DSL document consists of a sequence of declarations.

Example:

```text
namespace SmartHome

entity HeatPump

entity BufferTank

relation connected_to

connection: HeatPump connected_to BufferTank
```

The order of declarations SHOULD NOT affect semantics. Forward references
are permitted subject to semantic resolution rules.

---

# 10. Conformance

A conforming parser SHALL reject documents containing:

- invalid UTF-8,
- malformed identifiers,
- use of reserved keywords as identifiers or local references,
- malformed label delimiters,
- unterminated comments,
- malformed literals.

Lexical validation SHALL precede syntactic validation.
