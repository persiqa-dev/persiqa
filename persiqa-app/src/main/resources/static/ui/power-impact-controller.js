/**
 * Presents a read-only electrical interruption analysis without recording a State change.
 *
 * Like the semantic controller, this receives the application dependencies explicitly so
 * it remains a small, independently understandable UI feature.
 */
export function createPowerImpactController({
  graphProfile,
  report,
  request,
  showPath,
  showStatus,
  state,
  translate
}) {
  const help = document.querySelector("#power-impact-help");
  const analyzeButton = document.querySelector("#analyze-power-impact");
  const analyzeHint = document.querySelector("#analyze-power-impact-hint");
  const dialog = document.querySelector("#power-impact-dialog");
  const closeButton = document.querySelector("#close-power-impact");
  const summary = document.querySelector("#power-impact-summary");
  const list = document.querySelector("#power-impact-list");

  function queryability() {
    if (!state.scopeId || !state.graphSource) return { allowed: false, reason: "powerImpact.noSource" };
    if (graphProfile().relationType !== "supplies") {
      return { allowed: false, reason: "powerImpact.notElectrical" };
    }
    return { allowed: true };
  }

  function clear() {
    dialog.close();
    state.powerImpact = null;
    render();
  }

  function witnessPath(impact, match) {
    const nodes = [impact.interruptedNode.id];
    match.witness.forEach((step) => {
      const current = nodes.at(-1);
      nodes.push(step.relation.source.id === current ? step.relation.target.id : step.relation.source.id);
    });
    return nodes.join(" → ");
  }

  function render() {
    const queryabilityState = queryability();
    analyzeButton.disabled = !queryabilityState.allowed;
    analyzeHint.title = queryabilityState.allowed ? "" : translate(queryabilityState.reason);
    help.textContent = translate(
        queryabilityState.allowed ? "powerImpact.ready" : queryabilityState.reason);
    summary.textContent = "";
    list.replaceChildren();
    if (!state.powerImpact) return;

    const impact = state.powerImpact;
    summary.textContent = impact.truncated
      ? translate("powerImpact.truncated")
      : translate("powerImpact.results", {
          count: impact.impacted.length,
          device: impact.interruptedNode.id
        });
    if (impact.impacted.length === 0) {
      list.textContent = translate("powerImpact.none");
      return;
    }
    impact.impacted.forEach((match) => {
      const item = document.createElement("div");
      item.className = "item derivation-proposal";
      const title = document.createElement("strong");
      title.textContent = match.target.id;
      const hops = document.createElement("span");
      hops.textContent = translate("semantic.hops", { count: match.hops });
      const witness = document.createElement("span");
      witness.textContent = translate("semantic.witness", { path: witnessPath(impact, match) });
      const path = document.createElement("button");
      path.type = "button";
      path.textContent = translate("semantic.showPath");
      path.addEventListener("click", () => {
        dialog.close();
        showPath(match.target.id).catch(report);
      });
      item.append(title, hops, witness, path);
      list.append(item);
    });
  }

  async function analyze() {
    const queryabilityState = queryability();
    if (!queryabilityState.allowed) {
      showStatus(translate(queryabilityState.reason), true);
      return;
    }
    analyzeButton.disabled = true;
    try {
      const query = new URLSearchParams({ interrupted: state.graphSource });
      state.powerImpact = await request(`/api/scopes/${state.scopeId}/analysis/power-impact?${query}`);
      render();
      dialog.showModal();
      showStatus(translate("powerImpact.found", { count: state.powerImpact.impacted.length }));
    } finally {
      analyzeButton.disabled = false;
    }
  }

  function bind() {
    analyzeButton.addEventListener("click", () => analyze().catch(report));
    closeButton.addEventListener("click", () => dialog.close());
  }

  return { bind, clear, render };
}
