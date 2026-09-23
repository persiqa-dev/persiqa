import { createRecordingController } from "./ui/recording-controller.js";
import { createPowerImpactController } from "./ui/power-impact-controller.js";
import { createSemanticController } from "./ui/semantic-controller.js";

const translations = {
  en: {
    "app.title": "Canonical Knowledge Model", "language.label": "Language",
    "theme.label": "Theme", "theme.dark": "Dark", "theme.light": "Light",
    "auth.subject": "Subject", "auth.password": "Password", "auth.connect": "Connect",
    "status.signIn": "Sign in with a development subject to begin.",
    "status.connected": "Connected. Select a scope or create a new one.",
    "status.connectFailed": "Could not connect: {detail}",
    "status.scopeCreated": "Created scope {name}.", "status.nodeRecorded": "Canonical node recorded.",
    "status.statementRecorded": "Recorded {statement} for {relation}.",
    "status.observationRecorded": "Appended an observation to {statement}.",
    "status.observationContextRequired": "Provide at least one observation-context value.",
    "scopes.title": "Scopes", "scopes.refresh": "Refresh scopes", "scopes.newName": "New scope name",
    "scopes.name": "Scope name", "scopes.navigation": "Model scopes", "scope.canonical": "Canonical scope",
    "scope.select": "Select a scope", "actions.create": "Create", "actions.search": "Search",
    "actions.refreshGraph": "Refresh graph", "actions.close": "Close", "summary.nodes": "Nodes", "summary.relations": "Relations",
    "summary.statements": "Statements", "empty.knowledge": "No canonical knowledge recorded yet.",
    "workspace.overview": "Overview", "workspace.knowledge": "Knowledge", "workspace.record": "Record knowledge",
    "knowledge.browseHelp": "Browse canonical knowledge in focused, paged lists.", "knowledge.searchHint": "Search the selected list",
    "knowledge.navigation": "Knowledge collections", "pagination.previous": "Previous", "pagination.next": "Next",
    "pagination.summary": "Page {page} of {total} · {count} items",
    "node.addCanonical": "Add canonical node", "node.add": "Add node", "endpoint.identity": "Identity", "endpoint.kind": "Kind",
    "state.record": "Record state", "state.recordHelp": "Record a contextual state owned by an entity or relation.",
    "state.ownerIdentity": "Owner identity", "state.ownerKind": "Owner kind", "state.ownerHint": "Choose MCB-01",
    "state.predicate": "State predicate", "state.predicateHint": "switchPosition", "state.value": "Value",
    "state.valueHint": "On, 22.5, or true",
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
    "relation.classifiedAs.help": "Assigns a general semantic classification to an entity or concept.",
    "relation.connectedTo.help": "Records a declared connection between two entities; it is symmetric, not automatically transitive.",
    "relation.contains.help": "Records structural or spatial containment from one entity to another.",
    "relation.dependsOn.help": "Records that one entity depends on another; only declared rules may compose it.",
    "relation.hasCapability.help": "Associates an entity with a capability; it does not mean that the capability is active.",
    "relation.hasState.help": "Associates an entity or relation with a contextual state.",
    "relation.hostedOn.help": "Records that one entity is hosted by another entity.",
    "relation.instanceOf.help": "Associates an entity with a specific product, model, or type concept.",
    "relation.playsRole.help": "Associates an entity with a contextual role concept.",
    "relation.supplies.help": "Records directed supply from one entity to another; it may be composed only by declared rules.",
    "inspector.statement": "Statement details", "inspector.supportingStatements": "Supporting Statements",
    "inspector.originalContext": "Original context", "inspector.observations": "Later observations",
    "inspector.evidence": "Evidence", "inspector.none": "None", "inspector.unspecified": "Unspecified",
    "inspector.noObservations": "No later observations recorded.", "context.observedAt": "Observed at",
    "context.validFrom": "Valid from", "context.validTo": "Valid to", "statement.predicate": "Predicate",
    "statement.subject": "Subject", "statement.object": "Object",
    "observation.add": "Append observation", "observation.append": "Append observation",
    "graph.title": "Knowledge graph", "graph.help": "Drag a node to arrange it, drag the background to pan, use the wheel to zoom, and select a node or relation for details.",
    "graph.reset": "Reset view", "graph.node": "Node details", "graph.incoming": "Incoming Relations",
    "graph.outgoing": "Outgoing Relations", "graph.empty": "No graph elements recorded yet.",
    "graph.explicitCount": "E: {count}", "graph.derivedCount": "D: {count}",
    "graph.state": "State: {value}", "graph.moreDetail": "+{count} detail",
    "graph.topology": "Topology", "graph.topologyElectrical": "Electrical supply", "graph.topologyDependency": "Dependencies",
    "graph.additionalRelation": "Additional relation",
    "graph.layout": "Layout", "graph.layoutLeftToRight": "Left to right", "graph.layoutTopToBottom": "Top to bottom",
    "graph.source": "Source device", "graph.sourceHint": "Choose MainSwitch-01", "graph.destination": "Destination device", "graph.destinationHint": "Choose ElectricBoiler-01", "graph.direction": "Direction", "graph.downstream": "Downstream", "graph.upstream": "Upstream",
    "graph.modeOverview": "Topology overview", "graph.modeSource": "Source-focused topology", "graph.modePath": "Selected path",
    "graph.modeOverviewHelp": "Choose a source device to focus this topology.",
    "graph.modeSourceHelp": "Now choose a destination device to show one exact path from the selected source.",
    "graph.modePathHelp": "Showing one ordered path between the selected source and destination.",
    "derivation.title": "Derived knowledge proposals", "derivation.noSource": "Choose a source device to find reviewable conclusions.",
    "derivation.ready": "Find conclusions supported by the selected semantic path.", "derivation.find": "Find proposals",
    "derivation.none": "No new derived conclusions are available.", "derivation.hops": "{count} hops",
    "derivation.evidence": "Evidence: {ids}", "derivation.accept": "Record as derived",
    "derivation.found": "Found {count} reviewable proposals.",
    "derivation.accepted": "Recorded derived statement {statement}.",
    "semantic.title": "Semantic reachability", "semantic.noSource": "Choose a source device to inspect auditable reachable devices.",
    "semantic.ready": "Inspect every semantically reachable device and its canonical witness.",
    "semantic.explore": "Explore reachability", "semantic.none": "No reachable devices found.",
    "semantic.hops": "{count} hops", "semantic.witness": "Witness: {path}",
    "semantic.showPath": "Show as path", "semantic.results": "{count} reachable devices from {source}.",
    "semantic.truncated": "Results are limited to {count} hops.",
    "powerImpact.title": "Power interruption impact", "powerImpact.noSource": "Choose an electrical source device to inspect what loses power.",
    "powerImpact.notElectrical": "Switch the topology to Electrical supply to analyze a power interruption.",
    "powerImpact.ready": "Assume the selected device is switched off and inspect downstream devices without another known supply path.",
    "powerImpact.analyze": "Analyze interruption", "powerImpact.none": "No downstream device is affected.",
    "powerImpact.results": "{count} devices lose power if {device} is switched off and no other known supply path remains.",
    "powerImpact.truncated": "The affected-device list was limited for this analysis.",
    "powerImpact.found": "Found {count} affected devices.",
  },
  hu: {
    "app.title": "Kanonikus tudásmodell", "language.label": "Nyelv",
    "theme.label": "Téma", "theme.dark": "Sötét", "theme.light": "Világos",
    "auth.subject": "Azonosító", "auth.password": "Jelszó", "auth.connect": "Csatlakozás",
    "status.signIn": "A kezdéshez jelentkezz be egy fejlesztői azonosítóval.",
    "status.connected": "Kapcsolódva. Válassz ki vagy hozz létre egy hatókört.",
    "status.connectFailed": "Nem sikerült kapcsolódni: {detail}",
    "status.scopeCreated": "A(z) {name} hatókör létrejött.", "status.nodeRecorded": "A kanonikus csomópont rögzítve.",
    "status.statementRecorded": "A(z) {statement} állítás rögzítve ehhez: {relation}.",
    "status.observationRecorded": "A megfigyelés hozzáadva ehhez: {statement}.",
    "status.observationContextRequired": "Adj meg legalább egy megfigyelési kontextusértéket.",
    "scopes.title": "Hatókörök", "scopes.refresh": "Hatókörök frissítése", "scopes.newName": "Új hatókör neve",
    "scopes.name": "Hatókör neve", "scopes.navigation": "Modellhatókörök", "scope.canonical": "Kanonikus hatókör",
    "scope.select": "Válassz egy hatókört", "actions.create": "Létrehozás", "actions.search": "Keresés",
    "actions.refreshGraph": "Gráf frissítése", "actions.close": "Bezárás", "summary.nodes": "Csomópontok", "summary.relations": "Kapcsolatok",
    "summary.statements": "Állítások", "empty.knowledge": "Még nincs rögzített kanonikus tudás.",
    "workspace.overview": "Áttekintés", "workspace.knowledge": "Tudás", "workspace.record": "Tudás rögzítése",
    "knowledge.browseHelp": "A kanonikus tudás fókuszált, lapozható listákban böngészhető.", "knowledge.searchHint": "Keresés a kiválasztott listában",
    "knowledge.navigation": "Tudásgyűjtemények", "pagination.previous": "Előző", "pagination.next": "Következő",
    "pagination.summary": "{page}. oldal / {total} · {count} elem",
    "node.addCanonical": "Kanonikus csomópont hozzáadása", "node.add": "Csomópont hozzáadása", "endpoint.identity": "Azonosító", "endpoint.kind": "Típus",
    "state.record": "Állapot rögzítése", "state.recordHelp": "Entitás vagy kapcsolat tulajdonoshoz kötött kontextuális állapot rögzítése.",
    "state.ownerIdentity": "Tulajdonos azonosítója", "state.ownerKind": "Tulajdonos típusa", "state.ownerHint": "Válaszd ki: MCB-01",
    "state.predicate": "Állapot predikátuma", "state.predicateHint": "kapcsolóÁllás", "state.value": "Érték",
    "state.valueHint": "On, 22.5 vagy true",
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
    "relation.classifiedAs.help": "Egy entitáshoz vagy fogalomhoz általános szemantikai besorolást kapcsol.",
    "relation.connectedTo.help": "Két entitás deklarált kapcsolatát rögzíti; szimmetrikus, de nem automatikusan tranzitív.",
    "relation.contains.help": "Egy entitás szerkezeti vagy térbeli tartalmazását rögzíti egy másik entitás felé.",
    "relation.dependsOn.help": "Azt rögzíti, hogy az egyik entitás függ a másiktól; összefűzését csak deklarált szabály engedheti.",
    "relation.hasCapability.help": "Entitást kapcsol képességhez; nem jelenti azt, hogy a képesség éppen aktív.",
    "relation.hasState.help": "Entitást vagy kapcsolatot kapcsol kontextuális állapothoz.",
    "relation.hostedOn.help": "Azt rögzíti, hogy egy entitás egy másik entitáson fut vagy van hostolva.",
    "relation.instanceOf.help": "Egy entitást konkrét termék-, modell- vagy típusfogalomhoz kapcsol.",
    "relation.playsRole.help": "Egy entitást kontextuális szerepfogalomhoz kapcsol.",
    "relation.supplies.help": "Irányított ellátást rögzít két entitás között; összefűzése csak deklarált szabály alapján lehetséges.",
    "inspector.statement": "Állítás részletei", "inspector.supportingStatements": "Alátámasztó állítások",
    "inspector.originalContext": "Eredeti kontextus", "inspector.observations": "Későbbi megfigyelések",
    "inspector.evidence": "Bizonyíték", "inspector.none": "Nincs", "inspector.unspecified": "Nincs megadva",
    "inspector.noObservations": "Nincs rögzített későbbi megfigyelés.", "context.observedAt": "Megfigyelés ideje",
    "context.validFrom": "Érvényesség kezdete", "context.validTo": "Érvényesség vége", "statement.predicate": "Predikátum",
    "statement.subject": "Alany", "statement.object": "Tárgy",
    "observation.add": "Megfigyelés hozzáadása", "observation.append": "Megfigyelés hozzáadása",
    "graph.title": "Tudásgráf", "graph.help": "Az elrendezéshez húzz egy csomópontot, a pásztázáshoz a hátteret, a nagyításhoz használd a görgőt, részletekhez pedig válassz egy csomópontot vagy kapcsolatot.",
    "graph.reset": "Nézet alaphelyzetbe", "graph.node": "Csomópont részletei", "graph.incoming": "Bejövő kapcsolatok",
    "graph.outgoing": "Kimenő kapcsolatok", "graph.empty": "Még nincs megjeleníthető gráfelem.",
    "graph.explicitCount": "E: {count}", "graph.derivedCount": "Sz: {count}",
    "graph.state": "Állapot: {value}", "graph.moreDetail": "+{count} részlet",
    "graph.topology": "Topológia", "graph.topologyElectrical": "Elektromos ellátás", "graph.topologyDependency": "Függőségek",
    "graph.additionalRelation": "További kapcsolat",
    "graph.layout": "Elrendezés", "graph.layoutLeftToRight": "Balról jobbra", "graph.layoutTopToBottom": "Fentről lefelé",
    "graph.source": "Forrás eszköz", "graph.sourceHint": "Válaszd ki: MainSwitch-01", "graph.destination": "Cél eszköz", "graph.destinationHint": "Válaszd ki: ElectricBoiler-01", "graph.direction": "Irány", "graph.downstream": "Leszálló", "graph.upstream": "Felszálló",
    "graph.modeOverview": "Topológiai áttekintés", "graph.modeSource": "Forrásra fókuszált topológia", "graph.modePath": "Kiválasztott útvonal",
    "graph.modeOverviewHelp": "Válassz ki egy forrás eszközt a topológia szűkítéséhez.",
    "graph.modeSourceHelp": "Most válassz cél eszközt, hogy a kiválasztott forrás és cél közötti pontos út jelenjen meg.",
    "graph.modePathHelp": "A kiválasztott forrás és cél közötti egy rendezett útvonal látható.",
    "derivation.title": "Származtatott tudásjavaslatok", "derivation.noSource": "Válassz forrás eszközt az ellenőrizhető következtetések kereséséhez.",
    "derivation.ready": "Keress a kiválasztott szemantikus út által alátámasztott következtetéseket.", "derivation.find": "Javaslatok keresése",
    "derivation.none": "Nincs új rögzíthető származtatott következtetés.", "derivation.hops": "{count} lépés",
    "derivation.evidence": "Bizonyíték: {ids}", "derivation.accept": "Rögzítés származtatottként",
    "derivation.found": "{count} ellenőrizhető javaslat található.",
    "derivation.accepted": "A(z) {statement} származtatott állítás rögzítve.",
    "semantic.title": "Szemantikus elérhetőség", "semantic.noSource": "Válassz forrás eszközt az auditálható elérhető eszközök vizsgálatához.",
    "semantic.ready": "Vizsgáld meg az összes szemantikusan elérhető eszközt és a kanonikus bizonyító útját.",
    "semantic.explore": "Elérhetőség vizsgálata", "semantic.none": "Nem található elérhető eszköz.",
    "semantic.hops": "{count} lépés", "semantic.witness": "Bizonyító út: {path}",
    "semantic.showPath": "Megjelenítés útként", "semantic.results": "{source} forrásból {count} elérhető eszköz.",
    "semantic.truncated": "Az eredmények legfeljebb {count} lépésig látszanak.",
    "powerImpact.title": "Áramkimaradás hatása", "powerImpact.noSource": "Válassz elektromos forráseszközt annak vizsgálatához, mi marad áram nélkül.",
    "powerImpact.notElectrical": "Áramkimaradás elemzéséhez válts Elektromos ellátás topológiára.",
    "powerImpact.ready": "A kiválasztott eszközt lekapcsoltnak feltételezve vizsgáld meg a más ismert betápút nélkül maradó downstream eszközöket.",
    "powerImpact.analyze": "Lekapcsolás elemzése", "powerImpact.none": "Nincs érintett downstream eszköz.",
    "powerImpact.results": "{device} lekapcsolásakor {count} eszköz marad áram nélkül, mert nincs más ismert betápútja.",
    "powerImpact.truncated": "Az érintett eszközök listája ehhez az elemzéshez korlátozva lett.",
    "powerImpact.found": "{count} érintett eszköz található.",
  }
};

const state = {
  authorization: null, scopeId: null, relationTypes: [], endpointKinds: new Map(), endpointCatalog: [], knowledge: null,
  graphProfile: "ELECTRICAL_SUPPLY", graphLayout: "LEFT_TO_RIGHT", graphSource: null, graphDestination: null, graphDestinations: [], graphDirection: "DOWNSTREAM", graphTopology: null,
  derivationProposals: [], derivationQueried: false,
  semanticTraversal: null,
  powerImpact: null,
  workbenchView: "overview", knowledgeList: "nodes", knowledgeListQuery: "", knowledgeListPage: 0, knowledgePage: null, knowledgeListRequest: 0,
  language: localStorage.getItem("persiqa.language") || navigator.language?.slice(0, 2) || "en",
  theme: localStorage.getItem("persiqa.theme") || "dark"
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
const graphMode = document.querySelector("#graph-mode");
const graphState = {
  x: 0, y: 0, scale: 1, dragging: null, nodeDragging: null,
  suppressClickNodeId: null, updateEdges: null
};
const svgNamespace = "http://www.w3.org/2000/svg";

if (!translations[state.language]) state.language = "en";
if (!["dark", "light"].includes(state.theme)) state.theme = "dark";

const recording = createRecordingController({
  findEndpointCandidates,
  kindLabel,
  loadKnowledge,
  relationTypeLabel,
  report,
  request,
  showStatus,
  state,
  translate: t
});

const semantics = createSemanticController({
  graphProfile,
  loadKnowledge,
  relationTypeLabel,
  report,
  request,
  showPath: showGraphPath,
  showStatus,
  state,
  translate: t
});

const powerImpact = createPowerImpactController({
  graphProfile,
  report,
  request,
  showPath: showGraphPath,
  showStatus,
  state,
  translate: t
});

function t(key, values = {}) {
  return translations[state.language][key]?.replace(/\{(\w+)\}/g, (_, name) => values[name] ?? `{${name}}`) || key;
}

function kindLabel(kind) { return t(`kind.${kind}`); }

function relationTypeLabel(typeId) {
  const key = `relation.${typeId}`;
  return translations[state.language][key] || typeId;
}

function graphProfile() {
  return state.graphProfile === "DEPENDENCY"
    ? { relationType: "dependsOn", direction: "DOWNSTREAM" }
    : { relationType: "supplies", direction: "DOWNSTREAM" };
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
  updateGraphMode();
  renderKnowledgeList();
  semantics.renderDerivationProposals();
  semantics.renderSemanticTraversal();
  powerImpact.render();
}

function applyTheme() {
  document.documentElement.dataset.theme = state.theme;
  document.querySelector("#theme").value = state.theme;
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
  recording.renderRelationTypes();
}

function renderKnowledge(knowledge, preserveDerivationProposals = false) {
  state.knowledge = knowledge;
  state.graphTopology = null;
  if (!preserveDerivationProposals) semantics.clearDerivationProposals();
  semantics.clearSemanticTraversal();
  powerImpact.clear();
  renderEndpointOptions(knowledge);
  renderGraphSources(knowledge);
  document.querySelector("#scope-title").textContent = knowledge.scope.name;
  const summary = document.querySelector("#graph-summary");
  summary.replaceChildren();
  [["summary.nodes", knowledge.nodeCount], ["summary.relations", knowledge.relationCount], ["summary.statements", knowledge.statementCount]]
    .forEach(([label, count]) => {
      const card = document.createElement("div");
      const number = document.createElement("strong");
      number.textContent = count;
      card.append(number, document.createTextNode(t(label)));
      summary.append(card);
    });
  renderKnowledgeGraph(knowledge);
}

function activateWorkbenchView(view) {
  state.workbenchView = view;
  document.querySelectorAll(".workbench-view").forEach((element) => {
    element.classList.toggle("hidden", element.id !== `${view}-view`);
  });
  document.querySelectorAll(".workbench-menu-item").forEach((button) => {
    const active = button.dataset.workbenchView === view;
    button.classList.toggle("active", active);
    button.setAttribute("aria-current", active ? "page" : "false");
  });
  if (view === "knowledge" && state.scopeId) loadKnowledgeList().catch(report);
}

function knowledgeListConfiguration() {
  return {
    nodes: {
      endpoint: "nodes",
      render: (node) => textItem(node.id, kindLabel(node.kind))
    },
    relations: {
      endpoint: "relations",
      render: (relation) => selectableItem(
        relation.id,
        `${relation.source.id} —${relationTypeLabel(relation.type.id)}→ ${relation.target.id}`,
        () => showRelationDetails(relation))
    },
    statements: {
      endpoint: "statements",
      render: (statement) => selectableItem(
        statement.id,
        `${t(`knowledge.${statement.knowledgeKind.toLowerCase()}`)} · ${relationTypeLabel(statement.predicate)}`,
        () => showStatementDetails(statement.id))
    }
  }[state.knowledgeList];
}

function renderKnowledgeList() {
  const page = state.knowledgePage;
  const list = document.querySelector("#knowledge-list");
  const previous = document.querySelector("#knowledge-previous-page");
  const next = document.querySelector("#knowledge-next-page");
  const summary = document.querySelector("#knowledge-page-summary");
  if (!page) {
    list.replaceChildren();
    previous.disabled = true;
    next.disabled = true;
    summary.textContent = "";
    return;
  }
  renderItems(list, page.content, knowledgeListConfiguration().render);
  previous.disabled = page.page === 0;
  next.disabled = page.page + 1 >= page.totalPages;
  summary.textContent = t("pagination.summary", {
    page: page.totalElements === 0 ? 0 : page.page + 1,
    total: page.totalPages,
    count: page.totalElements
  });
}

async function loadKnowledgeList() {
  if (!state.scopeId) return;
  const requestNumber = ++state.knowledgeListRequest;
  const query = new URLSearchParams({ page: String(state.knowledgeListPage), size: "25" });
  if (state.knowledgeListQuery) query.set("q", state.knowledgeListQuery);
  const page = await request(
    `/api/scopes/${state.scopeId}/${knowledgeListConfiguration().endpoint}?${query}`);
  if (requestNumber !== state.knowledgeListRequest) return;
  state.knowledgePage = page;
  if (page.page >= page.totalPages && page.totalPages > 0) {
    state.knowledgeListPage = page.totalPages - 1;
    return loadKnowledgeList();
  }
  state.knowledgeListPage = page.page;
  renderKnowledgeList();
}

function renderGraphSources(knowledge) {
  const options = document.querySelector("#graph-source-options");
  options.replaceChildren();
  state.endpointCatalog
    .filter((node) => node.kind === "ENTITY")
    .forEach((node) => {
      const option = document.createElement("option");
      option.value = node.id;
      options.append(option);
    });
  document.querySelector("#graph-source").value = state.graphSource || "";
}

function svgElement(name, attributes = {}) {
  const element = document.createElementNS(svgNamespace, name);
  Object.entries(attributes).forEach(([key, value]) => element.setAttribute(key, value));
  return element;
}

function relationColor(typeId) {
  const palette = ["#c25f05", "#c98a4d", "#d6ad38", "#55a889", "#bc6d6d", "#aa7b57"];
  const value = [...typeId].reduce((total, character) => total + character.charCodeAt(0), 0);
  return palette[value % palette.length];
}

function graphNodes(knowledge, topologyRelations, visibleNodeIds = null) {
  const nodes = new Map(knowledge.nodes
    .filter((node) => !visibleNodeIds || visibleNodeIds.has(node.id))
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

function reachableSupplies(anchorId, relations, direction) {
  const visibleNodeIds = new Set([anchorId]);
  const pending = [anchorId];
  while (pending.length > 0) {
    const sourceId = pending.shift();
    relations.filter((relation) => (direction === "DOWNSTREAM" ? relation.source.id : relation.target.id) === sourceId).forEach((relation) => {
      const nextId = direction === "DOWNSTREAM" ? relation.target.id : relation.source.id;
      if (!visibleNodeIds.has(nextId)) {
        visibleNodeIds.add(nextId);
        pending.push(nextId);
      }
    });
  }
  return visibleNodeIds;
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

function suppliesRefinementProjection(relations) {
  const supplies = relations.filter((relation) => relation.type.id === "supplies");
  const overviewRelationIds = new Set();
  const detailRelationIds = new Set();
  const detailNodeIds = new Set();
  const detailCounts = new Map();
  supplies.forEach((coarse) => {
    supplies.filter((first) => first.source.id === coarse.source.id).forEach((first) => {
      supplies.filter((second) => second.source.id === first.target.id
        && second.target.id === coarse.target.id).forEach((second) => {
        overviewRelationIds.add(coarse.id);
        detailRelationIds.add(first.id);
        detailRelationIds.add(second.id);
        detailNodeIds.add(first.target.id);
        detailCounts.set(coarse.id, (detailCounts.get(coarse.id) || 0) + 1);
      });
    });
  });
  return { overviewRelationIds, detailRelationIds, detailNodeIds, detailCounts };
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

function graphPositions(nodes, depths = null) {
  if (depths) {
    const levels = new Map();
    nodes.forEach((node) => {
      const depth = depths.get(node.id) ?? 0;
      const level = levels.get(depth) || [];
      level.push(node);
      levels.set(depth, level);
    });
    return new Map(nodes.map((node) => {
      const depth = depths.get(node.id) ?? 0;
      const level = levels.get(depth);
      const index = level.indexOf(node);
      return [node.id, { x: ((index + 1) * 1000) / (level.length + 1), y: 80 + (depth * 105) }];
    }));
  }
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

function topologyTreeLayout(nodes, relations, projection, orientation) {
  const depths = projection.depths;
  const children = new Map(nodes.map((node) => [node.id, []]));
  const primaryRelationIds = new Set();
  const primaryParents = new Set();

  // A projection may be a DAG. For the layout we make its deterministic spanning forest
  // explicit: each node gets the closest eligible predecessor as its visual parent.
  const parentCandidates = [...relations]
    .filter((relation) => depths.has(relation.source.id) && depths.has(relation.target.id))
    .filter((relation) => (depths.get(relation.source.id) ?? 0) < (depths.get(relation.target.id) ?? 0))
    .sort((left, right) => {
      const target = left.target.id.localeCompare(right.target.id);
      if (target !== 0) return target;
      const leftDistance = depths.get(left.target.id) - depths.get(left.source.id);
      const rightDistance = depths.get(right.target.id) - depths.get(right.source.id);
      return leftDistance - rightDistance || left.source.id.localeCompare(right.source.id)
        || left.id.localeCompare(right.id);
    });
  parentCandidates.forEach((relation) => {
    if (primaryParents.has(relation.target.id)) return;
    primaryParents.add(relation.target.id);
    primaryRelationIds.add(relation.id);
    children.get(relation.source.id).push(relation.target.id);
  });
  children.forEach((childIds) => childIds.sort((left, right) => left.localeCompare(right)));

  const roots = nodes
    .filter((node) => !primaryParents.has(node.id))
    .sort((left, right) => (depths.get(left.id) - depths.get(right.id)) || left.id.localeCompare(right.id));
  const positions = new Map();
  const placed = new Set();
  let nextLeaf = 0;
  const place = (nodeId) => {
    if (placed.has(nodeId)) return positions.get(nodeId).row;
    placed.add(nodeId);
    const childRows = children.get(nodeId)
      .filter((childId) => !placed.has(childId))
      .map(place);
    const row = childRows.length === 0
      ? nextLeaf++
      : (childRows[0] + childRows[childRows.length - 1]) / 2;
    positions.set(nodeId, { row });
    return row;
  };
  roots.forEach((node) => place(node.id));
  nodes.filter((node) => !placed.has(node.id)).sort((left, right) => left.id.localeCompare(right.id))
    .forEach((node) => place(node.id));

  const maximumDepth = Math.max(...nodes.map((node) => depths.get(node.id) ?? 0));
  const rowCount = Math.max(1, nextLeaf);
  const vertical = orientation === "TOP_TO_BOTTOM";
  const canvasWidth = vertical ? Math.max(1000, 180 + ((rowCount - 1) * 180)) : 1000;
  const canvasHeight = vertical ? 1000 : Math.max(800, 180 + ((rowCount - 1) * 150));
  positions.forEach((position, nodeId) => {
    positions.set(nodeId, {
      x: vertical
        ? 90 + (position.row * 180)
        : 100 + ((800 * (depths.get(nodeId) ?? 0)) / Math.max(1, maximumDepth)),
      y: vertical
        ? 100 + ((800 * (depths.get(nodeId) ?? 0)) / Math.max(1, maximumDepth))
        : 90 + (position.row * 150)
    });
  });
  return { positions, primaryRelationIds, canvasWidth, canvasHeight };
}

function configureGraphCanvas(canvasWidth, canvasHeight) {
  graphSvg.setAttribute("viewBox", `0 0 ${canvasWidth} ${canvasHeight}`);
}

function orthogonalEdge(source, target, busY = null) {
  if (source.x === target.x) {
    return {
      points: `${source.x},${source.y} ${target.x},${target.y}`,
      labelX: source.x,
      labelY: (source.y + target.y) / 2
    };
  }
  const middleY = busY ?? (source.y + target.y) / 2;
  return {
    points: `${source.x},${source.y} ${source.x},${middleY} ${target.x},${middleY} ${target.x},${target.y}`,
    labelX: (source.x + target.x) / 2,
    labelY: middleY
  };
}

function straightEdge(source, target) {
  return {
    points: `${source.x},${source.y} ${target.x},${target.y}`,
    labelX: (source.x + target.x) / 2,
    labelY: (source.y + target.y) / 2
  };
}

function nodeLabelLines(identity) {
  const words = identity
    .replace(/([a-z])([A-Z])/g, "$1 $2")
    .replace(/-(?=\d)/g, " ")
    .split(/[\s_-]+/)
    .filter(Boolean);
  if (words.length < 3) return [words.join("-")];
  const midpoint = Math.ceil(words.length / 2);
  return [words.slice(0, midpoint).join(" "), words.slice(midpoint).join("-")];
}

function appendNodeLabel(label, identity) {
  const lines = nodeLabelLines(identity);
  lines.forEach((line, index) => {
    const part = svgElement("tspan", { x: "0", dy: index === 0 ? -6 * (lines.length - 1) : "12" });
    part.textContent = line;
    label.append(part);
  });
  return lines.length;
}

function supplyTree(anchorId, relations, direction) {
  const depths = new Map([[anchorId, 0]]);
  const pending = [anchorId];
  while (pending.length > 0) {
    const current = pending.shift();
    relations.filter((relation) => (direction === "DOWNSTREAM" ? relation.source.id : relation.target.id) === current)
      .forEach((relation) => {
        const next = direction === "DOWNSTREAM" ? relation.target.id : relation.source.id;
        if (!depths.has(next)) {
          depths.set(next, depths.get(current) + 1);
          pending.push(next);
        }
      });
  }
  const terminals = new Set([...depths.keys()].filter((nodeId) => !relations.some((relation) =>
    (direction === "DOWNSTREAM" ? relation.source.id : relation.target.id) === nodeId)));
  return { depths, terminals };
}

function updateGraphTransform() {
  graphViewport.setAttribute("transform", `translate(${graphState.x} ${graphState.y}) scale(${graphState.scale})`);
  graphSvg.classList.remove("semantic-overview", "semantic-detail");
}

function graphModeState() {
  if (state.graphSource && state.graphDestination) return "path";
  if (state.graphSource) return "source";
  return "overview";
}

function updateGraphMode() {
  const mode = graphModeState();
  graphMode.replaceChildren();
  const label = document.createElement("strong");
  label.textContent = t(`graph.mode${mode.charAt(0).toUpperCase()}${mode.slice(1)}`);
  graphMode.append(label, document.createTextNode(` · ${t(`graph.mode${mode.charAt(0).toUpperCase()}${mode.slice(1)}Help`)}`));
  graphMode.closest(".topology-panel").classList.toggle("graph-path-mode", mode === "path");
}

function resetGraphView() {
  graphState.x = 0;
  graphState.y = 0;
  graphState.scale = 1;
  updateGraphTransform();
}

function renderKnowledgeGraph(knowledge) {
  updateGraphMode();
  graphViewport.replaceChildren();
  graphLegend.replaceChildren();
  const serverTopology = state.graphTopology;
  const supplyRelations = knowledge.relations.filter((relation) => relation.type.id === "supplies");
  const projection = serverTopology
    ? {
      depths: new Map(serverTopology.nodes.map((entry) => [entry.node.id, entry.depth])),
      terminals: new Set(serverTopology.nodes.filter((entry) => entry.terminal).map((entry) => entry.node.id))
    }
    : null;
  const visibleNodeIds = projection ? new Set(projection.depths.keys()) : null;
  const topologyRelations = serverTopology
    ? serverTopology.edges.map((edge, index) => ({
      id: edge.virtual ? `virtual-${index}` : edge.supportingRelationIds[0],
      type: { id: edge.relationType }, source: edge.source, target: edge.target,
      virtual: edge.virtual, hiddenNodeCount: edge.hiddenNodeCount,
      supportingRelationIds: edge.supportingRelationIds
    }))
    : knowledge.relations.filter((relation) => relation.type.id !== "hasState");
  const states = statesByEntity(knowledge.relations);
  const refinement = serverTopology
    ? { overviewRelationIds: new Set(), detailRelationIds: new Set(), detailNodeIds: new Set(), detailCounts: new Map() }
    : suppliesRefinementProjection(topologyRelations);
  const nodes = serverTopology
    ? serverTopology.nodes.map((entry) => entry.node)
    : graphNodes(knowledge, topologyRelations, visibleNodeIds);
  if (nodes.length === 0) {
    const message = svgElement("text", { x: "500", y: "300", "text-anchor": "middle", fill: "#c3b9af" });
    message.textContent = t("graph.empty");
    graphViewport.append(message);
    resetGraphView();
    return;
  }
  const layout = serverTopology
    ? topologyTreeLayout(nodes, topologyRelations, projection, state.graphLayout)
    : { positions: graphPositions(nodes, projection?.depths) };
  configureGraphCanvas(layout.canvasWidth ?? 1000, layout.canvasHeight ?? 800);
  const positions = layout.positions;
  const edgeElements = new Map();
  const relationTypes = [...new Set(topologyRelations.map((relation) => relation.type.id))].sort();
  relationTypes.forEach((typeId) => {
    const item = document.createElement("span");
    const marker = document.createElement("i");
    marker.style.backgroundColor = relationColor(typeId);
    item.append(marker, document.createTextNode(relationTypeLabel(typeId)));
    graphLegend.append(item);
  });
  if (serverTopology && topologyRelations.some((relation) => !layout.primaryRelationIds.has(relation.id))) {
    const item = document.createElement("span");
    const marker = document.createElement("i");
    marker.className = "graph-secondary-marker";
    item.append(marker, document.createTextNode(t("graph.additionalRelation")));
    graphLegend.append(item);
  }
  topologyRelations.forEach((relation) => {
    const source = positions.get(relation.source.id);
    const target = positions.get(relation.target.id);
    const geometry = serverTopology
      ? straightEdge(source, target)
      : orthogonalEdge(source, target);
    const resolutionClass = refinement.overviewRelationIds.has(relation.id)
      ? " graph-overview-relation"
      : refinement.detailRelationIds.has(relation.id) ? " graph-detail-relation" : "";
    const treeClass = projection && !serverTopology
      ? ` graph-tree-edge level-${projection.depths.get(relation.source.id)}` : "";
    const secondaryClass = serverTopology && !layout.primaryRelationIds.has(relation.id)
      ? " graph-secondary-edge" : "";
    const edge = svgElement("polyline", {
      class: `graph-edge${resolutionClass}${treeClass}${secondaryClass}`, points: geometry.points,
      stroke: relationColor(relation.type.id)
    });
    edge.addEventListener("click", (event) => {
      event.stopPropagation();
      if (relation.virtual) showTopologyEdgeDetails(relation); else showRelationDetails(relation);
    });
    graphViewport.append(edge);
    const label = serverTopology ? null : svgElement("text", {
      class: `graph-edge-label${resolutionClass}`, x: geometry.labelX, y: geometry.labelY - 7
    });
    if (label) {
      label.textContent = relationTypeLabel(relation.type.id);
      graphViewport.append(label);
    }
    if (refinement.overviewRelationIds.has(relation.id)) {
      const hint = svgElement("text", {
        class: "graph-edge-resolution-hint", x: geometry.labelX, y: geometry.labelY + 12
      });
      hint.textContent = t("graph.moreDetail", { count: refinement.detailCounts.get(relation.id) });
      graphViewport.append(hint);
    }
    if (relation.virtual) {
      const hint = svgElement("text", {
        class: "graph-edge-resolution-hint", x: geometry.labelX, y: geometry.labelY + 12
      });
      hint.textContent = t("graph.moreDetail", { count: relation.hiddenNodeCount });
      graphViewport.append(hint);
    }
    const detail = serverTopology ? null : svgElement("text", {
      class: `graph-edge-label graph-edge-detail${resolutionClass}`, x: geometry.labelX, y: geometry.labelY + 8
    });
    if (detail) {
      const counts = statementCountsForRelation(relation, knowledge.statements);
      detail.textContent = `${t("graph.explicitCount", { count: counts.explicit })} · ${t("graph.derivedCount", { count: counts.derived })}`;
      graphViewport.append(detail);
    }
    edgeElements.set(relation.id, { edge, label, detail });
  });
  if (projection && !serverTopology) {
    projection.terminals.forEach((terminalId) => {
      const depth = projection.depths.get(terminalId);
      if (depth < 2) return;
      const source = positions.get(state.graphSource);
      const target = positions.get(terminalId);
      const edge = svgElement("line", { class: "graph-edge graph-overview-virtual", x1: source.x, y1: source.y, x2: target.x, y2: target.y, stroke: "#fbbf24" });
      const hint = svgElement("text", { class: "graph-edge-resolution-hint graph-overview-virtual", x: (source.x + target.x) / 2, y: (source.y + target.y) / 2 - 8 });
      hint.textContent = t("graph.moreDetail", { count: depth - 1 });
      graphViewport.append(edge, hint);
    });
  }
  graphState.updateEdges = () => {
    topologyRelations.forEach((relation) => {
      const source = positions.get(relation.source.id);
      const target = positions.get(relation.target.id);
      const geometry = serverTopology
        ? straightEdge(source, target)
        : orthogonalEdge(source, target);
      const elements = edgeElements.get(relation.id);
      elements.edge.setAttribute("points", geometry.points);
      if (elements.label) {
        elements.label.setAttribute("x", geometry.labelX);
        elements.label.setAttribute("y", geometry.labelY - 7);
      }
      if (elements.detail) {
        elements.detail.setAttribute("x", geometry.labelX);
        elements.detail.setAttribute("y", geometry.labelY + 8);
      }
    });
  };
  nodes.forEach((node) => {
    const position = positions.get(node.id);
    const resolutionClass = refinement.detailNodeIds.has(node.id) ? " graph-detail-node" : "";
    const treeClass = projection && !serverTopology && node.id !== state.graphSource && !projection.terminals.has(node.id)
      ? ` graph-tree-intermediate level-${projection.depths.get(node.id)}` : "";
    const pathClass = serverTopology ? " graph-path-node" : "";
    const topologyRoleClass = serverTopology
      ? `${serverTopology.nodes.find((entry) => entry.node.id === node.id)?.anchor ? " graph-anchor-node" : ""}${projection.terminals.has(node.id) ? " graph-terminal-node" : ""}`
      : "";
    const group = svgElement("g", { class: `graph-node${resolutionClass}${treeClass}${pathClass}${topologyRoleClass}`, transform: `translate(${position.x} ${position.y})`, tabindex: "0", role: "button" });
    const circle = svgElement("circle", { r: serverTopology ? "50" : "38" });
    const label = svgElement("text", { y: "-3" });
    const labelLines = serverTopology ? appendNodeLabel(label, node.id) : 1;
    if (!serverTopology) label.textContent = node.id;
    const kind = svgElement("text", { class: "node-kind-label", y: labelLines > 1 ? "20" : "15" });
    kind.textContent = kindLabel(node.kind);
    const counts = statementCountsForNode(node.id, knowledge.statements);
    const knowledgeLabel = svgElement("text", { class: "node-knowledge-label", y: "34" });
    knowledgeLabel.textContent = `${t("graph.explicitCount", { count: counts.explicit })} · ${t("graph.derivedCount", { count: counts.derived })}`;
    const stateLabel = svgElement("text", { class: "node-state-label", y: "39" });
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
    const title = svgElement("title");
    title.textContent = node.id;
    group.append(title, circle, label, kind);
    if (!serverTopology) group.append(knowledgeLabel);
    if (stateIds) group.append(stateLabel);
    graphViewport.append(group);
  });
  updateGraphTransform();
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
  recording.renderEndpointOptions(knowledge);
}

async function findEndpointCandidates(query) {
  if (!state.scopeId) return [];
  const parameters = new URLSearchParams({ page: "0", size: "25" });
  if (query) parameters.set("q", query);
  const [nodes, relations] = await Promise.all([
    request(`/api/scopes/${state.scopeId}/nodes?${parameters}`),
    request(`/api/scopes/${state.scopeId}/relations?${parameters}`)
  ]);
  return [...nodes.content, ...relations.content.map((relation) => ({ id: relation.id, kind: "RELATION" }))];
}

async function loadKnowledge(preserveDerivationProposals = false) {
  if (!state.scopeId) return;
  const [summary, endpointCatalog] = await Promise.all([
    request(`/api/scopes/${state.scopeId}/knowledge/summary`),
    findEndpointCandidates("")
  ]);
  state.endpointCatalog = endpointCatalog;
  renderKnowledge(
    {
      ...summary,
      nodes: endpointCatalog.filter((entry) => entry.kind !== "RELATION"),
      relations: endpointCatalog
        .filter((entry) => entry.kind === "RELATION")
        .map((entry) => ({ id: entry.id })),
      statements: []
    },
    preserveDerivationProposals);
  if (state.graphSource && state.graphDestination) {
    await loadTopologyPath();
  } else if (state.graphSource) {
    await loadAnchoredTopology();
  } else {
    await loadInitialTopology();
  }
  if (state.workbenchView === "knowledge") await loadKnowledgeList();
}

async function loadInitialTopology() {
  if (!state.scopeId || state.graphSource) return;
  const query = new URLSearchParams({ profile: state.graphProfile, direction: state.graphDirection });
  state.graphTopology = await request(`/api/scopes/${state.scopeId}/topology/initial?${query}`);
  renderKnowledgeGraph(state.knowledge);
}

async function loadAnchoredTopology() {
  if (!state.scopeId || !state.graphSource || state.graphDestination) return;
  const query = new URLSearchParams({
    anchor: state.graphSource,
    direction: state.graphDirection,
    relationType: graphProfile().relationType,
    detailLevel: "DETAIL"
  });
  state.graphTopology = await request(`/api/scopes/${state.scopeId}/topology?${query}`);
  renderKnowledgeGraph(state.knowledge);
}

async function loadTopologyPath() {
  if (!state.scopeId || !state.graphSource || !state.graphDestination) return;
  const query = new URLSearchParams({
    source: state.graphSource, destination: state.graphDestination,
    direction: state.graphDirection, relationType: graphProfile().relationType
  });
  state.graphTopology = await request(`/api/scopes/${state.scopeId}/topology/path?${query}`);
  renderKnowledgeGraph(state.knowledge);
}

async function showGraphPath(destinationId) {
  if (!state.graphDestinations.some((node) => node.id === destinationId)) {
    throw new Error(t("semantic.none"));
  }
  state.graphDestination = destinationId;
  document.querySelector("#graph-destination").value = destinationId;
  updateGraphMode();
  await loadTopologyPath();
}

function showTopologyEdgeDetails(relation) {
  showInspector(`${relationTypeLabel(relation.type.id)}: ${t("graph.moreDetail", { count: relation.hiddenNodeCount })}`);
  appendMetadata(inspectorContent, [
    [t("statement.subject"), relation.source.id], [t("statement.object"), relation.target.id],
    [t("inspector.supportingStatements"), relation.supportingRelationIds.join(", ")]
  ]);
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

function observationInput(labelKey, name, type = "text") {
  const label = document.createElement("label");
  const labelText = document.createElement("span");
  labelText.textContent = t(labelKey);
  const input = document.createElement("input");
  input.name = name;
  input.type = type;
  if (name === "confidence") {
    input.min = "0";
    input.max = "1";
    input.step = "0.01";
  }
  label.append(labelText, input);
  return label;
}

function observationForm(statementId) {
  const form = document.createElement("form");
  form.className = "observation-form";
  const heading = document.createElement("h4");
  heading.textContent = t("observation.add");
  const fields = document.createElement("div");
  fields.className = "observation-fields";
  fields.append(
    observationInput("context.provenance", "provenance"),
    observationInput("context.confidence", "confidence", "number"),
    observationInput("context.observedAt", "observedAt", "datetime-local"),
    observationInput("context.validFrom", "validFrom", "datetime-local"),
    observationInput("context.validTo", "validTo", "datetime-local"),
    observationInput("context.scenario", "scenario"));
  const submit = document.createElement("button");
  submit.type = "submit";
  submit.textContent = t("observation.append");
  form.addEventListener("submit", async (event) => {
    event.preventDefault();
    try {
      const context = contextFrom(new FormData(form));
      if (!context) throw new Error(t("status.observationContextRequired"));
      submit.disabled = true;
      await request(
        `/api/scopes/${state.scopeId}/statements/${encodeURIComponent(statementId)}/observations`,
        { method: "POST", body: JSON.stringify(context) });
      showStatus(t("status.observationRecorded", { statement: statementId }));
      await showStatementDetails(statementId);
    } catch (error) {
      report(error);
      submit.disabled = false;
    }
  });
  form.append(heading, fields, submit);
  return form;
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
    inspectorContent.append(observationForm(statement.id));
  } catch (error) { report(error); }
}

async function selectScope(scopeId) {
  state.scopeId = scopeId;
  state.knowledgeListPage = 0;
  state.knowledgeListQuery = "";
  state.knowledgePage = null;
  document.querySelector("#knowledge-search").value = "";
  activateWorkbenchView("overview");
  workbench.classList.remove("hidden");
  await Promise.all([loadScopes(), loadKnowledge()]);
}

function populateNodeKinds() {
  recording.populateNodeKinds();
}

function instantFrom(form, field) {
  const value = form.get(field);
  return value ? new Date(value).toISOString() : null;
}

function contextFrom(form) {
  const confidence = form.get("confidence");
  const context = {
    provenance: form.get("provenance") || null,
    confidence: confidence ? Number(confidence) : null,
    observedAt: instantFrom(form, "observedAt"),
    validFrom: instantFrom(form, "validFrom"),
    validTo: instantFrom(form, "validTo"),
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
document.querySelectorAll(".workbench-menu-item").forEach((button) => {
  button.addEventListener("click", () => activateWorkbenchView(button.dataset.workbenchView));
});
document.querySelectorAll(".knowledge-tab").forEach((button) => {
  button.addEventListener("click", () => {
    state.knowledgeList = button.dataset.knowledgeList;
    state.knowledgeListPage = 0;
    state.knowledgePage = null;
    document.querySelectorAll(".knowledge-tab").forEach((tab) => {
      const active = tab === button;
      tab.classList.toggle("active", active);
      tab.setAttribute("aria-selected", String(active));
    });
    loadKnowledgeList().catch(report);
  });
});
let knowledgeSearchTimer;
document.querySelector("#knowledge-search").addEventListener("input", (event) => {
  state.knowledgeListQuery = event.target.value.trim();
  state.knowledgeListPage = 0;
  clearTimeout(knowledgeSearchTimer);
  knowledgeSearchTimer = setTimeout(() => loadKnowledgeList().catch(report), 200);
});
document.querySelector("#knowledge-previous-page").addEventListener("click", () => {
  if (state.knowledgeListPage === 0) return;
  state.knowledgeListPage -= 1;
  loadKnowledgeList().catch(report);
});
document.querySelector("#knowledge-next-page").addEventListener("click", () => {
  if (!state.knowledgePage || state.knowledgeListPage + 1 >= state.knowledgePage.totalPages) return;
  state.knowledgeListPage += 1;
  loadKnowledgeList().catch(report);
});
document.querySelector("#close-inspector").addEventListener("click", hideInspector);
document.querySelector("#reset-graph-view").addEventListener("click", resetGraphView);
document.querySelector("#graph-layout").addEventListener("change", (event) => {
  state.graphLayout = event.target.value;
  resetGraphView();
  if (state.knowledge) renderKnowledgeGraph(state.knowledge);
});
document.querySelector("#graph-topology").addEventListener("change", (event) => {
  state.graphProfile = event.target.value;
  state.graphDirection = graphProfile().direction;
  document.querySelector("#graph-direction").value = state.graphDirection;
  state.graphSource = null;
  state.graphDestination = null;
  state.graphDestinations = [];
  document.querySelector("#graph-source").value = "";
  const destination = document.querySelector("#graph-destination");
  destination.value = "";
  destination.disabled = true;
  state.graphTopology = null;
  semantics.clearDerivationProposals();
  semantics.clearSemanticTraversal();
  powerImpact.clear();
  updateGraphMode();
  renderGraphSources(state.knowledge);
  loadInitialTopology().catch(report);
});
document.querySelector("#graph-source").addEventListener("input", async (event) => {
  state.graphSource = event.target.value.trim() || null;
  state.graphTopology = null;
  semantics.clearDerivationProposals();
  semantics.clearSemanticTraversal();
  powerImpact.clear();
  state.graphDestination = null;
  const destination = document.querySelector("#graph-destination");
  destination.value = "";
  destination.disabled = !state.graphSource;
  updateGraphMode();
  if (!state.graphSource) {
    state.graphDestinations = [];
    await loadInitialTopology();
    return;
  }
  try {
    const query = new URLSearchParams({
      source: state.graphSource,
      direction: state.graphDirection,
      relationType: graphProfile().relationType
    });
    state.graphDestinations = await request(`/api/scopes/${state.scopeId}/topology/destinations?${query}`);
    const options = document.querySelector("#graph-destination-options");
    options.replaceChildren();
    state.graphDestinations.forEach((node) => {
      const option = document.createElement("option");
      option.value = node.id;
      options.append(option);
    });
    await loadAnchoredTopology();
  } catch (error) { report(error); }
});
document.querySelector("#graph-destination").addEventListener("input", (event) => {
  state.graphDestination = state.graphDestinations.some((node) => node.id === event.target.value)
    ? event.target.value
    : null;
  state.graphTopology = null;
  updateGraphMode();
  if (state.graphDestination) loadTopologyPath().catch(report); else loadAnchoredTopology().catch(report);
});
document.querySelector("#graph-direction").addEventListener("change", (event) => {
  state.graphDirection = event.target.value;
  state.graphTopology = null;
  semantics.clearDerivationProposals();
  semantics.clearSemanticTraversal();
  powerImpact.clear();
  state.graphDestination = null;
  document.querySelector("#graph-destination").value = "";
  updateGraphMode();
  if (state.graphSource) {
    document.querySelector("#graph-source").dispatchEvent(new Event("input"));
  } else {
    loadInitialTopology().catch(report);
  }
});
document.querySelector("#scope-search").addEventListener("input", () => loadScopes().catch(report));
document.querySelector("#language").addEventListener("change", (event) => {
  state.language = event.target.value;
  localStorage.setItem("persiqa.language", state.language);
  applyTranslations();
  hideInspector();
  if (state.scopeId) loadKnowledge().catch(report);
});
document.querySelector("#theme").addEventListener("change", (event) => {
  state.theme = event.target.value;
  localStorage.setItem("persiqa.theme", state.theme);
  applyTheme();
});
function graphPoint(event) {
  const bounds = graphSvg.getBoundingClientRect();
  const viewBox = graphSvg.viewBox.baseVal;
  return {
    x: (event.clientX - bounds.left) * viewBox.width / bounds.width,
    y: (event.clientY - bounds.top) * viewBox.height / bounds.height
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

function report(error) { showStatus(error.message, true); }

applyTheme();
applyTranslations();
recording.bind();
semantics.bind();
powerImpact.bind();
recording.resetRelationForm(document.querySelector("#record-relation-form"));
