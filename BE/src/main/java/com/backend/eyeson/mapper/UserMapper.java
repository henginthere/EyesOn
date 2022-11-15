package com.backend.eyeson.mapper;

import com.backend.eyeson.dto.UserDto;
import com.backend.eyeson.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper extends StructMapper<UserDto, UserEntity> {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
}
