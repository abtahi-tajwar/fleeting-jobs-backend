package com.fleetingtrails.fleetingjobsbackend.company.service;

import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyListItemResponse;
import com.fleetingtrails.fleetingjobsbackend.company.entity.CompanyEntity;
import com.fleetingtrails.fleetingjobsbackend.company.mapper.CompanyMapper;
import com.fleetingtrails.fleetingjobsbackend.company.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public CompanyService (
            CompanyRepository repository,
            CompanyMapper mapper
    ) {
        this.companyRepository = repository;
        this.companyMapper = mapper;
    }

    public List<CompanyListItemResponse> getCompanies () {
        List<CompanyEntity> companies = this.companyRepository.findAll();
        List<CompanyListItemResponse> response = new ArrayList<>();

        for (CompanyEntity company : companies) {
            response.add(companyMapper.toCompanyListItemResponse(company));
        }

        return response;
    }
}
