package com.backend.eyeson.mapper;

import com.backend.eyeson.dto.ComplaintsDto;
import com.backend.eyeson.dto.RequestCompDto;
import com.backend.eyeson.dto.ResponseCompDto;
import com.backend.eyeson.entity.ComplaintsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.io.IOException;

@Mapper
public interface CompMapper{
    CompMapper INSTANCE = Mappers.getMapper(CompMapper.class);

    @Mapping(target="compSeq", ignore = true)
    @Mapping(target="blindUser", ignore = true)
    @Mapping(target="angelUser", ignore = true)
    @Mapping(target="compState", ignore = true)
    @Mapping(target="compReturn", ignore = true)
    @Mapping(target="compTitle", ignore = true)
    @Mapping(target="compResultContent", ignore = true)
    @Mapping(target="compRegtime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target="compAddress", ignore = true)
    ComplaintsEntity toEntity(RequestCompDto requestCompDto);

    ResponseCompDto toDto(ComplaintsEntity complaintsEntity);

}
