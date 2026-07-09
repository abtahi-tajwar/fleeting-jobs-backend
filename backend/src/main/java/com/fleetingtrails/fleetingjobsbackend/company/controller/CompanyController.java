package com.fleetingtrails.fleetingjobsbackend.company.controller;

import com.fleetingtrails.fleetingjobsbackend.common.response.APIListResponse;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIPostResponse;
import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyCreateDto;
import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyGetDto;
import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyListItemResponse;
import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.company.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController (
            CompanyService service
    ) {
        this.companyService = service;
    }

    @GetMapping("/list")
    public APIListResponse<CompanyListItemResponse> getCompanies () {
        return APIListResponse.success(companyService.getCompanies());
    }

    @GetMapping("/get/{id}")
    public APIPostResponse<CompanyGetDto> getCompany (@PathVariable Long id) {
        return APIPostResponse.success(companyService.getCompanyById(id));
    }

    @PostMapping("/create")
    public APIPostResponse<CompanyGetDto> createCompany (@Valid @RequestBody CompanyCreateDto body) {
        return APIPostResponse.success(companyService.createCompany(body));
    }

    @PutMapping("/update/{id}")
    public APIPostResponse<CompanyGetDto> updateCompany (
            @PathVariable Long id,
            @Valid @RequestBody CompanyUpdateDto body
    ) {
        return APIPostResponse.success(companyService.updateCompany(id, body));
    }

    @DeleteMapping("/delete/{id}")
    public APIPostResponse<Void> deleteCompany (@PathVariable Long id) {
        companyService.deleteCompany(id);
        return APIPostResponse.success(null);
    }
}
