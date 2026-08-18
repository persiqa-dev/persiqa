# REF-002 --- Industrial Plant Reference Model

**Document:** Persiqa Reference Models (REF)

**Chapter:** REF-002

**Title:** Industrial Plant Reference Model

**Status:** Accepted

------------------------------------------------------------------------

# 1. System Overview

This reference model describes a complete industrial production plant.

It integrates electrical distribution, industrial networking,
automation, robotics, process control and production flow into a single
Persiqa knowledge model.

Its purpose is to validate the Persiqa Core under a realistic,
multi-domain industrial workload.

------------------------------------------------------------------------

# 2. Scope

Included:

-   Utility Power
-   Main Distribution Cabinet
-   Safety PLC
-   PLC
-   Industrial Ethernet
-   SCADA / HMI
-   Robot Cell
-   Conveyor System
-   Pneumatic System
-   Compressors
-   Valves
-   Sensors
-   Actuators
-   Production Line

Excluded:

-   Vendor programming languages
-   PLC ladder logic
-   SCADA screen design
-   MES / ERP integration

------------------------------------------------------------------------

# 3. Physical Architecture

Main subsystems:

-   Power Distribution
-   Industrial Network
-   Automation
-   Safety
-   Pneumatics
-   Material Handling
-   Robotics

Each subsystem is represented using the same Persiqa Core.

------------------------------------------------------------------------

# 4. Logical Architecture

Functional responsibilities:

-   Control
-   Measurement
-   Transport
-   Transformation
-   Safety
-   Monitoring

Subsystem boundaries are represented by Views rather than separate
ontologies.

------------------------------------------------------------------------

# 5. Canonical Statements (excerpt)

``` text
PLC controls Conveyor.

PLC controls Robot Cell.

Safety PLC controls Emergency Stop.

Presence Sensor measures Product Presence.

Compressor transports Compressed Air.

Pneumatic Valve controls Air Flow.

Robot Arm transforms Product Position.
```

------------------------------------------------------------------------

# 6. Refinement Hierarchy

``` text
Robot Arm
    refines Robot
        refines Machine
            refines Entity
```

``` text
Safety PLC
    refines PLC
        refines Controller
            refines Entity
```

------------------------------------------------------------------------

# 7. Views

Reference Views:

-   Physical
-   Electrical
-   Control
-   Safety
-   Pneumatic
-   Process Flow
-   Maintenance
-   Diagnostics

------------------------------------------------------------------------

# 8. Zoom Levels

Zoom 1

Entire Plant.

Zoom 2

Production Cell.

Zoom 3

Machine.

Zoom 4

Component.

Zoom 5

Terminal / Sensor.

------------------------------------------------------------------------

# 9. Entity Catalogue

Representative entities:

-   PLC
-   Safety PLC
-   Robot
-   Conveyor
-   Compressor
-   Valve
-   Sensor
-   Actuator
-   HMI
-   Switch

------------------------------------------------------------------------

# 10. Capability Catalogue

Representative capabilities:

-   Control Motion
-   Measure Position
-   Measure Presence
-   Transport Product
-   Transport Air
-   Transform Electrical Energy
-   Transform Product
-   Store Configuration

Each capability refines the universal Capability families.

------------------------------------------------------------------------

# 11. Relation Catalogue

Representative relations:

-   connected_to
-   composed_of
-   supplied_by
-   communicates_with
-   mounted_on
-   feeds

All are refinements of Relation.

------------------------------------------------------------------------

# 12. State Catalogue

Entity State:

-   Running
-   Idle
-   Fault
-   Emergency Stop Active
-   Product Count
-   Pressure

Relation State:

-   Bus Load
-   Link Status
-   Air Pressure
-   Flow Rate
-   Latency

------------------------------------------------------------------------

# 13. Example Reasoning

``` text
Presence Sensor detects Product

↓

PLC starts Conveyor

↓

Robot picks Product

↓

Robot places Product

↓

Product transported to next Station
```

Fault reasoning:

``` text
Emergency Stop Active

↓

Safety PLC stops Conveyor

↓

Robot stops

↓

Production Cell enters Safe State
```

------------------------------------------------------------------------

# 14. Example DSL

``` text
entity PLC

entity Conveyor

PLC controls Conveyor

Conveyor transports Product

PresenceSensor measures ProductPresence
```

Illustrative only. The DSL specification remains authoritative.

------------------------------------------------------------------------

# 15. Validation Checklist

This reference model validates:

-   Cross-domain modelling
-   Statement First
-   Universal Core
-   Universal Refinement
-   First-Class Relations
-   Capability Families
-   Incremental Truth
-   Multi-view consistency
-   Multi-zoom consistency
-   Reasoning readiness

It is intended as the primary industrial benchmark for future Persiqa
parsers, DSL implementations, reasoning engines and interoperability
testing.
