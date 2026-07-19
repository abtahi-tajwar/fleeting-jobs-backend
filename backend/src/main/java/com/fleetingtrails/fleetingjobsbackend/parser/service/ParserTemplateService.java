package com.fleetingtrails.fleetingjobsbackend.parser.service;

import com.fleetingtrails.fleetingjobsbackend.common.exception.ResourceNotFoundException;
import com.fleetingtrails.fleetingjobsbackend.company.entity.CompanyEntity;
import com.fleetingtrails.fleetingjobsbackend.company.repository.CompanyRepository;
import com.fleetingtrails.fleetingjobsbackend.parser.dto.ParserTemplateCreateDto;
import com.fleetingtrails.fleetingjobsbackend.parser.dto.ParserTemplateFilterDto;
import com.fleetingtrails.fleetingjobsbackend.parser.dto.ParserTemplateGetDto;
import com.fleetingtrails.fleetingjobsbackend.parser.dto.ParserTemplateListItemDto;
import com.fleetingtrails.fleetingjobsbackend.parser.dto.ParserTemplateUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.parser.entity.ParserTemplateEntity;
import com.fleetingtrails.fleetingjobsbackend.parser.mapper.ParserTemplateMapper;
import com.fleetingtrails.fleetingjobsbackend.parser.repository.ParserTemplateRepository;
import com.fleetingtrails.fleetingjobsbackend.parser.specification.ParserTemplateSpecification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParserTemplateService {
    private final ParserTemplateRepository parserTemplateRepository;
    private final CompanyRepository companyRepository;
    private final ParserTemplateMapper parserTemplateMapper;

    public ParserTemplateService (
            ParserTemplateRepository parserTemplateRepository,
            CompanyRepository companyRepository,
            ParserTemplateMapper parserTemplateMapper
    ) {
        this.parserTemplateRepository = parserTemplateRepository;
        this.companyRepository = companyRepository;
        this.parserTemplateMapper = parserTemplateMapper;
    }

    public List<ParserTemplateListItemDto> getParserTemplates (ParserTemplateFilterDto filter) {
        List<ParserTemplateEntity> templates =
                parserTemplateRepository.findAll(ParserTemplateSpecification.withFilters(filter));
        List<ParserTemplateListItemDto> response = new ArrayList<>();

        for (ParserTemplateEntity template : templates) {
            response.add(parserTemplateMapper.toListItemDto(template));
        }

        return response;
    }

    public ParserTemplateGetDto getParserTemplateById (Long id) {
        ParserTemplateEntity template = findParserTemplateOrThrow(id);
        return parserTemplateMapper.toGetDto(template);
    }

    public ParserTemplateGetDto createParserTemplate (ParserTemplateCreateDto dto) {
        CompanyEntity company = findCompanyOrThrow(dto.getCompanyId());

        if (parserTemplateRepository.existsByCompanyId(company.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A parser template already exists for company id: " + company.getId()
            );
        }

        ParserTemplateEntity entity = parserTemplateMapper.toEntity(dto);
        entity.setCompany(company);

        ParserTemplateEntity saved = parserTemplateRepository.save(entity);
        return parserTemplateMapper.toGetDto(saved);
    }

    public ParserTemplateGetDto updateParserTemplate (Long id, ParserTemplateUpdateDto dto) {
        ParserTemplateEntity template = findParserTemplateOrThrow(id);
        parserTemplateMapper.updateEntityFromDto(dto, template);

        if (dto.getCompanyId() != null) {
            reassignCompany(template, dto.getCompanyId());
        }

        ParserTemplateEntity updated = parserTemplateRepository.save(template);
        return parserTemplateMapper.toGetDto(updated);
    }

    public void deleteParserTemplate (Long id) {
        ParserTemplateEntity template = findParserTemplateOrThrow(id);
        parserTemplateRepository.delete(template);
    }

    private void reassignCompany (ParserTemplateEntity template, Long companyId) {
        boolean sameCompany = template.getCompany() != null
                && companyId.equals(template.getCompany().getId());

        if (sameCompany) {
            return;
        }

        if (parserTemplateRepository.existsByCompanyId(companyId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A parser template already exists for company id: " + companyId
            );
        }

        template.setCompany(findCompanyOrThrow(companyId));
    }

    private ParserTemplateEntity findParserTemplateOrThrow (Long id) {
        return parserTemplateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parser template not found with id: " + id));
    }

    private CompanyEntity findCompanyOrThrow (Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
    }
}
