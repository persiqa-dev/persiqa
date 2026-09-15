package com.persiqa;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.persiqa.application.KnowledgeApplicationService;
import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.Entity;
import com.persiqa.model.Ckm.Kind;
import com.persiqa.model.Ckm.KnowledgeKind;
import com.persiqa.model.Ckm.RelationType;
import com.persiqa.web.KnowledgeWriteController.CreateNodeRequest;
import com.persiqa.web.KnowledgeWriteController.CreateScopeRequest;
import com.persiqa.web.KnowledgeWriteController.NodeReference;
import com.persiqa.web.KnowledgeWriteController.RecordRelationRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

/** Verifies the first read-only HTTP endpoints without a client-side transport model. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class ScopeKnowledgeControllerIntegrationTest {
  @Autowired private KnowledgeApplicationService knowledge;
  @Autowired private MockMvc http;
  @Autowired private ObjectMapper json;

  @Test
  void returns_canonical_relations_and_statements_for_one_scope() throws Exception {
    var scope = UUID.randomUUID();
    var supplies =
        new RelationType("supplies", Set.of(Kind.ENTITY), Set.of(Kind.ENTITY), false, "none", true);

    knowledge.createScope(scope, "http-read-test");
    knowledge.assertRelation(
        scope,
        "supply-mcb-lamp",
        "supply-observation",
        supplies,
        new Entity("MCB-01"),
        new Entity("Lamp-01"),
        new Context("inspection", new BigDecimal("0.9"), "as-built"));

    http.perform(get("/api/scopes/{scopeId}/relations", scope))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value("supply-mcb-lamp"))
        .andExpect(jsonPath("$[0].type.id").value("supplies"));
    http.perform(get("/api/scopes/{scopeId}/statements", scope))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value("supply-observation"))
        .andExpect(jsonPath("$[0].knowledgeKind").value("EXPLICIT"));
  }

  @Test
  void records_explicit_and_derived_knowledge_and_appends_observations() throws Exception {
    var scopeResponse =
        http.perform(
                post("/api/scopes")
                    .contentType("application/json")
                    .content(json.writeValueAsString(new CreateScopeRequest("http-write-test"))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("http-write-test"))
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
            post("/api/scopes/{scopeId}/statements/{statementId}/observations", scopeId, "supply-1")
                .contentType("application/json")
                .content(
                    json.writeValueAsString(
                        new Context("reinspection", new BigDecimal("0.8"), "as-built"))))
        .andExpect(status().isNoContent());
    http.perform(get("/api/scopes/{scopeId}/statements/{statementId}", scopeId, "supply-1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("supply-1"))
        .andExpect(jsonPath("$.context.provenance").value("inspection"));
    http.perform(
            get(
                "/api/scopes/{scopeId}/statements/{statementId}/observations",
                scopeId,
                "supply-1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].provenance").value("inspection"))
        .andExpect(jsonPath("$[1].provenance").value("reinspection"));
    http.perform(get("/api/scopes/{scopeId}/statements", scopeId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[3].id").value("supply-3"))
        .andExpect(jsonPath("$[3].knowledgeKind").value("DERIVED"))
        .andExpect(jsonPath("$[3].derivedFrom.length()").value(2));
    http.perform(get("/api/scopes/{scopeId}/relations", scopeId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3));
    http.perform(
            get(
                "/api/scopes/{scopeId}/relations/{relationId}/statements",
                scopeId,
                "supply-mcb-outlet"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value("supply-0"))
        .andExpect(jsonPath("$[1].id").value("supply-3"));
  }

  @Test
  void rejects_relation_with_an_invalid_baseline_endpoint_profile() throws Exception {
    var scope = UUID.randomUUID();
    knowledge.createScope(scope, "invalid-http-write-test");

    http.perform(
            post("/api/scopes/{scopeId}/statements", scope)
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
    knowledge.createScope(scope, "progressive-knowledge-test");

    http.perform(
            post("/api/scopes/{scopeId}/nodes", scope)
                .contentType("application/json")
                .content(json.writeValueAsString(new CreateNodeRequest("Outlet-01", Kind.ENTITY))))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value("Outlet-01"));
    http.perform(get("/api/scopes/{scopeId}/nodes/{kind}/{nodeId}", scope, "ENTITY", "Outlet-01"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("Outlet-01"));
    http.perform(
            post("/api/scopes/{scopeId}/nodes", scope)
                .contentType("application/json")
                .content("{\"id\":\"Open\",\"kind\":\"STATE\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void returns_not_found_for_an_unknown_scope() throws Exception {
    var scope = UUID.randomUUID();

    http.perform(get("/api/scopes/{scopeId}/relations", scope)).andExpect(status().isNotFound());
    http.perform(
            post("/api/scopes/{scopeId}/statements", scope)
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

  private void postRelation(UUID scopeId, RecordRelationRequest request) throws Exception {
    http.perform(
            post("/api/scopes/{scopeId}/statements", scopeId)
                .contentType("application/json")
                .content(json.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.statement.id").value(request.statementId()))
        .andExpect(jsonPath("$.relation.type.id").value(request.relationType()));
  }
}
