# PCS-006 --- CKM v0.1 Conformance Corpus

**Document:** Persiqa Conformance Specification (PCS)  
**Chapter:** PCS-006  
**Title:** CKM v0.1 Conformance Corpus  
**Status:** Accepted  
**Version:** 0.1

------------------------------------------------------------------------

# 1. Purpose

This chapter defines the implementation-independent CKM v0.1 conformance corpus. It translates the electrical and Kubernetes reality tests into mandatory semantic test cases.

The corpus verifies CKM v0.1 semantics, not a particular DSL syntax, storage model, graph engine, or UI. Equivalent encodings are permitted when they produce the required observable canonical knowledge, validation result, provenance, and conflict behaviour.

# 2. Normative sources

Each test applies PMS-008, PMS-009, PCS-002, PCS-003, PCS-005, PEX-003, and PEX-005 as relevant.

# 3. Test result conventions

Every test SHALL report:

| Field | Meaning |
|---|---|
| Test ID | Stable corpus identifier. |
| Input knowledge | Explicit Relations, States, and Statements. |
| Registry context | Effective Relation Type contracts and active rules. |
| Expected result | Accepted, rejected, or quarantined outcome. |
| Provenance expectation | Required explicit or derived origin information. |
| Failure condition | Observable condition that fails the test. |

Accepted does not mean complete knowledge. Conflict preserved means incompatible assertions and their context remain available; it does not mean a truth value was selected.

# 4. Electrical corpus

## E-001 --- Incomplete outlet is valid

**Input**

    Entity: Outlet-17

**Expected result**

The model SHALL be accepted without type, feeder, cable, location, identifier, or State. The implementation SHALL NOT synthesize an unknown State, placeholder cable, or placeholder breaker fact.

**Pass criteria**

- Outlet-17 exists as an Entity.
- No missing-field error occurs.
- No fabricated knowledge is present.

**Verifies:** PMS-008 sections 4.1, 8.6, and 9.

## E-002 --- Independent semantic aspects

**Input**

    RCBO-01 instanceOf     Acti9-iDPN-Vigi-C16
    RCBO-01 classifiedAs   CircuitBreaker
    RCBO-01 classifiedAs   ResidualCurrentDevice
    RCBO-01 playsRole      BathroomCircuitProtection
    RCBO-01 hasCapability  OvercurrentProtection
    RCBO-01 hasState       Closed

**Expected result**

The model SHALL be accepted. The assertions remain separate facts; they SHALL NOT be reduced to one intrinsic type or status field.

**Pass criteria**

- RT-001 through RT-005 endpoint contracts validate.
- Multiple classifications coexist.
- Closed has RCBO-01 as owner.
- Capability association does not imply active operation.

**Verifies:** PMS-008 sections 4, 6, 7.3; PMS-009 RT-001 through RT-005.

## E-003 --- connectedTo is symmetric, not transitive

**Input**

    Cable-17 connectedTo JunctionBox-03
    JunctionBox-03 connectedTo Cable-21

**Expected result**

Each Relation validates as symmetric. Reversed projections MAY be exposed. The implementation SHALL NOT derive Cable-17 connectedTo Cable-21 from the input pair.

**Verifies:** PMS-009 RT-007.

## E-004 --- Controlled supply derivation

**Input**

    S-101 explicit: MCB-01 supplies JunctionBox-03
    S-102 explicit: JunctionBox-03 supplies Outlet-01

**Registry context**

An active electrical supply-composition rule declares the two Relations context-compatible.

**Expected result**

The implementation SHALL derive MCB-01 supplies Outlet-01 as a derived Statement or Relation. It SHALL retain S-101 and S-102 as explicit knowledge and record the composition rule and supporting assertions as provenance.

**Failure condition**

Deriving the result without the declared rule, or removing either input assertion.

**Verifies:** PMS-008 section 8.1; PMS-009 sections 5 and RT-008.

## E-005 --- Connectivity is not supply

**Input**

    MCB-01 connectedTo JunctionBox-03
    JunctionBox-03 connectedTo Outlet-01

**Expected result**

The implementation SHALL NOT derive MCB-01 supplies Outlet-01.

**Verifies:** PMS-009 RT-007 and RT-008.

## E-006 --- Competing electrical evidence

**Input**

    S-201: Breaker-01 supplies Outlet-01 [inspection A, confidence 0.8]
    S-202: Breaker-01 supplies Outlet-02 [inspection B, confidence 0.7]

**Expected result**

Both Statements SHALL be retained with source and confidence context. A declared circuit constraint MAY derive a conflict report, but SHALL NOT delete, overwrite, or collapse either Statement.

**Verifies:** PMS-008 sections 4.5 and 8.2 through 8.5; PMS-009 section 8.

# 5. Kubernetes corpus

## K-001 --- Workload placement and inverse projection

**Input**

    Pod-01 hostedOn VM-01

**Expected result**

The input SHALL validate under RT-010. VM-01 hosts Pod-01 MAY be produced only under the declared inverse contract. The implementation SHALL NOT infer that Pod-01 is running, healthy, or reachable.

**Verifies:** PMS-009 RT-010.

## K-002 --- Entity continuity across State and role

**Input**

    VM-01 playsRole     KubernetesNode
    VM-01 hasCapability ContainerExecution
    VM-01 hasState      Running

**Evolution**

    VM-01 hasState  Stopped
    VM-01 playsRole BuildRunner

**Expected result**

VM-01 retains one Entity continuity identity. Its State and role assertions SHALL NOT create replacement Entities.

**Verifies:** PMS-008 sections 4.1, 4.2, 4.4, and 5; PMS-009 RT-003 through RT-005.

## K-003 --- One canonical model, multiple representations

**Input**

    Server-01      hosts         VM-01
    Pod-01         hostedOn      VM-01
    Container-01   partOf        Pod-01
    Application-01 runsIn        Container-01
    Service-01     exposes       Application-01

**Registry context**

The non-baseline Relation Types in this fixture SHALL be declared extension
contracts. This test requires no inference from them.

**Expected result**

Infrastructure, application, and workload-detail views SHALL be projections of one canonical model. A new view SHALL NOT duplicate, replace, or alter canonical identities or Relations.

**Verifies:** PMS-008 section 10.2.

## K-004 --- Co-hosting does not imply dependency

**Input**

    Application-A hostedOn VM-01
    Application-B hostedOn VM-01

**Expected result**

The implementation SHALL NOT derive Application-A dependsOn Application-B, or its reverse, solely from shared hosting.

**Verifies:** PMS-009 RT-009 and RT-010.

# 6. Negative corpus

## N-001 --- Invalid hasState endpoint

**Input**

    Pump-01 hasState Cable-17

**Expected result**

The Relation SHALL be rejected, quarantined, or explicitly marked invalid because RT-005 requires a State target.

**Verifies:** PMS-009 sections 4 and RT-005.

## N-002 --- Unknown Relation Type

**Input**

    Pump-01 magicallyFeeds Outlet-01

**Expected result**

The implementation SHALL reject, quarantine, or retain the assertion explicitly as uninterpreted imported knowledge. It SHALL NOT silently assign supplies, connectedTo, or another baseline Relation Type.

**Verifies:** PMS-009 section 8.

## N-003 --- Relation identity is not endpoint tuple identity

**Input**

    R-1: Cable-17 connectedTo JunctionBox-03 [physical conductor A]
    R-2: Cable-17 connectedTo JunctionBox-03 [physical conductor B]

**Expected result**

Both Relations MAY coexist when their modeled context makes them distinct. The implementation SHALL NOT merge them solely because Relation Type and endpoints match.

**Verifies:** PMS-008 sections 4.3 and 5.2; PMS-009 section 3.

## N-004 --- Context-sensitive State conflict

**Input**

    S-401: Breaker-01 hasState Closed [observed at T1]
    S-402: Breaker-01 hasState Open   [observed at T2]

**Expected result**

The assertions SHALL coexist without conflict when T1 and T2 do not overlap. If equivalent mutually exclusive States have overlapping context, the implementation MAY report a conflict while preserving both assertions.

**Verifies:** PMS-008 sections 4.4 and 8.3 through 8.5; PMS-009 RT-005.

# 7. Minimum conformance profile

A CKM v0.1 implementation claiming support for the baseline Relation Type Registry SHALL pass E-001 through E-006, K-001 through K-004, and N-001 through N-004.

An implementation that does not implement a declared inference rule SHALL still pass non-inference tests and SHALL report RT-008 composition as unsupported rather than deriving an unconstrained result. It SHALL not claim that reasoning profile until E-004 passes.

# 8. Corpus maintenance

New tests SHALL preserve the conventions in section 3 and reference their normative clause. A new test SHALL NOT change an existing test ID. Changed expected results require a versioned specification revision.
