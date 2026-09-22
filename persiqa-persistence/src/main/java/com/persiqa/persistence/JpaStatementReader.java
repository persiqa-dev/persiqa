package com.persiqa.persistence;

import com.persiqa.model.Ckm.Capability;
import com.persiqa.model.Ckm.Concept;
import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.Entity;
import com.persiqa.model.Ckm.Kind;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.State;
import com.persiqa.model.Ckm.Statement;
import com.persiqa.persistence.entity.CanonicalObjectEntity;
import com.persiqa.persistence.entity.StatementContextEntity;
import com.persiqa.persistence.entity.StatementEntity;
import com.persiqa.persistence.json.TypedJsonValue;
import com.persiqa.persistence.repository.DerivationRepository;
import com.persiqa.persistence.repository.StatementContextRepository;
import com.persiqa.persistence.repository.StatementRepository;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/** Reconstructs statement collections from preloaded persistence rows. */
final class JpaStatementReader {
  private final StatementRepository statements;
  private final StatementContextRepository contexts;
  private final DerivationRepository derivations;
  private final JpaRelationReader relationReader;
  private final JpaObjectGraphLoader objectGraphLoader;

  JpaStatementReader(
      StatementRepository statements,
      StatementContextRepository contexts,
      DerivationRepository derivations,
      JpaRelationReader relationReader,
      JpaObjectGraphLoader objectGraphLoader) {
    this.statements = statements;
    this.contexts = contexts;
    this.derivations = derivations;
    this.relationReader = relationReader;
    this.objectGraphLoader = objectGraphLoader;
  }

  List<Statement> readSelected(UUID scopeId, Set<UUID> statementIds) {
    var statementRows = statements.findAllById(statementIds);
    var roots = new java.util.LinkedHashSet<UUID>(statementIds);
    statementRows.forEach(statement -> {
      roots.add(statement.subjectObjectId());
      if (statement.objectObjectId() != null) {
        roots.add(statement.objectObjectId());
      }
    });
    derivations.findByStatementIdIn(List.copyOf(statementIds))
        .forEach(link -> roots.add(link.evidenceObjectId()));
    return read(scopeId, objectGraphLoader.load(scopeId, roots), statementIds);
  }

  List<Statement> read(UUID scopeId, List<CanonicalObjectEntity> objects) {
    return read(scopeId, objects, null);
  }

  List<Statement> read(
      UUID scopeId, List<CanonicalObjectEntity> objects, Set<UUID> selectedStatementIds) {
    var objectsById = objects.stream().collect(java.util.stream.Collectors.toMap(
        CanonicalObjectEntity::id, object -> object));
    var statementObjects = objects.stream()
        .filter(object -> Kind.STATEMENT.name().equals(object.kind()))
        .filter(
            object ->
                selectedStatementIds == null || selectedStatementIds.contains(object.id()))
        .toList();
    var statementIds = statementObjects.stream().map(CanonicalObjectEntity::id).toList();
    var statementsById = statements.findAllById(statementIds).stream()
        .collect(java.util.stream.Collectors.toMap(StatementEntity::id, statement -> statement));
    var contextsByStatement =
        contexts.findByStatementIdInOrderByRecordedAtAscIdAsc(statementIds).stream()
            .collect(java.util.stream.Collectors.groupingBy(StatementContextEntity::statementId));
    var evidenceByStatement = derivations.findByStatementIdIn(statementIds).stream()
        .collect(java.util.stream.Collectors.groupingBy(link -> link.statementId()));
    var relations = relationReader.read(scopeId, objects).stream()
        .collect(java.util.stream.Collectors.toMap(Relation::id, relation -> relation));
    return statementObjects.stream().map(object -> statement(
        object, statementsById.get(object.id()), objectsById, relations,
        contextsByStatement.get(object.id()), evidenceByStatement.get(object.id()))).toList();
  }

  private static Statement statement(
      CanonicalObjectEntity object,
      StatementEntity statement,
      Map<UUID, CanonicalObjectEntity> objects,
      Map<String, Relation> relations,
      List<StatementContextEntity> contexts,
      List<com.persiqa.persistence.entity.DerivationEntity> derivations) {
    Object target = statement.objectObjectId() == null
        ? TypedJsonValue.read(statement.typedValue())
        : node(objects.get(statement.objectObjectId()), relations);
    var evidence = derivations == null ? Set.<String>of() : derivations.stream()
        .map(link -> objects.get(link.evidenceObjectId()).identityKey())
        .collect(java.util.stream.Collectors.toUnmodifiableSet());
    var context = contexts == null || contexts.isEmpty()
        ? Context.unspecified()
        : context(contexts.getFirst());
    return new Statement(
        object.identityKey(),
        com.persiqa.model.Ckm.KnowledgeKind.valueOf(statement.knowledgeKind()),
        statement.predicate(),
        node(objects.get(statement.subjectObjectId()), relations),
        target,
        evidence,
        context);
  }

  private static Node node(CanonicalObjectEntity object, Map<String, Relation> relations) {
    return switch (Kind.valueOf(object.kind())) {
      case ENTITY -> new Entity(object.identityKey());
      case CAPABILITY -> new Capability(object.identityKey());
      case CONCEPT -> new Concept(object.identityKey());
      case STATE -> new State(object.identityKey());
      case RELATION -> relations.get(object.identityKey());
      case STATEMENT, TYPED_VALUE ->
          throw new IllegalStateException("unsupported statement endpoint");
    };
  }

  private static Context context(StatementContextEntity entity) {
    return new Context(entity.provenanceReference(), entity.confidence(), entity.observedAt(),
        entity.validFrom(), entity.validTo(), entity.scenario());
  }
}
