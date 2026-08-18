# PAS-001 --- First Principles

**Document:** Persiqa Architecture Specification (PAS)

**Chapter:** PAS-001

**Title:** First Principles

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the fundamental principles from which every other
part of the Persiqa Architecture Specification is derived.

These principles are normative.

------------------------------------------------------------------------

# 2. First Principle --- Model Reality

Persiqa SHALL model reality rather than implementations.

Implementations are temporary.

Reality is the source of truth.

Consequences:

-   A Java class is not a model element.
-   A database table is not a model element.
-   A REST endpoint is not a model element.

Only concepts that exist in the modeled reality belong to the Core.

------------------------------------------------------------------------

# 3. Second Principle --- Model Knowledge

Persiqa does not claim to model reality perfectly.

Persiqa models the current knowledge about reality.

Therefore every model is considered valid for the knowledge available at
the time it was created.

Knowledge grows incrementally.

------------------------------------------------------------------------

# 4. Third Principle --- Statements First

The fundamental unit of knowledge SHALL be the Statement.

Everything stored by Persiqa SHALL be expressible as one or more
Statements.

The ontology exists to interpret Statements.

The persistence layer stores Statements.

Reasoning consumes and produces Statements.

------------------------------------------------------------------------

# 5. Fourth Principle --- Universal Core

The Core SHALL contain only concepts that are universal across
independent infrastructure domains.

A concept SHALL NOT become part of the Core because it is useful in a
single domain.

------------------------------------------------------------------------

# 6. Fifth Principle --- Refinement

Domain knowledge SHALL be expressed through refinement.

The preferred solution is always refinement of an existing concept.

Introducing a new Core concept is the last resort.

This principle applies equally to:

-   Entity
-   Capability
-   Relation

------------------------------------------------------------------------

# 7. Sixth Principle --- Incremental Truth

Refinement SHALL add knowledge.

Refinement SHALL NOT invalidate previously valid Statements.

Earlier Statements remain true unless explicitly replaced by newer
knowledge.

------------------------------------------------------------------------

# 8. Seventh Principle --- Stable Core

The Core is expected to evolve significantly slower than domain models.

The success of Persiqa depends on keeping the Core small, stable and
implementation independent.

------------------------------------------------------------------------

# 9. Eighth Principle --- Human First

The language SHALL be understandable by domain experts.

A plumber, electrician, automation engineer and software engineer SHOULD
be able to describe their infrastructure using the same conceptual
language.

Natural language readability SHALL take precedence whenever it does not
reduce semantic precision.

------------------------------------------------------------------------

# 10. Design Consequences

The first principles imply that:

-   the Core ontology remains intentionally minimal;
-   domain knowledge grows through refinement;
-   implementations derive from the specification;
-   Statements are the canonical representation of knowledge;
-   no implementation technology influences the Core architecture.

------------------------------------------------------------------------

# 11. Acceptance Criteria

Any future architectural proposal SHALL answer the following questions:

1.  Does it model reality or implementation?
2.  Can it be expressed as Statements?
3.  Can it be represented by refinement instead of a new Core concept?
4.  Has it been validated across multiple independent domains?
5.  Does it preserve Core simplicity?

If any answer is "No", the proposal SHALL NOT become part of the Persiqa
Core.
