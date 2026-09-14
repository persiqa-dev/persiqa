# PDS-007 --- Relation Type and Knowledge Context Syntax

**Document:** Persiqa DSL Specification (PDS)  
**Chapter:** PDS-007  
**Title:** Relation Type and Knowledge Context Syntax  
**Status:** Draft  
**Version:** 1.1

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the minimal DSL extension required to express PMS-009 Relation Type contracts and the Statement knowledge context required by PMS-008.

It is additive to PDS-002. Existing valid documents remain valid.

# 2. Relation Type declaration

A Relation Type declaration introduces a locally named registry contract.

    RelationTypeDecl ::= "relation-type" Identifier "{" RelationTypeField* "}"

    RelationTypeField ::= "source" EndpointProfile
                        | "target" EndpointProfile
                        | "inverse" Identifier | "none"
                        | "symmetric" BooleanLiteral
                        | "inference" InferencePolicy
                        | "cardinality" CardinalityPolicy
                        | "conflict" ConflictPolicy

    EndpointProfile ::= EndpointKind ("|" EndpointKind)*
    EndpointKind    ::= "Entity" | "Capability" | "Relation" | "State"
                      | "Statement" | "Concept" | "TypedValue"

A declaration SHALL provide source, target, symmetric, and inference fields. Omitted optional fields inherit the conservative defaults in PMS-009.

Example:

    relation-type connectedTo {
      source Entity
      target Entity
      inverse connectedTo
      symmetric true
      inference none
    }

A declaration SHALL resolve to one effective PMS-009-compatible registry contract. It SHALL NOT redefine a baseline identifier with incompatible meaning.

# 3. Statement context annotation

A Statement MAY carry an explicit knowledge context.

    ContextStatementDecl ::= LocalReference? Assertion ContextBlock?

    ContextBlock ::= "[" ContextField ("," ContextField)* "]"

    ContextField ::= "kind" KnowledgeKind
                   | "provenance" StringLiteral
                   | "confidence" DecimalLiteral
                   | "observedAt" StringLiteral
                   | "validFrom" StringLiteral
                   | "validTo" StringLiteral
                   | "derivedFrom" ReferenceList
                   | "rule" Identifier
                   | "scenario" Identifier

    KnowledgeKind ::= "explicit" | "derived"

    ReferenceList ::= Identifier ("|" Identifier)*

Example explicit assertion:

    s101: MCB-01 supplies JunctionBox-03
      [kind explicit, provenance "inspection-A", confidence 0.8]

Example derived assertion:

    s103: MCB-01 supplies Outlet-01
      [kind derived, derivedFrom s101|s102, rule electricalSupplyComposition]

Whitespace and line wrapping inside a ContextBlock are non-semantic.

# 4. Semantic rules

An unannotated Statement is explicit knowledge with unspecified provenance, confidence, and time; it SHALL NOT acquire invented metadata.

A Statement whose kind is derived SHALL provide at least one derivedFrom reference and a rule. Each reference SHALL resolve to a Statement, Relation, or other knowledge item accepted by the declared rule.

A context annotation SHALL describe the Statement, not redefine the identity of its subject, target, Relation, or State. Equal-looking Statements with different context SHALL remain separately identifiable.

Time values are lexical strings in this version. Their standard format and temporal type system remain open under PMS-008.

# 5. Fixture profile

A machine-readable CKM v0.1 fixture SHALL contain:

1. a VersionDecl of 1.1;
2. all non-baseline Relation Type declarations used by the fixture;
3. every Statement required to build the test input;
4. context annotations for explicit/derived, provenance, time, and confidence where the test expects them; and
5. an adjacent expected-result manifest.

The exact manifest serialization is outside the DSL and is defined by the testkit.

# 6. Conformance

A PDS 1.1 implementation claiming knowledge-context support SHALL parse valid declarations in sections 2 and 3, validate them against PMS-009, preserve Statement context through serialization, and reject missing derivation support for a derived Statement.


