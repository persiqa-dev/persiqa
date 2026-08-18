# Persiqa Documentation

This directory contains the documentation for the Persiqa project.

## Documentation Layers

### Vision

`docs/vision/`

Describes the purpose, scope, goals, and intended direction of Persiqa.

Vision documents are non-normative.

### Architecture

`docs/architecture/`

Documents the architectural structure and architectural decisions of Persiqa.

Architectural decisions are recorded under `docs/architecture/adr/`.

### Specification

`docs/specification/`

Contains the normative specifications defining the Persiqa model, language,
semantics, validation, conformance, and related behavior.

The specification is the authoritative source for normative semantics.

### Glossary

`docs/glossary/`

Defines the canonical terminology used throughout the project.

### Examples

`docs/examples/`

Contains concrete examples illustrating the Persiqa language and model.

Examples are explanatory and MUST NOT introduce semantics that are not defined
by the normative specifications.

### Reference

`docs/reference/`

Contains supporting and reference material that does not itself define the
normative model.

## Normative Authority

For normative behavior, use the documents under `docs/specification/`.

For architectural decisions and their rationale, use the ADRs under
`docs/architecture/adr/`.

For canonical terminology, use the glossary under `docs/glossary/`.

These documentation layers have different purposes and should not be treated
as interchangeable.

## Suggested Reading Order

For a new contributor or reader:

1. Vision
2. Architecture and ADRs
3. Glossary
4. Core model specifications
5. DSL and semantic specifications
6. Validation and conformance specifications
7. Examples and reference material
