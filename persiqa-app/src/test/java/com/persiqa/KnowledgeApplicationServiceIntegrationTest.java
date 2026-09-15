package com.persiqa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.persiqa.application.KnowledgeApplicationService;
import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.Entity;
import com.persiqa.model.Ckm.Kind;
import com.persiqa.model.Ckm.KnowledgeKind;
import com.persiqa.model.Ckm.RelationType;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/** Verifies the application use cases without exposing persistence details to callers. */
@SpringBootTest
@ActiveProfiles("dev")
class KnowledgeApplicationServiceIntegrationTest {
  @Autowired private KnowledgeApplicationService knowledge;

  @Test
  void records_statement_first_relations_and_preserves_observations() {
    var scope = UUID.randomUUID();
    var breaker = new Entity("MCB-01");
    var junctionBox = new Entity("JunctionBox-03");
    var outlet = new Entity("Outlet-01");
    var supplies =
        new RelationType("supplies", Set.of(Kind.ENTITY), Set.of(Kind.ENTITY), false, "none", true);

    knowledge.createScope(scope, "application-service-test");
    var first =
        knowledge.assertRelation(
            scope,
            "supply-1",
            supplies,
            breaker,
            junctionBox,
            new Context("inspection", new BigDecimal("0.9"), "as-built"));
    var second =
        knowledge.assertRelation(
            scope,
            "supply-2",
            supplies,
            junctionBox,
            outlet,
            new Context("inspection", new BigDecimal("0.9"), "as-built"));
    knowledge.appendObservation(
        scope, "supply-1", new Context("reinspection", new BigDecimal("0.8"), "as-maintained"));
    var derived =
        knowledge.recordDerivedRelation(
            scope,
            "supply-summary",
            supplies,
            breaker,
            outlet,
            Set.of(first.relation().id(), second.relation().id()),
            new Context("topology-analysis", new BigDecimal("0.95"), "as-built"));

    assertNotNull(knowledge.findRelation(scope, first.relation().id()));
    assertEquals(2, knowledge.findObservations(scope, "supply-1").size());
    assertEquals(KnowledgeKind.DERIVED, derived.statement().knowledgeKind());
    var relationIds =
        knowledge.findRelations(scope).stream()
            .map(relation -> relation.id())
            .collect(Collectors.toSet());
    assertEquals(
        Set.of("relation:supply-1", "relation:supply-2", "relation:supply-summary"), relationIds);
    assertEquals(
        Set.of("relation:supply-1", "relation:supply-2"),
        knowledge.findStatement(scope, "supply-summary").derivedFrom());
    var statementIds =
        knowledge.findStatements(scope).stream()
            .map(statement -> statement.id())
            .collect(Collectors.toSet());
    assertEquals(
        Set.of("supply-1", "supply-2", "supply-summary"), statementIds);
  }
}
