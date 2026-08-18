# PAS-005 --- Capability Model

**Document:** Persiqa Architecture Specification (PAS)

**Chapter:** PAS-005

**Title:** Capability Model

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the normative Capability model of the Persiqa Core.

Capabilities describe what an Entity is capable of, independent of its
current State or implementation.

------------------------------------------------------------------------

# 2. Definition

A Capability is a timeless ability of an Entity.

A Capability answers the question:

> "What is this Entity capable of doing?"

It SHALL NOT answer:

> "What is this Entity doing right now?"

Current behaviour SHALL be represented by State.

------------------------------------------------------------------------

# 3. Characteristics

A Capability:

-   SHALL belong to exactly one Entity.
-   SHALL be implementation independent.
-   SHALL be timeless.
-   MAY be refined.
-   SHALL remain valid regardless of current State.

------------------------------------------------------------------------

# 4. Universal Capability Families

The Persiqa Core intentionally defines only a small set of universal
Capability families.

Current families are:

-   Transport
-   Store
-   Transform
-   Measure
-   Control

These families represent universal patterns observed across independent
domains.

The Core SHALL NOT introduce domain-specific Capability families.

------------------------------------------------------------------------

# 5. Capability Refinement

Domain knowledge SHALL refine a Capability family rather than replace
it.

Examples:

## Transport

-   Transport Water
-   Transport Air
-   Transport Gas
-   Transport Information
-   Transport Electrical Energy

## Store

-   Store Water
-   Store Information
-   Store Electrical Energy
-   Store Goods

## Transform

-   Transform Electrical Energy → Heat
-   Transform Light → Electrical Energy
-   Transform Information → Information

## Measure

-   Measure Temperature
-   Measure Pressure
-   Measure Voltage
-   Measure Position
-   Measure Image

## Control

-   Control Flow
-   Control Heating
-   Control Circuit
-   Control Access

------------------------------------------------------------------------

# 6. Refinement Rules

CR-001

Every refined Capability SHALL remain semantically compatible with its
parent Capability.

CR-002

Refinement SHALL increase precision without changing meaning.

CR-003

Refinement SHALL NOT create a new Core Capability family.

------------------------------------------------------------------------

# 7. Validation

The Capability families have been validated across multiple independent
domains.

Examples include:

  -----------------------------------------------------------------------
  Domain             Capability Examples
  ------------------ ----------------------------------------------------
  Networking         Transport Information

  Electrical         Transport Electrical Energy, Transform Electrical
                     Energy

  Water              Transport Water, Control Flow

  HVAC               Measure Temperature, Control Heating

  PLC / OT           Measure Position, Control Circuit

  Robotics           Transform Energy, Transport Objects

  Logistics          Store Goods, Transport Goods

  Healthcare         Measure Vital Signs

  Building           Control Access
  -----------------------------------------------------------------------

The same Capability families were sufficient across all validated
domains.

------------------------------------------------------------------------

# 8. Non-Goals

The following are NOT Core Capability families:

-   NAT
-   Route
-   Authenticate
-   Capture
-   Filter
-   Assemble

These are expressed as domain-specific refinements, combinations of
universal Capability families, or higher-level semantics.

------------------------------------------------------------------------

# 9. Rationale

Repeated validation demonstrated that expanding the Core Capability set
was unnecessary.

Whenever a new Capability appeared to be required, it could be expressed
as a refinement of an existing universal Capability family.

This observation forms part of the Universal Refinement Principle and is
considered a foundational architectural property of the Persiqa Core.
