# PDS-005 — Validation Rules

**Document:** Persiqa DSL Specification (PDS)

**Chapter:** PDS-005

**Title:** Validation Rules

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the normative validation rules for Persiqa DSL documents
and Canonical Knowledge Models (CKM).

Validation SHALL determine whether a model is syntactically, semantically and
structurally valid.

------------------------------------------------------------------------

# 2. Validation Pipeline

Validation SHALL be performed in the following order:

```text
Lexical Validation
        ↓
Syntactic Validation
        ↓
Declaration Collection
        ↓
Reference Resolution
        ↓
Semantic Validation
        ↓
Refinement Validation
        ↓
Model Consistency Validation
```

A stage SHALL NOT execute if a preceding stage has failed.

------------------------------------------------------------------------

# 3. Lexical Validation

Lexical validation SHALL verify:

- UTF-8 encoding
- Valid identifiers
- Valid literals
- Reserved keyword usage
- Valid label delimiters
- Properly terminated comments

Lexical errors SHALL prevent further processing.

------------------------------------------------------------------------

# 4. Syntactic Validation

Syntactic validation SHALL verify:

- Grammar conformance
- Complete declarations
- Statement structure
- Valid block structure
- Valid namespace and import declarations
- Valid optional local-reference labels

Malformed documents SHALL be rejected.

------------------------------------------------------------------------

# 5. Declaration Collection

Before resolving references, the implementation SHALL collect declarations and
DSL-local references within their applicable scopes.

Declaration order SHALL NOT affect semantic validity unless explicitly
required by another specification.

Forward references SHALL therefore be supported.

------------------------------------------------------------------------

# 6. Reference Resolution

Reference validation SHALL verify:

- Every referenced identifier resolves to exactly one applicable object.
- Every DSL-local reference is unique within its lexical scope.
- A canonical CKM object is bound to at most one DSL-local reference within the same scope.
- References do not escape their scope unless explicitly imported or
  qualified.
- Qualified references resolve through the applicable namespace/import rules.
- Unresolved references are errors.
- Ambiguous references are errors.
- Cyclic semantic derivation is rejected.

A DSL-local reference is a lookup name only. It SHALL NOT contribute to the
canonical identity or semantic meaning of the referenced CKM object.

Semantic derivation SHALL occur only where explicitly defined by the
applicable semantic rule.

Implementations SHALL NOT perform arbitrary dereferencing until a requested
type happens to match.

------------------------------------------------------------------------

# 7. Type Compatibility

Resolved references SHALL be compatible with the semantic context in which
they are used.

The following minimum constraints SHALL hold:

```text
Statement Subject → Entity | Relation
State Owner       → Entity | Relation
Capability Owner  → Entity
Relation Source   → Entity
Relation Target   → Entity
```

A reference of an incompatible kind SHALL be rejected as a semantic type
error.

------------------------------------------------------------------------

# 8. Semantic Validation

Semantic validation SHALL verify:

- All referenced elements exist and resolve unambiguously.
- Element kinds are compatible.
- Statements have valid semantic meaning.
- Capability ownership conforms to the Capability Model.
- Relation endpoints conform to the binary Relation Model.
- State ownership conforms to the State Model.
- Semantic derivations used for references are explicitly defined.

------------------------------------------------------------------------

# 9. Refinement Validation

Implementations SHALL verify:

- No refinement cycles exist.
- Every refinement has a valid parent.
- Identity is preserved.
- Refinement never contradicts the Core ontology.

Invalid refinement SHALL invalidate the model.

------------------------------------------------------------------------

# 10. Consistency Validation

Consistency validation SHALL verify:

- Canonical identity uniqueness
- Duplicate declaration handling
- Namespace consistency
- Import consistency
- Statement consistency
- State conflicts

Implementations MAY additionally report redundant Statements as warnings.

------------------------------------------------------------------------

# 11. Diagnostics

Validation results SHALL distinguish between:

| Level | Meaning |
|---|---|
| Error | The model is invalid. |
| Warning | The model is valid but potentially problematic. |
| Information | Additional implementation guidance. |

Errors SHALL prevent CKM generation.

Warnings SHALL NOT.

------------------------------------------------------------------------

# 12. Conformance

Two conforming validators SHALL reach the same validation outcome for the same
Canonical Knowledge Model.

Implementation-specific diagnostics MAY differ in wording, but SHALL identify
the same violated normative rule.
