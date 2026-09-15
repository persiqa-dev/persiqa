# PAS-010 --- Logical Persistence Model

**Document:** Persiqa Architecture Specification (PAS)  
**Chapter:** PAS-010  
**Title:** Logical Persistence Model  
**Status:** Draft  
**Version:** 0.1

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the logical persistence model for CKM v0.1. It maps the semantics of PMS-008, PMS-009, and the Statement-first write model to durable records without allowing a database schema to redefine the Core.

It is technology-neutral. PostgreSQL, Flyway, ORM mappings, indexes, and physical value encoding are subsequent implementation decisions.

# 2. Persistence principles

1. Persistence SHALL preserve canonical identity, not invent it.
2. Explicit Statements, derived Statements, and canonical Relations SHALL remain separate records.
3. Relation identity SHALL NOT be constrained by the tuple of type, source, and target.
4. Context, provenance, confidence, temporal scope, conflicts, and derivation SHALL be append-preserving.
5. Views and presentation metadata SHALL NOT alter canonical knowledge.
6. Missing knowledge SHALL remain absent; storage SHALL NOT synthesize UNKNOWN values.

# 3. Logical record families

| Family | Purpose | Minimum identity |
|---|---|---|
| ModelScope | Boundary for one canonical knowledge model | scope_id |
| CanonicalObject | Common addressable identity and kind | object_id, kind |
| Entity | Entity continuity identity | object_id |
| Capability | Reusable capability concept or instance | object_id |
| RelationType | Registry contract/version | relation_type_id, version |
| Relation | Canonical semantic association | relation_id |
| State | Contextual condition owned by Entity or Relation | state_id |
| Statement | Explicit or derived knowledge assertion | statement_id |
| StatementContext | Provenance, confidence, temporal/scenario context | statement_id, context_id |
| Derivation | Evidence and rule links for derived knowledge | derived_statement_id, evidence_id |
| Canonicalization | Traceable association between Statement and canonical object | statement_id, object_id |
| Representation | Optional view/layout metadata | representation_id |

# 4. Canonical object layer

Every addressable CKM object SHALL have one CanonicalObject record:

    canonical_object
      object_id
      model_scope_id
      kind
      created_at
      retired_at (optional)

The record supplies stable storage addressability only. It SHALL NOT encode domain type, display label, lifecycle State, or source-system identifier as a replacement for CKM semantics.

Entity, Capability, Relation, State, and Statement records reference this common object identity where applicable.

# 5. Relation Type and Relation

    relation_type
      relation_type_id
      version
      semantic_identifier
      source_profile
      target_profile
      inverse_identifier
      symmetric
      inference_policy
      cardinality_policy
      conflict_policy
      lifecycle_status

    relation
      relation_id
      relation_type_id
      source_object_id
      target_object_id

A unique constraint on relation_type_id, source_object_id, and target_object_id SHALL NOT exist. A unique relation_id SHALL exist.

Relation Type version is part of semantic interpretation. A migration changing endpoint, inference, or conflict semantics SHALL produce an explicit Relation Type version or replacement, not silently rewrite prior meaning.

# 6. State

    state
      state_id
      owner_object_id
      predicate
      value_reference or typed_value
      context_key

State ownership SHALL reference exactly one Entity or Relation. State has contextual identity; a physical primary key is not evidence of independent Entity-like continuity. Historical, observed, planned, and conflicting States MAY coexist when their Statement contexts differ.

# 7. Statement-first knowledge

    statement
      statement_id
      knowledge_kind             -- explicit | derived
      predicate
      subject_object_id
      object_object_id (optional)
      typed_value (optional)

    statement_context
      context_id
      statement_id
      provenance_reference
      confidence
      observed_at
      valid_from
      valid_to
      scenario

    derivation
      derived_statement_id
      evidence_statement_id or evidence_object_id
      rule_identifier

A Statement SHALL have exactly one subject and predicate, plus exactly one object reference or typed value. A derived Statement SHALL retain one or more Derivation records. Statement equality SHALL NOT be enforced using only subject, predicate, and object.

# 8. Canonicalization and conflict

    canonicalization
      statement_id
      canonical_object_id
      mode                      -- asserts | supports | derives | disputes
      policy_identifier

Canonicalization is a traceable policy result, not a destructive upsert. Multiple Statements MAY support, dispute, or derive the same canonical Relation or State. Conflicts SHALL be represented by context and/or explicit conflict records; they SHALL NOT require deletion of competing Statements.

# 9. Representations

    representation
      representation_id
      model_scope_id
      name
      selection_definition
      layout_metadata

Representations are projections of ModelScope knowledge. They SHALL NOT own or duplicate canonical object identity. Layout, grouping, and styling metadata are presentation concerns.

# 10. Integrity rules

A physical implementation SHALL enforce or validate:

- all referenced canonical objects belong to the same ModelScope;
- Relation endpoints satisfy the effective Relation Type contract;
- State owner is Entity or Relation;
- derived Statements have derivation evidence;
- no Relation Type tuple uniqueness is introduced;
- Statement context remains append-preserving;
- canonicalization and conflict links resolve;
- representation records do not mutate canonical semantics.

# 11. PostgreSQL/Flyway readiness

The first PostgreSQL/Flyway implementation SHOULD use UUID primary keys, immutable Statement rows, append-only StatementContext/Derivation rows, foreign keys for structural integrity, and indexes for relation endpoint traversal, Statement subject/predicate lookup, and context-time queries.

It SHALL not introduce a single generic type column that collapses instance, classification, role, capability, and State semantics.

# 12. Open physical-design decisions

1. typed value representation and units;
2. context interval indexing;
3. JSON versus normalized storage for Relation Type policies;
4. event/audit timestamps and actor identity;
5. transaction boundaries for canonicalization;
6. retention and archival policy; and
7. multi-scope federation and import identity resolution.

