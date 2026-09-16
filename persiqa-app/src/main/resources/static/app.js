const translations = {
  en: {
    "app.title": "Canonical Knowledge Model", "language.label": "Language",
    "auth.subject": "Subject", "auth.password": "Password", "auth.connect": "Connect",
    "status.signIn": "Sign in with a development subject to begin.",
    "status.connected": "Connected. Select a scope or create a new one.",
    "status.connectFailed": "Could not connect: {detail}",
    "status.scopeCreated": "Created scope {name}.", "status.nodeRecorded": "Canonical node recorded.",
    "status.statementRecorded": "Recorded {statement} for {relation}.",
    "scopes.title": "Scopes", "scopes.refresh": "Refresh scopes", "scopes.newName": "New scope name",
    "scopes.name": "Scope name", "scopes.navigation": "Model scopes", "scope.canonical": "Canonical scope",
    "scope.select": "Select a scope", "actions.create": "Create", "actions.search": "Search",
    "actions.refreshGraph": "Refresh graph", "actions.close": "Close", "summary.nodes": "Nodes", "summary.relations": "Relations",
    "summary.statements": "Statements", "empty.knowledge": "No canonical knowledge recorded yet.",
    "node.addCanonical": "Add canonical node", "node.add": "Add node", "endpoint.identity": "Identity", "endpoint.kind": "Kind",
    "endpoint.sourceIdentity": "Source identity", "endpoint.sourceKind": "Source kind",
    "endpoint.targetIdentity": "Target identity", "endpoint.targetKind": "Target kind",
    "endpoint.sourceHint": "Choose or enter MCB-01", "endpoint.targetHint": "Choose or enter Outlet-01",
    "statement.record": "Record statement", "statement.relationIdentity": "Relation identity (optional)",
    "statement.relationIdentityHint": "Generated from source, type, and target",
    "statement.identity": "Statement identity (optional)",
    "statement.identityHint": "Generated from relation and knowledge kind",
    "statement.relationType": "Relation type", "statement.knowledgeKind": "Knowledge kind",
    "statement.evidence": "Evidence Statement identities (comma separated)",
    "statement.evidenceHint": "inspection-002, inspection-003", "knowledge.explicit": "Explicit",
    "knowledge.derived": "Derived", "context.title": "Knowledge context", "context.provenance": "Provenance",
    "context.provenanceHint": "as-built inspection", "context.confidence": "Confidence",
    "context.scenario": "Scenario", "context.scenarioHint": "installed",
    "kind.ENTITY": "Entity", "kind.CAPABILITY": "Capability", "kind.CONCEPT": "Concept",
    "kind.STATE": "State", "kind.RELATION": "Relation", "inspector.relation": "Relation details",
    "relation.classifiedAs": "Classified as", "relation.connectedTo": "Connected to",
    "relation.contains": "Contains", "relation.dependsOn": "Depends on",
    "relation.hasCapability": "Has capability", "relation.hasState": "Has state",
    "relation.hostedOn": "Hosted on", "relation.instanceOf": "Instance of",
    "relation.playsRole": "Plays role", "relation.supplies": "Supplies",
    "inspector.statement": "Statement details", "inspector.supportingStatements": "Supporting Statements",
    "inspector.originalContext": "Original context", "inspector.observations": "Later observations",
    "inspector.evidence": "Evidence", "inspector.none": "None", "inspector.unspecified": "Unspecified",
    "inspector.noObservations": "No later observations recorded.", "context.observedAt": "Observed at",
    "context.validFrom": "Valid from", "context.validTo": "Valid to", "statement.predicate": "Predicate",
    "statement.subject": "Subject", "statement.object": "Object",
    "graph.title": "Knowledge graph", "graph.help": "Drag a node to arrange it, drag the background to pan, use the wheel to zoom, and select a node or relation for details.",
    "graph.reset": "Reset view", "graph.node": "Node details", "graph.incoming": "Incoming Relations",
    "graph.outgoing": "Outgoing Relations", "graph.empty": "No graph elements recorded yet.",
    "graph.explicitCount": "E: {count}", "graph.derivedCount": "D: {count}",
    "graph.state": "State: {value}"
  },
  hu: {
    "app.title": "Kanonikus tudásmodell", "language.label": "Nyelv",
    "auth.subject": "Azonosító", "auth.password": "Jelszó", "auth.connect": "Csatlakozás",
    "status.signIn": "A kezdéshez jelentkezz be egy fejlesztői azonosítóval.",
    "status.connected": "Kapcsolódva. Válassz ki vagy hozz létre egy hatókört.",
    "status.connectFailed": "Nem sikerült kapcsolódni: {detail}",
    "status.scopeCreated": "A(z) {name} hatókör létrejött.", "status.nodeRecorded": "A kanonikus csomópont rögzítve.",
    "status.statementRecorded": "A(z) {statement} állítás rögzítve ehhez: {relation}.",
    "scopes.title": "Hatókörök", "scopes.refresh": "Hatókörök frissítése", "scopes.newName": "Új hatókör neve",
    "scopes.name": "Hatókör neve", "scopes.navigation": "Modellhatókörök", "scope.canonical": "Kanonikus hatókör",
    "scope.select": "Válassz egy hatókört", "actions.create": "Létrehozás", "actions.search": "Keresés",
    "actions.refreshGraph": "Gráf frissítése", "actions.close": "Bezárás", "summary.nodes": "Csomópontok", "summary.relations": "Kapcsolatok",
    "summary.statements": "Állítások", "empty.knowledge": "Még nincs rögzített kanonikus tudás.",
    "node.addCanonical": "Kanonikus csomópont hozzáadása", "node.add": "Csomópont hozzáadása", "endpoint.identity": "Azonosító", "endpoint.kind": "Típus",
    "endpoint.sourceIdentity": "Forrás azonosítója", "endpoint.sourceKind": "Forrás típusa",
    "endpoint.targetIdentity": "Cél azonosítója", "endpoint.targetKind": "Cél típusa",
    "endpoint.sourceHint": "Válaszd ki vagy add meg: MCB-01", "endpoint.targetHint": "Válaszd ki vagy add meg: Outlet-01",
    "statement.record": "Állítás rögzítése", "statement.relationIdentity": "Kapcsolat azonosítója (nem kötelező)",
    "statement.relationIdentityHint": "A forrásból, típusból és célból generálódik",
    "statement.identity": "Állítás azonosítója (nem kötelező)",
    "statement.identityHint": "A kapcsolatból és a tudás típusából generálódik",
    "statement.relationType": "Kapcsolattípus", "statement.knowledgeKind": "Tudás típusa",
    "statement.evidence": "Bizonyító állítások azonosítói (vesszővel elválasztva)",
    "statement.evidenceHint": "inspection-002, inspection-003", "knowledge.explicit": "Explicit",
    "knowledge.derived": "Származtatott", "context.title": "Tudáskontextus", "context.provenance": "Proveniencia",
    "context.provenanceHint": "megvalósulási felmérés", "context.confidence": "Bizonyosság",
    "context.scenario": "Szcenárió", "context.scenarioHint": "beépített",
    "kind.ENTITY": "Entitás", "kind.CAPABILITY": "Képesség", "kind.CONCEPT": "Fogalom",
    "kind.STATE": "Állapot", "kind.RELATION": "Kapcsolat", "inspector.relation": "Kapcsolat részletei",
    "relation.classifiedAs": "Besorolva mint", "relation.connectedTo": "Kapcsolódik ehhez",
    "relation.contains": "Tartalmaz", "relation.dependsOn": "Függ ettől",
    "relation.hasCapability": "Rendelkezik képességgel", "relation.hasState": "Állapota",
    "relation.hostedOn": "Ezen fut", "relation.instanceOf": "Példánya ennek",
    "relation.playsRole": "Ezt a szerepet tölti be", "relation.supplies": "Ellátja",
    "inspector.statement": "Állítás részletei", "inspector.supportingStatements": "Alátámasztó állítások",
    "inspector.originalContext": "Eredeti kontextus", "inspector.observations": "Későbbi megfigyelések",
    "inspector.evidence": "Bizonyíték", "inspector.none": "Nincs", "inspector.unspecified": "Nincs megadva",
    "inspector.noObservations": "Nincs rögzített későbbi megfigyelés.", "context.observedAt": "Megfigyelés ideje",
    "context.validFrom": "Érvényesség kezdete", "context.validTo": "Érvényesség vége", "statement.predicate": "Predikátum",
    "statement.subject": "Alany", "statement.object": "Tárgy",
    "graph.title": "Tudásgráf", "graph.help": "Az elrendezéshez húzz egy csomópontot, a pásztázáshoz a hátteret, a nagyításhoz használd a görgőt, részletekhez pedig válassz egy csomópontot vagy kapcsolatot.",
    "graph.reset": "Nézet alaphelyzetbe", "graph.node": "Csomópont részletei", "graph.incoming": "Bejövő kapcsolatok",
    "graph.outgoing": "Kimenő kapcsolatok", "graph.empty": "Még nincs megjeleníthető gráfelem.",
    "graph.explicitCount": "E: {count}", "graph.derivedCount": "Sz: {count}",
    "graph.state": "Állapot: {value}"
  }
};

const state = {
  authorization: null, scopeId: null, relationTypes: [], endpointKinds: new Map(), knowledge: null,
  language: localStorage.getItem("persiqa.language") || navigator.language?.slice(0, 2) || "en"
};

const statusElement = document.querySelector("#status");
const workspace = document.querySelector("#workspace");
const workbench = document.querySelector("#scope-workbench");
const scopeList = document.querySelector("#scope-list");
const inspector = document.querySelector("#inspector");
const inspectorTitle = document.querySelector("#inspector-title");
const inspectorContent = document.querySelector("#inspector-content");
const graphSvg = document.querySelector("#knowledge-graph");
const graphViewport = document.querySelector("#graph-viewport");
const graphLegend = document.querySelector("#graph-legend");
const graphState = {
  x: 0, y: 0, scale: 1, dragging: null, nodeDragging: null,
  suppressClickNodeId: null, updateEdges: null
};
const svgNamespace = "http://www.w3.org/2000/svg";

if (!translations[state.language]) state.language = "en";

function t(key, values = {}) {
  return translations[state.language][key]?.replace(/\{(\w+)\}/g, (_, name) => values[name] ?? `{${name}}`) || key;
}

function kindLabel(kind) { return t(`kind.${kind}`); }

function relationTypeLabel(typeId) {
  const key = `relation.${typeId}`;
  return translations[state.language][key] || typeId;
}

function applyTranslations() {
  document.documentElement.lang = state.language;
  document.querySelector("#language").value = state.language;
  document.querySelectorAll("[data-i18n]").forEach((element) => { element.textContent = t(element.dataset.i18n); });
  document.querySelectorAll("[data-i18n-placeholder]").forEach((element) => {
    element.placeholder = t(element.dataset.i18nPlaceholder);
  });
  document.querySelectorAll("[data-i18n-title]").forEach((element) => { element.title = t(element.dataset.i18nTitle); });
  document.querySelectorAll("[data-i18n-aria-label]").forEach((element) => {
    element.setAttribute("aria-label", t(element.dataset.i18nAriaLabel));
  });
  populateNodeKinds();
  renderRelationTypes();
  if (state.knowledge) renderKnowledgeGraph(state.knowledge);
}

function showStatus(message, error = false) {
  statusElement.textContent = message;
  statusElement.classList.toggle("error", error);
}

async function request(path, options = {}) {
  const headers = new Headers(options.headers || {});
  if (state.authorization) headers.set("Authorization", state.authorization);
  if (options.body) headers.set("Content-Type", "application/json");
  const response = await fetch(path, { ...options, headers });
  if (response.ok) return response.status === 204 ? null : response.json();
  let detail = `${response.status} ${response.statusText}`;
  try { detail = (await response.json()).detail || detail; } catch (_) { /* Keep HTTP detail. */ }
  throw new Error(detail);
}

function textItem(primary, secondary) {
  const item = document.createElement("div");
  item.className = "item";
  const title = document.createElement("strong");
  title.textContent = primary;
  const detail = document.createElement("span");
  detail.textContent = secondary;
  item.append(title, detail);
  return item;
}

function selectableItem(primary, secondary, onSelect) {
  const item = document.createElement("div");
  item.className = "item";
  const button = document.createElement("button");
  button.type = "button";
  const title = document.createElement("strong");
  title.textContent = primary;
  const detail = document.createElement("span");
  detail.textContent = secondary;
  button.append(title, detail);
  button.addEventListener("click", onSelect);
  item.append(button);
  return item;
}

function appendMetadata(target, entries) {
  const metadata = document.createElement("dl");
  metadata.className = "metadata";
  entries.forEach(([label, value]) => {
    const term = document.createElement("dt");
    term.textContent = label;
    const definition = document.createElement("dd");
    definition.textContent = value ?? t("inspector.unspecified");
    metadata.append(term, definition);
  });
  target.append(metadata);
}

function showInspector(title) {
  inspectorTitle.textContent = title;
  inspectorContent.replaceChildren();
  inspector.classList.remove("hidden");
}

function hideInspector() { inspector.classList.add("hidden"); }

function renderItems(target, items, mapper) {
  target.replaceChildren();
  if (items.length === 0) {
    target.textContent = t("empty.knowledge");
    return;
  }
  items.forEach((item) => target.append(mapper(item)));
}

function renderScopeList(scopes) {
  scopeList.replaceChildren();
  scopes.forEach((scope) => {
    const button = document.createElement("button");
    button.className = `scope-item${scope.id === state.scopeId ? " active" : ""}`;
    button.type = "button";
    const name = document.createElement("strong");
    name.textContent = scope.name;
    const owner = document.createElement("small");
    owner.textContent = scope.ownerSubject;
    button.append(name, owner);
    button.addEventListener("click", () => selectScope(scope.id));
    scopeList.append(button);
  });
}

async function loadScopes() {
  const query = document.querySelector("#scope-search").value.trim();
  const page = await request(`/api/scopes?size=100${query ? `&q=${encodeURIComponent(query)}` : ""}`);
  renderScopeList(page.content);
}

async function loadRelationTypes() {
  state.relationTypes = await request("/api/relation-types");
  renderRelationTypes();
}

function renderRelationTypes() {
  const relationType = document.querySelector("#relation-type");
  const selected = relationType.value;
  relationType.replaceChildren();
  state.relationTypes.forEach((type) => {
    const option = document.createElement("option");
    option.value = type.id;
    option.textContent = relationTypeLabel(type.id);
    relationType.append(option);
  });
  relationType.value = selected || state.relationTypes[0]?.id || "";
}

function renderKnowledge(knowledge) {
  state.knowledge = knowledge;
  renderEndpointOptions(knowledge);
  document.querySelector("#scope-title").textContent = knowledge.scope.name;
  const summary = document.querySelector("#graph-summary");
  summary.replaceChildren();
  [["summary.nodes", knowledge.nodes.length], ["summary.relations", knowledge.relations.length], ["summary.statements", knowledge.statements.length]]
    .forEach(([label, count]) => {
      const card = document.createElement("div");
      const number = document.createElement("strong");
      number.textContent = count;
      card.append(number, document.createTextNode(t(label)));
      summary.append(card);
    });
  renderItems(document.querySelector("#node-list"), knowledge.nodes,
    (node) => textItem(node.id, kindLabel(node.kind)));
  renderItems(document.querySelector("#relation-list"), knowledge.relations,
    (relation) => selectableItem(
      relation.id,
      `${relation.source.id} —${relationTypeLabel(relation.type.id)}→ ${relation.target.id}`,
      () => showRelationDetails(relation)));
  renderItems(document.querySelector("#statement-list"), knowledge.statements,
    (statement) => selectableItem(
      statement.id,
      `${t(`knowledge.${statement.knowledgeKind.toLowerCase()}`)} · ${relationTypeLabel(statement.predicate)}`,
      () => showStatementDetails(statement.id)));
  renderKnowledgeGraph(knowledge);
}

function svgElement(name, attributes = {}) {
  const element = document.createElementNS(svgNamespace, name);
  Object.entries(attributes).forEach(([key, value]) => element.setAttribute(key, value));
  return element;
}

function relationColor(typeId) {
  const palette = ["#38bdf8", "#a78bfa", "#fbbf24", "#34d399", "#fb7185", "#f97316"];
  const value = [...typeId].reduce((total, character) => total + character.charCodeAt(0), 0);
  return palette[value % palette.length];
}

function graphNodes(knowledge, topologyRelations) {
  const nodes = new Map(knowledge.nodes
    .filter((node) => node.kind !== "STATE")
    .map((node) => [node.id, node]));
  topologyRelations.forEach((relation) => {
    nodes.set(relation.source.id, relation.source);
    nodes.set(relation.target.id, relation.target);
  });
  return [...nodes.values()]
    .filter((node) => node.kind !== "STATE")
    .sort((left, right) => left.id.localeCompare(right.id));
}

function statesByEntity(relations) {
  return relations
    .filter((relation) => relation.type.id === "hasState"
      && relation.source.kind === "ENTITY"
      && relation.target.kind === "STATE")
    .reduce((states, relation) => {
      const ownerStates = states.get(relation.source.id) || [];
      ownerStates.push(relation.target.id);
      states.set(relation.source.id, ownerStates);
      return states;
    }, new Map());
}

function graphLayoutKey() {
  return `persiqa.graph.layout.${state.scopeId}`;
}

function savedGraphPositions() {
  try {
    const saved = JSON.parse(localStorage.getItem(graphLayoutKey()) || "{}");
    return new Map(Object.entries(saved).filter(([, position]) =>
      Number.isFinite(position?.x) && Number.isFinite(position?.y)));
  } catch (_) {
    return new Map();
  }
}

function saveGraphPosition(nodeId, position) {
  const positions = Object.fromEntries(savedGraphPositions());
  positions[nodeId] = position;
  localStorage.setItem(graphLayoutKey(), JSON.stringify(positions));
}

function graphPositions(nodes) {
  const centerX = 500;
  const centerY = 300;
  const radius = Math.max(120, Math.min(230, 38 * nodes.length));
  const generated = new Map(nodes.map((node, index) => {
    const angle = (2 * Math.PI * index / nodes.length) - (Math.PI / 2);
    return [node.id, { x: centerX + radius * Math.cos(angle), y: centerY + radius * Math.sin(angle) }];
  }));
  const saved = savedGraphPositions();
  nodes.forEach((node) => {
    if (saved.has(node.id)) generated.set(node.id, saved.get(node.id));
  });
  return generated;
}

function updateGraphTransform() {
  graphViewport.setAttribute("transform", `translate(${graphState.x} ${graphState.y}) scale(${graphState.scale})`);
  graphSvg.classList.toggle("semantic-overview", graphState.scale < 0.75);
  graphSvg.classList.toggle("semantic-detail", graphState.scale >= 1.35);
}

function resetGraphView() {
  graphState.x = 0;
  graphState.y = 0;
  graphState.scale = 1;
  updateGraphTransform();
}

function renderKnowledgeGraph(knowledge) {
  graphViewport.replaceChildren();
  graphLegend.replaceChildren();
  const topologyRelations = knowledge.relations.filter((relation) => relation.type.id !== "hasState");
  const states = statesByEntity(knowledge.relations);
  const nodes = graphNodes(knowledge, topologyRelations);
  if (nodes.length === 0) {
    const message = svgElement("text", { x: "500", y: "300", "text-anchor": "middle", fill: "#aebbd0" });
    message.textContent = t("graph.empty");
    graphViewport.append(message);
    resetGraphView();
    return;
  }
  const positions = graphPositions(nodes);
  const edgeElements = new Map();
  const relationTypes = [...new Set(topologyRelations.map((relation) => relation.type.id))].sort();
  relationTypes.forEach((typeId) => {
    const item = document.createElement("span");
    const marker = document.createElement("i");
    marker.style.backgroundColor = relationColor(typeId);
    item.append(marker, document.createTextNode(relationTypeLabel(typeId)));
    graphLegend.append(item);
  });
  topologyRelations.forEach((relation) => {
    const source = positions.get(relation.source.id);
    const target = positions.get(relation.target.id);
    const edge = svgElement("line", {
      class: "graph-edge", x1: source.x, y1: source.y, x2: target.x, y2: target.y,
      stroke: relationColor(relation.type.id)
    });
    edge.addEventListener("click", (event) => {
      event.stopPropagation();
      showRelationDetails(relation);
    });
    graphViewport.append(edge);
    const label = svgElement("text", {
      class: "graph-edge-label", x: (source.x + target.x) / 2, y: (source.y + target.y) / 2 - 7
    });
    label.textContent = relationTypeLabel(relation.type.id);
    graphViewport.append(label);
    const detail = svgElement("text", {
      class: "graph-edge-label graph-edge-detail", x: (source.x + target.x) / 2, y: (source.y + target.y) / 2 + 8
    });
    const counts = statementCountsForRelation(relation, knowledge.statements);
    detail.textContent = `${t("graph.explicitCount", { count: counts.explicit })} · ${t("graph.derivedCount", { count: counts.derived })}`;
    graphViewport.append(detail);
    edgeElements.set(relation.id, { edge, label, detail });
  });
  graphState.updateEdges = () => {
    topologyRelations.forEach((relation) => {
      const source = positions.get(relation.source.id);
      const target = positions.get(relation.target.id);
      const elements = edgeElements.get(relation.id);
      elements.edge.setAttribute("x1", source.x);
      elements.edge.setAttribute("y1", source.y);
      elements.edge.setAttribute("x2", target.x);
      elements.edge.setAttribute("y2", target.y);
      elements.label.setAttribute("x", (source.x + target.x) / 2);
      elements.label.setAttribute("y", (source.y + target.y) / 2 - 7);
      elements.detail.setAttribute("x", (source.x + target.x) / 2);
      elements.detail.setAttribute("y", (source.y + target.y) / 2 + 8);
    });
  };
  nodes.forEach((node) => {
    const position = positions.get(node.id);
    const group = svgElement("g", { class: "graph-node", transform: `translate(${position.x} ${position.y})`, tabindex: "0", role: "button" });
    const circle = svgElement("circle", { r: "38" });
    const label = svgElement("text", { y: "-3" });
    label.textContent = node.id;
    const kind = svgElement("text", { class: "node-kind-label", y: "15" });
    kind.textContent = kindLabel(node.kind);
    const counts = statementCountsForNode(node.id, knowledge.statements);
    const knowledgeLabel = svgElement("text", { class: "node-knowledge-label", y: "29" });
    knowledgeLabel.textContent = `${t("graph.explicitCount", { count: counts.explicit })} · ${t("graph.derivedCount", { count: counts.derived })}`;
    const stateLabel = svgElement("text", { class: "node-state-label", y: "45" });
    const stateIds = states.get(node.id);
    if (stateIds) stateLabel.textContent = t("graph.state", { value: stateIds.join(" · ") });
    const selectNode = (event) => {
      event.stopPropagation();
      if (graphState.suppressClickNodeId === node.id) {
        graphState.suppressClickNodeId = null;
        return;
      }
      showNodeDetails(node, knowledge.relations);
    };
    group.addEventListener("pointerdown", (event) => {
      if (event.button !== 0) return;
      event.stopPropagation();
      const pointer = graphCoordinates(graphPoint(event));
      graphState.nodeDragging = {
        pointerId: event.pointerId, nodeId: node.id, group, position,
        offsetX: pointer.x - position.x, offsetY: pointer.y - position.y, moved: false
      };
      graphSvg.setPointerCapture(event.pointerId);
    });
    group.addEventListener("click", selectNode);
    group.addEventListener("keydown", (event) => {
      if (event.key === "Enter" || event.key === " ") selectNode(event);
    });
    group.append(circle, label, kind, knowledgeLabel);
    if (stateIds) group.append(stateLabel);
    graphViewport.append(group);
  });
  resetGraphView();
}

function statementObjectId(statement) {
  return statement.object?.id ?? statement.object;
}

function statementCounts(statements) {
  return statements.reduce(
    (counts, statement) => ({
      ...counts,
      [statement.knowledgeKind.toLowerCase()]: counts[statement.knowledgeKind.toLowerCase()] + 1
    }),
    { explicit: 0, derived: 0 });
}

function statementCountsForRelation(relation, statements) {
  return statementCounts(statements.filter((statement) =>
    statement.subject.id === relation.source.id
      && statement.predicate === relation.type.id
      && statementObjectId(statement) === relation.target.id));
}

function statementCountsForNode(nodeId, statements) {
  return statementCounts(statements.filter((statement) =>
    statement.subject.id === nodeId || statementObjectId(statement) === nodeId));
}

function appendRelationButtons(title, relations) {
  const heading = document.createElement("h4");
  heading.textContent = title;
  inspectorContent.append(heading);
  if (relations.length === 0) {
    const none = document.createElement("p");
    none.textContent = t("inspector.none");
    inspectorContent.append(none);
    return;
  }
  const list = document.createElement("div");
  list.className = "inspector-list";
  relations.forEach((relation) => {
    const button = document.createElement("button");
    button.type = "button";
    button.textContent = `${relation.id} · ${relationTypeLabel(relation.type.id)}`;
    button.addEventListener("click", () => showRelationDetails(relation));
    list.append(button);
  });
  inspectorContent.append(list);
}

function showNodeDetails(node, relations) {
  showInspector(`${t("graph.node")}: ${node.id}`);
  appendMetadata(inspectorContent, [[t("endpoint.kind"), kindLabel(node.kind)]]);
  appendRelationButtons(t("graph.outgoing"), relations.filter((relation) => relation.source.id === node.id));
  appendRelationButtons(t("graph.incoming"), relations.filter((relation) => relation.target.id === node.id));
}

function renderEndpointOptions(knowledge) {
  state.endpointKinds.clear();
  knowledge.nodes.forEach((node) => state.endpointKinds.set(node.id, node.kind));
  knowledge.relations.forEach((relation) => state.endpointKinds.set(relation.id, "RELATION"));
  const options = document.querySelector("#endpoint-options");
  options.replaceChildren();
  state.endpointKinds.forEach((kind, id) => {
    const option = document.createElement("option");
    option.value = id;
    option.label = kindLabel(kind);
    options.append(option);
  });
}

function synchronizeEndpointKind(identityInput, kindSelect) {
  const kind = state.endpointKinds.get(identityInput.value.trim());
  if (kind) kindSelect.value = kind;
  kindSelect.disabled = Boolean(kind);
}

function resetEndpointKind(kindSelect) {
  kindSelect.disabled = false;
}

function resetRelationForm(formElement) {
  formElement.reset();
  resetEndpointKind(document.querySelector("#source-kind"));
  resetEndpointKind(document.querySelector("#target-kind"));
  document.querySelector("#evidence-field").classList.add("hidden");
}

function endpointFrom(form, identityName, kindName) {
  const id = form.get(identityName).trim();
  if (state.endpointKinds.has(id)) return { id };
  return { id, kind: form.get(kindName) };
}

async function loadKnowledge() {
  if (!state.scopeId) return;
  renderKnowledge(await request(`/api/scopes/${state.scopeId}/knowledge`));
}

async function showRelationDetails(relation) {
  showInspector(`${t("inspector.relation")}: ${relation.id}`);
  appendMetadata(inspectorContent, [
    [t("statement.subject"), `${relation.source.id} (${kindLabel(relation.source.kind)})`],
    [t("statement.predicate"), relationTypeLabel(relation.type.id)],
    [t("statement.object"), `${relation.target.id} (${kindLabel(relation.target.kind)})`]
  ]);
  try {
    const statements = await request(`/api/scopes/${state.scopeId}/relations/${encodeURIComponent(relation.id)}/statements`);
    const heading = document.createElement("h4");
    heading.textContent = t("inspector.supportingStatements");
    const list = document.createElement("div");
    list.className = "inspector-list";
    statements.forEach((statement) => {
      const button = document.createElement("button");
      button.type = "button";
      button.textContent = `${statement.id} · ${t(`knowledge.${statement.knowledgeKind.toLowerCase()}`)}`;
      button.addEventListener("click", () => showStatementDetails(statement.id));
      list.append(button);
    });
    inspectorContent.append(heading, list);
  } catch (error) { report(error); }
}

function contextEntries(context) {
  return [
    [t("context.provenance"), context?.provenance], [t("context.confidence"), context?.confidence],
    [t("context.scenario"), context?.scenario], [t("context.observedAt"), context?.observedAt],
    [t("context.validFrom"), context?.validFrom], [t("context.validTo"), context?.validTo]
  ];
}

async function showStatementDetails(statementId) {
  try {
    const [statement, observations] = await Promise.all([
      request(`/api/scopes/${state.scopeId}/statements/${encodeURIComponent(statementId)}`),
      request(`/api/scopes/${state.scopeId}/statements/${encodeURIComponent(statementId)}/observations`)
    ]);
    showInspector(`${t("inspector.statement")}: ${statement.id}`);
    appendMetadata(inspectorContent, [
      [t("statement.knowledgeKind"), t(`knowledge.${statement.knowledgeKind.toLowerCase()}`)],
      [t("statement.predicate"), relationTypeLabel(statement.predicate)], [t("statement.subject"), statement.subject.id],
      [t("statement.object"), statement.object?.id ?? statement.object],
      [t("inspector.evidence"), statement.derivedFrom.join(", ") || t("inspector.none")]
    ]);
    const contextHeading = document.createElement("h4");
    contextHeading.textContent = t("inspector.originalContext");
    inspectorContent.append(contextHeading);
    appendMetadata(inspectorContent, contextEntries(statement.context));
    const observationsHeading = document.createElement("h4");
    observationsHeading.textContent = t("inspector.observations");
    inspectorContent.append(observationsHeading);
    if (observations.length === 0) {
      const none = document.createElement("p");
      none.textContent = t("inspector.noObservations");
      inspectorContent.append(none);
    } else {
      observations.forEach((observation) => appendMetadata(inspectorContent, contextEntries(observation)));
    }
  } catch (error) { report(error); }
}

async function selectScope(scopeId) {
  state.scopeId = scopeId;
  workbench.classList.remove("hidden");
  await Promise.all([loadScopes(), loadKnowledge()]);
}

function populateNodeKinds() {
  document.querySelectorAll(".node-kind").forEach((select) => {
    const selected = select.value;
    select.replaceChildren();
    select.dataset.kinds.split(",").forEach((kind) => {
      const option = document.createElement("option");
      option.value = kind;
      option.textContent = kindLabel(kind);
      select.append(option);
    });
    select.value = selected || select.dataset.kinds.split(",")[0];
  });
}

function contextFrom(form) {
  const confidence = form.get("confidence");
  const context = {
    provenance: form.get("provenance") || null,
    confidence: confidence ? Number(confidence) : null,
    scenario: form.get("scenario") || null
  };
  return Object.values(context).some((value) => value !== null) ? context : null;
}

document.querySelector("#sign-in-form").addEventListener("submit", async (event) => {
  event.preventDefault();
  const form = new FormData(event.currentTarget);
  state.authorization = `Basic ${btoa(`${form.get("username")}:${form.get("password")}`)}`;
  try {
    await Promise.all([loadScopes(), loadRelationTypes()]);
    workspace.classList.remove("hidden");
    showStatus(t("status.connected"));
  } catch (error) {
    state.authorization = null;
    showStatus(t("status.connectFailed", { detail: error.message }), true);
  }
});

document.querySelector("#refresh-scopes").addEventListener("click", () => loadScopes().catch(report));
document.querySelector("#refresh-knowledge").addEventListener("click", () => loadKnowledge().catch(report));
document.querySelector("#close-inspector").addEventListener("click", hideInspector);
document.querySelector("#reset-graph-view").addEventListener("click", resetGraphView);
document.querySelector("#scope-search").addEventListener("input", () => loadScopes().catch(report));
document.querySelector("#language").addEventListener("change", (event) => {
  state.language = event.target.value;
  localStorage.setItem("persiqa.language", state.language);
  applyTranslations();
  hideInspector();
  if (state.scopeId) loadKnowledge().catch(report);
});
document.querySelector("#knowledge-kind").addEventListener("change", (event) => {
  document.querySelector("#evidence-field").classList.toggle("hidden", event.target.value !== "DERIVED");
});
document.querySelector("#source-id").addEventListener("input", (event) => {
  synchronizeEndpointKind(
    event.target,
    document.querySelector("#source-kind"));
});
document.querySelector("#target-id").addEventListener("input", (event) => {
  synchronizeEndpointKind(
    event.target,
    document.querySelector("#target-kind"));
});

function graphPoint(event) {
  const bounds = graphSvg.getBoundingClientRect();
  return {
    x: (event.clientX - bounds.left) * 1000 / bounds.width,
    y: (event.clientY - bounds.top) * 600 / bounds.height
  };
}

function graphCoordinates(point) {
  return {
    x: (point.x - graphState.x) / graphState.scale,
    y: (point.y - graphState.y) / graphState.scale
  };
}

graphSvg.addEventListener("pointerdown", (event) => {
  if (event.target.closest(".graph-node, .graph-edge")) return;
  graphState.dragging = { pointerId: event.pointerId, point: graphPoint(event), x: graphState.x, y: graphState.y };
  graphSvg.setPointerCapture(event.pointerId);
});
graphSvg.addEventListener("pointermove", (event) => {
  if (graphState.nodeDragging?.pointerId === event.pointerId) {
    const dragging = graphState.nodeDragging;
    const pointer = graphCoordinates(graphPoint(event));
    const next = { x: pointer.x - dragging.offsetX, y: pointer.y - dragging.offsetY };
    if (Math.abs(next.x - dragging.position.x) > 1 || Math.abs(next.y - dragging.position.y) > 1) {
      dragging.moved = true;
    }
    dragging.position.x = next.x;
    dragging.position.y = next.y;
    dragging.group.setAttribute("transform", `translate(${next.x} ${next.y})`);
    graphState.updateEdges?.();
    return;
  }
  if (!graphState.dragging || graphState.dragging.pointerId !== event.pointerId) return;
  const point = graphPoint(event);
  graphState.x = graphState.dragging.x + point.x - graphState.dragging.point.x;
  graphState.y = graphState.dragging.y + point.y - graphState.dragging.point.y;
  updateGraphTransform();
});
graphSvg.addEventListener("pointerup", (event) => {
  if (graphState.nodeDragging?.pointerId === event.pointerId) {
    const dragging = graphState.nodeDragging;
    if (dragging.moved) {
      saveGraphPosition(dragging.nodeId, dragging.position);
      graphState.suppressClickNodeId = dragging.nodeId;
    }
    graphState.nodeDragging = null;
    return;
  }
  if (graphState.dragging?.pointerId === event.pointerId) graphState.dragging = null;
});
graphSvg.addEventListener("wheel", (event) => {
  event.preventDefault();
  const point = graphPoint(event);
  const nextScale = Math.min(3, Math.max(0.35, graphState.scale * (event.deltaY < 0 ? 1.12 : 0.88)));
  const factor = nextScale / graphState.scale;
  graphState.x = point.x - (point.x - graphState.x) * factor;
  graphState.y = point.y - (point.y - graphState.y) * factor;
  graphState.scale = nextScale;
  updateGraphTransform();
}, { passive: false });

document.querySelector("#create-scope-form").addEventListener("submit", async (event) => {
  event.preventDefault();
  const formElement = event.currentTarget;
  try {
    const scope = await request("/api/scopes", {
      method: "POST",
      body: JSON.stringify({ name: document.querySelector("#scope-name").value })
    });
    formElement.reset();
    await selectScope(scope.id);
    showStatus(t("status.scopeCreated", { name: scope.name }));
  } catch (error) { report(error); }
});

document.querySelector("#create-node-form").addEventListener("submit", async (event) => {
  event.preventDefault();
  if (!state.scopeId) return;
  const formElement = event.currentTarget;
  try {
    const form = new FormData(formElement);
    await request(`/api/scopes/${state.scopeId}/nodes`, {
      method: "POST",
      body: JSON.stringify({ id: form.get("id"), kind: form.get("kind") })
    });
    formElement.reset();
    await loadKnowledge();
    showStatus(t("status.nodeRecorded"));
  } catch (error) { report(error); }
});

document.querySelector("#record-relation-form").addEventListener("submit", async (event) => {
  event.preventDefault();
  if (!state.scopeId) return;
  const formElement = event.currentTarget;
  try {
    const form = new FormData(formElement);
    const knowledgeKind = form.get("knowledgeKind");
    const evidence = String(form.get("derivedFrom") || "")
      .split(",").map((value) => value.trim()).filter(Boolean);
    const record = await request(`/api/scopes/${state.scopeId}/statements`, {
      method: "POST",
      body: JSON.stringify({
        relationId: form.get("relationId"), statementId: form.get("statementId"), knowledgeKind,
        relationType: form.get("relationType"),
        source: endpointFrom(form, "sourceId", "sourceKind"),
        target: endpointFrom(form, "targetId", "targetKind"),
        derivedFrom: knowledgeKind === "DERIVED" ? evidence : [], context: contextFrom(form)
      })
    });
    resetRelationForm(formElement);
    await loadKnowledge();
    showStatus(t("status.statementRecorded", { statement: record.statement.id, relation: record.relation.id }));
  } catch (error) { report(error); }
});

function report(error) { showStatus(error.message, true); }

applyTranslations();
resetRelationForm(document.querySelector("#record-relation-form"));
