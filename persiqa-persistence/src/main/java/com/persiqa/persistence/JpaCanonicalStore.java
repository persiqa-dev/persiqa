package com.persiqa.persistence;

import com.persiqa.core.CanonicalStore;
import com.persiqa.core.ModelScope;
import com.persiqa.core.PageQuery;
import com.persiqa.core.PageResult;
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
import com.persiqa.persistence.json.InferencePolicy;
import com.persiqa.persistence.json.TypedJsonValue;
import com.persiqa.persistence.repository.CanonicalObjectRepository;
import com.persiqa.persistence.repository.CanonicalizationRepository;
import com.persiqa.persistence.repository.DerivationRepository;
import com.persiqa.persistence.repository.IdentityCounterRepository;
import com.persiqa.persistence.repository.ModelScopeRepository;
import com.persiqa.persistence.repository.RelationRepository;
import com.persiqa.persistence.repository.RelationTypeRepository;
import com.persiqa.persistence.repository.RepresentationRepository;
import com.persiqa.persistence.repository.StateRepository;
import com.persiqa.persistence.repository.StatementContextRepository;
import com.persiqa.persistence.repository.StatementRepository;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA implementation of the PAS-010 persistence boundary for one CKM model scope.
 *
 * <p>It persists canonical objects separately from Statements and their append-preserved context,
 * derivation, and canonicalization records. It intentionally does not infer missing knowledge.
 */
@Service
public class JpaCanonicalStore implements CanonicalStore {
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
  private final IdentityCounterRepository identityCounters;
  private final CanonicalizationRepository canonicalizations;
  private final RepresentationRepository representations;
  private final JpaRelationReader relationReader;
  private final JpaStatementReader statementReader;

  public JpaCanonicalStore(
      CanonicalObjectRepository objects,
      ModelScopeRepository scopes,
      RelationTypeRepository relationTypes,
      RelationRepository relations,
      StateRepository states,
      StatementRepository statements,
      StatementContextRepository contexts,
      DerivationRepository derivations,
      IdentityCounterRepository identityCounters,
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
    this.identityCounters = identityCounters;
    this.canonicalizations = canonicalizations;
    this.representations = representations;
    var objectGraphLoader = new JpaObjectGraphLoader(objects, relations);
    this.relationReader = new JpaRelationReader(relations, relationTypes, objectGraphLoader);
    this.statementReader =
        new JpaStatementReader(
            statements, contexts, derivations, relationReader, objectGraphLoader);
  }

  /** Creates the scope when it does not exist and rejects renamed or reassigned scopes. */
  @Override
  @Transactional
  public void createScope(UUID scopeId, String name, String ownerSubject) {
    scopes
        .findById(scopeId)
        .ifPresentOrElse(
            existing -> {
              if (!existing.name().equals(name)) {
                throw new IllegalArgumentException("scope identity cannot be renamed");
              }
              if (!existing.ownerSubject().equals(ownerSubject)) {
                throw new IllegalArgumentException("scope owner cannot be reassigned");
              }
            },
            () -> scopes.save(new ModelScopeEntity(scopeId, name, ownerSubject)));
  }

  /** Returns whether the persistence scope identity exists. */
  @Override
  @Transactional(readOnly = true)
  public boolean scopeExists(UUID scopeId) {
    return scopes.existsById(scopeId);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<ModelScope> findScope(UUID scopeId) {
    return scopes
        .findById(scopeId)
        .map(entity -> new ModelScope(entity.id(), entity.name(), entity.ownerSubject()));
  }

  /** Returns scopes owned by one subject in stable display order. */
  @Override
  @Transactional(readOnly = true)
  public List<ModelScope> findScopesByOwner(String ownerSubject) {
    return scopes.findByOwnerSubjectOrderByNameAscIdAsc(ownerSubject).stream()
        .map(entity -> new ModelScope(entity.id(), entity.name(), entity.ownerSubject()))
        .toList();
  }

  /** Returns one page of owner-visible scopes, optionally filtered by scope name. */
  @Override
  @Transactional(readOnly = true)
  public PageResult<ModelScope> findScopesByOwner(String ownerSubject, PageQuery pageQuery) {
    var pageable = PageRequest.of(pageQuery.page(), pageQuery.size());
    Page<ModelScopeEntity> page =
        pageQuery.query() == null
            ? scopes.findByOwnerSubjectOrderByNameAscIdAsc(ownerSubject, pageable)
            : scopes.findByOwnerSubjectAndNameContainingIgnoreCaseOrderByNameAscIdAsc(
                ownerSubject, pageQuery.query(), pageable);
    return page(page, entity -> new ModelScope(entity.id(), entity.name(), entity.ownerSubject()));
  }

  /** Allocates a unique, zero-padded ordinal while holding the scope's database write lock. */
  @Override
  @Transactional
  public long nextIdentityOrdinal(UUID scopeId, String identityPrefix) {
    if (identityPrefix == null || identityPrefix.isBlank()) {
      throw new IllegalArgumentException("identity prefix must not be blank");
    }
    scopes
        .findByIdForIdentityAllocation(scopeId)
        .orElseThrow(() -> new IllegalArgumentException("unknown model scope: " + scopeId));
    var counter = identityCounters.findOrCreate(scopeId, identityPrefix);
    long ordinal;
    do {
      ordinal = counter.allocate();
    } while (objects.existsByScopeIdAndIdentityKey(scopeId, identity(identityPrefix, ordinal)));
    return ordinal;
  }

  /** Reconstructs a standalone canonical Node, or returns {@code null} when it is unknown. */
  @Override
  @Transactional(readOnly = true)
  public Node findNode(UUID scopeId, String identityKey) {
    var object = object(scopeId, identityKey);
    if (object == null
        || Kind.valueOf(object.kind()) == Kind.RELATION
        || Kind.valueOf(object.kind()) == Kind.STATEMENT
        || Kind.valueOf(object.kind()) == Kind.TYPED_VALUE) {
      return null;
    }
    return node(scopeId, object.id());
  }

  /** Returns standalone canonical Nodes in one scope in stable identity order. */
  @Override
  @Transactional(readOnly = true)
  public List<Node> findNodes(UUID scopeId) {
    return objects.findByScopeIdOrderByIdentityKey(scopeId).stream()
        .filter(JpaCanonicalStore::isStandaloneNode)
        .map(object -> node(scopeId, object.id()))
        .toList();
  }

  /** Returns one page of standalone Nodes, optionally filtered by canonical identity. */
  @Override
  @Transactional(readOnly = true)
  public PageResult<Node> findNodes(UUID scopeId, PageQuery pageQuery) {
    requireScope(scopeId);
    var page =
        pageQuery.query() == null
            ? objects.findByScopeIdAndKindInOrderByIdentityKey(
                scopeId, standaloneNodeKinds(), pageable(pageQuery))
            : objects.findByScopeIdAndKindInAndIdentityKeyContainingIgnoreCaseOrderByIdentityKey(
                scopeId, standaloneNodeKinds(), pageQuery.query(), pageable(pageQuery));
    return page(page, object -> node(scopeId, object.id()));
  }

  @Override
  @Transactional(readOnly = true)
  public long countNodes(UUID scopeId) {
    return objects.countByScopeIdAndKindIn(scopeId, standaloneNodeKinds());
  }

  /** Persists a Node and returns its storage identifier without changing its CKM identity. */
  @Override
  @Transactional
  public UUID save(UUID scopeId, Node node) {
    requireScope(scopeId);
    return saveNode(scopeId, node);
  }

  /** Persists a traceable relation assertion result without collapsing either record. */
  @Override
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
  @Override
  @Transactional
  public void appendContext(UUID scopeId, String statementIdentity, Context context) {
    var statementObject = object(scopeId, statementIdentity);
    if (statementObject == null || Kind.valueOf(statementObject.kind()) != Kind.STATEMENT) {
      throw new IllegalArgumentException("unknown Statement: " + statementIdentity);
    }
    contexts.save(contextEntity(statementObject.id(), context));
  }

  /** Returns every append-preserved context record for one Statement. */
  @Override
  @Transactional(readOnly = true)
  public List<Context> findContexts(UUID scopeId, String statementIdentity) {
    var statementObject = object(scopeId, statementIdentity);
    if (statementObject == null || Kind.valueOf(statementObject.kind()) != Kind.STATEMENT) {
      throw new IllegalArgumentException("unknown Statement: " + statementIdentity);
    }
    return contexts.findByStatementIdOrderByRecordedAtAscIdAsc(statementObject.id()).stream()
        .map(JpaCanonicalStore::context)
        .toList();
  }

  /** Persists presentation metadata without attaching it to canonical object identity. */
  @Transactional
  public void saveRepresentation(
      UUID scopeId,
      UUID representationId,
      String name,
      Map<String, Object> selectionDefinition,
      Map<String, Object> layoutMetadata) {
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
  @Override
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

  /** Returns every canonical Relation in one scope in stable identity order. */
  @Override
  @Transactional(readOnly = true)
  public List<Relation> findRelations(UUID scopeId) {
    requireScope(scopeId);
    return relationReader.read(scopeId, objects.findByScopeIdOrderByIdentityKey(scopeId));
  }

  /** Returns one page of Relations, optionally filtered by canonical identity. */
  @Override
  @Transactional(readOnly = true)
  public PageResult<Relation> findRelations(UUID scopeId, PageQuery pageQuery) {
    requireScope(scopeId);
    var page =
        pageQuery.query() == null
            ? objects.findByScopeIdAndKindInOrderByIdentityKey(
                scopeId, Set.of(Kind.RELATION.name()), pageable(pageQuery))
            : objects.findByScopeIdAndKindAndIdentityKeyContainingIgnoreCaseOrderByIdentityKey(
                scopeId, Kind.RELATION.name(), pageQuery.query(), pageable(pageQuery));
    var relationsById = relationReader.readSelected(
            scopeId,
            page.getContent().stream().map(CanonicalObjectEntity::id).collect(Collectors.toSet()))
        .stream()
        .collect(Collectors.toMap(Relation::id, relation -> relation));
    return page(page, object -> relationsById.get(object.identityKey()));
  }

  @Override
  @Transactional(readOnly = true)
  public long countRelations(UUID scopeId) {
    return objects.countByScopeIdAndKind(scopeId, Kind.RELATION.name());
  }

  @Override
  @Transactional(readOnly = true)
  public List<Relation> findRelationsByType(UUID scopeId, String relationType) {
    requireScope(scopeId);
    var type = relationTypes.findByIdentifierAndVersion(relationType, RELATION_TYPE_VERSION);
    if (type.isEmpty()) {
      return List.of();
    }
    var relationIds = relations.findByRelationTypeId(type.get().id()).stream()
        .map(RelationEntity::id)
        .collect(Collectors.toSet());
    return relationReader.readSelected(scopeId, relationIds);
  }

  /**
   * Reconstructs a Statement with its original assertion context.
   *
   * <p>The assertion context is the earliest persisted context record. Appended observations remain
   * available through {@link #findContexts(UUID, String)} and do not replace that context.
   */
  @Override
  @Transactional(readOnly = true)
  public Statement findStatement(UUID scopeId, String identityKey) {
    var object = object(scopeId, identityKey);
    if (object == null || Kind.valueOf(object.kind()) != Kind.STATEMENT) {
      return null;
    }
    var statement = statements.findById(object.id()).orElseThrow();
    var assertionContext =
        contexts.findByStatementIdOrderByRecordedAtAscIdAsc(statement.id()).stream()
            .findFirst()
            .map(JpaCanonicalStore::context)
            .orElseGet(Context::unspecified);
    var evidence =
        derivations.findByStatementId(statement.id()).stream()
            .map(link -> objectIdentity(scopeId, link.evidenceObjectId()))
            .collect(Collectors.toUnmodifiableSet());
    Object statementObject =
        statement.objectObjectId() == null
            ? TypedJsonValue.read(statement.typedValue())
            : node(scopeId, statement.objectObjectId());
    return new Statement(
        identityKey,
        com.persiqa.model.Ckm.KnowledgeKind.valueOf(statement.knowledgeKind()),
        statement.predicate(),
        node(scopeId, statement.subjectObjectId()),
        statementObject,
        evidence,
        assertionContext);
  }

  /** Returns every Statement in one scope in stable identity order. */
  @Override
  @Transactional(readOnly = true)
  public List<Statement> findStatements(UUID scopeId) {
    requireScope(scopeId);
    return statementReader.read(scopeId, objects.findByScopeIdOrderByIdentityKey(scopeId));
  }

  /** Returns one page of Statements, optionally filtered by canonical identity. */
  @Override
  @Transactional(readOnly = true)
  public PageResult<Statement> findStatements(UUID scopeId, PageQuery pageQuery) {
    requireScope(scopeId);
    var page =
        pageQuery.query() == null
            ? objects.findByScopeIdAndKindInOrderByIdentityKey(
                scopeId, Set.of(Kind.STATEMENT.name()), pageable(pageQuery))
            : objects.findByScopeIdAndKindAndIdentityKeyContainingIgnoreCaseOrderByIdentityKey(
                scopeId, Kind.STATEMENT.name(), pageQuery.query(), pageable(pageQuery));
    var statementsById = statementReader.readSelected(
            scopeId,
            page.getContent().stream().map(CanonicalObjectEntity::id).collect(Collectors.toSet()))
        .stream()
        .collect(Collectors.toMap(Statement::id, statement -> statement));
    return page(page, object -> statementsById.get(object.identityKey()));
  }

  @Override
  @Transactional(readOnly = true)
  public long countStatements(UUID scopeId) {
    return objects.countByScopeIdAndKind(scopeId, Kind.STATEMENT.name());
  }

  /** Returns every Statement canonically associated with one Relation in stable identity order. */
  @Override
  @Transactional(readOnly = true)
  public List<Statement> findStatementsForRelation(UUID scopeId, String relationIdentity) {
    var relationObject = object(scopeId, relationIdentity);
    if (relationObject == null || Kind.valueOf(relationObject.kind()) != Kind.RELATION) {
      return List.of();
    }
    return canonicalizations.findByCanonicalObjectId(relationObject.id()).stream()
        .map(link -> objectIdentity(scopeId, link.statementId()))
        .map(statementIdentity -> findStatement(scopeId, statementIdentity))
        .sorted(java.util.Comparator.comparing(Statement::id))
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<Statement> findStatementsForRelations(UUID scopeId, List<Relation> relations) {
    if (relations.isEmpty()) {
      return List.of();
    }
    var relationObjectIds = objects.findByScopeIdAndIdentityKeyIn(
            scopeId, relations.stream().map(Relation::id).toList()).stream()
        .map(CanonicalObjectEntity::id)
        .toList();
    var statementIds = canonicalizations.findByCanonicalObjectIdIn(relationObjectIds).stream()
        .map(CanonicalizationEntity::statementId)
        .collect(Collectors.toSet());
    return statementReader.readSelected(scopeId, statementIds);
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
    validateRelationEndpoints(relation);
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
          .ifPresentOrElse(
              existing -> updateState(existing, sourceId, state),
              () ->
                  states.save(
                      new StateEntity(
                          targetId,
                          sourceId,
                          state.predicate(),
                          TypedJsonValue.write(state.value()),
                          Map.of())));
    }
    return relationId;
  }

  private static void validateRelationEndpoints(Relation relation) {
    if (!relation.type().sources().contains(relation.source().kind())) {
      throw new IllegalArgumentException(
          "Relation source kind is not allowed by its type: " + relation.type().id());
    }
    if (!relation.type().targets().contains(relation.target().kind())) {
      throw new IllegalArgumentException(
          "Relation target kind is not allowed by its type: " + relation.type().id());
    }
  }

  private static void updateState(StateEntity existing, UUID ownerObjectId, State state) {
    if (!existing.ownerObjectId().equals(ownerObjectId)) {
      throw new IllegalArgumentException("State belongs to a different owner: " + state.id());
    }
    if (!existing.predicate().equals(state.predicate())) {
      throw new IllegalArgumentException("State identity cannot change predicate: " + state.id());
    }
    existing.updateValue(TypedJsonValue.write(state.value()));
  }

  private UUID saveStatement(UUID scopeId, Statement statement) {
    var statementId = objectId(scopeId, statement);
    var subjectId = saveNode(scopeId, statement.subject());
    UUID objectId = statement.object() instanceof Node node ? saveNode(scopeId, node) : null;
    Object typedValue = objectId == null ? TypedJsonValue.write(statement.object()) : null;
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
    var persistedContexts = contexts.findByStatementIdOrderByRecordedAtAscIdAsc(statementId);
    if (persistedContexts.isEmpty()) {
      contexts.save(contextEntity(statementId, statement.context()));
    } else if (!equivalentContext(context(persistedContexts.getFirst()), statement.context())) {
      throw new IllegalArgumentException("Statement identity cannot be overwritten");
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
                            type.sources(),
                            type.targets(),
                            type.inverse(),
                            type.symmetric(),
                            new InferencePolicy(type.composable())))
                    .id());
  }

  private UUID verifyRelationType(RelationTypeEntity existing, RelationType requested) {
    if (!existing.sourceProfile().equals(requested.sources())
        || !existing.targetProfile().equals(requested.targets())
        || !Objects.equals(existing.inverseIdentifier(), requested.inverse())
        || existing.symmetric() != requested.symmetric()
        || !existing.inferencePolicy().equals(new InferencePolicy(requested.composable()))) {
      throw new IllegalArgumentException(
          "Relation Type contract cannot be silently redefined: " + requested.id());
    }
    return existing.id();
  }

  private static RelationType relationType(RelationTypeEntity entity) {
    return new RelationType(
        entity.identifier(),
        Set.copyOf(entity.sourceProfile()),
        Set.copyOf(entity.targetProfile()),
        entity.symmetric(),
        entity.inverseIdentifier(),
        entity.inferencePolicy().composable());
  }

  private StatementContextEntity contextEntity(UUID statementId, Context context) {
    return new StatementContextEntity(
        UUID.randomUUID(),
        statementId,
        nextRecordingTime(statementId),
        context.provenance(),
        context.confidence(),
        context.observedAt(),
        context.validFrom(),
        context.validTo(),
        context.scenario());
  }

  private Instant nextRecordingTime(UUID statementId) {
    return contexts.findByStatementIdOrderByRecordedAtAscIdAsc(statementId).stream()
        .map(StatementContextEntity::recordedAt)
        .max(Instant::compareTo)
        .map(last -> last.plusMillis(1))
        .orElseGet(Instant::now);
  }

  private static Context context(StatementContextEntity entity) {
    return new Context(
        entity.provenanceReference(),
        entity.confidence(),
        entity.observedAt(),
        entity.validFrom(),
        entity.validTo(),
        entity.scenario());
  }

  private static boolean equivalentContext(Context left, Context right) {
    return Objects.equals(left.provenance(), right.provenance())
        && equivalentConfidence(left.confidence(), right.confidence())
        && Objects.equals(left.observedAt(), right.observedAt())
        && Objects.equals(left.validFrom(), right.validFrom())
        && Objects.equals(left.validTo(), right.validTo())
        && Objects.equals(left.scenario(), right.scenario());
  }

  private static boolean equivalentConfidence(
      java.math.BigDecimal left, java.math.BigDecimal right) {
    return left == null ? right == null : right != null && left.compareTo(right) == 0;
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
      case STATE -> state(scopeId, object);
      case RELATION -> findRelation(scopeId, object.identityKey());
      case STATEMENT, TYPED_VALUE ->
          throw new IllegalStateException("unsupported endpoint kind: " + object.kind());
    };
  }

  private State state(UUID scopeId, CanonicalObjectEntity object) {
    var persisted =
        states
            .findById(object.id())
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        "State is missing ownership metadata: " + object.identityKey()));
    var owner = objects.findById(persisted.ownerObjectId()).orElseThrow();
    if (!owner.scopeId().equals(scopeId)) {
      throw new IllegalStateException("cross-scope State owner");
    }
    return new State(
        object.identityKey(), persisted.predicate(), TypedJsonValue.read(persisted.typedValue()));
  }

  private CanonicalObjectEntity object(UUID scopeId, String identityKey) {
    return objects.findByScopeIdAndIdentityKey(scopeId, identityKey).orElse(null);
  }

  private static boolean isStandaloneNode(CanonicalObjectEntity object) {
    var kind = Kind.valueOf(object.kind());
    return kind != Kind.RELATION
        && kind != Kind.STATE
        && kind != Kind.STATEMENT
        && kind != Kind.TYPED_VALUE;
  }

  private static Set<String> standaloneNodeKinds() {
    return Set.of(
        Kind.ENTITY.name(), Kind.CAPABILITY.name(), Kind.CONCEPT.name());
  }

  private static PageRequest pageable(PageQuery pageQuery) {
    return PageRequest.of(pageQuery.page(), pageQuery.size());
  }

  private static String identity(String prefix, long ordinal) {
    return prefix + "-" + String.format(Locale.ROOT, "%03d", ordinal);
  }

  private static <T, R> PageResult<R> page(Page<T> source, Function<T, R> mapper) {
    return new PageResult<>(
        source.getContent().stream().map(mapper).toList(),
        source.getNumber(),
        source.getSize(),
        source.getTotalElements(),
        source.getTotalPages());
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
      Object typedValue) {
    if (!existing.knowledgeKind().equals(requested.knowledgeKind().name())
        || !existing.predicate().equals(requested.predicate())
        || !existing.subjectObjectId().equals(subjectId)
        || !Objects.equals(existing.objectObjectId(), objectId)
        || !Objects.equals(
            TypedJsonValue.read(existing.typedValue()), TypedJsonValue.read(typedValue))) {
      throw new IllegalArgumentException("Statement identity cannot be overwritten");
    }
  }
}
