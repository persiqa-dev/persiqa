# PAS-008 --- Knowledge Model

**Document:** Persiqa Architecture Specification (PAS)

**Chapter:** PAS-008

**Title:** Knowledge Model

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines how knowledge evolves within a Persiqa model.

The Core ontology is intentionally stable.

Knowledge changes through modelling operations rather than by changing
the ontology.

------------------------------------------------------------------------

# 2. Knowledge Principle

A Persiqa model SHALL be considered valid at every level of knowledge.

Knowledge is expected to grow incrementally.

Incomplete knowledge SHALL NOT invalidate a model.

------------------------------------------------------------------------

# 3. Modelling Operations

The Persiqa Core defines three modelling operations:

-   Refine
-   View
-   Zoom

These operations are not ontology concepts.

They describe how users interact with knowledge.

------------------------------------------------------------------------

# 4. Refine

## Definition

Refinement increases the precision of the model.

It adds new knowledge without invalidating existing knowledge.

### Example

Initial knowledge:

``` text
PC connected_to Switch.
```

Refined knowledge:

``` text
PC composed_of Network Interface.

Network Interface connected_to Patch Cable.

Patch Cable connected_to Wall Outlet.

Wall Outlet connected_to Patch Panel.

Patch Panel connected_to Switch.
```

The original Statement remains valid.

### Rules

KM-001

Refinement SHALL add knowledge.

KM-002

Refinement SHALL preserve Incremental Truth.

KM-003

Refinement SHALL NOT remove valid knowledge unless explicitly
superseded.

------------------------------------------------------------------------

# 5. View

## Definition

A View presents the same knowledge from a different perspective.

Examples:

-   Network View
-   Electrical View
-   Water View
-   Security View

### Rules

KM-004

A View SHALL NOT modify the underlying model.

KM-005

Multiple Views MAY coexist.

------------------------------------------------------------------------

# 6. Zoom

## Definition

Zoom changes the level of detail presented to the user.

Zoom affects presentation only.

### Example

Zoom Level 1

``` text
PC connected_to Switch.
```

Zoom Level 2

``` text
Network Interface connected_to Patch Cable.

Patch Cable connected_to Wall Outlet.

Wall Outlet connected_to Patch Panel.

Patch Panel connected_to Switch.
```

### Rules

KM-006

Zoom SHALL NOT modify knowledge.

KM-007

Zoom SHALL NOT create new Statements.

KM-008

Zoom SHALL preserve semantic equivalence.

------------------------------------------------------------------------

# 7. Knowledge Evolution

Knowledge typically follows this lifecycle:

Unknown

↓

Initial Statements

↓

Refinement

↓

Additional Statements

↓

Alternative Views

↓

Different Zoom Levels

At every stage the model remains valid for the knowledge currently
available.

------------------------------------------------------------------------

# 8. Validation

The knowledge model has been validated across multiple domains
including:

-   IT infrastructure
-   Home automation
-   Electrical systems
-   Water systems
-   HVAC
-   PLC / OT
-   Building modelling

In every domain, incremental refinement proved sufficient without
requiring ontology changes.

------------------------------------------------------------------------

# 9. Rationale

The Knowledge Model separates the evolution of knowledge from the
evolution of the Core.

The ontology changes rarely.

Knowledge changes continuously.

This separation enables long-term stability while allowing models to
grow naturally as understanding improves.
