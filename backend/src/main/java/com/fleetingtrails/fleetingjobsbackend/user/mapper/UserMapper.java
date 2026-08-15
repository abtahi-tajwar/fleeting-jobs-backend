package com.fleetingtrails.fleetingjobsbackend.user.mapper;

import com.fleetingtrails.fleetingjobsbackend.user.dto.UserCreateDto;
import com.fleetingtrails.fleetingjobsbackend.user.dto.UserResponseDto;
import com.fleetingtrails.fleetingjobsbackend.user.dto.UserUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    UserResponseDto toResponseDto(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    UserEntity toEntity(UserCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget UserEntity entity, UserUpdateDto updateDto);
}