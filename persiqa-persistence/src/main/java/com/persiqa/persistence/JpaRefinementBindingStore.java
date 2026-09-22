package com.persiqa.persistence;

import com.persiqa.core.RefinementBindingStore;
import com.persiqa.persistence.entity.RefinementBindingDetailEntity;
import com.persiqa.persistence.entity.RefinementBindingEntity;
import com.persiqa.persistence.repository.CanonicalObjectRepository;
import com.persiqa.persistence.repository.RefinementBindingDetailRepository;
import com.persiqa.persistence.repository.RefinementBindingRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** JPA adapter for non-canonical, presentation-level refinement bindings. */
@Service
public class JpaRefinementBindingStore implements RefinementBindingStore {
  private final CanonicalObjectRepository objects;
  private final RefinementBindingRepository bindings;
  private final RefinementBindingDetailRepository details;

  public JpaRefinementBindingStore(
      CanonicalObjectRepository objects,
      RefinementBindingRepository bindings,
      RefinementBindingDetailRepository details) {
    this.objects = objects;
    this.bindings = bindings;
    this.details = details;
  }

  @Override
  @Transactional(readOnly = true)
  public Set<String> activeCoarseRelationIds(UUID scopeId) {
    var coarseRelationIds = bindings.findByScopeIdAndInvalidatedAtIsNull(scopeId).stream()
        .map(RefinementBindingEntity::coarseRelationId)
        .toList();
    return objects.findByScopeIdAndIdIn(scopeId, coarseRelationIds).stream()
        .map(object -> object.identityKey())
        .collect(java.util.stream.Collectors.toUnmodifiableSet());
  }

  @Override
  @Transactional(readOnly = true)
  public Map<String, Binding> findAll(UUID scopeId) {
    var persisted = bindings.findByScopeId(scopeId);
    var identities = objects.findAllById(
        persisted.stream().map(RefinementBindingEntity::coarseRelationId).toList()).stream()
        .collect(
            java.util.stream.Collectors.toMap(
                object -> object.id(), object -> object.identityKey()));
    return persisted.stream()
        .collect(
            java.util.stream.Collectors.toUnmodifiableMap(
                binding -> identities.get(binding.coarseRelationId()),
                binding ->
                    new Binding(
                        binding.id(),
                        scopeId,
                        identities.get(binding.coarseRelationId()),
                        binding.active())));
  }

  @Override
  @Transactional
  public void save(
      UUID scopeId,
      String coarseRelationId,
      List<String> detailRelationIds,
      String declarer,
      Instant declaredAt) {
    var bindingId = UUID.randomUUID();
    bindings.save(new RefinementBindingEntity(
        bindingId, scopeId, objectId(scopeId, coarseRelationId), declarer, declaredAt));
    for (var ordinal = 0; ordinal < detailRelationIds.size(); ordinal++) {
      details.save(new RefinementBindingDetailEntity(
          bindingId, ordinal, objectId(scopeId, detailRelationIds.get(ordinal))));
    }
  }

  @Override
  @Transactional
  public void invalidate(Binding binding, String reason) {
    bindings.findById(binding.id()).ifPresent(entity -> entity.invalidate(reason));
  }

  @Override
  @Transactional
  public void reactivate(Binding binding, List<String> detailRelationIds) {
    var entity = bindings.findById(binding.id()).orElseThrow();
    entity.reactivate();
    details.deleteByBindingId(binding.id());
    for (var ordinal = 0; ordinal < detailRelationIds.size(); ordinal++) {
      details.save(new RefinementBindingDetailEntity(
          binding.id(), ordinal, objectId(binding.scopeId(), detailRelationIds.get(ordinal))));
    }
  }

  private UUID objectId(UUID scopeId, String identity) {
    return objects.findByScopeIdAndIdentityKey(scopeId, identity).orElseThrow().id();
  }
}
