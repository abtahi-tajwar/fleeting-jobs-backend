package com.fleetingtrails.fleetingjobsbackend.parser.dto;

import com.fleetingtrails.fleetingjobsbackend.parser.entity.ParserTemplateType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class ParserTemplateUpdateDto {
    /**
     * Optional. When provided, the parser template is reassigned to the given company.
     */
    @Getter
    @Setter
    private Long companyId;

    @NotNull(message = "Parser config is required")
    @Valid
    @Getter
    @Setter
    private ParserTemplateType config;

    public ParserTemplateUpdateDto () { }
}
