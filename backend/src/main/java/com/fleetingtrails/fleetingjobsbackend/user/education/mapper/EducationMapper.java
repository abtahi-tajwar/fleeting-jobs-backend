package com.fleetingtrails.fleetingjobsbackend.user.education.mapper;

import com.fleetingtrails.fleetingjobsbackend.user.education.dto.EducationCreateDto;
import com.fleetingtrails.fleetingjobsbackend.user.education.dto.EducationResponseDto;
import com.fleetingtrails.fleetingjobsbackend.user.education.dto.EducationUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.user.education.entity.EducationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EducationMapper {

    @Mapping(source = "user.id", target = "userId")
    EducationResponseDto toResponseDto(EducationEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    EducationEntity toEntity(EducationCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntity(@MappingTarget EducationEntity entity, EducationUpdateDto updateDto);
}
