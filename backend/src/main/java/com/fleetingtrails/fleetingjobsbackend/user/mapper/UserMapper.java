package com.fleetingtrails.fleetingjobsbackend.user.mapper;

import com.fleetingtrails.fleetingjobsbackend.user.dto.UserCreateDto;
import com.fleetingtrails.fleetingjobsbackend.user.dto.UserResponseDto;
import com.fleetingtrails.fleetingjobsbackend.user.dto.UserUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toResponseDto(UserEntity entity);

    UserEntity toEntity(UserCreateDto createDto);

    void updateEntity(@MappingTarget UserEntity entity, UserUpdateDto updateDto);
}