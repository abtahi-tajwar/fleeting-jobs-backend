package com.fleetingtrails.fleetingjobsbackend.company.controller;

import com.fleetingtrails.fleetingjobsbackend.company.dto.CompanyListItemResponse;
import com.fleetingtrails.fleetingjobsbackend.company.service.CompanyService;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

public class CompanyController {
    private final CompanyService companyService;

    public CompanyController (
            CompanyService service
    ) {
        this.companyService = service;
    }
    @GetMapping
    public List<CompanyListItemResponse> getCompanies () {
        // Needs to change
        return this.companyService.getCompanies();
    }
}
