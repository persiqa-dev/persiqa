package com.persiqa;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.persiqa.application.KnowledgeApplicationService;
import com.persiqa.application.RefinementBindingService;
import com.persiqa.core.ScopeAccessDeniedException;
import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.Entity;
import com.persiqa.model.Ckm.Kind;
import com.persiqa.model.Ckm.KnowledgeKind;
import com.persiqa.persistence.repository.RefinementBindingDetailRepository;
import com.persiqa.persistence.repository.RefinementBindingRepository;
import com.persiqa.web.KnowledgeWriteController.CreateNodeRequest;
import com.persiqa.web.KnowledgeWriteController.CreateScopeRequest;
import com.persiqa.web.KnowledgeWriteController.DerivationAcceptanceRequest;
import com.persiqa.web.KnowledgeWriteController.NodeReference;
import com.persiqa.web.KnowledgeWriteController.RecordRelationRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import tools.jackson.databind.ObjectMapper;

/** Verifies the first HTTP endpoints with scope ownership and stable DTOs. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class ScopeKnowledgeControllerIntegrationTest {
  private static final RequestPostProcessor ALICE = httpBasic("alice", "alice");
  private static final RequestPostProcessor BOB = httpBasic("bob", "bob");

  @Autowired private KnowledgeApplicationService knowledge;
  @Autowired private MockMvc http;
  @Autowired private ObjectMapper json;
  @Autowired private RefinementBindingRepository refinementBindings;
  @Autowired private RefinementBindingDetailRepository refinementDetails;
  @Autowired private RefinementBindingService refinementService;

  @Test
  void returns_canonical_relations_and_statements_for_one_scope() throws Exception {
    var scope = UUID.randomUUID();

    knowledge.createScope(scope, "http-read-test", "alice");
    knowledge.assertRelation(
        scope,
        "alice",
        "supply-mcb-lamp",
        "supply-observation",
        "supplies",
        new Entity("MCB-01"),
        new Entity("Lamp-01"),
        new Context("inspection", new BigDecimal("0.9"), "as-built"));

    http.perform(MockMvcRequestBuilders.get("/api/scopes/{scopeId}/relations", scope).with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].id").value("supply-mcb-lamp"))
        .andExpect(jsonPath("$.content[0].type.id").value("supplies"));
    http.perform(MockMvcRequestBuilders.get("/api/scopes/{scopeId}/statements", scope).with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].id").value("supply-observation"))
        .andExpect(jsonPath("$.content[0].knowledgeKind").value("EXPLICIT"))
        .andExpect(jsonPath("$.content[0].context.provenance").value("inspection"));
    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/knowledge/summary", scope)
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.scope.name").value("http-read-test"))
        .andExpect(jsonPath("$.nodeCount").value(2))
        .andExpect(jsonPath("$.relationCount").value(1))
        .andExpect(jsonPath("$.statementCount").value(1));
  }

  @Test
  void projects_composable_supply_paths_at_requested_detail_levels() throws Exception {
    var scope = UUID.randomUUID();
    var mainSwitch = new Entity("MainSwitch-01");
    var rcd = new Entity("RCD-01");
    knowledge.createScope(scope, "topology-projection-test", "alice");
    assertSupply(scope, "supply-main-rcd", mainSwitch, rcd);
    var breaker = new Entity("MCB-Boiler-01");
    assertSupply(scope, "supply-rcd-breaker", rcd, breaker);
    var boiler = new Entity("ElectricBoiler-01");
    assertSupply(scope, "supply-breaker-boiler", breaker, boiler);

    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/topology/initial", scope)
                .param("profile", "ELECTRICAL_SUPPLY")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nodes.length()").value(4))
        .andExpect(jsonPath("$.edges.length()").value(3))
        .andExpect(jsonPath("$.nodes[?(@.node.id == 'MainSwitch-01')].anchor").value(true))
        .andExpect(jsonPath("$.nodes[?(@.node.id == 'ElectricBoiler-01')].terminal").value(true));

    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/topology/initial", scope)
                .param("profile", "ELECTRICAL_SUPPLY")
                .param("direction", "UPSTREAM")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nodes[?(@.node.id == 'ElectricBoiler-01')].anchor").value(true))
        .andExpect(jsonPath("$.nodes[?(@.node.id == 'MainSwitch-01')].terminal").value(true));

    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/topology/destinations", scope)
                .param("source", "MainSwitch-01")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.id == 'ElectricBoiler-01')].kind").value("ENTITY"));
    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/topology/path", scope)
                .param("source", "MainSwitch-01")
                .param("destination", "ElectricBoiler-01")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nodes.length()").value(4))
        .andExpect(jsonPath("$.edges.length()").value(3));

    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/topology", scope)
                .param("anchor", "MainSwitch-01")
                .param("detailLevel", "OVERVIEW")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nodes.length()").value(2))
        .andExpect(jsonPath("$.edges[0].virtual").value(true))
        .andExpect(jsonPath("$.edges[0].hopCount").value(3))
        .andExpect(jsonPath("$.edges[0].hiddenNodeCount").value(2));
    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/topology", scope)
                .param("anchor", "MainSwitch-01")
                .param("detailLevel", "INTERMEDIATE")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nodes[?(@.node.id == 'RCD-01')].depth").value(1));
    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/topology", scope)
                .param("anchor", "MainSwitch-01")
                .param("detailLevel", "DETAIL")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nodes.length()").value(4))
        .andExpect(jsonPath("$.edges.length()").value(3))
        .andExpect(jsonPath("$.edges[0].virtual").value(false));
  }

  @Test
  void returns_auditable_semantic_traversal_with_canonical_statement_witnesses() throws Exception {
    var scope = UUID.randomUUID();
    var breaker = new Entity("MCB-01");
    var junctionBox = new Entity("JunctionBox-01");
    var boiler = new Entity("ElectricBoiler-01");
    knowledge.createScope(scope, "semantic-traversal-test", "alice");
    assertSupply(scope, "supply-breaker-junction", breaker, junctionBox);
    knowledge.recordDerivedRelation(
        scope,
        "alice",
        "supply-junction-boiler",
        "derived-supply-junction-boiler",
        "supplies",
        junctionBox,
        boiler,
        Set.of("statement-supply-breaker-junction"),
        Context.unspecified());

    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/semantic/traversal", scope)
                .param("anchor", "MCB-01")
                .param("relationType", "supplies")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.anchor.id").value("MCB-01"))
        .andExpect(jsonPath("$.direction").value("DOWNSTREAM"))
        .andExpect(jsonPath("$.truncated").value(false))
        .andExpect(jsonPath("$.matches[?(@.target.id == 'JunctionBox-01')].hops").value(1))
        .andExpect(jsonPath("$.matches[?(@.target.id == 'ElectricBoiler-01')].hops").value(2))
        .andExpect(
            jsonPath("$.matches[?(@.target.id == 'ElectricBoiler-01')].witness[1].relation.id")
                .value("supply-junction-boiler"))
        .andExpect(
            jsonPath(
                    "$.matches[?(@.target.id == 'ElectricBoiler-01')].witness[1]"
                        + ".supportingStatements[0].id")
                .value("derived-supply-junction-boiler"))
        .andExpect(
            jsonPath(
                    "$.matches[?(@.target.id == 'ElectricBoiler-01')].witness[1]"
                        + ".supportingStatements[0].derivedFrom[0]")
                .value("statement-supply-breaker-junction"));

    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/semantic/traversal", scope)
                .param("anchor", "ElectricBoiler-01")
                .param("relationType", "supplies")
                .param("direction", "UPSTREAM")
                .param("maxHops", "1")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.matches[?(@.target.id == 'JunctionBox-01')].hops").value(1))
        .andExpect(jsonPath("$.truncated").value(true));

    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/semantic/traversal", scope)
                .param("anchor", "MCB-01")
                .param("relationType", "connectedTo")
                .with(ALICE))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.detail")
                .value("relation type is not semantically composable: " + "connectedTo"));

    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/topology/path", scope)
                .param("source", "MCB-01")
                .param("destination", "ElectricBoiler-01")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nodes.length()").value(3))
        .andExpect(jsonPath("$.edges.length()").value(2))
        .andExpect(
            jsonPath("$.edges[?(@.supportingRelationIds[0] == 'supply-junction-boiler')]")
                .exists());
  }

  @Test
  void proposes_then_records_an_approved_transitive_derivation() throws Exception {
    var scope = UUID.randomUUID();
    var breaker = new Entity("MCB-01");
    var junctionBox = new Entity("JunctionBox-01");
    var boiler = new Entity("ElectricBoiler-01");
    knowledge.createScope(scope, "derivation-proposal-test", "alice");
    assertSupply(scope, "supply-breaker-junction", breaker, junctionBox);
    assertSupply(scope, "supply-junction-boiler", junctionBox, boiler);

    http.perform(
            MockMvcRequestBuilders.get(
                    "/api/scopes/{scopeId}/semantic/derivation-proposals", scope)
                .param("anchor", "MCB-01")
                .param("relationType", "supplies")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].reachable.id").value("ElectricBoiler-01"))
        .andExpect(jsonPath("$[0].source.id").value("MCB-01"))
        .andExpect(jsonPath("$[0].target.id").value("ElectricBoiler-01"))
        .andExpect(jsonPath("$[0].evidenceStatementIds.length()").value(2));

    var approval =
        new DerivationAcceptanceRequest(
            "MCB-01",
            "ElectricBoiler-01",
            "supplies",
            null,
            null,
            new Context("semantic-review", new BigDecimal("1.0"), "as-built"));
    http.perform(
            MockMvcRequestBuilders.post("/api/scopes/{scopeId}/semantic/derivations", scope)
                .with(ALICE)
                .contentType("application/json")
                .content(json.writeValueAsString(approval)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.statement.knowledgeKind").value("DERIVED"))
        .andExpect(jsonPath("$.statement.derivedFrom.length()").value(2))
        .andExpect(jsonPath("$.statement.context.provenance").value("semantic-review"));

    http.perform(
            MockMvcRequestBuilders.get(
                    "/api/scopes/{scopeId}/semantic/derivation-proposals", scope)
                .param("anchor", "MCB-01")
                .param("relationType", "supplies")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", org.hamcrest.Matchers.empty()));

    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/topology/path", scope)
                .param("source", "MCB-01")
                .param("destination", "ElectricBoiler-01")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nodes.length()").value(3))
        .andExpect(jsonPath("$.edges.length()").value(2))
        .andExpect(
            jsonPath(
                    "$.edges[?(@.supportingRelationIds[0] == "
                        + "'supply-mcb-01-supplies-electricboiler-01-001')]")
                .isEmpty());
  }

  @Test
  void prefers_a_detailed_path_over_a_coarse_supply_shortcut() throws Exception {
    var scope = UUID.randomUUID();
    var breaker = new Entity("MCB-Boiler-01");
    var junctionBox = new Entity("JunctionBox-Boiler-01");
    var circuit = new Entity("BoilerCircuit-01");
    knowledge.createScope(scope, "refined-topology-path-test", "alice");
    assertSupply(scope, "supply-breaker-circuit", breaker, circuit);
    assertSupply(scope, "supply-breaker-junction-box", breaker, junctionBox);
    assertSupply(scope, "supply-junction-box-circuit", junctionBox, circuit);
    var boiler = new Entity("ElectricBoiler-01");
    assertSupply(scope, "supply-circuit-boiler", circuit, boiler);

    var binding = refinementBindings.findByScopeId(scope).getFirst();
    Assertions.assertEquals(
        2, refinementDetails.findByBindingIdOrderByOrdinalAsc(binding.id()).size());

    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/topology/path", scope)
                .param("source", "MCB-Boiler-01")
                .param("destination", "ElectricBoiler-01")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nodes.length()").value(4))
        .andExpect(jsonPath("$.edges.length()").value(3))
        .andExpect(
            jsonPath("$.edges[?(@.supportingRelationIds[0] == 'supply-breaker-circuit')]")
                .isEmpty());
  }

  @Test
  void selects_one_deterministic_path_when_refinement_is_ambiguous() throws Exception {
    var scope = UUID.randomUUID();
    var breaker = new Entity("MCB-01");
    var circuit = new Entity("Circuit-01");
    var firstJunctionBox = new Entity("JunctionBox-01");
    knowledge.createScope(scope, "ambiguous-refinement-test", "alice");
    assertSupply(scope, "supply-breaker-circuit", breaker, circuit);
    assertSupply(scope, "supply-breaker-junction-one", breaker, firstJunctionBox);
    assertSupply(scope, "supply-junction-one-circuit", firstJunctionBox, circuit);
    var secondJunctionBox = new Entity("JunctionBox-02");
    assertSupply(scope, "supply-breaker-junction-two", breaker, secondJunctionBox);
    assertSupply(scope, "supply-junction-two-circuit", secondJunctionBox, circuit);

    Assertions.assertEquals(1, refinementBindings.findByScopeId(scope).size());
    Assertions.assertTrue(refinementBindings.findByScopeIdAndInvalidatedAtIsNull(scope).isEmpty());

    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/topology/path", scope)
                .param("source", "MCB-01")
                .param("destination", "Circuit-01")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nodes.length()").value(2))
        .andExpect(jsonPath("$.edges.length()").value(1))
        .andExpect(
            jsonPath("$.edges[?(@.supportingRelationIds[0] == 'supply-breaker-circuit')]")
                .exists());
  }

  @Test
  void reactivates_a_refinement_when_one_detailed_path_is_unambiguous_again() {
    var scope = UUID.randomUUID();
    var breaker = new Entity("MCB-01");
    var junctionBox = new Entity("JunctionBox-01");
    var circuit = new Entity("Circuit-01");
    knowledge.createScope(scope, "reactivated-refinement-test", "alice");
    assertSupply(scope, "supply-breaker-circuit", breaker, circuit);
    assertSupply(scope, "supply-breaker-junction", breaker, junctionBox);
    assertSupply(scope, "supply-junction-circuit", junctionBox, circuit);

    var binding = refinementBindings.findByScopeId(scope).getFirst();
    binding.invalidate("test-ambiguity-resolved");
    refinementBindings.save(binding);

    refinementService.detectAndBind(scope, "supplies");

    Assertions.assertEquals(
        1, refinementBindings.findByScopeIdAndInvalidatedAtIsNull(scope).size());
    Assertions.assertEquals(
        2, refinementDetails.findByBindingIdOrderByOrdinalAsc(binding.id()).size());
  }

  @Test
  void records_explicit_and_derived_knowledge_and_appends_observations() throws Exception {
    var scopeResponse =
        http.perform(
                MockMvcRequestBuilders.post("/api/scopes")
                    .with(ALICE)
                    .contentType("application/json")
                    .content(json.writeValueAsString(new CreateScopeRequest("http-write-test"))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("http-write-test"))
            .andExpect(jsonPath("$.ownerSubject").value("alice"))
            .andReturn();
    var scopeId =
        UUID.fromString(
            json.readTree(scopeResponse.getResponse().getContentAsString(StandardCharsets.UTF_8))
                .get("id")
                .stringValue());
    var breaker = new NodeReference("MCB-01", Kind.ENTITY);
    var junctionBox = new NodeReference("JunctionBox-03", Kind.ENTITY);
    var outlet = new NodeReference("Outlet-01", Kind.ENTITY);

    postRelation(
        scopeId,
        new RecordRelationRequest(
            "supply-mcb-outlet",
            "supply-0",
            KnowledgeKind.EXPLICIT,
            "supplies",
            breaker,
            outlet,
            Set.of(),
            new Context("inspection", new BigDecimal("0.8"), "as-built")));
    postRelation(
        scopeId,
        new RecordRelationRequest(
            "supply-mcb-junction-box",
            "supply-1",
            KnowledgeKind.EXPLICIT,
            "supplies",
            breaker,
            junctionBox,
            Set.of(),
            new Context("inspection", new BigDecimal("0.9"), "as-built")));
    postRelation(
        scopeId,
        new RecordRelationRequest(
            "supply-junction-box-outlet",
            "supply-2",
            KnowledgeKind.EXPLICIT,
            "supplies",
            junctionBox,
            outlet,
            Set.of(),
            Context.unspecified()));
    postRelation(
        scopeId,
        new RecordRelationRequest(
            "supply-mcb-outlet",
            "supply-3",
            KnowledgeKind.DERIVED,
            "supplies",
            breaker,
            outlet,
            Set.of("supply-1", "supply-2"),
            new Context("supply-rule", BigDecimal.ONE, "as-built")));

    http.perform(
            MockMvcRequestBuilders.post(
                    "/api/scopes/{scopeId}/statements/{statementId}/observations",
                    scopeId,
                    "supply-1")
                .with(ALICE)
                .contentType("application/json")
                .content(
                    json.writeValueAsString(
                        new Context("reinspection", new BigDecimal("0.8"), "as-built"))))
        .andExpect(status().isNoContent());
    http.perform(
            MockMvcRequestBuilders.get(
                    "/api/scopes/{scopeId}/statements/{statementId}", scopeId, "supply-1")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("supply-1"))
        .andExpect(jsonPath("$.context.provenance").value("inspection"));
    http.perform(
            MockMvcRequestBuilders.get(
                    "/api/scopes/{scopeId}/statements/{statementId}/observations",
                    scopeId,
                    "supply-1")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].provenance").value("reinspection"));
    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/statements", scopeId).with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[3].id").value("supply-3"))
        .andExpect(jsonPath("$.content[3].knowledgeKind").value("DERIVED"))
        .andExpect(jsonPath("$.content[3].derivedFrom.length()").value(2));
    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/relations", scopeId).with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(3));
    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/relations", scopeId)
                .param("q", "junction")
                .param("size", "1")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].id").value("supply-junction-box-outlet"))
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.totalPages").value(2));
    http.perform(
            MockMvcRequestBuilders.get(
                    "/api/scopes/{scopeId}/relations/{relationId}/statements",
                    scopeId,
                    "supply-mcb-outlet")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value("supply-0"))
        .andExpect(jsonPath("$[1].id").value("supply-3"));
  }

  @Test
  void rejects_relation_with_an_invalid_baseline_endpoint_profile() throws Exception {
    var scope = UUID.randomUUID();
    knowledge.createScope(scope, "invalid-http-write-test", "alice");

    http.perform(
            MockMvcRequestBuilders.post("/api/scopes/{scopeId}/statements", scope)
                .with(ALICE)
                .contentType("application/json")
                .content(
                    json.writeValueAsString(
                        new RecordRelationRequest(
                            "invalid-state",
                            "invalid-state",
                            KnowledgeKind.EXPLICIT,
                            "hasState",
                            new NodeReference("Pump-01", Kind.ENTITY),
                            new NodeReference("Cable-01", Kind.ENTITY),
                            Set.of(),
                            Context.unspecified()))))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.detail").value("invalid endpoints for hasState"));
  }

  @Test
  void records_an_entity_without_inventing_additional_knowledge() throws Exception {
    var scope = UUID.randomUUID();
    knowledge.createScope(scope, "progressive-knowledge-test", "alice");

    http.perform(
            MockMvcRequestBuilders.post("/api/scopes/{scopeId}/nodes", scope)
                .with(ALICE)
                .contentType("application/json")
                .content(json.writeValueAsString(new CreateNodeRequest("Outlet-01", Kind.ENTITY))))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value("Outlet-01"))
        .andExpect(jsonPath("$.kind").value("ENTITY"));
    http.perform(
            MockMvcRequestBuilders.get(
                    "/api/scopes/{scopeId}/nodes/{kind}/{nodeId}",
                    scope,
                    "ENTITY",
                    "Outlet-01")
                .with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("Outlet-01"));
    http.perform(MockMvcRequestBuilders.get("/api/scopes/{scopeId}/nodes", scope).with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[?(@.id == 'Outlet-01')].kind").value("ENTITY"));
    http.perform(
            MockMvcRequestBuilders.post("/api/scopes/{scopeId}/nodes", scope)
                .with(ALICE)
                .contentType("application/json")
                .content("{\"id\":\"Open\",\"kind\":\"STATE\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void returns_not_found_for_an_unknown_scope() throws Exception {
    var scope = UUID.randomUUID();

    http.perform(MockMvcRequestBuilders.get("/api/scopes/{scopeId}/relations", scope).with(ALICE))
        .andExpect(status().isNotFound());
    http.perform(
            MockMvcRequestBuilders.post("/api/scopes/{scopeId}/statements", scope)
                .with(ALICE)
                .contentType("application/json")
                .content(
                    json.writeValueAsString(
                        new RecordRelationRequest(
                            "unknown-scope-relation",
                            "unknown-scope-statement",
                            KnowledgeKind.EXPLICIT,
                            "supplies",
                            new NodeReference("MCB-01", Kind.ENTITY),
                            new NodeReference("Outlet-01", Kind.ENTITY),
                            Set.of(),
                            Context.unspecified()))))
        .andExpect(status().isNotFound());
  }

  @Test
  void rejects_access_for_a_non_owner() throws Exception {
    var scope = UUID.randomUUID();
    knowledge.createScope(scope, "owned-by-alice", "alice");

    http.perform(MockMvcRequestBuilders.get("/api/scopes/{scopeId}/relations", scope).with(BOB))
        .andExpect(status().isForbidden());
    Assertions.assertThrows(
        ScopeAccessDeniedException.class, () -> knowledge.findRelations(scope, "bob"));
  }

  @Test
  void lists_and_reads_only_scopes_owned_by_the_authenticated_subject() throws Exception {
    var alpha = UUID.randomUUID();
    var beta = UUID.randomUUID();
    var bobScope = UUID.randomUUID();
    knowledge.createScope(beta, "Beta", "alice");
    knowledge.createScope(alpha, "Alpha", "alice");
    knowledge.createScope(bobScope, "Bob scope", "bob");

    var response =
        http.perform(MockMvcRequestBuilders.get("/api/scopes").with(ALICE))
            .andExpect(status().isOk())
            .andReturn();
    var scopeIds = new java.util.HashSet<String>();
    var responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
    for (var scope : json.readTree(responseBody).get("content")) {
      scopeIds.add(scope.get("id").stringValue());
    }
    Assertions.assertTrue(scopeIds.contains(alpha.toString()));
    Assertions.assertTrue(scopeIds.contains(beta.toString()));
    Assertions.assertFalse(scopeIds.contains(bobScope.toString()));
    http.perform(MockMvcRequestBuilders.get("/api/scopes").param("q", "alpha").with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].id").value(alpha.toString()));
    http.perform(MockMvcRequestBuilders.get("/api/scopes/{scopeId}", alpha).with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Alpha"));
    http.perform(MockMvcRequestBuilders.get("/api/scopes/{scopeId}", alpha).with(BOB))
        .andExpect(status().isForbidden());
  }

  @Test
  void exposes_the_effective_relation_type_contracts_for_recording() throws Exception {
    http.perform(MockMvcRequestBuilders.get("/api/relation-types").with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(10))
        .andExpect(jsonPath("$[0].id").value("classifiedAs"))
        .andExpect(jsonPath("$[9].id").value("supplies"))
        .andExpect(jsonPath("$[9].sources[0]").value("ENTITY"))
        .andExpect(jsonPath("$[9].targets[0]").value("ENTITY"))
        .andExpect(jsonPath("$[9].composable").value(true));
  }

  @Test
  void exposes_the_authenticated_api_contract_through_openapi() throws Exception {
    http.perform(MockMvcRequestBuilders.get("/v3/api-docs"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.info.title").value("Persiqa API"))
        .andExpect(jsonPath("$.paths['/api/scopes']").exists())
        .andExpect(jsonPath("$.components.securitySchemes.basicAuth.scheme").value("basic"));
  }

  @Test
  void serves_the_thin_web_client_without_requiring_api_authentication() throws Exception {
    http.perform(MockMvcRequestBuilders.get("/index.html"))
        .andExpect(status().isOk())
        .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
            .string(org.hamcrest.Matchers.containsString("data-i18n=\"app.title\"")));
  }

  @Test
  void allocates_readable_identities_when_a_relation_request_omits_them() throws Exception {
    var scope = UUID.randomUUID();
    knowledge.createScope(scope, "generated-identities-test", "alice");
    var request =
        new RecordRelationRequest(
            null,
            null,
            KnowledgeKind.EXPLICIT,
            "supplies",
            new NodeReference("MCB-01", Kind.ENTITY),
            new NodeReference("Outlet-01", Kind.ENTITY),
            Set.of(),
            Context.unspecified());

    http.perform(
            MockMvcRequestBuilders.post("/api/scopes/{scopeId}/statements", scope)
                .with(ALICE)
                .contentType("application/json")
                .content(json.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.relation.id").value("rel-mcb-01-supplies-outlet-01-001"))
        .andExpect(
            jsonPath("$.statement.id")
                .value("stmt-rel-mcb-01-supplies-outlet-01-001-explicit-001"));
    http.perform(
            MockMvcRequestBuilders.post("/api/scopes/{scopeId}/statements", scope)
                .with(ALICE)
                .contentType("application/json")
                .content(json.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.relation.id").value("rel-mcb-01-supplies-outlet-01-002"))
        .andExpect(
            jsonPath("$.statement.id")
                .value("stmt-rel-mcb-01-supplies-outlet-01-002-explicit-001"));
  }

  @Test
  void resolves_an_existing_endpoint_from_its_identity_without_a_client_supplied_kind()
      throws Exception {
    var scope = UUID.randomUUID();
    knowledge.createScope(scope, "server-resolved-endpoint-test", "alice");
    knowledge.saveNode(scope, "alice", new Entity("MCB-01"));
    var request =
        new RecordRelationRequest(
            null,
            null,
            KnowledgeKind.EXPLICIT,
            "supplies",
            new NodeReference("MCB-01", null),
            new NodeReference("Outlet-01", Kind.ENTITY),
            Set.of(),
            Context.unspecified());

    http.perform(
            MockMvcRequestBuilders.post("/api/scopes/{scopeId}/statements", scope)
                .with(ALICE)
                .contentType("application/json")
                .content(json.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.relation.source.id").value("MCB-01"))
        .andExpect(jsonPath("$.relation.source.kind").value("ENTITY"));
  }

  @Test
  void rejects_a_kind_that_disagrees_with_an_existing_source_or_target_endpoint() throws Exception {
    var scope = UUID.randomUUID();
    knowledge.createScope(scope, "endpoint-kind-validation-test", "alice");
    knowledge.saveNode(scope, "alice", new Entity("MCB-01"));
    knowledge.saveNode(scope, "alice", new Entity("Outlet-01"));
    var sourceMismatch =
        new RecordRelationRequest(
            null,
            null,
            KnowledgeKind.EXPLICIT,
            "supplies",
            new NodeReference("MCB-01", Kind.CAPABILITY),
            new NodeReference("Outlet-01", null),
            Set.of(),
            Context.unspecified());
    var targetMismatch =
        new RecordRelationRequest(
            null,
            null,
            KnowledgeKind.EXPLICIT,
            "supplies",
            new NodeReference("MCB-01", null),
            new NodeReference("Outlet-01", Kind.CAPABILITY),
            Set.of(),
            Context.unspecified());

    assertEndpointKindMismatch(scope, sourceMismatch, "MCB-01");
    assertEndpointKindMismatch(scope, targetMismatch, "Outlet-01");
  }

  @Test
  void preserves_manually_supplied_relation_and_statement_identities() throws Exception {
    var scope = UUID.randomUUID();
    knowledge.createScope(scope, "manual-identities-test", "alice");
    var request =
        new RecordRelationRequest(
            "kitchen-circuit-link",
            "as-built-inspection-17",
            KnowledgeKind.EXPLICIT,
            "supplies",
            new NodeReference("MCB-01", Kind.ENTITY),
            new NodeReference("Outlet-01", Kind.ENTITY),
            Set.of(),
            Context.unspecified());

    http.perform(
            MockMvcRequestBuilders.post("/api/scopes/{scopeId}/statements", scope)
                .with(ALICE)
                .contentType("application/json")
                .content(json.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.relation.id").value("kitchen-circuit-link"))
        .andExpect(jsonPath("$.statement.id").value("as-built-inspection-17"));
  }

  @Test
  void rejects_unauthenticated_api_calls() throws Exception {
    http.perform(MockMvcRequestBuilders.get("/api/scopes/{scopeId}/relations", UUID.randomUUID()))
        .andExpect(status().isUnauthorized());
  }

  private void assertSupply(UUID scopeId, String id, Entity source, Entity target) {
    knowledge.assertRelation(
        scopeId,
        "alice",
        id,
        "statement-" + id,
        "supplies",
        source,
        target,
        Context.unspecified());
  }

  private void postRelation(UUID scopeId, RecordRelationRequest request) throws Exception {
    http.perform(
            MockMvcRequestBuilders.post("/api/scopes/{scopeId}/statements", scopeId)
                .with(ALICE)
                .contentType("application/json")
                .content(json.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.statement.id").value(request.statementId()))
        .andExpect(jsonPath("$.relation.type.id").value(request.relationType()));
  }

  private void assertEndpointKindMismatch(
      UUID scopeId, RecordRelationRequest request, String identity) throws Exception {
    http.perform(
            MockMvcRequestBuilders.post("/api/scopes/{scopeId}/statements", scopeId)
                .with(ALICE)
                .contentType("application/json")
                .content(json.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString(identity)));
  }
}
