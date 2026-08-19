package com.fleetingtrails.fleetingjobsbackend.profile._submodules.skill.mapper;

import com.fleetingtrails.fleetingjobsbackend.profile._submodules.skill.dto.SkillCreateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.skill.dto.SkillResponseDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.skill.dto.SkillUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.skill.entity.SkillEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    // This tells MapStruct to take the ID from the 'user' object inside SkillEntity and put it into 'userId' in the DTO
    @Mapping(source = "user.id", target = "userId")
    SkillResponseDto toResponseDto(SkillEntity entity);

    // When creating a new Skill, ignore the 'id' (it's auto-generated) and ignore the 'user' (we will set it manually in the Service)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    SkillEntity toEntity(SkillCreateDto createDto);

    // When updating, ignore the 'id' and 'user' so they aren't accidentally changed
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntity(@MappingTarget SkillEntity entity, SkillUpdateDto updateDto);
}