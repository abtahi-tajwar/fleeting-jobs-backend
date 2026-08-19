package com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.mapper;

import com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.dto.WorkExperienceCreateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.dto.WorkExperienceResponseDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.dto.WorkExperienceUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.entity.WorkExperienceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WorkExperienceMapper {

    @Mapping(source = "user.id", target = "userId")
    WorkExperienceResponseDto toResponseDto(WorkExperienceEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    WorkExperienceEntity toEntity(WorkExperienceCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntity(@MappingTarget WorkExperienceEntity entity, WorkExperienceUpdateDto updateDto);
}