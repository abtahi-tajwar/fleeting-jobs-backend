package com.fleetingtrails.fleetingjobsbackend.parser.controller;

import com.fleetingtrails.fleetingjobsbackend.common.response.APIListResponse;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIPostResponse;
import com.fleetingtrails.fleetingjobsbackend.parser.dto.ParserTemplateCreateDto;
import com.fleetingtrails.fleetingjobsbackend.parser.dto.ParserTemplateFilterDto;
import com.fleetingtrails.fleetingjobsbackend.parser.dto.ParserTemplateGetDto;
import com.fleetingtrails.fleetingjobsbackend.parser.dto.ParserTemplateListItemDto;
import com.fleetingtrails.fleetingjobsbackend.parser.dto.ParserTemplateUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.parser.service.ParserTemplateService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parser")
public class ParserTemplateController {
    private final ParserTemplateService parserTemplateService;

    public ParserTemplateController (
            ParserTemplateService service
    ) {
        this.parserTemplateService = service;
    }

    @GetMapping("/list")
    public APIListResponse<ParserTemplateListItemDto> getParserTemplates (
            @ModelAttribute ParserTemplateFilterDto filter
    ) {
        return APIListResponse.success(parserTemplateService.getParserTemplates(filter));
    }

    @GetMapping("/get/{id}")
    public APIPostResponse<ParserTemplateGetDto> getParserTemplate (@PathVariable Long id) {
        return APIPostResponse.success(parserTemplateService.getParserTemplateById(id));
    }

    @PostMapping("/create")
    public APIPostResponse<ParserTemplateGetDto> createParserTemplate (
            @Valid @RequestBody ParserTemplateCreateDto body
    ) {
        return APIPostResponse.success(parserTemplateService.createParserTemplate(body));
    }

    @PutMapping("/update/{id}")
    public APIPostResponse<ParserTemplateGetDto> updateParserTemplate (
            @PathVariable Long id,
            @Valid @RequestBody ParserTemplateUpdateDto body
    ) {
        return APIPostResponse.success(parserTemplateService.updateParserTemplate(id, body));
    }

    @DeleteMapping("/delete/{id}")
    public APIPostResponse<Void> deleteParserTemplate (@PathVariable Long id) {
        parserTemplateService.deleteParserTemplate(id);
        return APIPostResponse.success(null);
    }
}
