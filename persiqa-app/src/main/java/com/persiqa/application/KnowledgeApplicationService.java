package com.persiqa.application;

import com.persiqa.core.CanonicalStore;
import com.persiqa.core.ModelScope;
import com.persiqa.core.PageQuery;
import com.persiqa.core.PageResult;
import com.persiqa.core.ScopeAccessDeniedException;
import com.persiqa.core.StatementFirstRecording;
import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.KnowledgeKind;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.Statement;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Application use cases for recording and reading statement-first CKM knowledge. */
@Service
public class KnowledgeApplicationService {
  private static final String ASSERTION_POLICY = "statement-first-v0.1";

  private final CanonicalStore store;
  private final StatementFirstRecording recording;
  private final RefinementBindingService refinements;

  public KnowledgeApplicationService(
      CanonicalStore store,
      StatementFirstRecording recording,
      RefinementBindingService refinements) {
    this.store = store;
    this.recording = recording;
    this.refinements = refinements;
  }

  /** Creates one named CKM scope owned by the given subject. */
  @Transactional
  public ModelScope createScope(UUID scopeId, String name, String ownerSubject) {
    store.createScope(scopeId, name, ownerSubject);
    return store.findScope(scopeId).orElseThrow();
  }

  /** Returns whether the CKM scope identity exists. */
  @Transactional(readOnly = true)
  public boolean scopeExists(UUID scopeId) {
    return store.scopeExists(scopeId);
  }

  /** Returns scope metadata when present. */
  @Transactional(readOnly = true)
  public ModelScope findScope(UUID scopeId) {
    return store.findScope(scopeId).orElse(null);
  }

  /** Returns the scopes owned by the subject in stable display order. */
  @Transactional(readOnly = true)
  public List<ModelScope> findScopesOwnedBy(String subject) {
    return store.findScopesByOwner(subject);
  }

  /** Returns one page of scopes owned by the subject. */
  @Transactional(readOnly = true)
  public PageResult<ModelScope> findScopesOwnedBy(String subject, PageQuery pageQuery) {
    return store.findScopesByOwner(subject, pageQuery);
  }

  /** Returns scope metadata and counts without materializing the canonical graph. */
  @Transactional(readOnly = true)
  public ScopeKnowledgeSummary findKnowledgeSummary(UUID scopeId, String subject) {
    var scope = requireOwner(scopeId, subject);
    return new ScopeKnowledgeSummary(
        scope,
        store.countNodes(scopeId),
        store.countRelations(scopeId),
        store.countStatements(scopeId));
  }

  /** Returns one relation-type subgraph for bounded topology and semantic queries. */
  @Transactional(readOnly = true)
  public RelationGraph findRelationGraph(UUID scopeId, String subject, String relationType) {
    requireOwner(scopeId, subject);
    var relations = store.findRelationsByType(scopeId, relationType);
    return new RelationGraph(relations, store.findStatementsForRelations(scopeId, relations));
  }

  /** Persists a standalone canonical Node without requiring a Relation assertion. */
  @Transactional
  public void saveNode(UUID scopeId, String subject, Node node) {
    requireOwner(scopeId, subject);
    store.save(scopeId, node);
  }

  /**
   * Records a Relation statement, allocating readable identities when callers leave either one
   * unspecified.
   */
  @Transactional
  public RelationRecord recordRelation(
      UUID scopeId,
      String subject,
      String relationId,
      String statementId,
      KnowledgeKind knowledgeKind,
      String relationType,
      Node source,
      Node target,
      Set<String> evidence,
      Context context) {
    requireOwner(scopeId, subject);
    if (knowledgeKind == KnowledgeKind.EXPLICIT && !evidence.isEmpty()) {
      throw new IllegalArgumentException("explicit statement cannot declare derivedFrom evidence");
    }
    var resolvedRelationId =
        suppliedOrGenerated(
            relationId, () -> generatedRelationIdentity(scopeId, source, relationType, target));
    var resolvedStatementId =
        suppliedOrGenerated(
            statementId,
            () -> generatedStatementIdentity(scopeId, resolvedRelationId, knowledgeKind));
    var prepared =
        knowledgeKind == KnowledgeKind.EXPLICIT
            ? recording.assertRelation(
                resolvedRelationId, resolvedStatementId, relationType, source, target, context)
            : recording.deriveRelation(
                resolvedRelationId,
                resolvedStatementId,
                relationType,
                source,
                target,
                evidence,
                context);
    var mode = knowledgeKind == KnowledgeKind.EXPLICIT ? "ASSERTS" : "DERIVES";
    return persist(scopeId, prepared, mode);
  }

  /** Returns a standalone canonical Node, or {@code null} when the identity is unknown. */
  @Transactional(readOnly = true)
  public Node findNode(UUID scopeId, String subject, String nodeId) {
    requireOwner(scopeId, subject);
    return store.findNode(scopeId, nodeId);
  }

  /** Returns standalone canonical Nodes in one scope in stable identity order. */
  @Transactional(readOnly = true)
  public List<Node> findNodes(UUID scopeId, String subject) {
    requireOwner(scopeId, subject);
    return store.findNodes(scopeId);
  }

  /** Returns one page of standalone canonical Nodes visible to the subject. */
  @Transactional(readOnly = true)
  public PageResult<Node> findNodes(UUID scopeId, String subject, PageQuery pageQuery) {
    requireOwner(scopeId, subject);
    return store.findNodes(scopeId, pageQuery);
  }

  /** Records an explicit assertion and its separately addressable canonical Relation. */
  @Transactional
  public RelationRecord assertRelation(
      UUID scopeId,
      String subject,
      String relationId,
      String statementId,
      String relationType,
      Node source,
      Node target,
      Context context) {
    requireOwner(scopeId, subject);
    return persist(
        scopeId,
        recording.assertRelation(relationId, statementId, relationType, source, target, context),
        "ASSERTS");
  }

  /** Records a derived assertion with explicit persisted evidence identities. */
  @Transactional
  public RelationRecord recordDerivedRelation(
      UUID scopeId,
      String subject,
      String relationId,
      String statementId,
      String relationType,
      Node source,
      Node target,
      Set<String> evidence,
      Context context) {
    requireOwner(scopeId, subject);
    return persist(
        scopeId,
        recording.deriveRelation(
            relationId, statementId, relationType, source, target, evidence, context),
        "DERIVES");
  }

  /** Adds another independent observation to an existing Statement. */
  @Transactional
  public void appendObservation(UUID scopeId, String subject, String statementId, Context context) {
    requireOwner(scopeId, subject);
    store.appendContext(scopeId, statementId, context);
  }

  /** Returns the stored canonical Relation, or {@code null} when the identity is unknown. */
  @Transactional(readOnly = true)
  public Relation findRelation(UUID scopeId, String subject, String relationId) {
    requireOwner(scopeId, subject);
    return store.findRelation(scopeId, relationId);
  }

  /** Returns the canonical Relations in one scope in stable identity order. */
  @Transactional(readOnly = true)
  public List<Relation> findRelations(UUID scopeId, String subject) {
    requireOwner(scopeId, subject);
    return store.findRelations(scopeId);
  }

  /** Returns one page of canonical Relations visible to the subject. */
  @Transactional(readOnly = true)
  public PageResult<Relation> findRelations(UUID scopeId, String subject, PageQuery pageQuery) {
    requireOwner(scopeId, subject);
    return store.findRelations(scopeId, pageQuery);
  }

  /** Returns the stored Statement with its original assertion context, or {@code null}. */
  @Transactional(readOnly = true)
  public Statement findStatement(UUID scopeId, String subject, String statementId) {
    requireOwner(scopeId, subject);
    return store.findStatement(scopeId, statementId);
  }

  /** Returns the Statements in one scope in stable identity order. */
  @Transactional(readOnly = true)
  public List<Statement> findStatements(UUID scopeId, String subject) {
    requireOwner(scopeId, subject);
    return store.findStatements(scopeId);
  }

  /** Returns one page of Statements visible to the subject. */
  @Transactional(readOnly = true)
  public PageResult<Statement> findStatements(UUID scopeId, String subject, PageQuery pageQuery) {
    requireOwner(scopeId, subject);
    return store.findStatements(scopeId, pageQuery);
  }

  /** Returns every explicit or derived Statement that supports one canonical Relation. */
  @Transactional(readOnly = true)
  public List<Statement> findStatementsForRelation(
      UUID scopeId, String subject, String relationId) {
    requireOwner(scopeId, subject);
    return store.findStatementsForRelation(scopeId, relationId);
  }

  /**
   * Returns contexts appended after the immutable assertion context for one Statement.
   *
   * <p>The persistence store retains the assertion context as the first context record so a
   * Statement can be reconstructed without duplicating it. It is not itself a later observation.
   */
  @Transactional(readOnly = true)
  public List<Context> findObservations(UUID scopeId, String subject, String statementId) {
    requireOwner(scopeId, subject);
    return store.findContexts(scopeId, statementId).stream().skip(1).toList();
  }

  private RelationRecord persist(
      UUID scopeId, StatementFirstRecording.Record prepared, String canonicalizationMode) {
    store.save(scopeId, prepared.relation());
    store.save(scopeId, prepared.statement());
    store.canonicalize(
        scopeId,
        prepared.statement(),
        prepared.relation(),
        canonicalizationMode,
        ASSERTION_POLICY);
    if (prepared.statement().knowledgeKind() == KnowledgeKind.EXPLICIT) {
      refinements.detectAndBind(scopeId, prepared.relation().type().id());
    }
    return new RelationRecord(prepared.statement(), prepared.relation());
  }

  private String generatedRelationIdentity(
      UUID scopeId, Node source, String relationType, Node target) {
    var prefix =
        "rel-" + slug(source.id()) + "-" + slug(relationType) + "-" + slug(target.id());
    return numberedIdentity(prefix, store.nextIdentityOrdinal(scopeId, prefix));
  }

  private String generatedStatementIdentity(
      UUID scopeId, String relationId, KnowledgeKind knowledgeKind) {
    var prefix =
        "stmt-" + slug(relationId) + "-" + knowledgeKind.name().toLowerCase(Locale.ROOT);
    return numberedIdentity(prefix, store.nextIdentityOrdinal(scopeId, prefix));
  }

  private static String suppliedOrGenerated(
      String supplied, java.util.function.Supplier<String> generated) {
    return supplied == null || supplied.isBlank() ? generated.get() : supplied;
  }

  private static String numberedIdentity(String prefix, long ordinal) {
    return prefix + "-" + String.format(Locale.ROOT, "%03d", ordinal);
  }

  private static String slug(String value) {
    var normalized = Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    var slug = normalized.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-");
    slug = slug.replaceAll("(^-)|(-$)", "");
    return slug.isBlank() ? "item" : slug;
  }

  private ModelScope requireOwner(UUID scopeId, String subject) {
    var scope =
        store
            .findScope(scopeId)
            .orElseThrow(() -> new IllegalArgumentException("unknown model scope: " + scopeId));
    if (!scope.ownerSubject().equals(subject)) {
      throw new ScopeAccessDeniedException("subject is not the owner of scope " + scopeId);
    }
    return scope;
  }

  /** Compact scope overview for clients that load graph and lists through dedicated projections. */
  public record ScopeKnowledgeSummary(
      ModelScope scope, long nodeCount, long relationCount, long statementCount) {}

  /** Targeted relation-type graph input for server-side graph algorithms. */
  public record RelationGraph(List<Relation> relations, List<Statement> statements) {}

  /** Result of a statement-first relation recording use case. */
  public record RelationRecord(Statement statement, Relation relation) {}
}
