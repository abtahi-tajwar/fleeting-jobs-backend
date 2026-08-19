package com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.mapper;

import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.dto.CertificationCreateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.dto.CertificationResponseDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.dto.CertificationUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.entity.CertificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CertificationMapper {

    @Mapping(source = "user.id", target = "userId")
    CertificationResponseDto toResponseDto(CertificationEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    CertificationEntity toEntity(CertificationCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntity(@MappingTarget CertificationEntity entity, CertificationUpdateDto updateDto);
}