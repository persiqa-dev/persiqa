package com.persiqa.persistence;

import com.persiqa.model.Ckm.Capability;
import com.persiqa.model.Ckm.Concept;
import com.persiqa.model.Ckm.Entity;
import com.persiqa.model.Ckm.Kind;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.RelationType;
import com.persiqa.model.Ckm.State;
import java.util.Set;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/** Persists canonical nodes and relations using transactional, named-parameter JDBC operations. */
public final class JdbcCanonicalStore {
  private static final String VERSION = "0.1";
  private final UUID scopeId;
  private final NamedParameterJdbcTemplate jdbc;
  private final TransactionTemplate transactions;

  public JdbcCanonicalStore(DataSource dataSource, UUID scopeId) {
    this.scopeId = scopeId;
    this.jdbc = new NamedParameterJdbcTemplate(dataSource);
    this.transactions = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
  }

  public void createScope(String name) {
    jdbc.update(
        "insert into model_scope(scope_id,name) select :scopeId,:name "
            + "where not exists (select 1 from model_scope where scope_id=:scopeId)",
        parameters("scopeId", scopeId, "name", name));
  }

  public UUID save(Node node) {
    return transactions.execute(status -> saveNode(node));
  }

  public Relation findRelation(String identityKey) {
    return jdbc.query(
        "select r.source_object_id,r.target_object_id,t.semantic_identifier,t.is_symmetric,"
            + "t.inverse_identifier,t.inference_policy from relation r "
            + "join canonical_object o on o.object_id=r.relation_id "
            + "join relation_type t on t.relation_type_id=r.relation_type_id "
            + "where o.scope_id=:scopeId and o.identity_key=:identityKey",
        parameters("scopeId", scopeId, "identityKey", identityKey),
        result -> {
          if (!result.next()) {
            return null;
          }
          var type =
              new RelationType(
                  result.getString("semantic_identifier"),
                  Set.of(Kind.ENTITY),
                  Set.of(Kind.ENTITY),
                  result.getBoolean("is_symmetric"),
                  result.getString("inverse_identifier"),
                  result.getString("inference_policy").contains("true"));
          return new Relation(
              identityKey,
              type,
              node(result.getObject("source_object_id", UUID.class)),
              node(result.getObject("target_object_id", UUID.class)));
        });
  }

  private UUID saveNode(Node node) {
    if (node instanceof Relation relation) {
      var relationId = objectId(relation);
      var sourceId = saveNode(relation.source());
      var targetId = saveNode(relation.target());
      var typeId = relationTypeId(relation.type());
      jdbc.update(
          "insert into relation(relation_id,relation_type_id,source_object_id,target_object_id) "
              + "select :relationId,:typeId,:sourceId,:targetId where not exists "
              + "(select 1 from relation where relation_id=:relationId)",
          parameters(
              "relationId",
              relationId,
              "typeId",
              typeId,
              "sourceId",
              sourceId,
              "targetId",
              targetId));
      return relationId;
    }
    if (node instanceof Entity
        || node instanceof Capability
        || node instanceof State
        || node instanceof Concept) {
      return objectId(node);
    }
    throw new IllegalArgumentException("unsupported persistence node: " + node.kind());
  }

  private UUID objectId(Node node) {
    var existing =
        jdbc.query(
            "select object_id from canonical_object where scope_id=:scopeId "
                + "and identity_key=:identityKey",
            parameters("scopeId", scopeId, "identityKey", node.id()),
            result -> result.next() ? result.getObject("object_id", UUID.class) : null);
    if (existing != null) {
      return existing;
    }
    var objectId = UUID.randomUUID();
    jdbc.update(
        "insert into canonical_object(object_id,scope_id,identity_key,kind) "
            + "values (:objectId,:scopeId,:identityKey,:kind)",
        parameters(
            "objectId",
            objectId,
            "scopeId",
            scopeId,
            "identityKey",
            node.id(),
            "kind",
            node.kind().name()));
    return objectId;
  }

  private UUID relationTypeId(RelationType type) {
    var existing =
        jdbc.query(
            "select relation_type_id from relation_type where semantic_identifier=:identifier "
                + "and semantic_version=:version",
            parameters("identifier", type.id(), "version", VERSION),
            result -> result.next() ? result.getObject("relation_type_id", UUID.class) : null);
    if (existing != null) {
      return existing;
    }
    var typeId = UUID.randomUUID();
    jdbc.update(
        "insert into relation_type(relation_type_id,semantic_identifier,semantic_version,"
            + "source_profile,target_profile,inverse_identifier,is_symmetric,inference_policy) "
            + "values (:typeId,:identifier,:version,:sourceProfile,:targetProfile,"
            + ":inverse,:symmetric,:policy)",
        parameters(
            "typeId",
            typeId,
            "identifier",
            type.id(),
            "version",
            VERSION,
            "sourceProfile",
            "{}",
            "targetProfile",
            "{}",
            "inverse",
            type.inverse(),
            "symmetric",
            type.symmetric(),
            "policy",
            "{\"composable\":" + type.composable() + "}"));
    return typeId;
  }

  private Node node(UUID objectId) {
    return jdbc.query(
        "select identity_key,kind from canonical_object where object_id=:objectId "
            + "and scope_id=:scopeId",
        parameters("objectId", objectId, "scopeId", scopeId),
        result -> {
          if (!result.next()) {
            throw new IllegalStateException("missing canonical object: " + objectId);
          }
          var id = result.getString("identity_key");
          return switch (Kind.valueOf(result.getString("kind"))) {
            case ENTITY -> new Entity(id);
            case CAPABILITY -> new Capability(id);
            case STATE -> new State(id);
            case CONCEPT -> new Concept(id);
            default -> throw new IllegalStateException("unsupported relation endpoint kind");
          };
        });
  }

  private static MapSqlParameterSource parameters(Object... entries) {
    var parameters = new MapSqlParameterSource();
    for (var index = 0; index < entries.length; index += 2) {
      parameters.addValue((String) entries[index], entries[index + 1]);
    }
    return parameters;
  }
}
