package com.persiqa.application;

import com.persiqa.application.SemanticTraversalService.SemanticMatch;
import com.persiqa.application.TopologyProjectionService.Direction;
import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.KnowledgeKind;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Creates reviewable derived-knowledge proposals from semantic traversal witnesses. */
@Service
public class DerivationProposalService {
  private final KnowledgeApplicationService knowledge;
  private final SemanticTraversalService traversal;

  public DerivationProposalService(
      KnowledgeApplicationService knowledge, SemanticTraversalService traversal) {
    this.knowledge = knowledge;
    this.traversal = traversal;
  }

  /** Lists non-duplicating transitive conclusions that have complete Statement evidence. */
  @Transactional(readOnly = true)
  public List<DerivationProposal> propose(
      UUID scopeId,
      String subject,
      String anchorId,
      String relationType,
      Direction direction,
      int maxHops) {
    var semanticTraversal =
        traversal.traverse(scopeId, subject, anchorId, relationType, direction, maxHops);
    var relations = knowledge.findRelations(scopeId, subject);
    return semanticTraversal.matches().stream()
        .filter(match -> match.hops() > 1)
        .filter(DerivationProposalService::hasCompleteEvidence)
        .map(match -> proposal(semanticTraversal.anchor(), match, relationType, direction))
        .filter(proposal -> !hasCanonicalRelation(relations, proposal))
        .sorted(
            Comparator.comparing((DerivationProposal proposal) -> proposal.reachable().id())
                .thenComparing(proposal -> proposal.source().id()))
        .toList();
  }

  /** Accepts one currently valid proposal through the normal statement-first recording path. */
  @Transactional
  public KnowledgeApplicationService.RelationRecord accept(
      UUID scopeId,
      String subject,
      String anchorId,
      String reachableId,
      String relationType,
      Direction direction,
      int maxHops,
      Context context) {
    var proposal = propose(scopeId, subject, anchorId, relationType, direction, maxHops).stream()
        .filter(candidate -> candidate.reachable().id().equals(reachableId))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException("no current derivation proposal for: " + reachableId));
    return knowledge.recordRelation(
        scopeId,
        subject,
        null,
        null,
        KnowledgeKind.DERIVED,
        proposal.relationType(),
        proposal.source(),
        proposal.target(),
        new LinkedHashSet<>(proposal.evidenceStatementIds()),
        context);
  }

  private static boolean hasCompleteEvidence(SemanticMatch match) {
    return match.witness().stream().allMatch(step -> !step.supportingStatements().isEmpty());
  }

  private static DerivationProposal proposal(
      Node anchor, SemanticMatch match, String relationType, Direction direction) {
    var source = direction == Direction.DOWNSTREAM ? anchor : match.target();
    var target = direction == Direction.DOWNSTREAM ? match.target() : anchor;
    var evidence = match.witness().stream()
        .flatMap(step -> step.supportingStatements().stream())
        .map(statement -> statement.id())
        .distinct()
        .toList();
    var supportingRelations = match.witness().stream().map(step -> step.relation().id()).toList();
    return new DerivationProposal(
        match.target(), source, target, relationType, match.hops(), evidence, supportingRelations);
  }

  private static boolean hasCanonicalRelation(
      List<Relation> relations, DerivationProposal proposal) {
    return relations.stream()
        .anyMatch(
            relation ->
                relation.type().id().equals(proposal.relationType())
                    && relation.source().id().equals(proposal.source().id())
                    && relation.target().id().equals(proposal.target().id()));
  }

  /** A user-reviewable conclusion with the exact canonical evidence required to record it. */
  public record DerivationProposal(
      Node reachable,
      Node source,
      Node target,
      String relationType,
      int hops,
      List<String> evidenceStatementIds,
      List<String> supportingRelationIds) {}
}
