package com.fleetingtrails.fleetingjobsbackend.parser.mapper;

import com.fleetingtrails.fleetingjobsbackend.parser.dto.ParserTemplateCreateDto;
import com.fleetingtrails.fleetingjobsbackend.parser.dto.ParserTemplateGetDto;
import com.fleetingtrails.fleetingjobsbackend.parser.dto.ParserTemplateListItemDto;
import com.fleetingtrails.fleetingjobsbackend.parser.dto.ParserTemplateUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.parser.entity.ParserTemplateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ParserTemplateMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    ParserTemplateGetDto toGetDto(ParserTemplateEntity entity);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    ParserTemplateListItemDto toListItemDto(ParserTemplateEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    ParserTemplateEntity toEntity(ParserTemplateCreateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    void updateEntityFromDto(ParserTemplateUpdateDto dto, @MappingTarget ParserTemplateEntity entity);
}
