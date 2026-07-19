package com.fleetingtrails.fleetingjobsbackend.parser.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Query parameters used to filter the parser template list endpoint.
 * All fields are optional; when omitted the corresponding filter is skipped.
 */
public class ParserTemplateFilterDto {
    @Getter
    @Setter
    private Long companyId;

    @Getter
    @Setter
    private String companyName;

    public ParserTemplateFilterDto () { }
}
