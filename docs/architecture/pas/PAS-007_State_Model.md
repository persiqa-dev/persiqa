# PAS-007 --- State Model

**Document:** Persiqa Architecture Specification (PAS)

**Chapter:** PAS-007

**Title:** State Model

**Status:** Accepted

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the normative State model of the Persiqa Core.

State represents the current condition of an Entity or a Relation.

State answers the question:

> "What is true now?"

It SHALL NOT answer:

-   What the object is.
-   What the object is capable of.
-   How the object is implemented.

------------------------------------------------------------------------

# 2. Definition

A State is a temporal property describing the current condition of an
Entity or Relation.

State is expected to change over time without affecting identity.

------------------------------------------------------------------------

# 3. Characteristics

A State:

-   SHALL be temporal.
-   MAY change over time.
-   SHALL belong to exactly one Entity or one Relation.
-   SHALL NOT define an independent continuity identity.
-   Within its owner context, State has a canonical identity determined by
    its semantic predicate.
-   SHALL NOT define Capability.
-   MAY be replaced by a newer State.

------------------------------------------------------------------------

# 4. Entity State

Entity State describes the current condition of an Entity.

Examples:

``` text
Camera is Online.
Pump is Running.
Battery Charge = 82%.
Boiler Temperature = 93 °C.
```

Entity State SHALL describe only the Entity itself.

------------------------------------------------------------------------

# 5. Relation State

Relation State describes the current condition of a Relation.

Examples:

``` text
Camera connected_to Switch.

Link Speed = 1 Gbps.
RSSI = -58 dBm.
PoE Active = true.
```

``` text
Pump connected_to Pipe.

Flow Rate = 18 l/min.
Pressure = 2.3 bar.
```

Relation State SHALL describe the relationship, not either endpoint.

------------------------------------------------------------------------

# 6. State Evolution

State naturally evolves over time.

Example:

``` text
Battery Charge = 82%.
Battery Charge = 79%.
Battery Charge = 61%.
```

Each Statement represents knowledge valid at a specific point in time.

Implementations MAY preserve history, but the Core does not require
historical storage.

------------------------------------------------------------------------

# 7. State and Refinement

Refinement SHALL NOT replace State.

Refinement adds structural knowledge.

State describes temporal knowledge.

These concerns SHALL remain independent.

------------------------------------------------------------------------

# 8. Ontological Constraints

SC-001

Every State SHALL belong to exactly one Entity or one Relation.

SC-002

State SHALL remain temporal.

SC-003

Changing State SHALL NOT change identity.

SC-004

Changing State SHALL NOT change Capability.

SC-005

Relation State SHALL describe only the Relation.

SC-006

Entity State SHALL describe only the Entity.

------------------------------------------------------------------------

# 9. Validation

The State model has been validated across multiple domains.

Representative examples:

  Domain            State Examples
  ----------------- --------------------------
  Networking        Link Speed, RSSI
  IT                Online, Running
  Electrical        Voltage, Current, Charge
  Water             Flow Rate, Pressure
  HVAC              Temperature
  PLC / OT          Position, Speed
  Home Automation   On, Off
  Robotics          Position, Velocity

The same State model was sufficient across every validated domain.

------------------------------------------------------------------------

# 10. Rationale

Identity, Capability and State intentionally describe different
dimensions of the model.

An Entity may remain the same Entity while its State changes
continuously.

Likewise, a Capability remains valid regardless of the current State.

Separating these concerns preserves a stable ontology while allowing the
model to represent dynamic systems accurately.


### State Identity

State SHALL NOT define an independent continuity identity. Within its owner context, State has a canonical identity determined by its semantic predicate. Changing the State value SHALL NOT create a new canonical State object.
