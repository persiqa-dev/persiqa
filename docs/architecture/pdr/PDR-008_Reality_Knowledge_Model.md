# PDR-008 --- Reality → Knowledge → Model

**Document:** Persiqa Design Record (PDR)

**Chapter:** PDR-008

**Title:** Reality → Knowledge → Model

**Status:** Accepted

------------------------------------------------------------------------

# 1. Problem

Early discussions often treated the model as if it were reality itself.

As modelling scenarios became more complex, an important question
emerged:

> What exactly does Persiqa model?

------------------------------------------------------------------------

# 2. Initial Assumption

The initial assumption was simple:

Reality and the model were effectively the same.

As long as the model accurately described the infrastructure, no
distinction appeared necessary.

------------------------------------------------------------------------

# 3. Observation

Repeated refinement exercises exposed a contradiction.

Different people could create different models of the same
infrastructure.

Both models could be correct.

The difference was not reality.

The difference was knowledge.

The model reflected what was currently known, not everything that
objectively existed.

------------------------------------------------------------------------

# 4. Hypothesis

A new hypothesis emerged:

> Persiqa does not model reality directly.

> Persiqa models knowledge about reality.

If correct, incomplete models could still be valid because knowledge is
inherently incomplete and evolves over time.

------------------------------------------------------------------------

# 5. Experiments

The hypothesis was tested using progressive discovery.

For each scenario:

1.  Describe the observed reality.
2.  Record only the currently known facts.
3.  Refine the model as additional knowledge becomes available.
4.  Verify whether previous Statements remain valid.

Across all domains the same pattern emerged.

Knowledge evolved.

Reality remained unchanged.

------------------------------------------------------------------------

# 6. Counterexamples

Several alternatives were considered.

## Model Equals Reality

Rejected because different observers legitimately produced different
valid models.

## Model Equals Implementation

Rejected because implementations are derived from the model and vary
independently.

Neither alternative explained refinement, collaboration or incremental
truth.

------------------------------------------------------------------------

# 7. Discovery

The decisive insight was the separation of six distinct layers:

``` text
Reality
    ↓
Knowledge
    ↓
Statements
    ↓
Ontology
    ↓
Model
    ↓
Implementation
```

Each layer has a single responsibility.

Reality exists independently.

Knowledge represents what is known.

Statements express knowledge.

Ontology gives Statements meaning.

The model organises those Statements.

Implementations realise the model.

------------------------------------------------------------------------

# 8. Architectural Impact

This discovery unified several independent architectural decisions.

-   Statement First became the natural representation of knowledge.
-   Incremental Truth became a property of knowledge rather than
    reality.
-   Universal Refinement became a mechanism for increasing knowledge.
-   The Core became independent from implementation technologies.

The architecture became fundamentally knowledge-centric.

------------------------------------------------------------------------

# 9. Consequences

Future architectural discussions should first identify which layer a
concern belongs to.

Confusing these layers leads to unnecessary complexity and incorrect
abstractions.

Keeping them separate preserves both conceptual clarity and long-term
architectural stability.

This discovery provides the conceptual foundation that connects the
entire Persiqa architecture into a single coherent model.
