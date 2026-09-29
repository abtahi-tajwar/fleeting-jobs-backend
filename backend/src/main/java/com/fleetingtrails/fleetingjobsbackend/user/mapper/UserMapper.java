package com.fleetingtrails.fleetingjobsbackend.user.mapper;

import com.fleetingtrails.fleetingjobsbackend.user.dto.UserCreateDto;
import com.fleetingtrails.fleetingjobsbackend.user.dto.UserResponseDto;
import com.fleetingtrails.fleetingjobsbackend.user.dto.UserUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @BeanMapping(ignoreUnmappedSourceProperties = {
            "otp",
            "authProvider",
            "providerSubject",
            "emailVerified",
            "emailVerificationToken",
            "emailVerificationExpiresAt"
    })
    UserResponseDto toResponseDto(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "otp", ignore = true)
    @Mapping(target = "authProvider", ignore = true)
    @Mapping(target = "providerSubject", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "emailVerificationToken", ignore = true)
    @Mapping(target = "emailVerificationExpiresAt", ignore = true)
    UserEntity toEntity(UserCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "otp", ignore = true)
    @Mapping(target = "authProvider", ignore = true)
    @Mapping(target = "providerSubject", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "emailVerificationToken", ignore = true)
    @Mapping(target = "emailVerificationExpiresAt", ignore = true)
    void updateEntity(@MappingTarget UserEntity entity, UserUpdateDto updateDto);
}