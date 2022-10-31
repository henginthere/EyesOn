package com.backend.eyeson.mapper;

import com.backend.eyeson.dto.ResponseAngelInfoDto;
import com.backend.eyeson.entity.AngelInfoEntity;
import org.mapstruct.factory.Mappers;

public interface AngelMapper extends StructMapper<ResponseAngelInfoDto, AngelInfoEntity> {

    AngelMapper INSTANCE = Mappers.getMapper(AngelMapper.class);

}
