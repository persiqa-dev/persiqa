package io.persiqa.persistence;

import java.sql.DriverManager;
import java.util.UUID;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FlywayIntegrationTest {
  @Test void migration_creates_statement_first_schema_and_allows_parallel_relations() throws Exception {
    var database = "jdbc:h2:mem:ckm_" + UUID.randomUUID() + ";MODE=PostgreSQL;DB_CLOSE_DELAY=-1";
    Flyway.configure().dataSource(database, "sa", "").locations("classpath:db/h2/migration").load().migrate();
    try (var connection = DriverManager.getConnection(database, "sa", ""); var statement = connection.createStatement()) {
        var scope = UUID.randomUUID(); var source = UUID.randomUUID(); var target = UUID.randomUUID(); var first = UUID.randomUUID(); var second = UUID.randomUUID(); var type = UUID.randomUUID();
        statement.executeUpdate("insert into model_scope(scope_id,name) values ('"+scope+"','test')");
        for (var id : new UUID[]{source,target,first,second}) statement.executeUpdate("insert into canonical_object(object_id,scope_id,kind) values ('"+id+"','"+scope+"','ENTITY')");
        statement.executeUpdate("update canonical_object set kind='RELATION' where object_id in ('"+first+"','"+second+"')");
        statement.executeUpdate("insert into relation_type(relation_type_id,semantic_identifier,semantic_version,source_profile,target_profile) values ('"+type+"','connectedTo','1.0','{}','{}')");
        statement.executeUpdate("insert into relation(relation_id,relation_type_id,source_object_id,target_object_id) values ('"+first+"','"+type+"','"+source+"','"+target+"'),('"+second+"','"+type+"','"+source+"','"+target+"')");
        var result=statement.executeQuery("select count(*) from relation"); result.next(); assertEquals(2,result.getInt(1));
    }
  }
}
