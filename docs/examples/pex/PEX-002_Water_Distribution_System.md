# PEX-002 --- Water Distribution System

**Document:** Persiqa Example Collection (PEX)

**Chapter:** PEX-002

**Title:** Water Distribution System

**Status:** Accepted

------------------------------------------------------------------------

# 1. Real-World Scenario

A residential water distribution system consists of:

-   Well
-   Pump
-   Pressure Tank
-   Main Pipe
-   Shut-off Valve
-   Garden Tap
-   House

The objective is to model the system using only Persiqa Core concepts.

------------------------------------------------------------------------

# 2. Natural Language Description

The Well provides water.

The Pump transports water.

The Pump is connected to the Pressure Tank.

The Pressure Tank is connected to the Main Pipe.

The Main Pipe is connected to the House.

The Main Pipe is connected to the Garden Tap.

The Shut-off Valve controls water flow.

------------------------------------------------------------------------

# 3. Initial Statements

``` text
Pump connected_to Pressure Tank.
Pressure Tank connected_to Main Pipe.
Main Pipe connected_to House.

Pump transports Water.
Pressure Tank stores Water.
Main Pipe transports Water.
Shut-off Valve controls Flow.
```

The model is intentionally simple but already useful.

------------------------------------------------------------------------

# 4. Refinement

More detailed knowledge becomes available.

``` text
Main Pipe connected_to Tee Junction.

Tee Junction connected_to House Branch.

Tee Junction connected_to Garden Branch.

House Branch connected_to House.

Garden Branch connected_to Garden Tap.
```

Later:

``` text
Pump composed_of Motor.

Motor transforms Electrical Energy.

Pump transports Water.
```

The original statements remain valid.

Only the level of precision has increased.

------------------------------------------------------------------------

# 5. Views

## Hydraulic View

Shows only water transport.

-   Well
-   Pump
-   Pressure Tank
-   Main Pipe
-   House
-   Garden Tap

## Functional View

Shows Capabilities.

-   Transport Water
-   Store Water
-   Control Flow
-   Transform Electrical Energy

## Topology View

Shows only Relations.

``` text
Well → Pump
Pump → Pressure Tank
Pressure Tank → Main Pipe
Main Pipe → House
Main Pipe → Garden Tap
```

------------------------------------------------------------------------

# 6. Zoom Levels

## Zoom 1

``` text
Pump connected_to House.
```

## Zoom 2

``` text
Pump connected_to Pressure Tank.
Pressure Tank connected_to Main Pipe.
Main Pipe connected_to House.
```

## Zoom 3

``` text
Pump
    composed_of Motor

Motor
    transforms Electrical Energy

Pump
    connected_to Pressure Tank

Pressure Tank
    connected_to Main Pipe

Main Pipe
    connected_to Tee Junction

Tee Junction
    connected_to House Branch

House Branch
    connected_to House
```

Each zoom level describes the same infrastructure with different
precision.

------------------------------------------------------------------------

# 7. Validation

The example demonstrates that:

-   Pump, Pipe and Valve are Entity refinements.
-   Water transport is expressed through Capability.
-   Connectivity is expressed through Relation.
-   Pressure and Flow Rate belong to Relation State.
-   Pump status belongs to Entity State.
-   Refinement preserves previously valid Statements.

------------------------------------------------------------------------

# 8. Final Observations

This example validates that the same Persiqa Core used for networking
can model hydraulic infrastructure without introducing new Core
concepts.

The vocabulary changes.

The ontology does not.

This demonstrates that the Persiqa Core is independent of the
application domain.
