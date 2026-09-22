/**
 * Coordinates the structured CKM recording form without owning application state or transport.
 *
 * <p>The controller receives its dependencies explicitly so the form can remain a browser-native
 * ES module while the application keeps one shared source of truth.
 */
export function createRecordingController({
  kindLabel,
  loadKnowledge,
  relationTypeLabel,
  report,
  request,
  showStatus,
  state,
  translate
}) {
  const relationType = document.querySelector("#relation-type");
  const relationTypeHelp = document.querySelector("#relation-type-help");
  const sourceIdentity = document.querySelector("#source-id");
  const sourceKind = document.querySelector("#source-kind");
  const sourceOptions = document.querySelector("#source-endpoint-options");
  const targetIdentity = document.querySelector("#target-id");
  const targetKind = document.querySelector("#target-kind");
  const targetOptions = document.querySelector("#target-endpoint-options");

  function renderRelationTypes() {
    updateGuidance();
  }

  function renderEndpointOptions(knowledge) {
    state.endpointKinds.clear();
    knowledge.nodes.forEach((node) => state.endpointKinds.set(node.id, node.kind));
    knowledge.relations.forEach((relation) => state.endpointKinds.set(relation.id, "RELATION"));
    updateGuidance();
  }

  function updateGuidance() {
    const sourceKinds = [...new Set(state.relationTypes.flatMap((type) => type.sources))];
    updateKindChoices(sourceIdentity, sourceKind, sourceKinds, false);
    const selectedSourceKind = sourceKind.value;
    const eligibleTypes = selectedSourceKind
      ? state.relationTypes.filter((type) => type.sources.includes(selectedSourceKind))
      : state.relationTypes;
    renderRelationTypeOptions(eligibleTypes);
    const selectedType = eligibleTypes.find((type) => type.id === relationType.value);
    renderEndpointOptionsFor(
        sourceOptions, selectedSourceKind ? [selectedSourceKind] : sourceKinds);
    if (!selectedType) {
      disableTargetSelection();
      relationTypeHelp.textContent = "";
      return;
    }
    targetIdentity.disabled = false;
    relationTypeHelp.textContent = translate(`relation.${selectedType.id}.help`);
    updateKindChoices(targetIdentity, targetKind, selectedType.targets, true);
    renderEndpointOptionsFor(targetOptions, selectedType.targets);
  }

  function renderRelationTypeOptions(eligibleTypes) {
    const selected = relationType.value;
    relationType.replaceChildren();
    if (eligibleTypes.length !== 1) {
      relationType.append(new Option("", ""));
    }
    eligibleTypes.forEach((type) => {
      const option = new Option(relationTypeLabel(type.id), type.id);
      relationType.append(option);
    });
    relationType.value = eligibleTypes.some((type) => type.id === selected)
      ? selected
      : eligibleTypes.length === 1 ? eligibleTypes[0].id : "";
  }

  function updateKindChoices(identity, kind, allowedKinds, hideWhenDetermined) {
    kind.closest("label").classList.toggle("hidden", hideWhenDetermined && allowedKinds.length === 1);
    const knownKind = state.endpointKinds.get(identity.value.trim());
    if (knownKind && !allowedKinds.includes(knownKind)) {
      identity.value = "";
    }
    const selected = knownKind && allowedKinds.includes(knownKind) ? knownKind : kind.value;
    kind.replaceChildren();
    if (allowedKinds.length !== 1) {
      kind.append(new Option("", ""));
    }
    allowedKinds.forEach((allowedKind) => kind.append(new Option(kindLabel(allowedKind), allowedKind)));
    kind.value = allowedKinds.includes(selected)
      ? selected
      : allowedKinds.length === 1 ? allowedKinds[0] : "";
    synchronizeEndpointKind(identity, kind);
  }

  function renderEndpointOptionsFor(options, allowedKinds) {
    options.replaceChildren();
    state.endpointKinds.forEach((kind, id) => {
      if (!allowedKinds.includes(kind)) return;
      const option = new Option(id);
      option.label = kindLabel(kind);
      options.append(option);
    });
  }

  function synchronizeEndpointKind(identity, kind) {
    const knownKind = state.endpointKinds.get(identity.value.trim());
    if (knownKind && !Array.from(kind.options).some((option) => option.value === knownKind)) {
      identity.value = "";
      kind.disabled = false;
      return;
    }
    if (knownKind) kind.value = knownKind;
    kind.disabled = Boolean(knownKind);
  }

  function disableTargetSelection() {
    targetIdentity.value = "";
    targetIdentity.disabled = true;
    targetOptions.replaceChildren();
    targetKind.replaceChildren(new Option("", ""));
    targetKind.disabled = true;
    targetKind.closest("label").classList.remove("hidden");
  }

  function populateNodeKinds() {
    document.querySelectorAll(".node-kind").forEach((select) => {
      const selected = select.value;
      const kinds = select.dataset.kinds.split(",");
      select.replaceChildren();
      kinds.forEach((kind) => select.append(new Option(kindLabel(kind), kind)));
      select.value = selected || kinds[0];
    });
  }

  function resetRelationForm(form) {
    form.reset();
    sourceKind.disabled = false;
    targetKind.disabled = false;
    document.querySelector("#evidence-field").classList.add("hidden");
    updateGuidance();
  }

  function endpointFrom(form, identityName, kindName) {
    const id = form.get(identityName).trim();
    return state.endpointKinds.has(id) ? { id } : { id, kind: form.get(kindName) };
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

  function instantFrom(form, field) {
    const value = form.get(field);
    return value ? new Date(value).toISOString() : null;
  }

  function bind() {
    document.querySelector("#knowledge-kind").addEventListener("change", (event) => {
      document.querySelector("#evidence-field").classList.toggle("hidden", event.target.value !== "DERIVED");
    });
    relationType.addEventListener("change", updateGuidance);
    sourceKind.addEventListener("change", updateGuidance);
    sourceIdentity.addEventListener("input", () => {
      synchronizeEndpointKind(sourceIdentity, sourceKind);
      updateGuidance();
    });
    targetIdentity.addEventListener("input", () => synchronizeEndpointKind(targetIdentity, targetKind));
    document.querySelector("#create-node-form").addEventListener("submit", recordNode);
    document.querySelector("#record-relation-form").addEventListener("submit", recordRelation);
  }

  async function recordNode(event) {
    event.preventDefault();
    if (!state.scopeId) return;
    const form = event.currentTarget;
    try {
      const data = new FormData(form);
      await request(`/api/scopes/${state.scopeId}/nodes`, {
        method: "POST",
        body: JSON.stringify({ id: data.get("id"), kind: data.get("kind") })
      });
      form.reset();
      await loadKnowledge();
      showStatus(translate("status.nodeRecorded"));
    } catch (error) {
      report(error);
    }
  }

  async function recordRelation(event) {
    event.preventDefault();
    if (!state.scopeId) return;
    const form = event.currentTarget;
    try {
      const data = new FormData(form);
      const knowledgeKind = data.get("knowledgeKind");
      const evidence = String(data.get("derivedFrom") || "")
        .split(",")
        .map((value) => value.trim())
        .filter(Boolean);
      const record = await request(`/api/scopes/${state.scopeId}/statements`, {
        method: "POST",
        body: JSON.stringify({
          relationId: data.get("relationId"),
          statementId: data.get("statementId"),
          knowledgeKind,
          relationType: data.get("relationType"),
          source: endpointFrom(data, "sourceId", "sourceKind"),
          target: endpointFrom(data, "targetId", "targetKind"),
          derivedFrom: knowledgeKind === "DERIVED" ? evidence : [],
          context: contextFrom(data)
        })
      });
      resetRelationForm(form);
      await loadKnowledge();
      showStatus(
        translate("status.statementRecorded", {
          statement: record.statement.id,
          relation: record.relation.id
        }));
    } catch (error) {
      report(error);
    }
  }

  return { bind, populateNodeKinds, renderEndpointOptions, renderRelationTypes, resetRelationForm };
}
