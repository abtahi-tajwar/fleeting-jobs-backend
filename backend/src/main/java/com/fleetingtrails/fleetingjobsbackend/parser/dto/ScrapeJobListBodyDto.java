package com.fleetingtrails.fleetingjobsbackend.parser.dto;

import com.fleetingtrails.fleetingjobsbackend.parser.entity.ParserTemplateType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScrapeJobListBodyDto {
    Long company_id;
    String listing_url;
    ParserTemplateType parser_template;
}
