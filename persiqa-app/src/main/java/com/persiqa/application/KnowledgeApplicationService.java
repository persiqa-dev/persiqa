package com.persiqa.application;

import com.persiqa.core.CanonicalStore;
import com.persiqa.core.ModelScope;
import com.persiqa.core.ScopeAccessDeniedException;
import com.persiqa.core.StatementFirstRecording;
import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.Statement;
import java.util.List;
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

  public KnowledgeApplicationService(CanonicalStore store, StatementFirstRecording recording) {
    this.store = store;
    this.recording = recording;
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

  /** Persists a standalone canonical Node without requiring a Relation assertion. */
  @Transactional
  public void saveNode(UUID scopeId, String subject, Node node) {
    requireOwner(scopeId, subject);
    store.save(scopeId, node);
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

  /** Returns every explicit or derived Statement that supports one canonical Relation. */
  @Transactional(readOnly = true)
  public List<Statement> findStatementsForRelation(
      UUID scopeId, String subject, String relationId) {
    requireOwner(scopeId, subject);
    return store.findStatementsForRelation(scopeId, relationId);
  }

  /** Returns all append-preserved observations for one Statement. */
  @Transactional(readOnly = true)
  public List<Context> findObservations(UUID scopeId, String subject, String statementId) {
    requireOwner(scopeId, subject);
    return store.findContexts(scopeId, statementId);
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
    return new RelationRecord(prepared.statement(), prepared.relation());
  }

  private void requireOwner(UUID scopeId, String subject) {
    var scope =
        store
            .findScope(scopeId)
            .orElseThrow(() -> new IllegalArgumentException("unknown model scope: " + scopeId));
    if (!scope.ownerSubject().equals(subject)) {
      throw new ScopeAccessDeniedException("subject is not the owner of scope " + scopeId);
    }
  }

  /** Result of a statement-first relation recording use case. */
  public record RelationRecord(Statement statement, Relation relation) {}
}
