package com.fleetingtrails.fleetingjobsbackend.company.mapper;

import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyListItemResponse;
import com.fleetingtrails.fleetingjobsbackend.company.entity.CompanyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyListItemResponse toCompanyListItemResponse(CompanyEntity entity);
}
