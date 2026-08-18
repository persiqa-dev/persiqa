# REF-001 --- Smart Home Reference Model

**Document:** Persiqa Reference Models (REF)

**Chapter:** REF-001

**Title:** Smart Home Reference Model

**Status:** Accepted

------------------------------------------------------------------------

# 1. System Overview

This reference model describes a modern smart home integrating
electrical, networking, HVAC, irrigation and home automation systems
into a single Persiqa knowledge model.

Its purpose is to serve as the canonical end-to-end reference
implementation of the Persiqa Core.

------------------------------------------------------------------------

# 2. Scope

Included:

-   Home Assistant
-   MQTT Broker
-   Ethernet Network
-   Wi-Fi
-   Zigbee
-   Heat Pump
-   HVAC
-   Irrigation
-   Lighting
-   Sensors
-   Relays
-   Valves

Excluded:

-   Vendor-specific implementations
-   UI configuration
-   Automation scripting language

------------------------------------------------------------------------

# 3. Physical Architecture

Major physical entities:

-   Electrical Cabinet
-   Ethernet Switch
-   Wi-Fi Access Point
-   Zigbee Coordinator
-   Home Assistant Server
-   Heat Pump
-   Circulation Pump
-   Irrigation Controller
-   Solenoid Valves
-   Temperature Sensors
-   Presence Sensors

------------------------------------------------------------------------

# 4. Logical Architecture

Subsystems:

-   Communication
-   Climate Control
-   Irrigation
-   Lighting
-   Monitoring
-   Automation

Each subsystem shares the same Core ontology.

------------------------------------------------------------------------

# 5. Canonical Statements (excerpt)

``` text
Home Assistant connected_to MQTT Broker.

MQTT Broker connected_to Zigbee Coordinator.

Temperature Sensor measures Temperature.

Heat Pump transforms Thermal Energy.

Circulation Pump transports Water.

Lighting Controller controls Lighting.

Valve controls Water Flow.
```

These Statements represent the canonical knowledge model.

------------------------------------------------------------------------

# 6. Refinement Hierarchy

Example:

``` text
Temperature Sensor
    refines Sensor
        refines Device
            refines Entity
```

Likewise:

``` text
Solenoid Valve
    refines Valve
        refines Actuator
            refines Entity
```

------------------------------------------------------------------------

# 7. Views

Provided Views include:

-   Physical
-   Electrical
-   Hydraulic
-   Communication
-   Automation
-   Climate
-   Security
-   Maintenance

------------------------------------------------------------------------

# 8. Zoom Levels

Zoom 1

Entire Smart Home.

Zoom 2

Subsystem level.

Zoom 3

Equipment level.

Zoom 4

Component level.

Every zoom level preserves semantic consistency.

------------------------------------------------------------------------

# 9. Entity Catalogue

Representative entities:

-   Home Assistant
-   MQTT Broker
-   Switch
-   Sensor
-   Valve
-   Relay
-   Pump
-   Heat Pump
-   Thermostat

All are Entity refinements.

------------------------------------------------------------------------

# 10. Capability Catalogue

Representative capabilities:

-   Measure Temperature
-   Transport Water
-   Transport Information
-   Store Information
-   Transform Thermal Energy
-   Control Lighting
-   Control Irrigation

Each capability refines one or more universal Capability families.

------------------------------------------------------------------------

# 11. Relation Catalogue

Representative relations:

-   connected_to
-   composed_of
-   communicates_with
-   supplies
-   feeds

All are semantic refinements of Relation.

------------------------------------------------------------------------

# 12. State Catalogue

Representative Entity State:

-   Online
-   Running
-   Fault
-   Temperature
-   Occupancy

Representative Relation State:

-   RSSI
-   Link Speed
-   Water Flow
-   Pressure
-   Latency

------------------------------------------------------------------------

# 13. Example Reasoning

Example:

``` text
Temperature > Target

↓

Automation requests Heating

↓

Heat Pump starts

↓

Water circulates

↓

Room temperature increases
```

Reasoning consumes and produces Statements while preserving the Core
ontology.

------------------------------------------------------------------------

# 14. Example DSL

``` text
entity HeatPump

entity TemperatureSensor

TemperatureSensor measures Temperature

HeatPump transforms ThermalEnergy

HeatPump connected_to BufferTank
```

Illustrative only. The final DSL specification remains authoritative.

------------------------------------------------------------------------

# 15. Validation Checklist

This reference model validates:

-   Statement First
-   Universal Core
-   Universal Refinement
-   First-Class Relations
-   Capability Families
-   Incremental Truth
-   Human First

It is suitable as a baseline for parser development, DSL validation,
reasoning engines and interoperability testing.
