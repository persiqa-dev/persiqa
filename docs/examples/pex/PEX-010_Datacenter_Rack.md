# PEX-010 --- Datacenter Rack

**Document:** Persiqa Example Collection (PEX)

**Chapter:** PEX-010

**Title:** Datacenter Rack

**Status:** Accepted

------------------------------------------------------------------------

# 1. Real-World Scenario

A modern datacenter rack consists of:

-   Rack
-   UPS
-   PDU
-   Firewall
-   Ethernet Switch
-   Patch Panel
-   Storage Array
-   Hypervisor Host
-   Kubernetes Worker
-   Virtual Machine

The objective is to demonstrate that physical infrastructure,
networking, virtualization and cloud workloads can be modelled using
only Persiqa Core concepts.

------------------------------------------------------------------------

# 2. Natural Language Description

The UPS stores electrical energy.

The PDU transports electrical energy.

The Firewall transports information.

The Ethernet Switch transports information.

The Patch Panel connects network equipment.

The Hypervisor Host transforms computing resources into virtual
execution environments.

The Kubernetes Worker transports and processes information.

The Storage Array stores information.

The Virtual Machine transforms information.

------------------------------------------------------------------------

# 3. Initial Statements

``` text
UPS connected_to PDU.
PDU connected_to Hypervisor Host.
Hypervisor Host connected_to Ethernet Switch.
Ethernet Switch connected_to Firewall.

UPS stores Electrical Energy.
PDU transports Electrical Energy.
Ethernet Switch transports Information.
Storage Array stores Information.
Hypervisor Host transforms Computing Resources.
```

The model intentionally begins with only the essential infrastructure.

------------------------------------------------------------------------

# 4. Refinement

Additional knowledge becomes available.

``` text
Ethernet Switch connected_to Patch Panel.

Patch Panel connected_to Firewall.

Storage Array connected_to Hypervisor Host.

Hypervisor Host connected_to Kubernetes Worker.

Kubernetes Worker connected_to Virtual Machine.
```

Later:

``` text
Hypervisor Host
    composed_of CPU

Hypervisor Host
    composed_of Memory

Virtual Machine
    runs_on Hypervisor Host
```

Earlier Statements remain valid.

Only the precision of the model increases.

------------------------------------------------------------------------

# 5. Views

## Power View

``` text
UPS
    → PDU
        → Hypervisor Host
```

## Network View

``` text
Firewall
    ↔ Ethernet Switch
        ↔ Patch Panel
```

## Compute View

``` text
Hypervisor Host
    → Kubernetes Worker
        → Virtual Machine
```

## Storage View

``` text
Storage Array
    ↔ Hypervisor Host
```

------------------------------------------------------------------------

# 6. Zoom Levels

## Zoom 1

``` text
Datacenter Rack provides Compute Services.
```

## Zoom 2

``` text
UPS connected_to PDU.
PDU connected_to Hypervisor Host.
Hypervisor Host connected_to Ethernet Switch.
```

## Zoom 3

``` text
Patch Panel connected_to Ethernet Switch.

Storage Array connected_to Hypervisor Host.

Hypervisor Host composed_of CPU.

Hypervisor Host composed_of Memory.

Virtual Machine runs_on Hypervisor Host.
```

Each zoom level represents the same rack with increasing precision.

------------------------------------------------------------------------

# 7. Validation

This example demonstrates that:

-   Rack components are Entity refinements.
-   Energy transport, information transport, storage, transformation and
    control are expressed through universal Capabilities.
-   Power cables, network links and storage connectivity are represented
    as Relations.
-   CPU load, memory utilisation and VM status belong to Entity State.
-   Link speed, bandwidth, latency and power consumption belong to
    Relation State.
-   Refinement preserves previously valid Statements.

------------------------------------------------------------------------

# 8. Final Observations

This example combines electrical infrastructure, networking, storage,
virtualization and cloud computing within a single coherent knowledge
model.

Despite spanning multiple technology layers, no datacenter-specific Core
concepts are required.

This concludes the reference example collection by demonstrating that
the Persiqa Core remains stable across traditional infrastructure,
cyber-physical systems and modern cloud platforms.

The examples collectively provide empirical evidence that Persiqa is a
universal modelling language rather than a domain-specific framework.
