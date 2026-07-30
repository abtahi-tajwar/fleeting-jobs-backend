package com.fleetingtrails.fleetingjobsbackend.user.award.mapper;

import com.fleetingtrails.fleetingjobsbackend.user.award.dto.AwardCreateDto;
import com.fleetingtrails.fleetingjobsbackend.user.award.dto.AwardResponseDto;
import com.fleetingtrails.fleetingjobsbackend.user.award.dto.AwardUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.user.award.entity.AwardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AwardMapper {

    @Mapping(source = "user.id", target = "userId")
    AwardResponseDto toResponseDto(AwardEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    AwardEntity toEntity(AwardCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntity(@MappingTarget AwardEntity entity, AwardUpdateDto updateDto);
}
