package com.fleetingtrails.fleetingjobsbackend.parser.dto;

import com.fleetingtrails.fleetingjobsbackend.parser.entity.ParserTemplateType;
import lombok.Getter;
import lombok.Setter;

public class ParserTemplateGetDto {
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long companyId;

    @Getter
    @Setter
    private String companyName;

    @Getter
    @Setter
    private ParserTemplateType config;

    public ParserTemplateGetDto () { }
}
