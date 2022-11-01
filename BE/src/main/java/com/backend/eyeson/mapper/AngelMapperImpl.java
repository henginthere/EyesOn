package com.backend.eyeson.mapper;

import com.backend.eyeson.dto.ResponseAngelInfoDto;
import com.backend.eyeson.entity.AngelInfoEntity;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class AngelMapperImpl implements AngelMapper {

    @Override
    public AngelInfoEntity toEntity(ResponseAngelInfoDto dto) {
        return null;
    }

    @Override
    public ResponseAngelInfoDto toDto(AngelInfoEntity entity) {
        if(entity == null){
            return null;
        }
        ResponseAngelInfoDto dto = new ResponseAngelInfoDto();
        dto.setAngelAlarmStart(entity.getAngelAlarmStart());
        dto.setAngelAlarmEnd(entity.getAngelAlarmEnd());
        dto.setAngelCompCnt(entity.getAngelCompCnt());
        dto.setAngelHelpCnt(entity.getAngelHelpCnt());
        dto.setAngelGender(entity.getAngelGender());
        dto.setAngelActive(entity.isAngelActive());
        return dto;
    }

    @Override
    public List<ResponseAngelInfoDto> toDtoList(List<AngelInfoEntity> entityList) {
        return null;
    }

    @Override
    public List<AngelInfoEntity> toEntityList(List<ResponseAngelInfoDto> dtoList) {
        return null;
    }
}
