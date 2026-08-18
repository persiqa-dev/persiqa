# PEX-003 --- Electrical Cabinet

**Document:** Persiqa Example Collection (PEX)

**Chapter:** PEX-003

**Title:** Electrical Cabinet

**Status:** Accepted

------------------------------------------------------------------------

# 1. Real-World Scenario

A residential electrical cabinet consists of:

-   Utility Grid
-   Main Switch
-   RCD
-   Circuit Breakers
-   Power Supply
-   Contactor
-   Relay
-   Lighting Circuit
-   Socket Circuit
-   Irrigation Controller

The objective is to model the system using only Persiqa Core concepts.

------------------------------------------------------------------------

# 2. Natural Language Description

The Utility Grid supplies electrical energy.

The Main Switch controls the incoming supply.

The RCD protects downstream circuits.

The Circuit Breakers protect individual circuits.

The Power Supply transforms electrical energy.

The Contactor controls power delivery.

The Relay controls the irrigation controller.

The Lighting Circuit transports electrical energy.

The Socket Circuit transports electrical energy.

------------------------------------------------------------------------

# 3. Initial Statements

``` text
Utility Grid connected_to Main Switch.
Main Switch connected_to RCD.
RCD connected_to Circuit Breaker.

Main Switch controls Electrical Energy.
Power Supply transforms Electrical Energy.
Lighting Circuit transports Electrical Energy.
Socket Circuit transports Electrical Energy.
Relay controls Irrigation Controller.
```

The model is intentionally incomplete but already valid.

------------------------------------------------------------------------

# 4. Refinement

Additional structural knowledge becomes available.

``` text
Circuit Breaker connected_to Lighting Circuit.
Circuit Breaker connected_to Socket Circuit.

Power Supply connected_to Relay.

Relay connected_to Irrigation Controller.
```

Later:

``` text
Power Supply
    transforms Electrical Energy

Power Supply
    composed_of Transformer

Transformer
    transforms Electrical Energy
```

Earlier Statements remain valid.

Only precision has increased.

------------------------------------------------------------------------

# 5. Views

## Electrical Topology View

Shows physical electrical connectivity.

-   Utility Grid
-   Main Switch
-   RCD
-   Circuit Breakers
-   Power Supply
-   Relay

## Functional View

Shows Capabilities.

-   Transport Electrical Energy
-   Transform Electrical Energy
-   Control Electrical Energy
-   Control Circuit

## Protection View

Shows protective devices and downstream relations.

------------------------------------------------------------------------

# 6. Zoom Levels

## Zoom 1

``` text
Utility Grid connected_to House.
```

## Zoom 2

``` text
Utility Grid connected_to Main Switch.
Main Switch connected_to RCD.
RCD connected_to Circuit Breaker.
```

## Zoom 3

``` text
Circuit Breaker connected_to Lighting Circuit.

Circuit Breaker connected_to Socket Circuit.

Power Supply connected_to Relay.

Relay connected_to Irrigation Controller.
```

Each zoom level represents the same installation with increasing detail.

------------------------------------------------------------------------

# 7. Validation

The example demonstrates that:

-   Switches, breakers, relays and power supplies are Entity
    refinements.
-   Energy transport, transformation and control are Capabilities.
-   Electrical wiring is represented through Relations.
-   Voltage, current and power belong to State.
-   Breaker status belongs to Entity State.
-   Cable load belongs to Relation State.
-   Refinement preserves previously valid Statements.

------------------------------------------------------------------------

# 8. Final Observations

This example confirms that the Persiqa Core models electrical
infrastructure without introducing electrical-specific ontology
concepts.

Electrical terminology changes.

The Core ontology does not.

The same architecture now successfully models networking, water
distribution and electrical systems using identical modelling
principles.
