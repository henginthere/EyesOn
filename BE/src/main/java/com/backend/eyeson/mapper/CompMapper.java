package com.backend.eyeson.mapper;

import com.backend.eyeson.dto.ComplaintsDto;
import com.backend.eyeson.entity.ComplaintsEntity;
import org.mapstruct.factory.Mappers;

public interface CompMapper extends StructMapper<ComplaintsDto, ComplaintsEntity> {
    CompMapper INSTANCE = Mappers.getMapper(CompMapper.class);
}
