# PGL-002 --- Domain Vocabulary

**Document:** Persiqa Glossary (PGL)

**Chapter:** PGL-002

**Title:** Domain Vocabulary

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the relationship between the universal Persiqa Core
vocabulary and domain-specific vocabulary.

Its objective is to ensure that domain models extend the Core without
modifying it.

------------------------------------------------------------------------

# 2. Core Vocabulary

Core vocabulary consists exclusively of the canonical Core ontology
concepts defined by the Persiqa specifications.

The Core SHALL remain stable across all domains.

The Core SHALL NOT contain domain-specific terminology.

------------------------------------------------------------------------

# 3. Domain Vocabulary

Domain vocabulary represents concepts belonging to a particular
application domain.

Examples include:

-   Router
-   Heat Pump
-   PLC
-   Kubernetes Worker
-   Infusion Pump

These concepts are not Core concepts.

They SHALL be introduced through Refinement.

------------------------------------------------------------------------

# 4. Refinement Rule

Every domain concept SHALL refine an existing canonical concept.

Examples:

``` text
Router
    refines Network Device
        refines Device
            refines Entity
```

``` text
Heat Pump
    refines Pump
        refines Equipment
            refines Entity
```

------------------------------------------------------------------------

# 5. Domain Independence

The meaning of the Core SHALL NOT depend on any domain vocabulary.

Removing a domain ontology SHALL NOT change the semantics of the Core.

------------------------------------------------------------------------

# 6. Cross-Domain Consistency

Different domains MAY define different refinement hierarchies while
sharing the same Core.

Examples:

-   Networking
-   Electrical Systems
-   Water Distribution
-   Industrial Automation
-   Healthcare
-   Cloud Infrastructure

All SHALL remain semantically compatible through the Core.

------------------------------------------------------------------------

# 7. Ontology Extension

A domain ontology MAY introduce:

-   refined Entities,
-   refined Capabilities,
-   refined Relations.

It SHALL NOT introduce new first-class concepts.

------------------------------------------------------------------------

# 8. Compatibility

A domain ontology is Persiqa-compatible if it:

-   preserves Core semantics,
-   uses Refinement correctly,
-   satisfies all CKM invariants,
-   remains reasoning compatible.

------------------------------------------------------------------------

# 9. Reference Examples

The PEX and REF document families provide reference examples
demonstrating correct application of domain vocabulary across multiple
engineering disciplines.

------------------------------------------------------------------------

# 10. Conformance

A conforming Persiqa domain model SHALL extend the Core exclusively
through Refinement.

The universal Core remains authoritative.

Domain vocabulary specializes the Core but never replaces it.
