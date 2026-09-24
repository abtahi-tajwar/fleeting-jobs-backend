package com.fleetingtrails.fleetingjobsbackend.company.controller;

import com.fleetingtrails.fleetingjobsbackend.auth.annotation.Authorize;
import com.fleetingtrails.fleetingjobsbackend.common.AppModule;
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

    @Authorize(
            module = AppModule.COMPANY,
            submodule = AppModule.Submodule.DEFAULT,
            action = "LIST"
    )
    @GetMapping("/list")
    public APIListResponse<CompanyListItemResponse> getCompanies () {
        return APIListResponse.success(companyService.getCompanies());
    }

    @Authorize(
            module = AppModule.COMPANY,
            submodule = AppModule.Submodule.DEFAULT,
            action = "READ"
    )
    @GetMapping("/get/{id}")
    public APIPostResponse<CompanyGetDto> getCompany (@PathVariable Long id) {
        return APIPostResponse.success(companyService.getCompanyById(id));
    }

    @Authorize(
            module = AppModule.COMPANY,
            submodule = AppModule.Submodule.DEFAULT,
            action = "CREATE"
    )
    @PostMapping("/create")
    public APIPostResponse<CompanyGetDto> createCompany (@Valid @RequestBody CompanyCreateDto body) {
        return APIPostResponse.success(companyService.createCompany(body));
    }

    @Authorize(
            module = AppModule.COMPANY,
            submodule = AppModule.Submodule.DEFAULT,
            action = "UPDATE"
    )
    @PutMapping("/update/{id}")
    public APIPostResponse<CompanyGetDto> updateCompany (
            @PathVariable Long id,
            @Valid @RequestBody CompanyUpdateDto body
    ) {
        return APIPostResponse.success(companyService.updateCompany(id, body));
    }

    @Authorize(
            module = AppModule.COMPANY,
            submodule = AppModule.Submodule.DEFAULT,
            action = "DELETE"
    )
    @DeleteMapping("/delete/{id}")
    public APIPostResponse<Void> deleteCompany (@PathVariable Long id) {
        companyService.deleteCompany(id);
        return APIPostResponse.success(null);
    }
}
