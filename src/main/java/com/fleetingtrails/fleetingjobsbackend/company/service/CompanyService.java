package com.fleetingtrails.fleetingjobsbackend.company.service;

import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyListItemResponse;
import com.fleetingtrails.fleetingjobsbackend.company.entity.CompanyEntity;
import com.fleetingtrails.fleetingjobsbackend.company.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService (CompanyRepository repository) {
        this.companyRepository = repository;
    }

    public List<CompanyListItemResponse> getCompanies () {
        List<CompanyEntity> companies = this.companyRepository.findAll();
        List<CompanyListItemResponse> response = new ArrayList<>();

        for (CompanyEntity company : companies) {

            CompanyListItemResponse dto = new CompanyListItemResponse();
            dto.setId(company.getId());
            dto.setName(company.getName());
            dto.setListingUrl(company.getListingUrl());
            dto.setSinglePageUrlTemplate(company.getSinglePageUrlTemplate());
            dto.setEnabled(company.getEnabled());
            response.add(dto);
        }

        return response;
    }
}
