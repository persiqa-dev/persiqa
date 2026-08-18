# PEX-007 --- Smart Building (HVAC)

**Document:** Persiqa Example Collection (PEX)

**Chapter:** PEX-007

**Title:** Smart Building (HVAC)

**Status:** Accepted

------------------------------------------------------------------------

# 1. Real-World Scenario

A smart building HVAC system consists of:

-   Building Management System (BMS)
-   Heat Pump
-   Buffer Tank
-   Circulation Pump
-   Mixing Valve
-   Fan Coil
-   Temperature Sensor
-   Thermostat
-   Building Zone

The objective is to model a cyber-physical HVAC system using only
Persiqa Core concepts.

------------------------------------------------------------------------

# 2. Natural Language Description

The Heat Pump transforms electrical energy into thermal energy.

The Circulation Pump transports water.

The Mixing Valve controls water flow.

The Buffer Tank stores thermal energy.

The Temperature Sensor measures temperature.

The Thermostat controls the Building Zone.

The BMS controls the HVAC equipment.

------------------------------------------------------------------------

# 3. Initial Statements

``` text
BMS connected_to Heat Pump.
BMS connected_to Thermostat.

Heat Pump transforms Thermal Energy.
Circulation Pump transports Water.
Buffer Tank stores Thermal Energy.
Temperature Sensor measures Temperature.
Thermostat controls Building Zone.
```

The model is intentionally incomplete but already useful.

------------------------------------------------------------------------

# 4. Refinement

Additional knowledge becomes available.

``` text
Heat Pump connected_to Buffer Tank.

Buffer Tank connected_to Circulation Pump.

Circulation Pump connected_to Mixing Valve.

Mixing Valve connected_to Fan Coil.

Fan Coil connected_to Building Zone.
```

Later:

``` text
Thermostat composed_of Temperature Sensor.

Temperature Sensor connected_to BMS.
```

Earlier Statements remain valid.

Only precision has increased.

------------------------------------------------------------------------

# 5. Views

## Hydraulic View

Heat Pump → Buffer Tank → Circulation Pump → Mixing Valve → Fan Coil

## Control View

BMS → Thermostat

BMS → Heat Pump

Thermostat → Mixing Valve

## Thermal View

Heat Pump transforms Thermal Energy.

Buffer Tank stores Thermal Energy.

Fan Coil transports Thermal Energy.

------------------------------------------------------------------------

# 6. Zoom Levels

## Zoom 1

``` text
BMS controls Building Climate.
```

## Zoom 2

``` text
BMS controls Heat Pump.
Thermostat controls Building Zone.
Circulation Pump transports Water.
```

## Zoom 3

``` text
Heat Pump connected_to Buffer Tank.

Buffer Tank connected_to Circulation Pump.

Circulation Pump connected_to Mixing Valve.

Mixing Valve connected_to Fan Coil.

Thermostat composed_of Temperature Sensor.
```

Each zoom level represents the same HVAC system with increasing
precision.

------------------------------------------------------------------------

# 7. Validation

This example demonstrates that:

-   HVAC equipment is represented as Entity refinements.
-   Heating, circulation, storage, measurement and control are
    Capabilities.
-   Pipes and communication links are Relations.
-   Water temperature, room temperature and operating mode belong to
    Entity State.
-   Flow rate and pressure belong to Relation State.
-   Refinement preserves previously valid Statements.

------------------------------------------------------------------------

# 8. Final Observations

This example combines physical infrastructure, automation and
information flow within a single model.

The same Core successfully models thermal, hydraulic and control systems
without introducing HVAC-specific ontology concepts.

This demonstrates that Persiqa naturally supports complex cyber-physical
systems spanning multiple engineering disciplines.
