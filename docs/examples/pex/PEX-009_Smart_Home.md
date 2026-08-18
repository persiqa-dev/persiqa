# EX-009 --- Smart Home

**Document:** Persiqa Example Collection (PEX)

**Chapter:** EX-009

**Title:** Smart Home

**Status:** Accepted

------------------------------------------------------------------------

# 1. Real-World Scenario

A smart home integrates multiple infrastructure domains into a single
system.

The installation consists of:

-   Home Assistant
-   MQTT Broker
-   Ethernet Switch
-   Wi-Fi Access Point
-   Zigbee Coordinator
-   Heat Pump
-   Irrigation Controller
-   Smart Thermostat
-   Presence Sensor
-   Lighting Controller

The objective is to demonstrate that multiple independent domains can
coexist within a single Persiqa model.

------------------------------------------------------------------------

# 2. Natural Language Description

Home Assistant controls the smart home.

The MQTT Broker transports information.

The Ethernet Switch transports information.

The Wi-Fi Access Point transports information.

The Zigbee Coordinator transports information.

The Smart Thermostat measures temperature.

The Heat Pump transforms thermal energy.

The Irrigation Controller controls irrigation.

The Presence Sensor measures occupancy.

The Lighting Controller controls lighting.

------------------------------------------------------------------------

# 3. Initial Statements

``` text
Home Assistant connected_to MQTT Broker.
MQTT Broker connected_to Zigbee Coordinator.
MQTT Broker connected_to Lighting Controller.

Home Assistant controls Smart Home.
Heat Pump transforms Thermal Energy.
Smart Thermostat measures Temperature.
Presence Sensor measures Occupancy.
Lighting Controller controls Lighting.
```

The initial model captures only the essential architecture.

------------------------------------------------------------------------

# 4. Refinement

Additional knowledge becomes available.

``` text
Ethernet Switch connected_to MQTT Broker.
Wi-Fi Access Point connected_to Ethernet Switch.

Home Assistant connected_to Heat Pump.

Home Assistant connected_to Irrigation Controller.
```

Later:

``` text
Smart Thermostat
    composed_of Temperature Sensor

Temperature Sensor
    measures Temperature

Lighting Controller
    connected_to Zigbee Coordinator
```

Earlier Statements remain valid.

Only additional knowledge has been introduced.

------------------------------------------------------------------------

# 5. Views

## Physical View

-   Heat Pump
-   Ethernet Switch
-   Wi-Fi Access Point
-   Zigbee Coordinator
-   Lighting Controller
-   Smart Thermostat

## Communication View

``` text
Home Assistant
    → MQTT Broker
        → Ethernet Switch
        → Zigbee Coordinator
```

## Automation View

``` text
Presence Sensor
    → Home Assistant
        → Lighting Controller

Temperature Sensor
    → Home Assistant
        → Heat Pump
```

## Energy View

-   Heat Pump transforms Thermal Energy.
-   Lighting consumes Electrical Energy.
-   Irrigation transports Water.

------------------------------------------------------------------------

# 6. Zoom Levels

## Zoom 1

``` text
Home Assistant controls Smart Home.
```

## Zoom 2

``` text
Home Assistant connected_to MQTT Broker.
MQTT Broker connected_to Zigbee Coordinator.
Heat Pump transforms Thermal Energy.
```

## Zoom 3

``` text
Temperature Sensor measures Temperature.

Home Assistant connected_to Irrigation Controller.

Lighting Controller connected_to Zigbee Coordinator.

Ethernet Switch connected_to Wi-Fi Access Point.
```

Each zoom level represents the same smart home with increasing
precision.

------------------------------------------------------------------------

# 7. Validation

This example demonstrates that:

-   Multiple infrastructure domains coexist in a single model.
-   Physical equipment, software and automation remain Entity
    refinements.
-   Communication, hydraulic and electrical connectivity are all
    Relations.
-   Measurement, transport, transformation and control remain universal
    Capabilities.
-   Device telemetry belongs to Entity State.
-   Link quality, bandwidth and flow belong to Relation State.
-   Refinement preserves previously valid Statements.

------------------------------------------------------------------------

# 8. Final Observations

This example combines networking, home automation, HVAC, lighting and
irrigation into a unified knowledge model.

Although the system spans multiple engineering disciplines, the Core
ontology remains unchanged.

This demonstrates that Persiqa models integrated cyber-physical
environments without requiring domain-specific architectural concepts.
