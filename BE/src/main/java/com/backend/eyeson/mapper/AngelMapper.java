package com.backend.eyeson.mapper;

import com.backend.eyeson.dto.ResponseAngelInfoDto;
import com.backend.eyeson.entity.AngelInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AngelMapper extends StructMapper<ResponseAngelInfoDto, AngelInfoEntity> {


    AngelMapper INSTANCE = Mappers.getMapper(AngelMapper.class);

    @Override
    ResponseAngelInfoDto toDto(final AngelInfoEntity angelInfoEntity);
}
