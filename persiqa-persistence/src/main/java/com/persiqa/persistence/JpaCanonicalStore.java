package com.persiqa.persistence;

import com.persiqa.model.Ckm.Capability;
import com.persiqa.model.Ckm.Concept;
import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.Entity;
import com.persiqa.model.Ckm.Kind;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.RelationType;
import com.persiqa.model.Ckm.State;
import com.persiqa.model.Ckm.Statement;
import com.persiqa.persistence.entity.CanonicalObjectEntity;
import com.persiqa.persistence.entity.CanonicalizationEntity;
import com.persiqa.persistence.entity.DerivationEntity;
import com.persiqa.persistence.entity.ModelScopeEntity;
import com.persiqa.persistence.entity.RelationEntity;
import com.persiqa.persistence.entity.RelationTypeEntity;
import com.persiqa.persistence.entity.RepresentationEntity;
import com.persiqa.persistence.entity.StateEntity;
import com.persiqa.persistence.entity.StatementContextEntity;
import com.persiqa.persistence.entity.StatementEntity;
import com.persiqa.persistence.repository.CanonicalObjectRepository;
import com.persiqa.persistence.repository.CanonicalizationRepository;
import com.persiqa.persistence.repository.DerivationRepository;
import com.persiqa.persistence.repository.ModelScopeRepository;
import com.persiqa.persistence.repository.RelationRepository;
import com.persiqa.persistence.repository.RelationTypeRepository;
import com.persiqa.persistence.repository.RepresentationRepository;
import com.persiqa.persistence.repository.StateRepository;
import com.persiqa.persistence.repository.StatementContextRepository;
import com.persiqa.persistence.repository.StatementRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA implementation of the PAS-010 persistence boundary for one CKM model scope.
 *
 * <p>It persists canonical objects separately from Statements and their append-preserved context,
 * derivation, and canonicalization records. It intentionally does not infer missing knowledge.
 */
@Service
public class JpaCanonicalStore {
  private static final String RELATION_TYPE_VERSION = "0.1";
  private static final String DEFAULT_DERIVATION_RULE_PREFIX = "relation-type:";

  private final CanonicalObjectRepository objects;
  private final ModelScopeRepository scopes;
  private final RelationTypeRepository relationTypes;
  private final RelationRepository relations;
  private final StateRepository states;
  private final StatementRepository statements;
  private final StatementContextRepository contexts;
  private final DerivationRepository derivations;
  private final CanonicalizationRepository canonicalizations;
  private final RepresentationRepository representations;

  public JpaCanonicalStore(
      CanonicalObjectRepository objects,
      ModelScopeRepository scopes,
      RelationTypeRepository relationTypes,
      RelationRepository relations,
      StateRepository states,
      StatementRepository statements,
      StatementContextRepository contexts,
      DerivationRepository derivations,
      CanonicalizationRepository canonicalizations,
      RepresentationRepository representations) {
    this.objects = objects;
    this.scopes = scopes;
    this.relationTypes = relationTypes;
    this.relations = relations;
    this.states = states;
    this.statements = statements;
    this.contexts = contexts;
    this.derivations = derivations;
    this.canonicalizations = canonicalizations;
    this.representations = representations;
  }

  /** Creates the scope when it does not exist and rejects a renamed existing scope. */
  @Transactional
  public void createScope(UUID scopeId, String name) {
    scopes
        .findById(scopeId)
        .ifPresentOrElse(
            existing -> {
              if (!existing.name().equals(name)) {
                throw new IllegalArgumentException("scope identity cannot be renamed");
              }
            },
            () -> scopes.save(new ModelScopeEntity(scopeId, name)));
  }

  /** Persists a Node and returns its storage identifier without changing its CKM identity. */
  @Transactional
  public UUID save(UUID scopeId, Node node) {
    requireScope(scopeId);
    return saveNode(scopeId, node);
  }

  /** Persists a traceable relation assertion result without collapsing either record. */
  @Transactional
  public void canonicalize(
      UUID scopeId,
      Statement statement,
      Node canonicalObject,
      String mode,
      String policyIdentifier) {
    var statementId = saveNode(scopeId, statement);
    var objectId = saveNode(scopeId, canonicalObject);
    var key = new CanonicalizationEntity.Key(statementId, objectId, mode);
    if (!canonicalizations.existsById(key)) {
      canonicalizations.save(
          new CanonicalizationEntity(statementId, objectId, mode, policyIdentifier));
    }
  }

  /** Appends an independent knowledge context without mutating the Statement assertion. */
  @Transactional
  public void appendContext(UUID scopeId, String statementIdentity, Context context) {
    var statementObject = object(scopeId, statementIdentity);
    if (statementObject == null || Kind.valueOf(statementObject.kind()) != Kind.STATEMENT) {
      throw new IllegalArgumentException("unknown Statement: " + statementIdentity);
    }
    contexts.save(contextEntity(statementObject.id(), context));
  }

  /** Returns every append-preserved context record for one Statement. */
  @Transactional(readOnly = true)
  public List<Context> findContexts(UUID scopeId, String statementIdentity) {
    var statementObject = object(scopeId, statementIdentity);
    if (statementObject == null || Kind.valueOf(statementObject.kind()) != Kind.STATEMENT) {
      throw new IllegalArgumentException("unknown Statement: " + statementIdentity);
    }
    return contexts.findByStatementIdOrderById(statementObject.id()).stream()
        .map(JpaCanonicalStore::context)
        .toList();
  }

  /** Persists presentation metadata without attaching it to canonical object identity. */
  @Transactional
  public void saveRepresentation(
      UUID scopeId,
      UUID representationId,
      String name,
      String selectionDefinition,
      String layoutMetadata) {
    requireScope(scopeId);
    representations
        .findById(representationId)
        .ifPresentOrElse(
            existing -> {
              throw new IllegalArgumentException("representation identity cannot be overwritten");
            },
            () ->
                representations.save(
                    new RepresentationEntity(
                        representationId, scopeId, name, selectionDefinition, layoutMetadata)));
  }

  /** Reconstructs an independently identified Relation and its full persisted type contract. */
  @Transactional(readOnly = true)
  public Relation findRelation(UUID scopeId, String identityKey) {
    var object = object(scopeId, identityKey);
    if (object == null || Kind.valueOf(object.kind()) != Kind.RELATION) {
      return null;
    }
    var relation = relations.findById(object.id()).orElseThrow();
    var type = relationTypes.findById(relation.relationTypeId()).orElseThrow();
    return new Relation(
        identityKey,
        relationType(type),
        node(scopeId, relation.sourceObjectId()),
        node(scopeId, relation.targetObjectId()));
  }

  /** Reconstructs a Statement together with one persisted context record. */
  @Transactional(readOnly = true)
  public Statement findStatement(UUID scopeId, String identityKey) {
    var object = object(scopeId, identityKey);
    if (object == null || Kind.valueOf(object.kind()) != Kind.STATEMENT) {
      return null;
    }
    var statement = statements.findById(object.id()).orElseThrow();
    var context = contexts.findByStatementIdOrderById(statement.id()).stream().findFirst();
    var evidence =
        derivations.findByStatementId(statement.id()).stream()
            .map(link -> objectIdentity(scopeId, link.evidenceObjectId()))
            .collect(Collectors.toUnmodifiableSet());
    Object statementObject =
        statement.objectObjectId() == null
            ? decodeValue(statement.typedValue())
            : node(scopeId, statement.objectObjectId());
    return new Statement(
        identityKey,
        com.persiqa.model.Ckm.KnowledgeKind.valueOf(statement.knowledgeKind()),
        statement.predicate(),
        node(scopeId, statement.subjectObjectId()),
        statementObject,
        evidence,
        context.map(JpaCanonicalStore::context).orElseGet(Context::unspecified));
  }

  private UUID saveNode(UUID scopeId, Node node) {
    if (node instanceof Relation relation) {
      return saveRelation(scopeId, relation);
    }
    if (node instanceof Statement statement) {
      return saveStatement(scopeId, statement);
    }
    return objectId(scopeId, node);
  }

  private UUID saveRelation(UUID scopeId, Relation relation) {
    var relationId = objectId(scopeId, relation);
    var sourceId = saveNode(scopeId, relation.source());
    var targetId = saveNode(scopeId, relation.target());
    var typeId = relationTypeId(relation.type());
    relations
        .findById(relationId)
        .ifPresentOrElse(
            existing -> verifyRelation(existing, typeId, sourceId, targetId),
            () -> relations.save(new RelationEntity(relationId, typeId, sourceId, targetId)));
    if (relation.type().id().equals("hasState") && relation.target() instanceof State state) {
      if (relation.source().kind() != Kind.ENTITY && relation.source().kind() != Kind.RELATION) {
        throw new IllegalArgumentException("State owner must be an Entity or Relation");
      }
      states
          .findById(targetId)
          .orElseGet(
              () ->
                  states.save(
                      new StateEntity(targetId, sourceId, "hasState", quote(state.id()), "{}")));
    }
    return relationId;
  }

  private UUID saveStatement(UUID scopeId, Statement statement) {
    var statementId = objectId(scopeId, statement);
    var subjectId = saveNode(scopeId, statement.subject());
    UUID objectId = statement.object() instanceof Node node ? saveNode(scopeId, node) : null;
    String typedValue = objectId == null ? encodeValue(statement.object()) : null;
    statements
        .findById(statementId)
        .ifPresentOrElse(
            existing -> verifyStatement(existing, statement, subjectId, objectId, typedValue),
            () ->
                statements.save(
                    new StatementEntity(
                        statementId,
                        statement.knowledgeKind().name(),
                        statement.predicate(),
                        subjectId,
                        objectId,
                        typedValue)));
    if (contexts.findByStatementIdOrderById(statementId).isEmpty()) {
      contexts.save(contextEntity(statementId, statement.context()));
    }
    if (statement.knowledgeKind() == com.persiqa.model.Ckm.KnowledgeKind.DERIVED) {
      for (var evidenceIdentity : statement.derivedFrom()) {
        var evidence = object(scopeId, evidenceIdentity);
        if (evidence == null) {
          throw new IllegalArgumentException(
              "derived Statement evidence is not persisted: " + evidenceIdentity);
        }
        var key =
            new DerivationEntity.Key(
                statementId, evidence.id(), DEFAULT_DERIVATION_RULE_PREFIX + statement.predicate());
        if (!derivations.existsById(key)) {
          derivations.save(
              new DerivationEntity(
                  statementId,
                  evidence.id(),
                  DEFAULT_DERIVATION_RULE_PREFIX + statement.predicate()));
        }
      }
    }
    return statementId;
  }

  private UUID objectId(UUID scopeId, Node node) {
    var existing = object(scopeId, node.id());
    if (existing != null) {
      if (!existing.kind().equals(node.kind().name())) {
        throw new IllegalArgumentException("canonical identity cannot change kind: " + node.id());
      }
      return existing.id();
    }
    var id = UUID.randomUUID();
    objects.save(new CanonicalObjectEntity(id, scopeId, node.id(), node.kind().name()));
    return id;
  }

  private UUID relationTypeId(RelationType type) {
    return relationTypes
        .findByIdentifierAndVersion(type.id(), RELATION_TYPE_VERSION)
        .map(existing -> verifyRelationType(existing, type))
        .orElseGet(
            () ->
                relationTypes
                    .save(
                        new RelationTypeEntity(
                            UUID.randomUUID(),
                            type.id(),
                            RELATION_TYPE_VERSION,
                            kinds(type.sources()),
                            kinds(type.targets()),
                            type.inverse(),
                            type.symmetric(),
                            "{\"composable\":" + type.composable() + "}"))
                    .id());
  }

  private UUID verifyRelationType(RelationTypeEntity existing, RelationType requested) {
    if (!existing.sourceProfile().equals(kinds(requested.sources()))
        || !existing.targetProfile().equals(kinds(requested.targets()))
        || !java.util.Objects.equals(existing.inverseIdentifier(), requested.inverse())
        || existing.symmetric() != requested.symmetric()
        || !existing.inferencePolicy().equals("{\"composable\":" + requested.composable() + "}")) {
      throw new IllegalArgumentException(
          "Relation Type contract cannot be silently redefined: " + requested.id());
    }
    return existing.id();
  }

  private static RelationType relationType(RelationTypeEntity entity) {
    return new RelationType(
        entity.identifier(),
        parseKinds(entity.sourceProfile()),
        parseKinds(entity.targetProfile()),
        entity.symmetric(),
        entity.inverseIdentifier(),
        entity.inferencePolicy().contains("\"composable\":true"));
  }

  private static Set<Kind> parseKinds(String json) {
    if (json.length() < 3) {
      return Set.of();
    }
    return Arrays.stream(json.substring(1, json.length() - 1).split(","))
        .filter(value -> !value.isBlank())
        .map(value -> value.replace("\"", ""))
        .map(Kind::valueOf)
        .collect(Collectors.toUnmodifiableSet());
  }

  private static String kinds(Set<Kind> kinds) {
    return kinds.stream()
        .map(Kind::name)
        .sorted()
        .map(JpaCanonicalStore::quote)
        .collect(Collectors.joining(",", "[", "]"));
  }

  private static StatementContextEntity contextEntity(UUID statementId, Context context) {
    return new StatementContextEntity(
        UUID.randomUUID(),
        statementId,
        context.provenance(),
        context.confidence() == null ? null : java.math.BigDecimal.valueOf(context.confidence()),
        context.observedAt(),
        context.validFrom(),
        context.validTo(),
        context.scenario());
  }

  private static Context context(StatementContextEntity entity) {
    return new Context(
        entity.provenanceReference(),
        entity.confidence() == null ? null : entity.confidence().doubleValue(),
        entity.observedAt(),
        entity.validFrom(),
        entity.validTo(),
        entity.scenario());
  }

  private Node node(UUID scopeId, UUID objectId) {
    var object = objects.findById(objectId).orElseThrow();
    if (!object.scopeId().equals(scopeId)) {
      throw new IllegalStateException("cross-scope canonical object reference");
    }
    return switch (Kind.valueOf(object.kind())) {
      case ENTITY -> new Entity(object.identityKey());
      case CAPABILITY -> new Capability(object.identityKey());
      case CONCEPT -> new Concept(object.identityKey());
      case STATE -> new State(object.identityKey());
      case RELATION -> findRelation(scopeId, object.identityKey());
      case STATEMENT, TYPED_VALUE ->
          throw new IllegalStateException("unsupported endpoint kind: " + object.kind());
    };
  }

  private CanonicalObjectEntity object(UUID scopeId, String identityKey) {
    return objects.findByScopeIdAndIdentityKey(scopeId, identityKey).orElse(null);
  }

  private String objectIdentity(UUID scopeId, UUID objectId) {
    var object = objects.findById(objectId).orElseThrow();
    if (!object.scopeId().equals(scopeId)) {
      throw new IllegalStateException("cross-scope derivation evidence");
    }
    return object.identityKey();
  }

  private void requireScope(UUID scopeId) {
    if (!scopes.existsById(scopeId)) {
      throw new IllegalArgumentException("unknown model scope: " + scopeId);
    }
  }

  private static void verifyRelation(
      RelationEntity existing, UUID typeId, UUID sourceId, UUID targetId) {
    if (!existing.relationTypeId().equals(typeId)
        || !existing.sourceObjectId().equals(sourceId)
        || !existing.targetObjectId().equals(targetId)) {
      throw new IllegalArgumentException("Relation identity cannot be overwritten");
    }
  }

  private static void verifyStatement(
      StatementEntity existing,
      Statement requested,
      UUID subjectId,
      UUID objectId,
      String typedValue) {
    if (!existing.knowledgeKind().equals(requested.knowledgeKind().name())
        || !existing.predicate().equals(requested.predicate())
        || !existing.subjectObjectId().equals(subjectId)
        || !java.util.Objects.equals(existing.objectObjectId(), objectId)
        || !java.util.Objects.equals(existing.typedValue(), typedValue)) {
      throw new IllegalArgumentException("Statement identity cannot be overwritten");
    }
  }

  private static String encodeValue(Object value) {
    if (value instanceof String string) {
      return quote(string);
    }
    if (value instanceof Number || value instanceof Boolean) {
      return value.toString();
    }
    throw new IllegalArgumentException("typed Statement values must be String, Number, or Boolean");
  }

  private static Object decodeValue(String json) {
    if (json.startsWith("\"") && json.endsWith("\"")) {
      return json.substring(1, json.length() - 1).replace("\\\"", "\"").replace("\\\\", "\\");
    }
    if ("true".equals(json) || "false".equals(json)) {
      return Boolean.valueOf(json);
    }
    return new java.math.BigDecimal(json);
  }

  private static String quote(String value) {
    return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
  }
}
