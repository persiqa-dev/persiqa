package com.persiqa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.Entity;
import com.persiqa.model.Ckm.Kind;
import com.persiqa.model.Ckm.KnowledgeKind;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.RelationType;
import com.persiqa.model.Ckm.State;
import com.persiqa.model.Ckm.Statement;
import com.persiqa.persistence.JpaCanonicalStore;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/** Verifies the production Flyway schema and JPA mapping against PostgreSQL. */
@SpringBootTest
@ActiveProfiles("postgres")
@Testcontainers
class JpaCanonicalStorePostgresIntegrationTest {
  @Container
  static final PostgreSQLContainer POSTGRES =
      new PostgreSQLContainer(DockerImageName.parse("postgres:17-alpine"));

  @Autowired private JpaCanonicalStore store;

  @DynamicPropertySource
  static void configureDatabase(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES::getUsername);
    registry.add("spring.datasource.password", POSTGRES::getPassword);
  }

  @Test
  void persists_a_relation_using_the_production_schema() {
    var scope = UUID.randomUUID();
    var breaker = new Entity("MCB-01");
    var outlet = new Entity("Outlet-01");
    var supplies =
        new RelationType("supplies", Set.of(Kind.ENTITY), Set.of(Kind.ENTITY), false, "none", true);

    store.createScope(scope, "postgres-integration-test");
    store.save(scope, new Relation("supply-a", supplies, breaker, outlet));

    var restored = store.findRelation(scope, "supply-a");
    assertNotNull(restored);
    assertEquals("supplies", restored.type().id());
    assertEquals("MCB-01", restored.source().id());
    assertEquals("Outlet-01", restored.target().id());
  }

  @Test
  void preserves_derived_evidence_context_and_state_in_postgresql() {
    var scope = UUID.randomUUID();
    var breaker = new Entity("MCB-01");
    var junctionBox = new Entity("JunctionBox-03");
    var outlet = new Entity("Outlet-01");
    var supplies =
        new RelationType("supplies", Set.of(Kind.ENTITY), Set.of(Kind.ENTITY), false, "none", true);
    var hasState =
        new RelationType(
            "hasState",
            Set.of(Kind.ENTITY, Kind.RELATION),
            Set.of(Kind.STATE),
            false,
            "none",
            false);

    store.createScope(scope, "postgres-knowledge-context-test");
    store.save(scope, new Relation("supply-1", supplies, breaker, junctionBox));
    store.save(scope, new Relation("supply-2", supplies, junctionBox, outlet));
    store.save(
        scope,
        new Statement(
            "derived-supply",
            KnowledgeKind.DERIVED,
            "supplies",
            breaker,
            outlet,
            Set.of("supply-1", "supply-2"),
            new Context("topology-analysis", new BigDecimal("0.95"), "as-built")));
    store.appendContext(
        scope,
        "derived-supply",
        new Context("reinspection", new BigDecimal("0.90"), "as-maintained"));
    store.save(scope, new Relation("breaker-closed", hasState, breaker, new State("Closed")));

    assertEquals(
        Set.of("supply-1", "supply-2"),
        store.findStatement(scope, "derived-supply").derivedFrom());
    assertEquals(2, store.findContexts(scope, "derived-supply").size());
    assertEquals("Closed", store.findRelation(scope, "breaker-closed").target().id());
  }
}
