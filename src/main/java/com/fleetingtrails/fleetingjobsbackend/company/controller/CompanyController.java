package com.fleetingtrails.fleetingjobsbackend.company.controller;

import com.fleetingtrails.fleetingjobsbackend.common.response.APIListResponse;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIPostResponse;
import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyCreateDto;
import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyGetDto;
import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyListItemResponse;
import com.fleetingtrails.fleetingjobsbackend.company.service.CompanyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    @PostMapping("/create")
    public APIPostResponse<CompanyGetDto> createCompany (CompanyCreateDto body) {
        return APIPostResponse.success(companyService.createCompany(body));
    }


}
