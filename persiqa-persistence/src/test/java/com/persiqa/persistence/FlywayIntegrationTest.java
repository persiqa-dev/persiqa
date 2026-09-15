package com.persiqa.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.UUID;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

class FlywayIntegrationTest {
  @Test
  void migration_creates_statement_first_schema_and_allows_parallel_relations() {
    var dataSource = new JdbcDataSource();
    dataSource.setURL(
        "jdbc:h2:mem:ckm_" + UUID.randomUUID() + ";MODE=PostgreSQL;DB_CLOSE_DELAY=-1");
    Flyway.configure()
        .dataSource(dataSource)
        .locations("classpath:db/h2/migration")
        .load()
        .migrate();
    var jdbc = new NamedParameterJdbcTemplate(dataSource);
    var scope = UUID.randomUUID();
    var source = UUID.randomUUID();
    var target = UUID.randomUUID();
    var first = UUID.randomUUID();
    var second = UUID.randomUUID();

    jdbc.update(
        "insert into model_scope(scope_id,name) values (:scopeId,:name)",
        Map.of("scopeId", scope, "name", "test"));
    for (var id : new UUID[] {source, target, first, second}) {
      jdbc.update(
          "insert into canonical_object(object_id,scope_id,identity_key,kind) values"
              + " (:objectId,:scopeId,:identityKey,:kind)",
          Map.of("objectId", id, "scopeId", scope, "identityKey", id.toString(), "kind", "ENTITY"));
    }
    jdbc.update(
        "update canonical_object set kind=:kind where object_id in (:first,:second)",
        Map.of("kind", "RELATION", "first", first, "second", second));
    var type = UUID.randomUUID();
    jdbc.update(
        "insert into"
            + " relation_type(relation_type_id,semantic_identifier,semantic_version,"
            + "source_profile,target_profile)"
            + " values (:typeId,:identifier,:version,:sourceProfile,:targetProfile)",
        Map.of(
            "typeId",
            type,
            "identifier",
            "connectedTo",
            "version",
            "1.0",
            "sourceProfile",
            "{}",
            "targetProfile",
            "{}"));
    for (var relation : new UUID[] {first, second}) {
      jdbc.update(
          "insert into relation(relation_id,relation_type_id,source_object_id,target_object_id)"
              + " values (:relationId,:typeId,:sourceId,:targetId)",
          Map.of("relationId", relation, "typeId", type, "sourceId", source, "targetId", target));
    }
    assertEquals(2, jdbc.queryForObject("select count(*) from relation", Map.of(), Integer.class));
  }
}
