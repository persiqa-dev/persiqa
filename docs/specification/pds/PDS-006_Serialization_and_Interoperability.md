# PDS-006 — Serialization and Interoperability

**Document:** Persiqa DSL Specification (PDS)  
**Chapter:** PDS-006  
**Title:** Serialization and Interoperability  
**Status:** Accepted

---

# 1. Purpose

This chapter defines the normative requirements for identifying Persiqa DSL
versions and for preserving semantic interoperability across parsers,
serializers, and other implementations.

The purpose of serialization is to preserve the meaning of the Canonical
Knowledge Model (CKM), not to prescribe a particular implementation format.

---

# 2. DSL Version Declaration

A Persiqa DSL document MAY declare the language version using:

```text
version <major>.<minor>
```

Example:

```text
version 1.0
```

The corresponding grammar production is defined by PDS-002:

```text
VersionDecl ::= "version" VersionNumber
```

A document SHALL contain no more than one version declaration.

If a document does not declare a version, the implementation SHALL apply the
default versioning rules defined for the relevant Persiqa DSL release.

An implementation SHALL NOT silently interpret a document as requiring a
different incompatible language version.

---

# 3. Version Semantics

The DSL version identifies the syntax and language semantics expected by the
document.

A version number consists of:

```text
major.minor
```

A change to the major version MAY introduce incompatible syntax or semantics.

A change to the minor version SHALL preserve backward compatibility unless
the corresponding release documentation explicitly defines a migration rule.

Implementations MAY support multiple DSL versions.

An implementation that cannot safely interpret the declared version SHALL
reject the document rather than silently changing its meaning.

---

# 4. Serialization Principle

The authoritative meaning of a Persiqa model is its Canonical Knowledge Model.

The DSL is one serialization and authoring representation of that model.

Therefore:

```text
DSL
  ↓ parse
CKM
  ↓ serialize
DSL
```

SHOULD preserve semantic meaning.

Formatting, ordering, whitespace, comments, and other non-semantic syntax MAY
change during serialization.

---

# 5. DSL-Local References

DSL-local references are authoring-level lookup names. They SHALL NOT
contribute to the canonical identity or semantic meaning of a CKM object.

A serializer MAY preserve local reference labels when serializing a DSL
document, but SHALL NOT require them for semantic equivalence. A serializer
MAY assign different local reference names or omit labels when the resulting
DSL remains semantically valid.

Canonical serialization SHALL NOT treat a local reference as part of the
identity of an Entity, Capability, Relation, State, or Statement.

---

# 6. Semantic Preservation

A conforming serializer SHALL preserve all semantically significant
information represented by the CKM.

This includes, where applicable:

- Entity identity,
- Capability identity and ownership,
- Relation identity,
- Relation source and target,
- Relation semantics,
- State ownership and values,
- Statement meaning,
- refinement,
- namespace information,
- imported model references.

A serializer SHALL NOT discard information merely because it is not required
by its own internal implementation.

---

# 7. Canonical Model as Interoperability Boundary

Different Persiqa implementations MAY use different:

- parsers,
- abstract syntax trees,
- storage systems,
- programming languages,
- databases,
- APIs,
- internal object models.

These implementation differences SHALL NOT change the semantics of the
resulting CKM.

The CKM is the interoperability boundary between syntax and implementation.

```text
          DSL
           │
         Parser
           │
           ▼
          CKM
           │
     ┌─────┴─────┐
     ▼           ▼
 Storage       Serializer
                   │
                   ▼
                  DSL
```

---

# 8. Round-Trip Requirement

For a semantically valid Persiqa document:

```text
D1 → parse → CKM → serialize → D2
```

D2 SHALL be semantically equivalent to D1.

D2 does not need to be textually identical to D1.

The following MAY change during a round trip:

- whitespace,
- indentation,
- declaration ordering where ordering is non-semantic,
- comments,
- formatting,
- equivalent lexical representations.

The following SHALL NOT change:

- entity identity,
- capability meaning,
- relation meaning,
- relation endpoints,
- state meaning,
- statement meaning,
- refinement semantics,
- namespace semantics.

---

# 9. Canonical Serialization

A serializer MAY provide a canonical serialization mode.

Canonical serialization SHOULD produce deterministic output for semantically
equivalent CKMs.

Canonical serialization MAY normalize:

- declaration ordering,
- whitespace,
- identifier formatting,
- literal formatting,
- namespace and import ordering.

Canonicalization SHALL NOT alter semantic meaning.

---

# 10. Views and Zooms

Views and Zooms are derived modeling or presentation constructs.

Serialization MAY preserve View and Zoom definitions when they are part of
the DSL document.

However, View and Zoom SHALL NOT be interpreted as additional Core ontology
concepts in the CKM.

Serialization of a View or Zoom therefore preserves the corresponding
modeling instruction or derived representation, not a new ontology element.

---

# 11. Imports and Namespaces

Namespace and import declarations SHALL be preserved sufficiently to maintain
identifier resolution and semantic meaning.

An implementation MAY resolve imported content into a single internal CKM.

If imported content is external to the serialized document, the serializer
MAY preserve the import declaration rather than embedding the imported model.

The resulting model SHALL remain semantically resolvable.

---

# 12. Unsupported Versions

If an implementation encounters a declared version that it does not support,
it SHALL fail explicitly.

It SHALL NOT:

- silently downgrade the version,
- silently upgrade the version,
- reinterpret incompatible syntax,
- discard unsupported semantic constructs.

An implementation MAY provide an explicit migration mechanism when a safe
migration path exists.

---

# 13. Unknown Extensions

Domain-specific extensions MAY be represented through the Persiqa refinement
and Capability mechanisms.

An implementation that does not understand a domain-specific extension MAY
reject the model if the extension is required for semantic interpretation.

An implementation SHALL NOT silently reinterpret an unknown extension as a
different Core concept.

---

# 14. Implementation Independence

The serialization format SHALL remain independent of:

- database schema,
- programming language,
- object model,
- network protocol,
- API framework,
- UI framework,
- visualization system.

A serialized Persiqa model represents infrastructure knowledge, not the
internal structure of a particular implementation.

---

# 15. Conformance

A conforming Persiqa DSL implementation SHALL:

1. parse the declared DSL version according to PDS-002;
2. reject unsupported incompatible versions;
3. preserve the semantic information required by the CKM;
4. preserve Relation source and target semantics;
5. preserve State and Statement semantics;
6. preserve refinement semantics;
7. preserve namespace and import semantics;
8. maintain semantic equivalence across valid parse/serialize round trips.

An implementation MAY use any internal representation provided that these
requirements are satisfied.

---

# 16. Relationship to Other Specifications

PDS-002 defines the grammar.

PDS-003 defines semantic validation.

PDS-004 defines canonical mapping between DSL constructs and the CKM.

PDS-006 defines serialization, versioning, and interoperability requirements.

The PMS defines the canonical object model, while the PAS remains the
normative architectural authority.

No implementation-specific serialization detail SHALL override the normative
semantics defined by the PAS, PMS, or PDS.
