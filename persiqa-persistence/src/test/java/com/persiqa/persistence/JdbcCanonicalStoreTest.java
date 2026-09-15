package com.persiqa.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.persiqa.model.Ckm.Entity;
import com.persiqa.model.Ckm.Kind;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.RelationType;
import java.util.Set;
import java.util.UUID;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

class JdbcCanonicalStoreTest {
  @Test
  void preserves_identity_separately_from_persistence_id_and_allows_parallel_relations() {
    var dataSource = new JdbcDataSource();
    dataSource.setURL(
        "jdbc:h2:mem:store_" + UUID.randomUUID() + ";MODE=PostgreSQL;DB_CLOSE_DELAY=-1");
    Flyway.configure()
        .dataSource(dataSource)
        .locations("classpath:db/h2/migration")
        .load()
        .migrate();
    var store = new JdbcCanonicalStore(dataSource, UUID.randomUUID());
    store.createScope("test");
    var source = new Entity("MCB-01");
    var target = new Entity("Outlet-01");
    var type =
        new RelationType("supplies", Set.of(Kind.ENTITY), Set.of(Kind.ENTITY), false, "none", true);
    var first = new Relation("supply-observation-a", type, source, target);
    var second = new Relation("supply-observation-b", type, source, target);

    var firstId = store.save(first);
    var secondId = store.save(second);

    assertNotEquals(firstId, secondId);
    assertEquals(first, store.findRelation("supply-observation-a"));
    assertEquals(second, store.findRelation("supply-observation-b"));
  }
}
