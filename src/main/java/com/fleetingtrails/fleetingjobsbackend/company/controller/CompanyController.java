package com.fleetingtrails.fleetingjobsbackend.company.controller;

import com.fleetingtrails.fleetingjobsbackend.common.response.APIListResponse;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIPostResponse;
import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyCreateDto;
import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyGetDto;
import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyListItemResponse;
import com.fleetingtrails.fleetingjobsbackend.company.service.CompanyService;
import org.springframework.web.bind.annotation.*;

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
    public APIPostResponse<CompanyGetDto> createCompany (@RequestBody CompanyCreateDto body) {
        System.out.println("Name: " + body.getName());

        System.out.println("ListingUrl: " + body.getListingUrl());

        System.out.println("Template: " + body.getSinglePageUrlTemplate());

        System.out.println("Enabled: " + body.getEnabled());
        return APIPostResponse.success(companyService.createCompany(body));
    }


}
