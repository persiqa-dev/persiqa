package com.persiqa.web.dto;

import com.persiqa.application.KnowledgeApplicationService.RelationRecord;
import com.persiqa.core.ModelScope;
import com.persiqa.model.Ckm.Context;
import com.persiqa.model.Ckm.Node;
import com.persiqa.model.Ckm.Relation;
import com.persiqa.model.Ckm.RelationType;
import com.persiqa.model.Ckm.Statement;
import com.persiqa.web.dto.KnowledgeDtos.NodeResponse;
import com.persiqa.web.dto.KnowledgeDtos.ObservationResponse;
import com.persiqa.web.dto.KnowledgeDtos.RelationRecordResponse;
import com.persiqa.web.dto.KnowledgeDtos.RelationResponse;
import com.persiqa.web.dto.KnowledgeDtos.RelationTypeResponse;
import com.persiqa.web.dto.KnowledgeDtos.ScopeResponse;
import com.persiqa.web.dto.KnowledgeDtos.StatementResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

/** MapStruct mapper from CKM / application types to HTTP DTOs. */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface KnowledgeMapper {
  ScopeResponse toScope(ModelScope scope);

  /** Node is a CKM interface, so accessors are mapped explicitly. */
  default NodeResponse toNode(Node node) {
    return node == null ? null : new NodeResponse(node.id(), node.kind());
  }

  RelationTypeResponse toRelationType(RelationType type);

  RelationResponse toRelation(Relation relation);

  ObservationResponse toObservation(Context context);

  List<ObservationResponse> toObservations(List<Context> contexts);

  List<RelationResponse> toRelations(List<Relation> relations);

  @Mapping(target = "object", qualifiedByName = "statementObject")
  @Mapping(target = "context", source = "context")
  StatementResponse toStatement(Statement statement);

  List<StatementResponse> toStatements(List<Statement> statements);

  RelationRecordResponse toRelationRecord(RelationRecord record);

  /** Maps a Statement object endpoint to a Node DTO when it is a Node. */
  @Named("statementObject")
  default Object mapStatementObject(Object object) {
    return object instanceof Node node ? toNode(node) : object;
  }
}
