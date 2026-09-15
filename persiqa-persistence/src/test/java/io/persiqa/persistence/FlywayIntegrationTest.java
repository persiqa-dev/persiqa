package io.persiqa.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.DriverManager;
import java.util.UUID;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;

class FlywayIntegrationTest {
  @Test
  void migration_creates_statement_first_schema_and_allows_parallel_relations() throws Exception {
    var database = "jdbc:h2:mem:ckm_" + UUID.randomUUID() + ";MODE=PostgreSQL;DB_CLOSE_DELAY=-1";
    Flyway.configure()
        .dataSource(database, "sa", "")
        .locations("classpath:db/h2/migration")
        .load()
        .migrate();
    try (var connection = DriverManager.getConnection(database, "sa", "")) {
      var scope = UUID.randomUUID();
      var source = UUID.randomUUID();
      var target = UUID.randomUUID();
      var first = UUID.randomUUID();
      var second = UUID.randomUUID();
      var type = UUID.randomUUID();
      try (var insertScope =
          connection.prepareStatement("insert into model_scope(scope_id,name) values (?,?)")) {
        insertScope.setObject(1, scope);
        insertScope.setString(2, "test");
        insertScope.executeUpdate();
      }
      try (var insertObject =
          connection.prepareStatement(
              "insert into canonical_object(object_id,scope_id,identity_key,kind) values"
                  + " (?,?,?,?)")) {
        for (var id : new UUID[] {source, target, first, second}) {
          insertObject.setObject(1, id);
          insertObject.setObject(2, scope);
          insertObject.setString(3, id.toString());
          insertObject.setString(4, "ENTITY");
          insertObject.addBatch();
        }
        insertObject.executeBatch();
      }
      try (var markRelations =
          connection.prepareStatement(
              "update canonical_object set kind = ? where object_id in (?,?)")) {
        markRelations.setString(1, "RELATION");
        markRelations.setObject(2, first);
        markRelations.setObject(3, second);
        markRelations.executeUpdate();
      }
      try (var insertType =
          connection.prepareStatement(
              "insert into"
                  + " relation_type(relation_type_id,semantic_identifier,semantic_version,source_profile,target_profile)"
                  + " values (?,?,?,?,?)")) {
        insertType.setObject(1, type);
        insertType.setString(2, "connectedTo");
        insertType.setString(3, "1.0");
        insertType.setString(4, "{}");
        insertType.setString(5, "{}");
        insertType.executeUpdate();
      }
      try (var insertRelation =
          connection.prepareStatement(
              "insert into relation(relation_id,relation_type_id,source_object_id,target_object_id)"
                  + " values (?,?,?,?)")) {
        for (var relation : new UUID[] {first, second}) {
          insertRelation.setObject(1, relation);
          insertRelation.setObject(2, type);
          insertRelation.setObject(3, source);
          insertRelation.setObject(4, target);
          insertRelation.addBatch();
        }
        insertRelation.executeBatch();
      }
      try (var statement = connection.createStatement();
          var result = statement.executeQuery("select count(*) from relation")) {
        result.next();
        assertEquals(2, result.getInt(1));
      }
    }
  }
}
