# PGL-000 — Glossary Introduction

**Document:** Persiqa Glossary (PGL)  
**Chapter:** PGL-000  
**Title:** Glossary Introduction  
**Status:** Accepted

---

# 1. Purpose

The Persiqa Glossary defines the canonical terminology used throughout the
Persiqa architecture, specifications, examples, and reference material.

The glossary exists to ensure that the same terms are used consistently
across the Persiqa documentation and implementation ecosystem.

The glossary is a terminology reference. It is not an independent source of
architectural or semantic authority.

---

# 2. Authority

The normative authority for Persiqa is defined by the applicable
specifications.

In particular:

- **PAS** defines the normative architecture.
- **PDS** defines the Persiqa DSL.
- **PMS** defines the canonical object model.
- **PRS** defines normative reasoning behavior.
- **PCS** defines conformance requirements.
- **PGL** defines and maintains canonical terminology.

PGL SHALL NOT override a normative definition in PAS, PDS, PMS, PRS, or PCS.

If a glossary definition conflicts with a normative specification, the
normative specification takes precedence and the glossary SHALL be corrected.

---

# 3. Single Source of Truth

For each terminology entry, PGL is the canonical location for the preferred
term, its concise meaning, and its terminology relationships.

Other Persiqa documents SHOULD reference PGL terminology rather than
introducing competing definitions.

This does not prevent normative specifications from defining a concept in
detail. The specification remains authoritative for the semantics of that
concept; PGL provides the canonical terminology used to refer to it.

---

# 4. Core Terminology

The current Core ontology consists of four ontological concepts:

- Entity
- Capability
- Relation
- State

Statement is the fundamental unit of expressed knowledge and is documented
as such in the relevant specifications. Statement is not a fifth Core
ontology concept.

The distinction SHALL be preserved in terminology:

```text
Core Ontology
├── Entity
├── Capability
├── Relation
└── State

Knowledge Representation
└── Statement
```

---

# 5. Historical Terminology

Persiqa has evolved through incremental architectural discovery.

Earlier documents may contain terminology that has since been superseded.

Examples include:

```text
Relationship → Relation
Projection   → derived representation / modeling operation
```

Historical documents MAY retain their original terminology when doing so is
necessary to preserve the historical record.

Current documents SHALL use the current canonical terminology unless they are
explicitly describing a historical state of the model.

---

# 6. Terminology Maintenance

A terminology change SHOULD be introduced only when the existing term is
ambiguous, misleading, or incompatible with the current model.

When a canonical term changes:

1. the current PGL entry SHALL be updated;
2. normative specifications SHALL be updated where required;
3. relevant examples and reference models SHOULD be updated;
4. historical documents SHOULD retain their original wording when that
   wording is part of the historical record;
5. the relationship between the old and new terms SHOULD be recorded.

The glossary SHALL NOT be used to silently redefine an architectural concept.

---

# 7. Scope

PGL may contain:

- canonical Core terminology;
- specification terminology;
- domain vocabulary used by Persiqa;
- synonyms and deprecated terms;
- terminology relationships;
- concise definitions and usage guidance.

Detailed architectural rules, grammar, object schemas, reasoning rules, and
conformance requirements belong to their respective normative specifications.

---

# 8. Relationship to Other Documentation

The documentation hierarchy is:

```text
Normative Specifications
    │
    ├── PAS
    ├── PDS
    ├── PMS
    ├── PRS
    └── PCS
          │
          ▼
Canonical Terminology
    │
    └── PGL
          │
          ▼
Examples / Reference Models / Other Documentation
```

PGL provides a shared vocabulary across these documents but does not replace
their normative authority.

---

# 9. Conformance

A Persiqa document SHOULD use canonical PGL terminology.

A document MAY use domain-specific terminology when required by its subject
matter, provided that the relationship to the canonical Persiqa terminology
is clear.

Normative Persiqa specifications SHALL use the current canonical terminology
unless explicitly documenting historical terminology.

---

# 10. Summary

PGL is the canonical terminology reference for Persiqa.

It provides one authoritative place for naming and concise terminology
definitions while leaving semantic and architectural authority with the
appropriate normative specifications.

This separation prevents terminology from becoming a competing source of
truth while allowing the Persiqa documentation to maintain a consistent
language.
