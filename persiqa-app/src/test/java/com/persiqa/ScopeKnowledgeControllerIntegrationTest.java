package com.persiqa;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.persiqa.application.KnowledgeApplicationService;
import com.persiqa.core.ScopeAccessDeniedException;
import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.Entity;
import com.persiqa.model.Ckm.Kind;
import com.persiqa.model.Ckm.KnowledgeKind;
import com.persiqa.web.KnowledgeWriteController.CreateNodeRequest;
import com.persiqa.web.KnowledgeWriteController.CreateScopeRequest;
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
        .andExpect(jsonPath("$[0].id").value("supply-mcb-lamp"))
        .andExpect(jsonPath("$[0].type.id").value("supplies"));
    http.perform(MockMvcRequestBuilders.get("/api/scopes/{scopeId}/statements", scope).with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value("supply-observation"))
        .andExpect(jsonPath("$[0].knowledgeKind").value("EXPLICIT"))
        .andExpect(jsonPath("$[0].context.provenance").value("inspection"));
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
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].provenance").value("inspection"))
        .andExpect(jsonPath("$[1].provenance").value("reinspection"));
    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/statements", scopeId).with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[3].id").value("supply-3"))
        .andExpect(jsonPath("$[3].knowledgeKind").value("DERIVED"))
        .andExpect(jsonPath("$[3].derivedFrom.length()").value(2));
    http.perform(
            MockMvcRequestBuilders.get("/api/scopes/{scopeId}/relations", scopeId).with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3));
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
    for (var scope : json.readTree(responseBody)) {
      scopeIds.add(scope.get("id").stringValue());
    }
    Assertions.assertTrue(scopeIds.contains(alpha.toString()));
    Assertions.assertTrue(scopeIds.contains(beta.toString()));
    Assertions.assertFalse(scopeIds.contains(bobScope.toString()));
    http.perform(MockMvcRequestBuilders.get("/api/scopes/{scopeId}", alpha).with(ALICE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Alpha"));
    http.perform(MockMvcRequestBuilders.get("/api/scopes/{scopeId}", alpha).with(BOB))
        .andExpect(status().isForbidden());
  }

  @Test
  void rejects_unauthenticated_api_calls() throws Exception {
    http.perform(MockMvcRequestBuilders.get("/api/scopes/{scopeId}/relations", UUID.randomUUID()))
        .andExpect(status().isUnauthorized());
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
}
