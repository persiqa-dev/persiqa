# PEX-006 --- Manufacturing Cell

**Document:** Persiqa Example Collection (PEX)

**Chapter:** PEX-006

**Title:** Manufacturing Cell

**Status:** Accepted

------------------------------------------------------------------------

# 1. Real-World Scenario

An automated manufacturing cell consists of:

-   PLC
-   HMI
-   I/O Module
-   Robot Arm
-   Conveyor
-   Presence Sensor
-   Safety Gate
-   Emergency Stop
-   Pneumatic Gripper
-   Finished Product

The objective is to model an industrial automation system using only
Persiqa Core concepts.

------------------------------------------------------------------------

# 2. Natural Language Description

The PLC controls the Robot Arm.

The PLC controls the Conveyor.

The Presence Sensor measures product presence.

The Robot Arm transforms the position of the product.

The Conveyor transports the product.

The Pneumatic Gripper transports the product.

The Safety Gate controls access to the cell.

The Emergency Stop controls machine operation.

------------------------------------------------------------------------

# 3. Initial Statements

``` text
PLC connected_to Robot Arm.
PLC connected_to Conveyor.
Presence Sensor connected_to PLC.

PLC controls Robot Arm.
PLC controls Conveyor.
Presence Sensor measures Product Presence.
Conveyor transports Product.
Robot Arm transforms Product Position.
```

The initial model intentionally captures only the essential knowledge.

------------------------------------------------------------------------

# 4. Refinement

Additional information becomes available.

``` text
PLC connected_to I/O Module.

I/O Module connected_to Presence Sensor.

I/O Module connected_to Emergency Stop.

Robot Arm composed_of Pneumatic Gripper.

Pneumatic Gripper transports Product.
```

Later:

``` text
Safety Gate connected_to PLC.

HMI connected_to PLC.
```

The original Statements remain valid while the model becomes more
precise.

------------------------------------------------------------------------

# 5. Views

## Physical View

-   PLC
-   HMI
-   Robot Arm
-   Conveyor
-   I/O Module
-   Sensors
-   Safety Devices

## Process View

``` text
Conveyor
    → Robot Arm
        → Finished Product
```

## Control View

``` text
PLC
    → Conveyor

PLC
    → Robot Arm

PLC
    → Safety Gate
```

## Safety View

``` text
Emergency Stop
    → PLC

Safety Gate
    → PLC
```

------------------------------------------------------------------------

# 6. Zoom Levels

## Zoom 1

``` text
PLC controls Manufacturing Cell.
```

## Zoom 2

``` text
PLC controls Conveyor.
PLC controls Robot Arm.
Presence Sensor measures Product Presence.
```

## Zoom 3

``` text
PLC connected_to I/O Module.

I/O Module connected_to Presence Sensor.

I/O Module connected_to Emergency Stop.

Robot Arm composed_of Pneumatic Gripper.

Pneumatic Gripper transports Product.
```

Each zoom level describes the same manufacturing cell with increasing
precision.

------------------------------------------------------------------------

# 7. Validation

This example demonstrates that:

-   PLCs, robots, conveyors and sensors are Entity refinements.
-   Measurement, transport, transformation and control are Capabilities.
-   Field wiring and industrial networks are represented by Relations.
-   Sensor values, operating modes and alarms belong to Entity State.
-   Bus quality, latency and signal integrity belong to Relation State.
-   Safety functions require no additional Core concepts.
-   Refinement preserves previous Statements.

------------------------------------------------------------------------

# 8. Final Observations

This example demonstrates that Persiqa naturally models industrial
automation without introducing PLC-specific or manufacturing-specific
ontology concepts.

Industrial terminology changes.

The Core ontology remains unchanged.

This example validates that the same universal modelling language spans
IT infrastructure, utilities, home automation, cloud platforms and
industrial control systems.
