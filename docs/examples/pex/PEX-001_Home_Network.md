# PEX-001 --- Home Network

**Document:** Persiqa Example Collection (PEX)

**Chapter:** PEX-001

**Title:** Home Network

**Status:** Accepted

------------------------------------------------------------------------

# 1. Real-World Scenario

A home network consists of:

-   Internet Service
-   Router
-   Ethernet Switch
-   Patch Panel
-   Wall Outlet
-   Desktop PC
-   NAS
-   Wi-Fi Access Point

The objective is to model the infrastructure without introducing
domain-specific Core concepts.

------------------------------------------------------------------------

# 2. Natural Language Description

The Internet Service is connected to the Router.

The Router is connected to the Switch.

The Switch is connected to the Patch Panel.

The Patch Panel is connected to the Wall Outlet.

The Desktop PC is connected to the Wall Outlet.

The NAS is connected to the Switch.

The Wi-Fi Access Point is connected to the Switch.

The Router transports information.

The Switch transports information.

The Desktop PC stores information.

The NAS stores information.

The Wi-Fi Access Point transports information.

------------------------------------------------------------------------

# 3. Initial Statements

``` text
Router connected_to Switch.
Desktop PC connected_to Switch.
NAS connected_to Switch.

Router transports Information.
Switch transports Information.
Desktop PC stores Information.
NAS stores Information.
```

At this stage the model is intentionally incomplete but valid.

------------------------------------------------------------------------

# 4. Refinement

Additional knowledge becomes available.

``` text
Desktop PC connected_to Wall Outlet.
Wall Outlet connected_to Patch Panel.
Patch Panel connected_to Switch.
```

Later:

``` text
Desktop PC composed_of Network Interface.

Network Interface connected_to Ethernet Cable.

Ethernet Cable connected_to Wall Outlet.
```

The original statement:

``` text
Desktop PC connected_to Switch.
```

remains correct, although less precise.

------------------------------------------------------------------------

# 5. Views

## Physical View

Shows physical equipment and cabling.

-   Router
-   Switch
-   Patch Panel
-   Wall Outlet
-   Ethernet Cable
-   Desktop PC

## Functional View

Shows capabilities.

-   Transport Information
-   Store Information

## Connectivity View

Shows only Relations.

``` text
Router → Switch
Switch → NAS
Switch → Patch Panel
Patch Panel → Wall Outlet
Wall Outlet → Desktop PC
```

------------------------------------------------------------------------

# 6. Zoom Levels

## Zoom 1

``` text
Desktop PC connected_to Switch.
```

## Zoom 2

``` text
Desktop PC connected_to Wall Outlet.
Wall Outlet connected_to Patch Panel.
Patch Panel connected_to Switch.
```

## Zoom 3

``` text
Desktop PC
    composed_of Network Interface

Network Interface
    connected_to Ethernet Cable

Ethernet Cable
    connected_to Wall Outlet

Wall Outlet
    connected_to Patch Panel

Patch Panel
    connected_to Switch
```

Every zoom level represents the same reality with different precision.

------------------------------------------------------------------------

# 7. Validation

The example demonstrates that:

-   no networking-specific Core concept is required;
-   all devices are Entity refinements;
-   connectivity is expressed using Relation;
-   behaviour is expressed using Capability;
-   operational values (for example Link Speed or RSSI) belong to
    Relation State;
-   refinement increases precision without invalidating earlier
    Statements.

------------------------------------------------------------------------

# 8. Final Observations

This example validates several architectural principles simultaneously:

-   Statement First
-   Universal Core
-   Universal Refinement
-   First-Class Relations
-   Incremental Truth
-   Human First

It serves as the reference example for networking and communication
systems within the Persiqa documentation.
