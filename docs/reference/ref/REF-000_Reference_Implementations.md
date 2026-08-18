# REF-000 --- Reference Implementations

**Document:** Persiqa Reference Models (REF)

**Chapter:** REF-000

**Title:** Reference Implementations

**Status:** Accepted

------------------------------------------------------------------------

# Purpose

The Reference Models demonstrate complete end-to-end Persiqa modelling.

Unlike the Example Collection (PEX), which validates individual
architectural principles, the Reference Models describe realistic
systems in sufficient depth to serve as implementation, parser,
reasoning and DSL reference material.

------------------------------------------------------------------------

# Position Within the Documentation

The Persiqa documentation is organised into five complementary families.

  Family   Purpose
  -------- -------------------------------------------
  PAS      What the architecture is
  RAS      Why the architecture is designed this way
  PDR      How the architecture was discovered
  PEX      Small, focused validation examples
  REF      Complete end-to-end reference systems

------------------------------------------------------------------------

# Standard Structure

Every Reference Model SHALL contain:

1.  System overview
2.  Scope and assumptions
3.  Real-world architecture
4.  Natural language description
5.  Canonical Statements
6.  Refinement hierarchy
7.  Views
8.  Zoom levels
9.  Entity catalogue
10. Capability catalogue
11. Relation catalogue
12. State catalogue
13. Example reasoning scenarios
14. Example DSL representation
15. Validation checklist

------------------------------------------------------------------------

# Planned Reference Models

  ID        Reference Model
  --------- ------------------
  REF-001   Smart Home
  REF-002   Industrial Plant
  REF-003   Datacenter

------------------------------------------------------------------------

# Success Criteria

A Reference Model SHALL be complete enough that an independent
implementation can:

-   build the knowledge graph,
-   execute reasoning,
-   validate refinement,
-   render multiple Views,
-   render multiple Zoom levels,
-   serialize and deserialize the model,
-   verify conformance with the Persiqa Core.

Reference Models are considered executable specifications for future
Persiqa implementations.
