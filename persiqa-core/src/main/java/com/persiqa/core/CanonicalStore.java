package com.persiqa.core;

import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Persistence-independent boundary for durable CKM storage.
 *
 * <p>Implementations MUST preserve statement-first separation: Statements, Relations, contexts,
 * derivation, and canonicalization remain distinct records.
 */
public interface CanonicalStore {
  /** Creates a scope or rejects identity/owner/name conflicts for an existing scope. */
  void createScope(UUID scopeId, String name, String ownerSubject);

  /** Returns whether the scope identity exists. */
  boolean scopeExists(UUID scopeId);

  /** Returns the scope metadata when present. */
  Optional<ModelScope> findScope(UUID scopeId);

  /** Returns scopes owned by one subject in stable display order. */
  List<ModelScope> findScopesByOwner(String ownerSubject);

  /** Returns one page of owner-visible scopes, optionally filtered by scope name. */
  PageResult<ModelScope> findScopesByOwner(String ownerSubject, PageQuery pageQuery);

  /** Persists a Node without changing its CKM identity. */
  UUID save(UUID scopeId, Node node);

  /** Persists a traceable association between a Statement and a canonical object. */
  void canonicalize(
      UUID scopeId,
      Statement statement,
      Node canonicalObject,
      String mode,
      String policyIdentifier);

  /** Appends an independent observation context for an existing Statement. */
  void appendContext(UUID scopeId, String statementIdentity, Context context);

  /** Returns every append-preserved observation for one Statement. */
  List<Context> findContexts(UUID scopeId, String statementIdentity);

  /** Reconstructs a standalone Node, or {@code null} when unknown. */
  Node findNode(UUID scopeId, String identityKey);

  /** Returns standalone canonical Nodes in one scope in stable identity order. */
  List<Node> findNodes(UUID scopeId);

  /** Returns one page of standalone Nodes, optionally filtered by canonical identity. */
  PageResult<Node> findNodes(UUID scopeId, PageQuery pageQuery);

  /** Reconstructs a Relation, or {@code null} when unknown. */
  Relation findRelation(UUID scopeId, String identityKey);

  /** Returns every Relation in one scope in stable identity order. */
  List<Relation> findRelations(UUID scopeId);

  /** Returns one page of Relations, optionally filtered by canonical identity. */
  PageResult<Relation> findRelations(UUID scopeId, PageQuery pageQuery);

  /**
   * Reconstructs a Statement with its original assertion context.
   *
   * <p>The Statement context is the first persisted context record. Later append-preserved
   * observations are available through {@link #findContexts(UUID, String)} and MUST NOT replace
   * the assertion context.
   */
  Statement findStatement(UUID scopeId, String identityKey);

  /** Returns every Statement in one scope in stable identity order. */
  List<Statement> findStatements(UUID scopeId);

  /** Returns one page of Statements, optionally filtered by canonical identity. */
  PageResult<Statement> findStatements(UUID scopeId, PageQuery pageQuery);

  /** Returns Statements canonically associated with one Relation. */
  List<Statement> findStatementsForRelation(UUID scopeId, String relationIdentity);
}
