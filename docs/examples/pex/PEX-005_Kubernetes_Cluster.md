# PEX-005 --- Kubernetes Cluster

**Document:** Persiqa Example Collection (PEX)

**Chapter:** PEX-005

**Title:** Kubernetes Cluster

**Status:** Accepted

------------------------------------------------------------------------

# 1. Real-World Scenario

A Kubernetes platform consists of:

-   Kubernetes Cluster
-   Control Plane
-   Worker Node
-   Deployment
-   Pod
-   Service
-   Ingress
-   Persistent Volume
-   Application

The objective is to model a cloud-native platform using only Persiqa
Core concepts.

------------------------------------------------------------------------

# 2. Natural Language Description

The Control Plane controls the Worker Nodes.

A Deployment manages Pods.

Pods run on Worker Nodes.

A Service connects Applications to Pods.

An Ingress routes external traffic to a Service.

A Persistent Volume stores application data.

Pods transport information.

Applications transform information.

------------------------------------------------------------------------

# 3. Initial Statements

``` text
Control Plane connected_to Worker Node.
Deployment connected_to Pod.
Pod connected_to Worker Node.
Service connected_to Pod.

Control Plane controls Worker Node.
Pod transports Information.
Persistent Volume stores Information.
Application transforms Information.
```

The model is intentionally incomplete but already valid.

------------------------------------------------------------------------

# 4. Refinement

Additional knowledge becomes available.

``` text
Application composed_of Container.

Container runs_on Pod.

Ingress connected_to Service.

Service connected_to Application.
```

Later:

``` text
Persistent Volume connected_to Pod.

Pod connected_to Container.
```

The original Statements remain valid.

Only the level of precision has increased.

------------------------------------------------------------------------

# 5. Views

## Infrastructure View

Shows cluster components.

-   Control Plane
-   Worker Nodes
-   Pods
-   Persistent Volumes

## Application View

Shows Deployments, Applications and Services.

## Communication View

``` text
Ingress
    → Service
        → Pod
            → Application
```

## Storage View

``` text
Application
    ↔ Persistent Volume
```

------------------------------------------------------------------------

# 6. Zoom Levels

## Zoom 1

``` text
Application connected_to Kubernetes Cluster.
```

## Zoom 2

``` text
Application connected_to Service.
Service connected_to Pod.
Pod connected_to Worker Node.
```

## Zoom 3

``` text
Deployment manages Pod.

Pod
    composed_of Container

Container
    runs_on Worker Node

Ingress
    connected_to Service

Persistent Volume
    connected_to Pod
```

Each zoom level represents the same platform with increasing precision.

------------------------------------------------------------------------

# 7. Validation

This example demonstrates that:

-   Cluster components are Entity refinements.
-   Scheduling, routing and storage remain domain semantics rather than
    Core concepts.
-   Communication is represented through Relations.
-   Information transport, storage, transformation and control are
    represented through Capabilities.
-   Pod status belongs to Entity State.
-   Network latency and bandwidth belong to Relation State.
-   Refinement preserves previously valid Statements.

------------------------------------------------------------------------

# 8. Final Observations

This example demonstrates that a modern cloud-native platform can be
modelled using the same Persiqa Core used for networking, water systems,
electrical infrastructure and home automation.

The terminology changes dramatically.

The Core ontology does not.

This reinforces the universality of the Persiqa architecture across both
physical and software-defined infrastructure.
