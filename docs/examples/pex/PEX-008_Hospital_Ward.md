# PEX-008 --- Hospital Ward

**Document:** Persiqa Example Collection (PEX)

**Chapter:** PEX-008

**Title:** Hospital Ward

**Status:** Accepted

------------------------------------------------------------------------

# 1. Real-World Scenario

A hospital ward consists of:

-   Patient
-   Bedside Monitor
-   Infusion Pump
-   Ventilator
-   Nurse Call System
-   Central Monitoring Station
-   Medical Gas Outlet
-   Oxygen Supply
-   Nurse

The objective is to model a clinical environment using only Persiqa Core
concepts.

------------------------------------------------------------------------

# 2. Natural Language Description

The Bedside Monitor measures patient vital signs.

The Infusion Pump transports medication.

The Ventilator transports air.

The Medical Gas Outlet transports oxygen.

The Nurse Call System transports information.

The Central Monitoring Station measures patient status.

The Nurse controls patient care.

------------------------------------------------------------------------

# 3. Initial Statements

``` text
Patient connected_to Bedside Monitor.
Patient connected_to Ventilator.
Infusion Pump connected_to Patient.

Bedside Monitor measures Vital Signs.
Infusion Pump transports Medication.
Ventilator transports Air.
Medical Gas Outlet transports Oxygen.
```

The model intentionally starts with essential knowledge only.

------------------------------------------------------------------------

# 4. Refinement

Additional knowledge becomes available.

``` text
Medical Gas Outlet connected_to Oxygen Supply.

Bedside Monitor connected_to Central Monitoring Station.

Nurse Call System connected_to Central Monitoring Station.

Nurse connected_to Nurse Call System.
```

Later:

``` text
Bedside Monitor
    composed_of ECG Module

ECG Module
    measures Heart Rhythm
```

Earlier Statements remain valid.

Only precision has increased.

------------------------------------------------------------------------

# 5. Views

## Clinical View

Patient ← Infusion Pump

Patient ← Ventilator

Patient ← Bedside Monitor

## Monitoring View

Bedside Monitor → Central Monitoring Station

Nurse Call System → Central Monitoring Station

## Therapy View

Infusion Pump transports Medication.

Ventilator transports Air.

Medical Gas Outlet transports Oxygen.

------------------------------------------------------------------------

# 6. Zoom Levels

## Zoom 1

``` text
Patient receives Clinical Care.
```

## Zoom 2

``` text
Patient connected_to Bedside Monitor.
Patient connected_to Ventilator.
Infusion Pump connected_to Patient.
```

## Zoom 3

``` text
Medical Gas Outlet connected_to Oxygen Supply.

Bedside Monitor connected_to Central Monitoring Station.

Bedside Monitor composed_of ECG Module.

ECG Module measures Heart Rhythm.
```

Each zoom level represents the same ward with increasing precision.

------------------------------------------------------------------------

# 7. Validation

This example demonstrates that:

-   Medical devices are Entity refinements.
-   Measurement, transport and control remain universal Capabilities.
-   Clinical and communication links are Relations.
-   Vital signs, alarm states and operating modes belong to Entity
    State.
-   Connection quality and gas flow belong to Relation State.
-   Refinement preserves previous Statements.

------------------------------------------------------------------------

# 8. Final Observations

Healthcare introduces specialised terminology but not specialised
ontology.

The same Persiqa Core models clinical equipment, patient monitoring,
therapy delivery and communication without introducing
healthcare-specific Core concepts.

This further validates that Persiqa models reality through universal
semantics rather than domain vocabulary.
