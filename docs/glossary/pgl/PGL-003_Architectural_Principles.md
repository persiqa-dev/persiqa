# PGL-003 --- Architectural Principles

**Document:** Persiqa Glossary (PGL)

**Chapter:** PGL-003

**Title:** Architectural Principles

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter summarizes the architectural principles reflected in the
Persiqa specifications.

These principles are provided as canonical terminology and architectural
guidance. They do not constitute an independent source of normative
architectural authority.

The normative meaning of each principle remains defined by the applicable
PAS, PDS, PMS, PRS, or PCS specification.

------------------------------------------------------------------------

# 2. Statement First

Knowledge SHALL be represented as Statements.

Statements are the canonical unit of semantic knowledge.

All higher-level knowledge representations SHALL ultimately be expressible
through Statements.

**Normative Sources:** PAS, PDS

------------------------------------------------------------------------

# 3. Universal Core

The Core SHALL contain only universal concepts.

Domain-specific concepts SHALL NOT be introduced into the Core ontology.

**Normative Sources:** PAS, PMS

------------------------------------------------------------------------

# 4. Universal Refinement

Domain-specific concepts SHALL be introduced through the applicable Core
extension mechanisms, primarily through refinement of existing Entity,
Capability, and Relation concepts.

Refinement increases semantic precision while preserving inherited meaning.

**Normative Sources:** PAS, PMS

------------------------------------------------------------------------

# 5. First-Class Relations

Relations are first-class Core ontology concepts.

A Relation possesses canonical identity, MAY own State, MAY participate in
reasoning and MAY be refined.

Relations SHALL NOT be treated merely as implementation references.

**Normative Sources:** PAS, PMS, PRS

------------------------------------------------------------------------

# 6. Incremental Truth

New knowledge SHALL refine or extend existing knowledge unless explicit
Replacement is declared.

Refinement and Replacement SHALL remain distinct semantic operations.

**Sources:** RAS, PRS

------------------------------------------------------------------------

# 7. Human First

Persiqa models SHALL remain understandable by humans.

Human readability SHALL NOT be sacrificed for implementation convenience.

Machine-processable representations SHALL preserve human semantics.

**Sources:** RAS, PDS

------------------------------------------------------------------------

# 8. Implementation Independence

The semantics of Persiqa SHALL be independent of:

- programming language,
- persistence technology,
- runtime architecture,
- serialization format.

Implementations conform to the model; the model SHALL NOT conform to
implementations.

**Normative Sources:** PMS, PDS

------------------------------------------------------------------------

# 9. Canonical Knowledge Model

The Canonical Knowledge Model (CKM) is the authoritative semantic
representation of every Persiqa model.

Parsing, validation, reasoning and serialization SHALL operate on the CKM.

**Normative Sources:** PMS, PDS, PRS

------------------------------------------------------------------------

# 10. Semantic Equivalence

Two representations are semantically equivalent if they describe the same
Canonical Knowledge Model.

Differences in syntax, formatting or implementation SHALL NOT affect
semantic meaning.

**Normative Sources:** PDS, PMS

------------------------------------------------------------------------

# 11. Conformance

Every Persiqa specification, implementation and domain ontology SHOULD
remain consistent with these architectural principles.

Where this glossary chapter conflicts with a normative specification, the
normative specification SHALL take precedence.

These principles summarize the architectural identity of Persiqa; they do
not independently define or override that architecture.
