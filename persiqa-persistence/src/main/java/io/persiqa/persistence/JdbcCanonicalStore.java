package io.persiqa.persistence;

import io.persiqa.model.Ckm.Capability;
import io.persiqa.model.Ckm.Concept;
import io.persiqa.model.Ckm.Entity;
import io.persiqa.model.Ckm.Kind;
import io.persiqa.model.Ckm.Node;
import io.persiqa.model.Ckm.Relation;
import io.persiqa.model.Ckm.RelationType;
import io.persiqa.model.Ckm.State;
import io.persiqa.model.Ckm.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import javax.sql.DataSource;

/** JDBC persistence boundary for canonical objects and Relations. Statements follow separately. */
public final class JdbcCanonicalStore {
  private static final String VERSION = "0.1";
  private final DataSource dataSource;
  private final UUID scopeId;

  public JdbcCanonicalStore(DataSource dataSource, UUID scopeId) {
    this.dataSource = dataSource;
    this.scopeId = scopeId;
  }

  public void createScope(String name) {
    try (var connection = dataSource.getConnection()) {
      if (objectId(connection, "select scope_id from model_scope where scope_id = ?", scopeId)
          == null) {
        try (var insert =
            connection.prepareStatement("insert into model_scope(scope_id,name) values (?,?)")) {
          insert.setObject(1, scopeId);
          insert.setString(2, name);
          insert.executeUpdate();
        }
      }
    } catch (SQLException error) {
      throw new PersistenceException(error);
    }
  }

  public UUID save(Node node) {
    try (var connection = dataSource.getConnection()) {
      return save(connection, node);
    } catch (SQLException error) {
      throw new PersistenceException(error);
    }
  }

  public Relation findRelation(String identityKey) {
    try (var connection = dataSource.getConnection();
        var query =
            connection.prepareStatement(
                """
select r.source_object_id, r.target_object_id, t.semantic_identifier, t.source_profile,
       t.target_profile, t.is_symmetric, t.inverse_identifier, t.inference_policy
  from relation r join canonical_object o on o.object_id = r.relation_id
  join relation_type t on t.relation_type_id = r.relation_type_id
 where o.scope_id = ? and o.identity_key = ?""")) {
      query.setObject(1, scopeId);
      query.setString(2, identityKey);
      try (var result = query.executeQuery()) {
        if (!result.next()) {
          return null;
        }
        var type =
            new RelationType(
                result.getString(3),
                kinds(result.getString(4)),
                kinds(result.getString(5)),
                result.getBoolean(6),
                result.getString(7),
                result.getString(8).contains("true"));
        return new Relation(
            identityKey,
            type,
            findNode(connection, (UUID) result.getObject(1)),
            findNode(connection, (UUID) result.getObject(2)));
      }
    } catch (SQLException error) {
      throw new PersistenceException(error);
    }
  }

  private UUID save(Connection connection, Node node) throws SQLException {
    if (node instanceof Statement statement) {
      return saveStatement(connection, statement);
    }
    if (node instanceof Relation relation) {
      var source = save(connection, relation.source());
      var target = save(connection, relation.target());
      var relationId = saveObject(connection, relation);
      var typeId = saveType(connection, relation.type());
      if (objectId(connection, "select relation_id from relation where relation_id = ?", relationId)
          == null) {
        try (var insert =
            connection.prepareStatement(
                "insert into"
                    + " relation(relation_id,relation_type_id,source_object_id,target_object_id)"
                    + " values (?,?,?,?)")) {
          insert.setObject(1, relationId);
          insert.setObject(2, typeId);
          insert.setObject(3, source);
          insert.setObject(4, target);
          insert.executeUpdate();
        }
      }
      return relationId;
    }
    if (node instanceof Entity
        || node instanceof Capability
        || node instanceof State
        || node instanceof Concept) {
      return saveObject(connection, node);
    }
    throw new IllegalArgumentException(
        "unsupported canonical object at this persistence boundary: " + node.kind());
  }

  private UUID saveStatement(Connection connection, Statement statement) throws SQLException {
    var statementId = saveObject(connection, statement);
    if (objectId(
            connection, "select statement_id from statement where statement_id = ?", statementId)
        != null) {
      return statementId;
    }
    var subjectId = save(connection, statement.subject());
    UUID objectId = statement.object() instanceof Node node ? save(connection, node) : null;
    try (var insert =
        connection.prepareStatement(
            "insert into statement(statement_id,knowledge_kind,predicate,subject_object_id,"
                + "object_object_id,typed_value) values (?,?,?,?,?,?)")) {
      insert.setObject(1, statementId);
      insert.setString(2, statement.knowledgeKind().name());
      insert.setString(3, statement.predicate());
      insert.setObject(4, subjectId);
      insert.setObject(5, objectId);
      insert.setString(6, objectId == null ? "\"" + statement.object() + "\"" : null);
      insert.executeUpdate();
    }
    saveContext(connection, statementId, statement);
    saveDerivations(connection, statementId, statement);
    return statementId;
  }

  private void saveContext(Connection connection, UUID statementId, Statement statement)
      throws SQLException {
    try (var insert =
        connection.prepareStatement(
            "insert into statement_context(context_id,statement_id,provenance_reference,"
                + "confidence,scenario) values (?,?,?,?,?)")) {
      insert.setObject(1, UUID.randomUUID());
      insert.setObject(2, statementId);
      insert.setString(3, statement.context().provenance());
      insert.setObject(4, statement.context().confidence());
      insert.setString(5, statement.context().validAt());
      insert.executeUpdate();
    }
  }

  private void saveDerivations(Connection connection, UUID statementId, Statement statement)
      throws SQLException {
    for (var evidence : statement.derivedFrom()) {
      var evidenceId =
          objectId(
              connection,
              "select object_id from canonical_object where scope_id = ? and identity_key = ?",
              scopeId,
              evidence);
      if (evidenceId == null) {
        throw new PersistenceException(
            "derivation evidence is not in the current scope: " + evidence);
      }
      try (var insert =
          connection.prepareStatement(
              "insert into derivation(derived_statement_id,evidence_object_id,rule_identifier)"
                  + " values (?,?,?)")) {
        insert.setObject(1, statementId);
        insert.setObject(2, evidenceId);
        insert.setString(3, statement.predicate());
        insert.executeUpdate();
      }
    }
  }

  private UUID saveObject(Connection connection, Node node) throws SQLException {
    var existing =
        objectId(
            connection,
            "select object_id from canonical_object where scope_id = ? and identity_key = ?",
            scopeId,
            node.id());
    if (existing != null) {
      return existing;
    }
    var id = UUID.randomUUID();
    try (var insert =
        connection.prepareStatement(
            "insert into canonical_object(object_id,scope_id,identity_key,kind) values"
                + " (?,?,?,?)")) {
      insert.setObject(1, id);
      insert.setObject(2, scopeId);
      insert.setString(3, node.id());
      insert.setString(4, node.kind().name());
      insert.executeUpdate();
    }
    return id;
  }

  private UUID saveType(Connection connection, RelationType type) throws SQLException {
    var existing =
        objectId(
            connection,
            "select relation_type_id from relation_type where semantic_identifier = ? and"
                + " semantic_version = ?",
            type.id(),
            VERSION);
    if (existing != null) {
      return existing;
    }
    var id = UUID.randomUUID();
    try (var insert =
        connection.prepareStatement(
            "insert into"
                + " relation_type(relation_type_id,semantic_identifier,semantic_version,"
                + "source_profile,target_profile,inverse_identifier,is_symmetric,inference_policy)"
                + " values (?,?,?,?,?,?,?,?)")) {
      insert.setObject(1, id);
      insert.setString(2, type.id());
      insert.setString(3, VERSION);
      insert.setString(4, profile(type.sources()));
      insert.setString(5, profile(type.targets()));
      insert.setString(6, type.inverse());
      insert.setBoolean(7, type.symmetric());
      insert.setString(8, "{\"composable\":" + type.composable() + "}");
      insert.executeUpdate();
    }
    return id;
  }

  private Node findNode(Connection connection, UUID objectId) throws SQLException {
    try (var query =
        connection.prepareStatement(
            "select identity_key,kind from canonical_object where object_id = ? and scope_id ="
                + " ?")) {
      query.setObject(1, objectId);
      query.setObject(2, scopeId);
      try (var result = query.executeQuery()) {
        if (!result.next()) {
          throw new PersistenceException(
              "referenced canonical object is outside the current scope");
        }
        var identity = result.getString(1);
        return switch (Kind.valueOf(result.getString(2))) {
          case ENTITY -> new Entity(identity);
          case CAPABILITY -> new Capability(identity);
          case STATE -> new State(identity);
          case CONCEPT -> new Concept(identity);
          case RELATION -> findRelation(identity);
          default -> throw new PersistenceException("unsupported relation endpoint kind");
        };
      }
    }
  }

  private static UUID objectId(Connection connection, String sql, Object... values)
      throws SQLException {
    try (var query = connection.prepareStatement(sql)) {
      for (int i = 0; i < values.length; i++) {
        query.setObject(i + 1, values[i]);
      }
      try (var result = query.executeQuery()) {
        return result.next() ? (UUID) result.getObject(1) : null;
      }
    }
  }

  private static String profile(Set<Kind> kinds) {
    return kinds.stream()
        .map(kind -> "\"" + kind.name() + "\"")
        .collect(java.util.stream.Collectors.joining(",", "[", "]"));
  }

  private static Set<Kind> kinds(String profile) {
    var values =
        profile
            .replace("[", "")
            .replace("]", "")
            .replace("\"", "")
            .replace("\\", "")
            .replaceAll("\\s", "");
    if (values.isEmpty()) {
      return Set.of();
    }
    return Arrays.stream(values.split(","))
        .map(Kind::valueOf)
        .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
  }

  public static final class PersistenceException extends RuntimeException {
    PersistenceException(Throwable cause) {
      super(cause);
    }

    PersistenceException(String message) {
      super(message);
    }
  }
}
