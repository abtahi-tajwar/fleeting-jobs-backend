package com.fleetingtrails.fleetingjobsbackend.company.service;

import com.fleetingtrails.fleetingjobsbackend.common.exception.ResourceNotFoundException;
import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyCreateDto;
import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyGetDto;
import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyListItemResponse;
import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyUpdateDto;
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

    public CompanyGetDto getCompanyById (Long id) {
        CompanyEntity company = findCompanyOrThrow(id);
        return companyMapper.toCompanyGetDto(company);
    }

    public CompanyGetDto createCompany (CompanyCreateDto dto) {
        CompanyEntity newCompanyEntity = companyMapper.toEntity(dto);
        CompanyEntity res = companyRepository.save(newCompanyEntity);
        return companyMapper.toCompanyGetDto(res);
    }

    public CompanyGetDto updateCompany (Long id, CompanyUpdateDto dto) {
        CompanyEntity company = findCompanyOrThrow(id);
        companyMapper.updateEntityFromDto(dto, company);
        CompanyEntity updated = companyRepository.save(company);
        return companyMapper.toCompanyGetDto(updated);
    }

    public void deleteCompany (Long id) {
        CompanyEntity company = findCompanyOrThrow(id);
        companyRepository.delete(company);
    }

    private CompanyEntity findCompanyOrThrow (Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
    }
}
