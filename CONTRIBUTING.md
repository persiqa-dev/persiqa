# Contributing to Persiqa

## Before Making Changes

Before changing the project, contributors SHOULD:

- read the applicable normative specifications;
- check existing ADRs before introducing architectural changes;
- identify the documents affected by the change;
- keep examples and reference material consistent with the normative model.

## Normative Documents

Documents under `docs/specification/` define the normative behavior and
semantics of Persiqa.

Changes that alter an architectural decision SHOULD be recorded in an ADR
under `docs/architecture/adr/`.

ADRs document architectural decisions and their rationale. They do not replace
the normative specifications.

## Terminology

Canonical terminology is defined by the glossary under `docs/glossary/`.

New terminology SHOULD NOT be introduced without checking the existing glossary
and the applicable specifications.

## Examples and Reference Material

Examples MUST NOT introduce semantics that are not defined by the normative
specifications.

When a specification changes, affected examples and reference material SHOULD
be reviewed for consistency.

## Pull Requests

A change should explain:

- what changed;
- why it changed;
- which specifications or architectural documents are affected;
- whether examples or conformance material need to be updated.

Changes to normative semantics SHOULD identify the affected specification
documents explicitly.

## Architectural Changes

Architectural changes SHOULD be captured in an ADR when they introduce,
modify, or supersede an architectural decision.

An ADR should explain the context, decision, consequences, and relevant
alternatives.

## Documentation Changes

Documentation should preserve the distinction between:

- normative specifications;
- architectural decisions;
- canonical terminology;
- examples and reference material;
- non-normative vision material.

When in doubt, prefer making the authoritative source explicit rather than
duplicating a rule in multiple documents.
