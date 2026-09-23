/**
 * Owns the semantic exploration and derived-knowledge proposal dialogs.
 *
 * The controller deliberately receives its dependencies from the application
 * composition root so this UI feature remains independent from global state.
 */
export function createSemanticController({
  graphProfile,
  loadKnowledge,
  relationTypeLabel,
  report,
  request,
  showPath,
  showStatus,
  state,
  translate
}) {
  const derivationHelp = document.querySelector("#derivation-help");
  const derivationProposalList = document.querySelector("#derivation-proposal-list");
  const findDerivationProposalsButton = document.querySelector("#find-derivation-proposals");
  const findDerivationProposalsHint = document.querySelector("#find-derivation-proposals-hint");
  const derivationProposalsDialog = document.querySelector("#derivation-proposals-dialog");
  const closeDerivationProposalsButton = document.querySelector("#close-derivation-proposals");
  const derivationFeedback = document.querySelector("#derivation-feedback");
  const semanticHelp = document.querySelector("#semantic-help");
  const exploreSemanticsButton = document.querySelector("#explore-semantics");
  const exploreSemanticsHint = document.querySelector("#explore-semantics-hint");
  const semanticTraversalDialog = document.querySelector("#semantic-traversal-dialog");
  const closeSemanticTraversalButton = document.querySelector("#close-semantic-traversal");
  const semanticTraversalSummary = document.querySelector("#semantic-traversal-summary");
  const semanticTraversalList = document.querySelector("#semantic-traversal-list");

  function canQuery() {
    return Boolean(state.scopeId && state.graphSource);
  }

  function clearDerivationProposals() {
    derivationProposalsDialog.close();
    derivationFeedback.textContent = "";
    state.derivationProposals = [];
    state.derivationQueried = false;
    renderDerivationProposals();
  }

  function clearSemanticTraversal() {
    semanticTraversalDialog.close();
    state.semanticTraversal = null;
    renderSemanticTraversal();
  }

  function renderSemanticTraversal() {
    const queryable = canQuery();
    exploreSemanticsButton.disabled = !queryable;
    exploreSemanticsHint.title = queryable ? "" : translate("semantic.noSource");
    semanticHelp.textContent = translate(queryable ? "semantic.ready" : "semantic.noSource");
    semanticTraversalList.replaceChildren();
    semanticTraversalSummary.textContent = "";
    if (!state.semanticTraversal) return;

    const traversal = state.semanticTraversal;
    semanticTraversalSummary.textContent = traversal.truncated
      ? translate("semantic.truncated", { count: traversal.maxHops })
      : translate("semantic.results", { count: traversal.matches.length, source: traversal.anchor.id });
    if (traversal.matches.length === 0) {
      semanticTraversalList.textContent = translate("semantic.none");
      return;
    }
    traversal.matches.forEach((match) => {
      const item = document.createElement("div");
      item.className = "item derivation-proposal";
      const title = document.createElement("strong");
      title.textContent = match.target.id;
      const hops = document.createElement("span");
      hops.textContent = translate("semantic.hops", { count: match.hops });
      const witness = document.createElement("span");
      const nodes = [traversal.anchor.id];
      match.witness.forEach((step) => {
        const current = nodes.at(-1);
        nodes.push(step.relation.source.id === current ? step.relation.target.id : step.relation.source.id);
      });
      witness.textContent = translate("semantic.witness", { path: nodes.join(" → ") });
      const showPath = document.createElement("button");
      showPath.type = "button";
      showPath.textContent = translate("semantic.showPath");
      showPath.addEventListener("click", () => showSemanticPath(match.target.id).catch(report));
      item.append(title, hops, witness, showPath);
      semanticTraversalList.append(item);
    });
  }

  async function loadSemanticTraversal() {
    if (!canQuery()) {
      showStatus(translate("semantic.noSource"), true);
      return;
    }
    exploreSemanticsButton.disabled = true;
    try {
      const query = new URLSearchParams({
        anchor: state.graphSource,
        relationType: graphProfile().relationType,
        direction: state.graphDirection,
        maxHops: "20"
      });
      state.semanticTraversal = await request(`/api/scopes/${state.scopeId}/semantic/traversal?${query}`);
      renderSemanticTraversal();
      semanticTraversalDialog.showModal();
    } finally {
      exploreSemanticsButton.disabled = false;
    }
  }

  async function showSemanticPath(destinationId) {
    semanticTraversalDialog.close();
    await showPath(destinationId);
  }

  function renderDerivationProposals() {
    const queryable = canQuery();
    findDerivationProposalsButton.disabled = !queryable;
    findDerivationProposalsHint.title = queryable ? "" : translate("derivation.noSource");
    derivationHelp.textContent = translate(queryable ? "derivation.ready" : "derivation.noSource");
    derivationProposalList.replaceChildren();
    if (!queryable || !state.derivationQueried) return;
    if (state.derivationProposals.length === 0) {
      derivationProposalList.textContent = translate("derivation.none");
      return;
    }
    state.derivationProposals.forEach((proposal) => {
      const item = document.createElement("div");
      item.className = "item derivation-proposal";
      const title = document.createElement("strong");
      title.textContent = `${proposal.source.id} —${relationTypeLabel(proposal.relationType)}→ ${proposal.target.id}`;
      const hops = document.createElement("span");
      hops.textContent = translate("derivation.hops", { count: proposal.hops });
      const evidence = document.createElement("span");
      evidence.textContent = translate("derivation.evidence", { ids: proposal.evidenceStatementIds.join(", ") });
      const accept = document.createElement("button");
      accept.type = "button";
      accept.textContent = translate("derivation.accept");
      accept.addEventListener("click", () => acceptDerivationProposal(proposal, accept).catch(report));
      item.append(title, hops, evidence, accept);
      derivationProposalList.append(item);
    });
  }

  async function loadDerivationProposals() {
    if (!canQuery()) {
      showStatus(translate("derivation.noSource"), true);
      return;
    }
    findDerivationProposalsButton.disabled = true;
    try {
      const query = new URLSearchParams({
        anchor: state.graphSource,
        relationType: graphProfile().relationType,
        direction: state.graphDirection,
        maxHops: "20"
      });
      state.derivationProposals = await request(
        `/api/scopes/${state.scopeId}/semantic/derivation-proposals?${query}`);
      state.derivationQueried = true;
      renderDerivationProposals();
      derivationProposalsDialog.showModal();
      showStatus(translate("derivation.found", { count: state.derivationProposals.length }));
    } finally {
      findDerivationProposalsButton.disabled = false;
    }
  }

  async function acceptDerivationProposal(proposal, button) {
    button.disabled = true;
    try {
      const record = await request(`/api/scopes/${state.scopeId}/semantic/derivations`, {
        method: "POST",
        body: JSON.stringify({
          anchor: state.graphSource,
          reachable: proposal.reachable.id,
          relationType: proposal.relationType,
          direction: state.graphDirection,
          maxHops: 20
        })
      });
      state.derivationProposals = state.derivationProposals.filter((candidate) => candidate !== proposal);
      await loadKnowledge(true);
      renderDerivationProposals();
      const accepted = translate("derivation.accepted", { statement: record.statement.id });
      derivationFeedback.textContent = accepted;
      showStatus(accepted);
    } catch (error) {
      button.disabled = false;
      throw error;
    }
  }

  function bind() {
    findDerivationProposalsButton.addEventListener("click", () => loadDerivationProposals().catch(report));
    closeDerivationProposalsButton.addEventListener("click", () => derivationProposalsDialog.close());
    exploreSemanticsButton.addEventListener("click", () => loadSemanticTraversal().catch(report));
    closeSemanticTraversalButton.addEventListener("click", () => semanticTraversalDialog.close());
  }

  return {
    bind,
    clearDerivationProposals,
    clearSemanticTraversal,
    renderDerivationProposals,
    renderSemanticTraversal
  };
}
