package com.persiqa.application;

import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.KnowledgeKind;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.RelationType;
import com.persiqa.model.Ckm.Statement;
import com.persiqa.persistence.JpaCanonicalStore;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Application use cases for recording and reading statement-first CKM knowledge. */
@Service
public class KnowledgeApplicationService {
  private static final String ASSERTION_POLICY = "statement-first-v0.1";

  private final JpaCanonicalStore store;

  public KnowledgeApplicationService(JpaCanonicalStore store) {
    this.store = store;
  }

  /** Creates one named CKM scope. */
  @Transactional
  public void createScope(UUID scopeId, String name) {
    store.createScope(scopeId, name);
  }

  /** Returns whether the CKM scope identity exists. */
  @Transactional(readOnly = true)
  public boolean scopeExists(UUID scopeId) {
    return store.scopeExists(scopeId);
  }

  /** Persists a standalone canonical Node without requiring a Relation assertion. */
  @Transactional
  public void saveNode(UUID scopeId, Node node) {
    store.save(scopeId, node);
  }

  /** Returns a standalone canonical Node, or {@code null} when the identity is unknown. */
  @Transactional(readOnly = true)
  public Node findNode(UUID scopeId, String nodeId) {
    return store.findNode(scopeId, nodeId);
  }

  /** Records an explicit assertion and its separately addressable canonical Relation. */
  @Transactional
  public RelationRecord assertRelation(
      UUID scopeId,
      String relationId,
      String statementId,
      RelationType type,
      Node source,
      Node target,
      Context context) {
    return recordRelation(
        scopeId,
        relationId,
        statementId,
        KnowledgeKind.EXPLICIT,
        type,
        source,
        target,
        Set.of(),
        context,
        "ASSERTS");
  }

  /** Records a derived assertion with explicit persisted evidence identities. */
  @Transactional
  public RelationRecord recordDerivedRelation(
      UUID scopeId,
      String relationId,
      String statementId,
      RelationType type,
      Node source,
      Node target,
      Set<String> evidence,
      Context context) {
    return recordRelation(
        scopeId,
        relationId,
        statementId,
        KnowledgeKind.DERIVED,
        type,
        source,
        target,
        evidence,
        context,
        "DERIVES");
  }

  /** Adds another independent observation to an existing Statement. */
  @Transactional
  public void appendObservation(UUID scopeId, String statementId, Context context) {
    store.appendContext(scopeId, statementId, context);
  }

  /** Returns the stored canonical Relation, or {@code null} when the identity is unknown. */
  @Transactional(readOnly = true)
  public Relation findRelation(UUID scopeId, String relationId) {
    return store.findRelation(scopeId, relationId);
  }

  /** Returns the canonical Relations in one scope in stable identity order. */
  @Transactional(readOnly = true)
  public List<Relation> findRelations(UUID scopeId) {
    return store.findRelations(scopeId);
  }

  /** Returns the stored Statement, or {@code null} when the identity is unknown. */
  @Transactional(readOnly = true)
  public Statement findStatement(UUID scopeId, String statementId) {
    return store.findStatement(scopeId, statementId);
  }

  /** Returns the Statements in one scope in stable identity order. */
  @Transactional(readOnly = true)
  public List<Statement> findStatements(UUID scopeId) {
    return store.findStatements(scopeId);
  }

  /** Returns every explicit or derived Statement that supports one canonical Relation. */
  @Transactional(readOnly = true)
  public List<Statement> findStatementsForRelation(UUID scopeId, String relationId) {
    return store.findStatementsForRelation(scopeId, relationId);
  }

  /** Returns all append-preserved observations for one Statement. */
  @Transactional(readOnly = true)
  public List<Context> findObservations(UUID scopeId, String statementId) {
    return store.findContexts(scopeId, statementId);
  }

  private RelationRecord recordRelation(
      UUID scopeId,
      String relationId,
      String statementId,
      KnowledgeKind knowledgeKind,
      RelationType type,
      Node source,
      Node target,
      Set<String> evidence,
      Context context,
      String canonicalizationMode) {
    var relation = new Relation(relationId, type, source, target);
    var statement =
        new Statement(statementId, knowledgeKind, type.id(), source, target, evidence, context);
    store.save(scopeId, relation);
    store.save(scopeId, statement);
    store.canonicalize(scopeId, statement, relation, canonicalizationMode, ASSERTION_POLICY);
    return new RelationRecord(statement, relation);
  }

  /** Result of a statement-first relation recording use case. */
  public record RelationRecord(Statement statement, Relation relation) {}
}
