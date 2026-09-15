package com.persiqa.web;

import com.persiqa.core.RelationRegistry;
import com.persiqa.web.dto.KnowledgeDtos.RelationTypeResponse;
import com.persiqa.web.dto.KnowledgeMapper;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** HTTP discovery of the effective Relation Type contracts available for recording. */
@RestController
@RequestMapping("/api/relation-types")
public class RelationTypeController {
  private final RelationRegistry registry;
  private final KnowledgeMapper mapper;

  public RelationTypeController(RelationRegistry registry, KnowledgeMapper mapper) {
    this.registry = registry;
    this.mapper = mapper;
  }

  /** Lists the registered Relation Type contracts in stable identifier order. */
  @GetMapping
  public List<RelationTypeResponse> findRelationTypes() {
    return registry.findAll().stream().map(mapper::toRelationType).toList();
  }
}
