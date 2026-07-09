package com.fleetingtrails.fleetingjobsbackend.company.mapper;

import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyCreateDto;
import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyGetDto;
import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyListItemResponse;
import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.company.entity.CompanyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyListItemResponse toCompanyListItemResponse(CompanyEntity entity);
    CompanyEntity toEntity(CompanyCreateDto dto);
    CompanyGetDto toCompanyGetDto(CompanyEntity entity);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(CompanyUpdateDto dto, @MappingTarget CompanyEntity entity);
}
