package com.persiqa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.persiqa.application.KnowledgeApplicationService;
import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.Entity;
import com.persiqa.model.Ckm.KnowledgeKind;
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
  private static final String OWNER = "alice";

  @Autowired private KnowledgeApplicationService knowledge;

  @Test
  void records_statement_first_relations_and_preserves_observations() {
    var scope = UUID.randomUUID();
    var breaker = new Entity("MCB-01");
    var junctionBox = new Entity("JunctionBox-03");
    var outlet = new Entity("Outlet-01");

    knowledge.createScope(scope, "application-service-test", OWNER);
    var coarse =
        knowledge.assertRelation(
            scope,
            OWNER,
            "supply-mcb-outlet",
            "supply-0",
            "supplies",
            breaker,
            outlet,
            new Context("inspection", new BigDecimal("0.8"), "as-built"));
    var first =
        knowledge.assertRelation(
            scope,
            OWNER,
            "supply-mcb-junction-box",
            "supply-1",
            "supplies",
            breaker,
            junctionBox,
            new Context("inspection", new BigDecimal("0.9"), "as-built"));
    var second =
        knowledge.assertRelation(
            scope,
            OWNER,
            "supply-junction-box-outlet",
            "supply-2",
            "supplies",
            junctionBox,
            outlet,
            new Context("inspection", new BigDecimal("0.9"), "as-built"));
    knowledge.appendObservation(
        scope,
        OWNER,
        "supply-1",
        new Context("reinspection", new BigDecimal("0.8"), "as-maintained"));
    var derived =
        knowledge.recordDerivedRelation(
            scope,
            OWNER,
            "supply-mcb-outlet",
            "supply-summary",
            "supplies",
            breaker,
            outlet,
            Set.of(first.statement().id(), second.statement().id()),
            new Context("topology-analysis", new BigDecimal("0.95"), "as-built"));

    assertNotNull(knowledge.findRelation(scope, OWNER, coarse.relation().id()));
    assertEquals(1, knowledge.findObservations(scope, OWNER, "supply-1").size());
    assertEquals(KnowledgeKind.DERIVED, derived.statement().knowledgeKind());
    assertEquals(
        Set.of("supply-0", "supply-summary"),
        knowledge.findStatementsForRelation(scope, OWNER, "supply-mcb-outlet").stream()
            .map(statement -> statement.id())
            .collect(Collectors.toSet()));
    var relationIds =
        knowledge.findRelations(scope, OWNER).stream()
            .map(relation -> relation.id())
            .collect(Collectors.toSet());
    assertEquals(
        Set.of("supply-mcb-outlet", "supply-mcb-junction-box", "supply-junction-box-outlet"),
        relationIds);
    assertEquals(
        Set.of("supply-1", "supply-2"),
        knowledge.findStatement(scope, OWNER, "supply-summary").derivedFrom());
    var statementIds =
        knowledge.findStatements(scope, OWNER).stream()
            .map(statement -> statement.id())
            .collect(Collectors.toSet());
    assertEquals(
        Set.of("supply-0", "supply-1", "supply-2", "supply-summary"), statementIds);
  }
}
