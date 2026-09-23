/** Presents non-destructive electrical topology observations in a focused dialog. */
export function createTopologyDiagnosticsController({
  graphProfile,
  report,
  request,
  showStatus,
  state,
  translate
}) {
  const button = document.querySelector("#inspect-topology");
  const hint = document.querySelector("#inspect-topology-hint");
  const help = document.querySelector("#topology-diagnostics-help");
  const dialog = document.querySelector("#topology-diagnostics-dialog");
  const close = document.querySelector("#close-topology-diagnostics");
  const list = document.querySelector("#topology-diagnostics-list");

  function queryable() {
    return Boolean(state.scopeId && graphProfile().relationType === "supplies");
  }

  function render() {
    const allowed = queryable();
    button.disabled = !allowed;
    hint.title = allowed ? "" : translate("diagnostics.notElectrical");
    help.textContent = translate(allowed ? "diagnostics.ready" : "diagnostics.notElectrical");
  }

  function clear() {
    dialog.close();
    list.replaceChildren();
    render();
  }

  function renderDiagnostics(diagnostics) {
    list.replaceChildren();
    if (diagnostics.length === 0) {
      list.textContent = translate("diagnostics.none");
      return;
    }
    diagnostics.forEach((diagnostic) => {
      const item = document.createElement("div");
      item.className = "item";
      const title = document.createElement("strong");
      title.textContent = translate(`diagnostics.${diagnostic.code}`);
      const detail = document.createElement("span");
      const related = diagnostic.relatedNodes.map((node) => node.id).join(" → ");
      detail.textContent = `${diagnostic.node.id}: ${related}`;
      item.append(title, detail);
      list.append(item);
    });
  }

  async function inspect() {
    if (!queryable()) {
      showStatus(translate("diagnostics.notElectrical"), true);
      return;
    }
    button.disabled = true;
    try {
      const result = await request(`/api/scopes/${state.scopeId}/analysis/topology-diagnostics`);
      renderDiagnostics(result.diagnostics);
      dialog.showModal();
    } finally {
      button.disabled = false;
    }
  }

  function bind() {
    button.addEventListener("click", () => inspect().catch(report));
    close.addEventListener("click", () => dialog.close());
  }

  return { bind, clear, render };
}
