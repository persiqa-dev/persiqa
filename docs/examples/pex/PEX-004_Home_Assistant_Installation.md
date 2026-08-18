# PEX-004 --- Home Assistant Installation

**Document:** Persiqa Example Collection (PEX)

**Chapter:** PEX-004

**Title:** Home Assistant Installation

**Status:** Accepted

------------------------------------------------------------------------

# 1. Real-World Scenario

A Home Assistant based automation system consists of:

-   Home Assistant
-   MQTT Broker
-   ESPHome Node
-   Tasmota Device
-   Zigbee Coordinator
-   Temperature Sensor
-   Irrigation Valve
-   Relay
-   Automation

The objective is to model the system without introducing
software-specific Core concepts.

------------------------------------------------------------------------

# 2. Natural Language Description

Home Assistant communicates with the MQTT Broker.

The MQTT Broker communicates with the ESPHome Node.

The MQTT Broker communicates with the Tasmota Device.

The ESPHome Node measures temperature.

The Tasmota Device controls the Relay.

The Relay controls the Irrigation Valve.

The Automation controls the Irrigation Valve using the measured
temperature.

------------------------------------------------------------------------

# 3. Initial Statements

``` text
Home Assistant connected_to MQTT Broker.
MQTT Broker connected_to ESPHome Node.
MQTT Broker connected_to Tasmota Device.

ESPHome Node measures Temperature.
Tasmota Device controls Relay.
Relay controls Irrigation Valve.
```

The model is intentionally incomplete but already valid.

------------------------------------------------------------------------

# 4. Refinement

Additional knowledge becomes available.

``` text
ESPHome Node composed_of Temperature Sensor.

Temperature Sensor measures Temperature.

Automation controls Irrigation Valve.

Automation connected_to Home Assistant.
```

Later:

``` text
Tasmota Device composed_of Relay.

Relay connected_to Irrigation Valve.
```

The original Statements remain valid.

Only additional knowledge has been introduced.

------------------------------------------------------------------------

# 5. Views

## Physical View

Shows devices.

-   ESPHome Node
-   Tasmota Device
-   Relay
-   Irrigation Valve

## Communication View

Shows communication paths.

``` text
Home Assistant
    → MQTT Broker
        → ESPHome Node
        → Tasmota Device
```

## Functional View

Shows Capabilities.

-   Measure Temperature
-   Control Relay
-   Control Flow

## Automation View

Shows logical control.

``` text
Temperature
    → Automation
        → Relay
            → Irrigation Valve
```

------------------------------------------------------------------------

# 6. Zoom Levels

## Zoom 1

``` text
Home Assistant controls Irrigation Valve.
```

## Zoom 2

``` text
Home Assistant connected_to MQTT Broker.

MQTT Broker connected_to Tasmota Device.

Tasmota Device controls Relay.

Relay controls Irrigation Valve.
```

## Zoom 3

``` text
ESPHome Node
    composed_of Temperature Sensor

Temperature Sensor
    measures Temperature

Automation
    controls Irrigation Valve

MQTT Broker
    connected_to ESPHome Node

MQTT Broker
    connected_to Tasmota Device
```

Each zoom level represents the same automation system with increasing
precision.

------------------------------------------------------------------------

# 7. Validation

This example demonstrates that:

-   Home Assistant, MQTT Broker, ESPHome and Tasmota are Entity
    refinements.
-   Communication is represented by Relations.
-   Measurement and control are represented by Capabilities.
-   Sensor readings belong to Entity State.
-   Connection quality and latency belong to Relation State.
-   Automation logic does not require new Core concepts.
-   Refinement preserves previous Statements.

------------------------------------------------------------------------

# 8. Final Observations

This example confirms that Persiqa models software-defined automation
systems using exactly the same Core ontology as physical infrastructure.

The domain vocabulary changes from pumps and switches to brokers and
automations.

The Core ontology remains unchanged.

This validates that Persiqa is equally applicable to physical, logical
and hybrid infrastructures.
